package GUI;

import BL.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginMenu extends JFrame {
    private int logoClickCount = 0;

    public LoginMenu() {
        // Configuración base de la ventana
        setTitle("CrediNet");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        //Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Imagen del usuario
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        ImageIcon originalIcon = new ImageIcon("Proyecto/assets/img/userIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));

        //Sirve para cuando se haga doble clic en la imagen abra el login de administrador
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoClickCount++;
                if (logoClickCount == 2) {
                    abrirLoginAdmin();
                }
            }
        });

        // Cargar fuente
        Font montserrat = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", 14);

        // Labels y campos de texto
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(montserrat);

        JTextField txtUsername = new JTextField(15);
        estilizarCampo(txtUsername);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(montserrat);

        JPasswordField txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);

        // Botón de login
        JButton btnLogin = getJButton(montserrat, 33, 150, 243);

        // Botón de registro
        JLabel lblRegister = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRegister.setForeground(new Color(70, 130, 180));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirRegistro(); // 🔹 placeholder
            }
        });

        // Agregar elementos al panel principal
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(crearFilaCampo(lblUser, txtUsername, 245, 245, 245));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearFilaCampo(lblPass, txtPassword, 245, 245, 245));
        mainPanel.add(Box.createVerticalStrut(25));

        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(lblRegister);

        add(mainPanel);
    }

    public static JButton getJButton(Font montserrat, int r, int g, int b) {
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(r, g, b));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(montserrat);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        btnLogin.addActionListener(e -> autenticarUsuario()); // placeholder
        return btnLogin;
    }

    // --- METHOD AUXILIAR PARA ALINEAR ETIQUETA + CAMPO ---
    public JPanel crearFilaCampo(JLabel etiqueta, JTextField campo, int r, int g, int b) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(new Color(r, g, b));

        etiqueta.setPreferredSize(new Dimension(100, 35));
        etiqueta.setMaximumSize(new Dimension(100, 35));
        etiqueta.setAlignmentY(Component.CENTER_ALIGNMENT);

        campo.setAlignmentY(Component.CENTER_ALIGNMENT);

        fila.add(etiqueta);
        fila.add(Box.createHorizontalStrut(10));
        fila.add(campo);

        return fila;
    }

    // Funciones
    private void abrirLoginAdmin() {
        AdminLogin adminLogin = new AdminLogin();
        adminLogin.mostrar();
        logoClickCount = 0;
        dispose();
    }

    private static void autenticarUsuario() {
        JOptionPane.showMessageDialog(null, "Autenticando usuario (placeholder)");
    }

    private void abrirRegistro() {
        JOptionPane.showMessageDialog(this, "Abrir pantalla de registro (placeholder)");
    }

    public void estilizarCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setMaximumSize(new Dimension(250, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    // Sirve para mostrar la ventana
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}