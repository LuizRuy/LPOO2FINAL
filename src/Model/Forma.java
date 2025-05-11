package Model;

public abstract class Forma {
    protected double dimensao;

    public Forma(double dimensao) {
        this.dimensao = dimensao;
    }

    public static final double AREA_MIN = 100;
    public static final double AREA_MAX = 1600;

    public abstract double calcularArea();

    public double getDimensao() {
        return dimensao;
    }

    public void setDimensao(double dimensao) {
        this.dimensao = dimensao;
    }
}