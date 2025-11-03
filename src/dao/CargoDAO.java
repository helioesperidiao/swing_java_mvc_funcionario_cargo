package dao;

import database.MysqlDatabase;
import model.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 💾 Classe responsável por realizar todas as operações de acesso ao banco de
 * dados
 * relacionadas à entidade {@link Cargo}.
 *
 * 🔹 Esta é a **camada DAO (Data Access Object)** do padrão MVC.
 * Ela tem como função principal **isolar a lógica de persistência**, ou seja,
 * tudo que envolve comandos SQL, conexões, inserções, consultas, exclusões,
 * etc.
 *
 * 🧠 Conceito didático:
 * - O DAO conversa diretamente com o banco (camada mais “baixa” do sistema).
 * - O Service chama o DAO.
 * - O Controller chama o Service.
 *
 * ⚙️ Padrão de chamada:
 * Controller → Service → DAO → Banco de Dados
 *
 * Nenhuma regra de negócio é implementada aqui, apenas operações CRUD.
 */
public class CargoDAO {

    /**
     * Instância da classe de conexão com o banco.
     * O DAO depende dela para obter a conexão JDBC.
     */
    private final MysqlDatabase database;

    /**
     * 🔧 Construtor com injeção de dependência.
     * Recebe uma instância de {@link MysqlDatabase} já configurada.
     *
     * @param databaseInstance instância responsável por fornecer conexões com o
     *                         banco.
     */
    public CargoDAO(MysqlDatabase databaseInstance) {
        System.out.println(">>>> CargoDAO.constructor()");
        this.database = databaseInstance;
    }

    /**
     * 🆕 Cria (insere) um novo cargo no banco de dados.
     *
     * Fluxo:
     * 1️⃣ Abre uma conexão com o banco.
     * 2️⃣ Prepara o comando SQL com parâmetros.
     * 3️⃣ Executa a inserção.
     * 4️⃣ Obtém o ID gerado automaticamente (chave primária).
     *
     * @param objCargoModel objeto contendo os dados do novo cargo.
     * @return ID gerado para o cargo inserido.
     * @throws SQLException se ocorrer erro na execução do SQL.
     */
    public int create(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.create()");
        String SQL = "INSERT INTO cargo (nomeCargo) VALUES (?);";

        // 1️⃣ Obter conexão com o banco
        Connection conn = database.getConnection();

        // 2️⃣ Preparar o comando SQL com retorno de chave gerada
        PreparedStatement stmt = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, objCargoModel.getNomeCargo());

