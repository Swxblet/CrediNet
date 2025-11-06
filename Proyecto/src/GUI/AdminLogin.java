package GUI;

import BL.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin extends JFrame {
    private int logoClickCount = 0;
    private final LoginMenu loginGUI = new LoginMenu();

    public AdminLogin() {
        // Configuración base
        setTitle("CrediNet | Admin Login");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        // --- PANEL PRINCIPAL ---
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- PANEL IZQUIERDO (DEGRADADO AZUL) ---
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(0, 123, 255),
                        getWidth(), getHeight(),
                        new Color(0, 82, 204));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setPreferredSize(new Dimension(320, getHeight()));
        leftPanel.setLayout(new GridBagLayout());

        // --- LOGO ADMIN ---
        JLabel lblLogo = new JLabel();
        ImageIcon originalIcon = new ImageIcon("Proyecto/assets/img/adminIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));
        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        // Doble clic: volver al login de usuario
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoClickCount++;
                if (logoClickCount == 2) {
                    abrirLoginUsuario();
                }
            }
        });

        leftPanel.add(lblLogo);

        // --- PANEL DERECHO (FORMULARIO LOGIN ADMIN) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Fuente
        Font montserrat = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", 14);

        JLabel lblTitle = new JLabel("Acceso de Administrador");
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(montserrat);
        JTextField txtUsername = new JTextField(15);
        loginGUI.estilizarCampo(txtUsername);

        JLabel lblEmail = new JLabel("Correo electrónico");
        lblEmail.setFont(montserrat);
        JTextField txtEmail = new JTextField(15);
        loginGUI.estilizarCampo(txtEmail);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(montserrat);
        JPasswordField txtPassword = new JPasswordField(15);
        loginGUI.estilizarCampo(txtPassword);

        JButton btnLogin = getAdminButton(montserrat, 4, 10, 36, txtUsername, txtEmail, txtPassword);

        // --- ENSAMBLAR PANEL DERECHO ---
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(30));

        rightPanel.add(loginGUI.crearFilaCampo(lblUser, txtUsername));
        rightPanel.add(Box.createVerticalStrut(15));

        rightPanel.add(loginGUI.crearFilaCampo(lblEmail, txtEmail));
        rightPanel.add(Box.createVerticalStrut(15));

        rightPanel.add(loginGUI.crearFilaCampo(lblPass, txtPassword));
        rightPanel.add(Box.createVerticalStrut(25));

        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalStrut(20));

        // Unir paneles
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void autenticarAdmin(String username, String email, String password) {
        System.out.println("Admin user: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        // TODO: aquí conectas con tu BL:
    }

    // --- VOLVER AL LOGIN DE USUARIO ---
    private void abrirLoginUsuario() {
        LoginMenu loginGUI = new LoginMenu();
        loginGUI.mostrar();
        logoClickCount = 0;
        dispose();
    }

    // --- Botón principal de Admin ---
    private JButton getAdminButton(Font montserrat, int r, int g, int b,
                                   JTextField txtUsername,
                                   JTextField txtEmail,
                                   JPasswordField txtPassword) {
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(r, g, b));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(montserrat);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));

        btnLogin.addActionListener(e -> {
            // 🔹 Extraemos los valores de los campos
            String username = txtUsername.getText();
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());

            // 🔹 Llamamos al método que valida al admin
            autenticarAdmin(username, email, password);
        });

        return btnLogin;
    }

    // --- MOSTRAR ---
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}