package RestaurantesLN.ssProdutos;

import java.util.List;
import java.util.Map;

public interface IGestProdutos {
    List<String> getListaProdutos();
    double getPrecoProduto(String nome);
    public List<String> getReceita(String nomeMenu);
    Map<String, Double> getCatalogoCompleto();
}