package Controller;

import Model.PrecoPizza;
import View.TelaPrecos;

public class PrecoController {
    private final TelaPrecos tela;
    private final PrecoPizza precoPizza;

    public PrecoController(TelaPrecos tela) {
        this.tela = tela;
        this.precoPizza = PrecoPizza.getInstance();
    }

    public void atualizarPrecos(double precoSimples, double precoEspecial, double precoPremium) {
        try {
            precoPizza.setPrecoSimples(precoSimples);
            precoPizza.setPrecoEspecial(precoEspecial);
            precoPizza.setPrecoPremium(precoPremium);
            tela.mostrarMensagem("Preços atualizados com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar preços: " + e.getMessage());
        }
    }

    public double getPrecoSimples() {
        return precoPizza.getPrecoSimples();
    }

    public double getPrecoEspecial() {
        return precoPizza.getPrecoEspecial();
    }

    public double getPrecoPremium() {
        return precoPizza.getPrecoPremium();
    }
} 