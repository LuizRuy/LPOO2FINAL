package Model.dao;

import Model.Sabor;
import enums.TipoPizza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaborDaoSql implements SaborDao {
    private static SaborDaoSql dao;

    private SaborDaoSql(){}

    public static SaborDaoSql getSaborDaoSql(){
        if(dao==null)
            return dao = new SaborDaoSql();
        else
            return dao;
    }
    
    @Override
    public void inserir(Sabor sabor) throws Exception {
        String sql = "INSERT INTO sabor (nome, tipo) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipo().name());
            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Sabor sabor) throws Exception {
        String sql = "UPDATE sabor SET nome = ?, tipo = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipo().name());
            stmt.setInt(3, sabor.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM sabor WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Sabor buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM sabor WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Sabor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        TipoPizza.valueOf(rs.getString("tipo"))
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Sabor> listarTodos() throws Exception {
        List<Sabor> sabores = new ArrayList<>();
        String sql = "SELECT * FROM sabor";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sabores.add(new Sabor(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    TipoPizza.valueOf(rs.getString("tipo"))
                ));
            }
        }
        return sabores;
    }

    @Override
    public List<Sabor> buscarPorTipo(TipoPizza tipo) throws Exception {
        List<Sabor> sabores = new ArrayList<>();
        String sql = "SELECT * FROM sabor WHERE tipo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sabores.add(new Sabor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        TipoPizza.valueOf(rs.getString("tipo"))
                    ));
                }
            }
        }
        return sabores;
    }
} 