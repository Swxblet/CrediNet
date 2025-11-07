package GUI;

import BL.ClientService;
import BL.FontLoader;
import BL.LoanService;
import BL.PaymentService;
import BL.CreditHistoryService;
//import BL.NotificationService; // cuando la tengas
import ET.Client;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginMenu extends JFrame {
    private int logoClickCount = 0;
    static ClientService clientService;

    public LoginMenu() {
        try {
            clientService = new ClientService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al iniciar el login",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Configuración base
        setTitle("CrediNet | Login");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- PANEL IZQUIERDO (DEGRADADO AZUL + GIF DE FONDO) ---
        JPanel leftPanel = new JPanel() {
            private final ImageIcon gif = new ImageIcon("Proyecto/assets/gif/loginAnimation.gif");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0,
                        new Color(0, 123, 255),
                        getWidth(), getHeight(),
                        new Color(0, 82, 204)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g.drawImage(gif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        leftPanel.setPreferredSize(new Dimension(320, getHeight()));
        leftPanel.setLayout(new GridBagLayout());

        // --- LOGO CENTRADO SOBRE EL GIF ---
        JLabel lblLogo = new JLabel();
        ImageIcon originalIcon = new ImageIcon("Proyecto/assets/img/userIcon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));
        lblLogo.setOpaque(false);

        leftPanel.add(lblLogo);

        // --- PANEL DERECHO (LOGIN) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Evento doble clic en logo (admin)
        lblLogo.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoClickCount++;
                if (logoClickCount == 2) {
                    abrirLoginAdmin();
                }
            }
        });

        // Fuente
        Font montserrat = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", 14);

        JLabel lblTitle = new JLabel("Iniciar sesión");
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Correo");
        lblUser.setFont(montserrat);
        JTextField txtUsername = new JTextField(15);
        estilizarCampo(txtUsername);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(montserrat);
        JPasswordField txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);

        JButton btnLogin = getJButton(montserrat, 33, 150, 243, txtUsername, txtPassword);

        JLabel lblRegister = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblRegister.setForeground(new Color(70, 130, 180));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirRegistro();
            }
        });

        // --- Panel que contiene TODO el formulario ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón con mismo ancho que los campos
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(250, 40));
        btnLogin.setPreferredSize(new Dimension(250, 40));

        // Agregamos en orden
        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(crearFilaCampo(lblUser, txtUsername));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(crearFilaCampo(lblPass, txtPassword));
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(btnLogin);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblRegister);
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Centrado vertical dentro del panel derecho
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(formPanel);
        rightPanel.add(Box.createVerticalGlue());

        // Unir paneles
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // --- Botón principal ---
    public JButton getJButton(Font montserrat, int r, int g, int b,
                              JTextField txtUsername, JPasswordField txtPassword) {
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(r, g, b));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(montserrat);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));

        btnLogin.addActionListener(e -> {
            String email = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            autenticarUsuario(email, password);
        });

        return btnLogin;
    }

    // --- Fila de campo ---
    public JPanel crearFilaCampo(JLabel etiqueta, JTextField campo) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
        fila.setBackground(new Color(245, 245, 245));

        int ancho = 250;
        int altoCampo = 35;

        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiqueta.setPreferredSize(new Dimension(ancho, etiqueta.getPreferredSize().height));
        etiqueta.setMaximumSize(new Dimension(ancho, etiqueta.getPreferredSize().height));
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campo.setPreferredSize(new Dimension(ancho, altoCampo));
        campo.setMaximumSize(new Dimension(ancho, altoCampo));

        fila.add(etiqueta);
        fila.add(Box.createVerticalStrut(5));
        fila.add(campo);

        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        return fila;
    }

    // --- Borde redondeado ---
    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(6, 10, 6, 10);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 10; insets.right = 10; insets.top = 6; insets.bottom = 6;
            return insets;
        }
    }

    public void estilizarCampo(JTextField campo) {
        int ancho = 250;
        campo.setPreferredSize(new Dimension(ancho, 35));
        campo.setMaximumSize(new Dimension(ancho, 35));
        campo.setBorder(new CompoundBorder(
                new RoundedBorder(12),
                new EmptyBorder(5, 10, 5, 10)
        ));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setOpaque(true);
        campo.setBackground(Color.WHITE);
    }

    // --- Auxiliares ---
    private void abrirLoginAdmin() {
        AdminLogin adminLogin = new AdminLogin();
        adminLogin.mostrar();
        logoClickCount = 0;
        dispose();
    }

    // 🔹 Autenticación REAL usando ClientService y abriendo ClientMenu
    private void autenticarUsuario(String email, String password) {
        if (clientService == null) {
            JOptionPane.showMessageDialog(this,
                    "Servicio de clientes no disponible.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client cliente = clientService.searchClientToVerify(email, password);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this,
                    "Credenciales no válidas",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LoanService loanService = new LoanService();
            PaymentService paymentService = new PaymentService();
            CreditHistoryService creditHistoryService = new CreditHistoryService();
            //NotificationService notificationService = new NotificationService(); // cuando la tengas

            ClientMenu clientMenu = new ClientMenu(
                    cliente,
                    loanService,
                    paymentService,
                    creditHistoryService
                    //notificationService
            );
            clientMenu.mostrar();
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos del usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        new RegisterMenu().mostrar();
        dispose();
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}