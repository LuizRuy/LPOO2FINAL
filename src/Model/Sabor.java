package Model;

import enums.TipoPizza;

public class Sabor {
    private int id;
    private String nome;
    private TipoPizza tipo;

    public Sabor(int id, String nome, TipoPizza tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Sabor(String nome, TipoPizza tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPizza getTipo() {
        return tipo;
    }

    public void setTipo(TipoPizza tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + ")";
    }
}
