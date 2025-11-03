package service;

import java.sql.SQLException;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.FuncionarioDAO;
import model.Funcionario;
import model.Cargo;

/**
 * 👔 Classe: FuncionarioService
 * 
 * 📘 Camada de <b>Serviço</b> responsável pelas regras de negócio da entidade {@link Funcionario}.
 * 
 * 🎯 Objetivos:
 * <ul>
 *   <li>Validar e aplicar regras antes de persistir dados no banco</li>
 *   <li>Centralizar a lógica de negócio, isolando o Controller da camada DAO</li>
 *   <li>Gerenciar operações de criação, atualização, exclusão e autenticação de funcionários</li>
 * </ul>
 * 
 * 🧠 Notas de Aula:
 * <ul>
 *   <li>Camada <b>Service</b> → contém <b>regras de negócio</b></li>
 *   <li>Camada <b>Model</b> → contém <b>regras de domínio</b> (validações de atributos)</li>
 *   <li>Usa <b>injeção de dependência</b> → recebe o DAO via construtor</li>
 *   <li>Camada intermediária entre o Controller e o banco de dados</li>
 * </ul>
 */
public class FuncionarioService {

    // 🔗 Dependência DAO (injeção de dependência)
    private final FuncionarioDAO funcionarioDAO;

    /**
     * 🏗️ Construtor com injeção de dependência.
     * 
     * @param funcionarioDAODependency Instância de {@link FuncionarioDAO}.
     */
    public FuncionarioService(FuncionarioDAO funcionarioDAODependency) {
        System.out.println(">> FuncionarioService.constructor()");
        this.funcionarioDAO = funcionarioDAODependency;
    }

    // ============================================================
    // 🧱 CRUD — Create / Read / Update / Delete
    // ============================================================

    /**
     * ➕ Cria um novo funcionário no sistema.
     * 
     * 🧠 Lógica:
     * <ol>
     *   <li>Valida os atributos via setters do Model (regras de domínio)</li>
     *   <li>Verifica se já existe funcionário com o mesmo email</li>
     *   <li>Criptografa a senha com <b>BCrypt</b></li>
     *   <li>Envia para o DAO persistir no banco</li>
     * </ol>
     * 
     * @param nomeFuncionario Nome completo do funcionário
     * @param email Email corporativo (único)
     * @param senha Senha em texto puro (será criptografada)
     * @param recebeValeTransporte Se recebe vale-transporte
     * @param cargo Cargo associado
     * @return ID do funcionário criado
     * @throws SQLException Erro de banco de dados
     * @throws Exception Se o email já estiver cadastrado
     */
    public int createFuncionario(String nomeFuncionario, String email, String senha,
                                 boolean recebeValeTransporte, Cargo cargo)
            throws SQLException, Exception {

        System.out.println(">>> FuncionarioService.createFuncionario()");

        // 🧩 Criação do objeto de domínio com validações automáticas
        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(nomeFuncionario);
        funcionario.setEmail(email);
        funcionario.setSenha(BCrypt.hashpw(senha, BCrypt.gensalt())); // 🔒 Criptografa a senha
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargo(cargo);

        // 🔍 Verifica duplicidade de email
        List<Funcionario> resultado = funcionarioDAO.findByField("email", email);
        if (!resultado.isEmpty()) {
            throw new Exception("⚠️ Já existe um funcionário com este email: " + email);
        }

        // 💾 Persiste no banco via DAO
        return funcionarioDAO.create(funcionario);
    }

    /**
     * 📋 Retorna todos os funcionários cadastrados.
     * 
     * @return Lista de {@link Funcionario}
     * @throws SQLException Se ocorrer erro de conexão
     */
    public List<Funcionario> findAll() throws SQLException {
        System.out.println(">>> FuncionarioService.findAll()");
        return funcionarioDAO.findAll();
    }

