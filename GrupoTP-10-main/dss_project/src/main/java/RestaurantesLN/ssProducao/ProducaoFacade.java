package RestaurantesLN.ssProducao;

import RestaurantesDL.ProducaoDAO;
import java.util.List;

public class ProducaoFacade implements IProducao {
    private ProducaoDAO producaoDAO;

    public ProducaoFacade(ProducaoDAO producaoDAO) {
        this.producaoDAO = producaoDAO;
    }

    public void criarTarefa(int pedidoId, String item, int qtd, String posto, int prioridade) throws Exception {
        if (qtd <= 0) throw new Exception("Quantidade inválida para produção.");
        this.producaoDAO.criarTarefa(pedidoId, item, qtd, posto, prioridade);
    }

    public List<Tarefa> getTarefasPorPosto(String posto) throws Exception {
        return this.producaoDAO.getTarefasPorPosto(posto);
    }

    public void atualizarEstadoTarefa(int idTarefa, String novoEstado) throws Exception {
        this.producaoDAO.atualizarEstadoTarefa(idTarefa, novoEstado);
    }

    public int getPedidoIdDeTarefa(int idTarefa) throws Exception {
        int idPedido = producaoDAO.getPedidoIdDeTarefa(idTarefa);
        if (idPedido == -1) throw new Exception("Tarefa não associada a nenhum pedido válido.");
        return idPedido;
    }

    public boolean temTarefasPendentes(int idPedido) throws Exception {
        return producaoDAO.temTarefasPendentes(idPedido);
    }
}