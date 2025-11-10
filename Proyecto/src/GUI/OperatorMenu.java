package GUI;

import BL.FontLoader;
import BL.LoanService;
import BL.PaymentService;
import BL.CreditHistoryService;
import BL.NotificationCenter;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class OperatorMenu extends JFrame {

    // ======= Paleta =======
    private static final Color BLUE_1 = new Color(0, 123, 255);
    private static final Color BLUE_2 = new Color(0, 82, 204);
    private static final Color BG_APP = new Color(245, 245, 245);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color BORDER_CARD = new Color(230, 230, 230);
    private static final Color TEXT_DARK = new Color(34, 34, 34);
    private static final Color TEXT_MUTED = new Color(90, 90, 90);
    private static final Color BTN_PRIMARY = new Color(33, 150, 243);

    // ======= Tipografías =======
    private final Font fTitle = safeFont(32, Font.BOLD);
    private final Font fH2    = safeFont(22, Font.BOLD);
    private final Font fH3    = safeFont(18, Font.BOLD);
    private final Font fUI    = safeFont(15, Font.PLAIN);
    private final Font fSmall = safeFont(13, Font.PLAIN);

    // ======= Servicios / operador logueado =======
    private final String operatorName;
    private final LoanService loanService;
    private final PaymentService paymentService;
    private final CreditHistoryService creditHistoryService;
    private final NotificationCenter notificationCenter;

    // Contenido principal
    private JPanel mainContent;
    private CardLayout mainContentLayout;

    // Dashboard responsivo (2x2 / 1 columna)
    private JPanel responsiveContent;

    // Referencias a ítems de navegación
    private RoundedPanel navDashboard;
    private RoundedPanel navSolicitudes;
    private RoundedPanel navPrestamos;
    private RoundedPanel navPagos;
    private RoundedPanel navClientes;
    private RoundedPanel navPerfil;

    public OperatorMenu(String operatorName,
                        LoanService loanService,
                        PaymentService paymentService,
                        CreditHistoryService creditHistoryService,
                        NotificationCenter notificationCenter) {

        this.operatorName = operatorName != null ? operatorName : "Operador";
        this.loanService = loanService;
        this.paymentService = paymentService;
        this.creditHistoryService = creditHistoryService;
        this.notificationCenter = notificationCenter;

        setTitle("CrediNet | Operador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);

        // Sidebar
        root.add(buildSidebar(), BorderLayout.WEST);

        // Centro: Topbar + contenido
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG_APP);
        center.add(buildTopbar(), BorderLayout.NORTH);

        mainContent = new JPanel();
        mainContentLayout = new CardLayout();
        mainContent.setLayout(mainContentLayout);

        // Vistas
        JPanel dashboardScreen   = buildDashboardContent();
        JPanel solicitudesScreen = buildSolicitudesScreen();
        JPanel prestamosScreen   = buildPrestamosScreen();
        JPanel pagosScreen       = buildPagosScreen();
        JPanel clientesScreen    = buildClientesScreen();
        JPanel perfilScreen      = buildPerfilScreen();

        mainContent.add(dashboardScreen,   "dashboard");
        mainContent.add(solicitudesScreen, "solicitudes");
        mainContent.add(prestamosScreen,   "prestamos");
        mainContent.add(pagosScreen,       "pagos");
        mainContent.add(clientesScreen,    "clientes");
        mainContent.add(perfilScreen,      "perfil");

        center.add(mainContent, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Ventana maximizada al iniciar
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Responsivo
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarLayoutResponsivo();
            }
        });

        mostrarDashboard();
    }

    // ================= Navegación =================

    private void mostrarDashboard() {
        mainContentLayout.show(mainContent, "dashboard");
        setActiveNav("dashboard");
        ajustarLayoutResponsivo();
    }

    private void mostrarSolicitudes() {
        mainContentLayout.show(mainContent, "solicitudes");
        setActiveNav("solicitudes");
    }

    private void mostrarPrestamos() {
        mainContentLayout.show(mainContent, "prestamos");
        setActiveNav("prestamos");
    }

    private void mostrarPagos() {
        mainContentLayout.show(mainContent, "pagos");
        setActiveNav("pagos");
    }

    private void mostrarClientes() {
        mainContentLayout.show(mainContent, "clientes");
        setActiveNav("clientes");
    }

    private void mostrarPerfil() {
        mainContentLayout.show(mainContent, "perfil");
        setActiveNav("perfil");
    }

    private void setActiveNav(String key) {
        setNavActive(navDashboard,   "dashboard".equals(key));
        setNavActive(navSolicitudes, "solicitudes".equals(key));
        setNavActive(navPrestamos,   "prestamos".equals(key));
        setNavActive(navPagos,       "pagos".equals(key));
        setNavActive(navClientes,    "clientes".equals(key));
        setNavActive(navPerfil,      "perfil".equals(key));
    }

    private void setNavActive(RoundedPanel nav, boolean active) {
        if (nav == null) return;
        Color activeColor = new Color(255, 255, 255, 32);
        Color inactiveColor = new Color(255, 255, 255, 20);
        nav.setBackgroundColor(active ? activeColor : inactiveColor);
    }

    private void ajustarLayoutResponsivo() {
        if (responsiveContent == null) return;
        int width = getWidth();
        CardLayout cl = (CardLayout) responsiveContent.getLayout();
        if (width < 1100) {
            cl.show(responsiveContent, "narrow");
        } else {
            cl.show(responsiveContent, "wide");
        }
    }

    // ================= Sidebar =================

    private JComponent buildSidebar() {
        GradientPanel side = new GradientPanel(BLUE_1, BLUE_2);
        side.setPreferredSize(new Dimension(260, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(24, 20, 24, 20));

        // Logo
        JPanel logoWrap = new JPanel();
        logoWrap.setOpaque(false);
        logoWrap.setLayout(new BoxLayout(logoWrap, BoxLayout.Y_AXIS));
        logoWrap.setMaximumSize(new Dimension(220, 120));

        RoundedPanel logoCircle = new RoundedPanel(80, Color.WHITE);
        logoCircle.setPreferredSize(new Dimension(80, 80));
        logoCircle.setMaximumSize(new Dimension(80, 80));
        logoCircle.setLayout(new GridBagLayout());

        ImageIcon rawIcon = new ImageIcon("Proyecto/assets/img/logo.png");
        Image scaled = rawIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoCircle.add(logoLabel);

        logoWrap.add(Box.createVerticalGlue());
        logoWrap.add(logoCircle);
        logoWrap.add(Box.createVerticalGlue());

        side.add(logoWrap);
        side.add(Box.createVerticalStrut(20));

        // Nav items
        navDashboard   = createNavItem("Dashboard", true,  this::mostrarDashboard);
        navSolicitudes = createNavItem("Solicitudes", false, this::mostrarSolicitudes);
        navPrestamos   = createNavItem("Préstamos", false,   this::mostrarPrestamos);
        navPagos       = createNavItem("Pagos", false,       this::mostrarPagos);
        navClientes    = createNavItem("Clientes", false,    this::mostrarClientes);
        navPerfil      = createNavItem("Perfil", false,      this::mostrarPerfil);

        side.add(navDashboard);
        side.add(Box.createVerticalStrut(14));
        side.add(navSolicitudes);
        side.add(Box.createVerticalStrut(14));
        side.add(navPrestamos);
        side.add(Box.createVerticalStrut(14));
        side.add(navPagos);
        side.add(Box.createVerticalStrut(14));
        side.add(navClientes);
        side.add(Box.createVerticalStrut(14));
        side.add(navPerfil);

        side.add(Box.createVerticalGlue());
        return side;
    }

    private RoundedPanel createNavItem(String text, boolean active, Runnable onClick) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(fUI);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(12, 16, 12, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> onClick.run());

        RoundedPanel wrap = new RoundedPanel(16,
                active ? new Color(255, 255, 255, 32) : new Color(255, 255, 255, 20));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);
        wrap.setMaximumSize(new Dimension(220, 44));
        wrap.setBorder(new EmptyBorder(0, 4, 0, 4));
        return wrap;
    }

    // ================= Topbar =================

    private JComponent buildTopbar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel brand = new JLabel("Panel de Operador");
        brand.setFont(fTitle);
        brand.setForeground(TEXT_DARK);
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brandPanel.setOpaque(false);
        brandPanel.add(brand);

        RoundedTextField search = new RoundedTextField(24);
        search.setFont(fSmall);
        search.setForeground(new Color(130, 130, 130));
        search.setBackground(new Color(248, 248, 248));
        search.setPreferredSize(new Dimension(260, 40));

        // Aquí más adelante puedes conectar la búsqueda unificada de operador
        // (por ahora es solo visual)
        search.putClientProperty("JTextField.placeholderText", "Buscar clientes, préstamos, pagos...");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        centerPanel.add(search, BorderLayout.CENTER);

        JPanel chip = new RoundedPanel(20, new Color(245, 248, 255));
        chip.setBorder(new CompoundLineBorder(new Color(210, 225, 255)));
        chip.setLayout(new GridBagLayout());

        JLabel dot = new JLabel();
        dot.setPreferredSize(new Dimension(26, 26));
        ImageIcon dotIcon = new ImageIcon("Proyecto/assets/img/userIcon.png");
        Image scaledImage = dotIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        dot.setIcon(new ImageIcon(scaledImage));
        dot.setVisible(true);

        JLabel hi = new JLabel("Hola, " + operatorName);
        hi.setFont(fSmall);
        hi.setForeground(new Color(22, 40, 77));

        JPanel chipInner = new JPanel();
        chipInner.setOpaque(false);
        chipInner.setLayout(new BoxLayout(chipInner, BoxLayout.X_AXIS));
        chipInner.add(dot);
        chipInner.add(Box.createHorizontalStrut(8));
        chipInner.add(hi);

        chip.add(chipInner);
        chip.setPreferredSize(new Dimension(210, 40));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(chip);

        top.add(brandPanel, BorderLayout.WEST);
        top.add(centerPanel, BorderLayout.CENTER);
        top.add(rightPanel, BorderLayout.EAST);

        return top;
    }

    // ================= Dashboard =================

    private JPanel buildDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_APP);

        responsiveContent = new JPanel(new CardLayout());
        responsiveContent.add(buildGrid2x2(), "wide");
        responsiveContent.add(buildGrid1Column(), "narrow");

        panel.add(responsiveContent, BorderLayout.CENTER);
        return panel;
    }

    private JComponent buildGrid2x2() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(BG_APP);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.BOTH;

        // Card 1: Resumen de hoy
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.weighty = 0.5;
        grid.add(buildCardResumenHoy(), c);

        // Card 2: Solicitudes pendientes
        c.gridx = 1; c.gridy = 0;
        grid.add(buildCardSolicitudesDashboard(), c);

        // Card 3: Préstamos activos / riesgo
        c.gridx = 0; c.gridy = 1;
        grid.add(buildCardPrestamosDashboard(), c);

        // Card 4: Actividad reciente
        c.gridx = 1; c.gridy = 1;
        grid.add(buildCardActividadDashboard(), c);

        return grid;
    }

    private JComponent buildGrid1Column() {
        JPanel column = new JPanel();
        column.setBackground(BG_APP);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(new EmptyBorder(12, 12, 12, 12));

        column.add(buildCardResumenHoy());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardSolicitudesDashboard());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardPrestamosDashboard());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardActividadDashboard());

        JScrollPane sp = new JScrollPane(column);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    // ====== Cards dashboard (placeholders listos para conectar) ======

    private JComponent buildCardResumenHoy() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Resumen de hoy");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel l1 = new JLabel("• Solicitudes pendientes: (conectar LoanService más adelante)");
        JLabel l2 = new JLabel("• Pagos por revisar: (conectar PaymentService más adelante)");
        JLabel l3 = new JLabel("• Clientes con alertas: (futuro: CreditHistoryService)");

        for (JLabel l : new JLabel[]{l1, l2, l3}) {
            l.setFont(fSmall);
            l.setForeground(TEXT_MUTED);
        }

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(l1);
        content.add(l2);
        content.add(l3);
        content.add(Box.createVerticalGlue());

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildCardSolicitudesDashboard() {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Solicitudes recientes");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(16, 16, 0, 16));

        card.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Cliente", "Monto", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // TODO: poblar con solicitudes reales desde LoanService
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(fUI);
        table.getTableHeader().setFont(fSmall);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(8, 16, 16, 16));

        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildCardPrestamosDashboard() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Visión de préstamos");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel l1 = new JLabel("• Total préstamos activos: (usar LoanService)");
        JLabel l2 = new JLabel("• En mora / atraso: (usar LoanService + reglas)");
        JLabel l3 = new JLabel("• Monto total colocado: (sumar montos)");

        for (JLabel l : new JLabel[]{l1, l2, l3}) {
            l.setFont(fSmall);
            l.setForeground(TEXT_MUTED);
        }

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(l1);
        content.add(l2);
        content.add(l3);
        content.add(Box.createVerticalGlue());

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildCardActividadDashboard() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Actividad reciente");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel l1 = new JLabel("Aquí luego puedes listar acciones del operador (aprobaciones, rechazos, pagos).");
        l1.setFont(fSmall);
        l1.setForeground(TEXT_MUTED);

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(l1);
        content.add(Box.createVerticalGlue());

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ================= Pantallas vacías (placeholders) =================

    private JPanel buildSolicitudesScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Gestión de solicitudes");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Aquí podrás revisar, aprobar o rechazar solicitudes de préstamo.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Body placeholder
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BorderLayout());
        JLabel info = new JLabel("TODO: conectar lista de solicitudes desde LoanService.", SwingConstants.CENTER);
        info.setFont(fUI);
        info.setForeground(TEXT_MUTED);
        body.add(info, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildPrestamosScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Préstamos supervisados");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Listado general de préstamos gestionados por el operador o por la sucursal.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Body placeholder
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        JLabel info = new JLabel("TODO: tabla de préstamos con filtros por estado, cliente, etc.", SwingConstants.CENTER);
        info.setFont(fUI);
        info.setForeground(TEXT_MUTED);
        body.add(info, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildPagosScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Pagos");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Aquí podrás revisar y validar pagos realizados por los clientes.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        JLabel info = new JLabel("TODO: historial de pagos con filtros y opciones de validación.", SwingConstants.CENTER);
        info.setFont(fUI);
        info.setForeground(TEXT_MUTED);
        body.add(info, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildClientesScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Clientes");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Consulta y gestiona la información básica de los clientes.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        JLabel info = new JLabel("TODO: tabla de clientes + buscador por nombre, cédula, etc.", SwingConstants.CENTER);
        info.setFont(fUI);
        info.setForeground(TEXT_MUTED);
        body.add(info, BorderLayout.CENTER);

        card.add(body, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildPerfilScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Perfil del operador");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Aquí podrás mostrar y editar la información del operador.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel info = new JLabel("TODO: datos del operador, cambio de contraseña, etc.");
        info.setFont(fUI);
        info.setForeground(TEXT_MUTED);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(info);
        body.add(Box.createVerticalGlue());

        card.add(body, BorderLayout.CENTER);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    // ================= Helpers UI =================

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(BTN_PRIMARY);
        b.setFont(fUI);
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private Font safeFont(int size, int style) {
        try {
            Font f = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", size);
            return f.deriveFont(style, (float) size);
        } catch (Throwable t) {
            return new Font("SansSerif", style, size);
        }
    }

    // Panel con degradado vertical
    static class GradientPanel extends JPanel {
        private final Color c1, c2;
        GradientPanel(Color c1, Color c2) {
            this.c1 = c1; this.c2 = c2; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Panel redondeado sencillo
    static class RoundedPanel extends JPanel {
        private final int radius;
        private Color bg;
        RoundedPanel(int radius, Color bg) {
            this.radius = radius; this.bg = bg;
            setOpaque(false);
        }
        public void setBackgroundColor(Color c) { this.bg = c; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Borde fino para chips/topbar
    static class CompoundLineBorder extends AbstractBorder {
        private final Color color;
        CompoundLineBorder(Color color) { this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, 40, 40);
            g2.dispose();
        }
    }

    // Card con sombra sutil
    static class CardPanel extends JPanel {
        CardPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(8, 8, 8, 8));
            setLayout(new BorderLayout());
        }
        @Override protected void paintComponent(Graphics g) {
            int arc = 18;
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(new Color(0, 0, 0, 60));
            g2.fill(new RoundRectangle2D.Float(6, 10, w - 12, h - 12, arc + 6, arc + 6));

            g2.setColor(BG_CARD);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 12, h - 12, arc, arc));
            g2.setColor(BORDER_CARD);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 13, h - 13, arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Dimension getPreferredSize() { return new Dimension(480, 240); }
    }

    // TextField redondeado (topbar search)
    static class RoundedTextField extends JTextField {
        private final int arc;
        RoundedTextField(int arc) {
            this.arc = arc;
            setBorder(new EmptyBorder(8, 14, 8, 14));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            super.paintComponent(g);
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
        }
    }

    // ======== Mostrar ========
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
