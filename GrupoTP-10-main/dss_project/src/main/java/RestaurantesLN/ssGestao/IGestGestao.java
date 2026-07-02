package RestaurantesLN.ssGestao;

import java.util.Map;

public interface IGestGestao {
    String adicionarFuncionario(String idRestaurante, String nome, String passwd, String cargo);

    boolean temStockSuficiente(String idRestaurante, String item, int quantidade);
    void consumirStock(String idRestaurante, Map<String, Integer> itens);
    boolean autenticar(String idFuncionario, String password);
    String getCargoFuncionario(String idFuncionario);
}
