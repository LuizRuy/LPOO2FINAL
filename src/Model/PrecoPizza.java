package Model;

import Model.dao.DaoFactory;
import Model.dao.DaoType;
import Model.dao.PrecoPizzaDao;
import enums.TipoPizza;

public class PrecoPizza {
    private static PrecoPizza instance;
    private double precoSimples;
    private double precoEspecial;
    private double precoPremium;

    private PrecoPizza() {
        try {
            PrecoPizzaDao dao = DaoFactory.getPrecoPizzaDao(DaoType.SQL);
            PrecoPizza precoDoBanco = dao.buscar();
            if (precoDoBanco != null) {
                this.precoSimples = precoDoBanco.getPrecoSimples();
                this.precoEspecial = precoDoBanco.getPrecoEspecial();
                this.precoPremium = precoDoBanco.getPrecoPremium();
            } else {
                this.precoSimples = 0.05;
                this.precoEspecial = 0.08;
                this.precoPremium = 0.12;
                dao.inserir(this);
            }
        } catch (Exception e) {
            this.precoSimples = 0.05;
            this.precoEspecial = 0.08;
            this.precoPremium = 0.12;
        }
    }

    public static PrecoPizza getInstance() {
        if (instance == null) {
            instance = new PrecoPizza();
        }
        return instance;
    }

    public double getPrecoSimples() {
        return precoSimples;
    }

    public double getPrecoEspecial() {
        return precoEspecial;
    }

    public double getPrecoPremium() {
        return precoPremium;
    }

    public double calcularPreco(Forma forma, TipoPizza tipo) {
        double area = forma.calcularArea();
        return area * getPrecoPorTipo(tipo);
    }

    private double getPrecoPorTipo(TipoPizza tipo) {
        switch (tipo) {
            case SIMPLES:
                return precoSimples;
            case ESPECIAL:
                return precoEspecial;
            case PREMIUM:
                return precoPremium;
            default:
                return precoSimples;
        }
    }

    public void setPrecoSimples(double precoSimples) {
        if (precoSimples <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.precoSimples = precoSimples;
    }

    public void setPrecoEspecial(double precoEspecial) {
        if (precoEspecial <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.precoEspecial = precoEspecial;
    }

    public void setPrecoPremium(double precoPremium) {
        if (precoPremium <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.precoPremium = precoPremium;
    }
}
