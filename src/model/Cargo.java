package model;

/**
 * Representa a entidade Cargo do sistema.
 * 
 * Objetivo:
 * - Encapsular os dados de um cargo.
 * - Garantir integridade dos atributos via getters e setters.
 */
public class Cargo {

    // Atributos privados
    private int idCargo;
    private String nomeCargo;

    /**
     * Construtor padrão da classe Cargo
     */
    public Cargo() {
        //System.out.println("⬆️  Cargo.constructor()");
    }

    /**
     * Retorna o ID do cargo
     * 
     * @return int - Identificador único do cargo
     */
    public int getIdCargo() {
        return idCargo;
    }

    /**
     * Define o ID do cargo.
     *
     * 🔹 Regra de domínio: garante que o ID seja sempre um número inteiro positivo.
     *
     * @param idCargo - Número inteiro positivo representando o ID do cargo
     * @throws IllegalArgumentException se o valor for <= 0
     */
    public void setIdCargo(int idCargo) {
        if (idCargo <= 0) {
            throw new IllegalArgumentException("idCargo deve ser maior que zero.");
        }
        this.idCargo = idCargo;
    }

    /**
     * Retorna o nome do cargo
     * 
     * @return String - Nome do cargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * Define o nome do cargo.
     *
     * 🔹 Regra de domínio: garante que o nome seja sempre uma string não vazia
     * e com pelo menos 3 caracteres.
     *
     * @param nomeCargo - Nome do cargo
     * @throws IllegalArgumentException se o valor for null, vazio ou tiver menos de 3 caracteres ou mais de 64
     */
    public void setNomeCargo(String nomeCargo) {
        if (nomeCargo == null) {
            throw new IllegalArgumentException("nomeCargo não pode ser null.");
        }
        String nomeTrim = nomeCargo.trim();
        if (nomeTrim.length() < 3) {
            throw new IllegalArgumentException("nomeCargo deve ter pelo menos 3 caracteres.");
        }
        if (nomeTrim.length() > 64) {
            throw new IllegalArgumentException("nomeCargo deve ter no máximo 64 caracteres.");
        }
        this.nomeCargo = nomeTrim;
    }

    @Override
    public String toString() {
        return "Cargo [idCargo=" + idCargo + ", nomeCargo=" + nomeCargo + "]";
    }
}
