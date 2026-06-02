package view;

import util.ThemeManager;
import controller.InvestorReadOnlyController;
import model.Optimization;
import model.PortfolioItem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Camada View (Swing) - InvestorReadOnlyDashboardView.
 * Tela principal da jornada do Investidor após login.
 * Exibe o portfólio otimizado em modo somente leitura.
 * 
 * @author leandrorocha
 */
public class InvestorReadOnlyDashboardView extends javax.swing.JFrame {

    // Componentes obrigatórios
    private JLabel lblInvestorName;
    private JLabel lblRiskProfile;
    private JLabel lblExpectedReturn;
    private JLabel lblTotalRisk;
    private JTable tblPortfolio;
    private JButton btnSair;

    private DefaultTableModel tableModel;
    private JPanel sidebar;
    private JLabel lblLogo;
    private JLabel lblUserName;
    private InvestorReadOnlyController controller;
    private double[] donutPercentuais = {45, 25, 15, 10, 5};
    private String[] donutLabels = {"TESOURO2029", "CDB_PRE", "KNRI11", "BOVA11", "BBDC4"};
    private JPanel pnlDonut;

    public InvestorReadOnlyDashboardView() {
        configurarInterface();
        // dados simulados apenas para preview visual isolado (sem controller)
        // quando setController + controller.loadMyPortfolio() são chamados,
        // os dados reais substituem tudo
    }

    public void setController(InvestorReadOnlyController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS CONTRATUAIS ---

    public void showView() { this.setVisible(true); }

    public void setInvestorName(String name) {
        lblInvestorName.setText(name);
        lblUserName.setText("Investidor: " + name);
    }

    public void setRiskProfile(String profile) {
        lblRiskProfile.setText("Perfil: " + profile);
    }

    public void setExpectedReturn(String value) {
        lblExpectedReturn.setText(value);
    }

    public void setTotalRisk(String value) {
        lblTotalRisk.setText(value);
    }

    // monta a tabela e o grafico donut com os itens otimizados da carteira
    public void loadPortfolioItems(List<PortfolioItem> items) {
        tableModel.setRowCount(0);
        if (items == null || items.isEmpty()) {
            donutPercentuais = new double[]{100};
            donutLabels = new String[]{"—"};
            if (pnlDonut != null) pnlDonut.repaint();
            return;
        }

        donutPercentuais = new double[items.size()];
        donutLabels = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            PortfolioItem item = items.get(i);
            String ticker = item.getAsset() != null ? item.getAsset().getTicker() : ("Ativo " + item.getAssetId());
            String nome = item.getAsset() != null ? item.getAsset().getName() : "—";
            String categoria = item.getAsset() != null ? item.getAsset().getCategory() : "—";

            BigDecimal perc = item.getSuggestedPercentage() != null
                    ? item.getSuggestedPercentage().multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;
            BigDecimal valorPosicao = item.getPositionValue() != null ? item.getPositionValue() : BigDecimal.ZERO;

            tableModel.addRow(new Object[]{
                    ticker, nome, categoria,
                    perc.setScale(1, RoundingMode.HALF_UP) + "%",
                    "R$ " + valorPosicao.setScale(2, RoundingMode.HALF_UP)
            });

            donutLabels[i] = ticker;
            donutPercentuais[i] = perc.doubleValue();
        }
        if (pnlDonut != null) pnlDonut.repaint();
    }

    // atualiza os cards de retorno esperado e risco total
    public void setOptimizationSummary(Optimization optimization) {
        if (optimization == null) {
            if (lblExpectedReturn != null) lblExpectedReturn.setText("—");
            if (lblTotalRisk != null) lblTotalRisk.setText("—");
            return;
        }
        if (lblExpectedReturn != null && optimization.getExpectedReturn() != null) {
            BigDecimal ret = optimization.getExpectedReturn().multiply(BigDecimal.valueOf(100));
            lblExpectedReturn.setText(ret.setScale(2, RoundingMode.HALF_UP) + "% a.a.");
        }
        if (lblTotalRisk != null && optimization.getTotalRisk() != null) {
            BigDecimal risco = optimization.getTotalRisk().multiply(BigDecimal.valueOf(100));
            lblTotalRisk.setText(risco.setScale(2, RoundingMode.HALF_UP) + "% a.a.");
        }
    }

    public void showError(String message) {
        util.MessageUtil.showError(this, message);
    }

