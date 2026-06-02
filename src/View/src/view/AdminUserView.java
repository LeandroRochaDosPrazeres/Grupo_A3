package view;

import util.ThemeManager;
import controller.AdminUserController;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Camada View (Swing) - AdminUserView.
 * Tela principal da jornada do Administrador.
 * Exibe todos os usuários cadastrados no sistema com opções de CRUD.
 * 
 * @author leandrorocha
 */
public class AdminUserView extends javax.swing.JFrame {

    // Componentes obrigatórios exigidos pela especificação técnica
    private JTable tblUsers;
    private JButton btnNovoUsuario;
    private JButton btnExcluirUsuario;
    private JButton btnAtualizar;
    private JButton btnSair;
    private JLabel lblTitulo;
    private JLabel lblUserName;
    private JPanel pnlResumoUsuarios;

    private DefaultTableModel tableModel;
    private JPanel sidebar;
    private JLabel lblLogo;
    private AdminUserController controller;

    public AdminUserView() {
        configurarInterface();
        // dados mockados só aparecem em modo isolado (sem controller)
        // quando o controller chama loadUsers(), a tabela é atualizada com dados reais
    }

    public void setController(AdminUserController controller) {
        this.controller = controller;
    }

    // joga na tabela os usuarios reais vindos do banco
    // (entra no lugar dos dados mockados quando tem Controller ligado)
    public void loadUsersTable(List<User> users) {
        tableModel.setRowCount(0);
        if (users == null) return;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (User u : users) {
            String role = u.getRole() != null ? u.getRole().name() : "—";
            String codigo = (u.getManagerCode() != null && !u.getManagerCode().isBlank())
                    ? u.getManagerCode() : "—";
            String ativo = u.isActive() ? "Sim" : "Não";
            String data = u.getCreatedAt() != null ? u.getCreatedAt().format(fmt) : "—";
            tableModel.addRow(new Object[]{
                    u.getId(), u.getName(), u.getEmail(), role, codigo, ativo, data
            });
        }
    }

    // --- MÉTODOS OBRIGATÓRIOS DA VIEW ---

    public void showView() {
        this.setVisible(true);
    }

    public void setLoggedUserName(String name) {
        lblUserName.setText("Admin: " + name);
    }

    public Long getSelectedUserId() {
        int linhaSelecionada = tblUsers.getSelectedRow();
        if (linhaSelecionada != -1) {
            Object valor = tableModel.getValueAt(linhaSelecionada, 0);
            if (valor instanceof Long) return (Long) valor;
            return Long.parseLong(valor.toString());
        }
        return null;
    }

    public void showSuccess(String message) {
        util.MessageUtil.showSuccess(this, message);
    }

    public void showError(String message) {
        util.MessageUtil.showError(this, message);
    }

    public boolean confirmDelete() {
        return util.MessageUtil.confirm(this, "Tem certeza que deseja excluir este usuário?");
    }

    public void closeView() {
        this.dispose();
    }

