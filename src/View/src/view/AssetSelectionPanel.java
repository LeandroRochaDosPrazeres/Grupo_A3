package view;

import util.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada View (Swing) - AssetSelectionPanel.
 * Painel reutilizável para seleção de ativos financeiros.
 * Pode ser embutido dentro de qualquer tela (JPanel, não JFrame).
 * 
 * @author leandrorocha
 */
public class AssetSelectionPanel extends javax.swing.JPanel {

    // Componentes obrigatórios da especificação
    private JTable tblAssets;
    private JButton btnSelecionarTodos;
    private JButton btnLimparSelecao;
    private JLabel lblAtivosDisponiveis;
    private DefaultTableModel tableModel;

    public AssetSelectionPanel() {
        configurarPainel();
    }

    // --- MÉTODOS CONTRATUAIS ---

    /**
     * Carrega ativos na tabela a partir de dados externos.
     * Cada item é um array: {ticker, nome, categoria, riscoBase}
     */
    public void loadAssets(List<String[]> assets) {
        tableModel.setRowCount(0);
        for (String[] asset : assets) {
            tableModel.addRow(new Object[]{false, asset[0], asset[1], asset[2], asset[3]});
        }
    }

    /**
     * Retorna a lista de tickers dos ativos selecionados (checkbox marcado).
     */
    public List<String> getSelectedAssets() {
        List<String> selecionados = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean selecionado = (Boolean) tableModel.getValueAt(i, 0);
            if (selecionado != null && selecionado) {
                String ticker = (String) tableModel.getValueAt(i, 1);
                selecionados.add(ticker);
            }
        }
        return selecionados;
    }

    /**
     * Limpa todas as seleções (desmarca todos os checkboxes).
     */
    public void clearSelection() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0);
        }
    }

    /**
     * Seleciona todos os ativos da tabela.
     */
    public void selectAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(true, i, 0);
        }
    }

    /**
     * Configuração visual do painel.
     */
    private void configurarPainel() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);

        // Cabeçalho com label e botões de ação rápida
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        lblAtivosDisponiveis = new JLabel("ATIVOS DISPONÍVEIS PARA SELEÇÃO");
        lblAtivosDisponiveis.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAtivosDisponiveis.setForeground(ThemeManager.getSubText());
        pnlHeader.add(lblAtivosDisponiveis, BorderLayout.WEST);

        JPanel pnlBotoesRapidos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotoesRapidos.setOpaque(false);

        btnSelecionarTodos = new JButton("Selecionar Todos");
        estilizarBotaoCompacto(btnSelecionarTodos);
        btnSelecionarTodos.addActionListener(e -> selectAll());

        btnLimparSelecao = new JButton("Limpar Seleção");
        estilizarBotaoCompacto(btnLimparSelecao);
        btnLimparSelecao.addActionListener(e -> clearSelection());

        pnlBotoesRapidos.add(btnSelecionarTodos);
        pnlBotoesRapidos.add(btnLimparSelecao);
        pnlHeader.add(pnlBotoesRapidos, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // Tabela de ativos com checkbox
        tableModel = new DefaultTableModel(
            new Object[]{"Sel.", "Ticker", "Nome do Ativo", "Categoria", "Risco Base"}, 0
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
        add(scrollPane, BorderLayout.CENTER);
    }

    private void estilizarBotaoCompacto(JButton botao) {
        botao.setFont(new Font("SansSerif", Font.PLAIN, 11));
        botao.setForeground(ThemeManager.getText());
        botao.setContentAreaFilled(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(4, 10, 4, 10)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setFocusPainted(false);
    }
}
