package controller;

import view.InvestorRegistrationOptimizationView;
import view.ManagerInvestorHistoryView; // Importação adicionada para a tela de histórico
/**
 *
 * @author leandrorocha
 */


public class InvestorController {

    private InvestorRegistrationOptimizationView registrationView;
    private ManagerInvestorHistoryView historyView; // Atributo adicionado para gerir o histórico

    // Construtor 1: Vinculado à tela de Cadastro/Otimização
    public InvestorController(InvestorRegistrationOptimizationView registrationView) {
        this.registrationView = registrationView;
    }

    // Construtor 2: Vinculado à tela de Histórico (Resolve o erro do ManagerController)
    public InvestorController(ManagerInvestorHistoryView historyView) {
        this.historyView = historyView;
    }

    public void registerAndOptimize() {
        System.out.println("InvestorController: Executando fluxo 'Cadastrar e Otimizar'...");
    }

    public void startNewInvestorRegistration() {
        if (registrationView != null) {
            registrationView.clearForm(); // Chama o método de limpeza da View
        }
    }

    public void backToManagerMain() {
        System.out.println("InvestorController: Solicitando retorno ao menu principal.");
    }

    public void loadInvestorHistory(Object manager) {
        System.out.println("InvestorController: Carregando histórico...");
    }

    public void openSelectedInvestorDashboard() {
        System.out.println("InvestorController: Abrindo dashboard...");
    }
}