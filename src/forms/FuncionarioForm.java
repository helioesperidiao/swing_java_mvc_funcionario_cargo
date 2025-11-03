package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import control.FuncionarioControl;
import control.CargoControl;
import dao.FuncionarioDAO;
import dao.CargoDAO;
import database.MysqlDatabase;
import model.Funcionario;
import model.Cargo;
import service.FuncionarioService;
import service.CargoService;

/**
 * 🧩 Classe: FuncionarioForm
 * -------------------------------------------------
 * 📚 Responsável pela camada de *View* (interface gráfica)
 * do módulo de funcionários no padrão MVC-S.
 *
 * 🏗️ Arquitetura:
 * - View ➜ FuncionarioForm (interface Swing)
 * - Controller ➜ FuncionarioControl
 * - Service ➜ FuncionarioService
 * - DAO ➜ FuncionarioDAO
 * - Model ➜ Funcionario / Cargo
 *
 * ✅ Funcionalidades:
 *  - Cadastrar, listar, atualizar e excluir funcionários.
 *  - Pesquisar por nome ou e-mail.
 *  - Relacionar funcionário a um cargo existente.
 *  - Conectar ao banco MySQL.
 */
public class FuncionarioForm extends JFrame {

    // 🧱 Campos do formulário
    private JTextField txtPesquisa;
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtSenha;
    private JCheckBox chkValeTransporte;
    private JComboBox<Cargo> cbCargo;

    // 📋 Tabela de listagem
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;

    // 🔘 Botões
    private JButton btnPesquisar;
    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    // 🧩 Controllers (ligação com a lógica)
    private FuncionarioControl funcionarioControl;
    private CargoControl cargoControl;

    /**
     * 🚀 Construtor principal da tela de funcionários.
     * - Inicializa a conexão com o banco.
     * - Cria instâncias de DAO, Service e Control.
     * - Monta a interface e carrega dados iniciais.
     */
    public FuncionarioForm() {
        // 🗄️ Cria conexão com o banco MySQL
        MysqlDatabase database = new MysqlDatabase(
                "127.0.0.1", "root", "", "gestao_rh", 3306);

        // ⚙️ Injeta dependências manualmente
        CargoDAO cargoDAO = new CargoDAO(database);
        CargoService cargoService = new CargoService(cargoDAO);
        this.cargoControl = new CargoControl(cargoService);

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(database);
        FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);
        this.funcionarioControl = new FuncionarioControl(funcionarioService);

