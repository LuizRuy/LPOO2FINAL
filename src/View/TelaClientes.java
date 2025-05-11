package View;

import Model.Cliente;
import Controller.ClienteController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TelaClientes extends JFrame {
    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtTelefone;
    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnBuscar;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private ClienteController controller;

    public TelaClientes() {
        setTitle("Gerenciamento de Clientes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new ClienteController(this);

        JPanel painelEntrada = new JPanel(new GridLayout(4, 2, 10, 10));
        painelEntrada.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelEntrada.add(txtNome);
        painelEntrada.add(new JLabel("Sobrenome:"));
        txtSobrenome = new JTextField();
        painelEntrada.add(txtSobrenome);
        painelEntrada.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelEntrada.add(txtTelefone);
        painelEntrada.add(new JLabel(""));
        btnBuscar = new JButton("Buscar");
        painelEntrada.add(btnBuscar);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 10, 10));
        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Sobrenome", "Telefone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        configurarListeners();

        carregarClientes();
    }

    private void configurarListeners() {
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCliente();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirCliente();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });

        tabelaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaClientes.getSelectedRow() != -1) {
                int linhaSelecionada = tabelaClientes.getSelectedRow();
                txtNome.setText(tabelaClientes.getValueAt(linhaSelecionada, 1).toString());
                txtSobrenome.setText(tabelaClientes.getValueAt(linhaSelecionada, 2).toString());
                txtTelefone.setText(tabelaClientes.getValueAt(linhaSelecionada, 3).toString());
            }
        });
    }

    private void carregarClientes() {
        try {
            modeloTabela.setRowCount(0);
            List<Cliente> clientes = controller.listarTodosClientes();
            if (clientes != null) {
                for (Cliente cliente : clientes) {
                    modeloTabela.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getSobrenome(),
                        cliente.getTelefone()
                    });
                }
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void cadastrarCliente() {
        try {
            String nome = txtNome.getText().trim();
            String sobrenome = txtSobrenome.getText().trim();
            String telefone = txtTelefone.getText().trim();

            if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios!");
                return;
            }

            boolean sucesso = controller.cadastrarCliente(nome, sobrenome, telefone);
            if (sucesso) {
                carregarClientes();
                limparCampos();
                mostrarMensagem("Cliente cadastrado com sucesso!");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private void atualizarCliente() {
        try {
            int linhaSelecionada = tabelaClientes.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Selecione um cliente para atualizar!");
                return;
            }

            int id = (int) tabelaClientes.getValueAt(linhaSelecionada, 0);
            String nome = txtNome.getText().trim();
            String sobrenome = txtSobrenome.getText().trim();
            String telefone = txtTelefone.getText().trim();

            if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
                mostrarErro("Todos os campos são obrigatórios!");
                return;
            }

            controller.atualizarCliente(id, nome, sobrenome, telefone);
            carregarClientes();
            limparCampos();
            mostrarMensagem("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    private void excluirCliente() {
        try {
            int linhaSelecionada = tabelaClientes.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Selecione um cliente para excluir!");
                return;
            }

            int id = (int) tabelaClientes.getValueAt(linhaSelecionada, 0);
            controller.excluirCliente(id);
            carregarClientes();
            limparCampos();
            mostrarMensagem("Cliente excluído com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        try {
            String telefone = txtTelefone.getText().trim();
            if (telefone.isEmpty()) {
                carregarClientes();
                return;
            }

            Cliente cliente = controller.buscarClientePorTelefone(telefone);
            if (cliente != null) {
                modeloTabela.setRowCount(0);
                modeloTabela.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getSobrenome(),
                    cliente.getTelefone()
                });
            } else {
                mostrarErro("Cliente não encontrado!");
            }
        } catch (Exception e) {
            mostrarErro("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    public void atualizarTabela() {
        carregarClientes();
    }

    public void limparCampos() {
        txtNome.setText("");
        txtSobrenome.setText("");
        txtTelefone.setText("");
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarErroClienteDuplicado(String telefone) {
        JOptionPane.showMessageDialog(
            this,
            "<html><b>Já existe um cliente cadastrado com o telefone:</b><br><br><font color='blue'>" + telefone + "</font><br><br>Por favor, utilize outro número ou edite o cliente existente.</html>",
            "Cliente já cadastrado",
            JOptionPane.WARNING_MESSAGE
        );
    }
} 