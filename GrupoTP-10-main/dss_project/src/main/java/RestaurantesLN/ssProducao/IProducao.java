package RestaurantesLN.ssProducao;

import java.util.List;

public interface IProducao {
    // Métodos que já tinhas
    void criarTarefa(int pedidoId, String item, int qtd, String posto, int prioridade) throws Exception;
    List<Tarefa> getTarefasPorPosto(String posto) throws Exception;
    void atualizarEstadoTarefa(int idTarefa, String novoEstado) throws Exception;

    // --- NOVAS FUNÇÕES PARA A AUTOMAÇÃO ---

    /**
     * Retorna o ID do pedido ao qual uma tarefa específica pertence.
     */
    int getPedidoIdDeTarefa(int idTarefa) throws Exception;

    /**
     * Verifica se ainda existem tarefas com estado 'PENDENTE' para um pedido.
     */
    boolean temTarefasPendentes(int idPedido) throws Exception;
}