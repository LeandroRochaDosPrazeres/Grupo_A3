package util;

import javax.swing.*;
import java.awt.*;

/**
 * Classe base para JFrames do sistema.
 * Centraliza configurações comuns: tamanho, posição, título e comportamento de fechamento.
 * 
 * @author leandrorocha
 */
public abstract class BaseFrame extends JFrame {

    /**
     * Configura o frame com título, tamanho padrão e centralização.
     */
    protected void configureFrame(String title) {
        setTitle("Finance Team - " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1250, 850));
        centerOnScreen();
    }

    /**
     * Centraliza a janela na tela do usuário.
     */
    protected void centerOnScreen() {
        setLocationRelativeTo(null);
    }

    /**
     * Exibe mensagem de erro padronizada.
     */
    protected void showError(String message) {
        MessageUtil.showError(this, message);
    }

    /**
     * Exibe mensagem de sucesso padronizada.
     */
    protected void showSuccess(String message) {
        MessageUtil.showSuccess(this, message);
    }

    /**
     * Exibe diálogo de confirmação padronizado.
     */
    protected boolean confirmAction(String message) {
        return MessageUtil.confirm(this, message);
    }
}
