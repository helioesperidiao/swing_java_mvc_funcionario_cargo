package service;

import java.sql.SQLException;
import java.util.List;

import dao.CargoDAO;
import model.Cargo;

/**
 * Classe responsável pela camada de serviço para a entidade Cargo.
 * 
 * Observações sobre injeção de dependência:
 * - O CargoService recebe uma instância de CargoDAO via construtor.
 * - Isso desacopla o serviço do DAO concreto, facilitando testes unitários e mocks.
 */
public class CargoService {

    private final CargoDAO cargoDAO;

    // Construtor com injeção de dependência
    public CargoService(CargoDAO cargoDAODependency) {
        System.out.println(">>  CargoService.constructor()");
        this.cargoDAO = cargoDAODependency;
    }

    /**
     * Cria um novo cargo
     * @param nomeCargo Nome do cargo
     * @return ID do cargo criado
     * @throws SQLException Em caso de falha no banco
     * @throws Exception Caso já exista cargo com mesmo nome
     */
    public int createCargo(String nomeCargo) throws SQLException, Exception {
        System.out.println(">>> CargoService.createCargo()");

        Cargo cargo = new Cargo();
        cargo.setNomeCargo(nomeCargo); // validação de regra de domínio

        // Verifica se já existe cargo com mesmo nome
        List<Cargo> resultado = cargoDAO.findByField("nomeCargo", nomeCargo);
        if (!resultado.isEmpty()) {
            throw new Exception("Cargo já existe: " + nomeCargo);
        }

        return cargoDAO.create(cargo);
    }

    /**
     * Retorna todos os cargos
     * @return Lista de cargos
     * @throws SQLException
     */
    public List<Cargo> findAll() throws SQLException {
        System.out.println(">>> CargoService.findAll()");
        return cargoDAO.findAll();
    }

    /**
     * Retorna um cargo por ID
     * @param idCargo
     * @return Cargo ou null se não encontrado
     * @throws SQLException
     */
    public Cargo findById(int idCargo) throws SQLException {
        System.out.println(">>> CargoService.findById()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio

        return cargoDAO.findById(cargo.getIdCargo());
    }

    /**
     * Atualiza um cargo existente
     * @param idCargo ID do cargo a ser atualizado
     * @param nomeCargo Novo nome do cargo
     * @return true se atualizado com sucesso
     * @throws SQLException
     * @throws Exception Se o nome do cargo for inválido
     */
    public boolean updateCargo(int idCargo, String nomeCargo) throws SQLException, Exception {
        System.out.println(">>> CargoService.updateCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio
        cargo.setNomeCargo(nomeCargo); // validação de regra de domínio

        return cargoDAO.update(cargo);
    }

    /**
     * Deleta um cargo por ID
     * @param idCargo ID do cargo
     * @return true se excluído com sucesso
     * @throws SQLException
     */
    public boolean deleteCargo(int idCargo) throws SQLException {
        System.out.println(">>> CargoService.deleteCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio

        return cargoDAO.delete(cargo);
    }
}
