package view;

import util.ThemeManager;
import controller.InvestorController;
import model.Asset;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada View (Swing) - InvestorRegistrationOptimizationView.
 * Tela de cadastro de investidor com seleção de ativos e disparo da otimização.
 * 
 * @author leandrorocha
 */
public class InvestorRegistrationOptimizationView extends javax.swing.JPanel {

    private InvestorController controller;

    // Componentes obrigatórios exigidos pela especificação técnica
    private JTextField txtInvestorName;
    private JTextField txtDocumentId;
    private JComboBox<String> cmbRiskProfile;
    private JTable tblAssets;
    private JButton btnCadastrarEOtimizar;
    private JButton btnLimpar;
    private JButton btnVoltar;
    private JLabel lblTitulo;
    
    private DefaultTableModel tableModel;
    private final List<Asset> assetsNaTabela = new ArrayList<>();

    public InvestorRegistrationOptimizationView() {
        configurarPainel();
        // ativos mockados só para preview visual isolado (sem controller)
        // quando o controller chama loadAssetsTable(), os dados reais são carregados
    }

    public void setController(InvestorController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS CONTRATUAIS DA VIEW ---
    
    public String getInvestorName() { 
        return txtInvestorName.getText(); 
    }

    public String getDocumentId() { 
        return txtDocumentId.getText(); 
    }

    public String getSelectedRiskProfile() { 
        String perfilSelecionado = (String) cmbRiskProfile.getSelectedItem();
        if ("CONSERVADOR".equals(perfilSelecionado)) return "CONSERVATIVE";
        if ("MODERADO".equals(perfilSelecionado)) return "MODERATE";
        if ("AGRESSIVO".equals(perfilSelecionado)) return "AGGRESSIVE";
        return perfilSelecionado;
    }

    public List<Asset> getSelectedAssets() {
        List<Asset> selecionados = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean marcado = (Boolean) tableModel.getValueAt(i, 0);
            if (marcado != null && marcado && i < assetsNaTabela.size()) {
                selecionados.add(assetsNaTabela.get(i));
            }
        }
        return selecionados;
    }

    // joga na tabela os ativos reais vindos do banco
    // (entra no lugar dos ativos mockados quando tem Controller ligado)
    public void loadAssetsTable(List<Asset> assets) {
        tableModel.setRowCount(0);
        assetsNaTabela.clear();
        if (assets == null) return;
        for (Asset asset : assets) {
            assetsNaTabela.add(asset);
            String risco = asset.getBaseRisk() != null ? asset.getBaseRisk().toString() : "—";
            tableModel.addRow(new Object[]{false, asset.getTicker(), asset.getName(), asset.getCategory(), risco});
        }
    }

