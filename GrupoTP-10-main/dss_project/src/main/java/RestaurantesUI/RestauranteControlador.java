package RestaurantesUI;

import RestaurantesDL.RestauranteDAO;
import RestaurantesLN.IRestaurantesLN;
import RestaurantesLN.ssGestao.Restaurante;
import RestaurantesLN.ssProducao.Tarefa;
import RestaurantesLN.ssVendas.MetodoPagamento;
import RestaurantesLN.ssVendas.Pedido;

import java.util.*;
import java.util.stream.Collectors;

public class RestauranteControlador extends Controlador {
    private String restauranteAtual;

    public RestauranteControlador(IRestaurantesLN model) {
        super(model);
    }

    public List<String> getListaRestaurantes() {
        return new ArrayList<>(RestauranteDAO.getInstance().keySet());
    }

    public void setRestauranteAtual(String id) {
        this.restauranteAtual = id;
    }

    public String getRestauranteAtual() {
        return this.restauranteAtual;
    }

    public Map<String, Double> getMenusComStock(String idRestaurante) {
        return getModel().getCatalogoCompleto(idRestaurante);
    }

    public int confirmarPedido(String idRest, Map<String, Integer> itens, String notas, String metodoPagamento, String modoentrega) throws Exception {
        return getModel().registarNovoPedido(idRest, itens, notas, metodoPagamento, modoentrega);
    }

    public boolean autenticar(String id, String pass) {
        return getModel().validarLogin(id, pass);
    }

    public void alterarEstadoPedido(int numPedido, String idRestaurante, String novoEstado) {
        getModel().alterarEstadoPedido(numPedido, idRestaurante, novoEstado);
    }

    public Vista ecraCozinha(String idFunc) {
        return new ProducaoPostoVista(this, idFunc);
    }

    public List<Pedido> getPedidosPorEstado(String idRestaurante, String estado) {
        return getModel().getPedidosPorEstado(idRestaurante, estado);
    }

    public String getCargoFuncionario(String idFuncionario) {
        return getModel().getCargoFuncionario(idFuncionario);
    }

    public Vista ecraIndicadores(String idFuncionario) {
        return new IndicadoresVista(this, idFuncionario);
    }

    public List<Tarefa> getTarefasDoPosto(String idFuncionario) {
        try {
            String cargo = getModel().getCargoFuncionario(idFuncionario);
            String posto = determinarPostoPorCargo(cargo);
            return getModel().getTarefasPorPosto(posto);
        } catch (Exception e) {
            System.err.println("Erro ao obter tarefas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void concluirTarefa(int idTarefa) {
        try {
            getModel().concluirTarefa(idTarefa);
        } catch (Exception e) {
            System.err.println("Erro ao concluir tarefa: " + e.getMessage());
        }
    }

    private String determinarPostoPorCargo(String cargo) {
        if (cargo == null) return "COZINHA";
        String c = cargo.toUpperCase();
        if (c.equals("COZINHA")) {
            return "COZINHA";
        }

        return c;
    }

    public Map<String, Integer> getStockRestauranteAtual() {
        String idRest = this.getRestauranteAtual();
        Restaurante r = RestauranteDAO.getInstance().get(idRest);
        return (r != null) ? r.getMapStock() : new HashMap<>();
    }

    public List<String> getMetodosPagamentoDisponiveis() {
        return Arrays.stream(MetodoPagamento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public Vista entregarPedidoPronto(String idFuncionario) {
        return new CozinhaVista(this, idFuncionario, "PRONTO", "ENTREGUE");
    }

}