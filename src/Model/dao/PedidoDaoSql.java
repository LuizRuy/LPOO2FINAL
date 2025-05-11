package Model.dao;

import Model.Cliente;
import Model.Pedido;
import Model.Pizza;
import Model.Sabor;
import Model.Forma;
import Model.Circulo;
import Model.Quadrado;
import Model.Triangulo;
import enums.EstadoPedido;
import enums.FormaPizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDaoSql implements PedidoDao {
    private static PedidoDaoSql dao;
    private static PizzaDao pizzaDAO;
    private static ClienteDao clienteDAO;

    private PedidoDaoSql(){
        pizzaDAO = DaoFactory.getPizzaDao(DaoType.SQL);
        clienteDAO = DaoFactory.getClienteDao(DaoType.SQL);
    }

    public static PedidoDaoSql getPedidoDaoSql(){
        if(dao==null)
            return dao = new PedidoDaoSql();
        else
            return dao;
    }
    
    @Override
    public void inserir(Pedido pedido) throws Exception {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 
            
            for (Pizza pizza : pedido.getPizzas()) {
                pizzaDAO.inserir(pizza);
            }

            String sql = "INSERT INTO pedido (cliente_id, estado) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pedido.getCliente().getId());
                stmt.setString(2, EstadoPedido.ABERTO.toString());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    }
                }
            }
            
            String sqlPedidoPizza = "INSERT INTO pedido_pizza (pedido_id, pizza_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPedidoPizza)) {
                for (Pizza pizza : pedido.getPizzas()) {
                    stmt.setInt(1, pedido.getId());
                    stmt.setInt(2, pizza.getId());
                    stmt.executeUpdate();
                }
            }
            
            conn.commit(); 
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (Exception ex) {
                    throw new Exception("Erro ao desfazer transação: " + ex.getMessage());
                }
            }
            throw new Exception("Erro ao inserir pedido: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void atualizar(Pedido pedido) throws Exception {
        String sql = "UPDATE pedido SET cliente_id = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setString(2, pedido.getEstado().name());
            stmt.setInt(3, pedido.getId());
            stmt.executeUpdate();
            
            // Atualizar pizzas do pedido
            excluirPizzasDoPedido(pedido.getId());
            inserirPizzasDoPedido(pedido);
        }
    }

    @Override
    public void excluir(int id) throws Exception {
        excluirPizzasDoPedido(id);

        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Pedido buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
                    
                    Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        cliente
                    );
                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));

                    carregarPizzasDoPedido(pedido);
                    
                    return pedido;
                }
            }
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
                
                Pedido pedido = new Pedido(
                    rs.getInt("id"),
                    cliente
                );
                pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));

                carregarPizzasDoPedido(pedido);
                
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    public void inserirPizzasDoPedido(Pedido pedido) throws Exception {
        String sql = "INSERT INTO pedido_pizza (pedido_id, pizza_id) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Pizza pizza : pedido.getPizzas()) {
                stmt.setInt(1, pedido.getId());
                stmt.setInt(2, pizza.getId());
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void excluirPizzasDoPedido(int pedidoId) throws Exception {
        String sql = "DELETE FROM pedido_pizza WHERE pedido_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void carregarPizzasDoPedido(Pedido pedido) throws Exception {
        String sql = "SELECT pizza_id FROM pedido_pizza WHERE pedido_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int pizzaId = rs.getInt("pizza_id");
                    Pizza pizza = pizzaDAO.buscarPorId(pizzaId);
                    if (pizza != null) {
                        pedido.adicionarPizza(pizza);
                    }
                }
            }
        }
    }

    @Override
    public Forma criarForma(FormaPizza formaPizza, double dimensao) {
        switch (formaPizza) {
            case CIRCULAR:
                return new Circulo(dimensao);
            case QUADRADO:
                return new Quadrado(dimensao);
            case TRIANGULO:
                return new Triangulo(dimensao);
            default:
                throw new IllegalArgumentException("Forma inválida: " + formaPizza);
        }
    }

    @Override
    public List<Pedido> buscarPorCliente(Cliente cliente) throws Exception {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE cliente_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        cliente
                    );
                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
                    
                    carregarPizzasDoPedido(pedido);
                    
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }
}
