package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.LogEntry;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogDAO extends SupabaseClient {

    private static final String TABLE = "/logs";

    public LogDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public LogEntry create(LogEntry log) {
        JsonObject json = new JsonObject();
        json.addProperty("user_id", log.getUserId());
        json.addProperty("action", log.getAction());
        json.addProperty("details", log.getDetails());

        JsonArray arr = post(TABLE, json);
        return fromJson(arr.get(0).getAsJsonObject());
    }

    public List<LogEntry> findRecentForUser(Long userId) {
        String query = TABLE
                + "?user_id=eq." + userId
                + "&order=created_at.desc"
                + "&limit=50";
        return toList(get(query));
    }

    private List<LogEntry> toList(JsonArray arr) {
        List<LogEntry> list = new ArrayList<>();
        for (JsonElement element : arr) {
            list.add(fromJson(element.getAsJsonObject()));
        }
        return list;
    }

    private LogEntry fromJson(JsonObject obj) {
        LogEntry log = new LogEntry();
        log.setId(obj.get("id").getAsLong());
        log.setUserId(obj.get("user_id").getAsLong());
        log.setAction(obj.get("action").getAsString());

        if (obj.has("details") && !obj.get("details").isJsonNull()) {
            log.setDetails(obj.get("details").getAsString());
        }
        if (obj.has("created_at") && !obj.get("created_at").isJsonNull()) {
            log.setCreatedAt(OffsetDateTime.parse(obj.get("created_at").getAsString()));
        }

        return log;
    }
}