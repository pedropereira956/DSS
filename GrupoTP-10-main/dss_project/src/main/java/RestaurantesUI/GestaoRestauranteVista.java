package RestaurantesUI;

import java.util.Scanner;

public class GestaoRestauranteVista implements Vista {
    private RestauranteControlador controlador;
    private String idFuncionario;

    public GestaoRestauranteVista(RestauranteControlador controlador, String idFunc) {
        this.controlador = controlador;
        this.idFuncionario = idFunc;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);
        String cargo = controlador.getCargoFuncionario(idFuncionario);

        System.out.println("\n--- PAINEL DE GESTÃO (" + cargo + ") ---");

        // Regras de Visualização
        boolean isCozinheiro = "COZINHA".equalsIgnoreCase(cargo);
        boolean isCaixa = "CAIXA".equalsIgnoreCase(cargo);
        boolean isCOO = "COO".equalsIgnoreCase(cargo);

        if (isCozinheiro || isCOO) {
            System.out.println("1. Ver Pedidos Pendentes (Cozinha)");
        }

        if (isCaixa || isCOO) {
            System.out.println("2. Entregar pedido pronto (Caixa)");
        }

        if (isCOO) {
            System.out.println("3. Consultar Stock Atual (COO)");
        }

        System.out.println("0. Logout");
        System.out.print("> ");

        String opcao = sc.nextLine();
        switch (opcao) {
            case "1":
                if (isCozinheiro || isCOO) return controlador.ecraCozinha(idFuncionario);
                break;
            case "2":
                if (isCaixa || isCOO) return controlador.entregarPedidoPronto(idFuncionario);
                break;
            case "3":
                if (isCOO) return controlador.ecraIndicadores(idFuncionario);
                break;
            case "0":
                return new EscolherUtilizador(controlador);
            default:
                System.out.println("Opção inválida ou sem permissões.");
                return this;
        }

        System.out.println("Acesso Negado.");
        return this;
    }
}