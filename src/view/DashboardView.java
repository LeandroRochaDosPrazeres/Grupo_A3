package view;

import util.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * DashboardView: Layout de alta fidelidade com alternador de tema por ícone.
 * Foco em indicadores analíticos e navegação limpa[cite: 108, 410].
 */
public class DashboardView extends JFrame {

    private JPanel sidebar, contentArea;
    private JLabel lblLogo;

    public DashboardView() {
        configurarInterface();
    }

    private void configurarInterface() {
        setTitle("Finance Team - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 850));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.getBg());

        // --- SIDEBAR (DIVISÕES E CENTRALIZAÇÃO) [cite: 30] ---
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(ThemeManager.getCard());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, 
                ThemeManager.isDarkMode() ? new Color(48, 54, 61) : new Color(216, 222, 228)));

        // 1. Logo Centralizada
        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
        lblLogo = new JLabel();
        atualizarLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 60)));

        // 2. Menu de Navegação 
        String[] menus = {"Dashboard", "Investidores", "Ativos", "Portfólios"};
        for (String m : menus) {
            sidebar.add(criarBotaoMenu(m, m.equals("Dashboard")));
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Empurra o ícone para o ponto mais baixo possível
        sidebar.add(Box.createVerticalGlue());
        
        // --- 3. ALTERNADOR DE TEMA (ÍCONE MINIMALISTA NO CANTO INFERIOR) ---
        // Símbolos monocromáticos inspirados na sua imagem
        String iconSymbol = ThemeManager.isDarkMode() ? "☾" : "☼"; 
        JButton btnTheme = new JButton(iconSymbol);
        btnTheme.setFont(new Font("SansSerif", Font.PLAIN, 22));
        btnTheme.setForeground(ThemeManager.getSubText());
        btnTheme.setContentAreaFilled(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setFocusPainted(false);
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Painel com margens mínimas (5px) para ficar bem no cantinho
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        themePanel.setOpaque(false);
        themePanel.setMaximumSize(new Dimension(260, 50));
        themePanel.add(btnTheme);
        
        btnTheme.addActionListener(e -> {
            ThemeManager.toggleTheme();
            configurarInterface();
            SwingUtilities.updateComponentTreeUI(this);
        });
        sidebar.add(themePanel);

        // --- ÁREA DE CONTEÚDO [cite: 107] ---
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(ThemeManager.getBg());
        contentArea.setBorder(new EmptyBorder(50, 60, 50, 60));

        // Cabeçalho e Cards [cite: 411]
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Visão Geral");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 34));
        lblTitulo.setForeground(ThemeManager.getText());
        headerPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(30, 0, 40, 0));
        cardsPanel.add(criarCard("Usuários", "12"));
        cardsPanel.add(criarCard("Investidores", "45"));
        cardsPanel.add(criarCard("Portfólios", "38"));
        cardsPanel.add(criarCard("Otimizações", "156"));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        contentArea.add(headerPanel, BorderLayout.NORTH);

        // Gráfico Analítico (Eixos e Grid mantidos) [cite: 63]
        contentArea.add(criarPainelBarras(), BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelBarras() {
        JPanel painel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.setColor(ThemeManager.isDarkMode() ? new Color(48, 54, 61) : new Color(216, 222, 228));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 15, 15));
                
                int pad = 80;
                int gH = getHeight() - 2 * pad;
                int gW = getWidth() - 2 * pad;
                int xB = pad;
                int yB = getHeight() - pad;
                
                String[] labs = {"Usuários", "Investidores", "Portfólios", "Otimizações"};
                int[] vals = {12, 45, 38, 156};
                int max = 200;

                // --- DESENHO DO GRID ---
                g2.setStroke(new BasicStroke(1));
                for(int i = 0; i <= max; i += 50) {
                    int y = yB - (i * gH) / max;
                    g2.setColor(ThemeManager.isDarkMode() ? new Color(255, 255, 255, 15) : new Color(0, 0, 0, 10));
                    g2.drawLine(xB, y, xB + gW, y);
                    g2.setColor(ThemeManager.getSubText());
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    g2.drawString(String.valueOf(i), xB - 35, y + 5);
                }

                g2.setColor(ThemeManager.getSubText());
                g2.drawLine(xB, yB, xB + gW, yB); 
                g2.drawLine(xB, yB, xB, yB - gH); 

                // --- BARRAS [cite: 25] ---
                int bW = gW / labs.length - 60;
                for (int i = 0; i < labs.length; i++) {
                    int valH = (vals[i] * gH) / max;
                    int x = xB + (i * (gW / labs.length)) + 30;
                    g2.setColor(ThemeManager.getAccent());
                    g2.fill(new RoundRectangle2D.Double(x, yB - valH, bW, valH, 8, 8));
                    g2.setColor(ThemeManager.getText());
                    g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                    g2.drawString(String.valueOf(vals[i]), x + bW/2 - 12, yB - valH - 12);
                    g2.setColor(ThemeManager.getSubText());
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    g2.drawString(labs[i], x + bW/2 - 35, yB + 25);
                }
                g2.dispose();
            }
        };
        painel.setOpaque(false);
        return painel;
    }

    private JPanel criarCard(String titulo, String valor) {
        JPanel card = new JPanel(new GridLayout(2, 1)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(ThemeManager.isDarkMode() ? new Color(48, 54, 61) : new Color(216, 222, 228));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel t = new JLabel(titulo.toUpperCase());
        t.setFont(new Font("SansSerif", Font.BOLD, 11));
        t.setForeground(ThemeManager.getSubText());
        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 26));
        v.setForeground(ThemeManager.getAccent());
        card.add(t); card.add(v);
        return card;
    }

    private JButton criarBotaoMenu(String texto, boolean ativo) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", ativo ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(ativo ? ThemeManager.getAccent() : ThemeManager.getText());
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 0, 12, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 45));
        return btn;
    }

    private void atualizarLogo() {
        String path = ThemeManager.isDarkMode() 
            ? "/view/resources/logo_sem_background_darkmode.png" 
            : "/view/resources/logo_sem_background_lightmode.png";
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            lblLogo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(140, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) { 
            lblLogo.setText("FINANCE TEAM");
            lblLogo.setForeground(ThemeManager.getText());
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardView().setVisible(true));
    }
}