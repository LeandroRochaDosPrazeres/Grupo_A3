package test;

import java.math.BigDecimal;
import model.Asset;
import model.Portfolio;
import model.PortfolioItem;
import model.User;
import model.UserRole;

public class ModelLayerTest {

    public static void main(String[] args) {
        System.out.println("Iniciando ModelLayerTest...");
        testRegrasBasicasDoDominio();
        System.out.println("ModelLayerTest finalizado com sucesso.");
    }

    private static void testRegrasBasicasDoDominio() {
        User user = new User();
        user.setRole(UserRole.MANAGER);
        user.setPasswordHash("abc123");

        check(user.isManager(), "Usuário deveria ser manager");
        check(!user.isAdmin(), "Usuário não deveria ser admin");
        check(!user.isInvestor(), "Usuário não deveria ser investor");
        check(user.checkPassword("abc123"), "Senha correta deveria validar");
        check(!user.checkPassword("outra"), "Senha errada não deveria validar");

        Asset asset = new Asset();
        asset.setTicker("PETR4");
        asset.setName("Petrobras PN");
        checkEquals("PETR4 - Petrobras PN", asset.getDisplayName(), "Display name do ativo inválido");

        PortfolioItem item1 = new PortfolioItem();
        item1.setQuantity(new BigDecimal("2"));
        item1.setAveragePrice(new BigDecimal("100.00"));

        PortfolioItem item2 = new PortfolioItem();
        item2.setQuantity(new BigDecimal("3"));
        item2.setAveragePrice(new BigDecimal("50.00"));

        checkBigDecimal(new BigDecimal("200.00"), item1.getPositionValue(), "Valor da posição item1 inválido");
        checkBigDecimal(new BigDecimal("150.00"), item2.getPositionValue(), "Valor da posição item2 inválido");

        Portfolio portfolio = new Portfolio();
        portfolio.addItem(item1);
        portfolio.addItem(item2);

        checkBigDecimal(new BigDecimal("350.00"), portfolio.getTotalValue(), "Valor total do portfólio inválido");
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
}