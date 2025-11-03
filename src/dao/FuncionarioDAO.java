package dao;

import org.mindrot.jbcrypt.BCrypt;

import database.MysqlDatabase;
import model.Funcionario;
import model.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 💼 Classe responsável por realizar todas as operações no banco de dados
 * relacionadas à entidade {@link Funcionario}.
 *
 * 🔹 Esta classe implementa o **padrão DAO (Data Access Object)**,
 * isolando toda a lógica de persistência (SQL, conexões, etc.)
 * para manter o código limpo e modular.
 *
 * 🧠 Arquitetura MVC:
 * Controller → Service → DAO → Banco de Dados
 *
 * ⚙️ Aqui ficam apenas comandos SQL (CRUD), sem regras de negócio.
 * Exemplo de regra de negócio: validação de e-mail único → Service.
 */
public class FuncionarioDAO {

    /**
     * Instância da classe responsável pela conexão com o banco de dados.
     */
    private final MysqlDatabase database;

    /**
     * 🔧 Construtor com injeção de dependência.
     * 
     * @param databaseInstance instância que fornece conexões MySQL.
     */
    public FuncionarioDAO(MysqlDatabase databaseInstance) {
        System.out.println(">>>> FuncionarioDAO.constructor()");
        this.database = databaseInstance;
    }

