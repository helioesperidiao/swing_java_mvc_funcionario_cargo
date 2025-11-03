package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import control.CargoControl;
import dao.CargoDAO;
import database.MysqlDatabase;
import model.Cargo;
import service.CargoService;

/**
 * 🧩 Classe: CargoForm
 * ----------------------------------------
 * 📚 Responsável pela interface gráfica (camada View) do sistema de cargos.
 * Faz parte do padrão MVC-S:
 * 
 * 🖥️ View → CargoForm (interface)
 * ⚙️ Controller → CargoControl
 * 🧠 Service → CargoService (regras de negócio)
 * 💾 DAO → CargoDAO (acesso ao banco)
 *
 * ✅ Permite:
 *  - Cadastrar, listar, atualizar e excluir cargos.
 *  - Pesquisar por ID ou nome.
 *  - Interagir com o banco de dados via MySQL.
 */
public class CargoForm extends JFrame {

    // 🧱 Campos da tela
    private JTextField txtPesquisa;
    private JTextField txtId;
    private JTextField txtNomeCargo;
    private JTable tabelaCargos;
    private DefaultTableModel tableModel;

    // 🔘 Botões de ação
    private JButton btnPesquisar;
    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    // 🧩 Controlador principal (liga a view à lógica)
    private CargoControl cargoControl;

    /**
     * 🚀 Construtor principal.
     * - Cria a conexão com o banco.
     * - Instancia DAO, Service e Control.
     * - Monta a interface e carrega os dados iniciais.
     */
    public CargoForm() {
        // 🗄️ Configuração da conexão MySQL
        MysqlDatabase database = new MysqlDatabase(
                "127.0.0.1", "root", "", "gestao_rh", 3306);

        // 🔁 Injeção manual de dependências
        CargoDAO cargoDAO = new CargoDAO(database);
        CargoService cargoService = new CargoService(cargoDAO);
        this.cargoControl = new CargoControl(cargoService);

        // 🎨 Inicializa a interface e carrega dados
        initializeUI();
        carregarTodosCargos();
    }

    /**
     * 🎨 Método responsável por montar toda a interface gráfica principal.
     */
    private void initializeUI() {
        setTitle("Sistema de Gerenciamento de Cargos");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela

        // 🧱 Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🧩 Criação dos subpainéis
        JPanel panelTopo = criarPanelTopo();
        JPanel panelTabela = criarPanelTabela();
        JPanel panelBotoes = criarPanelBotoes();

        // 🧩 Montagem da estrutura principal
        mainPanel.add(panelTopo, BorderLayout.NORTH);
        mainPanel.add(panelTabela, BorderLayout.CENTER);
        mainPanel.add(panelBotoes, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 🧱 Cria o painel superior (pesquisa + formulário).
     */
    private JPanel criarPanelTopo() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));

        JPanel panelPesquisa = criarPanelPesquisa();
        JPanel panelFormulario = criarPanelFormulario();

        panelTopo.add(panelPesquisa, BorderLayout.NORTH);
        panelTopo.add(panelFormulario, BorderLayout.CENTER);

