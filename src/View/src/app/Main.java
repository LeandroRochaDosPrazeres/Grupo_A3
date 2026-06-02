package app;

import controller.LoginController;
import view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de partida do sistema Finance Team.
 *
 * Monta a "caixa de ferramentas" (AppContext = DAOs + Services), cria a tela
 * de login, liga ela no LoginController e mostra. Daqui pra frente o fluxo
 * segue pelas camadas: View -> Controller -> Service -> DAO -> Supabase.
 *
 * @author leandrorocha
 */
public class Main {

    public static void main(String[] args) {
        // tenta deixar a janela com a aparencia do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // se nao rolar, segue com o visual padrao do Swing mesmo
        }

        // toda mexida em tela Swing tem que rodar na thread de eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                AppContext context = new AppContext();
                LoginView loginView = new LoginView();
                loginView.setController(new LoginController(loginView, context));
                loginView.showView();
            } catch (Exception ex) {
                // se faltar o .env ou der ruim na inicializacao, avisa de forma amigavel
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Falha ao iniciar a aplicação:\n" + ex.getMessage()
                                + "\n\nVerifique se o arquivo .env com SUPABASE_URL e "
                                + "SUPABASE_API_KEY está presente.",
                        "Erro de Inicialização", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
