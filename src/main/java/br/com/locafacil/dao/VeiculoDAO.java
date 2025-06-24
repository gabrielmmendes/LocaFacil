package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Veiculo;
import br.com.locafacil.model.StatusVeiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações de persistência (CRUD)
 * relacionadas aos veículos no banco de dados SQLite.
 */
public class VeiculoDAO {

    /**
     * Construtor padrão da classe VeiculoDAO.
     * Inicializa um novo objeto DAO para operações com veículos.
     */
    public VeiculoDAO() {
        // Construtor padrão
    }

    /**
     * Retorna uma lista de veículos com status DISPONIVEL.
     *
     * @return Lista de veículos disponíveis
     */
    public List<Veiculo> listarVeiculosDisponiveis() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM veiculo WHERE status = ?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StatusVeiculo.DISPONIVEL.name());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Veiculo v = new Veiculo(
                                rs.getLong("idVeiculo"),
                                rs.getString("placa"),
                                rs.getString("modelo"),
                                rs.getString("categoria"),
                                rs.getString("cor"),
                                rs.getInt("anoFabricacao"),
                                rs.getBigDecimal("valorDiaria")
                        );
                        v.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
                        lista.add(v);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Retorna uma lista de veículos com status diferente de DISPONIVEL.
     *
     * @return Lista de veículos não disponíveis (alugados, reservados, etc.)
     */
    public List<Veiculo> listarVeiculosNaoDisponiveis() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM veiculo WHERE status != ?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, StatusVeiculo.DISPONIVEL.name());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Veiculo v = new Veiculo(
                                rs.getLong("idVeiculo"),
                                rs.getString("placa"),
                                rs.getString("modelo"),
                                rs.getString("categoria"),
                                rs.getString("cor"),
                                rs.getInt("anoFabricacao"),
                                rs.getBigDecimal("valorDiaria")
                        );
                        v.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
                        lista.add(v);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Cria a tabela "veiculo" no banco de dados, caso ela ainda não exista.
     */
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS veiculo (" +
                "idVeiculo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "placa TEXT NOT NULL," +
                "modelo TEXT NOT NULL," +
                "categoria TEXT," +
                "cor TEXT," +
                "anoFabricacao INTEGER," +
                "valorDiaria REAL," +
                "status TEXT)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insere um novo veículo no banco de dados.
     *
     * @param v Objeto Veiculo a ser inserido
     */
    public void inserir(Veiculo v) {
        String sql = "INSERT INTO veiculo(placa, modelo, categoria, cor, anoFabricacao, valorDiaria, status) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, v.getPlaca());
                pstmt.setString(2, v.getModelo());
                pstmt.setString(3, v.getCategoria());
                pstmt.setString(4, v.getCor());
                pstmt.setInt(5, v.getAnoFabricacao());
                pstmt.setBigDecimal(6, v.getValorDiaria());
                pstmt.setString(7, v.getStatus().name());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna uma lista com todos os veículos cadastrados no banco.
     *
     * @return Lista completa de veículos
     */
    public List<Veiculo> listarTodos() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM veiculo";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Veiculo v = new Veiculo(
                            rs.getLong("idVeiculo"),
                            rs.getString("placa"),
                            rs.getString("modelo"),
                            rs.getString("categoria"),
                            rs.getString("cor"),
                            rs.getInt("anoFabricacao"),
                            rs.getBigDecimal("valorDiaria")
                    );
                    v.setStatus(StatusVeiculo.valueOf(rs.getString("status")));
                    lista.add(v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Atualiza os dados de um veículo existente no banco.
     *
     * @param v Objeto Veiculo com os dados atualizados
     */
    public void atualizar(Veiculo v) {
        String sql = "UPDATE veiculo SET placa=?, modelo=?, categoria=?, cor=?, anoFabricacao=?, valorDiaria=?, status=? WHERE idVeiculo=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, v.getPlaca());
                pstmt.setString(2, v.getModelo());
                pstmt.setString(3, v.getCategoria());
                pstmt.setString(4, v.getCor());
                pstmt.setInt(5, v.getAnoFabricacao());
                pstmt.setBigDecimal(6, v.getValorDiaria());
                pstmt.setString(7, v.getStatus().name());
                pstmt.setLong(8, v.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exclui um veículo do banco de dados, desde que ele não esteja
     * com status ALUGADO ou RESERVADO.
     *
     * @param id ID do veículo a ser excluído
     * @throws Exception Se o veículo estiver alugado ou reservado, ou em caso de erro de banco
     */
    public void excluir(long id) throws Exception {
        String sqlCheck = "SELECT status FROM veiculo WHERE idVeiculo=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Verifica status do veículo antes de excluir
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setLong(1, id);
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");
                        if (status.equals(StatusVeiculo.ALUGADO.name()) ||
                                status.equals(StatusVeiculo.RESERVADO.name())) {
                            throw new Exception("Não é possível excluir um veículo que está alugado ou reservado.");
                        }
                    }
                }
            }

            // Se permitido, exclui o veículo
            String sql = "DELETE FROM veiculo WHERE idVeiculo=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao excluir veículo: " + e.getMessage());
        }
    }
}
