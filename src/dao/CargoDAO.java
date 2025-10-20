package dao;

import database.MysqlDatabase;
import model.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe responsável por realizar operações no banco de dados
 * relacionadas à entidade Cargo.
 */
public class CargoDAO {

    private final MysqlDatabase database;

    // Construtor com injeção de dependência
    public CargoDAO(MysqlDatabase databaseInstance) {
        System.out.println(">>>>  CargoDAO.constructor()");
        this.database = databaseInstance;
    }

    // Cria um novo cargo
    public int create(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.create()");
        String SQL = "INSERT INTO cargo (nomeCargo) VALUES (?);";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, objCargoModel.getNomeCargo());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            stmt.close();
            throw new SQLException("Falha ao inserir cargo");
        }

        ResultSet rs = stmt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        rs.close();
        stmt.close();

        if (id == -1) {
            throw new SQLException("Falha ao obter ID do cargo inserido");
        }
        return id;
    }

    // Deleta um cargo pelo ID
    public boolean delete(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.delete()");
        String SQL = "DELETE FROM cargo WHERE idCargo = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setInt(1, objCargoModel.getIdCargo());

        int affectedRows = stmt.executeUpdate();
        stmt.close();
        return affectedRows > 0;
    }

    // Atualiza um cargo existente
    public boolean update(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.update()");
        String SQL = "UPDATE cargo SET nomeCargo = ? WHERE idCargo = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setString(1, objCargoModel.getNomeCargo());
        stmt.setInt(2, objCargoModel.getIdCargo());

        int affectedRows = stmt.executeUpdate();
        stmt.close();
        return affectedRows > 0;
    }

    // Retorna todos os cargos usando List
    public List<Cargo> findAll() throws SQLException {
        System.out.println(">>>> CargoDAO.findAll()");
        String SQL = "SELECT * FROM cargo;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        List<Cargo> cargos = new ArrayList<>();
        while (rs.next()) {
            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            cargos.add(c);
        }

        rs.close();
        stmt.close();
        return cargos;
    }

    // Busca um cargo pelo ID
    public Cargo findById(int idCargo) throws SQLException {
        List<Cargo> result = findByField("idCargo", idCargo);
        return result.isEmpty() ? null : result.get(0);
    }

    // Busca cargos por um campo específico usando List
    public List<Cargo> findByField(String field, Object value) throws SQLException {
        System.out.println(">>>> CargoDAO.findByField() - Campo: " + field + ", Valor: " + value);

        if (!field.equals("idCargo") && !field.equals("nomeCargo")) {
            throw new SQLException("Campo inválido para busca: " + field);
        }

        String SQL = "SELECT * FROM cargo WHERE " + field + " = ?;";
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        if (value instanceof Integer) {
            stmt.setInt(1, (Integer) value);
        } else if (value instanceof String) {
            stmt.setString(1, (String) value);
        } else {
            stmt.close();
            throw new SQLException("Tipo de valor inválido para busca");
        }

        ResultSet rs = stmt.executeQuery();
        List<Cargo> cargos = new ArrayList<>();
        while (rs.next()) {
            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            cargos.add(c);
        }

        rs.close();
        stmt.close();
        return cargos;
    }
}
