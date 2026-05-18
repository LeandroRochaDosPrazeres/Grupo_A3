package controller;

import view.ManagerMainView;
import view.InvestorRegistrationOptimizationView; 
import view.ManagerInvestorHistoryView; // ESTA LINHA RESOLVE O ERRO!

/**
 *
 * @author leandrorocha
 */

public class ManagerController {

    private ManagerMainView managerMainView;

    public ManagerController(ManagerMainView managerMainView) {
        this.managerMainView = managerMainView;
    }

    // --- MÉTODOS CONTRATUAIS DA JORNADA DO GERENTE ---

    public void openNewInvestorFlow() {
        // 1. Instancia a tela de cadastro (o JPanel que criamos)
        InvestorRegistrationOptimizationView cadastroView = new InvestorRegistrationOptimizationView();
        
        // 2. Cria o controlador específico para cuidar das ações dela
        InvestorController investorController = new InvestorController(cadastroView);
        cadastroView.setController(investorController);
        
        // 3. Manda a tela principal do gerente desenhar esse painel no centro!
        managerMainView.showPanel(cadastroView);
        System.out.println("Controller: Fluxo de novo investidor injetado com sucesso no painel central.");
    }

    public void openInvestorHistory() {
        // 1. Instancia a tela de histórico
        ManagerInvestorHistoryView historicoView = new ManagerInvestorHistoryView();
        
        // 2. Cria e vincula o controlador dela
        InvestorController investorController = new InvestorController(historicoView);
        historicoView.setController(investorController);
        
        // 3. Substitui o painel dinâmico da tela principal pelo histórico
        managerMainView.showPanel(historicoView);
        System.out.println("Controller: Painel de histórico de investidores injetado com sucesso.");
    }

    public void logout() {
        System.out.println("Controller: Realizando logout do gerente...");
    }
}