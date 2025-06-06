package View;

import Model.Sabor;
import Controller.SaborController;
import enums.TipoPizza;
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

        controller.listarTodosSabores();
    }

    private void configurarListeners() {
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.cadastrarSabor();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.atualizarSabor();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.excluirSaboresSelecionados();
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

    public void carregarSabores(List<Sabor> sabores) {
            modeloTabela.setRowCount(0);
            if (sabores != null) {
                for (Sabor sabor : sabores) {
                    modeloTabela.addRow(new Object[]{
                        sabor.getId(),
                        sabor.getNome(),
                        sabor.getTipo()
                    });
                }
            }
    }

    public Sabor getSaborForm() {

            String nome = txtNome.getText().trim();
            TipoPizza tipo = (TipoPizza) cmbTipo.getSelectedItem();

            if (nome.isEmpty()) {
                mostrarErro("O nome é obrigatório!");
                return null;
            }

            return new Sabor(nome, tipo);
    }

    public JTable getTabelaSabores() {
        return tabelaSabores;
    }

    public Sabor getSaborFormComId() {
        int linhaSelecionada = tabelaSabores.getSelectedRow();
        if (linhaSelecionada == -1) {
            mostrarErro("Selecione um sabor!");
            return null;
        }

        int id = (int) tabelaSabores.getValueAt(linhaSelecionada, 0);
        Sabor sabor = getSaborForm();
        if (sabor != null) {
            sabor.setId(id);
        }
        return sabor;
    }

    public List<Sabor> getSaboresSelecionados() {
        int[] linhas = this.getTabelaSabores().getSelectedRows();
        if (linhas.length == 0) {
            mostrarErro("Selecione ao menos um sabor para excluir!");
            return null;
        }

        List<Sabor> selecionados = new ArrayList<>();
        for (int linha : linhas) {
            int id = (int) tabelaSabores.getValueAt(linha, 0);
            String nome = (String) tabelaSabores.getValueAt(linha, 1);
            TipoPizza tipo = TipoPizza.valueOf(tabelaSabores.getValueAt(linha, 2).toString());
            selecionados.add(new Sabor(id, nome, tipo));
        }
        return selecionados;
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