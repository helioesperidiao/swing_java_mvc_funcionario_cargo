package service;

import java.sql.SQLException;
import java.util.List;
import dao.CargoDAO;
import model.Cargo;

/**
 * ⚙️ Classe: CargoService
 * 
 * 📘 Responsável pela camada de <b>Serviço</b> da entidade {@link Cargo}.
 * 
 * 🎯 Objetivos:
 * <ul>
 *   <li>Aplicar regras de negócio e validações antes de acessar o banco de dados</li>
 *   <li>Intermediar a comunicação entre a camada <b>Controller</b> e a camada <b>DAO</b></li>
 *   <li>Evitar que a Controller lide diretamente com SQL</li>
 * </ul>
 * 
 * 🧠 Notas de Aula:
 * <ul>
 *   <li>Camada Service = <b>Regras de Negócio</b></li>
 *   <li>Camada Model = <b>Regras de Domínio</b> (validação de atributos)</li>
 *   <li>Usa <b>injeção de dependência</b> (DAO é recebido via construtor)</li>
 *   <li>Facilita <b>testes unitários</b> e <b>substituição por mocks</b></li>
 * </ul>
 */
public class CargoService {

    // 🔗 Dependência para acesso ao banco (DAO)
    private final CargoDAO cargoDAO;

    /**
     * 🏗️ Construtor com Injeção de Dependência
     * 
     * 💡 Permite trocar a implementação do DAO sem alterar a lógica do serviço.
     */
    public CargoService(CargoDAO cargoDAODependency) {
        System.out.println(">> CargoService.constructor()");
        this.cargoDAO = cargoDAODependency;
    }

    // =====================================================
    // 📦 CRUD - Create / Read / Update / Delete
    // =====================================================

    /**
     * ➕ Cria um novo cargo no sistema.
     * 
     * 🧠 Lógica:
     * <ol>
     *   <li>Valida o nome do cargo (regra de domínio no model)</li>
     *   <li>Verifica se já existe outro com o mesmo nome</li>
     *   <li>Chama o DAO para inserir no banco</li>
     * </ol>
     *
     * @param nomeCargo Nome do cargo
     * @return ID gerado para o novo cargo
     * @throws SQLException Se ocorrer erro no banco
     * @throws Exception Se já existir cargo com o mesmo nome
     */
    public int createCargo(String nomeCargo) throws SQLException, Exception {
        System.out.println(">>> CargoService.createCargo()");

        // 🧱 Instancia um novo Cargo e aplica validação de domínio
        Cargo cargo = new Cargo();
        cargo.setNomeCargo(nomeCargo);

        // 🔎 Verifica duplicidade
        List<Cargo> resultado = cargoDAO.findByField("nomeCargo", nomeCargo);
        if (!resultado.isEmpty()) {
            throw new Exception("⚠️ Cargo já existe: " + nomeCargo);
        }

        // 💾 Persiste no banco via DAO
        return cargoDAO.create(cargo);
    }

    /**
     * 📋 Lista todos os cargos cadastrados.
     * 
     * @return Lista de cargos
     * @throws SQLException Se houver erro de conexão
     */
    public List<Cargo> findAll() throws SQLException {
        System.out.println(">>> CargoService.findAll()");
        return cargoDAO.findAll();
    }

    /**
     * 🔍 Busca um cargo específico pelo ID.
     * 
     * 🧠 Lógica:
     * <ul>
     *   <li>Valida o ID (regra de domínio no model)</li>
     *   <li>Consulta no banco via DAO</li>
     * </ul>
     *
     * @param idCargo ID do cargo
     * @return Objeto Cargo encontrado, ou <code>null</code> se não existir
     * @throws SQLException Se houver falha de acesso ao banco
     */
    public Cargo findById(int idCargo) throws SQLException {
        System.out.println(">>> CargoService.findById()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // Validação de domínio aplicada

        return cargoDAO.findById(cargo.getIdCargo());
    }

    /**
     * ✏️ Atualiza os dados de um cargo existente.
     * 
     * 🧠 Lógica:
     * <ol>
     *   <li>Valida o ID e o nome (regras de domínio)</li>
     *   <li>Chama o DAO para atualizar</li>
     * </ol>
     *
     * @param idCargo ID do cargo a ser atualizado
     * @param nomeCargo Novo nome do cargo
     * @return true se atualizado com sucesso
     * @throws SQLException Erro de conexão ou execução no banco
     * @throws Exception Se o nome do cargo for inválido
     */
    public boolean updateCargo(int idCargo, String nomeCargo) throws SQLException, Exception {
        System.out.println(">>> CargoService.updateCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo);
        cargo.setNomeCargo(nomeCargo);

        return cargoDAO.update(cargo);
    }

    /**
     * ❌ Exclui um cargo pelo ID.
     * 
     * 🧠 Lógica:
     * <ul>
     *   <li>Valida o ID do cargo</li>
     *   <li>Solicita ao DAO a exclusão no banco</li>
     * </ul>
     *
     * @param idCargo ID do cargo
     * @return true se excluído com sucesso
     * @throws SQLException Se ocorrer erro no banco
     */
    public boolean deleteCargo(int idCargo) throws SQLException {
        System.out.println(">>> CargoService.deleteCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo);

        return cargoDAO.delete(cargo);
    }
}
