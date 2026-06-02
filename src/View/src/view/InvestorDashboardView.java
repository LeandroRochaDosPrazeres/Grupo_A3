package view;

import util.ThemeManager;
import controller.InvestorController;
import model.Investor;
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
 * Camada View (Swing) - InvestorDashboardView.
 * Tela de resultado do portfólio otimizado (usada pelo gerente após otimizar).
 * 
 * @author leandrorocha
 */
public class InvestorDashboardView extends javax.swing.JPanel {

    private InvestorController controller;

    // Componentes de exibição
    private JLabel lblNomeInvestidor;
    private JLabel lblPerfilRisco;
    private JLabel lblRetornoEsperado;
    private JLabel lblVolatilidade;
    private JTable tblPortfolio;
    private JButton btnVoltar;
    private JButton btnNovoInvestidor;
    private DefaultTableModel tableModel;
    private double[] donutPercentuais = {40, 30, 20, 10};
    private String[] donutLabels = {"PETR4", "VALE3", "IVVB11", "BOVA11"};
    private JPanel pnlDonut;

    public InvestorDashboardView() {
        configurarPainel();
        // dados simulados apenas para preview visual isolado (sem controller)
        // quando setInvestorData + loadPortfolioItems + setOptimizationSummary são chamados,
        // os dados reais substituem tudo
    }

