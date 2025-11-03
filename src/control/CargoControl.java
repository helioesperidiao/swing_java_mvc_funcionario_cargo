package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Cargo;
import service.CargoService;

/**
 * Classe responsável por controlar a lógica de requisição/resposta
 * para a entidade Cargo.
 * 
 * Esta classe representa a camada "Controller" no padrão MVC.
 * Ela atua como intermediária entre a camada de serviço (regras de negócio)
 * e a camada de visão (interface do usuário, API ou outra forma de saída).
 * 
 * Observação:
 * - Aqui não há dependência de frameworks web (como Spring MVC ou Jakarta EE).
 * - A ideia é simular o comportamento de um controlador genérico.
 * 
 * Resumo didático (para anotações de aula):
 *
 *   Camada Control (Controller):
 *   Recebe as requisições (ou chamadas do usuário/sistema), chama os serviços (Service) e retorna respostas.
 *   Não contém regras de negócio, apenas coordena o fluxo.
 *
 *   Camada Service:
 *   Onde ficam as regras de negócio e operações sobre o domínio.
 *
 *   Camada Model:
 *   Onde ficam os dados e as regras de domínio (restrições e comportamento da entidade).
 * 
 * @author Hélio
 * @since 2025
 */
public class CargoControl {

    /** Serviço responsável por executar as regras de negócio relacionadas a Cargo. */
    private final CargoService cargoService;

    /**
     * Construtor com injeção de dependência.
     * Recebe um objeto do tipo CargoService para permitir o desacoplamento
     * entre as camadas.
     * 
     * @param cargoService instância de CargoService que contém as regras de negócio
     */
    public CargoControl(CargoService cargoService) {
        System.out.println(">> CargoControl.constructor()");
        this.cargoService = cargoService;
    }

    /**
     * Cria um novo cargo no sistema.
     * 
     * Fluxo típico de MVC:
     * - O Controller recebe os dados (ex: de uma requisição).
     * - Chama o Service para aplicar as regras de negócio e persistir no banco.
     * - Retorna o objeto criado.
     * 
     * @param cargo mapa contendo os dados do novo cargo
     * @return o objeto Cargo criado
     * @throws SQLException caso ocorra erro no banco de dados
     * @throws Exception    caso ocorra erro de validação ou de negócio
     */
    public Cargo store(Map<String, Object> cargo) throws SQLException, Exception {
        System.out.println(">> CargoControl.store()");

        // 1️⃣ Extrai o dado enviado (simulando o corpo de uma requisição)
        // Aqui esperamos que o mapa contenha uma chave "nomeCargo"
        String nomeCargo = (String) cargo.get("nomeCargo");

        // 2️⃣ Pede ao serviço para criar o cargo no banco de dados
        // O serviço aplica as regras de negócio e retorna o ID gerado
        int novoId = cargoService.createCargo(nomeCargo);

        // 3️⃣ Cria um objeto Cargo em memória com os dados resultantes
        // (útil para retornar ao cliente ou interface)
        Cargo c = new Cargo();
        c.setIdCargo(novoId);
        c.setNomeCargo(nomeCargo);

        // 4️⃣ Retorna o novo cargo criado
        return c;
    }

    /**
     * Lista todos os cargos cadastrados.
     * 
     * O Controller apenas solicita os dados ao Service e retorna a lista.
     * 
     * @return lista de todos os cargos
     * @throws SQLException caso ocorra erro ao acessar o banco
     */
    public List<Cargo> index() throws SQLException {
        System.out.println(">> CargoControl.index()");

        // 1️⃣ O Controller pede ao serviço a lista de cargos
        List<Cargo> cargos = cargoService.findAll();

        // 2️⃣ Retorna a lista obtida
        // (em um sistema web, aqui normalmente seria convertido para JSON)
        return cargos;
    }

