package GUI;

import BL.ClientService;
import BL.FontLoader;
import BL.ValidationUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RegisterMenu extends JFrame {
    ClientService clientService;

    {
        try {
            clientService = new ClientService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar el registro");
        }
    }

    public RegisterMenu(){
        setTitle("CrediNet | Registro");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());

        // PANEL IZQUIERDO (degradado azul)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 123, 255),
                        getWidth(), getHeight(), new Color(0, 82, 204));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));

        // PANEL DERECHO (formulario)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // LOGO
        ImageIcon logoOriginal = new ImageIcon("");
        Image imgEscalada = logoOriginal.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imgEscalada));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblTitle = new JLabel("Crear cuenta de cliente");
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(lblLogo);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(20));

        Font montserrat = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", 14);

        // PANEL CAMPOS (dos columnas)
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        fieldsPanel.setBackground(Color.WHITE);

        JTextField txtFullName = createRoundedFieldJText();
        JTextField txtId = createRoundedFieldJText();
        JTextField txtPhone = createRoundedFieldJText();
        JTextField txtAddress = createRoundedFieldJText();
        JTextField txtEmail = createRoundedFieldJText();
        JPasswordField txtPassword = createRoundedFieldJPassword();

        fieldsPanel.add(labeledField("Nombre completo", txtFullName, montserrat));
        fieldsPanel.add(labeledField("Identificación", txtId, montserrat));
        fieldsPanel.add(labeledField("Teléfono", txtPhone, montserrat));
        fieldsPanel.add(labeledField("Dirección", txtAddress, montserrat));
        fieldsPanel.add(labeledField("Correo electrónico", txtEmail, montserrat));
        fieldsPanel.add(labeledField("Contraseña", txtPassword, montserrat));

        rightPanel.add(fieldsPanel);
        rightPanel.add(Box.createVerticalStrut(20));

        // CONFIRMACIÓN
        JCheckBox chkConfirm = new JCheckBox("<html>¿Está de acuerdo con nuestros<br>términos y condiciones?</html>");
        chkConfirm.setBackground(Color.WHITE);
        chkConfirm.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rightPanel.add(chkConfirm);
        rightPanel.add(Box.createVerticalStrut(20));

        // BOTÓN REGISTRAR
        JButton btnRegister = new JButton("Registrar cuenta");
        btnRegister.setBackground(new Color(33, 150, 243));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(montserrat);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Registrar Cliente
        btnRegister.addActionListener(e -> {
            if (!chkConfirm.isSelected()) {
                JOptionPane.showMessageDialog(this, "Debes confirmar la información.");
                return;
            }

            if (ValidationUtils.isValidFullName(txtFullName.getText()) &&
                    ValidationUtils.isValidIdentification(txtId.getText()) &&
                    ValidationUtils.isValidPhone(txtPhone.getText()) &&
                    ValidationUtils.isValidEmail(txtEmail.getText()) &&
                    ValidationUtils.isValidAddress(txtAddress.getText())) {

                JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
                try{
                    clientService.addClient(txtId.getText(), txtFullName.getText(), txtEmail.getText(), txtAddress.getText(), txtPhone.getText(), txtPassword.getText());
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.");
                }
                dispose();
                new LoginMenu().mostrar();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, revisa los datos ingresados.");
            }
        });

        rightPanel.add(btnRegister);
        rightPanel.add(Box.createVerticalStrut(15));

        // ENLACE LOGIN
        JLabel lblBack = new JLabel("¿Ya tienes cuenta? Inicia sesión");
        lblBack.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblBack.setForeground(new Color(70, 130, 180));
        lblBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginMenu().mostrar();
                dispose();
            }
        });

        rightPanel.add(lblBack);
        rightPanel.add(Box.createVerticalStrut(10));

        // AGREGAR PANELES
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // --- TEXTFIELDS REDONDEADOS ---
    private JTextField createRoundedFieldJText() {
        JTextField field = new JTextField();
        field.setBorder(new RoundedBorder(10));
        field.setPreferredSize(new Dimension(180, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return field;
    }

    private JPasswordField createRoundedFieldJPassword() {
        JPasswordField field = new JPasswordField();
        field.setBorder(new RoundedBorder(10));
        field.setPreferredSize(new Dimension(180, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return field;
    }

    private JPanel labeledField(String label, JTextField field, Font font) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // --- BORDE REDONDEADO ---
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) { this.radius = radius; }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // --- Mostrar ---
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}