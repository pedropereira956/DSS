package RestaurantesLN.ssVendas;

import RestaurantesDL.PedidoDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestVendasFacade implements IGestVendas {
    private PedidoDAO pedidoDAO;

    public GestVendasFacade() {
        this.pedidoDAO = PedidoDAO.getInstance();
    }

    public String getRestauranteDePedido(int idPedido) throws Exception {
        Pedido p = this.pedidoDAO.get(String.valueOf(idPedido));
        if (p == null) throw new Exception("Pedido #" + idPedido + " não encontrado.");
        return p.getIdRestaurante();
    }

    public int criarPedido(String idRestaurante, Map<String, Integer> itens, String notas, String metodoPagamento, String modoentrega) {
        int novoNumero = this.pedidoDAO.size() + 1;
        Pedido novo = new Pedido(novoNumero, idRestaurante);

        for(Map.Entry<String, Integer> entry : itens.entrySet()) {
            novo.adicionarItem(entry.getKey(), entry.getValue());
        }

        novo.setNotas(notas);

        try {
            novo.setMetodoPagamento(MetodoPagamento.valueOf(metodoPagamento));
        } catch (IllegalArgumentException | NullPointerException e) {
            novo.setMetodoPagamento(MetodoPagamento.DINHEIRO);
        }

        try {
            novo.setModoEntrega(ModoEntrega.valueOf(modoentrega));
        } catch (Exception e) {
            novo.setModoEntrega(ModoEntrega.Local);
        }

        this.pedidoDAO.put(String.valueOf(novoNumero), novo);
        return novoNumero;
    }

    public void alterarEstadoPedido(int numPedido, String idRestaurante, String novoEstado) {
        if (numPedido > 0 && novoEstado != null) {
            this.pedidoDAO.atualizarEstado(numPedido, idRestaurante, novoEstado);
        }
    }

    public List<Pedido> getPedidosPorEstado(String idRestaurante, String estado) {
        return this.pedidoDAO.values().stream()
                .filter(p -> p.getIdRestaurante().equals(idRestaurante) && p.getEstado().equals(estado))
                .collect(Collectors.toList());
    }

    public List<String> getNomesMetodos() {
        List<String> metodos = new ArrayList<>();
        for (MetodoPagamento mp : MetodoPagamento.values()) {
            metodos.add(mp.name());
        }
        return metodos;
    }
}