    // =========================
    // 🆕 CREATE
    // =========================
    /**
     * Insere um novo funcionário no banco de dados.
     *
     * ⚙️ Passos:
     * 1️⃣ Gera o hash da senha (usando BCrypt).
     * 2️⃣ Prepara o comando SQL com os parâmetros.
     * 3️⃣ Executa o comando e retorna o ID gerado.
     *
     * @param objFuncionario objeto contendo os dados do novo funcionário.
     * @return ID do funcionário criado.
     * @throws SQLException se ocorrer erro ao executar o comando SQL.
     */
    public int create(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.create()");

        // 🔐 Gera hash seguro da senha antes de salvar no banco
        String hashedPassword = BCrypt.hashpw(objFuncionario.getSenha(), BCrypt.gensalt(12));

        String SQL = " INSERT INTO Funcionario (nomeFuncionario, email, senha, recebeValeTransporte, Cargo_idCargo) VALUES (?, ?, ?, ?, ?);";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, objFuncionario.getNomeFuncionario());
        stmt.setString(2, objFuncionario.getEmail());
        stmt.setString(3, hashedPassword); // ⚠️ Nunca salve senhas em texto puro!
        stmt.setBoolean(4, objFuncionario.isRecebeValeTransporte());
        stmt.setInt(5, objFuncionario.getCargo().getIdCargo());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            stmt.close();
            throw new SQLException("❌ Falha ao inserir funcionário (nenhuma linha afetada).");
        }

        // Obtém o ID gerado
        ResultSet rs = stmt.getGeneratedKeys();
        int id = -1;
        if (rs.next())
            id = rs.getInt(1);

        rs.close();
        stmt.close();

        if (id == -1)
            throw new SQLException("❌ Falha ao obter ID do funcionário inserido.");

        System.out.println("✅ Funcionário inserido com ID: " + id);
        return id;
    }

    // =========================
    // 🗑️ DELETE
    // =========================
    /**
     * Exclui um funcionário com base no ID.
     *
     * @param objFuncionario objeto contendo o ID do funcionário.
     * @return true se foi excluído, false caso contrário.
     * @throws SQLException se houver erro no banco.
     */
    public boolean delete(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.delete()");
        String SQL = "DELETE FROM Funcionario WHERE idFuncionario = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setInt(1, objFuncionario.getIdFuncionario());

        int affectedRows = stmt.executeUpdate();
        stmt.close();

        System.out.println(affectedRows > 0 ? "✅ Funcionário excluído." : "⚠️ Nenhum funcionário encontrado.");
        return affectedRows > 0;
    }

    // =========================
    // ✏️ UPDATE
    // =========================
    /**
     * Atualiza os dados de um funcionário existente.
     *
     * ⚠️ Observação: aqui a senha deve já estar criptografada (ou vir do hash
     * original).
     * Se for alterar senha, deve-se aplicar o BCrypt no Service antes de salvar.
     *
     * @param objFuncionario objeto com os novos dados do funcionário.
     * @return true se atualizado com sucesso.
     * @throws SQLException se houver erro no SQL.
     */
    public boolean update(Funcionario objFuncionario) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.update()");

        String SQL = "UPDATE Funcionario "
                   + "SET nomeFuncionario = ?, email = ?, senha = ?, recebeValeTransporte = ?, Cargo_idCargo = ? "
                   + "WHERE idFuncionario = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        stmt.setString(1, objFuncionario.getNomeFuncionario());
        stmt.setString(2, objFuncionario.getEmail());
        stmt.setString(3, objFuncionario.getSenha());
        stmt.setBoolean(4, objFuncionario.isRecebeValeTransporte());
        stmt.setInt(5, objFuncionario.getCargo().getIdCargo());
        stmt.setInt(6, objFuncionario.getIdFuncionario());

        int affectedRows = stmt.executeUpdate();
        stmt.close();

        System.out.println(affectedRows > 0 ? "✅ Funcionário atualizado." : "⚠️ Funcionário não encontrado.");
        return affectedRows > 0;
    }

    // =========================
    // 📋 FIND ALL
    // =========================
    /**
     * Retorna todos os funcionários cadastrados no banco.
     *
     * 🧩 Inclui o nome e o ID do cargo, utilizando JOIN.
     *
     * @return lista de funcionários completos.
     * @throws SQLException se ocorrer erro na consulta.
     */
    public List<Funcionario> findAll() throws SQLException {
        System.out.println(">>>> FuncionarioDAO.findAll()");

        String SQL = 
            "SELECT f.*, c.idCargo, c.nomeCargo " +
            "FROM Funcionario f " +
            "JOIN Cargo c ON f.Cargo_idCargo = c.idCargo;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        List<Funcionario> funcionarios = new ArrayList<>();

        while (rs.next()) {
            Funcionario f = new Funcionario();
            f.setIdFuncionario(rs.getInt("idFuncionario"));
            f.setNomeFuncionario(rs.getString("nomeFuncionario"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));

            // 🔗 Associação com Cargo
            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            f.setCargo(c);

            funcionarios.add(f);
        }

        rs.close();
        stmt.close();

        System.out.println("📦 Total de funcionários encontrados: " + funcionarios.size());
        return funcionarios;
    }

    // =========================
    // 🔍 FIND BY ID
    // =========================
    /**
     * Busca um funcionário específico pelo seu ID.
     *
     * @param idFuncionario identificador único.
     * @return objeto {@link Funcionario} ou null se não encontrado.
     * @throws SQLException se houver erro na execução da query.
     */
    public Funcionario findById(int idFuncionario) throws SQLException {
        List<Funcionario> result = findByField("idFuncionario", idFuncionario);
        return result.isEmpty() ? null : result.get(0);
    }

    // =========================
    // 🔎 FIND BY FIELD
    // =========================
    /**
     * Busca funcionários por um campo específico (como id, nome, email, etc).
     *
     * 🧠 Este método é genérico e pode ser reutilizado para várias consultas.
     *
     * @param field nome do campo (idFuncionario, nomeFuncionario, email,
     *              Cargo_idCargo).
     * @param value valor a ser buscado.
     * @return lista de funcionários correspondentes.
     * @throws SQLException se o campo ou tipo forem inválidos.
     */
    public List<Funcionario> findByField(String field, Object value) throws SQLException {
        System.out.println(">>>> FuncionarioDAO.findByField() - Campo: " + field + ", Valor: " + value);

        // ✅ Validação do campo permitido
        if (!field.equals("idFuncionario") &&
                !field.equals("nomeFuncionario") &&
                !field.equals("email") &&
                !field.equals("Cargo_idCargo")) {
            throw new SQLException("⚠️ Campo inválido para busca: " + field);
        }

        String SQL = 
                "SELECT f.*, c.idCargo, c.nomeCargo " +
                "FROM Funcionario f " +
                "JOIN Cargo c ON f.Cargo_idCargo = c.idCargo " +
                "WHERE f." + field + " = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        // 🔧 Define o tipo de parâmetro dinamicamente
        if (value instanceof Integer) {
            stmt.setInt(1, (Integer) value);
        } else if (value instanceof String) {
            stmt.setString(1, (String) value);
        } else if (value instanceof Boolean) {
            stmt.setBoolean(1, (Boolean) value);
        } else {
            stmt.close();
            throw new SQLException("⚠️ Tipo de valor inválido para busca.");
        }

        ResultSet rs = stmt.executeQuery();
        List<Funcionario> funcionarios = new ArrayList<>();

        while (rs.next()) {
            Funcionario f = new Funcionario();
            f.setIdFuncionario(rs.getInt("idFuncionario"));
            f.setNomeFuncionario(rs.getString("nomeFuncionario"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setRecebeValeTransporte(rs.getBoolean("recebeValeTransporte"));

            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            f.setCargo(c);

            funcionarios.add(f);
        }

        rs.close();
        stmt.close();

        System.out.println("📦 Funcionários encontrados: " + funcionarios.size());
        return funcionarios;
    }
}
