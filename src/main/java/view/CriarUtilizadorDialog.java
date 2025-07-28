package view;

// Importações necessárias para o diálogo e manipulação de componentes gráficos
import controller.CriarUtilizadorController;
import dao.PerfilDAO;
import model.Perfil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Janela de diálogo que permite criar um novo utilizador
 * Interface gráfica onde o utilizador preenche os dados e escolhe o perfil
 */
public class CriarUtilizadorDialog extends JDialog {

    // Campo para inserir o nome do novo utilizador
    private JTextField txtNome;

    // Campo para inserir o email (utilizador)
    private JTextField txtEmail;

    // Campo para inserir a password (com máscara)
    private JPasswordField txtPassword;

    // ComboBox (dropdown) para selecionar o perfil do utilizador
    private JComboBox<Perfil> comboPerfil;

    // Botão para submeter os dados e criar o utilizador
    private JButton btnGravar;

    /**
     * Construtor do diálogo
     * @param owner Janela pai para posicionamento e modal
     */
    public CriarUtilizadorDialog(JFrame owner) {
        super(owner, "Novo Utilizador", true); // modal true impede interação com janela pai
        setSize(400, 300);                    // define tamanho da janela
        setLocationRelativeTo(owner);         // centra na janela pai
        inicializarComponentes();              // método que cria e organiza componentes
    }

    /**
     * Método para inicializar e organizar os componentes gráficos do diálogo
     */
    private void inicializarComponentes() {
        // Painel com layout grid para dispor os campos e etiquetas em tabela 5x2
        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));
        // Espaçamento interno do painel
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Inicializar os campos de texto
        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        // Inicializar comboBox para os perfis
        comboPerfil = new JComboBox<>();

        // Obter a lista de perfis da base de dados para popular o comboPerfil
        PerfilDAO perfilDAO = new PerfilDAO();
        List<Perfil> perfis = perfilDAO.listarTodos();
        for (Perfil p : perfis) {
            comboPerfil.addItem(p);
        }

        // Inicializar botão para gravar os dados
        btnGravar = new JButton("Gravar");

        // Adicionar etiquetas e campos ao painel de forma ordenada
        painel.add(new JLabel("Nome:"));
        painel.add(txtNome);

        painel.add(new JLabel("Utilizador (Email):"));
        painel.add(txtEmail);

        painel.add(new JLabel("Password:"));
        painel.add(txtPassword);

        painel.add(new JLabel("Perfil:"));
        painel.add(comboPerfil);

        painel.add(new JLabel()); // célula vazia para alinhamento
        painel.add(btnGravar);

        // Registar ação no botão para quando clicado executar método gravar()
        btnGravar.addActionListener(e -> gravar());

        // Adicionar o painel à janela do diálogo
        add(painel);
    }

    /**
     * Método que recolhe os dados, chama o controller para criar o utilizador
     * e mostra mensagens ao utilizador consoante o resultado
     */
    private void gravar() {
        // Recolher texto dos campos
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        Perfil perfil = (Perfil) comboPerfil.getSelectedItem();

        // Instanciar o controller responsável pela criação
        CriarUtilizadorController controller = new CriarUtilizadorController();

        // Chamar método para criar utilizador e obter resultado
        CriarUtilizadorController.ResultadoCriacao resultado = controller.criarUtilizador(nome, email, password, perfil);

        // Verificar resultado e mostrar mensagem adequada
        switch (resultado) {
            case CAMPOS_INCOMPLETOS:
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                break;
            case EMAIL_INVALIDO:
                JOptionPane.showMessageDialog(this,
                        "O email não está num formato válido (ex: nome@dominio.pt).",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case EMAIL_EXISTENTE:
                JOptionPane.showMessageDialog(this,
                        "O email introduzido já existe.\nEscolha outro.",
                        "Erro de validação",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case SUCESSO:
                JOptionPane.showMessageDialog(this, "Utilizador criado com sucesso!");
                dispose(); // fechar diálogo após criação bem sucedida
                break;
            case ERRO:
            default:
                JOptionPane.showMessageDialog(this, "Erro ao criar o utilizador.");
                break;
        }
    }
}