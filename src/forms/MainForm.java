package forms;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MainForm extends JFrame {

    public MainForm(String nomeUsuario) {
        setTitle("Sistema RH - Usuário: " + nomeUsuario);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu
        setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastro = new JMenu("Cadastro");

        JMenuItem menuCargo = new JMenuItem(new AbstractAction("Cargos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCargoForm();
            }
        });

        JMenuItem menuFuncionario = new JMenuItem(new AbstractAction("Funcionários") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFuncionarioForm();
            }
        });

        menuCadastro.add(menuCargo);
        menuCadastro.add(menuFuncionario);

        menuBar.add(menuCadastro);
        return menuBar;
    }

    private void abrirCargoForm() {
        // Apenas cria e exibe o JFrame
        CargoForm cargoForm = new CargoForm();
        cargoForm.setVisible(true);
        cargoForm.toFront();
    }

    private void abrirFuncionarioForm() {
        FuncionarioForm funcionarioForm = new FuncionarioForm();
        funcionarioForm.setVisible(true);
        funcionarioForm.toFront();
    }

    // Main para testar
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainForm("Administrador").setVisible(true);
        });
    }
}
