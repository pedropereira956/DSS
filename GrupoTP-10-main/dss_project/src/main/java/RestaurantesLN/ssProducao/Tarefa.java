package RestaurantesLN.ssProducao;

public class Tarefa {
    private int idTarefa;
    private int idPedido;
    private String itemNome;
    private int quantidade;
    private int prioridade;

    public Tarefa(int id, int idPed, String nome, int qtd, int prio) {
        this.idTarefa = id;
        this.idPedido = idPed;
        this.itemNome = nome;
        this.quantidade = qtd;
        this.prioridade = prio;
    }

    public String toString() {
        String pStr = switch (prioridade) {
            case 1 -> "[!!! URGENTE !!!]";
            case 2 -> "[!  ALTA   !]";
            default -> "[ Normal ]";
        };

        return String.format("%s ID:%d | Pedido #%d | %dx %s",
                pStr, idTarefa, idPedido, quantidade, itemNome);
    }

    public int getIdTarefa() { return idTarefa; }
}