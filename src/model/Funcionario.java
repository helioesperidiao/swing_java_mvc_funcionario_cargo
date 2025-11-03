package model;

/**
 * 🧩 Classe: Funcionario
 * 
 * 📘 Representa a entidade <b>Funcionário</b> no sistema de Gestão de RH.
 * 
 * 🎯 Objetivos:
 * <ul>
 *   <li>Encapsular os dados de um funcionário</li>
 *   <li>Manter integridade dos atributos através de validações</li>
 *   <li>Demonstrar relacionamento com a entidade {@link Cargo}</li>
 * </ul>
 * 
 * 🧠 Notas de Aula:
 * <ul>
 *   <li>Esta é uma classe da camada <b>Model</b> do padrão MVC</li>
 *   <li>Os atributos são <b>privados</b> (🔒 encapsulamento)</li>
 *   <li>O acesso se dá por meio de <b>getters e setters</b></li>
 *   <li>Inclui regras de <b>domínio</b> — validações diretas dos dados</li>
 * </ul>
 */
public class Funcionario {

    // 🧱 Atributos privados — encapsulamento
    private int idFuncionario;             // 🔑 Identificador único
    private String nomeFuncionario;        // 🧍 Nome completo
    private String email;                  // 📧 Endereço de email corporativo
    private String senha;                  // 🔐 Senha para login
    private boolean recebeValeTransporte;  // 🚌 Indica se recebe VT
    private Cargo cargo;                   // ⚙️ Relação com o Cargo (composição)

    /**
     * 🏗️ Construtor padrão
     * 
     * ✏️ Cria um funcionário vazio com um cargo inicializado.
     */
    public Funcionario() {
        // System.out.println("⬆️ Funcionario.constructor()");
        this.cargo = new Cargo(); // evita NullPointerException em uso inicial
    }

    /**
     * 🧰 Construtor completo (com parâmetros)
     * 
     * ✏️ Facilita a criação de um objeto totalmente configurado.
     */
    public Funcionario(int idFuncionario, String nomeFuncionario, String email,
                       String senha, boolean recebeValeTransporte, Cargo cargo) {
        this.setIdFuncionario(idFuncionario);
        this.setNomeFuncionario(nomeFuncionario);
        this.setEmail(email);
        this.setSenha(senha);
        this.setRecebeValeTransporte(recebeValeTransporte);
        this.setCargo(cargo);
    }

    // =====================================================
    // 🧩 GETTERS e SETTERS — com regras de domínio e validações
    // =====================================================

    /**
     * 📤 Retorna o ID do funcionário.
     */
    public int getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * 📥 Define o ID do funcionário.
     * 
     * ⚙️ Regra de domínio:
     * - O ID deve ser um número inteiro positivo (> 0)
     */
    public void setIdFuncionario(int idFuncionario) {
        if (idFuncionario <= 0) {
            throw new IllegalArgumentException("⚠️ idFuncionario deve ser maior que zero.");
        }
        this.idFuncionario = idFuncionario;
    }

    /**
     * 📤 Retorna o nome do funcionário.
     */
    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    /**
     * 📥 Define o nome do funcionário.
     * 
     * ⚙️ Regras:
     * <ul>
     *   <li>Não pode ser nulo</li>
     *   <li>Não pode ser vazio</li>
     *   <li>Deve ter entre 3 e 128 caracteres</li>
     * </ul>
     */
    public void setNomeFuncionario(String nomeFuncionario) {
        if (nomeFuncionario == null) {
            throw new IllegalArgumentException("⚠️ nomeFuncionario não pode ser nulo.");
        }

        String nomeTrimmed = nomeFuncionario.trim();

        if (nomeTrimmed.length() < 3) {
            throw new IllegalArgumentException("⚠️ nomeFuncionario deve ter pelo menos 3 caracteres.");
        }

        if (nomeTrimmed.length() > 128) {
            throw new IllegalArgumentException("⚠️ nomeFuncionario deve ter no máximo 128 caracteres.");
        }

        this.nomeFuncionario = nomeTrimmed;
    }

    /**
     * 📤 Retorna o email do funcionário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * 📥 Define o email do funcionário.
     * 
     * ⚙️ Regras:
     * <ul>
     *   <li>Não pode ser nulo nem vazio</li>
     *   <li>Deve conter '@' e '.'</li>
     *   <li>Tamanho máximo de 64 caracteres</li>
     * </ul>
     */
    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("⚠️ email não pode ser nulo.");
        }

        String emailTrimmed = email.trim();

        if (emailTrimmed.isEmpty()) {
            throw new IllegalArgumentException("⚠️ email não pode ser vazio.");
        }

        if (emailTrimmed.length() > 64) {
            throw new IllegalArgumentException("⚠️ email deve ter no máximo 64 caracteres.");
        }

        // 🧠 Validação simples de formato de email
        if (!emailTrimmed.contains("@") || !emailTrimmed.contains(".")) {
            throw new IllegalArgumentException("⚠️ email inválido.");
        }

        this.email = emailTrimmed;
    }

    /**
     * 📤 Retorna a senha do funcionário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 📥 Define a senha do funcionário.
     * 
     * ⚙️ Regras:
     * <ul>
     *   <li>Não pode ser nula</li>
     *   <li>Deve conter entre 6 e 64 caracteres</li>
     * </ul>
     */
    public void setSenha(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("⚠️ senha não pode ser nula.");
        }

        String senhaTrimmed = senha.trim();

        if (senhaTrimmed.length() < 6) {
            throw new IllegalArgumentException("⚠️ senha deve ter pelo menos 6 caracteres.");
        }

        if (senhaTrimmed.length() > 64) {
            throw new IllegalArgumentException("⚠️ senha deve ter no máximo 64 caracteres.");
        }

        this.senha = senhaTrimmed;
    }

    /**
     * 🚏 Verifica se o funcionário recebe vale transporte.
     */
    public boolean isRecebeValeTransporte() {
        return recebeValeTransporte;
    }

    /**
     * 🚌 Define se o funcionário recebe vale transporte.
     */
    public void setRecebeValeTransporte(boolean recebeValeTransporte) {
        this.recebeValeTransporte = recebeValeTransporte;
    }

    /**
     * 📤 Retorna o cargo associado ao funcionário.
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * 📥 Define o cargo do funcionário.
     * 
     * ⚙️ Regra: o cargo não pode ser nulo.
     */
    public void setCargo(Cargo cargo) {
        if (cargo == null) {
            throw new IllegalArgumentException("⚠️ cargo não pode ser nulo.");
        }
        this.cargo = cargo;
    }

    // =====================================================
    // 🧾 Métodos auxiliares
    // =====================================================

    /**
     * 🪪 Retorna uma representação textual do funcionário.
     * 
     * 🧠 Usado em logs, tabelas e depuração.
     */
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
