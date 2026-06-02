package controller;

import app.AppContext;
import model.Investor;
import model.Optimization;
import model.Portfolio;
import model.User;
import view.InvestorReadOnlyDashboardView;
import view.LoginView;

import java.util.List;

/**
 * Controller da jornada do Investidor (so leitura).
 * Carrega a carteira otimizada do investidor logado e mostra na tela.
 * O investidor nao altera nada aqui na V1.
 *
 * @author leandrorocha
 */
public class InvestorReadOnlyController {

    private final InvestorReadOnlyDashboardView view; // tela do investidor
    private final User loggedUser;                    // usuario logado
    private final AppContext context;                 // DAOs + Services

    // injeta tela, usuario e contexto pelo construtor
    public InvestorReadOnlyController(InvestorReadOnlyDashboardView view, User loggedUser, AppContext context) {
        this.view = view;
        this.loggedUser = loggedUser;
        this.context = context;
    }

    // carrega a carteira do investidor ligado ao usuario que logou
    public void loadMyPortfolio() {
        try {
            // acha o investidor correspondente ao usuario logado
            Investor investor = encontrarInvestidor();
            if (investor == null) {
                // nao e erro: o investidor so ainda nao tem cadastro/carteira.
                // mostra um aviso calmo na propria tela, sem popup vermelho.
                view.mostrarEstadoVazio("Você ainda não possui uma carteira. Procure seu gerente.");
                return;
            }

            // preenche nome e perfil de risco la em cima
            view.setInvestorName(investor.getName());
            if (investor.getRiskProfile() != null) {
                view.setRiskProfile(investor.getRiskProfile().name());
            }

            // pega a carteira do investidor (na V1 usamos a primeira)
            List<Portfolio> carteiras = context.getPortfolioDAO().findByInvestor(investor.getId());
            if (carteiras.isEmpty()) {
                view.mostrarEstadoVazio("Você ainda não possui uma carteira otimizada.");
                return;
            }

            Portfolio portfolio = context.getPortfolioService()
                    .loadPortfolioWithItems(carteiras.get(0).getId());

            // pega a otimizacao mais recente (se houver) e joga tudo na tela
            List<Optimization> otimizacoes = context.getOptimizationDAO().findByPortfolio(portfolio.getId());
            Optimization ultima = otimizacoes.isEmpty() ? null : otimizacoes.get(otimizacoes.size() - 1);

            view.loadPortfolioItems(portfolio.getItems());
            view.setOptimizationSummary(ultima);
        } catch (RuntimeException ex) {
            // ai sim, se deu erro de verdade (conexao etc.), mostra o popup
            view.showError("Erro ao carregar seu portfólio: " + ex.getMessage());
        }
    }

    // procura nos investidores um cujo nome bate com o do usuario logado.
    // (vinculo simples da V1; o ideal seria uma FK user_id em investors)
    private Investor encontrarInvestidor() {
        List<Investor> todos = context.getInvestorDAO().findAll();
        for (Investor inv : todos) {
            if (inv.getName() != null && inv.getName().equalsIgnoreCase(loggedUser.getName())) {
                return inv;
            }
        }
        return null;
    }

    // sai da conta e volta pro login
    public void logout() {
        view.closeView();
        LoginView loginView = new LoginView();
        loginView.setController(new LoginController(loginView, context));
        loginView.showView();
    }
}
