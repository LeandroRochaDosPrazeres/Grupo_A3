package model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class PortfolioPrice {

    private LocalDate date;       
    private String ticker;        
    private BigDecimal price;     

    

    public PortfolioPrice() {
    }

    public PortfolioPrice(LocalDate date, String ticker, BigDecimal price) {
        this.date = date;
        this.ticker = ticker;
        this.price = price;
    }

    

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    

    @Override
    public String toString() {
        return "PortfolioPrice{" +
                "date=" + date +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                '}';
    }
}