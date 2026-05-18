package controller;

import view.ManagerMainView;
import view.InvestorRegistrationOptimizationView; 
import view.ManagerInvestorHistoryView; 
import view.InvestorDashboardView; 

/**
 *
 * @author leandrorocha
 */

public class ManagerController {

    private ManagerMainView managerMainView;

    public ManagerController(ManagerMainView managerMainView) {
        this.managerMainView = managerMainView;
    }

    public void openNewInvestorFlow() {
        InvestorRegistrationOptimizationView cadastroView = new InvestorRegistrationOptimizationView();
        InvestorController investorController = new InvestorController(cadastroView, this); // Passa o navegador
        cadastroView.setController(investorController);
        managerMainView.showPanel(cadastroView);
    }

    public void openInvestorHistory() {
        ManagerInvestorHistoryView historicoView = new ManagerInvestorHistoryView();
        InvestorController investorController = new InvestorController(historicoView, this); // Passa o navegador
        historicoView.setController(investorController);
        managerMainView.showPanel(historicoView);
    }

    public void openSelectedInvestorDashboard() {
        InvestorDashboardView dashboardView = new InvestorDashboardView();
        InvestorController investorController = new InvestorController(dashboardView, this); // Passa o navegador
        dashboardView.setController(investorController);
        managerMainView.showPanel(dashboardView);
    }

    public void logout() {
        System.out.println("Controller: Realizando logout do gerente...");
    }
}