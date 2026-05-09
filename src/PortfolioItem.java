package src;


import java.math.BigDecimal;

public class PortfolioItem {
    Long id, portfolioId, assetId;
    BigDecimal quantity, averagePrice, suggestedPercentage;
    Portfolio portfolio;
    Asset asset;

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
    
    public BigDecimal getPositionValue(){
        return quantity.multiply(averagePrice);
    }
}
