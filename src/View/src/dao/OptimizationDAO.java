package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Optimization;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class OptimizationDAO extends SupabaseClient {

    private static final String TABLE = "/optimizations";

    public OptimizationDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public Optimization create(Optimization opt) {
        JsonObject json = new JsonObject();
        json.addProperty("portfolio_id", opt.getPortfolioId());
        json.addProperty("run_by_user_id", opt.getRunByUserId());
        if (opt.getExpectedReturn() != null) {
            json.addProperty("expected_return", opt.getExpectedReturn());
        }
        if (opt.getTotalRisk() != null) {
            json.addProperty("total_risk", opt.getTotalRisk());
        }

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<Optimization> findByPortfolio(Long portfolioId) {
        return toList(get(TABLE + "?portfolio_id=eq." + portfolioId + "&select=*"));
    }

    private List<Optimization> toList(JsonArray arr) {
        List<Optimization> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private Optimization fromJson(JsonObject obj) {
        Optimization opt = new Optimization();
        opt.setId(obj.get("id").getAsLong());
        opt.setPortfolioId(obj.get("portfolio_id").getAsLong());
        opt.setRunByUserId(obj.get("run_by_user_id").getAsLong());

        if (obj.has("expected_return") && !obj.get("expected_return").isJsonNull()) {
            opt.setExpectedReturn(obj.get("expected_return").getAsBigDecimal());
        }
        if (obj.has("total_risk") && !obj.get("total_risk").isJsonNull()) {
            opt.setTotalRisk(obj.get("total_risk").getAsBigDecimal());
        }
        if (obj.has("created_at") && !obj.get("created_at").isJsonNull()) {
            opt.setCreatedAt(OffsetDateTime.parse(obj.get("created_at").getAsString()));
        }

        return opt;
    }
}