/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author leandrorocha
 */

import view.LoginView;

public class LoginController {
    // Atributo definido na documentação [cite: 268]
    private LoginView loginView;

    // Construtor que injeta a view [cite: 272]
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    // Método principal de ação [cite: 273]
    public void handleLogin() {
        // 1. Lê os dados da view [cite: 274]
        String email = loginView.getEmail();
        String senha = loginView.getPassword();

        // Simulação de lógica (isso depois chamará o AuthService) [cite: 275]
        if (email.isEmpty() || senha.isEmpty()) {
            // Se falha, chama o erro na view [cite: 277]
            loginView.showError("Por favor, preencha todos os campos.");
        } else {
            // Se sucesso, no futuro abrirá o Dashboard [cite: 276]
            System.out.println("Login solicitado para: " + email);
            loginView.showError("Conexão com o Controller funcionando! Próximo passo: integrar com o banco.");
        }
    }
}