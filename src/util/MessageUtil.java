package util;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para mensagens padronizadas usando JOptionPane.
 * Centraliza exibição de erros, sucessos e confirmações em todo o sistema.
 * 
 * @author leandrorocha
 */
public class MessageUtil {

    /**
     * Exibe mensagem de sucesso.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe mensagem de erro.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Exibe diálogo de confirmação (Sim/Não).
     * @return true se o usuário confirmou, false caso contrário.
     */
    public static boolean confirm(Component parent, String message) {
        int resposta = JOptionPane.showConfirmDialog(
            parent, message, "Confirmação",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
        return resposta == JOptionPane.YES_OPTION;
    }
}
