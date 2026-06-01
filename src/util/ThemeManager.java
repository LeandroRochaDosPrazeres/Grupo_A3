package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Gerenciador de Temas (Dark/Light) com utilitários de estilização.
 * Centraliza cores, renderers e helpers visuais para todas as Views.
 * 
 * @author leandrorocha
 */
public class ThemeManager {
    private static boolean darkMode = true;

    // --- DARK MODE (ALTO CONTRASTE) ---
    public static Color darkBg = new Color(13, 17, 23);
    public static Color darkCard = new Color(22, 27, 34);
    public static Color darkText = new Color(240, 246, 252);
    public static Color darkSubText = new Color(139, 148, 158);

    // --- LIGHT MODE (DIVISÕES CLARAS) ---
    public static Color lightBg = new Color(246, 248, 250);
    public static Color lightCard = new Color(255, 255, 255);
    public static Color lightText = new Color(31, 35, 40);
    public static Color lightSubText = new Color(87, 96, 106);

    public static Color accent = new Color(31, 111, 235);

    public static void toggleTheme() { darkMode = !darkMode; }
    public static boolean isDarkMode() { return darkMode; }

    public static Color getBg() { return darkMode ? darkBg : lightBg; }
    public static Color getCard() { return darkMode ? darkCard : lightCard; }
    public static Color getText() { return darkMode ? darkText : lightText; }
    public static Color getSubText() { return darkMode ? darkSubText : lightSubText; }
    public static Color getAccent() { return accent; }

    /** Cor de seleção em tabelas e listas */
    public static Color getSelection() {
        return darkMode ? new Color(33, 66, 131) : new Color(218, 232, 252);
    }

    /** Cor de borda sutil */
    public static Color getBorder() {
        return darkMode ? new Color(48, 54, 61) : new Color(216, 222, 228);
    }

    /** Cor de hover em botões do menu */
    public static Color getHover() {
        return darkMode ? new Color(33, 38, 45) : new Color(234, 238, 242);
    }

    // ========================================================================
    // UTILITÁRIOS DE ESTILIZAÇÃO
    // ========================================================================

    /**
     * Estiliza um JComboBox para ficar coerente com o tema.
     * Resolve o problema de texto invisível e fundo branco/preto.
     */
    public static void estilizarComboBox(JComboBox<?> combo) {
        combo.setBackground(getCard());
        combo.setForeground(getText());
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(getBorder(), 1));
        
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(ThemeManager.getSelection());
                    setForeground(ThemeManager.getText());
                } else {
                    setBackground(ThemeManager.getCard());
                    setForeground(ThemeManager.getText());
                }
                setBorder(new EmptyBorder(6, 10, 6, 10));
                setFont(new Font("SansSerif", Font.PLAIN, 13));
                return this;
            }
        });

        // Estiliza o popup do combo
        Object popup = combo.getUI().getAccessibleChild(combo, 0);
        if (popup instanceof JPopupMenu) {
            JPopupMenu pm = (JPopupMenu) popup;
            pm.setBorder(BorderFactory.createLineBorder(getBorder(), 1));
        }
    }

    /**
     * Estiliza uma JTable completa (header, células, seleção, grid).
     */
    public static void estilizarTabela(JTable tabela) {
        tabela.setRowHeight(36);
        tabela.setBackground(getCard());
        tabela.setForeground(getText());
        tabela.setGridColor(getBorder());
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setSelectionBackground(getSelection());
        tabela.setSelectionForeground(getText());
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));

        // Header estilizado
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(getBg());
        header.setForeground(getSubText());
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, getBorder()));
        header.setReorderingAllowed(false);

        // Renderer padrão para células de texto
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(4, 10, 4, 10));
                if (isSelected) {
                    setBackground(ThemeManager.getSelection());
                    setForeground(ThemeManager.getText());
                } else {
                    setBackground(row % 2 == 0 ? ThemeManager.getCard() : ThemeManager.getBg());
                    setForeground(ThemeManager.getText());
                }
                return this;
            }
        };

        // Aplica o renderer a todas as colunas de texto
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            if (tabela.getColumnClass(i) != Boolean.class) {
                tabela.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }
        }
    }

    /**
     * Estiliza um JScrollPane para ficar coerente com o tema.
     */
    public static void estilizarScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(getBorder(), 1));
        scrollPane.getViewport().setBackground(getCard());
        
        // Scrollbar customizada
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = getSubText();
                this.trackColor = getBg();
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return criarBotaoInvisivel();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return criarBotaoInvisivel();
            }
        });
    }

    private static JButton criarBotaoInvisivel() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }
}
