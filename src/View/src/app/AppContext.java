package app;

import dao.AssetDAO;
import dao.InvestorDAO;
import dao.LogDAO;
import dao.OptimizationDAO;
import dao.PortfolioDAO;
import dao.PortfolioItemDAO;
import dao.PortfolioPriceDAO;
import dao.UserDAO;
import service.AuthService;
import service.InvestorService;
import service.PortfolioOptimizerService;
import service.PortfolioService;

/**
 * "Caixa de ferramentas" da aplicacao (a montagem das pecas).
 *
 * Cria uma vez so cada DAO e cada Service e deixa todo mundo pronto pra usar.
 * As credenciais do Supabase (SUPABASE_URL e SUPABASE_API_KEY) sao lidas
 * sozinhas do arquivo .env pelo proprio SupabaseClient, por isso passamos
 * null/null aqui nos DAOs.
 *
 * @author leandrorocha
 */
public class AppContext {

    // --- DAOs (acesso ao banco) ---
    private final UserDAO userDAO;
    private final InvestorDAO investorDAO;
    private final AssetDAO assetDAO;
    private final PortfolioDAO portfolioDAO;
    private final PortfolioItemDAO portfolioItemDAO;
    private final OptimizationDAO optimizationDAO;
    private final LogDAO logDAO;
    private final PortfolioPriceDAO portfolioPriceDAO;

    // --- Services (regras de negocio) ---
    private final AuthService authService;
    private final InvestorService investorService;
    private final PortfolioService portfolioService;
    private final PortfolioOptimizerService optimizerService;

    public AppContext() {
        // os DAOs recebem null/null e pegam as credenciais do .env sozinhos
        this(new UserDAO(null, null),
             new InvestorDAO(null, null),
             new AssetDAO(null, null),
             new PortfolioDAO(null, null),
             new PortfolioItemDAO(null, null),
             new OptimizationDAO(null, null),
             new LogDAO(null, null),
             new PortfolioPriceDAO(null, null));
    }

    /**
     * Construtor que recebe os DAOs prontos.
     * Serve pra trocar os DAOs por versoes de teste (em memoria), sem precisar
     * de banco/internet. Em producao usa-se o construtor sem argumentos.
     */
    public AppContext(UserDAO userDAO, InvestorDAO investorDAO, AssetDAO assetDAO,
                      PortfolioDAO portfolioDAO, PortfolioItemDAO portfolioItemDAO,
                      OptimizationDAO optimizationDAO, LogDAO logDAO,
                      PortfolioPriceDAO portfolioPriceDAO) {
        this.userDAO = userDAO;
        this.investorDAO = investorDAO;
        this.assetDAO = assetDAO;
        this.portfolioDAO = portfolioDAO;
        this.portfolioItemDAO = portfolioItemDAO;
        this.optimizationDAO = optimizationDAO;
        this.logDAO = logDAO;
        this.portfolioPriceDAO = portfolioPriceDAO;

        // os Services recebem os DAOs que precisam (injecao de dependencia na mao)
        this.authService = new AuthService(userDAO, logDAO);
        this.investorService = new InvestorService(investorDAO, logDAO);
        this.portfolioService = new PortfolioService(portfolioDAO, portfolioItemDAO, investorDAO, logDAO);
        this.optimizerService = new PortfolioOptimizerService(
                portfolioService, portfolioPriceDAO, optimizationDAO, portfolioItemDAO);
    }

    // --- getters dos DAOs ---
    public UserDAO getUserDAO() { return userDAO; }
    public InvestorDAO getInvestorDAO() { return investorDAO; }
    public AssetDAO getAssetDAO() { return assetDAO; }
    public PortfolioDAO getPortfolioDAO() { return portfolioDAO; }
    public PortfolioItemDAO getPortfolioItemDAO() { return portfolioItemDAO; }
    public OptimizationDAO getOptimizationDAO() { return optimizationDAO; }
    public LogDAO getLogDAO() { return logDAO; }
    public PortfolioPriceDAO getPortfolioPriceDAO() { return portfolioPriceDAO; }

    // --- getters dos Services ---
    public AuthService getAuthService() { return authService; }
    public InvestorService getInvestorService() { return investorService; }
    public PortfolioService getPortfolioService() { return portfolioService; }
    public PortfolioOptimizerService getOptimizerService() { return optimizerService; }
}
