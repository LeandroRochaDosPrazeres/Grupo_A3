package controller;

import app.AppContext;
import model.User;
import view.AdminUserView;
import view.InvestorReadOnlyDashboardView;
import view.LoginView;
import view.ManagerMainView;

/**
 * Controller do login.
 * Pega email/senha da tela, manda pro AuthService validar e, se der certo,
 * abre a tela do perfil certo (Admin, Gerente ou Investidor).
 *
 * @author leandrorocha
 */
public class LoginController {

    // a tela de login que esse controller comanda
    private final LoginView loginView;
    // "caixa" com todos os DAOs e Services prontos pra usar
    private final AppContext context;

    // recebe a view e o contexto da aplicacao pelo construtor
    public LoginController(LoginView loginView, AppContext context) {
        this.loginView = loginView;
        this.context = context;
    }

    // chamado quando o usuario clica em "Login"
    public void handleLogin() {
        // 1. le os dados que o usuario digitou
        String email = loginView.getEmail();
        String senha = loginView.getPassword();

        // 2. nao deixa passar campo vazio
        if (email == null || email.trim().isEmpty() || senha == null || senha.isEmpty()) {
            loginView.showError("Por favor, preencha todos os campos.");
            return;
        }

        try {
            // 3. tenta autenticar de verdade (vai no banco via Service -> DAO)
            User user = context.getAuthService().login(email.trim(), senha);
            // 4. deu certo: abre a tela conforme o perfil e fecha o login
            abrirTelaPorPerfil(user);
            loginView.closeView();
        } catch (RuntimeException ex) {
            // 5. deu errado (senha errada, usuario inativo etc.): mostra o aviso
            loginView.showError(ex.getMessage());
        }
    }

    // decide qual tela abrir de acordo com o papel do usuario
    private void abrirTelaPorPerfil(User user) {
        if (user.isAdmin()) {
            // ADMIN -> tela de gestao de usuarios
            AdminUserView view = new AdminUserView();
            AdminUserController controller = new AdminUserController(view, context);
            view.setController(controller);
            view.setLoggedUserName(user.getName());
            controller.loadUsers(); // ja carrega a lista de usuarios do banco
            view.showView();
        } else if (user.isManager()) {
            // MANAGER -> painel do gerente (cadastro/otimizacao/historico)
            ManagerMainView view = new ManagerMainView();
            ManagerController controller = new ManagerController(view, user, context);
            view.setController(controller);
            view.setLoggedUserName(user.getName());
            view.showView();
        } else {
            // INVESTOR -> dashboard somente leitura com a carteira otimizada
            InvestorReadOnlyDashboardView view = new InvestorReadOnlyDashboardView();
            InvestorReadOnlyController controller = new InvestorReadOnlyController(view, user, context);
            view.setController(controller);
            view.setInvestorName(user.getName());
            controller.loadMyPortfolio(); // busca a carteira do investidor logado
            view.showView();
        }
    }
}