    /**
     * 🔍 Busca um funcionário específico pelo ID.
     * 
     * @param idFuncionario Identificador único do funcionário
     * @return Objeto {@link Funcionario} ou null se não encontrado
     * @throws SQLException Se houver falha na consulta
     */
    public Funcionario findById(int idFuncionario) throws SQLException {
        System.out.println(">>> FuncionarioService.findById()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario); // 🔒 Validação de domínio

        return funcionarioDAO.findById(funcionario.getIdFuncionario());
    }

    /**
     * ✏️ Atualiza os dados de um funcionário existente.
     * 
     * 🧠 Lógica:
     * <ul>
     *   <li>Valida dados via model</li>
     *   <li>Criptografa senha nova (se informada)</li>
     *   <li>Chama o DAO para persistir as alterações</li>
     * </ul>
     * 
     * @param idFuncionario ID do funcionário
     * @param nomeFuncionario Novo nome
     * @param email Novo email
     * @param senha Nova senha
     * @param recebeValeTransporte Novo valor para vale-transporte
     * @param cargo Novo cargo
     * @return true se atualizado com sucesso
     * @throws SQLException Erro de banco de dados
     */
    public boolean updateFuncionario(int idFuncionario, String nomeFuncionario, String email, String senha,
                                     boolean recebeValeTransporte, Cargo cargo)
            throws SQLException {
        System.out.println(">>> FuncionarioService.updateFuncionario()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario);
        funcionario.setNomeFuncionario(nomeFuncionario);
        funcionario.setEmail(email);

        // 🔐 Atualiza a senha (criptografada)
        funcionario.setSenha(BCrypt.hashpw(senha, BCrypt.gensalt()));
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargo(cargo);

        return funcionarioDAO.update(funcionario);
    }

    /**
     * ❌ Exclui um funcionário com base no ID.
     * 
     * @param idFuncionario Identificador do funcionário
     * @return true se excluído com sucesso
     * @throws SQLException Se ocorrer erro de banco
     */
    public boolean deleteFuncionario(int idFuncionario) throws SQLException {
        System.out.println(">>> FuncionarioService.deleteFuncionario()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario);

        return funcionarioDAO.delete(funcionario);
    }

    // ============================================================
    // 🔎 Consultas específicas
    // ============================================================

    /**
     * 🔎 Busca todos os funcionários que possuem determinado cargo.
     * 
     * @param cargo Objeto {@link Cargo}
     * @return Lista de funcionários que possuem o cargo informado
     * @throws SQLException Erro ao consultar o banco
     */
    public List<Funcionario> findByCargo(Cargo cargo) throws SQLException {
        System.out.println(">>> FuncionarioService.findByCargo()");
        return funcionarioDAO.findByField("Cargo_idCargo", cargo.getIdCargo());
    }

    /**
     * 📧 Busca um funcionário pelo email.
     * 
     * @param email Email do funcionário
     * @return {@link Funcionario} encontrado, ou null se não existir
     * @throws SQLException Erro de conexão ou consulta
     */
    public Funcionario findByEmail(String email) throws SQLException {
        System.out.println(">>> FuncionarioService.findByEmail()");
        List<Funcionario> result = funcionarioDAO.findByField("email", email);
        return result.isEmpty() ? null : result.get(0);
    }

    // ============================================================
    // 🔐 Autenticação (Login)
    // ============================================================

    /**
     * 🔐 Realiza a autenticação de um funcionário.
     * 
     * 🧠 Lógica:
     * <ol>
     *   <li>Busca o funcionário pelo email</li>
     *   <li>Verifica a senha informada com o hash armazenado via <b>BCrypt</b></li>
     *   <li>Retorna o funcionário autenticado ou null se inválido</li>
     * </ol>
     * 
     * @param email Email do funcionário
     * @param senha Senha digitada (texto puro)
     * @return Funcionário autenticado ou null se falha
     * @throws SQLException Se houver erro de banco
     */
    public Funcionario login(String email, String senha) throws SQLException {
        System.out.println(">>> FuncionarioService.login()");

        // 🔎 Busca funcionário pelo email
        List<Funcionario> lista = funcionarioDAO.findByField("email", email);

        if (lista == null || lista.isEmpty()) {
            return null; // ❌ Email não encontrado
        }

        Funcionario f = lista.get(0);
        System.out.println("🔐 Verificando senha criptografada...");

        // ✅ Verifica senha com BCrypt
        if (BCrypt.checkpw(senha, f.getSenha())) {
            return f; // Login bem-sucedido 🎉
        } else {
            return null; // Senha incorreta ⚠️
        }
    }
}
