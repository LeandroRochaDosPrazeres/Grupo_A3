package service;

import dao.PortfolioDAO;
import dao.PortfolioItemDAO;
import dao.InvestorDAO;
import dao.LogDAO;
import model.LogEntry;
import model.Portfolio;
import model.PortfolioItem;
import java.util.List;

public class PortfolioService {
    private final PortfolioDAO portfolioDAO;
    private final PortfolioItemDAO portfolioItemDAO;
    private final InvestorDAO investorDAO;
    private final LogDAO logDAO;

    public PortfolioService(PortfolioDAO portfolioDAO, PortfolioItemDAO portfolioItemDAO,
                            InvestorDAO investorDAO, LogDAO logDAO) {
        this.portfolioDAO = portfolioDAO;
        this.portfolioItemDAO = portfolioItemDAO;
        this.investorDAO = investorDAO;
        this.logDAO = logDAO;
    }


    public Portfolio createPortfolio(Portfolio portfolio) {
        Portfolio created = portfolioDAO.create(portfolio);

        LogEntry log = new LogEntry();
        log.setAction("CREATE_PORTFOLIO");
        log.setDetails(String.format("Carteira '%s' (ID: %d) criada para o investidor (ID: %d)",
                created.getName(), created.getId(), created.getInvestorId()));
        try { logDAO.create(log); } catch (Exception ignored) { }

        return created;
    }

    public void addItem(PortfolioItem item) {
        portfolioItemDAO.create(item);

        LogEntry log = new LogEntry();
        log.setAction("ADD_ITEM");
        log.setDetails(String.format("Ativo ID %d adicionado à carteira ID %d",
                item.getAssetId(), item.getPortfolioId()));
        try { logDAO.create(log); } catch (Exception ignored) { }
    }


    public void removeItem(Long portfolioItemId) {
        portfolioItemDAO.delete(portfolioItemId);

        LogEntry log = new LogEntry();
        log.setAction("REMOVE_ITEM");
        log.setDetails(String.format("Item ID %d removido do portfólio", portfolioItemId));
        try { logDAO.create(log); } catch (Exception ignored) { }
    }

    public Portfolio loadPortfolioWithItems(Long portfolioId) {
        Portfolio portfolio = portfolioDAO.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfólio não encontrado"));
        List<PortfolioItem> items = portfolioItemDAO.findByPortfolio(portfolioId);
        for (PortfolioItem item : items) {
            portfolio.addItem(item);
        }
        // tambem carrega o investidor dono da carteira (precisamos do perfil de
        // risco dele pra otimizacao funcionar direito)
        if (portfolio.getInvestorId() != null) {
            investorDAO.findById(portfolio.getInvestorId())
                    .ifPresent(portfolio::setInvestor);
        }
        return portfolio;
    }
}