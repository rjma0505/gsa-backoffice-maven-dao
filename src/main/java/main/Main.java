package main;

// importa a interface de login
import view.LoginView;

/**
 * Classe principal para arrancar a aplicação de backoffice.
 *
 * Esta classe serve como ponto de entrada da aplicação,
 * chamando diretamente a interface de login.
 */
public class Main {

    public static void main(String[] args) {
        // inicia a interface de login, passando os argumentos da consola
        // ao método estático main do LoginView
        LoginView.main(args);
    }
}



//