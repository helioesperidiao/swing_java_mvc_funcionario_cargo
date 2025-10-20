package forms;

import control.FuncionarioControl;
import dao.FuncionarioDAO;
import database.MysqlDatabase;
import model.Funcionario;
import service.FuncionarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;

public class LoginForm extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;

    private FuncionarioControl funcionarioControl;

    public LoginForm() {
        // Configura a conexão com o banco
        MysqlDatabase database = new MysqlDatabase(
                "127.0.0.1", "root", "", "gestao_rh", 3306);
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(database);
        FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);
        this.funcionarioControl = new FuncionarioControl(funcionarioService);

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login - Sistema RH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label Email
        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(200, 30)); // Aumenta o tamanho
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Campo ocupa mais espaço
        gbc.gridwidth = 2;
        panel.add(txtEmail, gbc);

        // Label Senha
        JLabel lblSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(lblSenha, gbc);

        txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(200, 30)); // Aumenta o tamanho
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        panel.add(txtSenha, gbc);

        // Botão Login
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 0;
        panel.add(btnLogin, gbc);

        // Ação do botão
        btnLogin.addActionListener(e -> realizarLogin());

        add(panel);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe email e senha!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Object> response = funcionarioControl.login(email, senha);

        if ((Boolean) response.get("success")) {
            JOptionPane.showMessageDialog(this, response.get("message"), "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Exemplo: mostrar o nome do funcionário logado
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String nomeFuncionario = (String) data.get("nomeFuncionario");
            System.out.println("Usuário logado: " + nomeFuncionario);
            MainForm main = new MainForm(nomeFuncionario);
            main.setVisible(true);
            // Aqui você poderia abrir o formulário principal do sistema
            // e fechar o login
            // this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, response.get("message"), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginForm().setVisible(true);
        });
    }
}
