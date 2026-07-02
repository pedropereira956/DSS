package RestaurantesUI;

import RestaurantesLN.ssVendas.ModoEntrega;

import java.util.*;

public class EcraTatilVista implements Vista {
    private RestauranteControlador controlador;
    private Map<String, Integer> carrinho;

    public EcraTatilVista(RestauranteControlador controlador) {
        this.controlador = controlador;
        this.carrinho = new HashMap<>();
    }

    public Vista run() {
        Scanner sc = new Scanner(System.in);
        String idRest = controlador.getRestauranteAtual();

        System.out.println("\n--- SELF-SERVICE (" + idRest + ") ---");
        System.out.println("Menus Disponíveis:");

        Map<String, Double> menusMap = controlador.getMenusComStock(idRest);
        List<String> nomesMenus = new ArrayList<>(menusMap.keySet());

        if (nomesMenus.isEmpty()) {
            System.out.println("[Aviso: Não há menus disponíveis de momento]");
        } else {
            for (int i = 0; i < nomesMenus.size(); i++) {
                String nome = nomesMenus.get(i);
                Double preco = menusMap.get(nome);
                System.out.println((i + 1) + ". " + nome + " (" + preco + "€)");
            }
        }

        System.out.println("\nOpções: [Nº do Menu] para adicionar | 'F' Finalizar | 'V' Voltar");
        System.out.print("> ");
        String input = sc.nextLine();

        if (input.equalsIgnoreCase("F")) {
            return finalizarPedido(idRest);
        } else if (input.equalsIgnoreCase("V")) {
            return new EscolherUtilizador(controlador);
        }

        try {
            int escolha = Integer.parseInt(input);
            if (escolha > 0 && escolha <= nomesMenus.size()) {
                String nomeSelecionado = nomesMenus.get(escolha - 1);

                carrinho.put(nomeSelecionado, carrinho.getOrDefault(nomeSelecionado, 0) + 1);

                System.out.println("\n-----------------------------------------");
                System.out.println("✓ " + nomeSelecionado + " adicionado!");

                System.out.println("CARRINHO ATUAL:");
                double totalParcial = 0;
                for (Map.Entry<String, Integer> item : carrinho.entrySet()) {
                    String nome = item.getKey();
                    int qtd = item.getValue();
                    double precoUnitario = menusMap.get(nome);
                    double subtotal = precoUnitario * qtd;
                    totalParcial += subtotal;

                    System.out.printf("  - %d x %-20s (%.2f€)%n", qtd, nome, subtotal);
                }
                System.out.printf("TOTAL PARCIAL: %.2f€%n", totalParcial);
                System.out.println("-----------------------------------------");

            } else {
                System.out.println("Número fora do intervalo disponível.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite o número do menu ou 'F'/'V'.");
        }

        return this;
    }

    private Vista finalizarPedido(String idRest) {
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return new EscolherUtilizador(controlador);
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("\nAlguma nota especial? (Ex: sem cebola, alergias) [ENTER para ignorar]");
        System.out.print("> ");
        String notas = sc.nextLine();

        System.out.println("\nEscolha o Método de Pagamento:");
        List<String> metodos = controlador.getMetodosPagamentoDisponiveis();
        for (int i = 0; i < metodos.size(); i++) {
            System.out.println((i + 1) + ". " + metodos.get(i));
        }
        System.out.print("> ");
        String metodoSelecionado = "DINHEIRO";
        try {
            int op = Integer.parseInt(sc.nextLine());
            if (op > 0 && op <= metodos.size()) metodoSelecionado = metodos.get(op - 1);
        } catch (Exception e) { System.out.println("Definido DINHEIRO por defeito."); }

        System.out.println("\nEscolha o Modo de Entrega:");
        ModoEntrega[] modos = ModoEntrega.values();
        for (int i = 0; i < modos.length; i++) {
            System.out.println((i + 1) + ". " + modos[i].name());
        }
        System.out.print("> ");

        String modoSelecionado = ModoEntrega.Local.name(); // Padrão
        try {
            int opModo = Integer.parseInt(sc.nextLine());
            if (opModo > 0 && opModo <= modos.length) {
                modoSelecionado = modos[opModo - 1].name();
            }
        } catch (Exception e) {
            System.out.println("Opção inválida, definido como Local por defeito.");
        }

        try {
            int numTicket = controlador.confirmarPedido(idRest, carrinho, notas, metodoSelecionado, modoSelecionado);

            System.out.println("\n*****************************************");
            System.out.println("   PEDIDO CONFIRMADO! SENHA NÚMERO: " + numTicket);
            System.out.println("   Entrega: " + modoSelecionado);
            System.out.println("   Pagamento: " + metodoSelecionado);
            System.out.println("   Aguarde na zona de entrega.");
            System.out.println("*****************************************\n");
        } catch (Exception e) {
            System.out.println("Erro ao processar pedido: " + e.getMessage());
        }

        return new EscolherUtilizador(controlador);
    }
}