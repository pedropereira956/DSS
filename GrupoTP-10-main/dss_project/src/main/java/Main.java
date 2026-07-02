import RestaurantesLN.IRestaurantesLN;
import RestaurantesLN.RestaurantesLNFacade;
import RestaurantesUI.EscolherRestauranteVista;
import RestaurantesUI.EscolherUtilizador;
import RestaurantesUI.RestauranteControlador;
import RestaurantesUI.Vista;

public class Main {
    public static void main(String[] args) throws Exception {
        IRestaurantesLN restaurantes = new RestaurantesLNFacade();
        RestauranteControlador controlador = new RestauranteControlador(restaurantes);
        Vista nextView = new EscolherRestauranteVista(controlador);

        do {
            nextView = nextView.run();
        } while (nextView != null);
    }
}
