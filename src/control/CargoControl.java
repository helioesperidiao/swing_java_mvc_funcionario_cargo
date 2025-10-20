package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Cargo;
import service.CargoService;

/**
 * Classe responsável por controlar a lógica de requisição/resposta para a
 * entidade Cargo.
 * Sem dependência de framework web.
 */
public class CargoControl {

    private final CargoService cargoService;

    // Construtor com injeção de dependência
    public CargoControl(CargoService cargoService) {
        System.out.println(">> CargoControl.constructor()");
        this.cargoService = cargoService;
    }

    // Cria um novo cargo
    public Cargo store(Map<String, Object> cargo) throws SQLException, Exception {
        System.out.println(">> CargoControl.store()");

        String nomeCargo = (String) cargo.get("nomeCargo");
        int novoId = cargoService.createCargo(nomeCargo);
        Cargo c = new Cargo();
        c.setIdCargo(novoId);
        c.setNomeCargo(nomeCargo);

        return c;
    }

    // Lista todos os cargos
    public List<Cargo> index() throws SQLException {
        System.out.println(">> CargoControl.index()");

        List<Cargo> cargos = cargoService.findAll();

        return cargos;
    }

    // Busca um cargo pelo ID
    public Cargo show(int idCargo) throws SQLException {
        System.out.println(">> CargoControl.show()");

        Cargo cargo = cargoService.findById(idCargo);

        return cargo;
    }

    // Atualiza um cargo existente
    public Map<String, Object> update(int idCargo, String nomeCargo) {
        System.out.println(">> CargoControl.update()");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean atualizou = cargoService.updateCargo(idCargo, nomeCargo);

            // Monta o objeto do cargo atualizado
            Map<String, Object> cargoData = new HashMap<>();
            cargoData.put("idCargo", idCargo);
            cargoData.put("nomeCargo", nomeCargo);

            // Coloca em uma lista
            List<Map<String, Object>> cargosList = new ArrayList<>();
            cargosList.add(cargoData);

            // Monta o objeto de dados
            Map<String, Object> data = new HashMap<>();
            data.put("cargos", cargosList);

            // Monta a resposta final
            response.put("success", atualizou);
            response.put("message", atualizou ? "Atualizado com sucesso" : "Cargo não encontrado para atualização");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // Deleta um cargo
    public Map<String, Object> destroy(int idCargo) {
        System.out.println(">> CargoControl.destroy()");
        Map<String, Object> response = new HashMap<>();
        try {
            boolean excluiu = cargoService.deleteCargo(idCargo);

            // Monta o objeto do cargo excluído
            Map<String, Object> cargoData = new HashMap<>();
            cargoData.put("idCargo", idCargo);

            // Coloca em uma lista
            List<Map<String, Object>> cargosList = new ArrayList<>();
            cargosList.add(cargoData);

            // Monta o objeto de dados
            Map<String, Object> data = new HashMap<>();
            data.put("cargos", cargosList);

            // Monta a resposta final
            response.put("success", excluiu);
            response.put("message", excluiu ? "Excluído com sucesso" : "Cargo não encontrado para exclusão");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

}
