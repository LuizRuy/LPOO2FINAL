package Controller;

import Model.Cliente;
import Model.dao.ClienteDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaClientes;
import java.util.List;

// Controller: ClienteController.java
public class ClienteController {
    private final TelaClientes tela;
    private final ClienteDao clienteDao;

    public ClienteController(TelaClientes tela) {
        this.tela = tela;
        this.clienteDao = DaoFactory.getClienteDao(DaoType.SQL);
    }

    public void cadastrar() {
        Cliente cliente = tela.getClienteForm();
        if (cliente == null) return;

        try {
            clienteDao.inserir(cliente);
            atualizarTabela();
            tela.limparCampos();
            tela.mostrarMensagem("Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Duplicate entry") && msg.contains("cliente.telefone")) {
                tela.mostrarErroClienteDuplicado(cliente.getTelefone());
            } else {
                tela.mostrarErro("Erro ao cadastrar cliente: " + e.getMessage());
            }
        }
    }

    public void atualizar() {
        Cliente cliente = tela.getClienteFormComId();
        if (cliente == null) return;

        try {
            clienteDao.atualizar(cliente);
            atualizarTabela();
            tela.limparCampos();
            tela.mostrarMensagem("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public void excluir() {
        try {
            List<Cliente> listaParaExcluir = tela.getClientesSelecionados();
            clienteDao.excluir(listaParaExcluir);
            tela.limparCampos();
            atualizarTabela();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    public void buscar() {
        String telefone = tela.getTelefoneBusca();
        String sobrenome = tela.getSobrenomeBusca();
        if (telefone.isEmpty()) {
            try {
                List<Cliente> clientes = clienteDao.buscarPorSobrenome(sobrenome);
                if (clientes != null) {
                    tela.mostrarClientes(clientes);
                } else {
                    tela.mostrarErro("Cliente não encontrado!");
                }
            } catch (Exception e) {
                tela.mostrarErro("Erro ao buscar cliente: " + e.getMessage());
            }
        }

        if(sobrenome.isEmpty()) {
            try {
                Cliente cliente = clienteDao.buscarPorTelefone(telefone);
                if (cliente != null) {
                    tela.mostrarCliente(cliente);
                } else {
                    tela.mostrarErro("Cliente não encontrado!");
                }
            } catch (Exception e) {
                tela.mostrarErro("Erro ao buscar cliente: " + e.getMessage());
            }
        }
    }

    public void atualizarTabela() {
        try {
            List<Cliente> clientes = clienteDao.listarTodos();
            tela.mostrarClientes(clientes);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar clientes: " + e.getMessage());
        }
    }
}