    // mostra um aviso calmo na propria tela (sem popup de erro) quando o
    // investidor ainda nao tem carteira otimizada pra exibir
    public void mostrarEstadoVazio(String mensagem) {
        tableModel.setRowCount(0);
        donutPercentuais = new double[]{100};
        donutLabels = new String[]{"sem dados"};
        if (pnlDonut != null) pnlDonut.repaint();
        if (lblExpectedReturn != null) lblExpectedReturn.setText("—");
        if (lblTotalRisk != null) lblTotalRisk.setText("—");
        if (lblRiskProfile != null) lblRiskProfile.setText(mensagem);
    }

    // helper de teste: quantas linhas a tabela da carteira esta mostrando
    public int getRowCountForTest() { return tableModel.getRowCount(); }

    public void closeView() { this.dispose(); }

    /**
     * Montagem da interface somente leitura com Sidebar.
     */
    private void configurarInterface() {
        setTitle("Finance Team - Meu Portfólio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 850));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.getBg());

        // --- SIDEBAR ---
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(ThemeManager.getCard());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeManager.getBorder()));

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        lblLogo = new JLabel();
        atualizarLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        lblUserName = new JLabel("Investidor: Carregando...", SwingConstants.CENTER);
        lblUserName.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblUserName.setForeground(ThemeManager.getSubText());
        lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUserName);

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Menu com item ativo destacado
        JButton btnPortfolio = criarBotaoMenu("Meu Portfólio");
        btnPortfolio.setForeground(ThemeManager.getAccent());
        btnSair = criarBotaoMenu("Sair");

        btnSair.addActionListener(e -> {
            if (controller != null) controller.logout();
            else dispose();
        });

        sidebar.add(btnPortfolio);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSair);

        sidebar.add(Box.createVerticalGlue());

        // Toggle de Tema profissional
        String textoTema = ThemeManager.isDarkMode() ? "Modo Claro" : "Modo Escuro";
        JButton btnTheme = new JButton(textoTema);
        btnTheme.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnTheme.setForeground(ThemeManager.getSubText());
        btnTheme.setContentAreaFilled(false);
        btnTheme.setBorderPainted(true);
        btnTheme.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(6, 12, 6, 12)
        ));
        btnTheme.setFocusPainted(false);
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTheme.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTheme.setMaximumSize(new Dimension(160, 32));

        btnTheme.addActionListener(e -> {
            ThemeManager.toggleTheme();
            configurarInterface();
            SwingUtilities.updateComponentTreeUI(this);
        });

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTheme);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- ÁREA CENTRAL ---
        JPanel areaPainelDireita = new JPanel(new BorderLayout(0, 20));
        areaPainelDireita.setBackground(ThemeManager.getBg());
        areaPainelDireita.setBorder(new EmptyBorder(50, 60, 50, 60));

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlHeader.setOpaque(false);

        lblInvestorName = new JLabel("Meu Portfólio");
        lblInvestorName.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblInvestorName.setForeground(ThemeManager.getText());

        lblRiskProfile = new JLabel("Perfil: —", SwingConstants.RIGHT);
        lblRiskProfile.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRiskProfile.setForeground(ThemeManager.getAccent());

        pnlHeader.add(lblInvestorName);
        pnlHeader.add(lblRiskProfile);
        areaPainelDireita.add(pnlHeader, BorderLayout.NORTH);

        // Painel Central
        JPanel pnlCentral = new JPanel(new BorderLayout(0, 25));
        pnlCentral.setOpaque(false);
        pnlCentral.setBorder(new EmptyBorder(30, 0, 0, 0));

        // Cards de Performance
        JPanel pnlCards = new JPanel(new GridLayout(1, 2, 25, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 110));

        pnlCards.add(criarCardInfo("Retorno Esperado", new Color(46, 160, 67)));
        pnlCards.add(criarCardInfo("Risco (Volatilidade)", new Color(248, 81, 73)));
        pnlCentral.add(pnlCards, BorderLayout.NORTH);

        // Painel do meio: Gráfico Donut + Tabela
        JPanel pnlMeio = new JPanel(new BorderLayout(20, 0));
        pnlMeio.setOpaque(false);

        // Gráfico Donut de alocação
        pnlDonut = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

                int cx = getWidth() / 2;
                int cy = getHeight() / 2 - 40;
                int raio = Math.min(getWidth(), getHeight()) / 3;

                double[] percentuais = donutPercentuais;
                Color[] cores = {new Color(31, 111, 235), new Color(46, 160, 67), new Color(255, 166, 0), new Color(248, 81, 73), new Color(163, 113, 247), new Color(0, 191, 165), new Color(255, 87, 34), new Color(120, 144, 156)};
                String[] labels = donutLabels;

                int startAngle = 0;
                for (int i = 0; i < percentuais.length; i++) {
                    int arcAngle = (int) (percentuais[i] * 3.6);
                    g2.setColor(cores[i % cores.length]);
                    g2.fillArc(cx - raio, cy - raio, raio * 2, raio * 2, startAngle, arcAngle);
                    startAngle += arcAngle;
                }

                int raioInterno = raio - 22;
                g2.setColor(ThemeManager.getCard());
                g2.fillOval(cx - raioInterno, cy - raioInterno, raioInterno * 2, raioInterno * 2);

                g2.setColor(ThemeManager.getText());
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                String totalTxt = "R$ 100k";
                int tw = g2.getFontMetrics().stringWidth(totalTxt);
                g2.drawString(totalTxt, cx - tw / 2, cy + 5);

                // Legenda vertical abaixo do donut
                int legendaY = cy + raio + 20;
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                for (int i = 0; i < labels.length; i++) {
                    int lx = 15;
                    g2.setColor(cores[i % cores.length]);
                    g2.fillRoundRect(lx, legendaY + i * 18, 10, 10, 3, 3);
                    g2.setColor(ThemeManager.getSubText());
                    g2.drawString(labels[i] + "  " + (int) percentuais[i] + "%", lx + 15, legendaY + i * 18 + 10);
                }
                g2.dispose();
            }
        };
        pnlDonut.setOpaque(false);
        pnlDonut.setPreferredSize(new Dimension(220, 0));
        pnlMeio.add(pnlDonut, BorderLayout.WEST);

        // Tabela dentro do pnlMeio
        JPanel pnlTabela = new JPanel(new BorderLayout(0, 10));
        pnlTabela.setOpaque(false);

        JLabel lblSubtitulo = new JLabel("COMPOSIÇÃO DO SEU PORTFÓLIO OTIMIZADO");
        lblSubtitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSubtitulo.setForeground(ThemeManager.getSubText());
        pnlTabela.add(lblSubtitulo, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new Object[]{"Ticker", "Nome do Ativo", "Categoria", "Alocação (%)", "Valor (R$)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblPortfolio = new JTable(tableModel);
        ThemeManager.estilizarTabela(tblPortfolio);

        JScrollPane scrollPane = new JScrollPane(tblPortfolio);
        ThemeManager.estilizarScrollPane(scrollPane);
        pnlTabela.add(scrollPane, BorderLayout.CENTER);

        pnlMeio.add(pnlTabela, BorderLayout.CENTER);
        pnlCentral.add(pnlMeio, BorderLayout.CENTER);
        areaPainelDireita.add(pnlCentral, BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(areaPainelDireita, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel criarCardInfo(String titulo, Color corValor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(ThemeManager.getSubText());

        JLabel v = new JLabel("—");
        v.setFont(new Font("SansSerif", Font.BOLD, 26));
        v.setForeground(corValor);

        // Guarda referência para atualização
        if (titulo.contains("Retorno")) {
            lblExpectedReturn = v;
        } else {
            lblTotalRisk = v;
        }

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(ThemeManager.getText());
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

    private void carregarDadosSimulados() {
        lblUserName.setText("Investidor: Ana Silva Mendes");
        lblInvestorName.setText("Meu Portfólio");
        lblRiskProfile.setText("Perfil: CONSERVADOR");
        lblExpectedReturn.setText("8.45% a.a.");
        lblTotalRisk.setText("3.20% a.a.");

        tableModel.addRow(new Object[]{"TESOURO2029", "Tesouro IPCA+ 2029", "Renda Fixa", "45.0%", "R$ 45.000,00"});
        tableModel.addRow(new Object[]{"CDB_PRE", "CDB Prefixado Itaú 12%", "Renda Fixa", "25.0%", "R$ 25.000,00"});
        tableModel.addRow(new Object[]{"KNRI11", "Kinea Renda Imobiliária FII", "FII", "15.0%", "R$ 15.000,00"});
        tableModel.addRow(new Object[]{"BOVA11", "iShares Ibovespa ETF", "ETF", "10.0%", "R$ 10.000,00"});
        tableModel.addRow(new Object[]{"BBDC4", "Banco Bradesco S.A.", "Ação", "5.0%", "R$ 5.000,00"});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InvestorReadOnlyDashboardView janela = new InvestorReadOnlyDashboardView();
            janela.setVisible(true);
        });
    }
}
