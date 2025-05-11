package Model;

import enums.EstadoPedido;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Pizza> pizzas;
    private EstadoPedido estado;
    private double precoTotal;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.pizzas = new ArrayList<>();
        this.estado = EstadoPedido.ABERTO;
        this.precoTotal = 0.0;
    }

    public void adicionarPizza(Pizza pizza) {
        pizzas.add(pizza);
        calcularPrecoTotal();
    }

    public void removerPizza(Pizza pizza) {
        pizzas.remove(pizza);
        calcularPrecoTotal();
    }

    private void calcularPrecoTotal() {
        precoTotal = 0.0;
        for (Pizza pizza : pizzas) {
            precoTotal += pizza.getPrecoTotal();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
        calcularPrecoTotal();
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id).append(" - ").append(cliente.getNome())
          .append(" ").append(cliente.getSobrenome()).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Pizzas:\n");
        for (Pizza pizza : pizzas) {
            sb.append("  ").append(pizza.toString()).append("\n");
        }
        sb.append("Total: R$ ").append(String.format("%.2f", precoTotal));
        return sb.toString();
    }
}
