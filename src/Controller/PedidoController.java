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
import enums.FormaPizza;
import Model.Circulo;
import Model.Quadrado;
import Model.Triangulo;
import java.util.List;

public class PedidoController {
    private final PedidoDao pedidoDao;
    private final ClienteDao clienteDao;
    private final PizzaDao pizzaDao;
    private final SaborDao saborDao;
    private final TelaPedidos view;
    private Cliente clienteSelecionado;
    private Pedido pedidoAtual;

    public PedidoController(TelaPedidos view) {
        this.view = view;
        this.pedidoDao = DaoFactory.getPedidoDao(DaoType.SQL);
        this.clienteDao = DaoFactory.getClienteDao(DaoType.SQL);
        this.pizzaDao = DaoFactory.getPizzaDao(DaoType.SQL);
        this.saborDao = DaoFactory.getSaborDao(DaoType.SQL);
    }

    public void buscarClientePorTelefone() {
        try {
            String telefone = view.getTelefoneCliente();
            if (telefone.isEmpty()) {
                view.mostrarErro("Digite o telefone do cliente!");
                return;
            }

            clienteSelecionado = clienteDao.buscarPorTelefone(telefone);
            if (clienteSelecionado != null) {
                view.atualizarLabelCliente(clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
                carregarPedidosExistentes();
            } else {
                view.mostrarErro("Cliente não encontrado!");
                view.atualizarLabelCliente("Cliente não selecionado");
            }
        } catch (Exception e) {
            view.mostrarErro("Erro ao buscar cliente: " + e.getMessage());
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

    public void buscarPedidoPorId() {
        try {
            int idPedido = view.getIdPedidoSelecionado();
            if (idPedido < 0) {
                view.mostrarErro("Selecione um pedido!");
                return;
            }

            pedidoAtual = pedidoDao.buscarPorId(idPedido);
            if (pedidoAtual == null) {
                view.mostrarErro("Pedido não encontrado!");
                return;
            }

            if (pedidoAtual.getEstado() != EstadoPedido.ABERTO) {
                view.mostrarErro("Apenas pedidos em estado ABERTO podem ser alterados!");
                return;
            }

            clienteSelecionado = pedidoAtual.getCliente();
            view.atualizarLabelCliente(clienteSelecionado.getNome() + " " + clienteSelecionado.getSobrenome());
            view.atualizarTabelaPizzas(pedidoAtual.getPizzas());
            view.atualizarPrecoTotal(calcularPrecoTotal(pedidoAtual));
            view.mostrarMensagem("Pedido carregado com sucesso!");
        } catch (Exception e) {
            view.mostrarErro("Erro ao buscar pedido: " + e.getMessage());
        }
    }

    public void adicionarPizzaAoPedido() {
        try {
            if (clienteSelecionado == null) {
                view.mostrarErro("Selecione um cliente primeiro!");
                return;
            }

            FormaPizza forma = view.getFormaPizza();
            boolean isPorArea = view.isPorArea();
            double dimensao = view.getDimensao();
            Sabor sabor1 = view.getSabor1();
            Sabor sabor2 = view.getSabor2();

            if (sabor1 == null) {
                view.mostrarErro("Selecione pelo menos um sabor!");
                return;
            }

            if (pedidoAtual == null) {
                pedidoAtual = new Pedido(0, clienteSelecionado);
                pedidoAtual.setEstado(EstadoPedido.ABERTO);
            }

            Pizza pizza = criarPizza(forma, isPorArea, dimensao, sabor1, sabor2);
            pedidoAtual.adicionarPizza(pizza);
            view.atualizarTabelaPizzas(pedidoAtual.getPizzas());
            view.atualizarPrecoTotal(calcularPrecoTotal(pedidoAtual));
        } catch (Exception e) {
            view.mostrarErro("Erro ao adicionar pizza: " + e.getMessage());
        }
    }

    public void removerPizzaDoPedido() {
        try {
            int linhaSelecionada = view.getLinhaPizzaSelecionada();
            if (linhaSelecionada < 0) {
                view.mostrarErro("Selecione uma pizza para remover!");
                return;
            }

            if (pedidoAtual == null || pedidoAtual.getPizzas().isEmpty()) {
                view.mostrarErro("Não há pizzas para remover!");
                return;
            }

            if (linhaSelecionada >= 0 && linhaSelecionada < pedidoAtual.getPizzas().size()) {
                Pizza pizza = pedidoAtual.getPizzas().get(linhaSelecionada);
                pedidoAtual.removerPizza(pizza);
                view.atualizarTabelaPizzas(pedidoAtual.getPizzas());
                view.atualizarPrecoTotal(calcularPrecoTotal(pedidoAtual));
            }
        } catch (Exception e) {
            view.mostrarErro("Erro ao remover pizza: " + e.getMessage());
        }
    }

    public void finalizarPedido() {
        try {
            if (pedidoAtual == null || pedidoAtual.getPizzas().isEmpty()) {
                view.mostrarErro("Adicione pelo menos uma pizza ao pedido!");
                return;
            }
            if (pedidoAtual.getId() == 0) {
                pedidoDao.inserir(pedidoAtual);
            } else {
                pedidoDao.atualizar(pedidoAtual);
            }

            view.mostrarMensagem("Pedido " +
                (pedidoAtual.getId() == 0 ? "finalizado" : "atualizado") + " com sucesso!");
            limparPedido();
        } catch (Exception e) {
            view.mostrarErro("Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    public void atualizarEstadoPedido(EstadoPedido novoEstado) {
        try {
            if (pedidoAtual == null) {
                view.mostrarErro("Nenhum pedido selecionado!");
                return;
            }

            pedidoAtual.setEstado(novoEstado);
            pedidoDao.atualizar(pedidoAtual);
            view.atualizarTabelaPizzas(pedidoAtual.getPizzas());
        } catch (Exception e) {
            view.mostrarErro("Erro ao atualizar estado do pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listarPedidosPorCliente() {
        try {
            if (clienteSelecionado == null) {
                view.mostrarErro("Nenhum cliente selecionado!");
                return null;
            }
            return pedidoDao.buscarPorCliente(clienteSelecionado);
        } catch (Exception e) {
            view.mostrarErro("Erro ao listar pedidos do cliente: " + e.getMessage());
            return null;
        }
    }

    private Pizza criarPizza(FormaPizza forma, boolean isPorArea, double dimensao, Sabor sabor1, Sabor sabor2) {
        // Cria o objeto Forma correto
        Model.Forma formaObj = null;
        switch (forma) {
            case CIRCULAR:
                formaObj = new Circulo(isPorArea ? Math.sqrt(dimensao / Math.PI) : dimensao);
                break;
            case QUADRADO:
                formaObj = new Quadrado(isPorArea ? Math.sqrt(dimensao) : dimensao);
                break;
            case TRIANGULO:
                formaObj = new Triangulo(isPorArea ? Math.sqrt(4 * dimensao / Math.sqrt(3)) : dimensao);
                break;
        }
        if (sabor2 != null) {
            return new Pizza(0, formaObj, sabor1, sabor2);
        } else {
            return new Pizza(0, formaObj, sabor1);
        }
    }

    private double calcularPrecoTotal(Pedido pedido) {
        return pedido.getPizzas().stream()
            .mapToDouble(Pizza::getPrecoTotal)
            .sum();
    }

    public void limparPedido() {
        pedidoAtual = null;
        clienteSelecionado = null;
        view.limparPedido();
    }

    public void carregarPedidosExistentes() {
        try {
            if (clienteSelecionado != null) {
                List<Pedido> pedidos = pedidoDao.buscarPorCliente(clienteSelecionado);
                view.atualizarComboPedidos(pedidos);
            }
        } catch (Exception e) {
            view.mostrarErro("Erro ao carregar pedidos: " + e.getMessage());
        }
    }
} 