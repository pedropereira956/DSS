package RestaurantesUI;

import RestaurantesLN.ssProducao.Tarefa;
import java.util.List;
import java.util.Scanner;

public class ProducaoPostoVista implements Vista {
    private RestauranteControlador controlador;
    private String idFunc;

    public ProducaoPostoVista(RestauranteControlador ctrl, String id) {
        this.controlador = ctrl;
        this.idFunc = id;
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);

        List<Tarefa> tarefas = controlador.getTarefasDoPosto(idFunc);

        System.out.println("\n--- FILA DE PRODUÇÃO (ITENS INDIVIDUAIS) ---");

        if (tarefas.isEmpty()) {
            System.out.println("Não existem itens pendentes para o seu posto.");
            System.out.println("Pressione Enter para voltar ou 'R' para atualizar...");
            if(sc.nextLine().equalsIgnoreCase("R")) return this;
            return new GestaoRestauranteVista(controlador, idFunc);
        }

        for (Tarefa t : tarefas) {
            System.out.println(t.toString());
        }

        System.out.println("\nOpções:");
        System.out.println("[ID da Tarefa] -> Marcar como Concluída");
        System.out.println("[R] -> Atualizar Lista");
        System.out.println("[V] -> Voltar ao Menu Principal");
        System.out.print("> ");

        String input = sc.nextLine().trim();

        if (input.equalsIgnoreCase("V")) return new GestaoRestauranteVista(controlador, idFunc);
        if (input.equalsIgnoreCase("R")) return this;

        try {
            int idTarefa = Integer.parseInt(input);
            controlador.concluirTarefa(idTarefa);
            System.out.println("Tarefa concluida com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Por favor, insira um ID de tarefa válido ou um comando (R/V).");
        } catch (Exception e) {
            System.out.println("Erro ao processar tarefa: " + e.getMessage());
        }

        return this;
    }
}