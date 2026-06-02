package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssetDAO extends SupabaseClient {

    private static final String TABLE = "/assets";

    public AssetDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public Asset create(Asset asset) {
        JsonObject json = new JsonObject();
        json.addProperty("ticker", asset.getTicker());
        json.addProperty("name", asset.getName());
        json.addProperty("category", asset.getCategory());
        if (asset.getBaseRisk() != null) {
            json.addProperty("base_risk", asset.getBaseRisk());
        }

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<Asset> findAll() {
        return toList(get(TABLE + "?select=*"));
    }

    public Optional<Asset> findByTicker(String ticker) {
        JsonArray arr = get(TABLE + "?ticker=eq." + ticker + "&select=*");
        if (arr.isEmpty()) return Optional.empty();
        return Optional.of(fromJson(arr.get(0).getAsJsonObject()));
    }

    private List<Asset> toList(JsonArray arr) {
        List<Asset> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private Asset fromJson(JsonObject obj) {
        Asset asset = new Asset();
        asset.setId(obj.get("id").getAsLong());
        asset.setTicker(obj.get("ticker").getAsString());
        asset.setName(obj.get("name").getAsString());
        asset.setCategory(obj.get("category").getAsString());

        if (obj.has("base_risk") && !obj.get("base_risk").isJsonNull()) {
            asset.setBaseRisk(obj.get("base_risk").getAsBigDecimal());
        }

        return asset;
    }
}