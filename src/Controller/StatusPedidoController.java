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

    public void listarTodosPedidos() {
        try {
            List<Pedido> pedidos = pedidoDao.listarTodos();
            tela.carregarPedidos(pedidos);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    public void atualizarStatus() {
        Pedido pedido = tela.atualizarStatus();
        if (pedido == null) return;

        try {
            Pedido pedidoCompleto = pedidoDao.buscarPorId(pedido.getId());
            if (pedidoCompleto == null) {
                tela.mostrarErro("Pedido não encontrado!");
                return;
            }

            pedidoCompleto.setEstado(pedido.getEstado());
            pedidoDao.atualizar(pedidoCompleto);

            listarTodosPedidos();
            tela.mostrarMensagem("Status atualizado com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar status: " + e.getMessage());
        }
    }

    public void buscarPedidoPorId() {
        try {
            Pedido pedido = tela.atualizarStatus();
            Pedido pedidoCompleto = pedidoDao.buscarPorId(pedido.getId());
        } catch (Exception e) {
            tela.mostrarErro("Erro ao buscar pedido: " + e.getMessage());
        }
    }
} 