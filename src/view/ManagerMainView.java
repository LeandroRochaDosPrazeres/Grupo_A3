package view;

import util.ThemeManager;
import controller.ManagerController; // Importa o controller renomeado conforme a V1
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Camada View (Swing) - ManagerMainView.
 * Tela inicial da jornada do gerente operacional. Funciona como um contêiner 
 * dinâmico para exibição de fluxos através do painel central.
 */
public class ManagerMainView extends javax.swing.JFrame {

    private ManagerController controller; // Vinculado ao Controller oficial
    
    // Componentes obrigatórios exigidos pela especificação técnica
    private JLabel lblTitulo;
    private JLabel lblUserName;
    private JButton btnNovoInvestidor;
    private JButton btnHistoricoInvestidores;
    private JButton btnSair;
    private JPanel pnlContent; // Painel central dinâmico
    
    private JPanel sidebar;
    private JLabel lblLogo;

    public ManagerMainView() {
        configurarInterface();
    }

    public void setController(ManagerController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS OBRIGATÓRIOS DA CONFIGURAÇÃO DA VIEW (CONFORME DOCUMENTAÇÃO) ---
    
    public void showView() { 
        this.setVisible(true); 
    }

    public void setLoggedUserName(String name) { 
        lblUserName.setText("Gerente: " + name); 
    }

    public void showPanel(JPanel panel) { 
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    public void showError(String message) { 
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE); 
    }

    /**
     * Montagem estrutural da interface com Sidebar e área dinâmica.
     */
    private void configurarInterface() {
        setTitle("Finance Team - Painel do Gerente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 850));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.getBg());

        // --- SIDEBAR (SISTEMA DE NAVEGAÇÃO CORPORATIVO) ---
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(ThemeManager.getCard());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, 
                ThemeManager.isDarkMode() ? new Color(48, 54, 61) : new Color(216, 222, 228)));

        // 1. Logo Centralizada
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        lblLogo = new JLabel();
        atualizarLogo();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Identificação do Usuário Logado na Sidebar
        lblUserName = new JLabel("Gerente: Carregando...", SwingConstants.CENTER);
        lblUserName.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblUserName.setForeground(ThemeManager.getSubText());
        lblUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUserName);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // 2. Criação Física dos Botões Contratuais do Menu
        btnNovoInvestidor = criarBotaoMenu("Novo Investidor");
        btnHistoricoInvestidores = criarBotaoMenu("Histórico");
        btnSair = criarBotaoMenu("Sair");

        // Ouvintes de eventos direcionando para o ManagerController
        btnNovoInvestidor.addActionListener(e -> {
            if (controller != null) controller.openNewInvestorFlow();
        });
        
        btnHistoricoInvestidores.addActionListener(e -> {
            if (controller != null) controller.openInvestorHistory();
        });
        
        btnSair.addActionListener(e -> {
            if (controller != null) controller.logout();
        });

        // Adiciona os botões à Sidebar
        sidebar.add(btnNovoInvestidor);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnHistoricoInvestidores);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSair);

        // Empurra o alternador monocromático para o rodapé
        sidebar.add(Box.createVerticalGlue());
        
        // 3. Alternador de Tema Ultra-Minimalista
        String iconSymbol = ThemeManager.isDarkMode() ? "☾" : "☼"; 
        JButton btnTheme = new JButton(iconSymbol);
        btnTheme.setFont(new Font("SansSerif", Font.PLAIN, 22));
        btnTheme.setForeground(ThemeManager.getSubText());
        btnTheme.setContentAreaFilled(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setFocusPainted(false);
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        themePanel.setOpaque(false);
        themePanel.setMaximumSize(new Dimension(260, 50));
        themePanel.add(btnTheme);
        
        btnTheme.addActionListener(e -> {
            ThemeManager.toggleTheme();
            configurarInterface();
            SwingUtilities.updateComponentTreeUI(this);
        });
        sidebar.add(themePanel);

        // --- ÁREA CENTRAL DE CONTEÚDO ---
        JPanel areaPainelDireita = new JPanel(new BorderLayout());
        areaPainelDireita.setBackground(ThemeManager.getBg());
        areaPainelDireita.setBorder(new EmptyBorder(50, 60, 50, 60));

        // Título Fixo do Cabeçalho
        lblTitulo = new JLabel("Painel Operacional");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 34));
        lblTitulo.setForeground(ThemeManager.getText());
        areaPainelDireita.add(lblTitulo, BorderLayout.NORTH);

        // Instanciação do Painel Dinâmico Vazio que receberá as sub-views
        pnlContent = new JPanel(new BorderLayout());
        pnlContent.setOpaque(false);
        pnlContent.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        // Painel temporário de boas-vindas
        JPanel pnlBoasVindas = new JPanel(new GridBagLayout());
        pnlBoasVindas.setOpaque(false);
        JLabel lblMsg = new JLabel("Selecione uma operação no menu lateral para iniciar.");
        lblMsg.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblMsg.setForeground(ThemeManager.getSubText());
        pnlBoasVindas.add(lblMsg);
        pnlContent.add(pnlBoasVindas, BorderLayout.CENTER);

        areaPainelDireita.add(pnlContent, BorderLayout.CENTER);

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(areaPainelDireita, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerMainView janela = new ManagerMainView();
            ManagerController controller = new ManagerController(janela);
            janela.setController(controller); // Vincula o cérebro à tela
            janela.setVisible(true);
        });
    }
}