package View;

import Model.Cliente;
import Model.Pedido;
import Model.Pizza;
import Model.Sabor;
import Controller.PedidoController;
import enums.FormaPizza;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TelaPedidos extends JFrame {
    private JTextField txtTelefone;
    private JButton btnBuscarCliente;
    private JLabel lblCliente;
    private JComboBox<FormaPizza> cmbForma;
    private JComboBox<String> cmbMedida;
    private JLabel lblDimensao;
    private JSpinner spnDimensao;
    private JComboBox<Sabor> cmbSabor1;
    private JComboBox<Sabor> cmbSabor2;
    private JButton btnAdicionarPizza;
    private JButton btnRemoverPizza;
    private JButton btnFinalizarPedido;
    private JTable tabelaPizzas;
    private DefaultTableModel modeloTabela;
    private PedidoController controller;
    private JLabel lblPrecoTotal;
    private JComboBox<String> cmbPedidos;
    private JButton btnCarregarPedido;

    public TelaPedidos() {
        setTitle("Fazer Pedido");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new PedidoController(this);

        JPanel painelCliente = new JPanel(new GridLayout(2, 3, 10, 10));

        painelCliente.add(new JLabel("Telefone do Cliente:"));
        txtTelefone = new JTextField();
        painelCliente.add(txtTelefone);
        btnBuscarCliente = new JButton("Buscar Cliente");
        painelCliente.add(btnBuscarCliente);
        painelCliente.add(new JLabel("Pedidos Existentes:"));
        cmbPedidos = new JComboBox<>();
        painelCliente.add(cmbPedidos);
        btnCarregarPedido = new JButton("Carregar Pedido");
        painelCliente.add(btnCarregarPedido);

        JPanel painelInfoCliente = new JPanel();
        lblCliente = new JLabel("Cliente não selecionado");
        painelInfoCliente.add(lblCliente);

        JPanel painelPizza = new JPanel(new GridLayout(6, 2, 10, 10));

        painelPizza.add(new JLabel("Forma:"));
        cmbForma = new JComboBox<>(FormaPizza.values());
        painelPizza.add(cmbForma);

        painelPizza.add(new JLabel("Escolha a medida:"));
        cmbMedida = new JComboBox<>(new String[]{"Tamanho (lado/raio)", "Área (cm²)"});
        painelPizza.add(cmbMedida);

        lblDimensao = new JLabel("Tamanho (cm):");
        painelPizza.add(lblDimensao);
        spnDimensao = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
        painelPizza.add(spnDimensao);

        painelPizza.add(new JLabel("Sabor 1:"));
        cmbSabor1 = new JComboBox<>();
        painelPizza.add(cmbSabor1);
        painelPizza.add(new JLabel("Sabor 2 (opcional):"));
        cmbSabor2 = new JComboBox<>();
        painelPizza.add(cmbSabor2);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        btnAdicionarPizza = new JButton("Adicionar Pizza");
        btnRemoverPizza = new JButton("Remover Pizza");
        btnFinalizarPedido = new JButton("Finalizar Pedido");
        lblPrecoTotal = new JLabel("Total: R$ 0,00");
        painelBotoes.add(btnAdicionarPizza);
        painelBotoes.add(btnRemoverPizza);
        painelBotoes.add(btnFinalizarPedido);
        painelBotoes.add(lblPrecoTotal);

        modeloTabela = new DefaultTableModel(new Object[]{"Forma", "Sabor 1", "Sabor 2", "Preço (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPizzas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaPizzas);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));

        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.add(painelCliente, BorderLayout.NORTH);
        painelSuperior.add(painelInfoCliente, BorderLayout.SOUTH);

        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.add(painelPizza, BorderLayout.NORTH);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        javax.swing.border.EmptyBorder borda = new javax.swing.border.EmptyBorder(10, 10, 10, 10);
        painelPrincipal.setBorder(borda);
        painelPizza.setBorder(borda);

        add(painelPrincipal);

        configurarListeners();
        controller.listarTodosSabores();
    }

    private void configurarListeners() {
        btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buscarClientePorTelefone();
            }
        });

        btnAdicionarPizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.adicionarPizzaAoPedido();
            }
        });

        btnRemoverPizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removerPizzaDoPedido();
            }
        });

        btnFinalizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.finalizarPedido();
            }
        });

        btnCarregarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buscarPedidoPorId();
            }
        });

        cmbMedida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarLabelDimensao();
            }
        });

        cmbForma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarLabelDimensao();
            }
        });
    }

    private void atualizarLabelDimensao() {
        FormaPizza forma = (FormaPizza) cmbForma.getSelectedItem();
        boolean isPorArea = cmbMedida.getSelectedIndex() == 1;

        if (isPorArea) {
            lblDimensao.setText("Área (cm²):");
            spnDimensao.setModel(new SpinnerNumberModel(400, 100, 1600, 50));
        } else {
            String medida;
            int min, max, valorInicial;

            switch (forma) {
                case CIRCULAR:
                    medida = "Raio";
                    min = 7;
                    max = 23;
                    valorInicial = 15;
                    break;
                case TRIANGULO:
                    medida = "Lado";
                    min = 10;
                    max = 60;
                    valorInicial = 20;
                    break;
                default:
                    medida = "Lado";
                    min = 10;
                    max = 40;
                    valorInicial = 20;
                    break;
            }

            lblDimensao.setText(medida + " (cm):");
            spnDimensao.setModel(new SpinnerNumberModel(valorInicial, min, max, 1));
        }
    }


    public void atualizarSabores(List<Sabor> sabores) {
        cmbSabor1.removeAllItems();
        cmbSabor2.removeAllItems();
        cmbSabor2.addItem(null);

        if (sabores != null) {
            for (Sabor sabor : sabores) {
                cmbSabor1.addItem(sabor);
                cmbSabor2.addItem(sabor);
            }
        }
    }


    public void atualizarLabelCliente(String texto) {
        lblCliente.setText(texto);
    }

    public void atualizarTabelaPizzas(List<Pizza> pizzas) {
        modeloTabela.setRowCount(0);
        for (Pizza pizza : pizzas) {
            modeloTabela.addRow(new Object[]{
                pizza.getForma().getClass().getSimpleName(),
                pizza.getSabor1().getNome(),
                pizza.getSabor2() != null ? pizza.getSabor2().getNome() : "-",
                String.format("R$ %.2f", pizza.getPrecoTotal())
            });
        }
    }

    public void atualizarPrecoTotal(double total) {
        lblPrecoTotal.setText(String.format("Total: R$ %.2f", total));
    }

    public void atualizarComboPedidos(List<Pedido> pedidos) {
        cmbPedidos.removeAllItems();
        if (pedidos != null) {
            for (Pedido pedido : pedidos) {
                cmbPedidos.addItem(String.format("Pedido #%d - R$ %.2f - %s", 
                    pedido.getId(), 
                    pedido.getPrecoTotal(),
                    pedido.getEstado()));
            }
        }
    }

    public void limparPedido() {
        modeloTabela.setRowCount(0);
        lblPrecoTotal.setText("Total: R$ 0,00");
        lblCliente.setText("Cliente não selecionado");
        txtTelefone.setText("");
        cmbPedidos.removeAllItems();
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public String getTelefoneCliente() {
        return txtTelefone.getText().trim();
    }

    public FormaPizza getFormaPizza() {
        return (FormaPizza) cmbForma.getSelectedItem();
    }

    public boolean isPorArea() {
        return cmbMedida.getSelectedIndex() == 1;
    }

    public double getDimensao() {
        return ((Number) spnDimensao.getValue()).doubleValue();
    }

    public Sabor getSabor1() {
        return (Sabor) cmbSabor1.getSelectedItem();
    }

    public Sabor getSabor2() {
        return (Sabor) cmbSabor2.getSelectedItem();
    }

    public int getLinhaPizzaSelecionada() {
        return tabelaPizzas.getSelectedRow();
    }

    public int getIdPedidoSelecionado() {
        String pedidoSelecionado = (String) cmbPedidos.getSelectedItem();
        if (pedidoSelecionado != null) {
            return Integer.parseInt(pedidoSelecionado.split("#")[1].split(" ")[0]);
        }
        return -1;
    }
} 