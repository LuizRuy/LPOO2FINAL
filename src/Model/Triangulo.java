package Model;

public class Triangulo extends Forma {
    public static final double LADO_MIN = 20;
    public static final double LADO_MAX = 60;

    public Triangulo(double lado) {
        super(lado);
        if (lado < LADO_MIN || lado > LADO_MAX) {
            throw new IllegalArgumentException("Lado deve estar entre " + LADO_MIN + " e " + LADO_MAX + " cm");
        }
    }

    @Override
    public double calcularArea() {
        return (Math.sqrt(3) / 4) * dimensao * dimensao;
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
