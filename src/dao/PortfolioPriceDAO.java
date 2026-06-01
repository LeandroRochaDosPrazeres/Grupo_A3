package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.PortfolioPrice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioPriceDAO extends SupabaseClient {

    private static final String TABLE = "/portfolio_prices";

    public PortfolioPriceDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public void upsertPrice(PortfolioPrice price) {
        JsonObject json = new JsonObject();
        json.addProperty("date", price.getDate().toString());
        json.addProperty("ticker", price.getTicker());
        json.addProperty("price", price.getPrice());
        upsert(TABLE, json);
    }

    public List<PortfolioPrice> findByTicker(String ticker) {
        return toList(get(TABLE + "?ticker=eq." + ticker + "&select=*"));
    }

    private List<PortfolioPrice> toList(JsonArray arr) {
        List<PortfolioPrice> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private PortfolioPrice fromJson(JsonObject obj) {
        PortfolioPrice price = new PortfolioPrice();
        price.setDate(LocalDate.parse(obj.get("date").getAsString()));
        price.setTicker(obj.get("ticker").getAsString());
        price.setPrice(obj.get("price").getAsBigDecimal());
        return price;
    }
}