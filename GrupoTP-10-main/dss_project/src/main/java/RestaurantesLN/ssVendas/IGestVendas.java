package RestaurantesLN.ssVendas;

import java.util.Map;
import java.util.List;

public interface IGestVendas {
    int criarPedido(String idRestaurante, Map<String, Integer> itens, String notas, String metodoPagamento, String modoentrega);
    void alterarEstadoPedido(int numPedido, String idRestaurante, String novoEstado);
    List<Pedido> getPedidosPorEstado(String idRestaurante, String estado);
    List<String> getNomesMetodos();
    public String getRestauranteDePedido(int idPedido) throws Exception;
}