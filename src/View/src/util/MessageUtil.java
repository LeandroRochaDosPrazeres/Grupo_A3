package util;

import javax.swing.*;
import java.awt.*;

/**
 * Atalho pra mostrar mensagens padronizadas (sucesso, erro, confirmacao).
 * Junta tudo num lugar so pra nao espalhar JOptionPane pelo codigo.
 *
 * Tem um "modo silencioso": quando ligado, em vez de abrir janelinha, so
 * guarda a ultima mensagem. Isso serve pros testes automatizados nao travarem
 * esperando alguem clicar em "OK". No uso normal fica desligado.
 *
 * @author leandrorocha
 */
public class MessageUtil {

    // quando true, nao abre dialog nenhum (usado nos testes)
    private static boolean silentMode = false;
    // guarda a ultima mensagem mostrada (util pros testes conferirem)
    private static String lastMessage = "";
    // resposta fixa pra confirmacao quando esta em modo silencioso
    private static boolean autoConfirm = true;

    public static void setSilentMode(boolean on) { silentMode = on; }
    public static String getLastMessage() { return lastMessage; }
    public static void setAutoConfirm(boolean value) { autoConfirm = value; }

    // mensagem de sucesso
    public static void showSuccess(Component parent, String message) {
        lastMessage = message;
        if (silentMode) return;
        JOptionPane.showMessageDialog(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // mensagem de erro
    public static void showError(Component parent, String message) {
        lastMessage = message;
        if (silentMode) return;
        JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // mensagem de aviso (warning)
    public static void showWarning(Component parent, String message) {
        lastMessage = message;
        if (silentMode) return;
        JOptionPane.showMessageDialog(parent, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // confirmacao Sim/Nao. Retorna true se confirmou.
    public static boolean confirm(Component parent, String message) {
        lastMessage = message;
        if (silentMode) return autoConfirm;
        int resposta = JOptionPane.showConfirmDialog(
            parent, message, "Confirmação",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
        return resposta == JOptionPane.YES_OPTION;
    }
}
