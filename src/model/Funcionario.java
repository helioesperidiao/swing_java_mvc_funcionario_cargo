package model;

/**
 * Representa a entidade Funcionario do sistema.
 *
 * Objetivo:
 * - Encapsular os dados de um funcionário.
 * - Garantir integridade dos atributos via getters e setters.
 */
public class Funcionario {

    // Atributos privados
    private int idFuncionario;
    private String nomeFuncionario;
    private String email;
    private String senha;
    private boolean recebeValeTransporte;
    private Cargo cargo; // Relacionamento com Cargo

    /**
     * Construtor padrão
     */
    public Funcionario() {
        // System.out.println("⬆️ Funcionario.constructor()");
        this.cargo = new Cargo();
    }

    /**
     * Construtor com parâmetros
     */
    public Funcionario(int idFuncionario, String nomeFuncionario, String email,
            String senha, boolean recebeValeTransporte, Cargo cargo) {
        this.setIdFuncionario(idFuncionario);
        this.setNomeFuncionario(nomeFuncionario);
        this.setEmail(email);
        this.setSenha(senha);
        this.setRecebeValeTransporte(recebeValeTransporte);
        this.setCargo(cargo);
        // System.out.println("⬆️ Funcionario.constructor(...)");
    }

    // ============================
    // Getters e Setters
    // ============================

    public int getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * Regra: idFuncionario deve ser maior que zero
     */
    public void setIdFuncionario(int idFuncionario) {
        if (idFuncionario <= 0) {
            throw new IllegalArgumentException("idFuncionario deve ser maior que zero.");
        }
        this.idFuncionario = idFuncionario;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    /**
     * Regra: nome não pode ser nulo, vazio e deve ter entre 3 e 128 caracteres
     */
    public void setNomeFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null) {
            throw new IllegalArgumentException("nomeFuncionario não pode ser nulo.");
        }

        String nomeTrimmed = nomeFuncionario.trim();

        if (nomeTrimmed.length() < 3) {
            throw new IllegalArgumentException("nomeFuncionario deve ter pelo menos 3 caracteres.");
        }

        if (nomeTrimmed.length() > 128) {
            throw new IllegalArgumentException("nomeFuncionario deve ter no máximo 128 caracteres.");
        }

        this.nomeFuncionario = nomeTrimmed;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Regra: email deve ser válido, não nulo, até 64 caracteres
     */
    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email não pode ser nulo.");
        }

        String emailTrimmed = email.trim();

        if (emailTrimmed.isEmpty()) {
            throw new IllegalArgumentException("email não pode ser vazio.");
        }

        if (emailTrimmed.length() > 64) {
            throw new IllegalArgumentException("email deve ter no máximo 64 caracteres.");
        }

        // Validação básica de formato de email
        if (!emailTrimmed.contains("@") || !emailTrimmed.contains(".")) {
            throw new IllegalArgumentException("email inválido.");
        }

        this.email = emailTrimmed;
    }

    public String getSenha() {
        return senha;
    }

    /**
     * Regra: senha não pode ser nula, deve ter entre 6 e 64 caracteres
     */
    public void setSenha(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("senha não pode ser nula.");
        }

        String senhaTrimmed = senha.trim();

        if (senhaTrimmed.length() < 6) {
            throw new IllegalArgumentException("senha deve ter pelo menos 6 caracteres.");
        }

        if (senhaTrimmed.length() > 64) {
            throw new IllegalArgumentException("senha deve ter no máximo 64 caracteres.");
        }

        this.senha = senhaTrimmed;
    }

    public boolean isRecebeValeTransporte() {
        return recebeValeTransporte;
    }

    public void setRecebeValeTransporte(boolean recebeValeTransporte) {
        this.recebeValeTransporte = recebeValeTransporte;
    }

    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Regra: cargo não pode ser nulo
     */
    public void setCargo(Cargo cargo) {
        if (cargo == null) {
            throw new IllegalArgumentException("cargo não pode ser nulo.");
        }
        this.cargo = cargo;
    }

    // ============================
    // Método auxiliar para exibir informações
    // ============================
    @Override
    public String toString() {
        return "Funcionario{" +
                "idFuncionario=" + idFuncionario +
                ", nomeFuncionario='" + nomeFuncionario + '\'' +
                ", email='" + email + '\'' +
                ", recebeValeTransporte=" + recebeValeTransporte +
                ", cargo=" + (cargo != null ? cargo.getNomeCargo() : "null") +
                '}';
    }
}
