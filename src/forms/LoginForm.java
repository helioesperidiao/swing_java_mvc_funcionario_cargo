package forms;

import control.FuncionarioControl;
import dao.FuncionarioDAO;
import database.MysqlDatabase;
import service.FuncionarioService;

import javax.swing.*;

import java.awt.*;
import java.util.Map;

/**
 * 🧑‍💼 LoginForm
 *
 * <p>
 * Janela de login do sistema de Gestão de RH.
 * </p>
 *
 * <p>
 * Usa layout <b>null</b> (layout absoluto) para posicionamento manual dos
 * componentes.
 * </p>
 * 
 * 🧩 Padrão de arquitetura: MVC + Service + DAO.
 */
public class LoginForm extends JFrame {

    // 🧱 Componentes de interface
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;

    // 🎛️ Controller responsável pelo login
    private FuncionarioControl funcionarioControl;

    /**
     * 🚀 Construtor — Inicializa dependências e interface
     */
    public LoginForm() {
        // ⚙️ Configuração do banco
        MysqlDatabase database = new MysqlDatabase("127.0.0.1", "root", "", "gestao_rh", 3306);
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(database);
        FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);
        this.funcionarioControl = new FuncionarioControl(funcionarioService);

        // 🧩 Inicializa interface
        initializeUI();
    }

    /**
     * 🎨 Cria a tela de login usando layout absoluto (null layout)
     */
    private void initializeUI() {
        setTitle("Login - Sistema RH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null); // Centraliza a janela
        setResizable(false);

        // 🔲 Define layout absoluto
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250)); // cor suave de fundo

        // 🏷️ Título
        JLabel lblTitulo = new JLabel("Acesso ao Sistema RH", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBounds(100, 20, 300, 30);
        getContentPane().add(lblTitulo);

        // 📧 Label Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(80, 80, 100, 25);
        getContentPane().add(lblEmail);

        // 📨 Campo Email
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(160, 80, 250, 30); // largura maior
        getContentPane().add(txtEmail);

        // 🔑 Label Senha
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSenha.setBounds(80, 130, 100, 25);
        getContentPane().add(lblSenha);

        // 🔒 Campo Senha
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBounds(160, 130, 250, 30); // largura maior
        getContentPane().add(txtSenha);

        // 🔓 Botão Login
        btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(190, 190, 120, 35);
        getContentPane().add(btnLogin);

        // 🖱️ Ação do botão
        btnLogin.addActionListener(e -> realizarLogin());
    }

    /**
     * 🔐 Realiza a autenticação do usuário
     */
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

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String nomeFuncionario = (String) data.get("nomeFuncionario");

            System.out.println("Usuário logado: " + nomeFuncionario);

            MainForm main = new MainForm(nomeFuncionario);
            main.setVisible(true);

            // Fecha o login (opcional)
            // this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, response.get("message"), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 🏁 Método principal — executa a tela de login
     */
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
