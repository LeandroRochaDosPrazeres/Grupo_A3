package view;

import controller.LoginController;
import util.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Camada View (Swing) - LoginView.
 * Interface minimalista com logótipo dinâmico e suporte a Temas.
 */
public class LoginView extends javax.swing.JFrame {

    private LoginController controller;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnThemeToggle;
    private JLabel lblLogo; 
    private JPanel loginCard;

    public LoginView() {
        configurarInterface();
    }

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    // --- MÉTODOS OBRIGATÓRIOS DA VIEW (CONFORME DOCUMENTAÇÃO) ---
    public void showView() { 
        this.setVisible(true); 
    }
    
    public String getEmail() { 
        return txtEmail.getText(); 
    }
    
    public String getPassword() { 
        return new String(txtPassword.getPassword()); 
    }
    
    public void showError(String m) { 
        JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); 
    }
    
    public void closeView() { 
        this.dispose(); 
    }

    /**
     * Configuração da Interface Gráfica com Logótipo.
     */
    private void configurarInterface() {
        setTitle("Finance Team - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ThemeManager.getBg());
        
        loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(ThemeManager.getCard());
        loginCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 1. Logótipo Dinâmico
        lblLogo = new JLabel("", SwingConstants.CENTER);
        atualizarLogo(); 
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        loginCard.add(lblLogo, gbc);

        // 2. Campo E-mail
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 15, 10);
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEmail.setForeground(ThemeManager.getText());
        loginCard.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtEmail = new JTextField(15);
        estilizarCampo(txtEmail);
        loginCard.add(txtEmail, gbc);

        // 3. Campo Senha
        gbc.gridy = 2; gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSenha.setForeground(ThemeManager.getText());
        loginCard.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtPassword = new JPasswordField(15);
        estilizarCampo(txtPassword);
        loginCard.add(txtPassword, gbc);

        // 4. Botão Login
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        estilizarBotaoPrincipal(btnLogin);
        btnLogin.addActionListener(e -> {
            if (controller != null) controller.handleLogin();
        });
        loginCard.add(btnLogin, gbc);

        // 5. Botão Alternar Modo
        gbc.gridy = 4; gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String textoTema = ThemeManager.isDarkMode() ? "ALTERAR PARA PLANO CLARO" : "ALTERAR PARA PLANO ESCURO";
        btnThemeToggle = new JButton(textoTema);
        estilizarBotaoSecundario(btnThemeToggle);
        btnThemeToggle.addActionListener(e -> {
            ThemeManager.toggleTheme();
            configurarInterface(); 
            SwingUtilities.updateComponentTreeUI(this);
        });
        loginCard.add(btnThemeToggle, gbc);

        mainPanel.add(loginCard);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Carrega o logótipo correto com base no tema ativo.
     */
    private void atualizarLogo() {
        String path = ThemeManager.isDarkMode() 
            ? "/view/resources/logo_sem_background_darkmode.png" 
            : "/view/resources/logo_sem_background_lightmode.png";
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Erro ao carregar logótipo: " + e.getMessage());
            lblLogo.setText("FINANCE TEAM"); 
            lblLogo.setForeground(ThemeManager.getText());
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
        }
    }

    private void estilizarCampo(JTextComponent campo) {
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setBackground(ThemeManager.getBg());
        campo.setForeground(ThemeManager.getText());
        campo.setCaretColor(ThemeManager.getAccent());
        campo.setBorder(BorderFactory.createLineBorder(ThemeManager.getAccent(), 1));
    }

    private void estilizarBotaoPrincipal(JButton botao) {
        botao.setPreferredSize(new Dimension(140, 40)); 
        botao.setBackground(ThemeManager.getAccent());
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setContentAreaFilled(false);
        botao.setForeground(ThemeManager.getSubText());
        botao.setFont(new Font("SansSerif", Font.BOLD, 10));
        botao.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.getSubText()));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        LoginView v = new LoginView();
        v.setController(new LoginController(v));
        v.showView();
    }
}