package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Veiculo;
import br.com.locafacil.model.StatusVeiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

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

    public void excluir(long id) throws Exception {
        // Verificar se o veículo está alugado ou reservado antes de excluir
        String sqlCheck = "SELECT status FROM veiculo WHERE idVeiculo=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, verificar o status do veículo
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

            // Se chegou aqui, pode excluir o veículo
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
