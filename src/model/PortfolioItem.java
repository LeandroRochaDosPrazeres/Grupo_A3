// PortfolioItem.java
package model;

import java.math.BigDecimal;

public class PortfolioItem {

    private Long id;
    private Long portfolioId;
    private Long assetId;
    private BigDecimal quantity;
    private BigDecimal averagePrice;
    private BigDecimal suggestedPercentage;
    private Portfolio portfolio;
    private Asset asset;

    public PortfolioItem() {
    }

    public PortfolioItem(Long id, Long portfolioId, Long assetId,
                         BigDecimal quantity, BigDecimal averagePrice, BigDecimal suggestedPercentage) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.assetId = assetId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.suggestedPercentage = suggestedPercentage;
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

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getSuggestedPercentage() {
        return suggestedPercentage;
    }

    public void setSuggestedPercentage(BigDecimal suggestedPercentage) {
        this.suggestedPercentage = suggestedPercentage;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getPositionValue() {
        if (quantity == null || averagePrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(averagePrice);
    }
}