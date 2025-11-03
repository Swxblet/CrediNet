package GUI;

import BL.FontLoader;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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

    public ClientMenu() {
        setTitle("CrediNet | Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("Proyecto/assets/img/miniLogo.png").getImage());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_APP);

        // Sidebar (WEST)
        root.add(buildSidebar(), BorderLayout.WEST);

        // Centro: Topbar + contenido
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG_APP);
        center.add(buildTopbar(), BorderLayout.NORTH);
        center.add(buildContent(), BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
    }

    // ======================
    // Sidebar degradado + logo + menú
    // ======================
    private JComponent buildSidebar() {
        GradientPanel side = new GradientPanel(BLUE_1, BLUE_2);
        side.setPreferredSize(new Dimension(260, getHeight()));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(24, 20, 24, 20));

        // Logo circular
        JPanel logoWrap = new JPanel(null);
        logoWrap.setOpaque(false);
        logoWrap.setMaximumSize(new Dimension(220, 90));
        RoundedPanel logoCircle = new RoundedPanel(80, Color.WHITE);
        logoCircle.setBounds(60, 10, 80, 80);
        JLabel cn = new JLabel("CN", SwingConstants.CENTER);
        cn.setFont(safeFont(24, Font.BOLD));
        cn.setForeground(BLUE_2);
        cn.setBounds(60, 10, 80, 80);
        logoWrap.add(logoCircle);
        logoWrap.add(cn);

        side.add(logoWrap);
        side.add(Box.createVerticalStrut(10));

        // Items de navegación
        side.add(navItem("Dashboard", true));
        side.add(Box.createVerticalStrut(14));
        side.add(navItem("Mis Préstamos", false));
        side.add(Box.createVerticalStrut(14));
        side.add(navItem("Pagos", false));
        side.add(Box.createVerticalStrut(14));
        side.add(navItem("Estados de Cuenta", false));
        side.add(Box.createVerticalStrut(14));
        side.add(navItem("Perfil", false));

        side.add(Box.createVerticalGlue());
        return side;
    }

    private JComponent navItem(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(fUI);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(12, 16, 12, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel wrap = new RoundedPanel(16, active ? new Color(255,255,255,32) : new Color(255,255,255,20));
        wrap.setLayout(new BorderLayout());
        wrap.add(btn, BorderLayout.CENTER);
        wrap.setMaximumSize(new Dimension(220, 44));
        wrap.setBorder(new EmptyBorder(0,4,0,4));
        return wrap;
    }

    // ======================
    // Topbar con buscador y chip usuario
    // ======================
    private JComponent buildTopbar() {
        JPanel top = new JPanel(null) {
            @Override public Dimension getPreferredSize() { return new Dimension(100, 76); }
        };
        top.setBackground(Color.WHITE);

        JLabel brand = new JLabel("CrediNet");
        brand.setFont(fTitle);
        brand.setForeground(TEXT_DARK);
        brand.setBounds(20, 18, 180, 40);
        top.add(brand);

        // Buscador redondeado
        JTextField search = new RoundedTextField(24);
        search.setText("Buscar en tu cuenta...");
        search.setFont(fSmall);
        search.setForeground(new Color(130,130,130));
        search.setBackground(new Color(248,248,248));
        search.setBounds(220, 18, 420, 40);
        top.add(search);

        // Chip "Hola, Madeline"
        JPanel chip = new RoundedPanel(20, new Color(245,248,255));
        chip.setBorder(new CompoundLineBorder(new Color(210,225,255)));
        chip.setLayout(null);
        chip.setBounds(980, 18, 190, 40);
        JPanel dot = new RoundedPanel(15, BLUE_1);
        dot.setBounds(10, 7, 26, 26);
        chip.add(dot);
        JLabel hi = new JLabel("Hola, Madeline");
        hi.setFont(fSmall);
        hi.setForeground(new Color(22,40,77));
        hi.setBounds(44, 9, 130, 22);
        chip.add(hi);

        top.add(chip);
        return top;
    }

    // ======================
    // Contenido (cards en 2x2)
    // ======================
    private JComponent buildContent() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(BG_APP);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.fill = GridBagConstraints.BOTH;

        // Card 1: Préstamo activo
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5; c.weighty = 0.5;
        grid.add(buildCardPrestamoActivo(), c);

        // Card 2: Simulador
        c.gridx = 1; c.gridy = 0;
        grid.add(buildCardSimulador(), c);

        // Card 3: Notificaciones
        c.gridx = 0; c.gridy = 1;
        grid.add(buildCardNotificaciones(), c);

        // Card 4: Mis préstamos
        c.gridx = 1; c.gridy = 1;
        grid.add(buildCardTablaPrestamos(), c);

        return grid;
    }

    // ---------- Card: Préstamo activo ----------
    private JComponent buildCardPrestamoActivo() {
        CardPanel card = new CardPanel();
        card.setLayout(null);

        JLabel title = new JLabel("Préstamo activo");
        title.setFont(fH2); title.setForeground(TEXT_DARK);
        title.setBounds(24, 16, 400, 28);
        card.add(title);

        JLabel saldo = new JLabel("Saldo: ₡ 1 250 000");
        saldo.setFont(fH3); saldo.setForeground(new Color(22,40,77));
        saldo.setBounds(24, 56, 300, 24);
        card.add(saldo);

        JLabel prox = new JLabel("Próxima cuota: ₡ 85 300 — 05 Dic 2025");
        prox.setFont(fUI); prox.setForeground(TEXT_MUTED);
        prox.setBounds(24, 86, 370, 20);
        card.add(prox);

        // Chip estado
        ChipLabel chip = new ChipLabel("VIGENTE", new Color(232,248,237), new Color(39,125,65));
        chip.setBounds(24, 116, 110, 28);
        card.add(chip);

        JButton pagar = primaryButton("Pagar hoy");
        pagar.setBounds(card.getWidth()-220, card.getHeight()-64, 180, 44);
        // reposicionar cuando se asigne tamaño
        card.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                pagar.setBounds(card.getWidth()-204, card.getHeight()-64, 180, 44);
            }
        });
        card.add(pagar);

        return card;
    }

    // ---------- Card: Simulador ----------
    private JComponent buildCardSimulador() {
        CardPanel card = new CardPanel();
        card.setLayout(null);

        JLabel title = new JLabel("Simula tu préstamo");
        title.setFont(fH2); title.setForeground(TEXT_DARK);
        title.setBounds(24, 16, 400, 28);
        card.add(title);

        JLabel lMonto = new JLabel("Monto");
        lMonto.setFont(fUI); lMonto.setForeground(TEXT_MUTED);
        lMonto.setBounds(24, 56, 200, 20);
        card.add(lMonto);

        JSlider sMonto = new JSlider(100000, 2000000, 1250000);
        sMonto.setBounds(24, 80, 420, 40);
        card.add(sMonto);

        JLabel lPlazo = new JLabel("Plazo (meses)");
        lPlazo.setFont(fUI); lPlazo.setForeground(TEXT_MUTED);
        lPlazo.setBounds(24, 120, 200, 20);
        card.add(lPlazo);

        JSlider sPlazo = new JSlider(6, 84, 24);
        sPlazo.setBounds(24, 144, 420, 40);
        card.add(sPlazo);

        JLabel cuota = new JLabel("Cuota estimada: ₡ 85 300");
        cuota.setFont(fH3); cuota.setForeground(new Color(22,40,77));
        cuota.setBounds(24, 190, 300, 24);
        card.add(cuota);

        // cálculo simple (placeholder)
        sMonto.addChangeListener(e -> updateCuota(sMonto, sPlazo, cuota));
        sPlazo.addChangeListener(e -> updateCuota(sMonto, sPlazo, cuota));

        return card;
    }

    private void updateCuota(JSlider sMonto, JSlider sPlazo, JLabel out) {
        double monto = sMonto.getValue();
        double n = sPlazo.getValue();
        double i = 0.019; // 1.9% mensual (demo)
        double cuota = (monto * i) / (1 - Math.pow(1 + i, -n));
        out.setText("Cuota estimada: ₡ " + formatMoney((int)Math.round(cuota)));
    }

    private String formatMoney(int v) {
        return String.format("%,d", v).replace(',', ' ').replace('.', ' ');
    }

    // ---------- Card: Notificaciones ----------
    private JComponent buildCardNotificaciones() {
        CardPanel card = new CardPanel();
        card.setLayout(null);

        JLabel title = new JLabel("Notificaciones");
        title.setFont(fH2); title.setForeground(TEXT_DARK);
        title.setBounds(24, 16, 400, 28);
        card.add(title);

        int y = 56;
        card.add(notifRow("Documento pendiente de firma", "Hoy",
                new Color(255,243,224), new Color(120, 98, 43), y)); y += 56;
        card.add(notifRow("Pago programado en 3 días", "Hace 1 h",
                new Color(232,248,237), new Color(39,125,65), y)); y += 56;
        card.add(notifRow("Nuevo mensaje del operador", "Ayer",
                new Color(227,242,253), new Color(30,136,229), y));
        return card;
    }

    private JComponent notifRow(String title, String when, Color bg, Color fg, int y) {
        JPanel row = new RoundedPanel(12, bg);
        row.setLayout(null);
        row.setBounds(24, y, 520, 40);
        JLabel l = new JLabel(title);
        l.setFont(fUI); l.setForeground(new Color(30,30,30));
        l.setBounds(14, 10, 360, 20);
        JLabel r = new JLabel(when);
        r.setFont(fSmall); r.setForeground(new Color(90,90,90));
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        r.setBounds(520-110, 10, 100, 20);
        row.add(l); row.add(r);
        return row;
    }

    // ---------- Card: Tabla Mis préstamos ----------
    private JComponent buildCardTablaPrestamos() {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout(0, 8));
        JLabel title = new JLabel("Mis préstamos");
        title.setFont(fH2); title.setForeground(TEXT_DARK);
        title.setBorder(new EmptyBorder(16, 16, 0, 16));
        card.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Monto", "Saldo", "Estado"};
        Object[][] data = {
                {"#1024", "₡ 1 500 000", "₡ 1 250 000", new Status("VIGENTE", new Color(232,248,237), new Color(39,125,65))},
                {"#0988", "₡   800 000", "₡   210 000", new Status("LIQUIDADO", new Color(227,242,253), new Color(30,136,229))},
                {"#0901", "₡   500 000", "₡    85 300", new Status("ATRASO", new Color(255,235,238), new Color(176,0,32))}
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 3) ? Status.class : String.class;
            }
        };
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
            // Ajusta la ruta si tu Montserrat está en otra carpeta
            Font f = FontLoader.loadFont("Proyecto/assets/fonts/Montserrat-Regular.ttf", size);
            return f.deriveFont(style, (float) size);
        } catch (Throwable t) {
            return new Font("SansSerif", style, size);
        }
    }

    // Panel con degradado vertical
    static class GradientPanel extends JPanel {
        private final Color c1, c2;
        GradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
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
        RoundedPanel(int radius, Color bg) { this.radius = radius; this.bg = bg; setOpaque(false); }
        public void setBackgroundColor(Color c) { this.bg = c; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Borde fino para chip/topbar
    static class CompoundLineBorder extends AbstractBorder {
        private final Color color;
        CompoundLineBorder(Color color) { this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, 40, 40);
            g2.dispose();
        }
    }

    // Card con sombra sutil
    static class CardPanel extends JPanel {
        CardPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(8, 8, 8, 8));
        }
        @Override protected void paintComponent(Graphics g) {
            int arc = 18;
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // sombra
            g2.setColor(new Color(0,0,0,60));
            g2.fill(new RoundRectangle2D.Float(6, 10, w-12, h-12, arc+6, arc+6));

            // card
            g2.setColor(BG_CARD);
            g2.fill(new RoundRectangle2D.Float(0, 0, w-12, h-12, arc, arc));
            g2.setColor(BORDER_CARD);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w-13, h-13, arc, arc));
            g2.dispose();
            super.paintComponent(g);
            setLayout(new BorderLayout()); // permite BorderLayout si se usa
        }
        @Override public Dimension getPreferredSize() { return new Dimension(540, 260); }
    }

    // Chip (estado / badge)
    static class ChipLabel extends JComponent {
        private final String text;
        private final Color bg, fg;
        ChipLabel(String text, Color bg, Color fg) { this.text = text; this.bg = bg; this.fg = fg; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
            g2.setColor(fg);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            int ty = (getHeight() - fm.getHeight())/2 + fm.getAscent();
            g2.drawString(text, 12, ty);
            g2.dispose();
        }
        @Override public Dimension getPreferredSize() { return new Dimension(110, 28); }
    }

    // Status object + renderer para la tabla
    static class Status {
        final String text; final Color bg; final Color fg;
        Status(String t, Color bg, Color fg){ this.text=t; this.bg=bg; this.fg=fg; }
    }
    static class StatusRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
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
        RoundedTextField(int arc){ this.arc = arc; setBorder(new EmptyBorder(8,14,8,14)); setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(), arc, arc);
            super.paintComponent(g);
            g2.setColor(new Color(225,225,225));
            g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1, arc, arc);
            g2.dispose();
        }
    }

    // ======== Mostrar ========
    public void mostrar() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