    /**
     * Montagem estrutural da interface com Sidebar e área de conteúdo.
     */
    private void configurarInterface() {
        setTitle("Finance Team - Painel do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 850));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.getBg());

        // --- SIDEBAR ---
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(ThemeManager.getCard());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeManager.getBorder()));

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        lblLogo = new JLabel();
        atualizarLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        lblUserName = new JLabel("Admin: Carregando...", SwingConstants.CENTER);
        lblUserName.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblUserName.setForeground(ThemeManager.getSubText());
        lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUserName);

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        btnNovoUsuario = criarBotaoMenu("Novo Usuário");
        btnExcluirUsuario = criarBotaoMenu("Excluir Usuário");
        btnAtualizar = criarBotaoMenu("Atualizar Lista");
        btnSair = criarBotaoMenu("Sair");

        btnNovoUsuario.addActionListener(e -> {
            if (controller != null) controller.openCreateUserForm();
            else abrirFormularioNovoUsuario();
        });
        btnExcluirUsuario.addActionListener(e -> {
            if (controller != null) controller.deleteSelectedUser();
            else excluirUsuarioSelecionado();
        });
        btnAtualizar.addActionListener(e -> {
            if (controller != null) controller.loadUsers();
        });
        btnSair.addActionListener(e -> {
            if (controller != null) controller.logout();
        });

        sidebar.add(btnNovoUsuario);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnExcluirUsuario);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnAtualizar);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSair);

        sidebar.add(Box.createVerticalGlue());

        // Toggle de Tema profissional
        String textoTema = ThemeManager.isDarkMode() ? "Modo Claro" : "Modo Escuro";
        JButton btnTheme = new JButton(textoTema);
        btnTheme.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnTheme.setForeground(ThemeManager.getSubText());
        btnTheme.setContentAreaFilled(false);
        btnTheme.setBorderPainted(true);
        btnTheme.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(6, 12, 6, 12)
        ));
        btnTheme.setFocusPainted(false);
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTheme.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTheme.setMaximumSize(new Dimension(160, 32));

        btnTheme.addActionListener(e -> {
            ThemeManager.toggleTheme();
            configurarInterface();
            if (controller != null) controller.loadUsers();
            else carregarUsuariosMockados();
            SwingUtilities.updateComponentTreeUI(this);
        });

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnTheme);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- ÁREA CENTRAL ---
        JPanel areaPainelDireita = new JPanel(new BorderLayout(0, 20));
        areaPainelDireita.setBackground(ThemeManager.getBg());
        areaPainelDireita.setBorder(new EmptyBorder(50, 60, 50, 60));

        lblTitulo = new JLabel("Gestão de Usuários");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 34));
        lblTitulo.setForeground(ThemeManager.getText());
        areaPainelDireita.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlCentral = new JPanel(new BorderLayout(0, 20));
        pnlCentral.setOpaque(false);
        pnlCentral.setBorder(new EmptyBorder(30, 0, 0, 0));

        // Painel superior: Cards + Gráfico de barras
        JPanel pnlSuperior = new JPanel(new BorderLayout(20, 0));
        pnlSuperior.setOpaque(false);
        pnlSuperior.setPreferredSize(new Dimension(0, 140));

        // Cards de Resumo
        pnlResumoUsuarios = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlResumoUsuarios.setOpaque(false);
        pnlResumoUsuarios.add(criarCardResumo("Administradores", "1", ThemeManager.getAccent()));
        pnlResumoUsuarios.add(criarCardResumo("Gerentes", "2", new Color(46, 160, 67)));
        pnlResumoUsuarios.add(criarCardResumo("Investidores", "2", new Color(248, 81, 73)));
        pnlSuperior.add(pnlResumoUsuarios, BorderLayout.CENTER);

        // Gráfico de barras horizontal
        JPanel pnlGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCard());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

                int padding = 15;
                int barHeight = 20;
                int gap = 14;
                int startY = 38;
                int maxBarWidth = getWidth() - padding * 2 - 90;

                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                g2.setColor(ThemeManager.getSubText());
                g2.drawString("Distribuição", padding, 22);

                int[] valores = {1, 2, 2};
                Color[] cores = {ThemeManager.getAccent(), new Color(46, 160, 67), new Color(248, 81, 73)};
                String[] labels = {"Admin", "Gerente", "Investidor"};
                int total = 5;

                for (int i = 0; i < valores.length; i++) {
                    int y = startY + i * (barHeight + gap);
                    int barWidth = Math.max(20, (int) ((double) valores[i] / total * maxBarWidth));
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    g2.setColor(ThemeManager.getSubText());
                    g2.drawString(labels[i], padding, y + 14);
                    g2.setColor(cores[i]);
                    g2.fillRoundRect(padding + 65, y, barWidth, barHeight, 8, 8);
                    g2.setColor(ThemeManager.getText());
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    g2.drawString(valores[i] + " (" + (valores[i] * 100 / total) + "%)", padding + 70 + barWidth, y + 14);
                }
                g2.dispose();
            }
        };
        pnlGrafico.setOpaque(false);
        pnlGrafico.setPreferredSize(new Dimension(280, 0));
        pnlSuperior.add(pnlGrafico, BorderLayout.EAST);

        pnlCentral.add(pnlSuperior, BorderLayout.NORTH);

        // Tabela de Usuários
        JPanel pnlTabela = new JPanel(new BorderLayout(0, 10));
        pnlTabela.setOpaque(false);

        JLabel lblSubtitulo = new JLabel("USUÁRIOS CADASTRADOS NO SISTEMA");
        lblSubtitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSubtitulo.setForeground(ThemeManager.getSubText());
        pnlTabela.add(lblSubtitulo, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Email", "Perfil", "Cód. Gerente", "Ativo", "Data de Criação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblUsers = new JTable(tableModel);
        ThemeManager.estilizarTabela(tblUsers);
        tblUsers.getColumnModel().getColumn(0).setMaxWidth(50);
        tblUsers.getColumnModel().getColumn(5).setMaxWidth(60);

        JScrollPane scrollPane = new JScrollPane(tblUsers);
        ThemeManager.estilizarScrollPane(scrollPane);
        pnlTabela.add(scrollPane, BorderLayout.CENTER);

        pnlCentral.add(pnlTabela, BorderLayout.CENTER);
        areaPainelDireita.add(pnlCentral, BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(areaPainelDireita, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    private void abrirFormularioNovoUsuario() {
        UserFormDialog dialog = new UserFormDialog(this);
        dialog.setVisible(true);
    }

    private void excluirUsuarioSelecionado() {
        Long id = getSelectedUserId();
        if (id == null) {
            showError("Por favor, selecione um usuário na tabela para excluir.");
            return;
        }
        if (confirmDelete()) {
            int linhaSelecionada = tblUsers.getSelectedRow();
            tableModel.removeRow(linhaSelecionada);
            showSuccess("Usuário excluído com sucesso.");
        }
    }

    private JPanel criarCardResumo(String titulo, String valor, Color corValor) {
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
        v.setFont(new Font("SansSerif", Font.BOLD, 28));
        v.setForeground(corValor);

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(ThemeManager.getText());
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 0, 12, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 45));
        return btn;
    }

    private void atualizarLogo() {
        String path = ThemeManager.isDarkMode()
            ? "/view/resources/logo_sem_background_darkmode.png"
            : "/view/resources/logo_sem_background_lightmode.png";
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            lblLogo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(140, -1, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            lblLogo.setText("FINANCE TEAM");
            lblLogo.setForeground(ThemeManager.getText());
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
    }

    private void carregarUsuariosMockados() {
        tableModel.addRow(new Object[]{1L, "João Admin", "joao.admin@financeteam.com", "ADMIN", "—", "Sim", "15/01/2025"});
        tableModel.addRow(new Object[]{2L, "Maria Gerente", "maria.gerente@financeteam.com", "GERENTE", "MGR-001", "Sim", "10/02/2025"});
        tableModel.addRow(new Object[]{3L, "Pedro Gerente", "pedro.gerente@financeteam.com", "GERENTE", "MGR-002", "Sim", "05/03/2025"});
        tableModel.addRow(new Object[]{4L, "Ana Investidora", "ana.invest@financeteam.com", "INVESTIDOR", "—", "Sim", "20/04/2025"});
        tableModel.addRow(new Object[]{5L, "Carlos Investidor", "carlos.invest@financeteam.com", "INVESTIDOR", "—", "Não", "12/05/2025"});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminUserView janela = new AdminUserView();
            janela.setLoggedUserName("João Admin");
            janela.setVisible(true);
        });
    }
}
