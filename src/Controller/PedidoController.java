package Controller;

import Model.Cliente;
import Model.Pedido;
import Model.Pizza;
import Model.Sabor;
import Model.dao.ClienteDao;
import Model.dao.PedidoDao;
import Model.dao.PizzaDao;
import Model.dao.SaborDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaPedidos;
import enums.EstadoPedido;
import java.util.List;

public class PedidoController {
    private final PedidoDao pedidoDao;
    private final ClienteDao clienteDao;
    private final PizzaDao pizzaDao;
    private final SaborDao saborDao;
    private final TelaPedidos view;

    public PedidoController(TelaPedidos view) {
        this.view = view;
        this.pedidoDao = DaoFactory.getPedidoDao(DaoType.SQL);
        this.clienteDao = DaoFactory.getClienteDao(DaoType.SQL);
        this.pizzaDao = DaoFactory.getPizzaDao(DaoType.SQL);
        this.saborDao = DaoFactory.getSaborDao(DaoType.SQL);
    }

    public Cliente buscarClientePorTelefone(String telefone) {
        try {
            return clienteDao.buscarPorTelefone(telefone);
        } catch (Exception e) {
            view.mostrarErro("Erro ao buscar cliente: " + e.getMessage());
            return null;
        }
    }

    public List<Sabor> listarTodosSabores() {
        try {
            return saborDao.listarTodos();
        } catch (Exception e) {
            view.mostrarErro("Erro ao listar sabores: " + e.getMessage());
            return null;
        }
    }

    public List<Pedido> listarTodosPedidos() {
        try {
            return pedidoDao.listarTodos();
        } catch (Exception e) {
            view.mostrarErro("Erro ao listar pedidos: " + e.getMessage());
            return null;
        }
    }

    public Pedido buscarPedidoPorId(int id) {
        try {
            return pedidoDao.buscarPorId(id);
        } catch (Exception e) {
            view.mostrarErro("Erro ao buscar pedido: " + e.getMessage());
            return null;
        }
    }

    public void adicionarPizzaAoPedido(Pedido pedido, Pizza pizza) {
        try {
            pizzaDao.inserir(pizza);
        } catch (Exception e) {
            view.mostrarErro("Erro ao adicionar pizza: " + e.getMessage());
        }
    }

    public void removerPizzaDoPedido(Pedido pedido, Pizza pizza) {
        try {
            pizzaDao.excluir(pizza.getId());
        } catch (Exception e) {
            view.mostrarErro("Erro ao remover pizza: " + e.getMessage());
        }
    }

    public void finalizarPedido(Pedido pedido) {
        try {
            // Para novo pedido, insere todas as pizzas
            if (pedido.getId() == 0) {
                for (Pizza pizza : pedido.getPizzas()) {
                    if (pizza.getId() == 0) {
                        pizzaDao.inserir(pizza);
                    }
                }
            }

            // Salvar ou atualizar no banco
            if (pedido.getId() == 0) {
                pedidoDao.inserir(pedido);
            } else {
                pedidoDao.atualizar(pedido);
            }

            view.mostrarMensagem("Pedido " + 
                (pedido.getId() == 0 ? "finalizado" : "atualizado") + " com sucesso!");
        } catch (Exception e) {
            view.mostrarErro("Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    public void atualizarEstadoPedido(Pedido pedido, EstadoPedido novoEstado) {
        try {
            pedido.setEstado(novoEstado);
            pedidoDao.atualizar(pedido);
            view.atualizarTabelaPizzas();
        } catch (Exception e) {
            view.mostrarErro("Erro ao atualizar estado do pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listarPedidosPorCliente(Cliente cliente) {
        try {
            return pedidoDao.buscarPorCliente(cliente);
        } catch (Exception e) {
            view.mostrarErro("Erro ao listar pedidos do cliente: " + e.getMessage());
            return null;
        }
    }
} 