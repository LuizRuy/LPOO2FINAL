package Model;

public class Quadrado extends Forma {
    public static final double LADO_MIN = 10;
    public static final double LADO_MAX = 40;

    public Quadrado(double lado) {
        super(lado);
        if (lado < LADO_MIN || lado > LADO_MAX) {
            throw new IllegalArgumentException("Lado deve estar entre " + LADO_MIN + " e " + LADO_MAX + " cm");
        }
    }

    @Override
    public double calcularArea() {
        return dimensao * dimensao;
    }

    public double getLado() {
        return dimensao;
    }

    public void setLado(double lado) {
        if (lado < LADO_MIN || lado > LADO_MAX) {
            throw new IllegalArgumentException("Lado deve estar entre " + LADO_MIN + " e " + LADO_MAX + " cm");
        }
        this.dimensao = lado;
    }
}
