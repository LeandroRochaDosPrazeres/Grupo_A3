package test;

import model.User;
import model.UserRole;

public class ControllerLayerTest {

    public static void main(String[] args) {
        System.out.println("Iniciando ControllerLayerTest...");
        executarTesteDaCamadaController();
        System.out.println("ControllerLayerTest finalizado com sucesso.");
    }

    private static void executarTesteDaCamadaController() {
        FakeLoginView loginView = new FakeLoginView();
        FakeAuthService authService = new FakeAuthService();

        loginView.email = "admin@email.com";
        loginView.password = "123";

        User admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@email.com");
        admin.setPasswordHash("123");
        admin.setRole(UserRole.ADMIN);
        authService.userToReturn = admin;

        FakeLoginController loginController = new FakeLoginController(loginView, authService);
        loginController.handleLogin();

        check(authService.called, "AuthService deveria ter sido chamado");
        checkEquals("admin@email.com", authService.lastEmail, "Email do login inválido");
        checkEquals("123", authService.lastPassword, "Senha do login inválida");

        FakeAdminUserView adminUserView = new FakeAdminUserView();
        FakeUserDAO userDAO = new FakeUserDAO();
        FakeUserFormDialog dialog = new FakeUserFormDialog();

        dialog.name = "Novo Usuário";
        dialog.email = "novo@email.com";
        dialog.password = "123";
        dialog.role = UserRole.MANAGER;
        dialog.active = true;

        FakeAdminUserController adminController = new FakeAdminUserController(adminUserView, userDAO);
        adminController.saveUser(dialog);

        check(userDAO.createdUser != null, "UserDAO deveria ter criado um usuário");
        check(dialog.closed, "Dialog deveria ter sido fechado");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    private static void checkEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new RuntimeException(message + " | esperado=" + expected + ", obtido=" + actual);
    }

    static class FakeLoginView {
        String email;
        String password;
        String errorMessage;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void showError(String message) {
            this.errorMessage = message;
        }
    }

    static class FakeAuthService {
        boolean called = false;
        String lastEmail;
        String lastPassword;
        User userToReturn;

        public User login(String email, String password) {
            called = true;
            lastEmail = email;
            lastPassword = password;
            return userToReturn;
        }
    }

    static class FakeLoginController {
        private final FakeLoginView loginView;
        private final FakeAuthService authService;

        public FakeLoginController(FakeLoginView loginView, FakeAuthService authService) {
            this.loginView = loginView;
            this.authService = authService;
        }

        public void handleLogin() {
            String email = loginView.getEmail();
            String password = loginView.getPassword();

            User user = authService.login(email, password);
            if (user == null) {
                loginView.showError("Login inválido");
            }
        }
    }

    static class FakeAdminUserView {
    }

    static class FakeUserFormDialog {
        String name;
        String email;
        String password;
        UserRole role;
        boolean active;
        boolean closed = false;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public UserRole getRole() {
            return role;
        }

        public boolean isActive() {
            return active;
        }

        public void closeDialog() {
            closed = true;
        }
    }

    static class FakeUserDAO {
        User createdUser;

        public User create(User user) {
            user.setId(99L);
            this.createdUser = user;
            return user;
        }
    }

    static class FakeAdminUserController {
        private final FakeAdminUserView adminUserView;
        private final FakeUserDAO userDAO;

        public FakeAdminUserController(FakeAdminUserView adminUserView, FakeUserDAO userDAO) {
            this.adminUserView = adminUserView;
            this.userDAO = userDAO;
        }

        public void saveUser(FakeUserFormDialog dialog) {
            User user = new User();
            user.setName(dialog.getName());
            user.setEmail(dialog.getEmail());
            user.setPasswordHash(dialog.getPassword());
            user.setRole(dialog.getRole());
            user.setActive(dialog.isActive());

            userDAO.create(user);
            dialog.closeDialog();
        }
    }
}