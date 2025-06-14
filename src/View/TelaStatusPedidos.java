package View;

import Model.Pedido;
import Model.Cliente;
import Controller.StatusPedidoController;
import enums.EstadoPedido;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TelaStatusPedidos extends JFrame {
    private JTable tabelaPedidos;
    private DefaultTableModel modeloTabela;
    private JComboBox<EstadoPedido> cmbEstado;
    private JButton btnAtualizar;
    private StatusPedidoController controller;

    public TelaStatusPedidos() {
        setTitle("Status dos Pedidos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new StatusPedidoController(this);

        JPanel painelControle = new JPanel(new GridLayout(1, 3, 10, 10));
        painelControle.add(new JLabel("Novo Estado:"));
        cmbEstado = new JComboBox<>(EstadoPedido.values());
        painelControle.add(cmbEstado);
        btnAtualizar = new JButton("Atualizar Status");
        painelControle.add(btnAtualizar);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Cliente", "Estado", "Total (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPedidos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(painelControle, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(painelPrincipal);

        configurarListeners();

        controller.listarTodosPedidos();
    }

    private void configurarListeners() {
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.atualizarStatus();
            }
        });
    }

    public void carregarPedidos(List<Pedido> pedidos) {
        try {
            modeloTabela.setRowCount(0);
            if (pedidos != null) {
                for (Pedido pedido : pedidos) {
                    modeloTabela.addRow(new Object[]{
                        pedido.getId(),
                        pedido.getCliente().getNome() + " " + pedido.getCliente().getSobrenome(),
                        pedido.getEstado(),
                        String.format("%.2f", pedido.getPrecoTotal())
                    });
                }
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    public Pedido atualizarStatus() {
        int linhaSelecionada = tabelaPedidos.getSelectedRow();
        if (linhaSelecionada == -1) {
            mostrarErro("Selecione um pedido para atualizar!");
            return null;
        }

        int id = (int) tabelaPedidos.getValueAt(linhaSelecionada, 0);
        EstadoPedido novoEstado = (EstadoPedido) cmbEstado.getSelectedItem();

        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setEstado(novoEstado);

        return pedido;
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
} 