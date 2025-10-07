package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblRegister;
    private JLabel lblLogo;
    private int logoClickCount = 0;

    public LoginGUI() {
        // Configuración base de la ventana
        setTitle("Banco BEC - Login");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PANEL PRINCIPAL ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // --- LOGO ---
        lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        ImageIcon originalIcon = new ImageIcon("Proyecto/assets/img/logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));

        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoClickCount++;
                if (logoClickCount == 2) {
                    abrirLoginAdmin(); // 🔹 placeholder
                    logoClickCount = 0;
                }
            }
        });

        // --- FUENTE ---
        Font montserrat = cargarFuenteMontserrat(16f); // 🔹 placeholder

        // --- LABELS Y CAMPOS ---
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(montserrat);

        txtUsername = new JTextField(15);
        estilizarCampo(txtUsername);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(montserrat);

        txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);

        // --- BOTÓN LOGIN ---
        btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(montserrat);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        btnLogin.addActionListener(e -> autenticarUsuario()); // 🔹 placeholder

        // --- REGISTRO ---
        lblRegister = new JLabel("¿No tienes cuenta? Regístrate aquí");
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

        // --- AGREGAR COMPONENTES ---
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(crearFilaCampo(lblUser, txtUsername));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearFilaCampo(lblPass, txtPassword));
        mainPanel.add(Box.createVerticalStrut(25));

        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(lblRegister);

        add(mainPanel);
    }

    // --- MÉTODO AUXILIAR PARA ALINEAR ETIQUETA + CAMPO ---
    private JPanel crearFilaCampo(JLabel etiqueta, JTextField campo) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setBackground(new Color(245, 245, 245));

        etiqueta.setPreferredSize(new Dimension(100, 35));
        etiqueta.setMaximumSize(new Dimension(100, 35));
        etiqueta.setAlignmentY(Component.CENTER_ALIGNMENT);

        campo.setAlignmentY(Component.CENTER_ALIGNMENT);

        fila.add(etiqueta);
        fila.add(Box.createHorizontalStrut(10));
        fila.add(campo);

        return fila;
    }

    // --- PLACEHOLDERS DE FUNCIONES ---
    private void abrirLoginAdmin() {
        JOptionPane.showMessageDialog(this, "Login de administrador activado (placeholder)");
    }

    private void autenticarUsuario() {
        JOptionPane.showMessageDialog(this, "Autenticando usuario (placeholder)");
    }

    private void abrirRegistro() {
        JOptionPane.showMessageDialog(this, "Abrir pantalla de registro (placeholder)");
    }

    private void estilizarCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setMaximumSize(new Dimension(250, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    private Font cargarFuenteMontserrat(float size) {
        // 🔹 Placeholder: reemplazar con carga real desde assets
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }

    // --- MÉTODO PARA MOSTRAR EL LOGIN ---
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}

