package View;

import Model.Cliente;
import Controller.ClienteController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
    public JTable tabelaClientes;
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

        // Adiciona validação para nome e sobrenome (apenas letras e espaço)
        txtNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        txtSobrenome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        // Adiciona validação para telefone (apenas números)
        txtTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

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

        controller.atualizarTabela();
        tabelaClientes.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public JTable getTabelaClientes() {
        return tabelaClientes;
    }

    public Cliente getClienteForm() {
        String nome = txtNome.getText().trim();
        String sobrenome = txtSobrenome.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios!");
            return null;
        }

        return new Cliente(0, nome, sobrenome, telefone);
    }

    public Cliente getClienteFormComId() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        if (linhaSelecionada == -1) {
            mostrarErro("Selecione um cliente!");
            return null;
        }

        int id = (int) tabelaClientes.getValueAt(linhaSelecionada, 0);
        Cliente cliente = getClienteForm();
        if (cliente != null) {
            cliente.setId(id);
        }
        return cliente;
    }

    public List<Cliente> getClientesSelecionados() {
        int[] linhas = this.getTabelaClientes().getSelectedRows();
        if (linhas.length == 0) {
            mostrarErro("Selecione ao menos um cliente para excluir!");
            return null;
        }

        List<Cliente> selecionados = new ArrayList();
        for (int linha : linhas) {
            int id = (int) tabelaClientes.getValueAt(linha, 0);
            String nome = (String) tabelaClientes.getValueAt(linha, 1);
            String sobrenome = (String) tabelaClientes.getValueAt(linha, 2);
            String telefone = (String) tabelaClientes.getValueAt(linha, 3);
            selecionados.add(new Cliente(id, nome, sobrenome, telefone));
        }
        return selecionados;
    }


    public String getTelefoneBusca() {
        return txtTelefone.getText().trim();
    }

    public void mostrarClientes(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente cliente : clientes) {
            modeloTabela.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getSobrenome(),
                    cliente.getTelefone()
            });
        }
    }

    public void mostrarCliente(Cliente cliente) {
        modeloTabela.setRowCount(0);
        modeloTabela.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getSobrenome(),
                cliente.getTelefone()
        });
    }

    public void limparCampos() {
        txtNome.setText("");
        txtSobrenome.setText("");
        txtTelefone.setText("");
        tabelaClientes.clearSelection();
    }

    public void mostrarMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarErro(String erro) {
        JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarErroClienteDuplicado(String telefone) {
        mostrarErro("Telefone " + telefone + " já cadastrado!");
    }

    private void configurarListeners() {
        btnCadastrar.addActionListener(e -> controller.cadastrar());
        btnAtualizar.addActionListener(e -> controller.atualizar());
        btnExcluir.addActionListener(e -> controller.excluir());
        btnBuscar.addActionListener(e -> controller.buscar());

        tabelaClientes.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaClientes.getSelectedRow() != -1) {
                int linha = tabelaClientes.getSelectedRow();
                txtNome.setText((String) tabelaClientes.getValueAt(linha, 1));
                txtSobrenome.setText((String) tabelaClientes.getValueAt(linha, 2));
                txtTelefone.setText((String) tabelaClientes.getValueAt(linha, 3));
            }
        });
    }
} 