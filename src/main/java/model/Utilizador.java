package model;

/**
 * Classe modelo que representa um utilizador do sistema
 * Guarda os dados básicos de autenticação e associação de perfil
 */
public class Utilizador {

    // identificador único do utilizador (chave primária)
    private int id;

    // nome completo do utilizador
    private String nome;

    // email ou nome de utilizador usado no login
    private String utilizador;

    // password cifrada (hash)
    private String palavraChave;

    // id do perfil associado a este utilizador
    private int perfilId;

    /**
     * Obtém o ID do utilizador
     *
     * @return identificador numérico
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do utilizador
     *
     * @param id identificador numérico
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome completo do utilizador
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do utilizador
     *
     * @param nome texto do nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o campo de login (email ou nome de utilizador)
     *
     * @return texto do login
     */
    public String getUtilizador() {
        return utilizador;
    }

    /**
     * Define o campo de login (email ou nome de utilizador)
     *
     * @param utilizador texto do login
     */
    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    /**
     * Obtém a password cifrada (hash)
     *
     * @return hash da password
     */
    public String getPalavraChave() {
        return palavraChave;
    }

    /**
     * Define a password cifrada
     *
     * @param palavraChave hash da password
     */
    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }

    /**
     * Obtém o ID do perfil associado
     *
     * @return id do perfil
     */
    public int getPerfilId() {
        return perfilId;
    }

    /**
     * Define o ID do perfil associado
     *
     * @param perfilId id do perfil
     */
    public void setPerfilId(int perfilId) {
        this.perfilId = perfilId;
    }
}