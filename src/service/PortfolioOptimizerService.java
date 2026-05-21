package service;

import dao.OptimizationDAO;
import dao.PortfolioItemDAO;
import dao.PortfolioPriceDAO;
import model.Optimization;
import model.Portfolio;
import model.PortfolioItem;
import model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioOptimizerService {

    private final PortfolioService portfolioService;
    private final PortfolioPriceDAO portfolioPriceDAO;
    private final OptimizationDAO optimizationDAO;
    private final PortfolioItemDAO portfolioItemDAO;

    public PortfolioOptimizerService(PortfolioService portfolioService,
                                     PortfolioPriceDAO portfolioPriceDAO,
                                     OptimizationDAO optimizationDAO,
                                     PortfolioItemDAO portfolioItemDAO) {
        this.portfolioService = portfolioService;
        this.portfolioPriceDAO = portfolioPriceDAO;
        this.optimizationDAO = optimizationDAO;
        this.portfolioItemDAO = portfolioItemDAO;
    }

    public Optimization optimizePortfolio(Long portfolioId, User currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        if (!currentUser.isManager() && !currentUser.isAdmin()) {
            throw new RuntimeException("Acesso negado: apenas gerentes e administradores podem otimizar portfólios");
        }

        Portfolio portfolio = portfolioService.loadPortfolioWithItems(portfolioId);

        List<PortfolioItem> items = portfolio.getItems();
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Portfólio sem itens para otimização");
        }

        String riskProfile = (portfolio.getInvestor() != null
                && portfolio.getInvestor().getRiskProfile() != null)
                ? portfolio.getInvestor().getRiskProfile().name()
                : "MODERATE";

        Map<Long, BigDecimal> suggestedPercentages = calculateSuggestedPercentages(items, riskProfile);

        for (PortfolioItem item : items) {
            BigDecimal suggested = suggestedPercentages.get(item.getAssetId());
            if (suggested != null) {
                item.setSuggestedPercentage(suggested);
                portfolioItemDAO.updateSuggestedPercentage(item.getId(), suggested);
            }
        }

        BigDecimal expectedReturn = calculateExpectedReturn(items);
        BigDecimal totalRisk = calculateTotalRisk(riskProfile);

        Optimization optimization = new Optimization();
        optimization.setPortfolioId(portfolioId);
        optimization.setRunByUserId(currentUser.getId());
        optimization.setExpectedReturn(expectedReturn);
        optimization.setTotalRisk(totalRisk);

        return optimizationDAO.create(optimization);
    }

    private Map<Long, BigDecimal> calculateSuggestedPercentages(List<PortfolioItem> items, String riskProfile) {
        Map<Long, BigDecimal> result = new HashMap<>();
        int n = items.size();

        switch (riskProfile.toUpperCase()) {
            case "CONSERVATIVE":
                if (n == 1) {
                    result.put(items.get(0).getAssetId(), BigDecimal.ONE);
                    break;
                }
                result.put(items.get(0).getAssetId(), BigDecimal.valueOf(0.60));
                BigDecimal remaining = BigDecimal.valueOf(0.40)
                        .divide(BigDecimal.valueOf(n - 1), 4, RoundingMode.HALF_UP);
                for (int i = 1; i < n; i++) {
                    result.put(items.get(i).getAssetId(), remaining);
                }
                break;

            case "AGGRESSIVE":
                if (n == 1) {
                    result.put(items.get(0).getAssetId(), BigDecimal.ONE);
                    break;
                }
                BigDecimal rest = BigDecimal.valueOf(0.40)
                        .divide(BigDecimal.valueOf(n - 1), 4, RoundingMode.HALF_UP);
                for (int i = 0; i < n - 1; i++) {
                    result.put(items.get(i).getAssetId(), rest);
                }
                result.put(items.get(n - 1).getAssetId(), BigDecimal.valueOf(0.60));
                break;

            case "MODERATE":
            default:
                BigDecimal equal = BigDecimal.valueOf(1.0 / n).setScale(4, RoundingMode.HALF_UP);
                for (PortfolioItem item : items) {
                    result.put(item.getAssetId(), equal);
                }
                break;
        }
        return result;
    }


    private BigDecimal calculateExpectedReturn(List<PortfolioItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (PortfolioItem item : items) {
            if (item.getSuggestedPercentage() != null) {
                total = total.add(item.getSuggestedPercentage());
            }
        }
        return total.divide(BigDecimal.valueOf(items.size()), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalRisk(String riskProfile) {
        switch (riskProfile.toUpperCase()) {
            case "CONSERVATIVE": return BigDecimal.valueOf(0.20);
            case "AGGRESSIVE":   return BigDecimal.valueOf(0.80);
            default:             return BigDecimal.valueOf(0.50);
        }
    }
}
