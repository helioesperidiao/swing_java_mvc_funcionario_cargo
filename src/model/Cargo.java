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
     * Construtor padrão
     */
    public Cargo() {
        //System.out.println("⬆️  Cargo.constructor()");
    }

    /**
     * Construtor com parâmetros
     */
    public Cargo(int idCargo, String nomeCargo) {
        this.setIdCargo(idCargo);
        this.setNomeCargo(nomeCargo);
        //System.out.println("⬆️  Cargo.constructor(idCargo, nomeCargo)");
    }

    /**
     * Getter para idCargo
     * 
     * @return Identificador único do cargo
     */
    public int getIdCargo() {
        return idCargo;
    }

    /**
     * Setter para idCargo
     * 🔹 Regra de domínio: deve ser um número inteiro positivo.
     * 
     * @param idCargo Número inteiro positivo representando o ID do cargo
     * @throws IllegalArgumentException Se o valor não for válido
     */
    public void setIdCargo(int idCargo) {
        if (idCargo <= 0) {
            throw new IllegalArgumentException("idCargo deve ser maior que zero.");
        }
        this.idCargo = idCargo;
    }

    /**
     * Getter para nomeCargo
     * 
     * @return Nome do cargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * Setter para nomeCargo
     * 🔹 Regra de domínio: não pode ser nulo, vazio e deve ter entre 3 e 64
     * caracteres
     * 
     * @param nomeCargo Nome do cargo
     * @throws IllegalArgumentException Se o valor não for válido
     */
    public void setNomeCargo(String nomeCargo) {
        if (nomeCargo == null) {
            throw new IllegalArgumentException("nomeCargo não pode ser nulo.");
        }

        String nomeTrimmed = nomeCargo.trim();

        if (nomeTrimmed.length() < 3) {
            throw new IllegalArgumentException("nomeCargo deve ter pelo menos 3 caracteres.");
        }

        if (nomeTrimmed.length() > 64) {
            throw new IllegalArgumentException("nomeCargo deve ter no máximo 64 caracteres.");
        }

        this.nomeCargo = nomeTrimmed;
    }
    public String toString(){
        return this.getNomeCargo();
    }
}
