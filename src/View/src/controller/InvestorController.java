package controller;

import app.AppContext;
import model.Asset;
import model.Investor;
import model.Optimization;
import model.Portfolio;
import model.PortfolioItem;
import model.RiskProfile;
import model.User;
import view.InvestorDashboardView;
import view.InvestorRegistrationOptimizationView;
import view.ManagerInvestorHistoryView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller "faz tudo" da parte de investidor dentro da jornada do Gerente.
 * Liga as telas (cadastro, historico e dashboard) com os Services de verdade:
 * cadastra investidor, monta a carteira, roda a otimizacao e mostra o resultado.
 *
 * @author leandrorocha
 */
public class InvestorController {

    // so uma destas telas fica preenchida por vez, dependendo de qual fluxo abriu
    private InvestorRegistrationOptimizationView registrationView;
    private ManagerInvestorHistoryView historyView;
    private InvestorDashboardView dashboardView;

    private final ManagerController managerController; // pra navegar entre telas
    private final User loggedUser;                     // gerente logado
    private final AppContext context;                  // DAOs + Services

    // Na V1 nao pedimos valor/quantidade na tela, entao usamos um total fixo
    // de R$ 100 mil e dividimos igualmente entre os ativos so pra ter um numero.
    private static final BigDecimal VALOR_BASE_PORTFOLIO = new BigDecimal("100000");

    // construtor usado quando abrimos a tela de cadastro/otimizacao
    public InvestorController(InvestorRegistrationOptimizationView registrationView,
                              ManagerController managerController, User loggedUser, AppContext context) {
        this.registrationView = registrationView;
        this.managerController = managerController;
        this.loggedUser = loggedUser;
        this.context = context;
    }

    // construtor usado quando abrimos a tela de historico
    public InvestorController(ManagerInvestorHistoryView historyView,
                              ManagerController managerController, User loggedUser, AppContext context) {
        this.historyView = historyView;
        this.managerController = managerController;
        this.loggedUser = loggedUser;
        this.context = context;
    }

    // ====================================================================
    // ATIVOS DA TELA DE CADASTRO
    // ====================================================================

    // busca os ativos no banco e joga na tabela de selecao da tela de cadastro
    public void loadAssets() {
        if (registrationView == null) return;
        try {
            List<Asset> assets = context.getAssetDAO().findAll();
            registrationView.loadAssetsTable(assets);
        } catch (RuntimeException ex) {
            registrationView.showError("Não foi possível carregar os ativos: " + ex.getMessage());
        }
    }

    // ====================================================================
    // FLUXO PRINCIPAL: CADASTRAR E OTIMIZAR
    // ====================================================================

    // o coracao do sistema: cadastra o investidor, cria a carteira, adiciona os
    // ativos, roda a otimizacao e mostra o dashboard com o resultado
    public void registerAndOptimize() {
        if (registrationView == null) return;

        // pega tudo que o gerente preencheu na tela
        String nome = registrationView.getInvestorName();
        String documento = registrationView.getDocumentId();
        String perfilStr = registrationView.getSelectedRiskProfile();
        List<Asset> ativosSelecionados = registrationView.getSelectedAssets();

        // validacoes simples de tela (as regras de negocio ficam no Service)
        if (nome == null || nome.trim().isEmpty()) {
            registrationView.showError("Informe o nome do investidor.");
            return;
        }
        if (documento == null || documento.trim().isEmpty()) {
            registrationView.showError("Informe o documento (CPF) do investidor.");
            return;
        }
        if (ativosSelecionados == null || ativosSelecionados.isEmpty()) {
            registrationView.showError("Selecione pelo menos um ativo para compor o portfólio.");
            return;
        }

        try {
            // 1. cria o investidor (o Service valida que e gerente e grava o log)
            Investor investor = new Investor();
            investor.setName(nome.trim());
            investor.setDocumentId(documento.trim());
            investor.setRiskProfile(RiskProfile.valueOf(perfilStr));
            Investor investidorSalvo = context.getInvestorService().createInvestor(investor, loggedUser);

            // 2. cria a carteira ligada a esse investidor
            Portfolio portfolio = new Portfolio();
            portfolio.setInvestorId(investidorSalvo.getId());
            portfolio.setName("Carteira de " + investidorSalvo.getName());
            Portfolio portfolioSalvo = context.getPortfolioService().createPortfolio(portfolio);

            // 3. divide o valor base entre os ativos e adiciona cada um na carteira
            BigDecimal valorPorAtivo = VALOR_BASE_PORTFOLIO.divide(
                    BigDecimal.valueOf(ativosSelecionados.size()), 2, java.math.RoundingMode.HALF_UP);
            for (Asset asset : ativosSelecionados) {
                PortfolioItem item = new PortfolioItem();
                item.setPortfolioId(portfolioSalvo.getId());
                item.setAssetId(asset.getId());
                // preco medio simbolico (1) e quantidade = fatia do valor base
                item.setAveragePrice(BigDecimal.ONE);
                item.setQuantity(valorPorAtivo);
                context.getPortfolioService().addItem(item);
            }

            // 4. roda o motor de otimizacao (distribui % conforme o perfil de risco)
            Optimization otimizacao = context.getOptimizerService()
                    .optimizePortfolio(portfolioSalvo.getId(), loggedUser);

            // 5. recarrega a carteira ja com os percentuais sugeridos gravados
            Portfolio portfolioCompleto = context.getPortfolioService()
                    .loadPortfolioWithItems(portfolioSalvo.getId());
            portfolioCompleto.setInvestor(investidorSalvo);

            // 6. mostra o dashboard com os numeros reais e avisa que deu certo
            mostrarDashboard(investidorSalvo, portfolioCompleto, otimizacao);
            registrationView.showSuccess("Investidor cadastrado e portfólio otimizado com sucesso!");

        } catch (RuntimeException ex) {
            registrationView.showError("Erro ao cadastrar/otimizar: " + ex.getMessage());
        }
    }

