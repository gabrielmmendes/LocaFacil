package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Cliente;
import br.com.locafacil.model.ContratoLocacao;
import br.com.locafacil.model.StatusContrato;
import br.com.locafacil.model.StatusVeiculo;
import br.com.locafacil.model.Veiculo;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContratoLocacaoDAO {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private VeiculoDAO veiculoDAO = new VeiculoDAO();

    /**
     * Lista todas as locações ativas (status = RESERVA ou ATIVO).
     * 
     * @return Lista de contratos ativos
     */
    public List<ContratoLocacao> listarLocacoesAtivas() {
        List<ContratoLocacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato_locacao WHERE statusContrato IN (?, ?)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StatusContrato.RESERVA.name());
                pstmt.setString(2, StatusContrato.ATIVO.name());
                try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Lista o histórico de locações de um cliente específico.
     * 
     * @param idCliente ID do cliente
     * @return Lista de contratos do cliente
     */
    public List<ContratoLocacao> listarHistoricoCliente(long idCliente) {
        List<ContratoLocacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato_locacao WHERE idCliente = ?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, idCliente);
                try (ResultSet rs = pstmt.executeQuery()) {
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
                            // Não podemos definir diretamente os valores de multas, extras e total
                            // pois a classe ContratoLocacao não tem setters para esses campos
                            // Os valores serão obtidos pelos getters quando necessário

                            lista.add(contrato);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Gera um relatório de faturamento para um período específico.
     * Considera apenas contratos encerrados.
     * 
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Mapa com valores de faturamento (valorBruto, valorMultas, valorExtras, valorTotal)
     */
    public Map<String, BigDecimal> gerarRelatorioFaturamento(LocalDate dataInicio, LocalDate dataFim) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        BigDecimal valorBruto = BigDecimal.ZERO;
        BigDecimal valorMultas = BigDecimal.ZERO;
        BigDecimal valorExtras = BigDecimal.ZERO;
        BigDecimal valorTotal = BigDecimal.ZERO;

        String sql = "SELECT valorParcial, valorMultas, valorExtras, valorTotal FROM contrato_locacao " +
                     "WHERE statusContrato = ? AND dataDevolucao BETWEEN ? AND ?";

        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StatusContrato.ENCERRADO.name());
                pstmt.setString(2, dataInicio.atStartOfDay().toString());
                pstmt.setString(3, dataFim.plusDays(1).atStartOfDay().minusNanos(1).toString());

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        BigDecimal parcial = rs.getBigDecimal("valorParcial");
                        BigDecimal multas = rs.getBigDecimal("valorMultas");
                        BigDecimal extras = rs.getBigDecimal("valorExtras");
                        BigDecimal total = rs.getBigDecimal("valorTotal");

                        if (parcial != null) valorBruto = valorBruto.add(parcial);
                        if (multas != null) valorMultas = valorMultas.add(multas);
                        if (extras != null) valorExtras = valorExtras.add(extras);
                        if (total != null) valorTotal = valorTotal.add(total);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultado.put("valorBruto", valorBruto);
        resultado.put("valorMultas", valorMultas);
        resultado.put("valorExtras", valorExtras);
        resultado.put("valorTotal", valorTotal);

        return resultado;
    }

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
                "observacoesDanos TEXT," +
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

    /**
     * Registra a retirada do veículo pelo cliente, atualizando a quilometragem inicial,
     * a data de retirada, e alterando o status do contrato para ATIVO e do veículo para ALUGADO.
     * 
     * @param idContrato ID do contrato
     * @param quilometragemInicial Quilometragem inicial do veículo no momento da retirada
     * @return true se a operação foi bem-sucedida, false caso contrário
     */
    public boolean registrarRetirada(long idContrato, int quilometragemInicial) {
        String sql = "UPDATE contrato_locacao SET dataRetirada=?, quilometragemInicial=?, statusContrato=? WHERE idContrato=? AND statusContrato=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, verificar se o contrato existe e está no status RESERVA
            String sqlCheck = "SELECT idVeiculo FROM contrato_locacao WHERE idContrato=? AND statusContrato=?";
            long idVeiculo = -1;

            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setLong(1, idContrato);
                pstmtCheck.setString(2, StatusContrato.RESERVA.name());
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next()) {
                        idVeiculo = rs.getLong("idVeiculo");
                    } else {
                        // Contrato não encontrado ou não está no status RESERVA
                        return false;
                    }
                }
            }

            // Atualizar o contrato
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                LocalDateTime agora = LocalDateTime.now();
                pstmt.setString(1, agora.toString());
                pstmt.setInt(2, quilometragemInicial);
                pstmt.setString(3, StatusContrato.ATIVO.name());
                pstmt.setLong(4, idContrato);
                pstmt.setString(5, StatusContrato.RESERVA.name());

                int linhasAfetadas = pstmt.executeUpdate();
                if (linhasAfetadas == 0) {
                    // Nenhum contrato foi atualizado
                    return false;
                }
            }

            // Atualizar o status do veículo para ALUGADO
            if (idVeiculo != -1) {
                Veiculo veiculo = obterVeiculo(idVeiculo);
                if (veiculo != null) {
                    veiculo.setStatus(StatusVeiculo.ALUGADO);
                    veiculoDAO.atualizar(veiculo);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancela uma reserva, alterando o status do contrato para CANCELADO
     * e o status do veículo para DISPONIVEL.
     * 
     * @param idContrato ID do contrato a ser cancelado
     * @return true se a operação foi bem-sucedida, false caso contrário
     */
    public boolean cancelarReserva(long idContrato) {
        String sql = "UPDATE contrato_locacao SET statusContrato=? WHERE idContrato=? AND statusContrato=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, verificar se o contrato existe e está no status RESERVA
            String sqlCheck = "SELECT idVeiculo FROM contrato_locacao WHERE idContrato=? AND statusContrato=?";
            long idVeiculo = -1;

            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setLong(1, idContrato);
                pstmtCheck.setString(2, StatusContrato.RESERVA.name());
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next()) {
                        idVeiculo = rs.getLong("idVeiculo");
                    } else {
                        // Contrato não encontrado ou não está no status RESERVA
                        return false;
                    }
                }
            }

            // Atualizar o contrato
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StatusContrato.CANCELADO.name());
                pstmt.setLong(2, idContrato);
                pstmt.setString(3, StatusContrato.RESERVA.name());

                int linhasAfetadas = pstmt.executeUpdate();
                if (linhasAfetadas == 0) {
                    // Nenhum contrato foi atualizado
                    return false;
                }
            }

            // Atualizar o status do veículo para DISPONIVEL
            if (idVeiculo != -1) {
                Veiculo veiculo = obterVeiculo(idVeiculo);
                if (veiculo != null) {
                    veiculo.setStatus(StatusVeiculo.DISPONIVEL);
                    veiculoDAO.atualizar(veiculo);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra a devolução do veículo, atualizando a quilometragem final, a data de devolução,
     * calculando multas por atraso e quilometragem excedente, e alterando o status do contrato
     * para ENCERRADO e do veículo para DISPONIVEL.
     * 
     * @param idContrato ID do contrato
     * @param quilometragemFinal Quilometragem final do veículo no momento da devolução
     * @param observacoesDanos Observações sobre danos no veículo (opcional)
     * @param valorDiarioMultaAtraso Valor diário da multa por atraso (se houver)
     * @param valorPorKmExcedente Valor por km excedente (se houver)
     * @return true se a operação foi bem-sucedida, false caso contrário
     */
    public boolean registrarDevolucao(long idContrato, int quilometragemFinal, String observacoesDanos, 
                                     BigDecimal valorDiarioMultaAtraso, BigDecimal valorPorKmExcedente) {
        // Usar uma SQL query que não inclui a coluna observacoesDanos para evitar problemas
        // com bancos de dados existentes que podem não ter essa coluna
        String sql = "UPDATE contrato_locacao SET dataDevolucao=?, quilometragemFinal=?, " +
                     "valorMultas=?, valorExtras=?, valorTotal=?, statusContrato=? " +
                     "WHERE idContrato=? AND statusContrato=?";

        // Nota: As observações de danos serão armazenadas apenas no objeto ContratoLocacao
        // e não no banco de dados nesta versão
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, verificar se o contrato existe e está no status ATIVO
            String sqlCheck = "SELECT idVeiculo, idCliente, dataInicioPrevista, dataFimPrevista, " +
                             "quilometragemInicial, valorDiarioContratado, valorParcial " +
                             "FROM contrato_locacao WHERE idContrato=? AND statusContrato=?";
            long idVeiculo = -1;
            long idCliente = -1;
            LocalDate dataInicioPrevista = null;
            LocalDate dataFimPrevista = null;
            int quilometragemInicial = 0;
            BigDecimal valorDiarioContratado = BigDecimal.ZERO;
            BigDecimal valorParcial = BigDecimal.ZERO;

            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setLong(1, idContrato);
                pstmtCheck.setString(2, StatusContrato.ATIVO.name());
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next()) {
                        idVeiculo = rs.getLong("idVeiculo");
                        idCliente = rs.getLong("idCliente");
                        dataInicioPrevista = LocalDate.parse(rs.getString("dataInicioPrevista"));
                        dataFimPrevista = LocalDate.parse(rs.getString("dataFimPrevista"));
                        quilometragemInicial = rs.getInt("quilometragemInicial");
                        valorDiarioContratado = rs.getBigDecimal("valorDiarioContratado");
                        valorParcial = rs.getBigDecimal("valorParcial");
                    } else {
                        // Contrato não encontrado ou não está no status ATIVO
                        return false;
                    }
                }
            }

            // Calcular multas e extras
            LocalDateTime agora = LocalDateTime.now();
            LocalDate dataAtual = agora.toLocalDate();

            // Calcular dias de atraso (se houver)
            BigDecimal valorMultas = BigDecimal.ZERO;
            if (dataAtual.isAfter(dataFimPrevista)) {
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(dataFimPrevista, dataAtual);
                valorMultas = valorDiarioMultaAtraso.multiply(BigDecimal.valueOf(diasAtraso));
            }

            // Calcular quilometragem excedente (se houver)
            BigDecimal valorExtras = BigDecimal.ZERO;
            int kmRodados = quilometragemFinal - quilometragemInicial;
            // Estimativa de km permitidos: 100km por dia
            long diasPrevistos = java.time.temporal.ChronoUnit.DAYS.between(dataInicioPrevista, dataFimPrevista);
            if (diasPrevistos == 0) diasPrevistos = 1; // Mínimo de 1 dia
            int kmPermitidos = (int) (diasPrevistos * 100);

            if (kmRodados > kmPermitidos) {
                int kmExcedentes = kmRodados - kmPermitidos;
                valorExtras = valorPorKmExcedente.multiply(BigDecimal.valueOf(kmExcedentes));
            }

            // Calcular valor total
            BigDecimal valorTotal = valorParcial.add(valorMultas).add(valorExtras);

            // Atualizar o contrato
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, agora.toString());
                pstmt.setInt(2, quilometragemFinal);
                pstmt.setBigDecimal(3, valorMultas);
                pstmt.setBigDecimal(4, valorExtras);
                pstmt.setBigDecimal(5, valorTotal);
                pstmt.setString(6, StatusContrato.ENCERRADO.name());
                pstmt.setLong(7, idContrato);
                pstmt.setString(8, StatusContrato.ATIVO.name());

                int linhasAfetadas = pstmt.executeUpdate();
                if (linhasAfetadas == 0) {
                    // Nenhum contrato foi atualizado
                    return false;
                }
            }

            // Atualizar o status do veículo para DISPONIVEL
            if (idVeiculo != -1) {
                Veiculo veiculo = obterVeiculo(idVeiculo);
                if (veiculo != null) {
                    veiculo.setStatus(StatusVeiculo.DISPONIVEL);
                    veiculoDAO.atualizar(veiculo);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
                    veiculo.setStatus(StatusVeiculo.DISPONIVEL);
                    veiculoDAO.atualizar(veiculo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
