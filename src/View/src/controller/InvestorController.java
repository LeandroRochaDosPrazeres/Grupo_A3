package controller;

import view.InvestorRegistrationOptimizationView;
import view.ManagerInvestorHistoryView;
import view.InvestorDashboardView;

/**
 *
 * @author leandrorocha
 */

public class InvestorController {

    private InvestorRegistrationOptimizationView registrationView;
    private ManagerInvestorHistoryView historyView;
    private InvestorDashboardView dashboardView;
    private ManagerController managerController; // Permite navegar entre telas

    // Construtor 1: Cadastro
    public InvestorController(InvestorRegistrationOptimizationView registrationView, ManagerController managerController) {
        this.registrationView = registrationView;
        this.managerController = managerController;
    }

    // Construtor 2: Histórico
    public InvestorController(ManagerInvestorHistoryView historyView, ManagerController managerController) {
        this.historyView = historyView;
        this.managerController = managerController;
    }

    // Construtor 3: Dashboard
    public InvestorController(InvestorDashboardView dashboardView, ManagerController managerController) {
        this.dashboardView = dashboardView;
        this.managerController = managerController;
    }

    public void registerAndOptimize() {
        System.out.println("InvestorController: Calculando otimização...");
        if (managerController != null) {
            managerController.openSelectedInvestorDashboard(); // Abre o dashboard após cadastrar
        }
    }

    public void backToManagerMain() {
        if (managerController != null) {
            managerController.openInvestorHistory(); // Volta para o histórico ao clicar em voltar
        }
    }

    public void loadInvestorHistory(Object manager) {
        System.out.println("InvestorController: Atualizando dados...");
    }

    public void openSelectedInvestorDashboard() {
        if (managerController != null) {
            managerController.openSelectedInvestorDashboard(); // LOGA A MÁGICA DA MUDANÇA DE TELA!
        }
    }
}