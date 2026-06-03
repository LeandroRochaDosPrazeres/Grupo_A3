package test;

import model.Asset;
import model.Investor;
import model.Optimization;
import model.PortfolioItem;
import model.RiskProfile;
import model.User;
import model.UserRole;
import view.AdminUserView;
import view.InvestorDashboardView;
import view.InvestorReadOnlyDashboardView;
import view.InvestorRegistrationOptimizationView;
import view.LoginView;
import view.ManagerInvestorHistoryView;
import view.ManagerMainView;
import view.UserFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViewLayerTest {

    private static int total = 0;
    private static int ok = 0;

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");

        runOnEdt(ViewLayerTest::testLoginView);
        runOnEdt(ViewLayerTest::testAdminUserView);
        runOnEdt(ViewLayerTest::testUserFormDialog);
        runOnEdt(ViewLayerTest::testManagerMainView);
        runOnEdt(ViewLayerTest::testManagerInvestorHistoryView);
        runOnEdt(ViewLayerTest::testInvestorRegistrationOptimizationView);
        runOnEdt(ViewLayerTest::testInvestorDashboardView);
        runOnEdt(ViewLayerTest::testInvestorReadOnlyDashboardView);

        System.out.println("======================================");
        System.out.println("VIEW LAYER RIGOROUS TEST");
        System.out.println("Total de verificações: " + total);
        System.out.println("Sucessos: " + ok);
        System.out.println("Falhas: " + (total - ok));
        System.out.println("======================================");

        if (total != ok) {
            throw new RuntimeException("Falhas encontradas na camada view.");
        }
    }

    private static void testLoginView() throws Exception {
        LoginView view = new LoginView();

        JTextField txtEmail = getField(view, "txtEmail", JTextField.class);
        JPasswordField txtPassword = getField(view, "txtPassword", JPasswordField.class);
        JButton btnLogin = getField(view, "btnLogin", JButton.class);
        JButton btnThemeToggle = getField(view, "btnThemeToggle", JButton.class);
        JLabel lblLogo = getField(view, "lblLogo", JLabel.class);

        txtEmail.setText("teste@empresa.com");
        txtPassword.setText("123456");

        checkEquals("teste@empresa.com", view.getEmail(), "LoginView.getEmail()");
        checkEquals("123456", view.getPassword(), "LoginView.getPassword()");
        checkNotNull(btnLogin, "LoginView deve criar btnLogin");
        checkNotNull(btnThemeToggle, "LoginView deve criar btnThemeToggle");
        checkNotNull(lblLogo, "LoginView deve criar lblLogo");

        view.showView();
        checkTrue(view.isVisible(), "LoginView.showView() deve exibir a janela");

        view.closeView();
        checkFalse(view.isDisplayable(), "LoginView.closeView() deve encerrar a janela");
    }

    private static void testAdminUserView() throws Exception {
        AdminUserView view = new AdminUserView();

        JTable tblUsers = getField(view, "tblUsers", JTable.class);
        DefaultTableModel tableModel = getField(view, "tableModel", DefaultTableModel.class);
        JLabel lblUserName = getField(view, "lblUserName", JLabel.class);

        List<User> users = new ArrayList<>();

        User u1 = new User();
        u1.setId(1L);
        u1.setName("Ana");
        u1.setEmail("ana@empresa.com");
        u1.setRole(UserRole.ADMIN);
        u1.setManagerCode("");
        u1.setActive(true);
        u1.setCreatedAt(OffsetDateTime.parse("2026-01-10T10:15:30+00:00"));
        users.add(u1);

        User u2 = new User();
        u2.setId(2L);
        u2.setName("Bruno");
        u2.setEmail("bruno@empresa.com");
        u2.setRole(UserRole.MANAGER);
        u2.setManagerCode("MGR-01");
        u2.setActive(false);
        u2.setCreatedAt(OffsetDateTime.parse("2026-02-11T08:00:00+00:00"));
        users.add(u2);

        view.loadUsersTable(users);

        checkEquals(2, tableModel.getRowCount(), "AdminUserView.loadUsersTable() deve carregar 2 linhas");
        checkEquals(7, tableModel.getColumnCount(), "AdminUserView deve ter 7 colunas");
        checkEquals("Ana", String.valueOf(tableModel.getValueAt(0, 1)), "Nome da primeira linha");
        checkEquals("ADMIN", String.valueOf(tableModel.getValueAt(0, 3)), "Perfil da primeira linha");
        checkEquals("—", String.valueOf(tableModel.getValueAt(0, 4)), "Manager code vazio deve virar traço");
        checkEquals("Sim", String.valueOf(tableModel.getValueAt(0, 5)), "Ativo true deve virar Sim");
        checkEquals("Não", String.valueOf(tableModel.getValueAt(1, 5)), "Ativo false deve virar Não");

        tblUsers.setRowSelectionInterval(1, 1);
        checkEquals(2L, view.getSelectedUserId(), "AdminUserView.getSelectedUserId() deve ler ID da linha selecionada");

        tblUsers.clearSelection();
        checkNull(view.getSelectedUserId(), "AdminUserView.getSelectedUserId() sem seleção deve retornar null");

        view.setLoggedUserName("Carlos Admin");
        checkEquals("Admin: Carlos Admin", lblUserName.getText(), "AdminUserView.setLoggedUserName()");

        view.showView();
        checkTrue(view.isVisible(), "AdminUserView.showView()");
        view.closeView();
        checkFalse(view.isDisplayable(), "AdminUserView.closeView()");
    }

    private static void testUserFormDialog() throws Exception {
        JFrame parent = new JFrame();
        UserFormDialog dialog = new UserFormDialog(parent);

        JTextField txtManagerCode = getField(dialog, "txtManagerCode", JTextField.class);
        JLabel lblManagerCode = getField(dialog, "lblManagerCode", JLabel.class);
        JCheckBox chkActive = getField(dialog, "chkActive", JCheckBox.class);

        dialog.setNameForTest("Maria da Silva");
        dialog.setEmailForTest("maria@empresa.com");
        dialog.setPasswordForTest("senha123");
        dialog.setManagerCodeForTest("COD-99");

        checkEquals("Maria da Silva", dialog.getName(), "UserFormDialog.getName()");
        checkEquals("maria@empresa.com", dialog.getEmail(), "UserFormDialog.getEmail()");
        checkEquals("senha123", dialog.getPassword(), "UserFormDialog.getPassword()");
        checkTrue(dialog.isActive(), "UserFormDialog deve iniciar ativo");

        dialog.setRoleForTest("ADMIN");
        checkEquals("ADMIN", dialog.getSelectedRole(), "Mapeamento ADMIN");
        checkFalse(txtManagerCode.isVisible(), "Campo manager code deve ficar oculto para ADMIN");
        checkFalse(lblManagerCode.isVisible(), "Label manager code deve ficar oculta para ADMIN");

        dialog.setRoleForTest("MANAGER");
        checkEquals("MANAGER", dialog.getSelectedRole(), "Mapeamento MANAGER");
        checkTrue(txtManagerCode.isVisible(), "Campo manager code deve aparecer para MANAGER");
        checkTrue(lblManagerCode.isVisible(), "Label manager code deve aparecer para MANAGER");

        dialog.setRoleForTest("INVESTOR");
        checkEquals("INVESTOR", dialog.getSelectedRole(), "Mapeamento INVESTOR");

        checkEquals("COD-99", dialog.getManagerCode(), "UserFormDialog.getManagerCode()");

        chkActive.setSelected(false);
        checkFalse(dialog.isActive(), "UserFormDialog.isActive() deve refletir checkbox");

        dialog.closeDialog();
        checkFalse(dialog.isDisplayable(), "UserFormDialog.closeDialog()");
        parent.dispose();
    }

    private static void testManagerMainView() throws Exception {
        ManagerMainView view = new ManagerMainView();

        JLabel lblUserName = getField(view, "lblUserName", JLabel.class);
        JPanel pnlContent = getField(view, "pnlContent", JPanel.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("painel-teste");
        panel.add(new JLabel("Conteúdo interno"), BorderLayout.CENTER);

        view.setLoggedUserName("Gerente João");
        checkEquals("Gerente: Gerente João", lblUserName.getText(), "ManagerMainView.setLoggedUserName()");

        int antes = pnlContent.getComponentCount();
        view.showPanel(panel);
        int depois = pnlContent.getComponentCount();

        checkTrue(depois >= 1, "ManagerMainView.showPanel() deve inserir painel");
        checkNotNull(findComponentByName(pnlContent, "painel-teste"), "ManagerMainView.showPanel() deve manter painel inserido");
        checkTrue(depois != antes || depois == 1, "ManagerMainView.showPanel() deve reconfigurar conteúdo");

        view.showView();
        checkTrue(view.isVisible(), "ManagerMainView.showView()");
        view.dispose();
        checkFalse(view.isDisplayable(), "ManagerMainView.dispose()");
    }

    private static void testManagerInvestorHistoryView() throws Exception {
        ManagerInvestorHistoryView view = new ManagerInvestorHistoryView();

        JTable tblInvestors = getField(view, "tblInvestors", JTable.class);
        DefaultTableModel tableModel = getField(view, "tableModel", DefaultTableModel.class);

        List<Investor> investidores = new ArrayList<>();

        Investor i1 = new Investor();
        i1.setId(10L);
        i1.setName("Ana Invest");
        i1.setDocumentId("111.111.111-11");
        i1.setRiskProfile(RiskProfile.CONSERVATIVE);
        i1.setCreatedAt(OffsetDateTime.parse("2026-03-01T10:00:00+00:00"));
        investidores.add(i1);

        Investor i2 = new Investor();
        i2.setId(20L);
        i2.setName("Bruno Invest");
        i2.setDocumentId("222.222.222-22");
        i2.setRiskProfile(RiskProfile.AGGRESSIVE);
        i2.setCreatedAt(OffsetDateTime.parse("2026-03-02T10:00:00+00:00"));
        investidores.add(i2);

        view.loadInvestorsTable(investidores);

        checkEquals(2, view.getRowCountForTest(), "ManagerInvestorHistoryView deve carregar 2 investidores");
        checkEquals("Ana Invest", String.valueOf(tableModel.getValueAt(0, 0)), "Nome investidor linha 1");
        checkEquals("111.111.111-11", String.valueOf(tableModel.getValueAt(0, 1)), "Documento investidor linha 1");
        checkEquals("CONSERVATIVE", String.valueOf(tableModel.getValueAt(0, 2)), "Perfil deve usar enum name()");
        checkEquals("—", String.valueOf(tableModel.getValueAt(0, 3)), "Coluna ativos selecionados hoje fica com traço");

        tblInvestors.setRowSelectionInterval(1, 1);
        checkEquals(20L, view.getSelectedInvestorId(), "ManagerInvestorHistoryView.getSelectedInvestorId()");
        checkEquals("222.222.222-22", view.getSelectedInvestorDocument(), "ManagerInvestorHistoryView.getSelectedInvestorDocument()");

        tblInvestors.clearSelection();
        checkNull(view.getSelectedInvestorId(), "Sem seleção, getSelectedInvestorId() deve retornar null");
        checkNull(view.getSelectedInvestorDocument(), "Sem seleção, getSelectedInvestorDocument() deve retornar null");
    }

    private static void testInvestorRegistrationOptimizationView() throws Exception {
        InvestorRegistrationOptimizationView view = new InvestorRegistrationOptimizationView();

        view.setInvestorNameForTest("Clara Lima");
        view.setDocumentIdForTest("333.333.333-33");
        view.setRiskProfileForTest("AGRESSIVO");

        checkEquals("Clara Lima", view.getInvestorName(), "InvestorRegistrationOptimizationView.getInvestorName()");
        checkEquals("333.333.333-33", view.getDocumentId(), "InvestorRegistrationOptimizationView.getDocumentId()");
        checkEquals("AGGRESSIVE", view.getSelectedRiskProfile(), "Mapeamento AGRESSIVO -> AGGRESSIVE");

        List<Asset> assets = new ArrayList<>();

        Asset a1 = new Asset();
        a1.setId(1L);
        a1.setTicker("PETR4");
        a1.setName("Petrobras");
        a1.setCategory("Ação");
        a1.setBaseRisk(new BigDecimal("0.90"));
        assets.add(a1);

        Asset a2 = new Asset();
        a2.setId(2L);
        a2.setTicker("VALE3");
        a2.setName("Vale");
        a2.setCategory("Ação");
        a2.setBaseRisk(new BigDecimal("0.80"));
        assets.add(a2);

        Asset a3 = new Asset();
        a3.setId(3L);
        a3.setTicker("TESOURO2029");
        a3.setName("Tesouro IPCA+");
        a3.setCategory("Renda Fixa");
        a3.setBaseRisk(new BigDecimal("0.20"));
        assets.add(a3);

        view.loadAssetsTable(assets);
        view.selecionarAtivosParaTeste(2);

        List<Asset> selecionados = view.getSelectedAssets();
        checkEquals(2, selecionados.size(), "getSelectedAssets() deve retornar 2 ativos marcados");
        checkEquals("PETR4", selecionados.get(0).getTicker(), "Primeiro ativo selecionado");
        checkEquals("VALE3", selecionados.get(1).getTicker(), "Segundo ativo selecionado");

        view.clearForm();
        checkEquals("", view.getInvestorName(), "clearForm() deve limpar nome");
        checkEquals("", view.getDocumentId(), "clearForm() deve limpar documento");
        checkEquals("CONSERVATIVE", view.getSelectedRiskProfile(), "clearForm() deve resetar combo para CONSERVADOR");
        checkEquals(0, view.getSelectedAssets().size(), "clearForm() deve desmarcar ativos");
    }

    private static void testInvestorDashboardView() throws Exception {
        InvestorDashboardView view = new InvestorDashboardView();

        JLabel lblNomeInvestidor = getField(view, "lblNomeInvestidor", JLabel.class);
        JLabel lblPerfilRisco = getField(view, "lblPerfilRisco", JLabel.class);
        JLabel lblRetornoEsperado = getField(view, "lblRetornoEsperado", JLabel.class);
        JLabel lblVolatilidade = getField(view, "lblVolatilidade", JLabel.class);
        DefaultTableModel tableModel = getField(view, "tableModel", DefaultTableModel.class);

        Investor investor = new Investor();
        investor.setName("Roberto Dias");
        investor.setRiskProfile(RiskProfile.MODERATE);

        view.setInvestorData(investor);
        checkEquals("Investidor: Roberto Dias", lblNomeInvestidor.getText(), "InvestorDashboardView.setInvestorData() nome");
        checkEquals("Perfil: MODERATE", lblPerfilRisco.getText(), "InvestorDashboardView.setInvestorData() perfil");

        Asset asset1 = new Asset();
        asset1.setId(1L);
        asset1.setTicker("BOVA11");
        asset1.setName("ETF Ibovespa");
        asset1.setCategory("ETF");

        Asset asset2 = new Asset();
        asset2.setId(2L);
        asset2.setTicker("TESOURO2029");
        asset2.setName("Tesouro");
        asset2.setCategory("Renda Fixa");

        PortfolioItem p1 = new PortfolioItem();
        p1.setAsset(asset1);
        p1.setAssetId(1L);
        p1.setQuantity(new BigDecimal("10"));
        p1.setAveragePrice(new BigDecimal("100"));
        p1.setSuggestedPercentage(new BigDecimal("0.40"));

        PortfolioItem p2 = new PortfolioItem();
        p2.setAsset(asset2);
        p2.setAssetId(2L);
        p2.setQuantity(new BigDecimal("5"));
        p2.setAveragePrice(new BigDecimal("200"));
        p2.setSuggestedPercentage(new BigDecimal("0.60"));

        view.loadPortfolioItems(List.of(p1, p2));

        checkEquals(2, view.getRowCountForTest(), "InvestorDashboardView.loadPortfolioItems()");
        checkEquals("BOVA11", String.valueOf(tableModel.getValueAt(0, 0)), "Ticker linha 1");
        checkEquals("ETF Ibovespa", String.valueOf(tableModel.getValueAt(0, 1)), "Nome linha 1");
        checkEquals("ETF", String.valueOf(tableModel.getValueAt(0, 2)), "Categoria linha 1");
        checkEquals("40.0%", String.valueOf(tableModel.getValueAt(0, 3)), "Percentual linha 1");
        checkEquals("R$ 1000.00", String.valueOf(tableModel.getValueAt(0, 4)), "Valor posição linha 1");

        Optimization opt = new Optimization();
        opt.setExpectedReturn(new BigDecimal("0.1234"));
        opt.setTotalRisk(new BigDecimal("0.0567"));

        view.setOptimizationSummary(opt);
        checkEquals("12.34% a.a.", lblRetornoEsperado.getText(), "Resumo retorno");
        checkEquals("5.67% a.a.", lblVolatilidade.getText(), "Resumo risco");

        view.setOptimizationSummary(null);
        checkEquals("—", lblRetornoEsperado.getText(), "Resumo retorno nulo");
        checkEquals("—", lblVolatilidade.getText(), "Resumo risco nulo");
    }

    private static void testInvestorReadOnlyDashboardView() throws Exception {
        InvestorReadOnlyDashboardView view = new InvestorReadOnlyDashboardView();

        JLabel lblInvestorName = getField(view, "lblInvestorName", JLabel.class);
        JLabel lblRiskProfile = getField(view, "lblRiskProfile", JLabel.class);
        JLabel lblExpectedReturn = getField(view, "lblExpectedReturn", JLabel.class);
        JLabel lblTotalRisk = getField(view, "lblTotalRisk", JLabel.class);
        JLabel lblUserName = getField(view, "lblUserName", JLabel.class);
        DefaultTableModel tableModel = getField(view, "tableModel", DefaultTableModel.class);

        view.setInvestorName("Fernanda Rocha");
        view.setRiskProfile("AGGRESSIVE");
        view.setExpectedReturn("15.00% a.a.");
        view.setTotalRisk("8.00% a.a.");

        checkEquals("Fernanda Rocha", lblInvestorName.getText(), "InvestorReadOnlyDashboardView.setInvestorName()");
        checkEquals("Investidor: Fernanda Rocha", lblUserName.getText(), "InvestorReadOnlyDashboardView deve atualizar label lateral");
        checkEquals("Perfil: AGGRESSIVE", lblRiskProfile.getText(), "InvestorReadOnlyDashboardView.setRiskProfile()");
        checkEquals("15.00% a.a.", lblExpectedReturn.getText(), "InvestorReadOnlyDashboardView.setExpectedReturn()");
        checkEquals("8.00% a.a.", lblTotalRisk.getText(), "InvestorReadOnlyDashboardView.setTotalRisk()");

        Asset asset = new Asset();
        asset.setId(1L);
        asset.setTicker("KNRI11");
        asset.setName("Kinea Renda Imobiliária");
        asset.setCategory("FII");

        PortfolioItem item = new PortfolioItem();
        item.setAsset(asset);
        item.setAssetId(1L);
        item.setQuantity(new BigDecimal("20"));
        item.setAveragePrice(new BigDecimal("50"));
        item.setSuggestedPercentage(new BigDecimal("0.25"));

        view.loadPortfolioItems(List.of(item));
        checkEquals(1, view.getRowCountForTest(), "InvestorReadOnlyDashboardView.loadPortfolioItems()");
        checkEquals("KNRI11", String.valueOf(tableModel.getValueAt(0, 0)), "Ticker na tabela readonly");
        checkEquals("25.0%", String.valueOf(tableModel.getValueAt(0, 3)), "Percentual na tabela readonly");
        checkEquals("R$ 1000.00", String.valueOf(tableModel.getValueAt(0, 4)), "Valor na tabela readonly");

        Optimization opt = new Optimization();
        opt.setExpectedReturn(new BigDecimal("0.0987"));
        opt.setTotalRisk(new BigDecimal("0.0345"));

        view.setOptimizationSummary(opt);
        checkEquals("9.87% a.a.", lblExpectedReturn.getText(), "Readonly resumo retorno");
        checkEquals("3.45% a.a.", lblTotalRisk.getText(), "Readonly resumo risco");

        view.mostrarEstadoVazio("Nenhuma carteira encontrada");
        checkEquals(0, view.getRowCountForTest(), "mostrarEstadoVazio() deve limpar tabela");
        checkEquals("—", lblExpectedReturn.getText(), "mostrarEstadoVazio() limpa retorno");
        checkEquals("—", lblTotalRisk.getText(), "mostrarEstadoVazio() limpa risco");
        checkEquals("Nenhuma carteira encontrada", lblRiskProfile.getText(), "mostrarEstadoVazio() reaproveita label de perfil");

        view.closeView();
        checkFalse(view.isDisplayable(), "InvestorReadOnlyDashboardView.closeView()");
    }

    private static void runOnEdt(TestBlock block) throws Exception {
        if (SwingUtilities.isEventDispatchThread()) {
            block.run();
            return;
        }

        final Exception[] ex = new Exception[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                block.run();
            } catch (Exception e) {
                ex[0] = e;
            }
        });

        if (ex[0] != null) throw ex[0];
    }

    private static <T> T getField(Object target, String fieldName, Class<T> type) throws Exception {
        Field f = findField(target.getClass(), fieldName);
        f.setAccessible(true);
        Object value = f.get(target);
        return type.cast(value);
    }

    private static Field findField(Class<?> clazz, String name) throws Exception {
        Class<?> c = clazz;
        while (c != null) {
            try {
                return c.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }

    private static Component findComponentByName(Container root, String name) {
        for (Component c : root.getComponents()) {
            if (name.equals(c.getName())) return c;
            if (c instanceof Container) {
                Component child = findComponentByName((Container) c, name);
                if (child != null) return child;
            }
        }
        return null;
    }

    private static void checkTrue(boolean condition, String message) {
        total++;
        if (!condition) throw new AssertionError(message);
        ok++;
    }

    private static void checkFalse(boolean condition, String message) {
        checkTrue(!condition, message);
    }

    private static void checkNull(Object value, String message) {
        checkTrue(value == null, message + " | esperado: null, obtido: " + value);
    }

    private static void checkNotNull(Object value, String message) {
        checkTrue(value != null, message);
    }

    private static void checkEquals(Object expected, Object actual, String message) {
        total++;
        boolean equals = (expected == null && actual == null)
                || (expected != null && expected.equals(actual));
        if (!equals) {
            throw new AssertionError(message + " | esperado: " + expected + " | obtido: " + actual);
        }
        ok++;
    }

    @FunctionalInterface
    private interface TestBlock {
        void run() throws Exception;
    }
}