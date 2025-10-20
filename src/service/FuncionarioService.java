package service;

import java.sql.SQLException;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.FuncionarioDAO;
import model.Funcionario;
import model.Cargo;

/**
 * Classe responsável pela camada de serviço para a entidade Funcionario.
 * 
 * Observações sobre injeção de dependência:
 * - O FuncionarioService recebe uma instância de FuncionarioDAO via construtor.
 * - Isso desacopla o serviço do DAO concreto, facilitando testes unitários e
 * mocks.
 */
public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    // Construtor com injeção de dependência
    public FuncionarioService(FuncionarioDAO funcionarioDAODependency) {
        System.out.println(">> FuncionarioService.constructor()");
        this.funcionarioDAO = funcionarioDAODependency;
    }

    /**
     * Cria um novo funcionário
     * 
     * @param nomeFuncionario      Nome do funcionário
     * @param email                Email do funcionário
     * @param senha                Senha do funcionário
     * @param recebeValeTransporte Se recebe vale-transporte
     * @param cargo                Cargo associado
     * @return ID do funcionário criado
     * @throws SQLException
     * @throws Exception    Caso já exista funcionário com o mesmo email
     */
    public int createFuncionario(String nomeFuncionario, String email, String senha, boolean recebeValeTransporte,
            Cargo cargo)
            throws SQLException, Exception {

        System.out.println(">>> FuncionarioService.createFuncionario()");

        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(nomeFuncionario);
        funcionario.setEmail(email);
        funcionario.setSenha(senha);
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargo(cargo);

        // Verifica se já existe funcionário com o mesmo email
        List<Funcionario> resultado = funcionarioDAO.findByField("email", email);
        if (!resultado.isEmpty()) {
            throw new Exception("Já existe um funcionário com este email: " + email);
        }

        return funcionarioDAO.create(funcionario);
    }

    /**
     * Retorna todos os funcionários
     * 
     * @return Lista de funcionários
     * @throws SQLException
     */
    public List<Funcionario> findAll() throws SQLException {
        System.out.println(">>> FuncionarioService.findAll()");
        return funcionarioDAO.findAll();
    }

    /**
     * Retorna um funcionário por ID
     * 
     * @param idFuncionario ID do funcionário
     * @return Funcionario ou null se não encontrado
     * @throws SQLException
     */
    public Funcionario findById(int idFuncionario) throws SQLException {
        System.out.println(">>> FuncionarioService.findById()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario); // validação simples

        return funcionarioDAO.findById(funcionario.getIdFuncionario());
    }

    /**
     * Atualiza um funcionário existente
     * 
     * @param idFuncionario        ID do funcionário
     * @param nomeFuncionario      Novo nome
     * @param email                Novo email
     * @param senha                Nova senha
     * @param recebeValeTransporte Novo valor para vale-transporte
     * @param cargo                Novo cargo
     * @return true se atualizado com sucesso
     * @throws SQLException
     */
    public boolean updateFuncionario(int idFuncionario, String nomeFuncionario, String email, String senha,
            boolean recebeValeTransporte, Cargo cargo) throws SQLException {
        System.out.println(">>> FuncionarioService.updateFuncionario()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario);
        funcionario.setNomeFuncionario(nomeFuncionario);
        funcionario.setEmail(email);
        funcionario.setSenha(senha);
        funcionario.setRecebeValeTransporte(recebeValeTransporte);
        funcionario.setCargo(cargo);

        return funcionarioDAO.update(funcionario);
    }

    /**
     * Deleta um funcionário por ID
     * 
     * @param idFuncionario ID do funcionário
     * @return true se excluído com sucesso
     * @throws SQLException
     */
    public boolean deleteFuncionario(int idFuncionario) throws SQLException {
        System.out.println(">>> FuncionarioService.deleteFuncionario()");

        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(idFuncionario);

        return funcionarioDAO.delete(funcionario);
    }

    /**
     * Busca funcionários por cargo
     * 
     * @param cargo Cargo desejado
     * @return Lista de funcionários que possuem o cargo especificado
     * @throws SQLException
     */
    public List<Funcionario> findByCargo(Cargo cargo) throws SQLException {
        System.out.println(">>> FuncionarioService.findByCargo()");
        return funcionarioDAO.findByField("Cargo_idCargo", cargo.getIdCargo());
    }

    /**
     * Busca funcionário por email
     * 
     * @param email Email do funcionário
     * @return Funcionario ou null se não encontrado
     * @throws SQLException
     */
    public Funcionario findByEmail(String email) throws SQLException {
        System.out.println(">>> FuncionarioService.findByEmail()");
        List<Funcionario> result = funcionarioDAO.findByField("email", email);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Realiza o login de um funcionário.
     * 
     * @param email Email do usuário
     * @param senha Senha digitada pelo usuário
     * @return Funcionario logado se sucesso, ou null se login inválido
     * @throws SQLException Erro de banco de dados
     */
    /**
     * Faz login de um funcionário
     * 
     * @param email Email do usuário
     * @param senha Senha digitada
     * @return Funcionario se login correto, null se inválido
     * @throws SQLException Erro de acesso ao banco
     */
    public Funcionario login(String email, String senha) throws SQLException {
        // Busca funcionário pelo email
        List<Funcionario> lista = funcionarioDAO.findByField("email", email);

        if (lista == null || lista.isEmpty()) {
            return null; // email não encontrado
        }

        Funcionario f = lista.get(0);
        System.out.println(f.getSenha());
        // Verifica senha usando BCrypt
        if (BCrypt.checkpw(senha, f.getSenha())) {
            return f; // login bem-sucedido
        } else {
            return null; // senha incorreta
        }
    }
}
