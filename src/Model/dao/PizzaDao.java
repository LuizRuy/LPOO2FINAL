package Model.dao;

import Model.Pizza;
import enums.FormaPizza;

import java.sql.ResultSet;

public interface PizzaDao extends Dao<Pizza>{
    Pizza construirPizza(ResultSet rs) throws Exception;
    Model.Forma criarForma(FormaPizza formaPizza, double dimensao);
}
