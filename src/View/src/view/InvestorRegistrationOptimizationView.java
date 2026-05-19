package view;

import util.ThemeManager;
import controller.InvestorController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leandrorocha
 */

public class InvestorRegistrationOptimizationView extends javax.swing.JPanel {

    private InvestorController controller;

    // Componentes obrigatórios exigidos pela especificação técnica
    private JTextField txtInvestorName;
    private JTextField txtDocumentId;
    private JComboBox<String> cmbRiskProfile; // Exibe em PT-BR, converte internamente
    private JTable tblAssets;
    private JButton btnCadastrarEOtimizar;
    private JButton btnLimpar;
    private JButton btnVoltar;
    private JLabel lblTitulo;
    
    private DefaultTableModel tableModel;

    public InvestorRegistrationOptimizationView() {
        configurarPainel();
        carregarAtivosMockados();
    }

    public void setController(InvestorController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS CONTRATUAIS DA VIEW (AJUSTADO PARA TRADUÇÃO SUPABASE) ---
    
    public String getInvestorName() { 
        return txtInvestorName.getText(); 
    }

    public String getDocumentId() { 
        return txtDocumentId.getText(); 
    }

    /**
     * Captura a seleção em português e mapeia para a String esperada pelo banco.
     */
    public String getSelectedRiskProfile() { 
        String perfilSelecionado = (String) cmbRiskProfile.getSelectedItem();
        
        if ("CONSERVADOR".equals(perfilSelecionado)) {
            return "CONSERVATIVE";
        } else if ("MODERADO".equals(perfilSelecionado)) {
            return "MODERATE";
        } else if ("AGRESSIVO".equals(perfilSelecionado)) {
            return "AGGRESSIVE";
        }
        return perfilSelecionado;
    }

    /**
     * Varre a tabela e retorna a lista de Tickers dos ativos selecionados.
     */
    public List<String> getSelectedAssets() {
        List<String> ativosSelecionados = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selecionado = (Boolean) tableModel.getValueAt(i, 0);
            if (selecionado != null && selecionado) {
                String ticker = (String) tableModel.getValueAt(i, 1);
                ativosSelecionados.add(ticker);
            }
        }
        return ativosSelecionados;
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
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Estruturação visual utilizando GridBagLayout para manter o padrão minimalista.
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

        // Rótulo e Campo: Nome
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setForeground(ThemeManager.getText());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 1;
        pnlInvestorData.add(lblNome, gbc);

        txtInvestorName = new JTextField(20);
        estilizarCampo(txtInvestorName);
        gbc.gridx = 1;
        pnlInvestorData.add(txtInvestorName, gbc);

        // Rótulo e Campo: Documento
        JLabel lblDoc = new JLabel("Documento (ID):");
        lblDoc.setForeground(ThemeManager.getText());
        lblDoc.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 2;
        pnlInvestorData.add(lblDoc, gbc);

        txtDocumentId = new JTextField(15);
        estilizarCampo(txtDocumentId);
        gbc.gridx = 3;
        pnlInvestorData.add(txtDocumentId, gbc);

        // Rótulo e Campo: Perfil de Risco
        JLabel lblPerfil = new JLabel("Perfil de Risco:");
        lblPerfil.setForeground(ThemeManager.getText());
        lblPerfil.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2;
        pnlInvestorData.add(lblPerfil, gbc);

        // --- ALTERAÇÃO SOLICITADA: Opções alteradas de inglês para português do Brasil ---
        String[] perfis = {"CONSERVADOR", "MODERADO", "AGRESSIVO"};
        cmbRiskProfile = new JComboBox<>(perfis);
        cmbRiskProfile.setPreferredSize(new Dimension(200, 35));
        cmbRiskProfile.setBackground(ThemeManager.getBg());
        cmbRiskProfile.setForeground(ThemeManager.getText());
        gbc.gridx = 1;
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
            new Object[]{"Selecionar", "Ticker", "Nome do Ativo", "Categoria", "Risco Base"}, 0
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
        tblAssets.setRowHeight(30);
        tblAssets.setBackground(ThemeManager.getCard());
        tblAssets.setForeground(ThemeManager.getText());
        tblAssets.setGridColor(ThemeManager.getBg());
        
        JScrollPane scrollPane = new JScrollPane(tblAssets);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeManager.getCard()));
        scrollPane.getViewport().setBackground(ThemeManager.getBg());
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
        tableModel.addRow(new Object[]{false, "PETR4", "Petróleo Brasileiro S.A.", "Ação", "Alto"});
        tableModel.addRow(new Object[]{false, "VALE3", "Vale S.A.", "Ação", "Alto"});
        tableModel.addRow(new Object[]{false, "BBDC4", "Banco Bradesco S.A.", "Ação", "Médio"});
        tableModel.addRow(new Object[]{false, "BOVA11", "iShares Ibovespa Index ETF", "ETF", "Médio"});
        tableModel.addRow(new Object[]{false, "TESOURO2029", "Tesouro IPCA+", "Renda Fixa", "Baixo"});
        tableModel.addRow(new Object[]{false, "CDB_PRE", "CDB Prefixado Itaú", "Renda Fixa", "Baixo"});
        tableModel.addRow(new Object[]{false, "KNRI11", "Kinea Renda Imobiliária FII", "FII", "Baixo"});
        tableModel.addRow(new Object[]{false, "IVVB11", "S&P 500 Index ETF", "ETF", "Alto"});
    }

    private void abrirBordaCampo(JTextField campo) {
        campo.setBorder(BorderFactory.createLineBorder(ThemeManager.isDarkMode() ? new Color(56, 139, 253) : new Color(216, 222, 228), 1));
    }

    private void estilizarCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(200, 35));
        campo.setBackground(ThemeManager.getBg());
        campo.setForeground(ThemeManager.getText());
        campo.setCaretColor(ThemeManager.getAccent());
        abrirBordaCampo(campo);
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
        botao.setBorder(BorderFactory.createLineBorder(ThemeManager.getSubText(), 1));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}