    // monta o dashboard e pede pro ManagerController exibir no painel central
    private void mostrarDashboard(Investor investor, Portfolio portfolio, Optimization optimization) {
        dashboardView = new InvestorDashboardView();
        dashboardView.setController(this);
        dashboardView.setInvestorData(investor);
        dashboardView.loadPortfolioItems(portfolio.getItems());
        dashboardView.setOptimizationSummary(optimization);
        if (managerController != null) {
            managerController.showDashboard(dashboardView);
        }
    }

    // ====================================================================
    // HISTÓRICO DE INVESTIDORES
    // ====================================================================

    // carrega na tabela so os investidores cadastrados por este gerente
    public void loadInvestorHistory() {
        if (historyView == null) return;
        try {
            List<Investor> investidores = context.getInvestorService().findByManager(loggedUser.getId());
            historyView.loadInvestorsTable(investidores);
        } catch (RuntimeException ex) {
            historyView.showError("Não foi possível carregar o histórico: " + ex.getMessage());
        }
    }

    // abre o dashboard do investidor que o gerente selecionou na tabela
    public void openSelectedInvestorDashboard() {
        if (historyView == null) return;

        // descobre quem foi selecionado
        Long investorId = historyView.getSelectedInvestorId();
        if (investorId == null) {
            historyView.showError("Selecione um investidor na tabela para visualizar a carteira.");
            return;
        }

        try {
            Investor investor = context.getInvestorService().getInvestorById(investorId);

            // pega a carteira do investidor (na V1 usamos a primeira)
            List<Portfolio> carteiras = context.getPortfolioDAO().findByInvestor(investorId);
            if (carteiras.isEmpty()) {
                historyView.showError("Este investidor ainda não possui carteira cadastrada.");
                return;
            }
            Portfolio portfolio = context.getPortfolioService()
                    .loadPortfolioWithItems(carteiras.get(0).getId());
            portfolio.setInvestor(investor);

            // pega a otimizacao mais recente (se ja tiver rodado alguma)
            List<Optimization> otimizacoes = context.getOptimizationDAO().findByPortfolio(portfolio.getId());
            Optimization ultima = otimizacoes.isEmpty() ? null : otimizacoes.get(otimizacoes.size() - 1);

            mostrarDashboard(investor, portfolio, ultima);
        } catch (RuntimeException ex) {
            historyView.showError("Erro ao abrir a carteira: " + ex.getMessage());
        }
    }

    // ====================================================================
    // NAVEGAÇÃO
    // ====================================================================

    // botao "voltar": leva de volta pro historico de investidores
    public void backToManagerMain() {
        if (managerController != null) {
            managerController.openInvestorHistory();
        }
    }

    // botao "novo investidor": abre de novo a tela de cadastro limpa
    public void startNewInvestorRegistration() {
        if (managerController != null) {
            managerController.openNewInvestorFlow();
        }
    }
}
