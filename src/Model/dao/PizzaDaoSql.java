package Model.dao;

import Model.Pizza;
import Model.Sabor;
import enums.FormaPizza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaDaoSql implements PizzaDao {
    private static PizzaDaoSql dao;
    private static SaborDao saborDAO;

    private PizzaDaoSql(){
        saborDAO = DaoFactory.getSaborDao(DaoType.SQL);
    }

    public static PizzaDaoSql getPizzaDaoSql(){
        if(dao==null)
            return dao = new PizzaDaoSql();
        else
            return dao;
    }
    
    @Override
    public void inserir(Pizza pizza) throws Exception {
        String sql = "INSERT INTO pizza (forma, dimensao, sabor1_id, sabor2_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pizza.getTipoForma().toString());
            stmt.setDouble(2, pizza.getForma().getDimensao());
            stmt.setInt(3, pizza.getSabor1().getId());
            if (pizza.getSabor2() != null) {
                stmt.setInt(4, pizza.getSabor2().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pizza.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Pizza pizza) throws Exception {
        String sql = "UPDATE pizza SET forma = ?, dimensao = ?, sabor1_id = ?, sabor2_id = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pizza.getTipoForma().toString());
            stmt.setDouble(2, pizza.getForma().getDimensao());
            stmt.setInt(3, pizza.getSabor1().getId());
            if (pizza.getSabor2() != null) {
                stmt.setInt(4, pizza.getSabor2().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setInt(5, pizza.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM pizza WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Pizza buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM pizza WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirPizza(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Pizza> listarTodos() throws Exception {
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT * FROM pizza";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pizzas.add(construirPizza(rs));
            }
        }
        return pizzas;
    }

    @Override
    public Pizza construirPizza(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        FormaPizza formaPizza = FormaPizza.valueOf(rs.getString("forma"));
        double dimensao = rs.getDouble("dimensao");
        Sabor sabor1 = saborDAO.buscarPorId(rs.getInt("sabor1_id"));
        
        Pizza pizza;
        if (rs.getObject("sabor2_id") != null) {
            Sabor sabor2 = saborDAO.buscarPorId(rs.getInt("sabor2_id"));
            pizza = new Pizza(id, criarForma(formaPizza, dimensao), sabor1, sabor2);
        } else {
            pizza = new Pizza(id, criarForma(formaPizza, dimensao), sabor1);
        }
        
        return pizza;
    }

    @Override
    public Model.Forma criarForma(FormaPizza formaPizza, double dimensao) {
        switch (formaPizza) {
            case CIRCULAR:
                return new Model.Circulo(dimensao);
            case QUADRADO:
                return new Model.Quadrado(dimensao);
            case TRIANGULO:
                return new Model.Triangulo(dimensao);
            default:
                throw new IllegalArgumentException("Forma inválida: " + formaPizza);
        }
    }
} 