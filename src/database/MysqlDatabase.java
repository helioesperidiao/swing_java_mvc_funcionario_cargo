package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco MySQL.
 *
 * - Singleton para manter uma única conexão ativa.
 * - Configuração via construtor.
 */
public class MysqlDatabase {

    // Instância singleton
    private static MysqlDatabase instance;

    // Conexão única
    private Connection connection;

    // Configurações do banco
    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    /**
     * Construtor privado para singleton.
     */
    public MysqlDatabase(String host, String user, String password, String database, int port) {
        this.host = host != null ? host : "127.0.0.1";
        this.user = user != null ? user : "root";
        this.password = password != null ? password : "";
        this.database = database != null ? database : "gestao_rh";
        this.port = port > 0 ? port : 3306;
    }

    /**
     * Retorna a instância singleton.
     */
    public static MysqlDatabase getInstance(String host, String user, String password, String database, int port)
            throws SQLException {
        if (instance == null) {
            instance = new MysqlDatabase(host, user, password, database, port);
            instance.connect();
        }
        return instance;
    }

    /**
     * Conecta ao banco MySQL.
     */
    private void connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println(">  Conectado ao MySQL com sucesso!");
            } catch (SQLException e) {
                System.err.println("> Falha ao conectar ao MySQL: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Retorna a conexão ativa.
     */
    public Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            connect();
        }
        return this.connection;
    }
}
