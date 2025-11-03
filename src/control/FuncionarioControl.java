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
 * Classe responsável por controlar a lógica de requisição/resposta
 * para a entidade Funcionario.
 *
 * Esta classe faz parte da camada **Controller** no padrão MVC.
 * Seu papel é coordenar as operações:
 * - Receber as requisições (ou chamadas do sistema)
 * - Chamar a camada de **Service** para aplicar regras de negócio
 * - Retornar os resultados para a camada de apresentação (ex: API, console,
 * etc.)
 *
 * Observações didáticas:
 * - Nenhuma regra de negócio é implementada aqui.
 * - Esta classe apenas organiza o fluxo entre Model e Service.
 * - Não depende de nenhum framework web (como Spring ou Jakarta EE).
 *
 * @author
 * @since 2025
 */
public class FuncionarioControl {

    /**
     * Referência para a camada de serviço (Service),
     * responsável pelas regras de negócio do funcionário.
     */
    private final FuncionarioService funcionarioService;

    /**
     * Construtor com injeção de dependência.
     * Recebe um objeto de serviço (FuncionarioService) já instanciado.
     *
     * @param funcionarioService objeto responsável pelas regras de negócio
     */
    public FuncionarioControl(FuncionarioService funcionarioService) {
        System.out.println(">> FuncionarioControl.constructor()");
        this.funcionarioService = funcionarioService;
    }

    /**
     * Cria um novo funcionário no sistema.
     *
     * Fluxo didático:
     * - O Controller recebe os dados (normalmente vindos de uma View ou API).
     * - Ele chama o Service, que valida e salva o funcionário no banco.
     * - O Controller monta o objeto de retorno.
     *
     * @param data Mapa contendo os dados do novo funcionário
     * @return Objeto Funcionario criado
     * @throws SQLException caso ocorra erro no banco de dados
     * @throws Exception    caso ocorra erro de validação ou de negócio
     */
    public Funcionario store(Map<String, Object> data) throws SQLException, Exception {
        System.out.println(">> FuncionarioControl.store()");

        // --- Extração de dados da requisição ---
        // Cada valor é obtido a partir das chaves enviadas (ex: nome, email etc.)
        String nomeFuncionario = (String) data.get("nomeFuncionario");
        String email = (String) data.get("email");
        String senha = (String) data.get("senha");

        // Converte o valor do campo booleano (caso exista)
        boolean recebeValeTransporte = data.get("recebeValeTransporte") != null
                && (Boolean) data.get("recebeValeTransporte");

        // Espera-se que o objeto Cargo já esteja montado (pode vir de outra camada)
        Cargo cargo = (Cargo) data.get("cargo");

        // --- Chamada à camada de serviço ---
        // Aqui o Service faz toda a validação e persistência.
        int novoId = funcionarioService.createFuncionario(
                nomeFuncionario, email, senha, recebeValeTransporte, cargo);

        // --- Montagem do objeto de retorno ---
        Funcionario f = new Funcionario();
        f.setIdFuncionario(novoId);
        f.setNomeFuncionario(nomeFuncionario);
        f.setEmail(email);
        f.setSenha(senha);
        f.setRecebeValeTransporte(recebeValeTransporte);
        f.setCargo(cargo);

        // Retorna o funcionário criado
        return f;
    }

    /**
     * Lista todos os funcionários cadastrados no sistema.
     *
     * @return Lista de objetos Funcionario
     * @throws SQLException caso ocorra erro ao acessar o banco
     */
    public List<Funcionario> index() throws SQLException {
        System.out.println(">> FuncionarioControl.index()");

        // O Controller apenas chama o Service e retorna o resultado.
        return funcionarioService.findAll();
    }

    /**
     * Busca um funcionário específico pelo ID.
     *
     * @param idFuncionario identificador único do funcionário
     * @return Objeto Funcionario correspondente ou null se não encontrado
     * @throws SQLException caso ocorra erro no banco
     */
    public Funcionario show(int idFuncionario) throws SQLException {
        System.out.println(">> FuncionarioControl.show()");

        // Busca o funcionário usando o Service
        return funcionarioService.findById(idFuncionario);
    }

