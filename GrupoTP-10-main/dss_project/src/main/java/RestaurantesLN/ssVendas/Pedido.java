package RestaurantesLN.ssVendas;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Pedido {
    private int numero;
    private LocalDateTime hora;
    private String estado;
    private Map<String, Integer> itens;
    private String idRestaurante;
    private String notas;
    private MetodoPagamento metodoPagamento;
    private ModoEntrega modoentrega;

    public Pedido(int numero, String idRestaurante) {
        this.numero = numero;
        this.idRestaurante = idRestaurante;
        this.hora = LocalDateTime.now();
        this.estado = "PENDENTE";
        this.itens = new HashMap<>();
    }

    public void adicionarItem(String produto, int quantidade) {
        this.itens.put(produto, this.itens.getOrDefault(produto, 0) + quantidade);
    }

    // Getters
    public int getNumero() { return numero; }
    public String getEstado() { return estado; }
    public String getIdRestaurante() { return idRestaurante; }
    public Map<String, Integer> getItens() {
        return Collections.unmodifiableMap(this.itens);
    }
    public String getNotas() { return notas; }
    public MetodoPagamento getMetodoPagamento(){ return metodoPagamento; }
    public ModoEntrega getModoEntrega() { return modoentrega; }

    // Setters
    public void setEstado(String novoEstado) { this.estado = novoEstado; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setMetodoPagamento(MetodoPagamento mp) { this.metodoPagamento = mp; }
    public void setModoEntrega(ModoEntrega modo) { this.modoentrega = modo; }


    @Override
    public String toString() {
        return "Pedido #" + numero + " [" + estado + "] - " + itens.size() + " itens.";
    }
}