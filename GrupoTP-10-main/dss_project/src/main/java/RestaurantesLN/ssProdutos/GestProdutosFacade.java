package RestaurantesLN.ssProdutos;

import RestaurantesDL.MenuDAO;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class GestProdutosFacade implements IGestProdutos {
    private Map<String, Double> catalogo;

    public GestProdutosFacade() {
        this.catalogo = new HashMap<>();
    }

    public List<String> getListaProdutos() {
        return new ArrayList<>(this.catalogo.keySet());
    }

    public double getPrecoProduto(String nome) {
        return this.catalogo.getOrDefault(nome, 0.0);
    }

    public List<String> getReceita(String nomeMenu) {
        Map<String, List<String>> todasReceitas = MenuDAO.getReceitas();
        return todasReceitas.getOrDefault(nomeMenu, List.of());
    }

    public Map<String, Double> getCatalogoCompleto() {
        return MenuDAO.getPrecos();
    }
}