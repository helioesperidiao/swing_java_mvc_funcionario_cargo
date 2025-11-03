package model;

/**
 * 🧩 Classe: Cargo
 * 
 * 📘 Representa a entidade <b>Cargo</b> no sistema de Gestão de RH.
 * 
 * 🎯 Objetivo:
 * <ul>
 * <li>Encapsular os dados de um cargo (ID e nome)</li>
 * <li>Garantir integridade dos atributos através de validações</li>
 * <li>Servir como modelo para persistência e manipulação de dados</li>
 * </ul>
 * 
 * 🧠 Notas de Aula:
 * <ul>
 * <li>Esta é uma classe de <b>modelo</b> (camada Model no padrão MVC)</li>
 * <li>Os atributos são privados ➜ respeitando o princípio de
 * <b>encapsulamento</b></li>
 * <li>O acesso é feito por meio de <b>getters</b> e <b>setters</b></li>
 * </ul>
 */
public class Cargo {

    // 🧱 Atributos privados — boa prática de encapsulamento
    private int idCargo; // 🔑 Identificador único do cargo
    private String nomeCargo; // 🏷️ Nome descritivo do cargo

    /**
     * 🏗️ Construtor padrão (sem parâmetros)
     * 
     * ✏️ Usado quando queremos criar um objeto vazio e preencher depois.
     */
    public Cargo() {
        // System.out.println("⬆️ Cargo.constructor() chamado");
    }

    /**
     * 🧰 Construtor completo (com parâmetros)
     * 
     * ✏️ Facilita a criação de objetos totalmente configurados.
     * 
     * @param idCargo   ID único do cargo
     * @param nomeCargo Nome do cargo
     */
    public Cargo(int idCargo, String nomeCargo) {
        this.setIdCargo(idCargo); // Usa o setter (com validação)
        this.setNomeCargo(nomeCargo);
        // System.out.println("🆕 Cargo criado com parâmetros.");
    }

    /**
     * 📤 Getter para <b>idCargo</b>
     * 
     * @return O ID único do cargo
     */
    public int getIdCargo() {
        return idCargo;
    }

    /**
     * 📥 Setter para <b>idCargo</b>
     * 
     * 🧠 Regra de negócio:
     * - O ID deve ser um número inteiro positivo (não pode ser 0 ou negativo)
     * 
     * @param idCargo número inteiro positivo representando o ID do cargo
     * @throws IllegalArgumentException se o valor não for válido
     */
    public void setIdCargo(int idCargo) {
        if (idCargo <= 0) {
            throw new IllegalArgumentException("⚠️ idCargo deve ser maior que zero.");
        }
        this.idCargo = idCargo;
    }

    /**
     * 📤 Getter para <b>nomeCargo</b>
     * 
     * @return Nome do cargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * 📥 Setter para <b>nomeCargo</b>
     * 
     * 🧠 Regras de validação:
     * <ul>
     * <li>Não pode ser nulo</li>
     * <li>Não pode ser vazio</li>
     * <li>Deve conter entre 3 e 64 caracteres</li>
     * </ul>
     * 
     * @param nomeCargo Nome do cargo
     * @throws IllegalArgumentException se o valor for inválido
     */
    public void setNomeCargo(String nomeCargo) {
        if (nomeCargo == null) {
            throw new IllegalArgumentException("⚠️ nomeCargo não pode ser nulo.");
        }

        String nomeTrimmed = nomeCargo.trim();

        if (nomeTrimmed.isEmpty() || nomeTrimmed.length() < 3) {
            throw new IllegalArgumentException("⚠️ nomeCargo deve ter pelo menos 3 caracteres.");
        }

        if (nomeTrimmed.length() > 64) {
            throw new IllegalArgumentException("⚠️ nomeCargo deve ter no máximo 64 caracteres.");
        }

        this.nomeCargo = nomeTrimmed;
    }

    /**
     * 🧾 Sobrescrita do método toString()
     * 
     * 🧠 Permite que o cargo seja exibido de forma legível em listas, logs e
     * tabelas.
     * 
     * @return Nome do cargo
     */
    @Override
    public String toString() {

        return this.getNomeCargo();
    }
}
