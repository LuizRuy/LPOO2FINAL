package View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TelaPrincipal extends JFrame {
    private JButton btnClientes;
    private JButton btnPedidos;
    private JButton btnSabores;
    private JButton btnPrecos;
    private JButton btnStatusPedidos;

    public TelaPrincipal() {
        setTitle("Sistema de Pizzaria");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        JPanel painelBotoes = new JPanel(new GridLayout(5, 1, 10, 10));

        btnClientes = new JButton("Gerenciar Clientes");
        btnPedidos = new JButton("Fazer Pedido");
        btnSabores = new JButton("Gerenciar Sabores");
        btnPrecos = new JButton("Atualizar Preços");
        btnStatusPedidos = new JButton("Status dos Pedidos");

        painelBotoes.add(btnClientes);
        painelBotoes.add(btnPedidos);
        painelBotoes.add(btnSabores);
        painelBotoes.add(btnPrecos);
        painelBotoes.add(btnStatusPedidos);

        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        add(painelPrincipal);

        configurarListeners();
    }

    private void configurarListeners() {
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaClientes().setVisible(true);
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaPedidos().setVisible(true);
            }
        });

        btnSabores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaSabores().setVisible(true);
            }
        });

        btnPrecos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaPrecos().setVisible(true);
            }
        });

        btnStatusPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaStatusPedidos().setVisible(true);
            }
        });
    }
} 