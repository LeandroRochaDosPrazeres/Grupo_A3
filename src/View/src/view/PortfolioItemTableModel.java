package view;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para itens do portfólio otimizado.
 * Usado no InvestorDashboardView e InvestorReadOnlyDashboardView.
 * 
 * @author leandrorocha
 */
public class PortfolioItemTableModel extends AbstractTableModel {

    private final String[] colunas = {"Ticker", "Nome do Ativo", "Categoria", "Alocação (%)", "Valor (R$)"};
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Portfólio otimizado é somente leitura
    }

    /**
     * Adiciona um item do portfólio à tabela.
     * @param ticker Código do ativo
     * @param name Nome do ativo
     * @param category Categoria
     * @param allocation Percentual de alocação (ex: "40.0%")
     * @param value Valor em reais (ex: "R$ 40.000,00")
     */
    public void addItem(String ticker, String name, String category, String allocation, String value) {
        dados.add(new Object[]{ticker, name, category, allocation, value});
        fireTableRowsInserted(dados.size() - 1, dados.size() - 1);
    }

    /**
     * Retorna o ticker do item na linha especificada.
     */
    public String getTicker(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dados.size()) {
            return (String) dados.get(rowIndex)[0];
        }
        return null;
    }

    /**
     * Limpa todos os dados da tabela.
     */
    public void clear() {
        dados.clear();
        fireTableDataChanged();
    }

    /**
     * Substitui todos os dados da tabela de uma vez.
     */
    public void setItems(List<Object[]> items) {
        dados.clear();
        dados.addAll(items);
        fireTableDataChanged();
    }
}
