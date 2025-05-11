package Model;

public class Circulo extends Forma {
    public static final double RAIO_MIN = 7;
    public static final double RAIO_MAX = 23;

    public Circulo(double raio) {
        super(raio);
        if (raio < RAIO_MIN || raio > RAIO_MAX) {
            throw new IllegalArgumentException("Raio deve estar entre " + RAIO_MIN + " e " + RAIO_MAX + " cm");
        }
    }

    @Override
    public double calcularArea() {
        return Math.PI * dimensao * dimensao;
    }

    public double getRaio() {
        return dimensao;
    }

    public void setRaio(double raio) {
        if (raio < RAIO_MIN || raio > RAIO_MAX) {
            throw new IllegalArgumentException("Raio deve estar entre " + RAIO_MIN + " e " + RAIO_MAX + " cm");
        }
        this.dimensao = raio;
    }
}
