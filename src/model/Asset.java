package model;


import java.math.BigDecimal;

public class Asset {
    String ticker, name, category;
    Long id;
    BigDecimal baseRisk;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBaseRisk() {
        return baseRisk;
    }

    public void setBaseRisk(BigDecimal baseRisk) {
        this.baseRisk = baseRisk;
    }

    public String getDisplayName() {
        return ticker+" - "+name;
    }
    
    
}
