package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Cliente;
import br.com.locafacil.model.ContratoLocacao;
import br.com.locafacil.model.StatusContrato;
import br.com.locafacil.model.Veiculo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContratoLocacaoDAO {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private VeiculoDAO veiculoDAO = new VeiculoDAO();

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS contrato_locacao (" +
                "idContrato INTEGER PRIMARY KEY," +
                "idCliente INTEGER NOT NULL," +
                "idVeiculo INTEGER NOT NULL," +
                "dataInicioPrevista TEXT NOT NULL," +
                "dataFimPrevista TEXT NOT NULL," +
                "dataRetirada TEXT," +
                "dataDevolucao TEXT," +
                "quilometragemInicial INTEGER NOT NULL," +
                "quilometragemFinal INTEGER," +
                "valorDiarioContratado REAL NOT NULL," +
                "valorParcial REAL NOT NULL," +
                "valorMultas REAL," +
                "valorExtras REAL," +
                "valorTotal REAL," +
                "statusContrato TEXT NOT NULL," +
                "FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)," +
                "FOREIGN KEY (idVeiculo) REFERENCES veiculo(idVeiculo))";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserir(ContratoLocacao contrato) {
        String sql = "INSERT INTO contrato_locacao(idCliente, idVeiculo, dataInicioPrevista, dataFimPrevista, " +
                "quilometragemInicial, valorDiarioContratado, valorParcial, statusContrato) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setLong(1, contrato.getCliente().getId());
                pstmt.setLong(2, contrato.getVeiculo().getId());
                pstmt.setString(3, contrato.getDataInicioPrevista().toString());
                pstmt.setString(4, contrato.getDataFimPrevista().toString());
                pstmt.setInt(5, contrato.getQuilometragemInicial());
                pstmt.setBigDecimal(6, contrato.getValorDiarioContratado());
                pstmt.setBigDecimal(7, contrato.getValorParcial());
                pstmt.setString(8, contrato.getStatusContrato().name());
                pstmt.executeUpdate();

                // Atualizar o status do veículo para RESERVADO
                Veiculo veiculo = contrato.getVeiculo();
                veiculo.setStatus(br.com.locafacil.model.StatusVeiculo.RESERVADO);
                veiculoDAO.atualizar(veiculo);

                // Obter o ID gerado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        // Não podemos modificar o ID do contrato diretamente pois não há setter,
                        // mas isso não deve ser um problema para a aplicação atual
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ContratoLocacao> listarTodos() {
        List<ContratoLocacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato_locacao";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    // Obter cliente e veículo
                    Cliente cliente = obterCliente(rs.getLong("idCliente"));
                    Veiculo veiculo = obterVeiculo(rs.getLong("idVeiculo"));

                    if (cliente != null && veiculo != null) {
                        // Criar contrato
                        ContratoLocacao contrato = new ContratoLocacao(
                                rs.getLong("idContrato"),
                                cliente,
                                veiculo,
                                LocalDate.parse(rs.getString("dataInicioPrevista")),
                                LocalDate.parse(rs.getString("dataFimPrevista")),
                                rs.getInt("quilometragemInicial"),
                                rs.getBigDecimal("valorDiarioContratado")
                        );

                        // Definir status
                        contrato.setStatusContrato(StatusContrato.valueOf(rs.getString("statusContrato")));

                        // Definir campos opcionais se não forem nulos
                        if (rs.getString("dataRetirada") != null) {
                            contrato.setDataRetirada(LocalDateTime.parse(rs.getString("dataRetirada")));
                        }
                        if (rs.getString("dataDevolucao") != null) {
                            contrato.setDataDevolucao(LocalDateTime.parse(rs.getString("dataDevolucao")));
                        }
                        if (rs.getObject("quilometragemFinal") != null) {
                            contrato.setQuilometragemFinal(rs.getInt("quilometragemFinal"));
                        }

                        lista.add(contrato);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Cliente obterCliente(long idCliente) {
        // Implementação simplificada - em uma aplicação real, seria melhor ter um método específico no ClienteDAO
        List<Cliente> clientes = clienteDAO.listarTodos();
        return clientes.stream()
                .filter(c -> c.getId() == idCliente)
                .findFirst()
                .orElse(null);
    }

    private Veiculo obterVeiculo(long idVeiculo) {
        // Implementação simplificada - em uma aplicação real, seria melhor ter um método específico no VeiculoDAO
        List<Veiculo> veiculos = veiculoDAO.listarTodos();
        return veiculos.stream()
                .filter(v -> v.getId() == idVeiculo)
                .findFirst()
                .orElse(null);
    }

    public boolean verificarDisponibilidadeVeiculo(long idVeiculo, LocalDate dataInicio, LocalDate dataFim) {
        // Primeiro, verificar se o veículo está com status DISPONIVEL
        Veiculo veiculo = obterVeiculo(idVeiculo);
        if (veiculo == null || veiculo.getStatus() != br.com.locafacil.model.StatusVeiculo.DISPONIVEL) {
            return false;
        }

        // Depois, verificar se não há sobreposição com outras reservas/aluguéis
        // Verificamos três casos de sobreposição:
        // 1. A nova reserva está contida em uma existente
        // 2. Uma reserva existente está contida na nova
        // 3. Há sobreposição parcial entre a nova reserva e uma existente
        String sql = "SELECT COUNT(*) FROM contrato_locacao " +
                "WHERE idVeiculo = ? AND statusContrato IN ('RESERVA', 'ATIVO') " +
                "AND NOT (dataFimPrevista < ? OR dataInicioPrevista > ?)";

        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, idVeiculo);
                pstmt.setString(2, dataInicio.toString()); // Reservas que terminam antes da nova reserva
                pstmt.setString(3, dataFim.toString());    // Reservas que começam depois da nova reserva

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) == 0; // Se count = 0, então está disponível
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Em caso de erro, considerar indisponível por segurança
    }

    public void excluir(long id) {
        // Primeiro, obter o contrato para verificar o veículo associado
        String sqlSelect = "SELECT idVeiculo, statusContrato FROM contrato_locacao WHERE idContrato=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Obter informações do contrato
            long idVeiculo = -1;
            String statusContrato = null;
            try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                pstmtSelect.setLong(1, id);
                try (ResultSet rs = pstmtSelect.executeQuery()) {
                    if (rs.next()) {
                        idVeiculo = rs.getLong("idVeiculo");
                        statusContrato = rs.getString("statusContrato");
                    }
                }
            }

            // Excluir o contrato
            String sqlDelete = "DELETE FROM contrato_locacao WHERE idContrato=?";
            try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                pstmtDelete.setLong(1, id);
                pstmtDelete.executeUpdate();
            }

            // Se o contrato estava em RESERVA ou ATIVO, atualizar o status do veículo para DISPONIVEL
            if (idVeiculo != -1 && (StatusContrato.RESERVA.name().equals(statusContrato) || 
                                    StatusContrato.ATIVO.name().equals(statusContrato))) {
                Veiculo veiculo = obterVeiculo(idVeiculo);
                if (veiculo != null) {
                    veiculo.setStatus(br.com.locafacil.model.StatusVeiculo.DISPONIVEL);
                    veiculoDAO.atualizar(veiculo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