        // 🖥️ Inicializa a interface
        initializeUI();
        carregarTodosFuncionarios();
        carregarCargosCombo();
    }

    /**
     * 🎨 Configura toda a interface principal.
     */
    private void initializeUI() {
        setTitle("Sistema de Gerenciamento de Funcionários");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTopo = criarPanelTopo();
        JPanel panelTabela = criarPanelTabela();
        JPanel panelBotoes = criarPanelBotoes();

        mainPanel.add(panelTopo, BorderLayout.NORTH);
        mainPanel.add(panelTabela, BorderLayout.CENTER);
        mainPanel.add(panelBotoes, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 🧱 Cria o painel superior (pesquisa + formulário de dados).
     */
    private JPanel criarPanelTopo() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));

        // 🔍 Seção de pesquisa
        JPanel panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPesquisa.setBorder(BorderFactory.createTitledBorder("Pesquisa"));

        JLabel lblPesquisa = new JLabel("Pesquisar:");
        txtPesquisa = new JTextField(20);
        btnPesquisar = new JButton("Pesquisar");

        panelPesquisa.add(lblPesquisa);
        panelPesquisa.add(txtPesquisa);
        panelPesquisa.add(btnPesquisar);

        // 🖱️ Eventos de pesquisa
        btnPesquisar.addActionListener(e -> pesquisarFuncionarios());
        txtPesquisa.addActionListener(e -> pesquisarFuncionarios());

        // 📝 Seção de formulário
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        txtId.setEditable(false); // 🔒 Campo não editável

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JTextField();

        JLabel lblVale = new JLabel("Recebe Vale Transporte:");
        chkValeTransporte = new JCheckBox();

        JLabel lblCargo = new JLabel("Cargo:");
        cbCargo = new JComboBox<>();

        // 🧩 Adiciona os campos no grid
        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(lblNome, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelFormulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelFormulario.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(lblSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelFormulario.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(lblVale, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panelFormulario.add(chkValeTransporte, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelFormulario.add(lblCargo, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panelFormulario.add(cbCargo, gbc);

        panelTopo.add(panelPesquisa, BorderLayout.NORTH);
        panelTopo.add(panelFormulario, BorderLayout.CENTER);

        return panelTopo;
    }

    /**
     * 📋 Cria o painel central (tabela de listagem).
     */
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Funcionários"));

        String[] colunas = {"ID", "Nome", "Email", "Vale Transporte", "Cargo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaFuncionarios = new JTable(tableModel);
        tabelaFuncionarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 🖱️ Evento: seleção de linha
        tabelaFuncionarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarFuncionarioDaTabela();
        });

        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 🔘 Cria o painel inferior com os botões de ação.
     */
    private JPanel criarPanelBotoes() {
        JPanel panel = new JPanel(new FlowLayout());

        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        Dimension btnSize = new Dimension(120, 30);
        btnCadastrar.setPreferredSize(btnSize);
        btnAtualizar.setPreferredSize(btnSize);
        btnExcluir.setPreferredSize(btnSize);
        btnLimpar.setPreferredSize(btnSize);

        panel.add(btnCadastrar);
        panel.add(btnAtualizar);
        panel.add(btnExcluir);
        panel.add(btnLimpar);

        // ⚙️ Define as ações
        btnCadastrar.addActionListener(e -> cadastrarFuncionario());
        btnAtualizar.addActionListener(e -> atualizarFuncionario());
        btnExcluir.addActionListener(e -> excluirFuncionario());
        btnLimpar.addActionListener(e -> limparCampos());

        return panel;
    }

    /**
     * 🔄 Carrega todos os funcionários do banco.
     */
    private void carregarTodosFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioControl.index();
            atualizarTabela(funcionarios);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar funcionários: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🧠 Carrega os cargos disponíveis no comboBox.
     */
    private void carregarCargosCombo() {
        try {
            List<Cargo> cargos = cargoControl.index();
            cbCargo.removeAllItems();
            for (Cargo c : cargos) cbCargo.addItem(c);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cargos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🔍 Pesquisa funcionários por nome ou e-mail.
     */
    private void pesquisarFuncionarios() {
        String termo = txtPesquisa.getText().trim();
        if (termo.isEmpty()) {
            carregarTodosFuncionarios();
            return;
        }

        try {
            List<Funcionario> funcionarios = funcionarioControl.index();
            List<Funcionario> filtrados = new ArrayList<>();

            for (Funcionario f : funcionarios) {
                if (f.getNomeFuncionario().toLowerCase().contains(termo.toLowerCase()) ||
                    f.getEmail().toLowerCase().contains(termo.toLowerCase())) {
                    filtrados.add(f);
                }
            }

            atualizarTabela(filtrados);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao pesquisar funcionários: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ➕ Cadastra um novo funcionário.
     */
    private void cadastrarFuncionario() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();
        boolean vale = chkValeTransporte.isSelected();
        Cargo cargo = (Cargo) cbCargo.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Preencha todos os campos obrigatórios!");
            return;
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("nomeFuncionario", nome);
            data.put("email", email);
            data.put("senha", senha);
            data.put("recebeValeTransporte", vale);
            data.put("cargo", cargo);

            funcionarioControl.store(data);
            JOptionPane.showMessageDialog(this, "✅ Funcionário cadastrado com sucesso!");
            limparCampos();
            carregarTodosFuncionarios();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ✏️ Atualiza os dados de um funcionário existente.
     */
    private void atualizarFuncionario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para atualizar!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();
        boolean vale = chkValeTransporte.isSelected();
        Cargo cargo = (Cargo) cbCargo.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Preencha todos os campos obrigatórios!");
            return;
        }

        Map<String, Object> response = funcionarioControl.update(id, nome, email, senha, vale, cargo);
        if ((Boolean) response.get("success")) {
            JOptionPane.showMessageDialog(this, "✅ " + response.get("message"));
            limparCampos();
            carregarTodosFuncionarios();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + response.get("message"),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🗑️ Exclui um funcionário selecionado.
     */
    private void excluirFuncionario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para excluir!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this,
                "❓ Deseja realmente excluir este funcionário?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Map<String, Object> response = funcionarioControl.destroy(id);
            if ((Boolean) response.get("success")) {
                JOptionPane.showMessageDialog(this, "🗑️ " + response.get("message"));
                limparCampos();
                carregarTodosFuncionarios();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir: " + response.get("message"),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 🖱️ Ao clicar na tabela, preenche o formulário com os dados selecionados.
     */
    private void selecionarFuncionarioDaTabela() {
        int row = tabelaFuncionarios.getSelectedRow();
        if (row != -1) {
            txtId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
            txtNome.setText((String) tableModel.getValueAt(row, 1));
            txtEmail.setText((String) tableModel.getValueAt(row, 2));
            chkValeTransporte.setSelected((Boolean) tableModel.getValueAt(row, 3));

            // 🔄 Seleciona o cargo correto no comboBox
            String nomeCargo = (String) tableModel.getValueAt(row, 4);
            for (int i = 0; i < cbCargo.getItemCount(); i++) {
                if (cbCargo.getItemAt(i).getNomeCargo().equals(nomeCargo)) {
                    cbCargo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * 🧼 Limpa os campos e reseta a seleção da tabela.
     */
    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        chkValeTransporte.setSelected(false);
        cbCargo.setSelectedIndex(-1);
        txtPesquisa.setText("");
        tabelaFuncionarios.clearSelection();
    }

    /**
     * 🔄 Atualiza a tabela com a lista de funcionários.
     */
    private void atualizarTabela(List<Funcionario> funcionarios) {
        tableModel.setRowCount(0);
        for (Funcionario f : funcionarios) {
            tableModel.addRow(new Object[]{
                    f.getIdFuncionario(),
                    f.getNomeFuncionario(),
                    f.getEmail(),
                    f.isRecebeValeTransporte(),
                    f.getCargo() != null ? f.getCargo().getNomeCargo() : ""
            });
        }
    }

    /**
     * ▶️ Ponto de entrada da aplicação Swing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { e.printStackTrace(); }
            new FuncionarioForm().setVisible(true);
        });
    }
}
