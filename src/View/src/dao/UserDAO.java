package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.User;
import model.UserRole;

import java.time.OffsetDateTime;
import java.util.Optional;

public class UserDAO extends SupabaseClient {

    private static final String TABLE = "/users";

    public UserDAO(String projectUrl, String apiKey) {
        super(projectUrl, apiKey);
    }

    public User create(User user) {
        JsonObject json = new JsonObject();
        json.addProperty("name", user.getName());
        json.addProperty("email", user.getEmail());
        json.addProperty("password_hash", user.getPasswordHash());
        json.addProperty("role", user.getRole().name());
        json.addProperty("active", user.isActive());
        if (user.getManagerCode() != null) {
            json.addProperty("manager_code", user.getManagerCode());
        }

        JsonArray array = post(TABLE, json);
        return fromJson(array.get(0).getAsJsonObject());
    }

    public Optional<User> findByEmail(String email) {
        JsonArray array = get(TABLE + "?email=eq." + email + "&select=*");
        if (array.isEmpty()) return Optional.empty();
        return Optional.of(fromJson(array.get(0).getAsJsonObject()));
    }

    public java.util.List<User> findAll() {
        JsonArray array = get(TABLE + "?select=*&order=id.asc");
        java.util.List<User> users = new java.util.ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            users.add(fromJson(array.get(i).getAsJsonObject()));
        }
        return users;
    }

    public Optional<User> findById(Long id) {
        JsonArray array = get(TABLE + "?id=eq." + id + "&select=*");
        if (array.isEmpty()) return Optional.empty();
        return Optional.of(fromJson(array.get(0).getAsJsonObject()));
    }

    public void update(User user) {
        JsonObject json = new JsonObject();
        json.addProperty("name", user.getName());
        json.addProperty("email", user.getEmail());
        json.addProperty("password_hash", user.getPasswordHash());
        json.addProperty("role", user.getRole().name());
        json.addProperty("active", user.isActive());
        if (user.getManagerCode() != null) {
            json.addProperty("manager_code", user.getManagerCode());
        }

        patch(TABLE + "?id=eq." + user.getId(), json);
    }

    public void delete(Long id) {
        deleteById(TABLE, id);
    }

    private User fromJson(JsonObject obj) {
        User user = new User();
        user.setId(obj.get("id").getAsLong());
        user.setName(obj.get("name").getAsString());
        user.setEmail(obj.get("email").getAsString());
        user.setPasswordHash(obj.get("password_hash").getAsString());
        user.setRole(UserRole.valueOf(obj.get("role").getAsString()));
        user.setActive(obj.get("active").getAsBoolean());

        if (obj.has("manager_code") && !obj.get("manager_code").isJsonNull()) {
            user.setManagerCode(obj.get("manager_code").getAsString());
        }
        if (obj.has("created_at") && !obj.get("created_at").isJsonNull()) {
            user.setCreatedAt(OffsetDateTime.parse(obj.get("created_at").getAsString()));
        }

        return user;
    }
}