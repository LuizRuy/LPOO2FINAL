package Model.dao;

import Model.PrecoPizza;

public interface PrecoPizzaDao extends Dao<PrecoPizza> {
    PrecoPizza buscar() throws Exception;
} 