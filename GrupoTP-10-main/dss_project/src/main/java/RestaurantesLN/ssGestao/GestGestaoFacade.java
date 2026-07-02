package RestaurantesLN.ssGestao;

import RestaurantesDL.FuncionarioDAO;
import RestaurantesDL.RestauranteDAO;
import java.util.Map;

public class GestGestaoFacade implements IGestGestao {
    private RestauranteDAO restaurantesDAO;
    private FuncionarioDAO funcionarioDAO;


    public GestGestaoFacade() {
        this.restaurantesDAO = RestauranteDAO.getInstance();
        this.funcionarioDAO = FuncionarioDAO.getInstance();
    }

    public String adicionarFuncionario(String idRestaurante, String nome, String passwd, String cargo) {
        String idFunc = "F" + System.currentTimeMillis() % 10000;
        Funcionario novo = new Funcionario(idFunc, nome, passwd, cargo, idRestaurante);

        funcionarioDAO.put(idFunc, novo);

        return idFunc;
    }

    public boolean temStockSuficiente(String idRestaurante, String item, int quantidade) {
        Restaurante r = this.restaurantesDAO.get(idRestaurante);
        if (r != null) {
            return r.existeStock(item, quantidade);
        }
        return false;
    }

    public void consumirStock(String idRestaurante, Map<String, Integer> itens) {
        for (Map.Entry<String, Integer> entry : itens.entrySet()) {
            restaurantesDAO.atualizarQuantidadeStock(idRestaurante, entry.getKey(), -entry.getValue());
        }
    }

    public boolean autenticar(String idFuncionario, String password) {
        Funcionario f = funcionarioDAO.get(idFuncionario);

        if (f != null) {
            return f.validarLogin(password);
        }

        return false;
    }

    public String getCargoFuncionario(String idFuncionario){
        Funcionario f = funcionarioDAO.get(idFuncionario);
        if (f != null) {
            return  f.getCargo();
        }
        return null;
    }
}