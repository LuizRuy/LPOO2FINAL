package View;

import Model.Sabor;
import Controller.SaborController;
import enums.TipoPizza;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TelaSabores extends JFrame {
    private JTextField txtNome;
    private JComboBox<TipoPizza> cmbTipo;
    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JTable tabelaSabores;
    private DefaultTableModel modeloTabela;
    private SaborController controller;

    public TelaSabores() {
        setTitle("Gerenciamento de Sabores");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new SaborController(this);

        JPanel painelEntrada = new JPanel(new GridLayout(3, 2, 10, 10));
        painelEntrada.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelEntrada.add(txtNome);
        painelEntrada.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(TipoPizza.values());
        painelEntrada.add(cmbTipo);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 10, 10));
        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaSabores = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaSabores);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        configurarListeners();

        carregarSabores();
    }

    private void configurarListeners() {
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarSabor();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarSabor();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirSabor();
            }
        });

        tabelaSabores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaSabores.getSelectedRow() != -1) {
                int linhaSelecionada = tabelaSabores.getSelectedRow();
                txtNome.setText(tabelaSabores.getValueAt(linhaSelecionada, 1).toString());
                cmbTipo.setSelectedItem(TipoPizza.valueOf(tabelaSabores.getValueAt(linhaSelecionada, 2).toString()));
            }
        });
    }

    private void carregarSabores() {
        try {
            modeloTabela.setRowCount(0);
            List<Sabor> sabores = controller.listarTodosSabores();
            if (sabores != null) {
                for (Sabor sabor : sabores) {
                    modeloTabela.addRow(new Object[]{
                        sabor.getId(),
                        sabor.getNome(),
                        sabor.getTipo()
                    });
                }
            }
        } catch (Exception e) {
            mostrarErro("Erro ao carregar sabores: " + e.getMessage());
        }
    }

    private void cadastrarSabor() {
        try {
            String nome = txtNome.getText().trim();
            TipoPizza tipo = (TipoPizza) cmbTipo.getSelectedItem();

            if (nome.isEmpty()) {
                mostrarErro("O nome é obrigatório!");
                return;
            }

            controller.cadastrarSabor(nome, tipo);
            carregarSabores();
            limparCampos();
            mostrarMensagem("Sabor cadastrado com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao cadastrar sabor: " + e.getMessage());
        }
    }

    private void atualizarSabor() {
        try {
            int linhaSelecionada = tabelaSabores.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Selecione um sabor para atualizar!");
                return;
            }

            int id = (int) tabelaSabores.getValueAt(linhaSelecionada, 0);
            String nome = txtNome.getText().trim();
            TipoPizza tipo = (TipoPizza) cmbTipo.getSelectedItem();

            if (nome.isEmpty()) {
                mostrarErro("O nome é obrigatório!");
                return;
            }

            controller.atualizarSabor(id, nome, tipo);
            carregarSabores();
            limparCampos();
            mostrarMensagem("Sabor atualizado com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao atualizar sabor: " + e.getMessage());
        }
    }

    private void excluirSabor() {
        try {
            int linhaSelecionada = tabelaSabores.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarErro("Selecione um sabor para excluir!");
                return;
            }

            int id = (int) tabelaSabores.getValueAt(linhaSelecionada, 0);
            controller.excluirSabor(id);
            carregarSabores();
            limparCampos();
            mostrarMensagem("Sabor excluído com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao excluir sabor: " + e.getMessage());
        }
    }

    public void atualizarTabela() {
        carregarSabores();
    }

    public void limparCampos() {
        txtNome.setText("");
        cmbTipo.setSelectedIndex(0);
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
} 