package view;

// A importação de LoginController não é mais necessária aqui, pois a View não o chama diretamente.
// import controller.LoginController;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface de login do backoffice
 * Atua como a "View" na arquitetura MVC, exibindo a interface
 * e fornecendo métodos para o Controller aceder aos dados de entrada.
 */
public class LoginView extends JFrame {

    // campo de texto para o email (utilizador)
    private JTextField txtEmail;

    // campo de password
    private JPasswordField txtPassword;

    // botão de login
    private JButton btnLogin;

    /**
     * Cores da aplicação definidas globalmente para consistência.
     */
    public static final Color APP_BACKGROUND_COLOR = new Color(58, 64, 78);
    public static final Color FIELD_BACKGROUND_COLOR = new Color(68, 74, 88);
    public static final Color FIELD_FOREGROUND_COLOR = Color.WHITE;
    public static final Color CARET_COLOR = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(100, 149, 237);
    public static final Color BUTTON_BACKGROUND_COLOR = new Color(33, 36, 45);
    public static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;


    /**
     * Construtor da interface de login
     */
    public LoginView() {
        setTitle("Backoffice - Login");
        setSize(400, 200);
        setLocationRelativeTo(null); // centra a janela no ecrã
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // apply global theme only once in main, not here
        inicializarComponentes();
    }

