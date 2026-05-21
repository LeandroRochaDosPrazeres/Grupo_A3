package view;

import util.ThemeManager;
import controller.InvestorController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Camada View (Swing) - ManagerInvestorHistoryView.
 * Tela de histórico dos investidores cadastrados pelo gerente logado.
 * 
 * @author leandrorocha
 */
public class ManagerInvestorHistoryView extends javax.swing.JPanel {

    private InvestorController controller;

    // Componentes obrigatórios da especificação
    private JTable tblInvestors;
    private JButton btnVisualizarCarteira;
    private JButton btnAtualizar;
    private JLabel lblTitulo;
    private DefaultTableModel tableModel;

    public ManagerInvestorHistoryView() {
        configurarPainel();
        carregarHistoricoMockado();
    }

    public void setController(InvestorController controller) {
        this.controller = controller;
    }

    /**
     * Retorna o Documento do investidor selecionado na tabela.
     */
    public String getSelectedInvestorDocument() {
        int linhaSelecionada = tblInvestors.getSelectedRow();
        if (linhaSelecionada != -1) {
            return (String) tableModel.getValueAt(linhaSelecionada, 1);
        }
        return null;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Configuração do layout e design do histórico.
     */
    private void configurarPainel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(ThemeManager.getBg());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Cabeçalho do Histórico
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        lblTitulo = new JLabel("HISTÓRICO DE INVESTIDORES CADASTRADOS");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(ThemeManager.getAccent());
        pnlHeader.add(lblTitulo, BorderLayout.WEST);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAtualizar.setForeground(ThemeManager.getText());
        btnAtualizar.setContentAreaFilled(false);
        btnAtualizar.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 1));
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.setPreferredSize(new Dimension(120, 32));
        btnAtualizar.addActionListener(e -> {
            if (controller != null) controller.loadInvestorHistory(null);
        });
        pnlHeader.add(btnAtualizar, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // 2. Tabela de Investidores
        tableModel = new DefaultTableModel(
            new Object[]{"Nome do Investidor", "Documento (CPF)", "Perfil de Risco", "Ativos Selecionados", "Data de Cadastro"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblInvestors = new JTable(tableModel);
        ThemeManager.estilizarTabela(tblInvestors);

        JScrollPane scrollPane = new JScrollPane(tblInvestors);
        ThemeManager.estilizarScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Barra de Ações Inferior
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pnlAcoes.setOpaque(false);

        btnVisualizarCarteira = new JButton("VISUALIZAR CARTEIRA OTIMIZADA") {
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
        btnVisualizarCarteira.setPreferredSize(new Dimension(280, 45));
        btnVisualizarCarteira.setBackground(ThemeManager.getAccent());
        btnVisualizarCarteira.setForeground(Color.WHITE);
        btnVisualizarCarteira.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVisualizarCarteira.setFocusPainted(false);
        btnVisualizarCarteira.setBorderPainted(false);
        btnVisualizarCarteira.setContentAreaFilled(false);
        btnVisualizarCarteira.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnVisualizarCarteira.addActionListener(e -> {
            String doc = getSelectedInvestorDocument();
            if (doc == null) {
                showError("Por favor, selecione um investidor na tabela para visualizar a carteira.");
            } else if (controller != null) {
                controller.openSelectedInvestorDashboard();
            }
        });

        pnlAcoes.add(btnVisualizarCarteira);
        add(pnlAcoes, BorderLayout.SOUTH);
    }

    private void carregarHistoricoMockado() {
        tableModel.addRow(new Object[]{"Ana Silva Mendes", "123.456.789-00", "CONSERVADOR", "TESOURO2029, CDB_PRE, KNRI11", "15/03/2025"});
        tableModel.addRow(new Object[]{"Carlos Roberto Souza", "987.654.321-11", "AGRESSIVO", "PETR4, VALE3, IVVB11", "22/03/2025"});
        tableModel.addRow(new Object[]{"Mariana Oliveira Dias", "455.788.122-33", "MODERADO", "BOVA11, KNRI11, BBDC4", "10/04/2025"});
        tableModel.addRow(new Object[]{"Fernando Costa Lima", "321.654.987-44", "CONSERVADOR", "TESOURO2029, CDB_PRE", "28/04/2025"});
        tableModel.addRow(new Object[]{"Juliana Pereira Santos", "789.123.456-55", "MODERADO", "BOVA11, VALE3, KNRI11, CDB_PRE", "05/05/2025"});
    }
}
