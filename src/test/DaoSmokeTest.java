package test;

import dao.InvestorDAO;
import dao.LogDAO;
import dao.PortfolioDAO;
import dao.UserDAO;
import model.Investor;
import model.LogEntry;
import model.Portfolio;
import model.RiskProfile;
import model.User;
import model.UserRole;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoSmokeTest {

    public static void main(String[] args) {
        System.out.println("Iniciando DaoSmokeTest...");
        executarSmokeTest();
        System.out.println("DaoSmokeTest finalizado com sucesso.");
    }

    private static void executarSmokeTest() {
        UserDAO userDAO = new UserDAO();
        InvestorDAO investorDAO = new InvestorDAO();
        PortfolioDAO portfolioDAO = new PortfolioDAO();
        LogDAO logDAO = new LogDAO();

        Long createdUserId = null;
        Long createdInvestorId = null;
        Long createdPortfolioId = null;
        Long createdLogId = null;

        String suffix = String.valueOf(System.currentTimeMillis());

        try {
            User user = new User();
            user.setName("Smoke Manager " + suffix);
            user.setEmail("smoke.manager." + suffix + "@test.com");
            user.setPasswordHash("123");
            user.setRole(UserRole.MANAGER);
            user.setActive(true);
            user.setManagerCode("MGR-" + suffix);

            User createdUser = userDAO.create(user);
            createdUserId = createdUser.getId();
            check(createdUserId != null, "Falha ao criar user");

            Optional<User> foundUser = userDAO.findById(createdUserId);
            check(foundUser.isPresent(), "User criado não encontrado");
            checkEquals(user.getEmail(), foundUser.get().getEmail(), "Email do user diferente");

            Investor investor = new Investor();
            investor.setName("Smoke Investor " + suffix);
            investor.setDocumentId("DOC-" + suffix);
            investor.setRiskProfile(RiskProfile.MODERATE);
            investor.setResponsibleManagerId(createdUserId);

            Investor createdInvestor = investorDAO.create(investor);
            createdInvestorId = createdInvestor.getId();
            check(createdInvestorId != null, "Falha ao criar investor");

            Optional<Investor> foundInvestor = investorDAO.findById(createdInvestorId);
            check(foundInvestor.isPresent(), "Investor criado não encontrado");
            checkEquals(createdUserId, foundInvestor.get().getResponsibleManagerId(), "Manager do investor diferente");

            Portfolio portfolio = new Portfolio();
            portfolio.setInvestorId(createdInvestorId);
            portfolio.setName("Smoke Portfolio " + suffix);
            portfolio.setDesiredRiskLevel(new BigDecimal("0.50"));

            Portfolio createdPortfolio = portfolioDAO.create(portfolio);
            createdPortfolioId = createdPortfolio.getId();
            check(createdPortfolioId != null, "Falha ao criar portfolio");

            Optional<Portfolio> foundPortfolio = portfolioDAO.findById(createdPortfolioId);
            check(foundPortfolio.isPresent(), "Portfolio criado não encontrado");
            checkEquals(createdInvestorId, foundPortfolio.get().getInvestorId(), "Investor do portfolio diferente");

            LogEntry log = new LogEntry();
            log.setUserId(createdUserId);
            log.setAction("SMOKE_TEST");
            log.setDetails("Teste automatizado DAO " + suffix);

            LogEntry createdLog = logDAO.create(log);
            createdLogId = createdLog.getId();
            check(createdLogId != null, "Falha ao criar log");

            List logs = logDAO.findRecentForUser(createdUserId);
            check(logs != null && !logs.isEmpty(), "Logs recentes não encontrados");

        } finally {
            if (createdLogId != null) {
                try {
                    logDAO.delete(createdLogId);
                } catch (Exception e) {
                    System.out.println("Aviso ao excluir log: " + e.getMessage());
                }
            }

            if (createdPortfolioId != null) {
                try {
                    portfolioDAO.delete(createdPortfolioId);
                } catch (Exception e) {
                    System.out.println("Aviso ao excluir portfolio: " + e.getMessage());
                }
            }

            if (createdInvestorId != null) {
                try {
                    investorDAO.delete(createdInvestorId);
                } catch (Exception e) {
                    System.out.println("Aviso ao excluir investor: " + e.getMessage());
                }
            }

            if (createdUserId != null) {
                try {
                    userDAO.delete(createdUserId);
                } catch (Exception e) {
                    System.out.println("Aviso ao excluir user: " + e.getMessage());
                }
            }
        }
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
}