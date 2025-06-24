package br.com.locafacil.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para gerenciar conexões com o banco de dados SQLite.
 * Fornece métodos para estabelecer conexões com o banco de dados do sistema.
 */
public class ConexaoSQLite {

    /**
     * Construtor padrão da classe ConexaoSQLite.
     * Esta classe utiliza apenas métodos estáticos, portanto o construtor não é utilizado diretamente.
     */
    public ConexaoSQLite() {
        // Construtor padrão
    }

    private static final String URL = "jdbc:sqlite:locadora.db";

    /**
     * Estabelece uma conexão com o banco de dados SQLite.
     * 
     * @return Objeto Connection representando a conexão com o banco de dados,
     *         ou null se ocorrer um erro na conexão
     */
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
