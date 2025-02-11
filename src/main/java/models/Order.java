package models;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<String> ingredients;

    public Order() {
        ingredients = new ArrayList<>();
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}