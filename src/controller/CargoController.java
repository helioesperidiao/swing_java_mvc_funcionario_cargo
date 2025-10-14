package controller;

import model.Cargo;
import model.dao.CargoDAO;

import java.util.List;

public class CargoController {

    private CargoDAO cargoDAO;

    public CargoController() {
        this.cargoDAO = new CargoDAO();
    }

    // Método para criar um cargo
    public boolean createController(String nomeCargo) {
        if (nomeCargo == null || nomeCargo.isEmpty()) {
            System.out.println("O nome do cargo é obrigatório.");
            return false;
        }

        Cargo cargo = new Cargo();
        cargo.setNomeCargo(nomeCargo);

        return cargoDAO.create(cargo);
    }

    // Método para buscar um cargo por ID
    public Cargo readByIdController(int idCargo) {
        if (idCargo <= 0) {
            System.out.println("ID inválido.");
            return null;
        }
        return cargoDAO.readById(idCargo);
    }

    // Método para atualizar um cargo
    public boolean updateController(int idCargo, String nomeCargo) {
        Cargo cargo = cargoDAO.readById(idCargo);
        if (cargo == null) {
            System.out.println("Cargo não encontrado.");
            return false;
        }

        cargo.setNomeCargo(nomeCargo);
        return cargoDAO.update(cargo);
    }

    // Método para deletar um cargo
    public boolean deleteController(int idCargo) {
        return cargoDAO.delete(idCargo);
    }

    // Método para listar todos os cargos
    public List<Cargo> readAllController() {
        return cargoDAO.readAll();
    }
}
