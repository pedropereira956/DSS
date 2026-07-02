package RestaurantesUI;

import java.util.Scanner;

public class EscolherUtilizador implements Vista {
    private RestauranteControlador controlador;

    public EscolherUtilizador(RestauranteControlador controlador) {
        this.controlador = controlador;
    }

    public Vista run() {
        System.out.println("=== BEM-VINDO AO RESTAURANTE ===");
        System.out.println("1. Ecrã Tátil (Fazer Pedido)");
        System.out.println("2. Área de Funcionário (Login)");
        System.out.println("0. Sair");

        Scanner sc = new Scanner(System.in);
        String opcao = sc.nextLine();

        switch (opcao) {
            case "1": return new EcraTatilVista(controlador);
            case "2": return new IniciarSessaoVista(controlador);
            case "0": return null;
            default:
                System.out.println("Opção inválida!");
                return this;
        }
    }
}