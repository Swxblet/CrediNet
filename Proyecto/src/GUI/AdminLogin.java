package GUI;

import BL.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame {
    LoginMenu loginGUI = new LoginMenu();
    private JTextField txtEmail;
    private int logoClickCount;
    public AdminLogin() {
        // Configuración base de la ventana
        setTitle("CrediNet Admin");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        //Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(207, 224, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Imagen del administrador
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        ImageIcon originalIcon = new ImageIcon("Proyecto/assets/img/adminIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));

        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoClickCount++;
                if (logoClickCount == 2) {
                    abrirLoginUsuario();
                }
            }
        });

        // Cargar fuente
        Font montserrat = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", 14);

        // Labels y campos de texto
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(montserrat);
        lblUser.setForeground(Color.BLACK);

        JTextField txtUsername = new JTextField(15);
        loginGUI.estilizarCampo(txtUsername);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(montserrat);
        lblEmail.setForeground(Color.BLACK);

        JTextField txtEmail = new JTextField(15);
        loginGUI.estilizarCampo(txtEmail);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(montserrat);
        lblPass.setForeground(Color.BLACK);

        JPasswordField txtPassword = new JPasswordField(15);
        loginGUI.estilizarCampo(txtPassword);

        // Botón de login
        JButton btnLogin = LoginMenu.getJButton(montserrat, 4, 10, 36);

        // Agregar elementos al panel principal
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(loginGUI.crearFilaCampo(lblUser, txtUsername,207, 224, 250));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(loginGUI.crearFilaCampo(lblEmail, txtEmail,207, 224, 250));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(loginGUI.crearFilaCampo(lblPass, txtPassword,207, 224, 250));
        mainPanel.add(Box.createVerticalStrut(25));

        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalStrut(20));

        add(mainPanel);
    }

    private void autenticarAdmin() {
        JOptionPane.showMessageDialog(this, "Autenticando usuario (placeholder)");
    }

    private void abrirLoginUsuario() {
        LoginMenu loginGUI = new LoginMenu();
        loginGUI.mostrar();
        logoClickCount = 0;
        dispose();
    }

    // Sirve para mostrar la ventana
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
