package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Funcionario;
import model.Cargo;
import service.FuncionarioService;

/**
 * Classe responsável por controlar a lógica de requisição/resposta para a
 * entidade Funcionario.
 * Sem dependência de framework web.
 */
public class FuncionarioControl {

    private final FuncionarioService funcionarioService;

    // Construtor com injeção de dependência
    public FuncionarioControl(FuncionarioService funcionarioService) {
        System.out.println(">> FuncionarioControl.constructor()");
        this.funcionarioService = funcionarioService;
    }

    // Cria um novo funcionário
    public Funcionario store(Map<String, Object> data) throws SQLException, Exception {
        System.out.println(">> FuncionarioControl.store()");

        String nomeFuncionario = (String) data.get("nomeFuncionario");
        String email = (String) data.get("email");
        String senha = (String) data.get("senha");
        boolean recebeValeTransporte = data.get("recebeValeTransporte") != null
                && (Boolean) data.get("recebeValeTransporte");
        Cargo cargo = (Cargo) data.get("cargo"); // espera um objeto Cargo válido

        int novoId = funcionarioService.createFuncionario(nomeFuncionario, email, senha, recebeValeTransporte, cargo);

        Funcionario f = new Funcionario();
        f.setIdFuncionario(novoId);
        f.setNomeFuncionario(nomeFuncionario);
        f.setEmail(email);
        f.setSenha(senha);
        f.setRecebeValeTransporte(recebeValeTransporte);
        f.setCargo(cargo);

        return f;
    }

    // Lista todos os funcionários
    public List<Funcionario> index() throws SQLException {
        System.out.println(">> FuncionarioControl.index()");
        return funcionarioService.findAll();
    }

    // Busca um funcionário pelo ID
    public Funcionario show(int idFuncionario) throws SQLException {
        System.out.println(">> FuncionarioControl.show()");
        return funcionarioService.findById(idFuncionario);
    }

    // Atualiza um funcionário existente
    public Map<String, Object> update(int idFuncionario, String nomeFuncionario, String email,
            String senha, boolean recebeValeTransporte, Cargo cargo) {
        System.out.println(">> FuncionarioControl.update()");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean atualizou = funcionarioService.updateFuncionario(idFuncionario, nomeFuncionario, email, senha,
                    recebeValeTransporte, cargo);

            // Monta o objeto do funcionário atualizado
            Map<String, Object> funcionarioData = new HashMap<>();
            funcionarioData.put("idFuncionario", idFuncionario);
            funcionarioData.put("nomeFuncionario", nomeFuncionario);
            funcionarioData.put("email", email);
            funcionarioData.put("recebeValeTransporte", recebeValeTransporte);
            funcionarioData.put("cargo", cargo != null ? cargo.getNomeCargo() : null);

            // Coloca em uma lista
            List<Map<String, Object>> funcionariosList = new ArrayList<>();
            funcionariosList.add(funcionarioData);

            // Monta o objeto de dados
            Map<String, Object> data = new HashMap<>();
            data.put("funcionarios", funcionariosList);

            // Monta a resposta final
            response.put("success", atualizou);
            response.put("message",
                    atualizou ? "Atualizado com sucesso" : "Funcionário não encontrado para atualização");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // Deleta um funcionário
    public Map<String, Object> destroy(int idFuncionario) {
        System.out.println(">> FuncionarioControl.destroy()");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean excluiu = funcionarioService.deleteFuncionario(idFuncionario);

            // Monta o objeto do funcionário excluído
            Map<String, Object> funcionarioData = new HashMap<>();
            funcionarioData.put("idFuncionario", idFuncionario);

            // Coloca em uma lista
            List<Map<String, Object>> funcionariosList = new ArrayList<>();
            funcionariosList.add(funcionarioData);

            // Monta o objeto de dados
            Map<String, Object> data = new HashMap<>();
            data.put("funcionarios", funcionariosList);

            // Monta a resposta final
            response.put("success", excluiu);
            response.put("message", excluiu ? "Excluído com sucesso" : "Funcionário não encontrado para exclusão");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * Realiza o login de um funcionário.
     *
     * @param email Email do usuário
     * @param senha Senha digitada pelo usuário
     * @return Map<String, Object> contendo sucesso, mensagem e dados do funcionário
     */
    public Map<String, Object> login(String email, String senha) {
        Map<String, Object> response = new HashMap<>();
        try {
            Funcionario f = funcionarioService.login(email, senha);
            if (f != null) {
                response.put("success", true);
                response.put("message", "Login realizado com sucesso!");

                // Dados do funcionário
                Map<String, Object> data = new HashMap<>();
                data.put("idFuncionario", f.getIdFuncionario());
                data.put("nomeFuncionario", f.getNomeFuncionario());
                data.put("email", f.getEmail());
                data.put("recebeValeTransporte", f.isRecebeValeTransporte());
                data.put("cargo", f.getCargo().getNomeCargo());

                response.put("data", data);
            } else {
                response.put("success", false);
                response.put("message", "Email ou senha inválidos.");
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro inesperado: " + e.getMessage());
        }

        return response;
    }
}
