package dao;

import org.mindrot.jbcrypt.BCrypt;

import database.MysqlDatabase;
import model.Funcionario;
import model.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações no banco de dados
 * relacionadas à entidade Funcionario.
 */
public class FuncionarioDAO {

    private final MysqlDatabase database;

    // Construtor com injeção de dependência
    public FuncionarioDAO(MysqlDatabase databaseInstance) {
        System.out.println(">>>> FuncionarioDAO.constructor()");
        this.database = databaseInstance;
    }

    // =========================
    // CREATE
    // =========================
    public int create(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.create()");

        // Gera hash da senha
        String hashedPassword = BCrypt.hashpw(objFuncionario.getSenha(), BCrypt.gensalt(12));

        String SQL = "INSERT INTO Funcionario (nomeFuncionario, email, senha, recebeValeTransporte, Cargo_idCargo) " +
                "VALUES (?, ?, ?, ?, ?);";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, objFuncionario.getNomeFuncionario());
        stmt.setString(2, objFuncionario.getEmail());
        stmt.setString(3, hashedPassword); // Salva hash, não senha plain
        stmt.setBoolean(4, objFuncionario.isRecebeValeTransporte());
        stmt.setInt(5, objFuncionario.getCargo().getIdCargo());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            stmt.close();
            throw new SQLException("Falha ao inserir funcionario");
        }

        ResultSet rs = stmt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        rs.close();
        stmt.close();

        if (id == -1) {
            throw new SQLException("Falha ao obter ID do funcionario inserido");
        }
        return id;
    }

    // =========================
    // DELETE
    // =========================
    public boolean delete(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.delete()");
        String SQL = "DELETE FROM Funcionario WHERE idFuncionario = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setInt(1, objFuncionario.getIdFuncionario());

        int affectedRows = stmt.executeUpdate();
        stmt.close();
        return affectedRows > 0;
    }

    // =========================
    // UPDATE
    // =========================
    public boolean update(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.update()");
        String SQL = "UPDATE Funcionario SET nomeFuncionario = ?, email = ?, senha = ?, " +
                "recebeValeTransporte = ?, Cargo_idCargo = ? WHERE idFuncionario = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        stmt.setString(1, objFuncionario.getNomeFuncionario());
        stmt.setString(2, objFuncionario.getEmail());
        stmt.setString(3, objFuncionario.getSenha());
        stmt.setBoolean(4, objFuncionario.isRecebeValeTransporte());
        stmt.setInt(5, objFuncionario.getCargo().getIdCargo());
        stmt.setInt(6, objFuncionario.getIdFuncionario());

        int affectedRows = stmt.executeUpdate();
        stmt.close();
        return affectedRows > 0;
    }

    // =========================
    // FIND ALL
    // =========================
    public List<Funcionario> findAll() throws SQLException {
        System.out.println(">>>> FuncionarioDAO.findAll()");
        String SQL = "SELECT f.*, c.idCargo, c.nomeCargo FROM Funcionario f " +
                "JOIN Cargo c ON f.Cargo_idCargo = c.idCargo;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        List<Funcionario> funcionarios = new ArrayList<>();
        while (rs.next()) {
            Funcionario f = new Funcionario();
            f.setIdFuncionario(rs.getInt("idFuncionario"));
            f.setNomeFuncionario(rs.getString("nomeFuncionario"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));

            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            f.setCargo(c);

            funcionarios.add(f);
        }

        rs.close();
        stmt.close();
        return funcionarios;
    }

    // =========================
    // FIND BY ID
    // =========================
    public Funcionario findById(int idFuncionario) throws SQLException {
        List<Funcionario> result = findByField("idFuncionario", idFuncionario);
        return result.isEmpty() ? null : result.get(0);
    }

    // =========================
    // FIND BY FIELD
    // =========================
    public List<Funcionario> findByField(String field, Object value) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.findByField() - Campo: " + field + ", Valor: " + value);

        if (!field.equals("idFuncionario") &&
                !field.equals("nomeFuncionario") &&
                !field.equals("email") &&
                !field.equals("Cargo_idCargo")) {
            throw new SQLException("Campo inválido para busca: " + field);
        }

        String SQL = "SELECT f.*, c.idCargo, c.nomeCargo FROM Funcionario f " +
                "JOIN Cargo c ON f.Cargo_idCargo = c.idCargo " +
                "WHERE f." + field + " = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        if (value instanceof Integer) {
            stmt.setInt(1, (Integer) value);
        } else if (value instanceof String) {
            stmt.setString(1, (String) value);
        } else if (value instanceof Boolean) {
            stmt.setBoolean(1, (Boolean) value);
        } else {
            stmt.close();
            throw new SQLException("Tipo de valor inválido para busca");
        }

        ResultSet rs = stmt.executeQuery();
        List<Funcionario> funcionarios = new ArrayList<>();
        while (rs.next()) {
            Funcionario f = new Funcionario();
            f.setIdFuncionario(rs.getInt("idFuncionario"));
            f.setNomeFuncionario(rs.getString("nomeFuncionario"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));

            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            f.setCargo(c);

            funcionarios.add(f);
        }

        rs.close();
        stmt.close();
        return funcionarios;
    }

}
