package Controller;

import Model.Sabor;
import Model.dao.SaborDao;
import Model.dao.DaoFactory;
import Model.dao.DaoType;
import View.TelaSabores;
import enums.TipoPizza;
import java.util.List;

public class SaborController {
    private final TelaSabores tela;
    private final SaborDao saborDao;

    public SaborController(TelaSabores tela) {
        this.tela = tela;
        this.saborDao = DaoFactory.getSaborDao(DaoType.SQL);
    }

    public void cadastrarSabor(String nome, TipoPizza tipo) {
        try {
            Sabor sabor = new Sabor(0, nome, tipo);
            saborDao.inserir(sabor);
            tela.atualizarTabela();
            tela.limparCampos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao cadastrar sabor: " + e.getMessage());
        }
    }

    public void atualizarSabor(int id, String nome, TipoPizza tipo) {
        try {
            Sabor sabor = new Sabor(id, nome, tipo);
            saborDao.atualizar(sabor);
            tela.atualizarTabela();
            tela.limparCampos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar sabor: " + e.getMessage());
        }
    }

    public void excluirSabor(int id) {
        try {
            saborDao.excluir(id);
            tela.atualizarTabela();
            tela.limparCampos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao excluir sabor: " + e.getMessage());
        }
    }

    public List<Sabor> listarTodosSabores() {
        try {
            return saborDao.listarTodos();
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar sabores: " + e.getMessage());
            return null;
        }
    }

    public List<Sabor> buscarSaboresPorTipo(TipoPizza tipo) {
        try {
            return saborDao.buscarPorTipo(tipo);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao buscar sabores: " + e.getMessage());
            return null;
        }
    }
} 