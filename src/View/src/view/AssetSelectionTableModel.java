package view;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para seleção de ativos com checkbox.
 * Usado no AssetSelectionPanel e InvestorRegistrationOptimizationView.
 * 
 * @author leandrorocha
 */
public class AssetSelectionTableModel extends AbstractTableModel {

    private final String[] colunas = {"Sel.", "Ticker", "Nome do Ativo", "Categoria", "Risco Base"};
    private final List<Object[]> dados = new ArrayList<>();

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dados.get(rowIndex)[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dados.get(rowIndex)[columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // Apenas o checkbox é editável
    }

    /**
     * Adiciona um ativo à tabela.
     * @param ticker Código do ativo (ex: PETR4)
     * @param name Nome completo do ativo
     * @param category Categoria (Ação, ETF, Renda Fixa, FII)
     * @param baseRisk Nível de risco (Alto, Médio, Baixo)
     */
    public void addAsset(String ticker, String name, String category, String baseRisk) {
        dados.add(new Object[]{false, ticker, name, category, baseRisk});
        fireTableRowsInserted(dados.size() - 1, dados.size() - 1);
    }

    /**
     * Retorna a lista de tickers dos ativos selecionados.
     */
    public List<String> getSelectedTickers() {
        List<String> selecionados = new ArrayList<>();
        for (Object[] row : dados) {
            if (Boolean.TRUE.equals(row[0])) {
                selecionados.add((String) row[1]);
            }
        }
        return selecionados;
    }

    /**
     * Seleciona todos os ativos.
     */
    public void selectAll() {
        for (Object[] row : dados) {
            row[0] = true;
        }
        fireTableDataChanged();
    }

    /**
     * Desmarca todos os ativos.
     */
    public void clearSelection() {
        for (Object[] row : dados) {
            row[0] = false;
        }
        fireTableDataChanged();
    }

    /**
     * Limpa todos os dados da tabela.
     */
    public void clear() {
        dados.clear();
        fireTableDataChanged();
    }
}
