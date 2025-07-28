package controller;

// importa os DAOs e modelos necessários
import dao.UtilizadorDAO;
import dao.PerfilDAO;
import model.Perfil;
import model.Utilizador;
import service.PasswordEncryptionService;
import view.LoginView; // Importa a LoginView
import view.DashboardView; // Importa a DashboardView, assumindo que existe
import javax.swing.JOptionPane; // Para exibir mensagens

/**
 * Controlador responsável pela autenticação de utilizadores no sistema
 */
public class LoginController {

    private final LoginView loginView;
    private final UtilizadorDAO utilizadorDAO;
    private final PerfilDAO perfilDAO;

    public LoginController() {
        this.loginView = new LoginView(); // Cria a instância da LoginView
        this.utilizadorDAO = new UtilizadorDAO();
        this.perfilDAO = new PerfilDAO(); // Inicializa PerfilDAO

        // Verifica se o botão obtido da LoginView não é nulo antes de adicionar o listener
        if (this.loginView.getBtnEntrar() != null) {
            System.out.println("Botão de Login encontrado na LoginView. A adicionar ActionListener...");
            // Adiciona o ActionListener ao botão de login da LoginView
            this.loginView.getBtnEntrar().addActionListener(e -> {
                System.out.println("Botão de Login Clicado!"); // Verifica se o ActionListener está a ser acionado

                String email = loginView.getUtilizador();
                String palavraChave = loginView.getPalavraChave();

                System.out.println("Tentativa de autenticação para o email: " + email);
                // ATENÇÃO: Não imprimir a palavra-chave em texto simples em produção! Apenas para debug.
                System.out.println("Palavra-chave fornecida (texto simples): " + palavraChave);

                int resultadoAutenticacao = autenticar(email, palavraChave);

                if (resultadoAutenticacao == 1) {
                    System.out.println("Login aceite para: " + email);
                    JOptionPane.showMessageDialog(loginView,
                            "Login bem-sucedido!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    loginView.dispose(); // Fecha a janela de login
                    // Supondo que DashboardView tem um construtor que a torna visível ou um método mostrar()
                    new DashboardView().mostrar(); // Abre a próxima janela (Dashboard)
                } else if (resultadoAutenticacao == -1) {
                    System.out.println("Acesso Negado: Perfil não autorizado para: " + email);
                    JOptionPane.showMessageDialog(loginView,
                            "Apenas utilizadores com perfil Administrador podem aceder ao backoffice.",
                            "Acesso Negado",
                            JOptionPane.ERROR_MESSAGE);
                } else { // resultadoAutenticacao == 0
                    System.out.println("Credenciais inválidas para: " + email);
                    JOptionPane.showMessageDialog(loginView,
                            "Credenciais inválidas. Verifique seu email e senha.",
                            "Erro de Autenticação",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            System.err.println("ERRO: O botão de login (getBtnEntrar()) é nulo na LoginView. O ActionListener não pode ser adicionado.");
        }

        // Torna a janela de login visível após configurar o listener
        this.loginView.setVisible(true);
    }

    /**
     * Método para autenticar o utilizador no login
     *
     * @param email email do utilizador (campo utilizador na BD)
     * @param palavraChave password em texto simples
     * @return 1 se for válido e com perfil Administrador
     * 0 se credenciais inválidas
     * -1 se perfil não autorizado
     */
    public int autenticar(String email, String palavraChave) {
        // cria o DAO para aceder à tabela utilizador
        Utilizador u = utilizadorDAO.obterPorEmail(email);

        if (u == null) {
            System.out.println("Utilizador não encontrado na base de dados para o email: " + email);
            return 0;
        }

        System.out.println("Utilizador encontrado: " + u.getUtilizador());
        System.out.println("Hash da palavra-passe recuperada da BD: " + u.getPalavraChave());
        System.out.println("Hash da palavra-pass: " + PasswordEncryptionService.hashPassword(palavraChave));


        boolean passwordCorreta = PasswordEncryptionService.verifyPassword(palavraChave, u.getPalavraChave());
        System.out.println("Resultado da verificação da palavra-passe (BCrypt.checkpw): " + passwordCorreta);

        if (passwordCorreta) {
            // obtém o perfil associado ao utilizador
            Perfil p = perfilDAO.obterPorId(u.getPerfilId());

            if (p == null) {
                System.out.println("Erro: Perfil não encontrado para o ID: " + u.getPerfilId());
                return -1;
            }

            System.out.println("Perfil do utilizador: " + p.getDescricao());

            // verifica se o perfil é Administrador
            if (p.getDescricao().equalsIgnoreCase("Administrador")) {
                System.out.println("Autenticação bem-sucedida. Perfil: Administrador.");
                return 1; // sucesso
            } else {
                System.out.println("Autenticação bem-sucedida, mas perfil não autorizado: " + p.getDescricao());
                return -1; // perfil existe mas não é administrador
            }
        } else {
            System.out.println("Palavra-passe incorreta para o utilizador: " + email);
            return 0; // credenciais inválidas (palavra-passe incorreta)
        }
    }
}