        // 3️⃣ Executar o comando
        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 0) {
            stmt.close();
            throw new SQLException("❌ Falha ao inserir cargo (nenhuma linha afetada).");
        }

        // 4️⃣ Capturar o ID gerado automaticamente
        ResultSet rs = stmt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        // 5️⃣ Fechar recursos
        rs.close();
        stmt.close();

        // 6️⃣ Validar se o ID foi obtido corretamente
        if (id == -1) {
            throw new SQLException("❌ Falha ao obter ID do cargo inserido.");
        }

        System.out.println("✅ Cargo inserido com ID: " + id);
        return id;
    }

    /**
     * 🗑️ Deleta um cargo existente com base em seu ID.
     *
     * @param objCargoModel objeto Cargo contendo o ID a ser excluído.
     * @return true se o registro foi excluído, false caso contrário.
     * @throws SQLException se ocorrer erro ao executar o SQL.
     */
    public boolean delete(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.delete()");
        String SQL = "DELETE FROM cargo WHERE idCargo = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setInt(1, objCargoModel.getIdCargo());

        // Executa e retorna se alguma linha foi afetada
        int affectedRows = stmt.executeUpdate();
        stmt.close();

        System.out.println(affectedRows > 0 ? "✅ Cargo excluído!" : "⚠️ Nenhum cargo encontrado para exclusão.");
        return affectedRows > 0;
    }

    /**
     * ✏️ Atualiza o nome de um cargo existente.
     *
     * @param objCargoModel objeto Cargo contendo o ID e o novo nome.
     * @return true se o cargo foi atualizado com sucesso.
     * @throws SQLException se ocorrer erro no banco.
     */
    public boolean update(Cargo objCargoModel) throws SQLException {
        System.out.println(">>>> CargoDAO.update()");
        String SQL = "UPDATE cargo SET nomeCargo = ? WHERE idCargo = ?;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.setString(1, objCargoModel.getNomeCargo());
        stmt.setInt(2, objCargoModel.getIdCargo());

        int affectedRows = stmt.executeUpdate();
        stmt.close();

        System.out.println(affectedRows > 0 ? "✅ Cargo atualizado!" : "⚠️ Cargo não encontrado para atualização.");
        return affectedRows > 0;
    }

    /**
     * 📋 Retorna todos os cargos cadastrados no banco.
     *
     * @return Lista com todos os objetos Cargo encontrados.
     * @throws SQLException se ocorrer erro de consulta.
     */
    public List<Cargo> findAll() throws SQLException {
        System.out.println(">>>> CargoDAO.findAll()");
        String SQL = "SELECT * FROM cargo;";

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        // 🧱 Criação da lista que armazenará os cargos retornados
        List<Cargo> cargos = new ArrayList<>();

        // 🔁 Percorre todos os registros do banco
        while (rs.next()) {
            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            cargos.add(c);
        }

        // 🔒 Fecha os recursos para evitar vazamento de memória
        rs.close();
        stmt.close();

        System.out.println("📦 Total de cargos encontrados: " + cargos.size());
        return cargos;
    }

    /**
     * 🔍 Busca um cargo específico pelo seu ID.
     *
     * Internamente usa o método genérico {@link #findByField(String, Object)}.
     *
     * @param idCargo identificador do cargo.
     * @return Cargo encontrado ou null se não existir.
     * @throws SQLException em caso de erro no SQL.
     */
    public Cargo findById(int idCargo) throws SQLException {
        List<Cargo> result = findByField("idCargo", idCargo);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 🔎 Busca cargos por um campo específico (idCargo ou nomeCargo).
     *
     * Este método é **genérico**, ou seja, pode buscar por diferentes colunas,
     * desde que sejam permitidas.
     *
     * @param field nome do campo (ex: "idCargo" ou "nomeCargo").
     * @param value valor a ser buscado.
     * @return lista de cargos encontrados.
     * @throws SQLException se o campo ou o tipo forem inválidos.
     */
    public List<Cargo> findByField(String field, Object value) throws SQLException {
        System.out.println(">>>> CargoDAO.findByField() - Campo: " + field + ", Valor: " + value);

        // 🧩 Validação do campo permitido
        if (!field.equals("idCargo") && !field.equals("nomeCargo")) {
            throw new SQLException("⚠️ Campo inválido para busca: " + field);
        }

        String SQL = "SELECT * FROM cargo WHERE " + field + " = ?;";
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL);

        // 🔧 Define o tipo de parâmetro dinamicamente
        if (value instanceof Integer) {
            stmt.setInt(1, (Integer) value);
        } else if (value instanceof String) {
            stmt.setString(1, (String) value);
        } else {
            stmt.close();
            throw new SQLException("⚠️ Tipo de valor inválido para busca.");
        }

        ResultSet rs = stmt.executeQuery();
        List<Cargo> cargos = new ArrayList<>();

        // 🔁 Converte cada linha do resultado em um objeto Cargo
        while (rs.next()) {
            Cargo c = new Cargo();
            c.setIdCargo(rs.getInt("idCargo"));
            c.setNomeCargo(rs.getString("nomeCargo"));
            cargos.add(c);
        }

        rs.close();
        stmt.close();

        System.out.println("📦 Resultados encontrados: " + cargos.size());
        return cargos;
    }
}
