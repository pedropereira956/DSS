package RestaurantesUI;

import java.util.Map;
import java.util.Scanner;

public class IndicadoresVista implements Vista {
    private RestauranteControlador controlador;
    private String idFunc;

    public IndicadoresVista(RestauranteControlador ctrl, String id) {
        this.controlador = ctrl;
        this.idFunc = id;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- INDICADORES DE GESTÃO (COO) ---");

        Map<String, Integer> stock = controlador.getStockRestauranteAtual();

        System.out.println("\n[STOCK ATUAL]");
        if (stock.isEmpty()) {
            System.out.println("Nenhum item registado no inventário.");
        } else {
            System.out.printf("%-20s | %-10s%n", "Ingrediente", "Quantidade");
            System.out.println("-------------------------------------------");
            stock.forEach((item, qtd) -> {
                System.out.printf("%-20s | %-10d%n", item, qtd);
            });
        }

        System.out.println("\nPrima ENTER para voltar...");
        sc.nextLine();
        return new GestaoRestauranteVista(controlador, idFunc);
    }
}