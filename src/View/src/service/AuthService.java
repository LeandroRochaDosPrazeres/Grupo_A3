package service;

import dao.UserDAO;
import dao.LogDAO;
import model.LogEntry;
import model.User;

public class AuthService {
    private final UserDAO userDAO;
    private final LogDAO logDAO;

    public AuthService(UserDAO userDAO, LogDAO logDAO) {
        this.userDAO = userDAO;
        this.logDAO = logDAO;
    }

    public User login(String email, String plainPassword) {
        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isActive()) {
            throw new RuntimeException("Usuário inativo");
        }

        if (!user.checkPassword(plainPassword)) {
            LogEntry failLog = new LogEntry();
            failLog.setUserId(user.getId());
            failLog.setAction("LOGIN_FAILED");
            failLog.setDetails("Tentativa de login com senha incorreta para: " + email);
            logDAO.create(failLog);
            throw new RuntimeException("Senha incorreta");
        }

        LogEntry successLog = new LogEntry();
        successLog.setUserId(user.getId());
        successLog.setAction("LOGIN_SUCCESS");
        successLog.setDetails("Login bem-sucedido para: " + email);
        logDAO.create(successLog);

        return user;
    }
}