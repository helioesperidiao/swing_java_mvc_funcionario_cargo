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

public class CargoForm extends JFrame {
    private JTextField txtPesquisa;
    private JTextField txtId;
    private JTextField txtNomeCargo;
    private JTable tabelaCargos;
    private DefaultTableModel tableModel;
    private JButton btnPesquisar;
    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    private CargoControl cargoControl;

    public CargoForm() {
        MysqlDatabase database = new MysqlDatabase(
                "127.0.0.1", "root", "", "gestao_rh", 3306);

        CargoDAO cargoDAO = new CargoDAO(database);
        // Inicializa o controlador
        CargoService cargoService = new CargoService(cargoDAO);
        this.cargoControl = new CargoControl(cargoService);

        initializeUI();
        carregarTodosCargos();
    }

    private void initializeUI() {
        setTitle("Sistema de Gerenciamento de Cargos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel do topo (pesquisa + formulário)
        JPanel panelTopo = criarPanelTopo();

        // Painel da tabela
        JPanel panelTabela = criarPanelTabela();

        // Painel de botões
        JPanel panelBotoes = criarPanelBotoes();

        // Adiciona os painéis ao painel principal
        mainPanel.add(panelTopo, BorderLayout.NORTH);
        mainPanel.add(panelTabela, BorderLayout.CENTER);
        mainPanel.add(panelBotoes, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel criarPanelTopo() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));

        // Painel de pesquisa
        JPanel panelPesquisa = criarPanelPesquisa();

        // Painel de formulário
        JPanel panelFormulario = criarPanelFormulario();

        // Adiciona os painéis ao topo
        panelTopo.add(panelPesquisa, BorderLayout.NORTH);
        panelTopo.add(panelFormulario, BorderLayout.CENTER);

        return panelTopo;
    }

    private JPanel criarPanelPesquisa() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Pesquisa"));
        panel.setPreferredSize(new Dimension(0, 70)); // Altura fixa

        JLabel lblPesquisa = new JLabel("Pesquisar:");
        txtPesquisa = new JTextField(20);
        btnPesquisar = new JButton("Pesquisar");

        panel.add(lblPesquisa);
        panel.add(txtPesquisa);
        panel.add(btnPesquisar);

        // Ação do botão pesquisar
        btnPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarCargos();
            }
        });

        // Ação do Enter no campo de pesquisa
        txtPesquisa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarCargos();
            }
        });

        return panel;
    }

    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Cargo"));
        panel.setPreferredSize(new Dimension(0, 100)); // Altura fixa

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        txtId.setEditable(false);

        JLabel lblNomeCargo = new JLabel("Nome do Cargo:");
        txtNomeCargo = new JTextField();

        // Linha 1 - ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        panel.add(lblId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.9;
        panel.add(txtId, gbc);

        // Linha 2 - Nome do Cargo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        panel.add(lblNomeCargo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.9;
        panel.add(txtNomeCargo, gbc);

        return panel;
    }

    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Cargos"));

        // Modelo da tabela
        String[] colunas = { "ID", "Nome do Cargo" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna a tabela não editável
            }
        };

        tabelaCargos = new JTable(tableModel);
        tabelaCargos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adiciona listener para seleção na tabela
        tabelaCargos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarCargoDaTabela();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaCargos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPanelBotoes() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        // Define tamanhos consistentes para os botões
        Dimension btnSize = new Dimension(100, 30);
        btnCadastrar.setPreferredSize(btnSize);
        btnAtualizar.setPreferredSize(btnSize);
        btnExcluir.setPreferredSize(btnSize);
        btnLimpar.setPreferredSize(btnSize);

        panel.add(btnCadastrar);
        panel.add(btnAtualizar);
        panel.add(btnExcluir);
        panel.add(btnLimpar);

        // Ações dos botões
        btnCadastrar.addActionListener(e -> cadastrarCargo());
        btnAtualizar.addActionListener(e -> atualizarCargo());
        btnExcluir.addActionListener(e -> excluirCargo());
        btnLimpar.addActionListener(e -> limparCampos());

        return panel;
    }

    private void carregarTodosCargos() {
        try {
            List<Cargo> cargos = cargoControl.index();
            this.atualizarTabela(cargos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar cargos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Opcional: para debug
        }
    }

    private void pesquisarCargos() {
        String termoPesquisa = txtPesquisa.getText().trim();

        if (termoPesquisa.isEmpty()) {
            carregarTodosCargos();
            return;
        }

        // Tenta pesquisar por ID
        try {
            int id = Integer.parseInt(termoPesquisa);

            // Assumindo que o método show também foi modificado para retornar Cargo
            // diretamente
            // Se não foi, você precisará ajustar esta parte
            try {
                Cargo cargo = cargoControl.show(id);
                if (cargo != null) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[] { cargo.getIdCargo(), cargo.getNomeCargo() });
                } else {
                    JOptionPane.showMessageDialog(this, "Cargo não encontrado!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao pesquisar cargo: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            // Se não for número, pesquisa por nome em todos os cargos
            try {
                List<Cargo> cargos = cargoControl.index();

                // Filtra os cargos pelo termo de pesquisa
                List<Cargo> cargosFiltrados = new ArrayList<>();
                for (Cargo cargo : cargos) {
                    if (cargo.getNomeCargo().toLowerCase().contains(termoPesquisa.toLowerCase())) {
                        cargosFiltrados.add(cargo);
                    }
                }
                atualizarTabela(cargosFiltrados);

                if (cargosFiltrados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhum cargo encontrado com o termo: " + termoPesquisa);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar cargos: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cadastrarCargo() {
        System.out.println("> CargoForm.cadastrarCargo()");
        String nomeCargo = txtNomeCargo.getText().trim();

        if (nomeCargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o nome do cargo!");
            return;
        }

        // Correção para versões anteriores do Java
        Map<String, Object> cargoData = new HashMap<>();
        cargoData.put("nomeCargo", nomeCargo);

        try {
            Cargo novoCargo = cargoControl.store(cargoData);
            JOptionPane.showMessageDialog(this, "Cargo cadastrado com sucesso" + novoCargo);
            limparCampos();
            carregarTodosCargos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar:" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar:" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void atualizarCargo() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cargo para atualizar!");
            return;
        }

        String nomeCargo = txtNomeCargo.getText().trim();

        if (nomeCargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o nome do cargo!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        Map<String, Object> response = cargoControl.update(id, nomeCargo);

        if ((Boolean) response.get("success")) {
            JOptionPane.showMessageDialog(this, response.get("message"));
            limparCampos();
            carregarTodosCargos();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + response.get("message"),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCargo() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cargo para excluir!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este cargo?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Map<String, Object> response = cargoControl.destroy(id);

            if ((Boolean) response.get("success")) {
                JOptionPane.showMessageDialog(this, response.get("message"));
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

    private void selecionarCargoDaTabela() {
        int selectedRow = tabelaCargos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            String nomeCargo = (String) tableModel.getValueAt(selectedRow, 1);

            txtId.setText(String.valueOf(id));
            txtNomeCargo.setText(nomeCargo);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNomeCargo.setText("");
        txtPesquisa.setText("");
        tabelaCargos.clearSelection();
    }

    private void atualizarTabela(List<Cargo> cargos) {
        tableModel.setRowCount(0);
        for (Cargo cargo : cargos) {
            tableModel.addRow(new Object[] { cargo.getIdCargo(), cargo.getNomeCargo() });
        }
    }

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