package controller;

import app.AppContext;
import model.User;
import model.UserRole;
import view.AdminUserView;
import view.LoginView;
import view.UserFormDialog;

import java.util.List;

/**
 * Controller da jornada do Administrador (o CRUD de usuarios do sistema).
 * Fala direto com o UserDAO pra listar, criar e excluir usuarios.
 *
 * @author leandrorocha
 */
public class AdminUserController {

    private final AdminUserView adminUserView; // tela do admin
    private final AppContext context;          // DAOs + Services

    // injeta a tela e o contexto pelo construtor
    public AdminUserController(AdminUserView adminUserView, AppContext context) {
        this.adminUserView = adminUserView;
        this.context = context;
    }

    // busca todos os usuarios no banco e joga na tabela
    public void loadUsers() {
        try {
            List<User> users = context.getUserDAO().findAll();
            adminUserView.loadUsersTable(users);
        } catch (RuntimeException ex) {
            adminUserView.showError("Não foi possível carregar os usuários: " + ex.getMessage());
        }
    }

    // abre a janelinha (modal) de cadastro de novo usuario
    public void openCreateUserForm() {
        UserFormDialog dialog = new UserFormDialog(adminUserView);
        dialog.setController(this); // o dialog vai chamar saveUser() aqui de volta
        dialog.setVisible(true);
    }

    // le os dados do dialog, valida, cria o usuario e recarrega a tabela
    public void saveUser(UserFormDialog dialog) {
        String name = dialog.getName();
        String email = dialog.getEmail();
        String password = dialog.getPassword();
        String roleStr = dialog.getSelectedRole();

        // campos obrigatorios nao podem ficar vazios
        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {
            dialog.showError("Preencha todos os campos obrigatórios (Nome, E-mail e Senha).");
            return;
        }

        try {
            // monta o objeto User com o que veio da tela
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPasswordHash(password);
            user.setRole(UserRole.valueOf(roleStr));
            user.setActive(dialog.isActive());
            // codigo do gerente so faz sentido se o perfil for MANAGER
            if (user.getRole() == UserRole.MANAGER) {
                String code = dialog.getManagerCode();
                if (code != null && !code.trim().isEmpty()) {
                    user.setManagerCode(code.trim());
                }
            }

            // grava no banco, fecha o dialog e atualiza a lista
            context.getUserDAO().create(user);
            dialog.closeDialog();
            adminUserView.showSuccess("Usuário \"" + user.getName() + "\" cadastrado com sucesso!");
            loadUsers();
        } catch (RuntimeException ex) {
            dialog.showError("Erro ao cadastrar usuário: " + ex.getMessage());
        }
    }

    // exclui o usuario selecionado (pedindo confirmacao antes)
    public void deleteSelectedUser() {
        Long id = adminUserView.getSelectedUserId();
        if (id == null) {
            adminUserView.showError("Selecione um usuário na tabela para excluir.");
            return;
        }
        if (!adminUserView.confirmDelete()) {
            return; // usuario clicou em "nao"
        }
        try {
            context.getUserDAO().delete(id);
            adminUserView.showSuccess("Usuário excluído com sucesso.");
            loadUsers();
        } catch (RuntimeException ex) {
            adminUserView.showError("Erro ao excluir usuário: " + ex.getMessage());
        }
    }

    // sai da conta do admin e volta pro login
    public void logout() {
        adminUserView.closeView();
        LoginView loginView = new LoginView();
        loginView.setController(new LoginController(loginView, context));
        loginView.showView();
    }
}
