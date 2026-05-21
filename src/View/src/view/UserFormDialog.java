package view;

import util.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Camada View (Swing) - UserFormDialog.
 * Janela modal para cadastro/edição de usuários do sistema.
 * 
 * @author leandrorocha
 */
public class UserFormDialog extends javax.swing.JDialog {

    // Componentes obrigatórios
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JTextField txtManagerCode;
    private JCheckBox chkActive;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private JLabel lblManagerCode;

    public UserFormDialog(JFrame parent) {
        super(parent, "Cadastrar Novo Usuário", true);
        configurarInterface();
    }

    // --- MÉTODOS CONTRATUAIS ---

    public String getName() { return txtName.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }

    public String getSelectedRole() {
        String selecionado = (String) cmbRole.getSelectedItem();
        if ("ADMINISTRADOR".equals(selecionado)) return "ADMIN";
        if ("GERENTE".equals(selecionado)) return "MANAGER";
        if ("INVESTIDOR".equals(selecionado)) return "INVESTOR";
        return selecionado;
    }

    public String getManagerCode() { return txtManagerCode.getText(); }
    public boolean isActive() { return chkActive.isSelected(); }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void closeDialog() { this.dispose(); }

    /**
     * Configuração da interface do formulário modal.
     */
    private void configurarInterface() {
        setSize(520, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(ThemeManager.getCard());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;

        // Título
        JLabel lblTitulo = new JLabel("NOVO USUÁRIO");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(ThemeManager.getAccent());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 20, 8);
        mainPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(criarLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        estilizarCampo(txtName);
        mainPanel.add(txtName, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(criarLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        estilizarCampo(txtEmail);
        mainPanel.add(txtEmail, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(criarLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        estilizarCampoSenha(txtPassword);
        mainPanel.add(txtPassword, gbc);

        // Perfil (Role)
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(criarLabel("Perfil:"), gbc);
        gbc.gridx = 1;
        String[] perfis = {"ADMINISTRADOR", "GERENTE", "INVESTIDOR"};
        cmbRole = new JComboBox<>(perfis);
        cmbRole.setPreferredSize(new Dimension(250, 35));
        ThemeManager.estilizarComboBox(cmbRole);
        cmbRole.addActionListener(e -> atualizarVisibilidadeManagerCode());
        mainPanel.add(cmbRole, gbc);

        // Código do Gerente
        gbc.gridx = 0; gbc.gridy = 5;
        lblManagerCode = criarLabel("Código Gerente:");
        mainPanel.add(lblManagerCode, gbc);
        gbc.gridx = 1;
        txtManagerCode = new JTextField(20);
        estilizarCampo(txtManagerCode);
        mainPanel.add(txtManagerCode, gbc);

        // Ativo
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(criarLabel("Status:"), gbc);
        gbc.gridx = 1;
        chkActive = new JCheckBox("Usuário ativo");
        chkActive.setSelected(true);
        chkActive.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chkActive.setForeground(ThemeManager.getText());
        chkActive.setBackground(ThemeManager.getCard());
        chkActive.setFocusPainted(false);
        mainPanel.add(chkActive, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 8, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlBotoes.setOpaque(false);

        btnCancelar = new JButton("CANCELAR");
        estilizarBotaoSecundario(btnCancelar);
        btnCancelar.addActionListener(e -> dispose());

        btnSalvar = new JButton("SALVAR") {
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
        estilizarBotaoPrincipal(btnSalvar);
        btnSalvar.addActionListener(e -> salvarUsuario());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnSalvar);
        mainPanel.add(pnlBotoes, gbc);

        setContentPane(mainPanel);
        atualizarVisibilidadeManagerCode();
    }

    private void atualizarVisibilidadeManagerCode() {
        boolean isGerente = "GERENTE".equals(cmbRole.getSelectedItem());
        txtManagerCode.setVisible(isGerente);
        lblManagerCode.setVisible(isGerente);
    }

    private void salvarUsuario() {
        if (getName().trim().isEmpty() || getEmail().trim().isEmpty() || getPassword().trim().isEmpty()) {
            showError("Por favor, preencha todos os campos obrigatórios (Nome, E-mail e Senha).");
            return;
        }
        System.out.println("UserFormDialog: Salvando usuário - " + getName() + " (" + getSelectedRole() + ")");
        JOptionPane.showMessageDialog(this, "Usuário \"" + getName() + "\" cadastrado com sucesso!",
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        closeDialog();
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(ThemeManager.getText());
        return lbl;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setBackground(ThemeManager.getBg());
        campo.setForeground(ThemeManager.getText());
        campo.setCaretColor(ThemeManager.getAccent());
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void estilizarCampoSenha(JPasswordField campo) {
        campo.setPreferredSize(new Dimension(250, 35));
        campo.setBackground(ThemeManager.getBg());
        campo.setForeground(ThemeManager.getText());
        campo.setCaretColor(ThemeManager.getAccent());
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorder(), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void estilizarBotaoPrincipal(JButton botao) {
        botao.setPreferredSize(new Dimension(140, 40));
        botao.setBackground(ThemeManager.getAccent());
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarBotaoSecundario(JButton botao) {
        botao.setPreferredSize(new Dimension(140, 40));
        botao.setContentAreaFilled(false);
        botao.setForeground(ThemeManager.getText());
        botao.setFont(new Font("SansSerif", Font.BOLD, 11));
        botao.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorder(), 1));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
