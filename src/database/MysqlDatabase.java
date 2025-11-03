package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 🧩 Classe responsável por **gerenciar a conexão com o banco de dados MySQL**.
 *
 * 🔹 Esta classe utiliza o **padrão Singleton**, garantindo que exista apenas
 * uma instância de conexão ativa durante a execução da aplicação.
 *
 * ⚙️ Responsabilidades:
 * - Criar e manter uma única conexão JDBC.
 * - Reabrir a conexão automaticamente se ela for perdida.
 * - Centralizar a configuração do banco de dados.
 *
 * 💡 Essa classe é usada pelas classes DAO para obter conexões seguras e
 * reaproveitáveis.
 */
public class MysqlDatabase {

    // ==============================
    // 🔁 SINGLETON (instância única)
    // ==============================

    /** Instância única da classe (Singleton) */
    private static MysqlDatabase instance;

    /** Conexão ativa com o banco MySQL */
    private Connection connection;

    // ==============================
    // ⚙️ Configurações do banco
    // ==============================

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    // ==============================
    // 🏗️ CONSTRUTOR 
    // ==============================
    /**
     * Construtor
     * 
     * Ele define as configurações de conexão e é usado apenas dentro do Singleton.
     *
     * @param host     endereço do servidor MySQL
     * @param user     usuário de acesso
     * @param password senha do banco
     * @param database nome do banco de dados
     * @param port     porta de conexão (padrão: 3306)
     */
    public MysqlDatabase(String host, String user, String password, String database, int port) {
        System.out.println("⚙️ Criando instância de MysqlDatabase...");
        this.host = host != null ? host : "127.0.0.1";
        this.user = user != null ? user : "root";
        this.password = password != null ? password : "";
        this.database = database != null ? database : "gestao_rh";
        this.port = port > 0 ? port : 3306;
    }

    // ==============================
    // 🔑 MÉTODO DE ACESSO AO SINGLETON
    // ==============================
    /**
     * Retorna a instância única (singleton) de {@link MysqlDatabase}.
     *
     * 🧠 Se ainda não existir uma instância, ela será criada e a conexão será
     * aberta.
     *
     * @param host     endereço do servidor MySQL
     * @param user     usuário de acesso
     * @param password senha de acesso
     * @param database nome do banco
     * @param port     porta de conexão
     * @return instância única de MysqlDatabase
     * @throws SQLException caso ocorra falha na conexão
     */
    public static MysqlDatabase getInstance(String host, String user, String password, String database, int port)
            throws SQLException {

        // Cria a instância se ainda não existir
        if (instance == null) {
            instance = new MysqlDatabase(host, user, password, database, port);
            instance.connect(); // 🔌 Estabelece a conexão
        }
        return instance;
    }

    // ==============================
    // 🔌 CONECTAR AO BANCO
    // ==============================
    /**
     * Estabelece a conexão com o banco MySQL usando o JDBC.
     *
     * ⚙️ Passos:
     * 1️⃣ Monta a URL de conexão.
     * 2️⃣ Usa o DriverManager para se conectar.
     * 3️⃣ Exibe logs de sucesso ou erro.
     *
     * @throws SQLException caso a conexão falhe
     */
    private void connect() throws SQLException {
        // Verifica se a conexão ainda não existe ou foi encerrada
        if (this.connection == null || this.connection.isClosed()) {
            try {
                // 🔗 Monta a URL JDBC de conexão
                String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                        + "?useSSL=false&serverTimezone=UTC";

                // 💾 Estabelece a conexão
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Conectado ao MySQL com sucesso!");

            } catch (SQLException e) {
                System.err.println("❌ Falha ao conectar ao MySQL: " + e.getMessage());
                throw e; // Relança o erro para tratamento externo
            }
        }
    }

    // ==============================
    // 🔄 OBTÉM CONEXÃO ATIVA
    // ==============================
    /**
     * Retorna a conexão ativa com o banco de dados.
     *
     * 🧠 Caso a conexão tenha sido fechada, ela será automaticamente reaberta.
     *
     * @return objeto {@link Connection} ativo.
     * @throws SQLException se ocorrer erro ao reconectar.
     */
    public Connection getConnection() throws SQLException {
        // Se a conexão estiver fechada, reconecta automaticamente
        if (this.connection == null || this.connection.isClosed()) {
            System.out.println("⚠️ Conexão perdida. Tentando reconectar...");
            connect();
        }
        return this.connection;
    }

}