    public void setController(InvestorController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS CONTRATUAIS ---

    public void setInvestorName(String name) {
        lblNomeInvestidor.setText("Investidor: " + name);
    }

    public void setRiskProfile(String profile) {
        lblPerfilRisco.setText("Perfil: " + profile);
    }

    // preenche nome e perfil de risco la em cima usando o objeto Investor
    public void setInvestorData(Investor investor) {
        if (investor == null) return;
        lblNomeInvestidor.setText("Investidor: " + investor.getName());
        if (investor.getRiskProfile() != null) {
            lblPerfilRisco.setText("Perfil: " + investor.getRiskProfile().name());
        }
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

        BigDecimal valorTotal = BigDecimal.ZERO;
        for (PortfolioItem item : items) {
            if (item.getPositionValue() != null) {
                valorTotal = valorTotal.add(item.getPositionValue());
            }
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
            if (lblRetornoEsperado != null) lblRetornoEsperado.setText("—");
            if (lblVolatilidade != null) lblVolatilidade.setText("—");
            return;
        }
        if (lblRetornoEsperado != null && optimization.getExpectedReturn() != null) {
            BigDecimal ret = optimization.getExpectedReturn().multiply(BigDecimal.valueOf(100));
            lblRetornoEsperado.setText(ret.setScale(2, RoundingMode.HALF_UP) + "% a.a.");
        }
        if (lblVolatilidade != null && optimization.getTotalRisk() != null) {
            BigDecimal risco = optimization.getTotalRisk().multiply(BigDecimal.valueOf(100));
            lblVolatilidade.setText(risco.setScale(2, RoundingMode.HALF_UP) + "% a.a.");
        }
    }

    public void showError(String message) {
        util.MessageUtil.showError(this, message);
    }

    // helper de teste: quantas linhas a tabela de alocacao esta mostrando
    public int getRowCountForTest() { return tableModel.getRowCount(); }

    /**
     * Estrutura visual baseada em Cards e Tabelas.
     */
    private void configurarPainel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(ThemeManager.getBg());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Cabeçalho Resumido
        JPanel pnlHeader = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlHeader.setOpaque(false);

        lblNomeInvestidor = new JLabel("Investidor: Carregando...");
        lblNomeInvestidor.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNomeInvestidor.setForeground(ThemeManager.getText());

        lblPerfilRisco = new JLabel("Perfil: —", SwingConstants.RIGHT);
        lblPerfilRisco.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPerfilRisco.setForeground(ThemeManager.getAccent());

        pnlHeader.add(lblNomeInvestidor);
        pnlHeader.add(lblPerfilRisco);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Painel Central (Cards + Tabela)
        JPanel pnlCentral = new JPanel(new BorderLayout(0, 25));
        pnlCentral.setOpaque(false);

        // Cards de Performance (Retorno e Risco)
        JPanel pnlCards = new JPanel(new GridLayout(1, 2, 25, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 110));

        pnlCards.add(criarCardInfo("Retorno Esperado", "—", new Color(46, 160, 67)));
        pnlCards.add(criarCardInfo("Risco (Volatilidade)", "—", new Color(248, 81, 73)));
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
                Color[] cores = {new Color(31, 111, 235), new Color(46, 160, 67), new Color(248, 81, 73), new Color(255, 166, 0), new Color(163, 113, 247), new Color(0, 191, 165), new Color(255, 87, 34), new Color(120, 144, 156)};
                String[] labels = donutLabels;

                int startAngle = 0;
                for (int i = 0; i < percentuais.length; i++) {
                    int arcAngle = (int) (percentuais[i] * 3.6);
                    g2.setColor(cores[i % cores.length]);
                    g2.fillArc(cx - raio, cy - raio, raio * 2, raio * 2, startAngle, arcAngle);
                    startAngle += arcAngle;
                }

                // Furo central (donut)
                int raioInterno = raio - 22;
                g2.setColor(ThemeManager.getCard());
                g2.fillOval(cx - raioInterno, cy - raioInterno, raioInterno * 2, raioInterno * 2);

                // Texto central
                g2.setColor(ThemeManager.getText());
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                String totalTxt = "R$ 100k";
                int tw = g2.getFontMetrics().stringWidth(totalTxt);
                g2.drawString(totalTxt, cx - tw / 2, cy + 5);

                // Legenda vertical
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

        // Subtítulo e tabela dentro do pnlMeio
        JPanel pnlTabela = new JPanel(new BorderLayout(0, 10));
        pnlTabela.setOpaque(false);

        JLabel lblSubtitulo = new JLabel("ALOCAÇÃO OTIMIZADA DO PORTFÓLIO");
        lblSubtitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSubtitulo.setForeground(ThemeManager.getSubText());
        pnlTabela.add(lblSubtitulo, BorderLayout.NORTH);

        // Tabela de Alocação
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
        add(pnlCentral, BorderLayout.CENTER);

        // 3. Rodapé com botões de navegação
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFooter.setOpaque(false);

        btnVoltar = new JButton("VOLTAR AO MENU");
        estilizarBotaoSecundario(btnVoltar);
        btnVoltar.addActionListener(e -> {
            if (controller != null) controller.backToManagerMain();
        });

        btnNovoInvestidor = new JButton("NOVO INVESTIDOR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        estilizarBotaoPrincipal(btnNovoInvestidor);
        btnNovoInvestidor.addActionListener(e -> {
            if (controller != null) controller.startNewInvestorRegistration();
        });

        pnlFooter.add(btnVoltar);
        pnlFooter.add(btnNovoInvestidor);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel criarCardInfo(String titulo, String valor, Color corValor) {
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

        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.BOLD, 26));
        v.setForeground(corValor);

        // Captura referências para atualização com dados reais
        if (titulo.contains("Retorno")) {
            lblRetornoEsperado = v;
        } else {
            lblVolatilidade = v;
        }

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private void carregarDadosSimulados() {
        lblNomeInvestidor.setText("Investidor: Carlos Roberto Souza");
        lblPerfilRisco.setText("Perfil: AGRESSIVO");
        
        tableModel.addRow(new Object[]{"PETR4", "Petróleo Brasileiro S.A.", "Ação", "40.0%", "R$ 40.000,00"});
        tableModel.addRow(new Object[]{"VALE3", "Vale S.A.", "Ação", "30.0%", "R$ 30.000,00"});
        tableModel.addRow(new Object[]{"IVVB11", "iShares S&P 500 ETF", "ETF", "20.0%", "R$ 20.000,00"});
        tableModel.addRow(new Object[]{"BOVA11", "iShares Ibovespa ETF", "ETF", "10.0%", "R$ 10.000,00"});
    }

    private void estilizarBotaoPrincipal(JButton botao) {
        botao.setPreferredSize(new Dimension(180, 45));
        botao.setBackground(ThemeManager.getAccent());
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setPreferredSize(new Dimension(180, 45));
        botao.setContentAreaFilled(false);
        botao.setForeground(ThemeManager.getText());
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 1));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
