package service;

import dao.CargoDAO;
import model.Cargo;
import utils.ErrorResponse;

import java.util.List;

/**
 * Classe responsável pela camada de serviço para a entidade Cargo.
 * 
 * Observações sobre injeção de dependência:
 * - O CargoService recebe uma instância de CargoDAO via construtor.
 * - Isso segue o padrão de injeção de dependência, tornando o serviço desacoplado
 *   do DAO concreto, facilitando testes unitários e substituição por mocks.
 */
public class CargoService {

    private final CargoDAO cargoDAO;

    /**
     * Construtor da classe CargoService
     * @param cargoDAO Dependência injetada de CargoDAO
     */
    public CargoService(CargoDAO cargoDAO) {
        System.out.println("⬆️  CargoService.constructor()");
        this.cargoDAO = cargoDAO;
    }

    /**
     * Cria um novo cargo
     * 
     * @param nomeCargo Nome do cargo
     * @return ID do novo cargo criado
     * @throws ErrorResponse se já existir um cargo com o mesmo nome
     */
    public int createCargo(String nomeCargo) throws Exception {
        System.out.println("🟣 CargoService.createCargo()");

        Cargo cargo = new Cargo();
        cargo.setNomeCargo(nomeCargo); // validação de regra de domínio no setter

        // Valida regra de negócio: não pode existir outro cargo com mesmo nome
        List<Cargo> resultado = cargoDAO.findByField("nomeCargo", cargo.getNomeCargo());
        if (!resultado.isEmpty()) {
            throw new ErrorResponse(
                400,
                "Cargo já existe",
                "O cargo " + cargo.getNomeCargo() + " já existe"
            );
        }

        return cargoDAO.create(cargo);
    }

    /**
     * Retorna todos os cargos
     * 
     * @return Lista de cargos
     */
    public List<Cargo> findAll() {
        System.out.println("🟣 CargoService.findAll()");
        return cargoDAO.findAll();
    }

    /**
     * Retorna um cargo por ID
     * 
     * @param idCargo Identificador do cargo
     * @return Cargo encontrado
     * @throws Exception se o ID for inválido
     */
    public Cargo findById(int idCargo) throws Exception {
        System.out.println("🟣 CargoService.findById()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio

        return cargoDAO.findById(cargo.getIdCargo());
    }

    /**
     * Atualiza um cargo existente
     * 
     * @param idCargo ID do cargo a ser atualizado
     * @param nomeCargo Novo nome do cargo
     * @return Cargo atualizado
     * @throws Exception se idCargo ou nomeCargo inválidos
     */
    public Cargo updateCargo(int idCargo, String nomeCargo) throws Exception {
        System.out.println("🟣 CargoService.updateCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio
        cargo.setNomeCargo(nomeCargo); // validação de regra de domínio

        return cargoDAO.update(cargo);
    }

    /**
     * Deleta um cargo por ID
     * 
     * @param idCargo ID do cargo a ser deletado
     * @return boolean indicando sucesso da operação
     * @throws Exception se idCargo inválido
     */
    public boolean deleteCargo(int idCargo) throws Exception {
        System.out.println("🟣 CargoService.deleteCargo()");

        Cargo cargo = new Cargo();
        cargo.setIdCargo(idCargo); // validação de regra de domínio

        return cargoDAO.delete(cargo);
    }
}
