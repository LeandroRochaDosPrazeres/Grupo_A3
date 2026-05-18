package controller;

import view.InvestorRegistrationOptimizationView; // Importação crucial

/**
 *
 * @author leandrorocha
 */

public class InvestorController {

    private InvestorRegistrationOptimizationView registrationView;

    public InvestorController(InvestorRegistrationOptimizationView registrationView) {
        this.registrationView = registrationView;
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