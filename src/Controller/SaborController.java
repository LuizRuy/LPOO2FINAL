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

    public void cadastrarSabor() {
        try {
            Sabor sabor = tela.getSaborForm();
            if (sabor == null) return;

            saborDao.inserir(sabor);
            listarTodosSabores();
            tela.limparCampos();
            tela.mostrarMensagem("Sabor cadastrado com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao cadastrar sabor: " + e.getMessage());
        }
    }

    public void atualizarSabor() {
        try {
            Sabor sabor = tela.getSaborFormComId();
            if (sabor == null) return;

            saborDao.atualizar(sabor);
            listarTodosSabores();
            tela.limparCampos();
            tela.mostrarMensagem("Sabor atualizado com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao atualizar sabor: " + e.getMessage());
        }
    }

    public void excluirSaboresSelecionados() {
        try {
            List<Sabor> selecionados = tela.getSaboresSelecionados();
            if (selecionados == null) return;

            for (Sabor sabor : selecionados) {
                saborDao.excluir(sabor.getId());
            }

            listarTodosSabores();
            tela.limparCampos();
            tela.mostrarMensagem("Sabor(es) excluído(s) com sucesso!");
        } catch (Exception e) {
            tela.mostrarErro("Erro ao excluir sabores: " + e.getMessage());
        }
    }

    public void listarTodosSabores() {
        try {
            List<Sabor> sabores = saborDao.listarTodos();
            tela.carregarSabores(sabores);
        } catch (Exception e) {
            tela.mostrarErro("Erro ao listar sabores: " + e.getMessage());
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
