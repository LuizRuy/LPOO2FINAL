package View;

import Model.Cliente;
import Model.Circulo;
import Model.Forma;
import Model.Pedido;
import Model.Pizza;
import Model.Quadrado;
import Model.Sabor;
import Model.Triangulo;
import Controller.PedidoController;
import enums.FormaPizza;
import enums.EstadoPedido;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
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
    private Cliente clienteSelecionado;
    private List<Pizza> pizzasDoPedido;
    private JLabel lblPrecoTotal;
    private JTextField txtNumeroPedido;
    private JButton btnCarregarPedido;
    private Pedido pedidoAtual;
    private JComboBox<String> cmbPedidos;

    public TelaPedidos() {
        setTitle("Fazer Pedido");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new PedidoController(this);
        pizzasDoPedido = new ArrayList<>();

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

        carregarSabores();

        carregarPedidosExistentes();
    }

    private void configurarListeners() {
        btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });

        btnAdicionarPizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPizza();
            }
        });

        btnRemoverPizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerPizza();
            }
        });

        btnFinalizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarPedido();
            }
        });

        btnCarregarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarPedidoSelecionado();
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
            String medida = forma == FormaPizza.CIRCULAR ? "Raio" : "Lado";
            lblDimensao.setText(medida + " (cm):");
            
            int min = forma == FormaPizza.CIRCULAR ? 7 : 10;
            int max = forma == FormaPizza.CIRCULAR ? 23 : 40;
            int valorInicial = forma == FormaPizza.CIRCULAR ? 15 : 20;
            spnDimensao.setModel(new SpinnerNumberModel(valorInicial, min, max, 1));
        }
    }

    private void buscarCliente() {
        try {
            String telefone = txtTelefone.getText().trim();
            if (telefone.isEmpty()) {
                mostrarErro("Digite o telefone do cliente!");
                return;
            }

            clienteSelecionado = controller.buscarClientePorTelefone(telefone);
            if (clienteSelecionado != null) {
                lblCliente.setText("Cliente: " + clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
            } else {
                mostrarErro("Cliente não encontrado!");
                lblCliente.setText("Cliente não selecionado");
            }
            carregarPedidosExistentes();
        } catch (Exception e) {
            mostrarErro("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    private void carregarSabores() {
        try {
            List<Sabor> sabores = controller.listarTodosSabores();

            cmbSabor1.removeAllItems();
            cmbSabor2.removeAllItems();

            cmbSabor2.addItem(null);
            
            for (Sabor sabor : sabores) {
                cmbSabor1.addItem(sabor);
                cmbSabor2.addItem(sabor);
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar sabores: " + e.getMessage());
        }
    }

    private void adicionarPizza() {
        try {
            if (clienteSelecionado == null) {
                mostrarErro("Selecione um cliente primeiro!");
                return;
            }

            FormaPizza forma = (FormaPizza) cmbForma.getSelectedItem();
            boolean isPorArea = cmbMedida.getSelectedIndex() == 1;
            double dimensao = ((Number) spnDimensao.getValue()).doubleValue();
            Sabor sabor1 = (Sabor) cmbSabor1.getSelectedItem();
            Sabor sabor2 = (Sabor) cmbSabor2.getSelectedItem();

            if (sabor1 == null) {
                mostrarErro("Selecione pelo menos um sabor!");
                return;
            }

            double areaOriginal = dimensao;
            Forma formaPizza;
            if (isPorArea) {
                double dimensaoReal = Math.sqrt(dimensao);
                if (forma == FormaPizza.CIRCULAR) {
                    dimensaoReal = Math.sqrt(dimensao / Math.PI);
                }
                dimensao = dimensaoReal;

                String tipoMedida = forma == FormaPizza.CIRCULAR ? "raio" : "lado";
                String mensagem = String.format("Para uma %s com área de %.1f cm²:\n%s calculado: %.1f cm", 
                    forma.toString().toLowerCase(), 
                    areaOriginal,
                    tipoMedida.substring(0, 1).toUpperCase() + tipoMedida.substring(1),
                    dimensao);
                mostrarMensagem(mensagem);
            }

            switch (forma) {
                case CIRCULAR:
                    if (dimensao < 7 || dimensao > 23) {
                        mostrarErro("O raio deve estar entre 7 e 23 cm!");
                        return;
                    }
                    formaPizza = new Circulo(dimensao);
                    break;
                case QUADRADO:
                    if (dimensao < 10 || dimensao > 40) {
                        mostrarErro("O lado deve estar entre 10 e 40 cm!");
                        return;
                    }
                    formaPizza = new Quadrado(dimensao);
                    break;
                case TRIANGULO:
                    formaPizza = new Triangulo(dimensao);
                    break;
                default:
                    throw new IllegalArgumentException("Forma inválida");
            }

            Pizza pizza = new Pizza(0, formaPizza, sabor1);
            if (sabor2 != null) {
                pizza = new Pizza(0, formaPizza, sabor1, sabor2);
            }

            if (pedidoAtual != null && pedidoAtual.getId() != 0) {
                controller.adicionarPizzaAoPedido(pedidoAtual, pizza);
            }

            pizzasDoPedido.add(pizza);

            modeloTabela.addRow(new Object[]{
                forma,
                sabor1.getNome(),
                sabor2 != null ? sabor2.getNome() : "-",
                String.format("%.2f", pizza.getPrecoTotal())
            });
            
            atualizarPrecoTotal();
        } catch (Exception e) {
            mostrarErro("Erro ao adicionar pizza: " + e.getMessage());
        }
    }

    private void removerPizza() {
        int linhaSelecionada = tabelaPizzas.getSelectedRow();
        if (linhaSelecionada == -1) {
            mostrarErro("Selecione uma pizza para remover!");
            return;
        }

        Pizza pizza = pizzasDoPedido.get(linhaSelecionada);
        if (pedidoAtual != null && pedidoAtual.getId() != 0) {
            controller.removerPizzaDoPedido(pedidoAtual, pizza);
        }

        pizzasDoPedido.remove(linhaSelecionada);
        modeloTabela.removeRow(linhaSelecionada);
        atualizarPrecoTotal();
    }

    private void carregarPedidosExistentes() {
        try {
            cmbPedidos.removeAllItems();
            cmbPedidos.addItem("Selecione um pedido...");
            if (clienteSelecionado == null) {
                return;
            }
            List<Pedido> pedidos = controller.listarPedidosPorCliente(clienteSelecionado);
            if (pedidos != null) {
                for (Pedido pedido : pedidos) {
                    String itemPedido = String.format("Pedido #%d - R$ %.2f - %s", 
                        pedido.getId(), 
                        pedido.getPrecoTotal(),
                        pedido.getEstado());
                    cmbPedidos.addItem(itemPedido);
                }
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void carregarPedidoSelecionado() {
        try {
            int selectedIndex = cmbPedidos.getSelectedIndex();
            if (selectedIndex <= 0) {
                mostrarErro("Selecione um pedido!");
                return;
            }

            String selectedItem = (String) cmbPedidos.getSelectedItem();
            int pedidoId = Integer.parseInt(selectedItem.split("#")[1].split(" ")[0]);
            
            pedidoAtual = controller.buscarPedidoPorId(pedidoId);

            if (pedidoAtual == null) {
                mostrarErro("Pedido não encontrado!");
                return;
            }

            if (pedidoAtual.getEstado() != EstadoPedido.ABERTO) {
                mostrarErro("Apenas pedidos em estado ABERTO podem ser alterados!");
                return;
            }

            clienteSelecionado = pedidoAtual.getCliente();
            pizzasDoPedido = pedidoAtual.getPizzas();

            lblCliente.setText("Cliente: " + clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
            txtTelefone.setText(clienteSelecionado.getTelefone());
            atualizarTabelaPizzas();
            atualizarPrecoTotal();
            
            mostrarMensagem("Pedido carregado com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao carregar pedido: " + e.getMessage());
        }
    }

    private void finalizarPedido() {
        try {
            if (clienteSelecionado == null) {
                mostrarErro("Selecione um cliente primeiro!");
                return;
            }

            if (pizzasDoPedido.isEmpty()) {
                mostrarErro("Adicione pelo menos uma pizza ao pedido!");
                return;
            }

            if (pedidoAtual == null) {
                pedidoAtual = new Pedido(0, clienteSelecionado);
                pedidoAtual.setEstado(EstadoPedido.ABERTO);
                pedidoAtual.setPizzas(pizzasDoPedido);
            }

            controller.finalizarPedido(pedidoAtual);

            carregarPedidosExistentes();
            limparPedido();
        } catch (Exception e) {
            mostrarErro("Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    public void atualizarTabelaPizzas() {
        modeloTabela.setRowCount(0);
        for (Pizza pizza : pizzasDoPedido) {
            modeloTabela.addRow(new Object[]{
                pizza.getTipoForma(),
                pizza.getSabor1().getNome(),
                pizza.getSabor2() != null ? pizza.getSabor2().getNome() : "-",
                String.format("%.2f", pizza.getPrecoTotal())
            });
        }
    }

    public void limparPedido() {
        clienteSelecionado = null;
        pizzasDoPedido.clear();
        pedidoAtual = null;
        atualizarTabelaPizzas();
        atualizarPrecoTotal();
        txtTelefone.setText("");
        cmbPedidos.setSelectedIndex(0);
        lblCliente.setText("Cliente não selecionado");
    }

    private void atualizarPrecoTotal() {
        double total = 0.0;
        for (Pizza pizza : pizzasDoPedido) {
            total += pizza.getPrecoTotal();
        }
        lblPrecoTotal.setText(String.format("Total: R$ %.2f", total));
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
} 