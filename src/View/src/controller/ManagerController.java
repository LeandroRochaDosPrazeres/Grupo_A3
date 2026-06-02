package controller;

import app.AppContext;
import model.User;
import view.InvestorDashboardView;
import view.InvestorRegistrationOptimizationView;
import view.LoginView;
import view.ManagerInvestorHistoryView;
import view.ManagerMainView;

/**
 * Controller da jornada do Gerente.
 * Comanda a navegacao da ManagerMainView: troca o painel central entre o
 * cadastro/otimizacao e o historico de investidores. Repassa o usuario logado
 * e o contexto pro InvestorController fazer o trabalho pesado.
 *
 * @author leandrorocha
 */
public class ManagerController {

    private final ManagerMainView managerMainView; // tela principal do gerente
    private final User loggedUser;                  // gerente que esta logado
    private final AppContext context;               // DAOs + Services

    // injeta tela, usuario logado e contexto pelo construtor
    public ManagerController(ManagerMainView managerMainView, User loggedUser, AppContext context) {
        this.managerMainView = managerMainView;
        this.loggedUser = loggedUser;
        this.context = context;
    }

    // abre o formulario de "cadastrar e otimizar" no painel central
    public void openNewInvestorFlow() {
        InvestorRegistrationOptimizationView cadastroView = new InvestorRegistrationOptimizationView();
        InvestorController investorController = new InvestorController(cadastroView, this, loggedUser, context);
        cadastroView.setController(investorController);
        investorController.loadAssets(); // puxa os ativos do banco pra tabela de selecao
        managerMainView.showPanel(cadastroView);
    }

    // abre o historico de investidores do gerente no painel central
    public void openInvestorHistory() {
        ManagerInvestorHistoryView historicoView = new ManagerInvestorHistoryView();
        InvestorController investorController = new InvestorController(historicoView, this, loggedUser, context);
        historicoView.setController(investorController);
        investorController.loadInvestorHistory(); // carrega so os investidores desse gerente
        managerMainView.showPanel(historicoView);
    }

    // mostra o dashboard de um investidor (ja montado pelo InvestorController)
    public void showDashboard(InvestorDashboardView dashboardView) {
        managerMainView.showPanel(dashboardView);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public AppContext getContext() {
        return context;
    }

    // sai da conta do gerente e volta pra tela de login
    public void logout() {
        managerMainView.dispose();
        LoginView loginView = new LoginView();
        loginView.setController(new LoginController(loginView, context));
        loginView.showView();
    }
}
