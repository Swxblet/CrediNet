package GUI;

import BL.FontLoader;
import BL.LoanService;
import BL.PaymentService;
import BL.CreditHistoryService;
//import BL.NotificationService;
import ET.Client;
import ET.Loan;
import ET.Payment;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientMenu extends JFrame {
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

    // ======= Servicios / usuario logueado =======
    private final Client client;
    private final LoanService loanService;
    private final PaymentService paymentService;
    private final CreditHistoryService creditHistoryService;
    //private final NotificationService notificationService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

    // Contenido principal (dashboard vs solicitud vs mis préstamos vs pagos)
    private JPanel mainContent;
    private CardLayout mainContentLayout;

    // Dashboard responsivo (2x2 / 1 columna)
    private JPanel responsiveContent;

    // Referencias a ítems de navegación
    private RoundedPanel navDashboard;
    private RoundedPanel navSolicitar;
    private RoundedPanel navMisPrestamos;
    private RoundedPanel navPagos;

    // ========= NUEVO CONSTRUCTOR: recibe cliente + servicios =========
    public ClientMenu(Client client,
                      LoanService loanService,
                      PaymentService paymentService,
                      CreditHistoryService creditHistoryService
                      //NotificationService notificationService
                      ) {
        this.client = client;
        this.loanService = loanService;
        this.paymentService = paymentService;
        this.creditHistoryService = creditHistoryService;
        //this.notificationService = notificationService;

        setTitle("CrediNet | Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);

        // Sidebar (WEST)
        root.add(buildSidebar(), BorderLayout.WEST);

        // Centro: Topbar + contenido (CardLayout)
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG_APP);
        center.add(buildTopbar(), BorderLayout.NORTH);

        mainContent = new JPanel();
        mainContentLayout = new CardLayout();
        mainContent.setLayout(mainContentLayout);

        // Dashboard
        JPanel dashboard = buildDashboardContent();
        // Pantalla dedicada: Solicitar préstamo
        JPanel loanScreen = buildLoanRequestScreen();
        // Pantalla dedicada: Mis préstamos
        JPanel loansListScreen = buildLoansScreen();
        // Pantalla dedicada: Pagos
        JPanel paymentsScreen = buildPaymentsScreen();

        mainContent.add(dashboard, "dashboard");
        mainContent.add(loanScreen, "loan");
        mainContent.add(loansListScreen, "loans");
        mainContent.add(paymentsScreen, "payments");

        center.add(mainContent, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);

        // Ventana maximizada al iniciar
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Cambiar layout del dashboard según tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarLayoutResponsivo();
            }
        });

        // Mostrar dashboard por defecto
        mostrarDashboard();
    }

    // ======================
    // Cambiar vista principal
    // ======================
    private void mostrarDashboard() {
        mainContentLayout.show(mainContent, "dashboard");
        setActiveNav("dashboard");
        ajustarLayoutResponsivo();
    }

    private void mostrarPantallaSolicitud() {
        mainContentLayout.show(mainContent, "loan");
        setActiveNav("loan");
    }

    private void mostrarMisPrestamos() {
        mainContentLayout.show(mainContent, "loans");
        setActiveNav("loans");
    }

    private void mostrarPagos() {
        mainContentLayout.show(mainContent, "payments");
        setActiveNav("payments");
    }

    private void setActiveNav(String key) {
        setNavActive(navDashboard, "dashboard".equals(key));
        setNavActive(navSolicitar, "loan".equals(key));
        setNavActive(navMisPrestamos, "loans".equals(key));
        setNavActive(navPagos, "payments".equals(key));
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

    // ======================
    // Sidebar degradado + logo + menú
    // ======================
    private JComponent buildSidebar() {
        GradientPanel side = new GradientPanel(BLUE_1, BLUE_2);
        side.setPreferredSize(new Dimension(260, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(24, 20, 24, 20));

        // ---------- LOGO PERSONALIZADO EN CÍRCULO ----------
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

        // ---------- Items de navegación ----------
        navDashboard = createNavItem("Dashboard", true, this::mostrarDashboard);
        side.add(navDashboard);
        side.add(Box.createVerticalStrut(14));

        navMisPrestamos = createNavItem("Mis préstamos", false, this::mostrarMisPrestamos);
        side.add(navMisPrestamos);
        side.add(Box.createVerticalStrut(14));

        navSolicitar = createNavItem("Solicitar préstamo", false, this::mostrarPantallaSolicitud);
        side.add(navSolicitar);
        side.add(Box.createVerticalStrut(14));

        navPagos = createNavItem("Pagos", false, this::mostrarPagos);
        side.add(navPagos);
        side.add(Box.createVerticalStrut(14));

        side.add(simpleNavItem("Estados de Cuenta", false));
        side.add(Box.createVerticalStrut(14));
        side.add(simpleNavItem("Perfil", false));

        side.add(Box.createVerticalGlue());
        return side;
    }

    // NavItem con acción
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

        RoundedPanel wrap = new RoundedPanel(16, active ? new Color(255, 255, 255, 32) : new Color(255, 255, 255, 20));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);
        wrap.setMaximumSize(new Dimension(220, 44));
        wrap.setBorder(new EmptyBorder(0, 4, 0, 4));
        return wrap;
    }

    // NavItem solo visual (placeholder)
    private RoundedPanel simpleNavItem(String text, boolean active) {
        return createNavItem(text, active, () -> {
            JOptionPane.showMessageDialog(this,
                    "La sección \"" + text + "\" aún no está implementada.");
        });
    }

    // ======================
    // Topbar con buscador y chip usuario
    // ======================
    private JComponent buildTopbar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel brand = new JLabel("CrediNet");
        brand.setFont(fTitle);
        brand.setForeground(TEXT_DARK);
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brandPanel.setOpaque(false);
        brandPanel.add(brand);

        RoundedTextField search = new RoundedTextField(24);
        search.setText("Buscar en tu cuenta...");
        search.setFont(fSmall);
        search.setForeground(new Color(130, 130, 130));
        search.setBackground(new Color(248, 248, 248));
        search.setPreferredSize(new Dimension(200, 40));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        centerPanel.add(search, BorderLayout.CENTER);

        JPanel chip = new RoundedPanel(20, new Color(245, 248, 255));
        chip.setBorder(new CompoundLineBorder(new Color(210, 225, 255)));
        chip.setLayout(new GridBagLayout());

        JPanel dot = new RoundedPanel(15, BLUE_1);
        dot.setPreferredSize(new Dimension(26, 26));

        JLabel hi = new JLabel("Hola, " + getClientFirstName());
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

    private String getClientFirstName() {
        if (client == null || client.getFullName() == null || client.getFullName().isEmpty()) {
            return "cliente";
        }
        String[] parts = client.getFullName().trim().split("\\s+");
        return parts[0];
    }

    // ======================
    // Dashboard (envoltorio)
    // ======================
    private JPanel buildDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_APP);

        responsiveContent = new JPanel(new CardLayout());
        responsiveContent.add(buildGrid2x2(), "wide");
        responsiveContent.add(buildGrid1Column(), "narrow");

        panel.add(responsiveContent, BorderLayout.CENTER);
        return panel;
    }

    // ======================
    // Contenido: layout 2x2 (wide)
    // ======================
    private JComponent buildGrid2x2() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(BG_APP);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.BOTH;

        // Card 1: Préstamo activo
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.weighty = 0.5;
        grid.add(buildCardPrestamoActivo(), c);

        // Card 2: Solicitar préstamo
        c.gridx = 1; c.gridy = 0;
        grid.add(buildCardSolicitarPrestamo(), c);

        // Card 3: Notificaciones
        c.gridx = 0; c.gridy = 1;
        grid.add(buildCardNotificaciones(), c);

        // Card 4: Mis préstamos (resumen)
        c.gridx = 1; c.gridy = 1;
        grid.add(buildCardTablaPrestamos(), c);

        return grid;
    }

    // ======================
    // Contenido: layout 1 columna (narrow)
    // ======================
    private JComponent buildGrid1Column() {
        JPanel column = new JPanel();
        column.setBackground(BG_APP);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(new EmptyBorder(12, 12, 12, 12));

        column.add(buildCardPrestamoActivo());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardSolicitarPrestamo());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardNotificaciones());
        column.add(Box.createVerticalStrut(12));
        column.add(buildCardTablaPrestamos());

        JScrollPane sp = new JScrollPane(column);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    // ---------- Card: Préstamo activo (DATOS REALES) ----------
    private JComponent buildCardPrestamoActivo() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Préstamo activo");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel saldoLabel = new JLabel();
        saldoLabel.setFont(fH3);
        saldoLabel.setForeground(new Color(22, 40, 77));
        saldoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel();
        infoLabel.setFont(fUI);
        infoLabel.setForeground(TEXT_MUTED);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Obtener préstamo activo real
        Loan activeLoan = null;
        if (loanService != null && client != null) {
            activeLoan = loanService.getActiveLoanForClient(client.getClientId());
        }

        if (activeLoan != null) {
            long remaining = getRemainingBalance(activeLoan);
            saldoLabel.setText("Saldo pendiente: ₡ " + formatMoney(remaining));
            infoLabel.setText("Monto original: ₡ " + formatMoney(activeLoan.getAmount()) +
                    " · Vence: " + formatDate(activeLoan.getEndDate()));
        } else {
            saldoLabel.setText("No tienes préstamos activos actualmente.");
            infoLabel.setText("Cuando se apruebe un préstamo, verás su detalle aquí.");
        }

        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(saldoLabel);
        content.add(Box.createVerticalStrut(4));
        content.add(infoLabel);
        content.add(Box.createVerticalStrut(12));

        // Chip de estado solo si hay préstamo activo
        if (activeLoan != null) {
            Status st = statusFromString(activeLoan.getStatus());
            ChipLabel chip = new ChipLabel(st.text, st.bg, st.fg);
            chip.setAlignmentX(Component.LEFT_ALIGNMENT);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            chip.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mostrarMisPrestamos();
                }
            });
            content.add(chip);
            content.add(Box.createVerticalStrut(12));
        }

        // Botón pagar
        JButton pagar = primaryButton("Ir a pagos");
        pagar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pagar.setPreferredSize(new Dimension(160, 40));
        pagar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pagar.addActionListener(e -> mostrarPagos());
        if (activeLoan == null) {
            pagar.setEnabled(false);
        }

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(Box.createHorizontalGlue());
        footer.add(pagar);

        content.add(Box.createVerticalGlue());
        content.add(footer);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ---------- Card: Solicitar préstamo (dashboard) ----------
    private JComponent buildCardSolicitarPrestamo() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Solicita un nuevo préstamo");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Elige el monto y el plazo. Te mostraremos una proyección antes de enviar tu solicitud.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(4));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(16));

        // ====== Monto ======
        JPanel montoPanel = new JPanel();
        montoPanel.setOpaque(false);
        montoPanel.setLayout(new BoxLayout(montoPanel, BoxLayout.Y_AXIS));
        montoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMonto = new JLabel("Monto solicitado");
        lblMonto.setFont(fUI);
        lblMonto.setForeground(TEXT_MUTED);

        JPanel montoInputRow = new JPanel();
        montoInputRow.setOpaque(false);
        montoInputRow.setLayout(new BoxLayout(montoInputRow, BoxLayout.X_AXIS));

        JLabel colSymbol = new JLabel("₡");
        colSymbol.setFont(fUI);
        colSymbol.setForeground(TEXT_DARK);
        colSymbol.setBorder(new EmptyBorder(0, 0, 0, 6));

        JTextField txtMonto = new JTextField();
        txtMonto.setFont(fUI);
        txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtMonto.setPreferredSize(new Dimension(200, 32));

        montoInputRow.add(colSymbol);
        montoInputRow.add(txtMonto);

        JPanel quickAmounts = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quickAmounts.setOpaque(false);
        quickAmounts.setBorder(new EmptyBorder(6, 0, 0, 0));

        String[] sugerencias = { "500 000", "1 000 000", "2 000 000" };
        for (String s : sugerencias) {
            JButton chip = new JButton("₡ " + s);
            chip.setFont(fSmall);
            chip.setFocusPainted(false);
            chip.setBackground(new Color(240, 244, 255));
            chip.setForeground(new Color(33, 64, 120));
            chip.setBorder(new EmptyBorder(4, 10, 4, 10));
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            chip.addActionListener(e -> txtMonto.setText(s.replace(" ", "")));
            quickAmounts.add(chip);
            quickAmounts.add(Box.createHorizontalStrut(6));
        }

        montoPanel.add(lblMonto);
        montoPanel.add(Box.createVerticalStrut(4));
        montoPanel.add(montoInputRow);
        montoPanel.add(quickAmounts);

        content.add(montoPanel);
        content.add(Box.createVerticalStrut(16));

        // ====== Plazo ======
        JPanel plazoPanel = new JPanel();
        plazoPanel.setOpaque(false);
        plazoPanel.setLayout(new BoxLayout(plazoPanel, BoxLayout.Y_AXIS));
        plazoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPlazo = new JLabel("Plazo (meses)");
        lblPlazo.setFont(fUI);
        lblPlazo.setForeground(TEXT_MUTED);

        JSlider sPlazo = new JSlider(1, 360, 24);
        sPlazo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sPlazo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sPlazo.setPaintTicks(true);
        sPlazo.setPaintTrack(true);
        sPlazo.setMajorTickSpacing(59);
        sPlazo.setMinorTickSpacing(11);
        sPlazo.setPaintLabels(false);

        JLabel lblPlazoValor = new JLabel("Plazo seleccionado: 24 meses");
        lblPlazoValor.setFont(fSmall);
        lblPlazoValor.setForeground(TEXT_MUTED);
        lblPlazoValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHelpPlazo = new JLabel("De 1 a 360 meses. Plazos largos bajan la cuota mensual pero aumentan el monto total a pagar.");
        lblHelpPlazo.setFont(fSmall);
        lblHelpPlazo.setForeground(new Color(120, 120, 120));
        lblHelpPlazo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sPlazo.addChangeListener(e -> {
            int meses = sPlazo.getValue();
            lblPlazoValor.setText("Plazo seleccionado: " + meses + " mes" + (meses == 1 ? "" : "es"));
        });

        plazoPanel.add(lblPlazo);
        plazoPanel.add(Box.createVerticalStrut(4));
        plazoPanel.add(sPlazo);
        plazoPanel.add(Box.createVerticalStrut(4));
        plazoPanel.add(lblPlazoValor);
        plazoPanel.add(Box.createVerticalStrut(4));
        plazoPanel.add(lblHelpPlazo);

        content.add(plazoPanel);
        content.add(Box.createVerticalStrut(16));

        // ====== Resumen rápido ======
        RoundedPanel resumen = new RoundedPanel(16, new Color(248, 249, 252));
        resumen.setLayout(new GridLayout(3, 1, 0, 4));
        resumen.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblResMonto = new JLabel("Monto solicitado: —");
        lblResMonto.setFont(fSmall);
        lblResMonto.setForeground(TEXT_DARK);

        JLabel lblResPlazo = new JLabel("Plazo: —");
        lblResPlazo.setFont(fSmall);
        lblResPlazo.setForeground(TEXT_DARK);

        JLabel lblResCuota = new JLabel("Cuota estimada: — (tú pones la lógica)");
        lblResCuota.setFont(fSmall);
        lblResCuota.setForeground(new Color(22, 40, 77));

        resumen.add(lblResMonto);
        resumen.add(lblResPlazo);
        resumen.add(lblResCuota);

        sPlazo.addChangeListener(e -> {
            int meses = sPlazo.getValue();
            lblResPlazo.setText("Plazo: " + meses + " mes" + (meses == 1 ? "" : "es"));
        });

        txtMonto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String t = txtMonto.getText().trim();
                if (t.isEmpty()) {
                    lblResMonto.setText("Monto solicitado: —");
                } else {
                    lblResMonto.setText("Monto solicitado: ₡ " + t);
                }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        resumen.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(resumen);
        content.add(Box.createVerticalStrut(16));

        // ====== Footer (responsive) ======
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnContinuar = primaryButton("Continuar solicitud");
        btnContinuar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnContinuar.setPreferredSize(new Dimension(180, 40));
        btnContinuar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinuar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        btnContinuar.addActionListener(e -> mostrarPantallaSolicitud());

        footer.add(Box.createHorizontalGlue());
        footer.add(btnContinuar);

        content.add(footer);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ---------- Pantalla dedicada: Solicitar préstamo ----------
    // (igual que tenías, solo cambié el mensaje del JOptionPane)
    private JPanel buildLoanRequestScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Nueva solicitud de préstamo");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Completa los datos del préstamo. Tu solicitud será revisada por un asesor.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Body: 2 columnas
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 16, 16, 16);
        c.fill = GridBagConstraints.BOTH;

        // Columna izquierda: formulario
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(16, 0, 16, 8));

        JLabel lblDatos = new JLabel("Datos del préstamo");
        lblDatos.setFont(fUI);
        lblDatos.setForeground(TEXT_DARK);
        lblDatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMonto = new JLabel("Monto solicitado");
        lblMonto.setFont(fSmall);
        lblMonto.setForeground(TEXT_MUTED);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel montoRow = new JPanel();
        montoRow.setOpaque(false);
        montoRow.setLayout(new BoxLayout(montoRow, BoxLayout.X_AXIS));

        JLabel colSymbol = new JLabel("₡");
        colSymbol.setFont(fUI);
        colSymbol.setForeground(TEXT_DARK);
        colSymbol.setBorder(new EmptyBorder(0, 0, 0, 6));

        JTextField txtMonto = new JTextField();
        txtMonto.setFont(fUI);
        txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        montoRow.add(colSymbol);
        montoRow.add(txtMonto);

        JLabel lblPlazo = new JLabel("Plazo (meses)");
        lblPlazo.setFont(fSmall);
        lblPlazo.setForeground(TEXT_MUTED);
        lblPlazo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSpinner spPlazo = new JSpinner(new SpinnerNumberModel(24, 1, 360, 1));
        spPlazo.setFont(fUI);
        spPlazo.setMaximumSize(new Dimension(120, 32));

        JLabel lblTipo = new JLabel("Tipo de préstamo");
        lblTipo.setFont(fSmall);
        lblTipo.setForeground(TEXT_MUTED);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cbTipo = new JComboBox<>(new String[]{
                "Crédito personal",
                "Crédito de vehículo",
                "Crédito de vivienda",
                "Consolidación de deudas",
                "Otro"
        });
        cbTipo.setFont(fUI);
        cbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel lblDestino = new JLabel("Destino del crédito (opcional)");
        lblDestino.setFont(fSmall);
        lblDestino.setForeground(TEXT_MUTED);
        lblDestino.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea taDestino = new JTextArea(3, 20);
        taDestino.setFont(fUI);
        taDestino.setLineWrap(true);
        taDestino.setWrapStyleWord(true);
        JScrollPane spDestino = new JScrollPane(taDestino);
        spDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        left.add(lblDatos);
        left.add(Box.createVerticalStrut(12));
        left.add(lblMonto);
        left.add(Box.createVerticalStrut(4));
        left.add(montoRow);
        left.add(Box.createVerticalStrut(12));
        left.add(lblPlazo);
        left.add(Box.createVerticalStrut(4));
        left.add(spPlazo);
        left.add(Box.createVerticalStrut(12));
        left.add(lblTipo);
        left.add(Box.createVerticalStrut(4));
        left.add(cbTipo);
        left.add(Box.createVerticalStrut(12));
        left.add(lblDestino);
        left.add(Box.createVerticalStrut(4));
        left.add(spDestino);
        left.add(Box.createVerticalGlue());

        c.gridx = 0; c.gridy = 0; c.weightx = 0.6; c.weighty = 1.0;
        body.add(left, c);

        // Columna derecha: resumen
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(16, 8, 16, 0));

        JLabel lblResumen = new JLabel("Resumen del préstamo");
        lblResumen.setFont(fUI);
        lblResumen.setForeground(TEXT_DARK);
        lblResumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel resumen = new RoundedPanel(16, new Color(248, 249, 252));
        resumen.setLayout(new BoxLayout(resumen, BoxLayout.Y_AXIS));
        resumen.setBorder(new EmptyBorder(14, 14, 14, 14));
        resumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rMonto = new JLabel("Monto: —");
        JLabel rPlazo = new JLabel("Plazo: —");
        JLabel rTipo = new JLabel("Tipo: —");
        JLabel rCuota = new JLabel("Cuota estimada: —");
        JLabel rTotal = new JLabel("Monto total estimado: —");

        for (JLabel l : new JLabel[]{rMonto, rPlazo, rTipo, rCuota, rTotal}) {
            l.setFont(fSmall);
            l.setForeground(TEXT_DARK);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            resumen.add(l);
            resumen.add(Box.createVerticalStrut(4));
        }

        JLabel helper = new JLabel("<html><span style='color:#666666;'>Estos valores son estimados. La aprobación y tasa final dependen de la evaluación crediticia.</span></html>");
        helper.setFont(fSmall);
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        right.add(lblResumen);
        right.add(Box.createVerticalStrut(12));
        right.add(resumen);
        right.add(Box.createVerticalStrut(8));
        right.add(helper);
        right.add(Box.createVerticalGlue());

        c.gridx = 1; c.gridy = 0; c.weightx = 0.4; c.weighty = 1.0;
        body.add(right, c);

        card.add(body, BorderLayout.CENTER);

        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(TEXT_DARK);
        btnCancelar.setFont(fUI);
        btnCancelar.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnCancelar.addActionListener(e -> mostrarDashboard());

        JButton btnEnviar = primaryButton("Enviar solicitud");
        btnEnviar.setPreferredSize(new Dimension(180, 38));
        btnEnviar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí creas un Loan real usando LoanService y lo guardas en loans.dat.\n" +
                            "Tú te encargas de la lógica de negocio, yo de que la pantalla esté lista 💙");
        });

        footer.add(btnCancelar);
        footer.add(btnEnviar);

        card.add(footer, BorderLayout.SOUTH);

        GridBagConstraints wrapperC = new GridBagConstraints();
        wrapperC.gridx = 0; wrapperC.gridy = 0;
        wrapperC.weightx = 1.0; wrapperC.weighty = 1.0;
        wrapperC.fill = GridBagConstraints.BOTH;
        wrap.add(card, wrapperC);

        root.add(wrap, BorderLayout.CENTER);
        return root;
    }

    // ---------- Pantalla dedicada: Mis préstamos (DATOS REALES) ----------
    private JPanel buildLoansScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Mis préstamos");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Consulta el estado de tus préstamos, cuotas pendientes y detalles.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Body: 2 columnas
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 16, 16, 16);
        c.fill = GridBagConstraints.BOTH;

        // ---- Obtener préstamos reales ----
        List<Loan> loans = new ArrayList<>();
        if (loanService != null && client != null) {
            loans = loanService.getLoansByClientId(client.getClientId());
        }
        final List<Loan> loansForClient = loans;

        // Columna izquierda: tabla de préstamos
        String[] cols = {"ID", "Monto", "Saldo", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 3) ? Status.class : String.class;
            }
        };

        for (Loan loan : loansForClient) {
            long remaining = getRemainingBalance(loan);
            Status st = statusFromString(loan.getStatus());
            model.addRow(new Object[]{
                    "#" + loan.getLoanId(),
                    "₡ " + formatMoney(loan.getAmount()),
                    "₡ " + formatMoney(remaining),
                    st
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(fUI);
        table.getTableHeader().setFont(fSmall);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());

        JScrollPane spTable = new JScrollPane(table);
        spTable.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(spTable, BorderLayout.CENTER);

        c.gridx = 0; c.gridy = 0; c.weightx = 0.55; c.weighty = 1.0;
        body.add(left, c);

        // Columna derecha: detalle
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel lblDetalle = new JLabel("Detalle del préstamo");
        lblDetalle.setFont(fUI);
        lblDetalle.setForeground(TEXT_DARK);
        lblDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel detailCard = new RoundedPanel(16, new Color(248, 249, 252));
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        detailCard.setBorder(new EmptyBorder(14, 14, 14, 14));
        detailCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dId = new JLabel("ID: —");
        JLabel dMonto = new JLabel("Monto: —");
        JLabel dSaldo = new JLabel("Saldo: —");
        JLabel dEstado = new JLabel("Estado: —");
        JLabel dPlazo = new JLabel("Plazo: —");
        JLabel dInteres = new JLabel("Tasa interés: —");
        JLabel dVencimiento = new JLabel("Fecha de vencimiento: —");

        for (JLabel l : new JLabel[]{dId, dMonto, dSaldo, dEstado, dPlazo, dInteres, dVencimiento}) {
            l.setFont(fSmall);
            l.setForeground(TEXT_DARK);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailCard.add(l);
            detailCard.add(Box.createVerticalStrut(4));
        }

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.add(Box.createHorizontalGlue());

        JButton btnPagarCuota = primaryButton("Ir a pagos");
        btnPagarCuota.setPreferredSize(new Dimension(150, 36));
        btnPagarCuota.setMaximumSize(new Dimension(200, 36));
        btnPagarCuota.addActionListener(e -> mostrarPagos());

        JButton btnVerPlan = new JButton("Ver plan de pagos");
        btnVerPlan.setFocusPainted(false);
        btnVerPlan.setBackground(new Color(240, 240, 240));
        btnVerPlan.setForeground(TEXT_DARK);
        btnVerPlan.setFont(fSmall);
        btnVerPlan.setBorder(new EmptyBorder(8, 14, 8, 14));
        btnVerPlan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí podrías mostrar un plan de pagos (tabla de amortización) calculado desde el Loan.");
        });

        actions.add(btnVerPlan);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(btnPagarCuota);

        right.add(lblDetalle);
        right.add(Box.createVerticalStrut(12));
        right.add(detailCard);
        right.add(Box.createVerticalStrut(12));
        right.add(actions);
        right.add(Box.createVerticalGlue());

        c.gridx = 1; c.gridy = 0; c.weightx = 0.45; c.weighty = 1.0;
        body.add(right, c);

        // Actualizar detalle al seleccionar préstamo
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < loansForClient.size()) {
                    Loan loan = loansForClient.get(row);
                    long remaining = getRemainingBalance(loan);
                    Status st = statusFromString(loan.getStatus());

                    dId.setText("ID: #" + loan.getLoanId());
                    dMonto.setText("Monto: ₡ " + formatMoney(loan.getAmount()));
                    dSaldo.setText("Saldo: ₡ " + formatMoney(remaining));
                    dEstado.setText("Estado: " + st.text);
                    dPlazo.setText("Plazo: " + loan.getTermMonths() + " meses");
                    dInteres.setText("Tasa interés: " + loan.getInterestRate() + " %");
                    dVencimiento.setText("Fecha de vencimiento: " + formatDate(loan.getEndDate()));
                }
            }
        });

        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }

        card.add(body, BorderLayout.CENTER);

        GridBagConstraints wrapperC = new GridBagConstraints();
        wrapperC.gridx = 0; wrapperC.gridy = 0;
        wrapperC.weightx = 1.0; wrapperC.weighty = 1.0;
        wrapperC.fill = GridBagConstraints.BOTH;
        wrap.add(card, wrapperC);

        root.add(wrap, BorderLayout.CENTER);
        return root;
    }

    // ---------- Pantalla dedicada: Pagos (usa Payment reales) ----------
    private JPanel buildPaymentsScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(16, 16, 0, 16));

        JLabel title = new JLabel("Pagos");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Consulta el historial de pagos realizados y el resumen de tus movimientos.");
        subtitle.setFont(fSmall);
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // Body: 2 columnas
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 16, 16, 16);
        c.fill = GridBagConstraints.BOTH;

        // ---- Datos reales de pagos ----
        List<Payment> payments = new ArrayList<>();
        if (paymentService != null && client != null) {
            payments = paymentService.getPaymentsForClient(client.getClientId());
        }

        // Columna izquierda: historial de pagos
        String[] cols = {"Fecha", "Préstamo", "Monto", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        long totalPagado = 0;
        Payment ultimoPago = null;

        for (Payment p : payments) {
            String fecha = formatDate(p.getPaymentDate());
            String loanCode = (p.getLoanId() != null)
                    ? "#" + p.getLoanId().getLoanId()
                    : "—";
            long amount = p.getAmountPaid();
            totalPagado += amount;
            String monto = "₡ " + formatMoney(amount);

            model.addRow(new Object[]{fecha, loanCode, monto, "Pagado"});

            if (ultimoPago == null || (p.getPaymentDate() != null &&
                    p.getPaymentDate().after(ultimoPago.getPaymentDate()))) {
                ultimoPago = p;
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(fUI);
        table.getTableHeader().setFont(fSmall);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane spTable = new JScrollPane(table);
        spTable.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        JLabel lblSub = new JLabel("Historial de pagos");
        lblSub.setFont(fUI);
        lblSub.setForeground(TEXT_DARK);
        lblSub.setBorder(new EmptyBorder(8, 8, 0, 8));

        left.add(lblSub, BorderLayout.NORTH);
        left.add(spTable, BorderLayout.CENTER);

        c.gridx = 0; c.gridy = 0; c.weightx = 0.6; c.weighty = 1.0;
        body.add(left, c);

        // Columna derecha: resumen de pagos
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel lblResumen = new JLabel("Resumen de tus pagos");
        lblResumen.setFont(fUI);
        lblResumen.setForeground(TEXT_DARK);
        lblResumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel resumen = new RoundedPanel(16, new Color(248, 249, 252));
        resumen.setLayout(new BoxLayout(resumen, BoxLayout.Y_AXIS));
        resumen.setBorder(new EmptyBorder(14, 14, 14, 14));
        resumen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rCantidadPagos = new JLabel("Cantidad de pagos: " + payments.size());
        JLabel rTotalPagado = new JLabel("Total pagado: ₡ " + formatMoney(totalPagado));
        String ultimoTexto;
        if (ultimoPago != null) {
            ultimoTexto = "Último pago: ₡ " + formatMoney(ultimoPago.getAmountPaid()) +
                    " — " + formatDate(ultimoPago.getPaymentDate());
        } else {
            ultimoTexto = "Último pago: —";
        }
        JLabel rUltimoPago = new JLabel(ultimoTexto);

        for (JLabel l : new JLabel[]{rCantidadPagos, rTotalPagado, rUltimoPago}) {
            l.setFont(fSmall);
            l.setForeground(TEXT_DARK);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            resumen.add(l);
            resumen.add(Box.createVerticalStrut(4));
        }

        JLabel helper = new JLabel("<html><span style='color:#666666;'>Los montos mostrados corresponden a pagos ya realizados.</span></html>");
        helper.setFont(fSmall);
        helper.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Acciones
        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnVerPrestamos = primaryButton("Ver préstamos");
        btnVerPrestamos.setPreferredSize(new Dimension(180, 38));
        btnVerPrestamos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnVerPrestamos.addActionListener(e -> mostrarMisPrestamos());

        JButton btnNuevoPago = new JButton("Registrar pago manual");
        btnNuevoPago.setFocusPainted(false);
        btnNuevoPago.setBackground(new Color(240, 240, 240));
        btnNuevoPago.setForeground(TEXT_DARK);
        btnNuevoPago.setFont(fSmall);
        btnNuevoPago.setBorder(new EmptyBorder(8, 14, 8, 14));
        btnNuevoPago.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí podrías abrir un formulario para registrar un pago nuevo y guardarlo en payments.dat.");
        });

        actions.add(btnNuevoPago);
        actions.add(Box.createHorizontalStrut(8));
        actions.add(btnVerPrestamos);

        right.add(lblResumen);
        right.add(Box.createVerticalStrut(12));
        right.add(resumen);
        right.add(Box.createVerticalStrut(12));
        right.add(helper);
        right.add(Box.createVerticalStrut(16));
        right.add(actions);
        right.add(Box.createVerticalGlue());

        c.gridx = 1; c.gridy = 0; c.weightx = 0.4; c.weighty = 1.0;
        body.add(right, c);

        card.add(body, BorderLayout.CENTER);

        GridBagConstraints wrapperC = new GridBagConstraints();
        wrapperC.gridx = 0; wrapperC.gridy = 0;
        wrapperC.weightx = 1.0; wrapperC.weighty = 1.0;
        wrapperC.fill = GridBagConstraints.BOTH;
        wrap.add(card, wrapperC);

        root.add(wrap, BorderLayout.CENTER);
        return root;
    }

    // ---------- Card: Notificaciones (de momento mock) ----------
    private JComponent buildCardNotificaciones() {
        CardPanel card = new CardPanel();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Notificaciones");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(12));
        content.add(notifRow("Documento pendiente de firma", "Hoy",
                new Color(255, 243, 224), new Color(120, 98, 43)));
        content.add(Box.createVerticalStrut(8));
        content.add(notifRow("Pago registrado recientemente", "Hace 1 h",
                new Color(232, 248, 237), new Color(39, 125, 65)));
        content.add(Box.createVerticalStrut(8));
        content.add(notifRow("Nuevo mensaje del operador", "Ayer",
                new Color(227, 242, 253), new Color(30, 136, 229)));
        content.add(Box.createVerticalGlue());

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent notifRow(String title, String when, Color bg, Color fg) {
        RoundedPanel row = new RoundedPanel(12, bg);
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 12, 4, 12));

        JLabel l = new JLabel(title);
        l.setFont(fUI);
        l.setForeground(new Color(30, 30, 30));

        JLabel r = new JLabel(when);
        r.setFont(fSmall);
        r.setForeground(new Color(90, 90, 90));
        r.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.WEST);
        row.add(r, BorderLayout.EAST);
        return row;
    }

    // ---------- Card: Tabla Mis préstamos (dashboard, top 3) ----------
    private JComponent buildCardTablaPrestamos() {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout(0, 8));

        JLabel title = new JLabel("Mis préstamos");
        title.setFont(fH2);
        title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(16, 16, 0, 16));
        card.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Monto", "Saldo", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 3) ? Status.class : String.class;
            }
        };

        List<Loan> loans = new ArrayList<>();
        if (loanService != null && client != null) {
            loans = loanService.getLoansByClientId(client.getClientId());
        }

        int count = 0;
        for (Loan loan : loans) {
            long remaining = getRemainingBalance(loan);
            Status st = statusFromString(loan.getStatus());
            model.addRow(new Object[]{
                    "#" + loan.getLoanId(),
                    "₡ " + formatMoney(loan.getAmount()),
                    "₡ " + formatMoney(remaining),
                    st
            });
            count++;
            if (count >= 3) break; // solo los primeros 3 en el dashboard
        }

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(fUI);
        table.getTableHeader().setFont(fSmall);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (table.getColumnCount() > 3) {
            table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(0, 12, 12, 12));
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    // ====================== Helpers UI ======================
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

    private String formatMoney(long value) {
        return String.format("%,d", value).replace(',', ' ');
    }

    private String formatDate(Date date) {
        if (date == null) return "—";
        return dateFormat.format(date);
    }

    // Calcula saldo real usando Payments (totalToPay - suma de pagos)
    private long getRemainingBalance(Loan loan) {
        if (loan == null) return 0;
        long totalToPay = loan.getTotalToPay();  // campo de tu entidad
        long paid = 0;
        if (paymentService != null) {
            List<Payment> pagos = paymentService.getPaymentsForLoan(loan.getLoanId());
            for (Payment p : pagos) {
                paid += p.getAmountPaid();
            }
        }
        long remaining = totalToPay - paid;
        return Math.max(remaining, 0);
    }

    private Status statusFromString(String status) {
        if (status == null) {
            return new Status("DESCONOCIDO", new Color(240, 240, 240), new Color(90, 90, 90));
        }
        String s = status.toUpperCase();
        if (s.contains("VIGENTE")) {
            return new Status("VIGENTE",
                    new Color(232, 248, 237), new Color(39, 125, 65));
        } else if (s.contains("LIQUIDADO")) {
            return new Status("LIQUIDADO",
                    new Color(227, 242, 253), new Color(30, 136, 229));
        } else if (s.contains("ATRASO") || s.contains("MORA")) {
            return new Status("ATRASO",
                    new Color(255, 235, 238), new Color(176, 0, 32));
        }
        return new Status(status,
                new Color(240, 240, 240), new Color(90, 90, 90));
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

    // Chip (estado / badge)
    static class ChipLabel extends JComponent {
        private final String text;
        private final Color bg, fg;
        ChipLabel(String text, Color bg, Color fg) {
            this.text = text; this.bg = bg; this.fg = fg;
            setPreferredSize(new Dimension(110, 28));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setColor(fg);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(text, 12, ty);
            g2.dispose();
        }
    }

    // Status object + renderer para la tabla
    static class Status {
        final String text; final Color bg; final Color fg;
        Status(String t, Color bg, Color fg) { this.text = t; this.bg = bg; this.fg = fg; }
    }

    static class StatusRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int col) {
            Status s = (Status) value;
            ChipLabel chip = new ChipLabel(s.text, s.bg, s.fg);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
            p.setBackground(Color.WHITE);
            p.add(chip);
            return p;
        }
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
