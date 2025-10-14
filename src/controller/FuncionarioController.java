package controller;

import model.Funcionario;
import model.dao.FuncionarioDAO;

import java.util.List;

public class FuncionarioController {

    private FuncionarioDAO funcionarioDAO;

    public FuncionarioController() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    // Método para criar um funcionário
    public boolean createController(String nome, String email, String senha, boolean recebeValeTransporte, int cargoId) {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty()) {
            System.out.println("Nome e email são obrigatórios.");
            return false;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(nome);
        funcionario.setEmail(email);
        funcionario.setSenha(senha);
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargoId(cargoId);

        return funcionarioDAO.create(funcionario);
    }

    // Método para buscar um funcionário por ID
    public Funcionario readByIdController(int idFuncionario) {
        if (idFuncionario <= 0) {
            System.out.println("ID inválido.");
            return null;
        }
        return funcionarioDAO.readById(idFuncionario);
    }

    // Método para atualizar um funcionário
    public boolean updateController(int idFuncionario, String nome, String email, String senha, boolean recebeValeTransporte, int cargoId) {
        Funcionario funcionario = funcionarioDAO.readById(idFuncionario);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return false;
        }

        funcionario.setNomeFuncionario(nome);
        funcionario.setEmail(email);
        funcionario.setSenha(senha);
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargoId(cargoId);

        return funcionarioDAO.update(funcionario);
    }

    // Método para deletar um funcionário
    public boolean deleteController(int idFuncionario) {
        return funcionarioDAO.delete(idFuncionario);
    }

    // Método para listar todos os funcionários
    public List<Funcionario> readAllController() {
        return funcionarioDAO.readAll();
    }
}
