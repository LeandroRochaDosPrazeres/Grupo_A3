package controller;

import view.ManagerMainView;
import view.InvestorRegistrationOptimizationView; // Importa a nova view de cadastro
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
        // 1. Instancia a tela de cadastro (o JPanel que acabamos de criar)
        InvestorRegistrationOptimizationView cadastroView = new InvestorRegistrationOptimizationView();
        
        // 2. Cria o controlador específico para cuidar das ações dela
        InvestorController investorController = new InvestorController(cadastroView);
        cadastroView.setController(investorController);
        
        // 3. Manda a tela principal do gerente desenhar esse painel no centro!
        managerMainView.showPanel(cadastroView);
        System.out.println("Controller: Fluxo de novo investidor injetado com sucesso no painel central.");
    }

    public void openInvestorHistory() {
        System.out.println("Controller: Abrindo histórico de investidores...");
    }

    public void logout() {
        System.out.println("Controller: Realizando logout do gerente...");
    }
}