    /**
     * Inicializa todos os componentes do ecrã de login
     */
    private void inicializarComponentes() {
        // Painel principal com fundo escuro
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(APP_BACKGROUND_COLOR); // Cor de fundo escura

        // Estilizando os rótulos e campos de texto
        JLabel lblEmail = new JLabel("Utilizador (Email):");
        lblEmail.setForeground(FIELD_FOREGROUND_COLOR); // Texto branco
        txtEmail = new JTextField();
        estilizarCampoTexto(txtEmail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(FIELD_FOREGROUND_COLOR); // Texto branco
        txtPassword = new JPasswordField();
        estilizarCampoTexto(txtPassword);

        // Botão de login
        btnLogin = new JButton("Login");
        estilizarBotao(btnLogin);
        // REMOVIDO: btnLogin.addActionListener(e -> fazerLogin());
        // O ActionListener será adicionado pelo LoginController,
        // garantindo que o Controller principal lida com a autenticação.

        // Organiza os componentes no painel
        painel.add(lblEmail);
        painel.add(txtEmail);
        painel.add(lblPassword);
        painel.add(txtPassword);
        painel.add(new JLabel()); // Espaço vazio para alinhamento
        painel.add(btnLogin);

        // Adiciona o painel ao JFrame
        add(painel);
    }

    /**
     * Estiliza os campos de texto (email e senha)
     */
    private void estilizarCampoTexto(JTextField campo) {
        campo.setBackground(FIELD_BACKGROUND_COLOR); // Cor de fundo mais escura
        campo.setForeground(FIELD_FOREGROUND_COLOR); // Texto branco
        campo.setCaretColor(CARET_COLOR); // Cor do cursor
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1), // Borda azul vibrante
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Espaçamento interno
        ));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    /**
     * Estiliza os botões
     */
    private void estilizarBotao(JButton botao) {
        botao.setBackground(BUTTON_BACKGROUND_COLOR); // Fundo escuro
        botao.setForeground(BUTTON_FOREGROUND_COLOR); // Texto branco
        botao.setFocusPainted(false); // Remove borda de foco
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Fonte moderna

        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2), // Borda azul
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Espaçamento interno
        ));
    }

    // REMOVIDO: O método fazerLogin() foi movido para o LoginController.
    // A lógica de autenticação é responsabilidade do LoginController.
    /*
    private void fazerLogin() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        LoginController controller = new LoginController(); // ESTE ERA O PROBLEMA: CRIAVA UMA NOVA INSTÂNCIA
        int resultado = controller.autenticar(email, password);

        if (resultado == 1) {
            new DashboardView().mostrar();
            dispose();
        } else if (resultado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Apenas utilizadores com perfil Administrador podem aceder ao backoffice.",
                    "Acesso Negado",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Credenciais inválidas.",
                    "Erro de Autenticação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    */

    // Métodos para o LoginController aceder aos dados
    public String getUtilizador() {
        return txtEmail.getText();
    }

    public String getPalavraChave() {
        return new String(txtPassword.getPassword());
    }

    /**
     * Retorna o botão de login para que o Controller possa adicionar um ActionListener.
     *
     * @return O JButton do login.
     */
    public JButton getBtnEntrar() {
        return btnLogin;
    }

    /**
     * Aplica o estilo personalizado ao JOptionPane globalmente.
     * Este método deve ser chamado apenas uma vez no início da aplicação.
     */
    public static void aplicarTemaGlobal() {
        // Cores da sua aplicação (já definidas como constantes)
        // Fundo do JOptionPane e dos painéis internos
        UIManager.put("OptionPane.background", APP_BACKGROUND_COLOR);
        UIManager.put("Panel.background", APP_BACKGROUND_COLOR);

        // Cor do texto
        UIManager.put("OptionPane.messageForeground", FIELD_FOREGROUND_COLOR);
        UIManager.put("Label.foreground", FIELD_FOREGROUND_COLOR); // Garante que labels dentro do OptionPane também sejam brancas

        // Estilo dos botões no JOptionPane - apenas cores, sem alterar o formato/borda/foco
        UIManager.put("Button.background", BUTTON_BACKGROUND_COLOR);
        UIManager.put("Button.foreground", BUTTON_FOREGROUND_COLOR);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12)); // Fonte para botões do JOptionPane

        // Estilo para campos de texto dentro de OptionPanes ou outros diálogos simples
        UIManager.put("TextField.background", FIELD_BACKGROUND_COLOR);
        UIManager.put("TextField.foreground", FIELD_FOREGROUND_COLOR);
        UIManager.put("TextField.caretColor", CARET_COLOR);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        UIManager.put("PasswordField.background", FIELD_BACKGROUND_COLOR);
        UIManager.put("PasswordField.foreground", FIELD_FOREGROUND_COLOR);
        UIManager.put("PasswordField.caretColor", CARET_COLOR);
        UIManager.put("PasswordField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Estilo para JComboBoxes
        UIManager.put("ComboBox.background", FIELD_BACKGROUND_COLOR);
        UIManager.put("ComboBox.foreground", FIELD_FOREGROUND_COLOR);
        UIManager.put("ComboBox.selectionBackground", BORDER_COLOR); // Cor de seleção para itens do ComboBox
        UIManager.put("ComboBox.selectionForeground", FIELD_FOREGROUND_COLOR);
        UIManager.put("ComboBox.buttonBackground", BUTTON_BACKGROUND_COLOR); // Cor do botão de seta do ComboBox
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(BORDER_COLOR, 1)); // Borda para o ComboBox

        // Estilo da Tabela
        UIManager.put("Table.background", FIELD_BACKGROUND_COLOR);
        UIManager.put("Table.foreground", FIELD_FOREGROUND_COLOR);
        UIManager.put("Table.selectionBackground", new Color(100, 149, 237, 100)); // Seleção mais clara
        UIManager.put("Table.selectionForeground", FIELD_FOREGROUND_COLOR);
        UIManager.put("Table.gridColor", APP_BACKGROUND_COLOR.darker()); // Cor das linhas da grade
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 14));

        // Estilo do cabeçalho da tabela
        UIManager.put("TableHeader.background", APP_BACKGROUND_COLOR);
        UIManager.put("TableHeader.foreground", FIELD_FOREGROUND_COLOR);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        // Estilo do JScrollPane
        UIManager.put("ScrollPane.background", APP_BACKGROUND_COLOR);
        UIManager.put("Viewport.background", FIELD_BACKGROUND_COLOR); // Onde a tabela rola
    }

    /**
     * Método main para arrancar a aplicação diretamente
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Aplica o tema visual que você criou
            aplicarTemaGlobal();

            // Inicia a aplicação criando o Controller.
            // É o Controller que vai criar e mostrar a janela de login.
            new LoginController();
        });
    }
}
