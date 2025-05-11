package Model.dao;

import java.util.List;

public interface Dao<T> {
    void inserir(T objeto) throws Exception;
    void atualizar(T objeto) throws Exception;
    void excluir(int id) throws Exception;
    T buscarPorId(int id) throws Exception;
    List<T> listarTodos() throws Exception;
}
