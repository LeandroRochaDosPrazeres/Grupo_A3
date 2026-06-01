package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Portfolio;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortfolioDAO extends SupabaseClient {

    private static final String TABLE = "/portfolios";

    public PortfolioDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public Portfolio create(Portfolio portfolio) {
        JsonObject json = new JsonObject();
        json.addProperty("investor_id", portfolio.getInvestorId());
        json.addProperty("name", portfolio.getName());
        if (portfolio.getDesiredRiskLevel() != null) {
            json.addProperty("desired_risk_level", portfolio.getDesiredRiskLevel());
        }

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<Portfolio> findByInvestor(Long investorId) {
        return toList(get(TABLE + "?investor_id=eq." + investorId + "&select=*"));
    }

    public Optional<Portfolio> findById(Long id) {
        JsonArray arr = get(TABLE + "?id=eq." + id + "&select=*");
        if (arr.isEmpty()) return Optional.empty();
        return Optional.of(fromJson(arr.get(0).getAsJsonObject()));
    }

    private List<Portfolio> toList(JsonArray arr) {
        List<Portfolio> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private Portfolio fromJson(JsonObject obj) {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(obj.get("id").getAsLong());
        portfolio.setInvestorId(obj.get("investor_id").getAsLong());
        portfolio.setName(obj.get("name").getAsString());

        if (obj.has("desired_risk_level") && !obj.get("desired_risk_level").isJsonNull()) {
            portfolio.setDesiredRiskLevel(obj.get("desired_risk_level").getAsBigDecimal());
        }
        if (obj.has("created_at") && !obj.get("created_at").isJsonNull()) {
            portfolio.setCreatedAt(OffsetDateTime.parse(obj.get("created_at").getAsString()));
        }

        return portfolio;
    }
}