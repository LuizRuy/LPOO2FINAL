package Controller;

import Model.Pedido;
import Model.dao.PedidoDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaStatusPedidos;
import enums.EstadoPedido;
import java.util.List;

public class StatusPedidoController {
    private final TelaStatusPedidos tela;
    private final PedidoDao pedidoDao;

    public StatusPedidoController(TelaStatusPedidos tela) {
        this.tela = tela;
        this.pedidoDao = DaoFactory.getPedidoDao(DaoType.SQL);
    }

    public List<Pedido> listarTodosPedidos() {
        try {
            return pedidoDao.listarTodos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar pedidos: " + e.getMessage());
            return null;
        }
    }

    public void atualizarEstadoPedido(Pedido pedido, EstadoPedido novoEstado) {
        try {
            Pedido pedidoCompleto = pedidoDao.buscarPorId(pedido.getId());
            if (pedidoCompleto != null) {
                pedidoCompleto.setEstado(novoEstado);
                pedidoDao.atualizar(pedidoCompleto);
                tela.atualizarTabela();
            } else {
                tela.mostrarErro("Pedido não encontrado!");
            }
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar estado do pedido: " + e.getMessage());
        }
    }

    public Pedido buscarPedidoPorId(int id) {
        try {
            return pedidoDao.buscarPorId(id);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao buscar pedido: " + e.getMessage());
            return null;
        }
    }
} 