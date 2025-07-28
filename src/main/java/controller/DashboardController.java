package controller;

import dao.PerfilDAO;
import dao.UtilizadorDAO;
import model.Perfil;
import model.Utilizador;

import java.util.List;

public class DashboardController {

    private final UtilizadorDAO utilizadorDAO;
    private final PerfilDAO perfilDAO;

    public DashboardController() {
        utilizadorDAO = new UtilizadorDAO();
        perfilDAO = new PerfilDAO();
    }

    public List<Utilizador> listarUtilizadores() {
        return utilizadorDAO.listarTodos();
    }

    public void atualizarUtilizador(Utilizador u) {
        utilizadorDAO.atualizar(u);
    }

    public void apagarUtilizador(int id) throws Exception {
        Utilizador u = utilizadorDAO.obterPorId(id);
        if (u == null) {
            throw new Exception("Utilizador não encontrado.");
        }
        Perfil p = perfilDAO.obterPorId(u.getPerfilId());
        if (p != null && p.getDescricao().equalsIgnoreCase("Administrador")) {
            int nAdmins = utilizadorDAO.contarAdministradores();
            if (nAdmins <= 1) {
                throw new Exception("Não é permitido apagar o último Administrador.");
            }
        }
        utilizadorDAO.apagar(id);
    }

    public List<Perfil> listarPerfis() {
        return perfilDAO.listarTodos();
    }

    public Perfil obterPerfilPorDescricao(String desc) {
        return perfilDAO.obterPorDescricao(desc);
    }

    public void atualizarPassword(int id, String novaPasswordHash) {
        utilizadorDAO.atualizarPassword(id, novaPasswordHash);
    }

    // NOVOS MÉTODOS para validação na view

    /**
     * Obtem um utilizador pelo seu ID
     */
    public Utilizador obterUtilizadorPorId(int id) {
        return utilizadorDAO.obterPorId(id);
    }

    /**
     * Obtem um perfil pelo seu ID
     */
    public Perfil obterPerfilPorId(int id) {
        return perfilDAO.obterPorId(id);
    }

    /**
     * Conta o número de administradores existentes
     */
    public int contarAdministradores() {
        return utilizadorDAO.contarAdministradores();
    }
}