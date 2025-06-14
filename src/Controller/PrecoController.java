package Controller;

import Model.PrecoPizza;
import Model.dao.PrecoPizzaDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaPrecos;

public class PrecoController {
    private final TelaPrecos tela;
    private final PrecoPizzaDao precoDao;
    private PrecoPizza precoPizza;

    public PrecoController(TelaPrecos tela) {
        this.tela = tela;
        this.precoDao = DaoFactory.getPrecoPizzaDao(DaoType.SQL);
        carregarPrecos();
    }

    public void carregarPrecos() {
        try {
            precoPizza = precoDao.buscar();
            tela.setPrecos(precoPizza.getPrecoSimples(), precoPizza.getPrecoEspecial(), precoPizza.getPrecoPremium());
        } catch (Exception e) {
            tela.mostrarErro("Erro ao carregar preços: " + e.getMessage());
        }
    }

    public void atualizarPrecos() {
        try {
            double simples = tela.getPrecoSimples();
            double especial = tela.getPrecoEspecial();
            double premium = tela.getPrecoPremium();
            precoPizza.setPrecoSimples(simples);
            precoPizza.setPrecoEspecial(especial);
            precoPizza.setPrecoPremium(premium);
            precoDao.atualizar(precoPizza);
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