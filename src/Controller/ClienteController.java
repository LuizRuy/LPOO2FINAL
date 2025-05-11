package Controller;

import Model.Cliente;
import Model.dao.ClienteDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaClientes;
import java.util.List;

public class ClienteController {
    private final TelaClientes tela;
    private final ClienteDao clienteDao;

    public ClienteController(TelaClientes tela) {
        this.tela = tela;
        this.clienteDao = DaoFactory.getClienteDao(DaoType.SQL);
    }

    public boolean cadastrarCliente(String nome, String sobrenome, String telefone) {
        try {
            Cliente cliente = new Cliente(0, nome, sobrenome, telefone);
            clienteDao.inserir(cliente);
            tela.atualizarTabela();
            tela.limparCampos();
            return true;
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Duplicate entry") && msg.contains("cliente.telefone")) {
                tela.mostrarErroClienteDuplicado(telefone);
            } else {
                tela.mostrarErro("Erro ao cadastrar cliente: " + e.getMessage());
            }
            return false;
        }
    }

    public void atualizarCliente(int id, String nome, String sobrenome, String telefone) {
        try {
            Cliente cliente = new Cliente(id, nome, sobrenome, telefone);
            clienteDao.atualizar(cliente);
            tela.atualizarTabela();
            tela.limparCampos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public void excluirCliente(int id) {
        try {
            clienteDao.excluir(id);
            tela.atualizarTabela();
            tela.limparCampos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    public Cliente buscarClientePorTelefone(String telefone) {
        try {
            return clienteDao.buscarPorTelefone(telefone);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao buscar cliente: " + e.getMessage());
            return null;
        }
    }

    public List<Cliente> listarTodosClientes() {
        try {
            return clienteDao.listarTodos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar clientes: " + e.getMessage());
            return null;
        }
    }
} 