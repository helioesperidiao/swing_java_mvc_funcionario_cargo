package model;

/**
 * Representa a entidade Funcionario do sistema.
 * 
 * Objetivo:
 * - Encapsular os dados de um funcionário.
 * - Garantir integridade dos atributos via getters e setters.
 * - Associar corretamente um funcionário a um Cargo.
 */
public class Funcionario {

    private int idFuncionario;
    private String nomeFuncionario;
    private String email;
    private String senha;
    private boolean recebeValeTransporte;
    private Cargo cargo; // Associação ao Cargo

    /**
     * Construtor padrão
     */
    public Funcionario() {
        //System.out.println("⬆️  Funcionario.constructor()");
        this.cargo = new Cargo();
    }

    /**
     * Retorna o ID do funcionário
     * 
     * @return int - identificador único do funcionário
     */
    public int getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * Define o ID do funcionário.
     * 
     * 🔹 Regra de domínio: ID sempre positivo
     * 
     * @param idFuncionario - número inteiro positivo
     * @throws IllegalArgumentException se id <= 0
     */
    public void setIdFuncionario(int idFuncionario) {
        if (idFuncionario <= 0) {
            throw new IllegalArgumentException("idFuncionario deve ser maior que zero.");
        }
        this.idFuncionario = idFuncionario;
    }

    /**
     * Retorna o nome do funcionário
     * 
     * @return String - nome do funcionário
     */
    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    /**
     * Define o nome do funcionário.
     * 
     * 🔹 Regra de domínio: nome não nulo, não vazio, mínimo 3 e máximo 64 caracteres
     * 
     * @param nomeFuncionario - nome do funcionário
     * @throws IllegalArgumentException se inválido
     */
    public void setNomeFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null) {
            throw new IllegalArgumentException("nomeFuncionario não pode ser null.");
        }
        String nomeTrim = nomeFuncionario.trim();
        if (nomeTrim.length() < 3) {
            throw new IllegalArgumentException("nomeFuncionario deve ter pelo menos 3 caracteres.");
        }
        if (nomeTrim.length() > 64) {
            throw new IllegalArgumentException("nomeFuncionario deve ter no máximo 64 caracteres.");
        }
        this.nomeFuncionario = nomeTrim;
    }

    /**
     * Retorna o email do funcionário
     * 
     * @return String - email do funcionário
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do funcionário.
     * 
     * 🔹 Regra de domínio: email válido, não vazio
     * 
     * @param email - email do funcionário
     * @throws IllegalArgumentException se inválido
     */
    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email não pode ser null.");
        }
        String emailTrim = email.trim();
        if (emailTrim.isEmpty()) {
            throw new IllegalArgumentException("email não pode ser vazio.");
        }
        // Regex simples para validar email
        if (!emailTrim.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("email em formato inválido.");
        }
        this.email = emailTrim;
    }

    /**
     * Retorna a senha do funcionário
     * 
     * @return String - senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do funcionário.
     * 
     * 🔹 Regra de domínio: mínimo 6 caracteres, pelo menos 1 maiúscula, 1 número e 1 caractere especial
     * 
     * @param senha - senha do funcionário
     * @throws IllegalArgumentException se inválido
     */
    public void setSenha(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("senha não pode ser null.");
        }
        String senhaTrim = senha.trim();
        if (senhaTrim.length() < 6) {
            throw new IllegalArgumentException("senha deve ter pelo menos 6 caracteres.");
        }
        if (!senhaTrim.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("senha deve conter pelo menos uma letra maiúscula.");
        }
        if (!senhaTrim.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("senha deve conter pelo menos um número.");
        }
        if (!senhaTrim.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("senha deve conter pelo menos um caractere especial.");
        }
        this.senha = senhaTrim;
    }

    /**
     * Retorna se o funcionário recebe vale transporte
     * 
     * @return boolean
     */
    public boolean isRecebeValeTransporte() {
        return recebeValeTransporte;
    }

    /**
     * Define se o funcionário recebe vale transporte
     * 
     * @param recebeValeTransporte - true para sim, false para não
     */
    public void setRecebeValeTransporte(boolean recebeValeTransporte) {
        this.recebeValeTransporte = recebeValeTransporte;
    }

    /**
     * Retorna o Cargo associado
     * 
     * @return Cargo
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Define o Cargo do funcionário
     * 
     * @param cargo - instância válida de Cargo
     * @throws IllegalArgumentException se cargo for null
     */
    public void setCargo(Cargo cargo) {
        if (cargo == null) {
            throw new IllegalArgumentException("cargo não pode ser null.");
        }
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Funcionario [idFuncionario=" + idFuncionario + ", nomeFuncionario=" + nomeFuncionario +
               ", email=" + email + ", recebeValeTransporte=" + recebeValeTransporte +
               ", cargo=" + (cargo != null ? cargo.getNomeCargo() : "null") + "]";
    }
}
