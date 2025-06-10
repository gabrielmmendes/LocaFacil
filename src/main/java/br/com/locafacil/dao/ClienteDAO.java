package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS cliente (" +
                "idCliente INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "cpfCnpj TEXT," +
                "telefone TEXT," +
                "email TEXT," +
                "numeroCNH TEXT)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserir(Cliente c) {
        String sql = "INSERT INTO cliente(nome, cpfCnpj, telefone, email, numeroCNH) VALUES(?,?,?,?,?)";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, c.getNome());
                pstmt.setString(2, c.getCpfCnpj());
                pstmt.setString(3, c.getTelefone());
                pstmt.setString(4, c.getEmail());
                pstmt.setString(5, c.getNumeroCNH());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Cliente c = new Cliente(
                            rs.getLong("idCliente"),
                            rs.getString("nome"),
                            rs.getString("cpfCnpj"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getString("numeroCNH")
                    );
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(Cliente c) {
        String sql = "UPDATE cliente SET nome=?, cpfCnpj=?, telefone=?, email=?, numeroCNH=? WHERE idCliente=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, c.getNome());
                pstmt.setString(2, c.getCpfCnpj());
                pstmt.setString(3, c.getTelefone());
                pstmt.setString(4, c.getEmail());
                pstmt.setString(5, c.getNumeroCNH());
                pstmt.setLong(6, c.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(long id) {
        String sql = "DELETE FROM cliente WHERE idCliente=?";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

