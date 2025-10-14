package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.Banco;
import model.Cargo;

public class CargoDAO {
    // Método para criar um novo cargo no banco de dados
    public boolean create(Cargo cargo) {
        String sql = "INSERT INTO Cargo (nomeCargo) VALUES (?)";
        try (Connection conn = Banco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cargo.getNomeCargo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar um cargo pelo ID
    public Cargo readById(int idCargo) {
        String sql = "SELECT * FROM Cargo WHERE idCargo = ?";
        try (Connection conn = Banco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCargo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setIdCargo(rs.getInt("idCargo"));
                cargo.setNomeCargo(rs.getString("nomeCargo"));
                return cargo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para atualizar um cargo existente
    public boolean update(Cargo cargo) {
        String sql = "UPDATE Cargo SET nomeCargo = ? WHERE idCargo = ?";
        try (Connection conn = Banco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cargo.getNomeCargo());
            stmt.setInt(2, cargo.getIdCargo());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para deletar um cargo
    public boolean delete(int idCargo) {
        String sql = "DELETE FROM Cargo WHERE idCargo = ?";
        try (Connection conn = Banco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCargo);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar todos os cargos
    public List<Cargo> readAll() {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT * FROM Cargo";
        try (Connection conn = Banco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setIdCargo(rs.getInt("idCargo"));
                cargo.setNomeCargo(rs.getString("nomeCargo"));
                cargos.add(cargo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cargos;
    }
}
