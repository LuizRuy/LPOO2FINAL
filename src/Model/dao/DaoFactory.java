package Model.dao;

public class DaoFactory {
    private DaoFactory(){
    }

    public static ClienteDao getClienteDao(DaoType type){
        switch(type){
            case SQL :
                return ClienteDaoSql.getClienteDaoSql();
            default:
                throw new RuntimeException("Tipo não existe:"+type);
        }
    }

    public static PedidoDao getPedidoDao(DaoType type){
        switch(type){
            case SQL :
                return PedidoDaoSql.getPedidoDaoSql();
            default:
                throw new RuntimeException("Tipo não existe:"+type);
        }
    }

    public static PizzaDao getPizzaDao(DaoType type){
        switch(type){
            case SQL :
                return PizzaDaoSql.getPizzaDaoSql();
            default:
                throw new RuntimeException("Tipo não existe:"+type);
        }
    }

    public static SaborDao getSaborDao(DaoType type){
        switch(type){
            case SQL :
                return SaborDaoSql.getSaborDaoSql();
            default:
                throw new RuntimeException("Tipo não existe:"+type);
        }
    }
}
