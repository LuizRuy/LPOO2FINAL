package Model.dao;

import Model.PrecoPizza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class PrecoPizzaDaoSql implements PrecoPizzaDao {
    private static PrecoPizzaDaoSql dao;

    private PrecoPizzaDaoSql() {}

    public static PrecoPizzaDaoSql getPrecoPizzaDaoSql() {
        if (dao == null)
            dao = new PrecoPizzaDaoSql();
        return dao;
    }

    @Override
    public PrecoPizza buscar() throws Exception {
        String sql = "SELECT * FROM preco_pizza LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                PrecoPizza preco = PrecoPizza.getInstance();
                preco.setPrecoSimples(rs.getDouble("preco_simples"));
                preco.setPrecoEspecial(rs.getDouble("preco_especial"));
                preco.setPrecoPremium(rs.getDouble("preco_premium"));
                return preco;
            }
        }
        return null;
    }

    @Override
    public void atualizar(PrecoPizza preco) throws Exception {
        String sql = "UPDATE preco_pizza SET preco_simples = ?, preco_especial = ?, preco_premium = ? LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, preco.getPrecoSimples());
            stmt.setDouble(2, preco.getPrecoEspecial());
            stmt.setDouble(3, preco.getPrecoPremium());
            stmt.executeUpdate();
        }
    }

    @Override
    public PrecoPizza buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    @Override
    public void inserir(PrecoPizza preco) throws Exception {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    @Override
    public void excluir(int id) throws Exception {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }

    @Override
    public List<PrecoPizza> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Ainda não implementado.");
    }
} 