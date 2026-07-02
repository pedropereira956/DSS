package RestaurantesUI;

import RestaurantesLN.ssVendas.Pedido;
import java.util.List;
import java.util.Scanner;

public class CozinhaVista implements Vista {
    private RestauranteControlador controlador;
    private String idFunc;
    private String estadoFiltro;
    private String proximoEstado;

    public CozinhaVista(RestauranteControlador ctrl, String id, String filtro, String proximo) {
        this.controlador = ctrl;
        this.idFunc = id;
        this.estadoFiltro = filtro;
        this.proximoEstado = proximo;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);
        String idRest = controlador.getRestauranteAtual();

        List<Pedido> pedidos = controlador.getPedidosPorEstado(idRest, estadoFiltro);

        System.out.println("\n--- LISTA DE PEDIDOS: " + estadoFiltro + " ---");
        if (pedidos.isEmpty()) {
            System.out.println("Sem pedidos neste estado.");
            return new GestaoRestauranteVista(controlador, idFunc);
        }

        for (Pedido p : pedidos) {
            System.out.println("Ticket #" + p.getNumero());
            p.getItens().forEach((k, v) -> System.out.println("  " + v + "x " + k));
        }

        System.out.print("\nNº Ticket para passar a " + proximoEstado + " (ou 'V' voltar): ");
        String input = sc.nextLine();

        if (input.equalsIgnoreCase("V")) return new GestaoRestauranteVista(controlador, idFunc);

        try {
            int num = Integer.parseInt(input);
            controlador.alterarEstadoPedido(num, idRest, proximoEstado);
            System.out.println("Pedido #" + num + " atualizado para " + proximoEstado + "!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return this;
    }
}