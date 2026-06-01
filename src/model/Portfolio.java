package model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private Long id;
    private Long investorId;
    private String name;
    private BigDecimal desiredRiskLevel;
    private OffsetDateTime createdAt;
    private Investor investor;
    private List<PortfolioItem> items = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(Long id, Long investorId, String name,
                     BigDecimal desiredRiskLevel, OffsetDateTime createdAt) {
        this.id = id;
        this.investorId = investorId;
        this.name = name;
        this.desiredRiskLevel = desiredRiskLevel;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDesiredRiskLevel() {
        return desiredRiskLevel;
    }

    public void setDesiredRiskLevel(BigDecimal desiredRiskLevel) {
        this.desiredRiskLevel = desiredRiskLevel;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public List<PortfolioItem> getItems() {
        return items;
    }

    public void setItems(List<PortfolioItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalValue() {
        BigDecimal total = BigDecimal.ZERO;
        for (PortfolioItem item : items) {
            if (item != null && item.getPositionValue() != null) {
                total = total.add(item.getPositionValue());
            }
        }
        return total;
    }

    public void addItem(PortfolioItem item) {
        this.items.add(item);
    }

    public void removeItem(PortfolioItem item) {
        this.items.remove(item);
    }
}