    /**
     * Busca um cargo pelo seu ID.
     * 
     * @param idCargo identificador único do cargo
     * @return objeto Cargo correspondente ou null se não encontrado
     * @throws SQLException caso ocorra erro no banco
     */
    public Cargo show(int idCargo) throws SQLException {
        System.out.println(">> CargoControl.show()");

        // 1️⃣ Pede ao serviço para buscar um cargo específico
        Cargo cargo = cargoService.findById(idCargo);

        // 2️⃣ Retorna o resultado (pode ser null se não existir)
        return cargo;
    }

    /**
     * Atualiza os dados de um cargo existente.
     * 
     * - O método retorna um Map contendo o status da operação e mensagens de
     *   retorno.
     * - Isso é útil em APIs ou interfaces genéricas onde a resposta é serializada
     *   (ex: JSON).
     * 
     * @param idCargo   ID do cargo a ser atualizado
     * @param nomeCargo novo nome do cargo
     * @return mapa contendo o status (success, message, data)
     */
    public Map<String, Object> update(int idCargo, String nomeCargo) {
        System.out.println(">> CargoControl.update()");

        // 1️⃣ Cria um mapa que representará a resposta final da operação
        Map<String, Object> response = new HashMap<>();

        try {
            // 2️⃣ Chama o serviço para atualizar o cargo no banco
            boolean atualizou = cargoService.updateCargo(idCargo, nomeCargo);

            // 3️⃣ Monta um mapa com os dados do cargo atualizado
            Map<String, Object> cargoData = new HashMap<>();
            cargoData.put("idCargo", idCargo);
            cargoData.put("nomeCargo", nomeCargo);

            // 4️⃣ Cria uma lista com esse cargo (pode haver múltiplos em outras operações)
            List<Map<String, Object>> cargosList = new ArrayList<>();
            cargosList.add(cargoData);

            // 5️⃣ Cria um objeto “data” contendo essa lista
            Map<String, Object> data = new HashMap<>();
            data.put("cargos", cargosList);

            // 6️⃣ Monta o resultado final com mensagem e sucesso
            response.put("success", atualizou);
            response.put("message", atualizou
                    ? "Atualizado com sucesso"
                    : "Cargo não encontrado para atualização");
            response.put("data", data);

        } catch (Exception e) {
            // 7️⃣ Caso aconteça alguma exceção, retorna erro amigável
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        // 8️⃣ Retorna o mapa final (simulando uma resposta JSON)
        return response;
    }

    /**
     * Exclui um cargo do sistema.
     * 
     * - Usa um padrão semelhante ao método update().
     * - Retorna um mapa com informações sobre a operação.
     * 
     * @param idCargo identificador do cargo a ser removido
     * @return mapa com status e mensagem da operação
     */
    public Map<String, Object> destroy(int idCargo) {
        System.out.println(">> CargoControl.destroy()");

        // 1️⃣ Cria um mapa para montar a resposta final
        Map<String, Object> response = new HashMap<>();

        try {
            // 2️⃣ Chama o serviço para excluir o cargo pelo ID
            boolean excluiu = cargoService.deleteCargo(idCargo);

            // 3️⃣ Cria um mapa com os dados do cargo excluído
            Map<String, Object> cargoData = new HashMap<>();
            cargoData.put("idCargo", idCargo);

            // 4️⃣ Adiciona esse mapa a uma lista (estrutura padrão de resposta)
            List<Map<String, Object>> cargosList = new ArrayList<>();
            cargosList.add(cargoData);

            // 5️⃣ Cria o objeto “data” que agrupa os dados da resposta
            Map<String, Object> data = new HashMap<>();
            data.put("cargos", cargosList);

            // 6️⃣ Define se a operação foi bem-sucedida e monta mensagem
            response.put("success", excluiu);
            response.put("message", excluiu
                    ? "Excluído com sucesso"
                    : "Cargo não encontrado para exclusão");
            response.put("data", data);

        } catch (Exception e) {
            // 7️⃣ Caso ocorra erro, captura e retorna uma mensagem amigável
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        // 8️⃣ Retorna o mapa final, representando a resposta da operação
        return response;
    }

}
