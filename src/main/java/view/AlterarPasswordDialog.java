package view;

import controller.AlterarPasswordController;

import javax.swing.*;
import java.awt.*;

/**
 * Janela de diálogo para alterar a password de um utilizador existente
 */
public class AlterarPasswordDialog extends JDialog {

    // Campo para a nova password
    private JPasswordField txtPassword;

    // Campo para confirmação da nova password
    private JPasswordField txtPasswordRepeat;

    // Botão para gravar a nova password
    private JButton btnGravar;

    // Botão para cancelar a alteração
    private JButton btnCancelar;

    // ID do utilizador cuja password será alterada
    private int utilizadorId;

    /**
     * Construtor do diálogo
     *
     * @param owner Janela pai
     * @param utilizadorId ID do utilizador
     */
    public AlterarPasswordDialog(JFrame owner, int utilizadorId) {
        super(owner, "Alterar Password", true);
        this.utilizadorId = utilizadorId;

        setSize(400, 220);
        setLocationRelativeTo(owner);
        inicializarComponentes();
    }

    /**
     * Inicializa os componentes gráficos
     */
    private void inicializarComponentes() {
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtPassword = new JPasswordField();
        txtPasswordRepeat = new JPasswordField();

        btnGravar = new JButton("Gravar");
        btnCancelar = new JButton("Cancelar");

        painel.add(new JLabel("Nova Password:"));
        painel.add(txtPassword);

        painel.add(new JLabel("Confirmar Password:"));
        painel.add(txtPasswordRepeat);

        painel.add(btnCancelar);
        painel.add(btnGravar);

        btnCancelar.addActionListener(e -> dispose());
        btnGravar.addActionListener(e -> gravar());

        add(painel);
    }

    /**
     * Valida e grava a nova password através do controller
     */
    private void gravar() {
        String pass1 = new String(txtPassword.getPassword());
        String pass2 = new String(txtPasswordRepeat.getPassword());

        AlterarPasswordController controller = new AlterarPasswordController();
        AlterarPasswordController.ResultadoAlteracao resultado = controller.alterarPassword(utilizadorId, pass1, pass2);

        switch (resultado) {
            case CAMPOS_INCOMPLETOS:
                JOptionPane.showMessageDialog(this, "Preencha ambas as passwords.");
                break;
            case PASSWORDS_DIFERENTES:
                JOptionPane.showMessageDialog(this, "As passwords não coincidem.");
                break;
            case SUCESSO:
                JOptionPane.showMessageDialog(this, "Password atualizada com sucesso.");
                dispose();
                break;
            case ERRO:
            default:
                JOptionPane.showMessageDialog(this, "Erro ao atualizar a password.");
                break;
        }
    }
}