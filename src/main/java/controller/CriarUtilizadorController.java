package controller;

import dao.UtilizadorDAO;
import model.Perfil;
import model.Utilizador;
import service.PasswordEncryptionService;

import java.util.regex.Pattern;

/**
 * Controller para gerir a criação de utilizadores
 * Contém a lógica de validação e persistência
 */
public class CriarUtilizadorController {

    // Instância do DAO para interagir com a base de dados
    private final UtilizadorDAO dao = new UtilizadorDAO();

    /**
     * Enumeração para os possíveis resultados da criação
     */
    public enum ResultadoCriacao {
        SUCESSO,
        CAMPOS_INCOMPLETOS,
        EMAIL_INVALIDO,
        EMAIL_EXISTENTE,
        ERRO
    }

    /**
     * Método para criar um novo utilizador, incluindo validações
     *
     * @param nome Nome do utilizador
     * @param email Email do utilizador
     * @param password Password em texto simples
     * @param perfil Perfil associado ao utilizador
     * @return Resultado da operação
     */
    public ResultadoCriacao criarUtilizador(String nome, String email, String password, Perfil perfil) {
        // Verificar campos obrigatórios não vazios
        if (nome == null || nome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || perfil == null) {
            return ResultadoCriacao.CAMPOS_INCOMPLETOS;
        }

        // Validar formato do email usando regex
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if (!pattern.matcher(email).matches()) {
            return ResultadoCriacao.EMAIL_INVALIDO;
        }

        // Verificar se o email já existe na base de dados
        if (dao.existeEmail(email)) {
            return ResultadoCriacao.EMAIL_EXISTENTE;
        }

        // Criar objeto Utilizador e preencher os dados
        Utilizador u = new Utilizador();
        u.setNome(nome);
        u.setUtilizador(email);
        u.setPalavraChave(PasswordEncryptionService.hashPassword(password)); // hash da password
        u.setPerfilId(perfil.getId());

        // Tentar inserir na base de dados
        boolean inseriu = dao.inserir(u);
        if (inseriu) {
            return ResultadoCriacao.SUCESSO;
        } else {
            return ResultadoCriacao.ERRO;
        }
    }
}