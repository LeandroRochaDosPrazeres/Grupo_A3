package view;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para a tabela de usuários (AdminUserView).
 * Organiza dados de User para JTable sem manipulação manual de linhas.
 * 
 * @author leandrorocha
 */
public class UserTableModel extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome", "Email", "Perfil", "Cód. Gerente", "Ativo", "Data de Criação"};
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
     * Adiciona um usuário à tabela.
     * @param id ID do usuário
     * @param name Nome
     * @param email Email
     * @param role Perfil (ADMIN, GERENTE, INVESTIDOR)
     * @param managerCode Código do gerente (ou "—")
     * @param active "Sim" ou "Não"
     * @param createdAt Data de criação formatada
     */
    public void addUser(Long id, String name, String email, String role, String managerCode, String active, String createdAt) {
        dados.add(new Object[]{id, name, email, role, managerCode, active, createdAt});
        fireTableRowsInserted(dados.size() - 1, dados.size() - 1);
    }

    /**
     * Remove um usuário pelo índice da linha.
     */
    public void removeUser(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dados.size()) {
            dados.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    /**
     * Retorna o ID do usuário na linha especificada.
     */
    public Long getUserId(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dados.size()) {
            Object val = dados.get(rowIndex)[0];
            if (val instanceof Long) return (Long) val;
            return Long.parseLong(val.toString());
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
    public void setUsers(List<Object[]> users) {
        dados.clear();
        dados.addAll(users);
        fireTableDataChanged();
    }
}
