package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.Banco;
import model.Funcionario;

public class FuncionarioDAO {

    // Método para criar um novo funcionário no banco de dados
    public boolean create(Funcionario funcionario) {
        String sql = "INSERT INTO Funcionario (nomeFuncionario, email, senha, recebeValeTransporte, Cargo_idCargo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Banco.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getSenha());
            stmt.setBoolean(4, funcionario.isRecebeValeTransporte());
            stmt.setInt(5, funcionario.getCargo().getIdCargo());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar um funcionário pelo ID
    public Funcionario readById(int idFuncionario) {
        String sql = "SELECT * FROM Funcionario WHERE idFuncionario = ?";
        try (Connection conn = Banco.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
                funcionario.setNomeFuncionario(rs.getString("nomeFuncionario"));
                funcionario.setEmail(rs.getString("email"));
                funcionario.setSenha(rs.getString("senha"));
                funcionario.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));
                funcionario.getCargo().setIdCargo((rs.getInt("Cargo_idCargo")));
                return funcionario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para atualizar um funcionário existente
    public boolean update(Funcionario funcionario) {
        String sql = "UPDATE Funcionario SET nomeFuncionario = ?, email = ?, senha = ?, recebeValeTransporte = ?, Cargo_idCargo = ? WHERE idFuncionario = ?";
        try (Connection conn = Banco.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNomeFuncionario());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, funcionario.getSenha());
            stmt.setBoolean(4, funcionario.isRecebeValeTransporte());
            stmt.setInt(5, funcionario.getCargo().getIdCargo());
            stmt.setInt(6, funcionario.getIdFuncionario());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para deletar um funcionário
    public boolean delete(int idFuncionario) {
        String sql = "DELETE FROM Funcionario WHERE idFuncionario = ?";
        try (Connection conn = Banco.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar todos os funcionários
    public List<Funcionario> readAll() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";
        try (Connection conn = Banco.getConexao();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
                funcionario.setNomeFuncionario(rs.getString("nomeFuncionario"));
                funcionario.setEmail(rs.getString("email"));
                funcionario.setSenha(rs.getString("senha"));
                funcionario.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));
                funcionario.getCargo().setIdCargo(rs.getInt("Cargo_idCargo"));
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }
}
