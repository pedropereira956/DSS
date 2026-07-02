package RestaurantesUI;

import java.util.List;
import java.util.Scanner;

public class EscolherRestauranteVista implements Vista {
    private RestauranteControlador controlador;

    public EscolherRestauranteVista(RestauranteControlador controlador) {
        this.controlador = controlador;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== ChocapitosRestaurantes ===");
        System.out.println("Selecionar o Restaurante:");

        List<String> ids = controlador.getListaRestaurantes();

        if (ids.isEmpty()) {
            System.out.println("ERRO: Não existem restaurantes na Base de Dados!");
            return null;
        }

        for (int i = 0; i < ids.size(); i++) {
            System.out.println((i + 1) + ". " + ids.get(i));
        }
        System.out.println("0. Sair");

        System.out.print("> ");
        int opcao = Integer.parseInt(sc.nextLine());

        if (opcao == 0) return null;
        if (opcao > 0 && opcao <= ids.size()) {
            String idEscolhido = ids.get(opcao - 1);

            controlador.setRestauranteAtual(idEscolhido);

            System.out.println("Terminal configurado para: " + idEscolhido);
            return new EscolherUtilizador(controlador);
        }

        return this;
    }
}