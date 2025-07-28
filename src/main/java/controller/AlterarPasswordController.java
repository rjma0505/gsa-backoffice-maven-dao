package controller;

import dao.UtilizadorDAO;
import service.PasswordEncryptionService;

/**
 * Controller para gerir a alteração de password dos utilizadores
 */
public class AlterarPasswordController {

    // Instância do DAO para atualização na BD
    private final UtilizadorDAO dao = new UtilizadorDAO();

    /**
     * Enum com os possíveis resultados da alteração de password
     */
    public enum ResultadoAlteracao {
        SUCESSO,
        CAMPOS_INCOMPLETOS,
        PASSWORDS_DIFERENTES,
        ERRO
    }

    /**
     * Valida e atualiza a password do utilizador
     *
     * @param id ID do utilizador
     * @param pass1 Nova password
     * @param pass2 Confirmação da nova password
     * @return Resultado da operação
     */
    public ResultadoAlteracao alterarPassword(int id, String pass1, String pass2) {
        if (pass1 == null || pass1.trim().isEmpty() || pass2 == null || pass2.trim().isEmpty()) {
            return ResultadoAlteracao.CAMPOS_INCOMPLETOS;
        }

        if (!pass1.equals(pass2)) {
            return ResultadoAlteracao.PASSWORDS_DIFERENTES;
        }

        try {
            String hash = PasswordEncryptionService.hashPassword(pass1);
            dao.atualizarPassword(id, hash);
            return ResultadoAlteracao.SUCESSO;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoAlteracao.ERRO;
        }
    }
}
