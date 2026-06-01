package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Asset;
import model.PortfolioItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PortfolioItemDAO extends SupabaseClient {

    private static final String TABLE = "/portfolio_items";

    public PortfolioItemDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public PortfolioItem create(PortfolioItem item) {
        JsonObject json = new JsonObject();
        json.addProperty("portfolio_id", item.getPortfolioId());
        json.addProperty("asset_id", item.getAssetId());
        json.addProperty("quantity", item.getQuantity());
        json.addProperty("average_price", item.getAveragePrice());
        if (item.getSuggestedPercentage() != null) {
            json.addProperty("suggested_percentage", item.getSuggestedPercentage());
        }

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<PortfolioItem> findByPortfolio(Long portfolioId) {
        String query = TABLE
                + "?portfolio_id=eq." + portfolioId
                + "&select=*,assets!inner(id,ticker,name,category,base_risk)";
        return toList(get(query));
    }

    public void updateSuggestedPercentage(Long id, BigDecimal percentage) {
        JsonObject json = new JsonObject();
        json.addProperty("suggested_percentage", percentage);
        patch(TABLE + "?id=eq." + id, json);
    }

    private List<PortfolioItem> toList(JsonArray arr) {
        List<PortfolioItem> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private PortfolioItem fromJson(JsonObject obj) {
        PortfolioItem item = new PortfolioItem();
        item.setId(obj.get("id").getAsLong());
        item.setPortfolioId(obj.get("portfolio_id").getAsLong());
        item.setAssetId(obj.get("asset_id").getAsLong());
        item.setQuantity(obj.get("quantity").getAsBigDecimal());
        item.setAveragePrice(obj.get("average_price").getAsBigDecimal());

        if (obj.has("suggested_percentage") && !obj.get("suggested_percentage").isJsonNull()) {
            item.setSuggestedPercentage(obj.get("suggested_percentage").getAsBigDecimal());
        }

        if (obj.has("assets") && !obj.get("assets").isJsonNull()) {
            JsonObject assetObj = obj.get("assets").getAsJsonObject();
            Asset asset = new Asset();

            if (assetObj.has("id") && !assetObj.get("id").isJsonNull()) {
                asset.setId(assetObj.get("id").getAsLong());
            } else {
                asset.setId(item.getAssetId());
            }

            asset.setTicker(assetObj.get("ticker").getAsString());
            asset.setName(assetObj.get("name").getAsString());
            asset.setCategory(assetObj.get("category").getAsString());

            if (assetObj.has("base_risk") && !assetObj.get("base_risk").isJsonNull()) {
                asset.setBaseRisk(assetObj.get("base_risk").getAsBigDecimal());
            }

            item.setAsset(asset);
        }

        return item;
    }
}