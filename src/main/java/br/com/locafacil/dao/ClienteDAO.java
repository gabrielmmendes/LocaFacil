package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela persistência de dados dos clientes.
 * Implementa operações de CRUD (Create, Read, Update, Delete) para a entidade Cliente.
 */
public class ClienteDAO {

    /**
     * Construtor padrão da classe ClienteDAO.
     * Inicializa um novo objeto DAO para operações com clientes.
     */
    public ClienteDAO() {
        // Construtor padrão
    }

    /**
     * Cria a tabela de clientes no banco de dados se ela não existir.
     * Define a estrutura da tabela com todos os campos necessários.
     */
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

    /**
     * Insere um novo cliente no banco de dados.
     * 
     * @param c O objeto Cliente a ser inserido
     */
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

    /**
     * Retorna uma lista com todos os clientes cadastrados no banco de dados.
     * 
     * @return Lista de objetos Cliente
     */
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

    /**
     * Atualiza os dados de um cliente existente no banco de dados.
     * 
     * @param c O objeto Cliente com os dados atualizados
     */
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

    /**
     * Exclui um cliente do banco de dados pelo seu ID.
     * Verifica se o cliente possui contratos ativos antes de excluir.
     * 
     * @param id O ID do cliente a ser excluído
     * @throws Exception Se o cliente possuir contratos ativos ou ocorrer erro na exclusão
     */
    public void excluir(long id) throws Exception {
        // Verificar se o cliente possui contratos ativos antes de excluir
        String sqlCheck = "SELECT COUNT(*) FROM contrato_locacao WHERE idCliente=? AND statusContrato IN ('RESERVA', 'ATIVO')";
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, verificar se o cliente tem contratos ativos
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                pstmtCheck.setLong(1, id);
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Não é possível excluir um cliente que possui contrato de locação ativo.");
                    }
                }
            }

            // Se chegou aqui, pode excluir o cliente
            String sql = "DELETE FROM cliente WHERE idCliente=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}
