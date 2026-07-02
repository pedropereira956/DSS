package RestaurantesLN;

import RestaurantesDL.ProducaoDAO;
import RestaurantesLN.ssGestao.*;
import RestaurantesLN.ssProducao.IProducao;
import RestaurantesLN.ssProducao.ProducaoFacade;
import RestaurantesLN.ssProducao.Tarefa;
import RestaurantesLN.ssProdutos.*;
import RestaurantesLN.ssVendas.*;

import java.sql.Connection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import RestaurantesDL.Config;

public class RestaurantesLNFacade implements IRestaurantesLN {
    private IGestGestao ssGestao;
    private IGestProdutos ssProdutos;
    private IGestVendas ssVendas;
    private IProducao ssProducao;

    public RestaurantesLNFacade() {
        this.ssGestao = new GestGestaoFacade();
        this.ssProdutos = new GestProdutosFacade();
        this.ssVendas = new GestVendasFacade();

        try {
            Connection conn = Config.getConnection();
            if (conn != null) {
                ProducaoDAO pDAO = new ProducaoDAO(conn);
                this.ssProducao = new ProducaoFacade(pDAO);
            } else {
                System.err.println("[ERRO] sql error.");
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Falha no construtor da Facade: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int registarNovoPedido(String idRestaurante, Map<String, Integer> menusPedidos,
                                  String notas, String metodoPagamento, String modoEntrega) throws Exception {

        Map<String, Integer> ingredientesTotais = new HashMap<>();

        for (Map.Entry<String, Integer> entry : menusPedidos.entrySet()) {
            String nomeMenu = entry.getKey();
            int quantidadeMenus = entry.getValue();

            List<String> receita = ssProdutos.getReceita(nomeMenu);

            for (String ingrediente : receita) {
                ingredientesTotais.put(ingrediente,
                        ingredientesTotais.getOrDefault(ingrediente, 0) + quantidadeMenus);
            }
        }

        for (Map.Entry<String, Integer> entry : ingredientesTotais.entrySet()) {
            if (!ssGestao.temStockSuficiente(idRestaurante, entry.getKey(), entry.getValue())) {
                throw new Exception("Ingrediente esgotado para realizar o pedido: " + entry.getKey());
            }
        }

        ssGestao.consumirStock(idRestaurante, ingredientesTotais);

        int idPedido = ssVendas.criarPedido(idRestaurante, menusPedidos, notas, metodoPagamento, modoEntrega);

        for (Map.Entry<String, Integer> entry : menusPedidos.entrySet()) {
            String nomeItem = entry.getKey();
            int qtd = entry.getValue();

            ssProducao.criarTarefa(idPedido, "Preparar " + nomeItem, qtd, "COZINHA", 1);
        }

        String instrucaoEmpacotamento = "Empacotar e Verificar Pedido [" + modoEntrega + "]";

        ssProducao.criarTarefa(idPedido, instrucaoEmpacotamento, 1, "COZINHA", 2);

        return idPedido;
    }

    public Map<String, Double> getCatalogoCompleto(String idRestaurante) {
        Map<String, Double> todosOsMenus = this.ssProdutos.getCatalogoCompleto();
        Map<String, Double> menusDisponiveis = new HashMap<>();

        if (todosOsMenus == null || todosOsMenus.isEmpty()) {
            System.out.println("[DEBUG] Catálogo global de menus está vazio na DB.");
            return menusDisponiveis;
        }

        for (String nomeMenu : todosOsMenus.keySet()) {
            List<String> receita = this.ssProdutos.getReceita(nomeMenu);

            if (receita == null || receita.isEmpty()) {
                System.out.println("[ERRO] Menu " + nomeMenu + " não tem receita definida.");
                continue;
            }

            boolean temStockParaTodos = true;
            for (String ingrediente : receita) {
                if (!this.ssGestao.temStockSuficiente(idRestaurante, ingrediente, 1)) {
                    System.out.println("[INFORMAÇÃO] Menu " + nomeMenu + " escondido: Não existe " + ingrediente);
                    temStockParaTodos = false;
                    break;
                }
            }

            if (temStockParaTodos) {
                menusDisponiveis.put(nomeMenu, todosOsMenus.get(nomeMenu));
            }
        }

        return menusDisponiveis;
    }

    public boolean validarLogin(String idFunc, String password) {
        return ssGestao.autenticar(idFunc, password);
    }

    public List<Pedido> getPedidosPendentes(String idRestaurante) {
        return ssVendas.getPedidosPorEstado(idRestaurante, "PENDENTE");
    }

    public void alterarEstadoPedido(int numPedido, String idRestaurante, String novoEstado) {
        this.ssVendas.alterarEstadoPedido(numPedido, idRestaurante, novoEstado);
    }

    public List<Pedido> getPedidosPorEstado(String idRestaurante, String estado) {
        return this.ssVendas.getPedidosPorEstado(idRestaurante, estado);
    }

    public String getCargoFuncionario(String idFuncionario){
        return this.ssGestao.getCargoFuncionario(idFuncionario);
    }

    public List<Tarefa> getTarefasPorPosto(String posto) throws Exception {
        return this.ssProducao.getTarefasPorPosto(posto);
    }

    public void concluirTarefa(int idTarefa) throws Exception {
        int idPedido = ssProducao.getPedidoIdDeTarefa(idTarefa);
        String idRest = ssVendas.getRestauranteDePedido(idPedido);

        ssVendas.alterarEstadoPedido(idPedido, idRest, "PREPARACAO");
        ssProducao.atualizarEstadoTarefa(idTarefa, "CONCLUIDA");


        if (!ssProducao.temTarefasPendentes(idPedido)) {
            ssVendas.alterarEstadoPedido(idPedido, idRest, "PRONTO");

            System.out.println("[AUTOMAÇÃO] Pedido #" + idPedido + " concluído com sucesso!");
        }
    }
}