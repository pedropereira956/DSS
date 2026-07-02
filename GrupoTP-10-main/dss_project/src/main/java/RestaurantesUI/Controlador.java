package RestaurantesUI;

import RestaurantesLN.IRestaurantesLN;

public abstract class Controlador {
    private IRestaurantesLN model;

    public Controlador(IRestaurantesLN model) {
        this.model = model;
    }

    public IRestaurantesLN getModel() {
        return this.model;
    }
}
