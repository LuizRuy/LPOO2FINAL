package Model.dao;

import Model.Cliente;
import Model.Forma;
import Model.Pedido;
import enums.FormaPizza;

import java.util.List;

public interface PedidoDao extends Dao<Pedido>{
    void inserirPizzasDoPedido(Pedido pedido) throws Exception;
    void excluirPizzasDoPedido(int pedidoId) throws Exception;
    void carregarPizzasDoPedido(Pedido pedido) throws Exception;
    Forma criarForma(FormaPizza formaPizza, double dimensao);
    List<Pedido> buscarPorCliente(Cliente cliente) throws Exception;
}