        return panelTopo;
    }

    /**
     * 🔍 Cria o painel de pesquisa de cargos.
     */
    private JPanel criarPanelPesquisa() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Pesquisa"));
        panel.setPreferredSize(new Dimension(0, 70));

        JLabel lblPesquisa = new JLabel("Pesquisar:");
        txtPesquisa = new JTextField(20);
        btnPesquisar = new JButton("Pesquisar");

        panel.add(lblPesquisa);
        panel.add(txtPesquisa);
        panel.add(btnPesquisar);

        // 🖱️ Botão "Pesquisar"
        btnPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarCargos();
            }
        });

        // ⌨️ Enter também aciona a pesquisa
        txtPesquisa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarCargos();
            }
        });

        return panel;
    }

    /**
     * 📝 Cria o painel de formulário de cadastro/edição.
     */
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Cargo"));
        panel.setPreferredSize(new Dimension(0, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        txtId.setEditable(false); // 🔒 ID não pode ser editado

        JLabel lblNomeCargo = new JLabel("Nome do Cargo:");
        txtNomeCargo = new JTextField();

        // Linha 1 - ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblId, gbc);

        gbc.gridx = 1;
        panel.add(txtId, gbc);

        // Linha 2 - Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblNomeCargo, gbc);

        gbc.gridx = 1;
        panel.add(txtNomeCargo, gbc);

        return panel;
    }

    /**
     * 📋 Cria a tabela de exibição de cargos.
     */
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Cargos"));

        // 🧾 Colunas da tabela
        String[] colunas = {"ID", "Nome do Cargo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 🔒 Impede edição direta
            }
        };

        tabelaCargos = new JTable(tableModel);
        tabelaCargos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 🖱️ Evento de seleção
        tabelaCargos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarCargoDaTabela();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaCargos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 🔘 Cria o painel inferior com os botões de ação.
     */
    private JPanel criarPanelBotoes() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        // 📏 Define tamanho padrão
        Dimension btnSize = new Dimension(100, 30);
        btnCadastrar.setPreferredSize(btnSize);
        btnAtualizar.setPreferredSize(btnSize);
        btnExcluir.setPreferredSize(btnSize);
        btnLimpar.setPreferredSize(btnSize);

        panel.add(btnCadastrar);
        panel.add(btnAtualizar);
        panel.add(btnExcluir);
        panel.add(btnLimpar);

        // ⚙️ Ações dos botões
        btnCadastrar.addActionListener(e -> cadastrarCargo());
        btnAtualizar.addActionListener(e -> atualizarCargo());
        btnExcluir.addActionListener(e -> excluirCargo());
        btnLimpar.addActionListener(e -> limparCampos());

        return panel;
    }

    /**
     * 🔄 Carrega todos os cargos do banco.
     */
    private void carregarTodosCargos() {
        try {
            List<Cargo> cargos = cargoControl.index();
            atualizarTabela(cargos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cargos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🔍 Pesquisa cargos por ID ou nome.
     */
    private void pesquisarCargos() {
        String termoPesquisa = txtPesquisa.getText().trim();

        if (termoPesquisa.isEmpty()) {
            carregarTodosCargos();
            return;
        }

        // 🧠 Pesquisa por ID
        try {
            int id = Integer.parseInt(termoPesquisa);
            Cargo cargo = cargoControl.show(id);
            tableModel.setRowCount(0);
            if (cargo != null) {
                tableModel.addRow(new Object[]{cargo.getIdCargo(), cargo.getNomeCargo()});
            } else {
                JOptionPane.showMessageDialog(this, "Cargo não encontrado!");
            }
        } catch (NumberFormatException e) {
            // 🔤 Pesquisa por nome
            try {
                List<Cargo> cargos = cargoControl.index();
                List<Cargo> filtrados = new ArrayList<>();
                for (Cargo c : cargos) {
                    if (c.getNomeCargo().toLowerCase().contains(termoPesquisa.toLowerCase())) {
                        filtrados.add(c);
                    }
                }
                atualizarTabela(filtrados);
                if (filtrados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhum cargo encontrado com o termo: " + termoPesquisa);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao pesquisar cargos: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * ➕ Cadastra um novo cargo.
     */
    private void cadastrarCargo() {
        String nomeCargo = txtNomeCargo.getText().trim();
        if (nomeCargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o nome do cargo!");
            return;
        }

        Map<String, Object> cargoData = new HashMap<>();
        cargoData.put("nomeCargo", nomeCargo);

        try {
            Cargo novoCargo = cargoControl.store(cargoData);
            JOptionPane.showMessageDialog(this, "✅ Cargo cadastrado com sucesso!");
            limparCampos();
            carregarTodosCargos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ✏️ Atualiza um cargo selecionado.
     */
    private void atualizarCargo() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cargo para atualizar!");
            return;
        }

        String nomeCargo = txtNomeCargo.getText().trim();
        if (nomeCargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cargo!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        Map<String, Object> response = cargoControl.update(id, nomeCargo);

        if ((Boolean) response.get("success")) {
            JOptionPane.showMessageDialog(this, "✅ " + response.get("message"));
            limparCampos();
            carregarTodosCargos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + response.get("message"),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🗑️ Exclui um cargo selecionado.
     */
    private void excluirCargo() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cargo para excluir!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este cargo?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Map<String, Object> response = cargoControl.destroy(id);

            if ((Boolean) response.get("success")) {
                JOptionPane.showMessageDialog(this, "🗑️ " + response.get("message"));
                limparCampos();
                carregarTodosCargos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir: " + response.get("message"),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 🖱️ Preenche o formulário ao clicar em uma linha da tabela.
     */
    private void selecionarCargoDaTabela() {
        int selectedRow = tabelaCargos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            String nomeCargo = (String) tableModel.getValueAt(selectedRow, 1);

            txtId.setText(String.valueOf(id));
            txtNomeCargo.setText(nomeCargo);
        }
    }

    /**
     * 🧼 Limpa os campos e reseta a seleção.
     */
    private void limparCampos() {
        txtId.setText("");
        txtNomeCargo.setText("");
        txtPesquisa.setText("");
        tabelaCargos.clearSelection();
    }

    /**
     * 🔄 Atualiza os dados na tabela.
     */
    private void atualizarTabela(List<Cargo> cargos) {
        tableModel.setRowCount(0);
        for (Cargo cargo : cargos) {
            tableModel.addRow(new Object[]{cargo.getIdCargo(), cargo.getNomeCargo()});
        }
    }

    /**
     * ▶️ Método main — ponto de entrada da aplicação Swing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new CargoForm().setVisible(true);
        });
    }
}
