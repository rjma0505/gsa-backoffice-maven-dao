package dao;

// importa o modelo Perfil e a classe de ligação à base de dados
import model.Perfil;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações sobre a tabela 'perfil'
 *
 * Permite consultar e listar os perfis de acesso
 */
public class PerfilDAO {

    /**
     * Obtém um perfil a partir do seu ID
     *
     * @param id identificador do perfil
     * @return objeto Perfil preenchido ou null se não existir
     */
    public Perfil obterPorId(int id) {
        Perfil p = null;
        try (Connection con = ConexaoBD.getConnection()) {
            // query SQL para selecionar o perfil com o ID fornecido
            String sql = "SELECT * FROM perfil WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // se encontrou resultado, popula o objeto Perfil
            if (rs.next()) {
                p = new Perfil();
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // imprime erros para debug
        }
        return p;
    }

    /**
     * Lista todos os perfis disponíveis
     *
     * @return lista de objetos Perfil
     */
    public List<Perfil> listarTodos() {
        List<Perfil> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConnection()) {
            // query para selecionar todos os registos
            String sql = "SELECT * FROM perfil";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // percorre os resultados e adiciona à lista
            while (rs.next()) {
                Perfil p = new Perfil();
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtém um perfil a partir da descrição
     *
     * @param descricao texto descritivo do perfil
     * @return objeto Perfil preenchido ou null se não existir
     */
    public Perfil obterPorDescricao(String descricao) {
        Perfil p = null;
        try (Connection con = ConexaoBD.getConnection()) {
            // query para procurar o perfil com a descrição fornecida
            String sql = "SELECT * FROM perfil WHERE descricao=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, descricao);
            ResultSet rs = ps.executeQuery();

            // se encontrou resultado, popula o objeto Perfil
            if (rs.next()) {
                p = new Perfil();
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}