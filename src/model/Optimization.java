package model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Optimization {

    private Long id;
    private Long portfolioId;
    private Long runByUserId;
    private BigDecimal expectedReturn;
    private BigDecimal totalRisk;
    private OffsetDateTime createdAt;

    public Optimization() {
    }

    public Optimization(Long id, Long portfolioId, Long runByUserId,
                        BigDecimal expectedReturn, BigDecimal totalRisk, OffsetDateTime createdAt) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.runByUserId = runByUserId;
        this.expectedReturn = expectedReturn;
        this.totalRisk = totalRisk;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getRunByUserId() {
        return runByUserId;
    }

    public void setRunByUserId(Long runByUserId) {
        this.runByUserId = runByUserId;
    }

    public BigDecimal getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(BigDecimal expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public BigDecimal getTotalRisk() {
        return totalRisk;
    }

    public void setTotalRisk(BigDecimal totalRisk) {
        this.totalRisk = totalRisk;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Optimization{" +
                "id=" + id +
                ", portfolioId=" + portfolioId +
                ", runByUserId=" + runByUserId +
                ", expectedReturn=" + expectedReturn +
                ", totalRisk=" + totalRisk +
                ", createdAt=" + createdAt +
                '}';
    }
}