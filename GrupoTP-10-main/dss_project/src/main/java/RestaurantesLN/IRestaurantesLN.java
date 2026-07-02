package RestaurantesLN;

import RestaurantesLN.ssProducao.Tarefa;
import RestaurantesLN.ssVendas.Pedido;
import java.util.List;
import java.util.Map;

public interface IRestaurantesLN {

    /**
     * Valida as credenciais de um funcionário na base de dados.
     */
    boolean validarLogin(String idFunc, String password);

    /**
     * Devolve o catálogo de menus disponíveis para um determinado restaurante,
     * filtrando apenas aqueles que têm stock de todos os ingredientes necessários.
     */
    Map<String, Double> getCatalogoCompleto(String idRestaurante);

    /**
     * Regista um novo pedido no sistema.
     * Valida o stock de cada ingrediente dos menus pedidos e abate-o na base de dados.
     * @return O número do ticket (senha) gerado para o pedido.
     */
    public int registarNovoPedido(String idRestaurante, Map<String, Integer> menusPedidos,
                                  String notas, String metodoPagamento, String modoEntrega) throws Exception;

    /**
     * Devolve a lista de pedidos que ainda não foram entregues (estado PENDENTE).
     */
    List<Pedido> getPedidosPendentes(String idRestaurante);

    /**
     * (Opcional) Permite à cozinha alterar o estado de um pedido (ex: de PENDENTE para PRONTO).
     */
    public void alterarEstadoPedido(int numPedido, String idRestaurante, String novoEstado);

    List<Pedido> getPedidosPorEstado(String idRestaurante, String estado);

    String getCargoFuncionario(String idFuncionario);

    List<Tarefa> getTarefasPorPosto(String posto) throws Exception;

    void concluirTarefa(int idTarefa) throws Exception;
}