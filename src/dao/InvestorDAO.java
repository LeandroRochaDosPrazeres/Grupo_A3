package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Investor;
import model.RiskProfile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvestorDAO extends SupabaseClient {

    private static final String TABLE = "/investors";

    public InvestorDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public Investor create(Investor investor) {
        JsonObject json = new JsonObject();
        json.addProperty("name", investor.getName());
        json.addProperty("document_id", investor.getDocumentId());
        json.addProperty("risk_profile", investor.getRiskProfile().name());
        json.addProperty("responsible_manager_id", investor.getResponsibleManagerId());

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<Investor> findAll() {
        return toList(get(TABLE + "?select=*"));
    }

    public List<Investor> findByManager(Long managerId) {
        return toList(get(TABLE + "?responsible_manager_id=eq." + managerId + "&select=*"));
    }

    public Optional<Investor> findById(Long id) {
        JsonArray arr = get(TABLE + "?id=eq." + id + "&select=*");
        if (arr.isEmpty()) return Optional.empty();
        return Optional.of(fromJson(arr.get(0).getAsJsonObject()));
    }

    public void delete(Long id) {
        deleteById(TABLE, id);
    }

    private List<Investor> toList(JsonArray arr) {
        List<Investor> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private Investor fromJson(JsonObject obj) {
        Investor investor = new Investor();
        investor.setId(obj.get("id").getAsLong());
        investor.setName(obj.get("name").getAsString());
        investor.setDocumentId(obj.get("document_id").getAsString());
        investor.setRiskProfile(RiskProfile.valueOf(obj.get("risk_profile").getAsString()));
        investor.setResponsibleManagerId(obj.get("responsible_manager_id").getAsLong());

        if (obj.has("created_at") && !obj.get("created_at").isJsonNull()) {
            investor.setCreatedAt(OffsetDateTime.parse(obj.get("created_at").getAsString()));
        }

        return investor;
    }
}