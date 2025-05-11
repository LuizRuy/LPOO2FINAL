package Model.dao;

import Model.Cliente;

import java.util.List;

public interface ClienteDao extends Dao<Cliente>{
    List<Cliente> buscarPorSobrenome(String sobrenome) throws Exception;
    Cliente buscarPorTelefone(String telefone) throws Exception;
}
