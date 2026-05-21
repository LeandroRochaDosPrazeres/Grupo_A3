package service;

import dao.PortfolioDAO;
import dao.PortfolioItemDAO;
import dao.LogDAO;
import model.LogEntry;
import model.Portfolio;
import model.PortfolioItem;
import java.util.List;

public class PortfolioService {
    private final PortfolioDAO portfolioDAO;
    private final PortfolioItemDAO portfolioItemDAO;
    private final LogDAO logDAO;

    public PortfolioService(PortfolioDAO portfolioDAO, PortfolioItemDAO portfolioItemDAO, LogDAO logDAO) {
        this.portfolioDAO = portfolioDAO;
        this.portfolioItemDAO = portfolioItemDAO;
        this.logDAO = logDAO;
    }


    public Portfolio createPortfolio(Portfolio portfolio) {
        Portfolio created = portfolioDAO.create(portfolio);

        LogEntry log = new LogEntry();
        log.setAction("CREATE_PORTFOLIO");
        log.setDetails(String.format("Carteira '%s' (ID: %d) criada para o investidor (ID: %d)",
                created.getName(), created.getId(), created.getInvestorId()));
        logDAO.create(log);

        return created;
    }

    public void addItem(PortfolioItem item) {
        portfolioItemDAO.create(item);

        LogEntry log = new LogEntry();
        log.setAction("ADD_ITEM");
        log.setDetails(String.format("Ativo ID %d adicionado à carteira ID %d",
                item.getAssetId(), item.getPortfolioId()));
        logDAO.create(log);
    }


    public void removeItem(Long portfolioItemId) {
        portfolioItemDAO.delete(portfolioItemId);

        LogEntry log = new LogEntry();
        log.setAction("REMOVE_ITEM");
        log.setDetails(String.format("Item ID %d removido do portfólio", portfolioItemId));
        logDAO.create(log);
    }

    public Portfolio loadPortfolioWithItems(Long portfolioId) {
        Portfolio portfolio = portfolioDAO.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfólio não encontrado"));
        List<PortfolioItem> items = portfolioItemDAO.findByPortfolio(portfolioId);
        for (PortfolioItem item : items) {
            portfolio.addItem(item);
        }
        return portfolio;
    }
}