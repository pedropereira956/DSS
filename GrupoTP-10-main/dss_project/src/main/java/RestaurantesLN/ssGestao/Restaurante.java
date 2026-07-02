package RestaurantesLN.ssGestao;

import RestaurantesLN.ssVendas.Pedido;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Restaurante {
    private String id;
    private String nome;
    private String localizacao;
    private int capacidadeStock;

    private Map<String, Integer> stock;

    public Restaurante(String id, String nome, String localizacao, int capacidadeStock) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidadeStock = capacidadeStock;
        this.stock = new HashMap<>();
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getLocalizacao() { return localizacao; }
    public int getCapacidadeStock() { return capacidadeStock; }

    public void adicionarStock(String item, int qtd) {
        this.stock.put(item, qtd);
    }

    public Map<String, Integer> getMapStock() {
        return new HashMap<>(this.stock);
    }

    public boolean existeStock(String ingrediente, int quantidadeNecessaria) {
        return this.stock.getOrDefault(ingrediente, 0) >= quantidadeNecessaria;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurante that = (Restaurante) o;
        return id.equals(that.id);
    }

    public String toString() {
        return "Restaurante: " + nome + " (" + localizacao + ")";
    }
}