package model;

import java.math.BigDecimal;

public class Asset {

    private Long id;
    private String ticker;
    private String name;
    private String category;
    private BigDecimal baseRisk;

    public Asset() {
    }

    public Asset(Long id, String ticker, String name, String category, BigDecimal baseRisk) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.category = category;
        this.baseRisk = baseRisk;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getBaseRisk() {
        return baseRisk;
    }

    public void setBaseRisk(BigDecimal baseRisk) {
        this.baseRisk = baseRisk;
    }

    public String getDisplayName() {
        return ticker + " - " + name;
    }
}