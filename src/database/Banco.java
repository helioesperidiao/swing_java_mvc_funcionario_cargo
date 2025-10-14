package database;

import java.sql.*;

public class Banco {
    private static final String nomeBanco = "aula_api_2024";
    private static final String usuarioBanco = "root";
    private static final String usuarioBancoSenha = "";
    private static Connection conexao;

    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                final String stringConexao = "jdbc:mysql://localhost:3306/" + nomeBanco;
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(stringConexao, usuarioBanco, usuarioBancoSenha);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Problema ao carregar o driver
        } catch (SQLException e) {
            e.printStackTrace(); // Problema ao estabelecer a conexão
        }
        return conexao;
    }

    // Método opcional para fechar a conexão, se necessário
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Problema ao fechar a conexão
            }
        }
    }
}
