package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseClient {

    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    protected final String projectUrl;
    protected final String apiKey;
    protected final HttpClient httpClient;

    public SupabaseClient() {
        this.projectUrl = getRequired("SUPABASE_URL");
        this.apiKey = getRequired("SUPABASE_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

    public SupabaseClient(String projectUrl, String apiKey) {
        this.projectUrl = (projectUrl != null && !projectUrl.isBlank())
                ? projectUrl
                : getRequired("SUPABASE_URL");

        this.apiKey = (apiKey != null && !apiKey.isBlank())
                ? apiKey
                : getRequired("SUPABASE_API_KEY");

        this.httpClient = HttpClient.newHttpClient();
    }

    private String getRequired(String key) {
        String value = DOTENV.get(key);

        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variável obrigatória ausente: " + key);
        }

        return value;
    }

    protected JsonArray post(String tablePath, JsonObject body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(projectUrl + tablePath))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return JsonParser.parseString(response.body()).getAsJsonArray();
            }

            throw new RuntimeException("Erro POST: " + response.statusCode() + " - " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Falha no POST " + tablePath, e);
        }
    }

    protected JsonArray get(String tablePathWithQuery) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(projectUrl + tablePathWithQuery))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return JsonParser.parseString(response.body()).getAsJsonArray();
            }

            throw new RuntimeException("Erro GET: " + response.statusCode() + " - " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Falha no GET " + tablePathWithQuery, e);
        }
    }

    protected void deleteById(String tablePath, Long id) {
        try {
            String url = projectUrl + tablePath + "?id=eq." + id;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (!(response.statusCode() >= 200 && response.statusCode() < 300)) {
                throw new RuntimeException("Erro DELETE: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha no DELETE " + tablePath + " id=" + id, e);
        }
    }

    protected JsonArray patch(String tablePathWithFilter, JsonObject body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(projectUrl + tablePathWithFilter))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return JsonParser.parseString(response.body()).getAsJsonArray();
            }

            throw new RuntimeException("Erro PATCH: " + response.statusCode() + " - " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Falha no PATCH " + tablePathWithFilter, e);
        }
    }

    protected void upsert(String tablePath, JsonObject body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(projectUrl + tablePath))
                    .header("apikey", apiKey)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "resolution=merge-duplicates")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (!(response.statusCode() >= 200 && response.statusCode() < 300)) {
                throw new RuntimeException("Erro UPSERT: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha no UPSERT " + tablePath, e);
        }
    }
}