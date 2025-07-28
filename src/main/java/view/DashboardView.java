package view;

import controller.DashboardController;
import model.Perfil;
import model.Utilizador;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interface principal para gestão dos utilizadores no backoffice.
 */
public class DashboardView extends JFrame {

    private DashboardController controller;
    private JTable tabelaUtilizadores;
    private JButton btnCriar;
    private JButton btnEliminar;
    private JButton btnAlterarPassword;
    private JButton btnLogout;
    private DefaultTableModel modelo;
    private JLabel lblStatus;

    public DashboardView() {
        controller = new DashboardController();

        setTitle("Dashboard - Gestão de Utilizadores");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        carregarUtilizadores();
    }

    private void inicializarComponentes() {
        JPanel painel = new JPanel(new BorderLayout());

        // botões
        btnCriar = new JButton("Criar Utilizador");
        btnCriar.addActionListener(e -> abrirCriarUtilizador());

        btnEliminar = new JButton("Eliminar Utilizador");
        btnEliminar.addActionListener(e -> eliminarUtilizador());

        btnAlterarPassword = new JButton("Alterar Password");
        btnAlterarPassword.addActionListener(e -> abrirAlterarPassword());

        btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> fazerLogout());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCriar);
        painelBotoes.add(btnEliminar);
        painelBotoes.add(btnAlterarPassword);
        painelBotoes.add(btnLogout);

        painel.add(painelBotoes, BorderLayout.NORTH);

        // tabela
        String[] colunas = {"ID", "Nome", "Email", "Perfil"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID não editável
            }
        };

        tabelaUtilizadores = new JTable(modelo);

        // altura e fonte para melhor legibilidade
        tabelaUtilizadores.setRowHeight(30);
        tabelaUtilizadores.setFont(new Font("Arial", Font.PLAIN, 14));

        // editor dropdown para perfis
        List<Perfil> listaPerfis = controller.listarPerfis();
        String[] descricoesPerfis = listaPerfis.stream()
                .map(Perfil::getDescricao)
                .toArray(String[]::new);

        JComboBox<String> comboPerfil = new JComboBox<>(descricoesPerfis);
        tabelaUtilizadores.getColumnModel().getColumn(3)
                .setCellEditor(new DefaultCellEditor(comboPerfil));

        JScrollPane scroll = new JScrollPane(tabelaUtilizadores);
        painel.add(scroll, BorderLayout.CENTER);

        // label para mensagens de status
        lblStatus = new JLabel(" ");
        lblStatus.setOpaque(true);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setForeground(Color.BLACK);
        lblStatus.setBackground(Color.LIGHT_GRAY);

        painel.add(lblStatus, BorderLayout.SOUTH);

        // listener para detectar alterações na tabela
        modelo.addTableModelListener(e -> tratarAlteracaoTabela(e));

        add(painel);
    }

    private void carregarUtilizadores() {
        modelo.setRowCount(0);

        List<Utilizador> lista = controller.listarUtilizadores();

        for (Utilizador u : lista) {
            Perfil perfil = controller.obterPerfilPorDescricao(
                    controller.listarPerfis().stream()
                            .filter(p -> p.getId() == u.getPerfilId())
                            .map(Perfil::getDescricao)
                            .findFirst()
                            .orElse("")
            );

            modelo.addRow(new Object[]{
                    u.getId(),
                    u.getNome(),
                    u.getUtilizador(),
                    perfil != null ? perfil.getDescricao() : ""
            });
        }
    }

    private void tratarAlteracaoTabela(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int linha = e.getFirstRow();
            int id = (int) modelo.getValueAt(linha, 0);
            String nome = (String) modelo.getValueAt(linha, 1);
            String email = (String) modelo.getValueAt(linha, 2);
            String perfilDescricao = (String) modelo.getValueAt(linha, 3);

            Perfil perfil = controller.obterPerfilPorDescricao(perfilDescricao);

            Utilizador u = new Utilizador();
            u.setId(id);
            u.setNome(nome);
            u.setUtilizador(email);
            u.setPerfilId(perfil != null ? perfil.getId() : 1);

            try {
                // Obter estado anterior para comparação
                Utilizador utilizadorAntigo = controller.obterUtilizadorPorId(id);
                Perfil perfilAntigo = controller.obterPerfilPorId(utilizadorAntigo.getPerfilId());
                int nAdmins = controller.contarAdministradores();

                // Se era administrador e está a perder esse perfil e é o último administrador
                if (perfilAntigo != null
                        && perfilAntigo.getDescricao().equalsIgnoreCase("Administrador")
                        && !perfil.getDescricao().equalsIgnoreCase("Administrador")
                        && nAdmins <= 1) {

                    // Impede alteração, mostra alerta e reverte a mudança na tabela
                    JOptionPane.showMessageDialog(this,
                            "Não pode remover o perfil de Administrador ao único administrador existente.",
                            "Erro de validação",
                            JOptionPane.ERROR_MESSAGE);

                    // Reverter valor da tabela para o perfil antigo
                    modelo.setValueAt(perfilAntigo.getDescricao(), linha, 3);
                    return; // interrompe a atualização
                }

                // Atualiza no controller
                controller.atualizarUtilizador(u);
                lblStatus.setText("Dado atualizado na base de dados com sucesso.");
                lblStatus.setBackground(Color.GREEN);
                lblStatus.setForeground(Color.BLACK);
                new javax.swing.Timer(3000, ev -> {
                    lblStatus.setText(" ");
                    lblStatus.setBackground(Color.LIGHT_GRAY);
                }).start();

            } catch (Exception ex) {
                lblStatus.setText("Erro ao atualizar na base de dados!");
                lblStatus.setBackground(Color.RED);
                lblStatus.setForeground(Color.BLACK);
                new javax.swing.Timer(5000, ev -> {
                    lblStatus.setText(" ");
                    lblStatus.setBackground(Color.LIGHT_GRAY);
                }).start();
                ex.printStackTrace();
            }
        }
    }

    private void abrirCriarUtilizador() {
        new CriarUtilizadorDialog(this).setVisible(true);
        carregarUtilizadores();
    }

    private void abrirAlterarPassword() {
        int linha = tabelaUtilizadores.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            new AlterarPasswordDialog(this, id).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione primeiro um utilizador.");
        }
    }

    /**
     * Método para eliminar um utilizador selecionado,
     * verificando que não seja o último Administrador
     */
    private void eliminarUtilizador() {
        int linha = tabelaUtilizadores.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem a certeza que pretende eliminar o utilizador ID " + id + "?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.apagarUtilizador(id);
                    carregarUtilizadores();
                    lblStatus.setText("Utilizador eliminado com sucesso.");
                    lblStatus.setBackground(Color.GREEN);
                    lblStatus.setForeground(Color.BLACK);
                    new javax.swing.Timer(3000, ev -> {
                        lblStatus.setText(" ");
                        lblStatus.setBackground(Color.LIGHT_GRAY);
                    }).start();
                } catch (Exception ex) {
                    lblStatus.setText(ex.getMessage());
                    lblStatus.setBackground(Color.RED);
                    lblStatus.setForeground(Color.BLACK);
                    new javax.swing.Timer(5000, ev -> {
                        lblStatus.setText(" ");
                        lblStatus.setBackground(Color.LIGHT_GRAY);
                    }).start();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione primeiro um utilizador.");
        }
    }

    private void fazerLogout() {
        dispose();
        new LoginView().setVisible(true);
    }

    public void mostrar() {
        setVisible(true);
    }
}