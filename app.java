import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import forms.LoginForm;

public class app {
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
