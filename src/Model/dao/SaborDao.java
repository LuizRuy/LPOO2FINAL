package Model.dao;

import Model.Sabor;
import enums.TipoPizza;

import java.util.List;

public interface SaborDao extends Dao<Sabor>{
    List<Sabor> buscarPorTipo(TipoPizza tipo) throws Exception;
}
