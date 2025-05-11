package Model;

import enums.FormaPizza;
import enums.TipoPizza;

public class Pizza {
    private int id;
    private Forma forma;
    private Sabor sabor1;
    private Sabor sabor2;
    private double precoTotal;

    public Pizza(int id, Forma forma, Sabor sabor1) {
        this.id = id;
        this.forma = forma;
        this.sabor1 = sabor1;
        this.sabor2 = null;
        calcularPreco();
    }

    public Pizza(int id, Forma forma, Sabor sabor1, Sabor sabor2) {
        this.id = id;
        this.forma = forma;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        calcularPreco();
    }

    private void calcularPreco() {
        PrecoPizza precoPizza = PrecoPizza.getInstance();
        
        if (sabor2 == null) {
            precoTotal = precoPizza.calcularPreco(forma, sabor1.getTipo());
        } else {
            double preco1 = precoPizza.calcularPreco(forma, sabor1.getTipo());
            double preco2 = precoPizza.calcularPreco(forma, sabor2.getTipo());
            precoTotal = (preco1 + preco2) / 2;
        }
    }

    private double getPrecoPorTipo(TipoPizza tipo, PrecoPizza precoPizza) {
        switch (tipo) {
            case SIMPLES:
                return precoPizza.getPrecoSimples();
            case ESPECIAL:
                return precoPizza.getPrecoEspecial();
            case PREMIUM:
                return precoPizza.getPrecoPremium();
            default:
                return precoPizza.getPrecoSimples();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Forma getForma() {
        return forma;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
        calcularPreco();
    }

    public Sabor getSabor1() {
        return sabor1;
    }

    public void setSabor1(Sabor sabor1) {
        this.sabor1 = sabor1;
        calcularPreco();
    }

    public Sabor getSabor2() {
        return sabor2;
    }

    public void setSabor2(Sabor sabor2) {
        this.sabor2 = sabor2;
        calcularPreco();
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public FormaPizza getTipoForma() {
        if (forma instanceof Circulo) {
            return FormaPizza.CIRCULAR;
        } else if (forma instanceof Quadrado) {
            return FormaPizza.QUADRADO;
        } else {
            return FormaPizza.TRIANGULO;
        }
    }

    @Override
    public String toString() {
        String descricao = "Pizza " + getTipoForma() + " - " + sabor1.getNome();
        if (sabor2 != null) {
            descricao += " + " + sabor2.getNome();
        }
        descricao += " - R$ " + String.format("%.2f", precoTotal);
        return descricao;
    }
}