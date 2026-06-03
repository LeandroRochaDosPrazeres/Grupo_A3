package test;

import model.Investor;
import model.LogEntry;
import model.Portfolio;
import model.PortfolioItem;
import model.RiskProfile;
import model.User;
import model.UserRole;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceLayerTest {

    public static void main(String[] args) {
        System.out.println("Iniciando ServiceLayerTest...");
        executarTesteDaCamadaService();
        System.out.println("ServiceLayerTest finalizado com sucesso.");
    }

    private static void executarTesteDaCamadaService() {
        FakeAuthService authService = new FakeAuthService();
        FakeInvestorService investorService = new FakeInvestorService();
        FakePortfolioService portfolioService = new FakePortfolioService();
        FakeLogCollector logCollector = new FakeLogCollector();

        User manager = new User();
        manager.setId(10L);
        manager.setEmail("manager@email.com");
        manager.setPasswordHash("123");
        manager.setRole(UserRole.MANAGER);
        manager.setActive(true);

        authService.user = manager;

        User logged = authService.login("manager@email.com", "123");
        check(logged != null, "Login deveria retornar usuário");
        checkEquals(10L, logged.getId(), "ID do login inválido");
        logCollector.create(new LogEntry(logged.getId(), "LOGIN", "Login realizado"));

        Investor investor = new Investor();
        investor.setName("Maya");
        investor.setDocumentId("12345678900");
        investor.setRiskProfile(RiskProfile.MODERATE);

        Investor createdInvestor = investorService.createInvestor(investor, manager);
        check(createdInvestor != null, "Investor criado não pode ser nulo");
        checkEquals(10L, createdInvestor.getResponsibleManagerId(), "ManagerId do investor inválido");
        logCollector.create(new LogEntry(manager.getId(), "CREATE_INVESTOR", "Investor criado"));

        Portfolio portfolio = new Portfolio();
        portfolio.setId(100L);
        portfolio.setInvestorId(createdInvestor.getId());
        portfolio.setName("Carteira Teste");

        PortfolioItem item = new PortfolioItem();
        item.setId(1000L);
        item.setPortfolioId(100L);
        item.setAssetId(1L);
        item.setQuantity(new BigDecimal("5"));
        item.setAveragePrice(new BigDecimal("20.00"));

        portfolioService.portfolio = portfolio;
        portfolioService.items.add(item);

        Portfolio loaded = portfolioService.loadPortfolioWithItems(100L);

        check(loaded != null, "Portfolio carregado não pode ser nulo");
        checkEquals(1, loaded.getItems().size(), "Quantidade de itens inválida");
        checkBigDecimal(new BigDecimal("100.00"), loaded.getTotalValue(), "Valor total do portfolio inválido");
        check(logCollector.logsCriados > 0, "Deveria existir pelo menos um log");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    private static void checkEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new RuntimeException(message + " | esperado=" + expected + ", obtido=" + actual);
    }

    private static void checkBigDecimal(BigDecimal expected, BigDecimal actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && actual != null && expected.compareTo(actual) == 0) return;
        throw new RuntimeException(message + " | esperado=" + expected + ", obtido=" + actual);
    }

    static class FakeAuthService {
        User user;

        public User login(String email, String password) {
            if (user != null && email.equals(user.getEmail()) && user.checkPassword(password)) {
                return user;
            }
            return null;
        }
    }

    static class FakeInvestorService {
        public Investor createInvestor(Investor investor, User manager) {
            investor.setId(20L);
            investor.setResponsibleManagerId(manager.getId());
            return investor;
        }
    }

    static class FakePortfolioService {
        Portfolio portfolio;
        List<PortfolioItem> items = new ArrayList<>();

        public Portfolio loadPortfolioWithItems(Long id) {
            if (portfolio == null) {
                return null;
            }
            portfolio.setItems(items);
            return portfolio;
        }
    }

    static class FakeLogCollector {
        int logsCriados = 0;

        public LogEntry create(LogEntry log) {
            logsCriados++;
            log.setId((long) logsCriados);
            return log;
        }
    }
}