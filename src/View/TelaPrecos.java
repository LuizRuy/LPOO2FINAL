package View;

import Model.PrecoPizza;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TelaPrecos extends JFrame {
    private JTextField txtPrecoSimples;
    private JTextField txtPrecoEspecial;
    private JTextField txtPrecoPremium;
    private JButton btnSalvar;
    private PrecoPizza precoPizza;

    public TelaPrecos() {
        setTitle("Atualização de Preços");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        precoPizza = PrecoPizza.getInstance();

        JPanel painelEntrada = new JPanel(new GridLayout(3, 2, 10, 10));

        painelEntrada.add(new JLabel("Preço Simples (R$/cm²):"));
        txtPrecoSimples = new JTextField(String.valueOf(precoPizza.getPrecoSimples()));
        painelEntrada.add(txtPrecoSimples);
        
        painelEntrada.add(new JLabel("Preço Especial (R$/cm²):"));
        txtPrecoEspecial = new JTextField(String.valueOf(precoPizza.getPrecoEspecial()));
        painelEntrada.add(txtPrecoEspecial);
        
        painelEntrada.add(new JLabel("Preço Premium (R$/cm²):"));
        txtPrecoPremium = new JTextField(String.valueOf(precoPizza.getPrecoPremium()));
        painelEntrada.add(txtPrecoPremium);

        btnSalvar = new JButton("Salvar");
        JPanel painelBotao = new JPanel();
        painelBotao.add(btnSalvar);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(painelEntrada, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);

        javax.swing.border.EmptyBorder borda = new javax.swing.border.EmptyBorder(10, 10, 10, 10);
        painelPrincipal.setBorder(borda);

        add(painelPrincipal);

        configurarListener();
    }

    private void configurarListener() {
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPrecos();
            }
        });
    }

    private void salvarPrecos() {
        try {
            double precoSimples = Double.parseDouble(txtPrecoSimples.getText().trim());
            double precoEspecial = Double.parseDouble(txtPrecoEspecial.getText().trim());
            double precoPremium = Double.parseDouble(txtPrecoPremium.getText().trim());

            if (precoSimples <= 0 || precoEspecial <= 0 || precoPremium <= 0) {
                JOptionPane.showMessageDialog(this, "Todos os preços devem ser maiores que zero!");
                return;
            }

            precoPizza.setPrecoSimples(precoSimples);
            precoPizza.setPrecoEspecial(precoEspecial);
            precoPizza.setPrecoPremium(precoPremium);

            JOptionPane.showMessageDialog(this, "Configurações atualizadas com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço inválido! Digite um número válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar preços: " + e.getMessage());
        }
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
} 