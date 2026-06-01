package dao;

import model.User;
import model.UserRole;

public class TesteDAO {

    public static void main(String[] args) {
        String projectUrl = "https://SEU-PROJETO.supabase.co/rest/v1";
        String apiKey = "SUA_CHAVE";

        UserDAO userDAO = new UserDAO(projectUrl, apiKey);

        User user = new User();
        user.setName("Teste DAO");
        user.setEmail("teste+" + System.currentTimeMillis() + "@email.com");
        user.setPasswordHash("hash123");
        user.setRole(UserRole.MANAGER);
        user.setActive(true);

        User criado = userDAO.create(user);
        System.out.println("Criado: " + criado.getId() + " - " + criado.getEmail());
    }
}