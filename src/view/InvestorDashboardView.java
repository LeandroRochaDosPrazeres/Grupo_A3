package view;

import util.ThemeManager;
import controller.InvestorController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
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
    private DefaultTableModel tableModel;

    public InvestorDashboardView() {
        configurarPainel();
        carregarDadosSimulados(); // Dados mockados para validação da V1
    }

    public void setController(InvestorController controller) {
        this.controller = controller;
    }

    /**
     * Estrutura visual baseada em Cards e Tabelas de alta performance.
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

        lblPerfilRisco = new JLabel("Perfil: CARREGANDO...", SwingConstants.RIGHT);
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

        pnlCards.add(criarCardInfo("Retorno Esperado da Carteira", "12.85% a.a.", new Color(46, 160, 67)));
        pnlCards.add(criarCardInfo("Risco Estimado (Volatilidade)", "7.12% a.a.", new Color(248, 81, 73)));
        pnlCentral.add(pnlCards, BorderLayout.NORTH);

        // Tabela de Alocação (Portfólio Otimizado)
        tableModel = new DefaultTableModel(
            new Object[]{"Ticker", "Nome do Ativo", "Categoria", "Alocação (%)", "Valor (R$)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblPortfolio = new JTable(tableModel);
        tblPortfolio.setRowHeight(35);
        tblPortfolio.setBackground(ThemeManager.getCard());
        tblPortfolio.setForeground(ThemeManager.getText());
        tblPortfolio.setGridColor(ThemeManager.getBg());
        
        JScrollPane scrollPane = new JScrollPane(tblPortfolio);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeManager.getCard()));
        scrollPane.getViewport().setBackground(ThemeManager.getBg());
        pnlCentral.add(scrollPane, BorderLayout.CENTER);

        add(pnlCentral, BorderLayout.CENTER);

        // 3. Rodapé
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlFooter.setOpaque(false);

        btnVoltar = new JButton("VOLTAR AO MENU PRINCIPAL");
        btnVoltar.setPreferredSize(new Dimension(220, 45));
        btnVoltar.setContentAreaFilled(false);
        btnVoltar.setForeground(ThemeManager.getText());
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createLineBorder(ThemeManager.getSubText(), 1));
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            if (controller != null) controller.backToManagerMain();
        });

        pnlFooter.add(btnVoltar);
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

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private void carregarDadosSimulados() {
        lblNomeInvestidor.setText("Investidor: Carlos Roberto Souza");
        lblPerfilRisco.setText("Perfil: AGRESSIVO");
        
        tableModel.addRow(new Object[]{"PETR4", "Petrobras", "Ação", "40.0%", "R$ 40.000,00"});
        tableModel.addRow(new Object[]{"VALE3", "Vale S.A.", "Ação", "30.0%", "R$ 30.000,00"});
        tableModel.addRow(new Object[]{"IVVB11", "S&P 500 ETF", "ETF", "20.0%", "R$ 20.000,00"});
        tableModel.addRow(new Object[]{"BOVA11", "iShares Ibovespa", "ETF", "10.0%", "R$ 10.000,00"});
    }
}