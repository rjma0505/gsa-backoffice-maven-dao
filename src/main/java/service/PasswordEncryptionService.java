package service;

// importa a biblioteca BCrypt para cifrar passwords
import org.mindrot.jbcrypt.BCrypt;

/**
 * Serviço responsável por encriptar e validar palavras-chave
 *
 * Utiliza a biblioteca BCrypt para garantir a segurança
 */
public class PasswordEncryptionService {

    /**
     * Gera o hash de uma password em texto simples
     *
     * @param plainText password introduzida pelo utilizador
     * @return string cifrada com BCrypt
     */
    public static String hashPassword(String plainText) {
        // gera o hash com salt automaticamente
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    /**
     * Verifica se uma password em texto simples corresponde ao hash guardado
     *
     * @param plainText password fornecida pelo utilizador
     * @param hashed hash previamente armazenado na base de dados
     * @return true se coincidir, false caso contrário
     */
    public static boolean verifyPassword(String plainText, String hashed) {
        // compara o texto simples com o hash
        return BCrypt.checkpw(plainText, hashed);
    }
}