    /**
     * Atualiza os dados de um funcionário existente.
     *
     * Este método segue o mesmo padrão de retorno dos outros controllers:
     * retorna um Map contendo:
     * - success (boolean)
     * - message (mensagem de retorno)
     * - data (dados atualizados)
     *
     * @param idFuncionario        ID do funcionário
     * @param nomeFuncionario      novo nome
     * @param email                novo e-mail
     * @param senha                nova senha
     * @param recebeValeTransporte indica se recebe vale transporte
     * @param cargo                novo cargo associado
     * @return Mapa com o resultado da operação
     */
    public Map<String, Object> update(int idFuncionario, String nomeFuncionario, String email,
            String senha, boolean recebeValeTransporte, Cargo cargo) {
        System.out.println(">> FuncionarioControl.update()");
        Map<String, Object> response = new HashMap<>();

        try {
            // --- Chamada à camada de serviço ---
            boolean atualizou = funcionarioService.updateFuncionario(
                    idFuncionario, nomeFuncionario, email, senha,
                    recebeValeTransporte, cargo);

            // --- Montagem do objeto de retorno (para exibição ou API) ---
            Map<String, Object> funcionarioData = new HashMap<>();
            funcionarioData.put("idFuncionario", idFuncionario);
            funcionarioData.put("nomeFuncionario", nomeFuncionario);
            funcionarioData.put("email", email);
            funcionarioData.put("recebeValeTransporte", recebeValeTransporte);
            funcionarioData.put("cargo", cargo != null ? cargo.getNomeCargo() : null);

            // Lista para manter consistência com respostas anteriores
            List<Map<String, Object>> funcionariosList = new ArrayList<>();
            funcionariosList.add(funcionarioData);

            // Estrutura de dados principal
            Map<String, Object> data = new HashMap<>();
            data.put("funcionarios", funcionariosList);

            // --- Resposta final padronizada ---
            response.put("success", atualizou);
            response.put("message",
                    atualizou ? "Atualizado com sucesso" : "Funcionário não encontrado para atualização");
            response.put("data", data);

        } catch (Exception e) {
            // Em caso de erro, retorna mensagem amigável
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    /**
     * Exclui um funcionário do sistema.
     *
     * Retorna uma estrutura de dados padronizada (Map), com status e mensagem.
     *
     * @param idFuncionario ID do funcionário a ser excluído
     * @return Mapa com status da operação
     */
    public Map<String, Object> destroy(int idFuncionario) {
        System.out.println(">> FuncionarioControl.destroy()");
        Map<String, Object> response = new HashMap<>();

        try {
            // --- Chamada ao serviço ---
            boolean excluiu = funcionarioService.deleteFuncionario(idFuncionario);

            // --- Montagem dos dados do funcionário removido ---
            Map<String, Object> funcionarioData = new HashMap<>();
            funcionarioData.put("idFuncionario", idFuncionario);

            List<Map<String, Object>> funcionariosList = new ArrayList<>();
            funcionariosList.add(funcionarioData);

            Map<String, Object> data = new HashMap<>();
            data.put("funcionarios", funcionariosList);

            // --- Resposta final ---
            response.put("success", excluiu);
            response.put("message", excluiu
                    ? "Excluído com sucesso"
                    : "Funcionário não encontrado para exclusão");
            response.put("data", data);

        } catch (Exception e) {
            // Tratamento de exceções
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    /**
     * Realiza o login de um funcionário.
     *
     * Este método exemplifica bem o papel do Controller:
     * ele coordena a chamada ao Service e organiza o retorno.
     *
     * @param email Email informado pelo usuário
     * @param senha Senha informada pelo usuário
     * @return Mapa contendo sucesso, mensagem e (se válido) os dados do funcionário
     */
    public Map<String, Object> login(String email, String senha) {
        System.out.println(">> FuncionarioControl.login()");
        Map<String, Object> response = new HashMap<>();

        try {
            // --- Chamada à camada de serviço ---
            Funcionario f = funcionarioService.login(email, senha);

            if (f != null) {
                // Login válido
                response.put("success", true);
                response.put("message", "Login realizado com sucesso!");

                // Monta o objeto de dados do funcionário logado
                Map<String, Object> data = new HashMap<>();
                data.put("idFuncionario", f.getIdFuncionario());
                data.put("nomeFuncionario", f.getNomeFuncionario());
                data.put("email", f.getEmail());
                data.put("recebeValeTransporte", f.isRecebeValeTransporte());
                data.put("cargo", f.getCargo().getNomeCargo());

                response.put("data", data);

            } else {
                // Login inválido
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
