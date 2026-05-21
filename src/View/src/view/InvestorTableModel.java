package view;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para a tabela de investidores (ManagerInvestorHistoryView).
 * Organiza dados de Investor para JTable sem manipulação manual de linhas.
 * 
 * @author leandrorocha
 */
public class InvestorTableModel extends AbstractTableModel {

    private final String[] colunas = {"Nome do Investidor", "Documento (CPF)", "Perfil de Risco", "Ativos Selecionados", "Data de Cadastro"};
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
        return false;
    }

    /**
     * Adiciona um investidor à tabela.
     * @param name Nome do investidor
     * @param documentId CPF/Documento
     * @param riskProfile Perfil de risco (CONSERVADOR, MODERADO, AGRESSIVO)
     * @param assets Ativos selecionados (string separada por vírgula)
     * @param createdAt Data de cadastro formatada
     */
    public void addInvestor(String name, String documentId, String riskProfile, String assets, String createdAt) {
        dados.add(new Object[]{name, documentId, riskProfile, assets, createdAt});
        fireTableRowsInserted(dados.size() - 1, dados.size() - 1);
    }

    /**
     * Retorna o documento do investidor na linha especificada.
     */
    public String getInvestorDocument(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dados.size()) {
            return (String) dados.get(rowIndex)[1];
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
    public void setInvestors(List<Object[]> investors) {
        dados.clear();
        dados.addAll(investors);
        fireTableDataChanged();
    }
}
