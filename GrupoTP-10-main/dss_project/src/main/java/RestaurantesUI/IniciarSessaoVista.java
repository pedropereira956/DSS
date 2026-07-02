package RestaurantesUI;

import java.util.Scanner;

public class IniciarSessaoVista implements Vista {
    private RestauranteControlador controlador;

    public IniciarSessaoVista(RestauranteControlador controlador) {
        this.controlador = controlador;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- LOGIN DE FUNCIONÁRIO ---");

        System.out.print("ID Funcionário: ");
        String id = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (controlador.autenticar(id, pass)) {
            System.out.println("Login efetuado com sucesso!");
            return new GestaoRestauranteVista(controlador, id);
        } else {
            System.out.println("Credenciais inválidas! Tentar novamente.");
            System.out.println("1. Tentar novamente");
            System.out.println("0. Voltar");
            String op = sc.nextLine();
            return op.equals("1") ? this : new EscolherUtilizador(controlador);
        }
    }
}