    public void clearForm() {
        txtInvestorName.setText("");
        txtDocumentId.setText("");
        cmbRiskProfile.setSelectedIndex(0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0);
        }
    }

    public void showSuccess(String message) {
        util.MessageUtil.showSuccess(this, message);
    }

    public void showError(String message) {
        util.MessageUtil.showError(this, message);
    }

    // ----- helpers usados pelos testes automatizados (simulam o preenchimento) -----
    public void setInvestorNameForTest(String nome) { txtInvestorName.setText(nome); }
    public void setDocumentIdForTest(String doc) { txtDocumentId.setText(doc); }
    public void setRiskProfileForTest(String perfilPt) { cmbRiskProfile.setSelectedItem(perfilPt); }
    // marca os primeiros N ativos da tabela, como se o usuario clicasse nos checkboxes
    public void selecionarAtivosParaTeste(int quantos) {
        for (int i = 0; i < quantos && i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 0);
        }
    }

    /**
     * Estruturação visual do painel de cadastro.
     */
    private void configurarPainel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(ThemeManager.getBg());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Painel Superior: Card contendo o formulário do Investidor
        JPanel pnlInvestorData = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        pnlInvestorData.setOpaque(false);
        pnlInvestorData.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título Interno da Seção
        lblTitulo = new JLabel("NOVO CADASTRO DE INVESTIDOR");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(ThemeManager.getAccent());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 8, 15, 8);
        pnlInvestorData.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1; gbc.insets = new Insets(8, 8, 8, 8);

        // Campo: Nome
        gbc.gridx = 0; gbc.gridy = 1;
        pnlInvestorData.add(criarLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        txtInvestorName = new JTextField(20);
        estilizarCampo(txtInvestorName);
        pnlInvestorData.add(txtInvestorName, gbc);

        // Campo: Documento
        gbc.gridx = 2;
        pnlInvestorData.add(criarLabel("Documento (CPF):"), gbc);
        gbc.gridx = 3;
        txtDocumentId = new JTextField(15);
        estilizarCampo(txtDocumentId);
        pnlInvestorData.add(txtDocumentId, gbc);

        // Campo: Perfil de Risco
        gbc.gridx = 0; gbc.gridy = 2;
        pnlInvestorData.add(criarLabel("Perfil de Risco:"), gbc);
        gbc.gridx = 1;
        String[] perfis = {"CONSERVADOR", "MODERADO", "AGRESSIVO"};
        cmbRiskProfile = new JComboBox<>(perfis);
        cmbRiskProfile.setPreferredSize(new Dimension(200, 35));
        ThemeManager.estilizarComboBox(cmbRiskProfile);
        pnlInvestorData.add(cmbRiskProfile, gbc);

        add(pnlInvestorData, BorderLayout.NORTH);

        // 2. Painel Central: Listagem de Ativos Selecionáveis
        JPanel pnlAssetSelection = new JPanel(new BorderLayout(0, 10));
        pnlAssetSelection.setOpaque(false);

        JLabel lblSub = new JLabel("SELECIONE OS ATIVOS PARA COMPOR O PORTFÓLIO");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSub.setForeground(ThemeManager.getSubText());
        pnlAssetSelection.add(lblSub, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new Object[]{"✓", "Ticker", "Nome do Ativo", "Categoria", "Risco Base"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        tblAssets = new JTable(tableModel);
        ThemeManager.estilizarTabela(tblAssets);
        tblAssets.getColumnModel().getColumn(0).setMaxWidth(50);
        tblAssets.getColumnModel().getColumn(0).setMinWidth(50);
        
        JScrollPane scrollPane = new JScrollPane(tblAssets);
        ThemeManager.estilizarScrollPane(scrollPane);
        pnlAssetSelection.add(scrollPane, BorderLayout.CENTER);

        add(pnlAssetSelection, BorderLayout.CENTER);

        // 3. Painel Inferior: Barra de Controle Operacional
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlAcoes.setOpaque(false);

        btnVoltar = new JButton("VOLTAR");
        estilizarBotaoSecundario(btnVoltar);
        btnVoltar.addActionListener(e -> {
            if (controller != null) controller.backToManagerMain();
        });

        btnLimpar = new JButton("LIMPAR FORMULÁRIO");
        estilizarBotaoSecundario(btnLimpar);
        btnLimpar.addActionListener(e -> clearForm());

        btnCadastrarEOtimizar = new JButton("CADASTRAR E OTIMIZAR") {
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
        estilizarBotaoPrincipal(btnCadastrarEOtimizar);
        btnCadastrarEOtimizar.addActionListener(e -> {
            if (controller != null) controller.registerAndOptimize();
        });

        pnlAcoes.add(btnVoltar);
        pnlAcoes.add(btnLimpar);
        pnlAcoes.add(btnCadastrarEOtimizar);

        add(pnlAcoes, BorderLayout.SOUTH);
    }

    private void carregarAtivosMockados() {
        adicionarAtivoMock("PETR4", "Petróleo Brasileiro S.A.", "Ação", "Alto");
        adicionarAtivoMock("VALE3", "Vale S.A.", "Ação", "Alto");
        adicionarAtivoMock("BBDC4", "Banco Bradesco S.A.", "Ação", "Médio");
        adicionarAtivoMock("BOVA11", "iShares Ibovespa Index ETF", "ETF", "Médio");
        adicionarAtivoMock("TESOURO2029", "Tesouro IPCA+ 2029", "Renda Fixa", "Baixo");
        adicionarAtivoMock("CDB_PRE", "CDB Prefixado Itaú 12%", "Renda Fixa", "Baixo");
        adicionarAtivoMock("KNRI11", "Kinea Renda Imobiliária FII", "FII", "Baixo");
        adicionarAtivoMock("IVVB11", "iShares S&P 500 ETF", "ETF", "Alto");
    }

    private void adicionarAtivoMock(String ticker, String nome, String categoria, String risco) {
        Asset asset = new Asset();
        asset.setTicker(ticker);
        asset.setName(nome);
        asset.setCategory(categoria);
        assetsNaTabela.add(asset);
        tableModel.addRow(new Object[]{false, ticker, nome, categoria, risco});
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(ThemeManager.getText());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        return lbl;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(200, 35));
        campo.setBackground(ThemeManager.getBg());
        campo.setForeground(ThemeManager.getText());
        campo.setCaretColor(ThemeManager.getAccent());
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void estilizarBotaoPrincipal(JButton botao) {
        botao.setPreferredSize(new Dimension(220, 45));
        botao.setBackground(ThemeManager.getAccent());
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setPreferredSize(new Dimension(180, 45));
        botao.setContentAreaFilled(false);
        botao.setForeground(ThemeManager.getText());
        botao.setFont(new Font("SansSerif", Font.BOLD, 11));
        botao.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 1));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
