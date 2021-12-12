package ca.cmpt213.a4.webappserver.model;

import java.util.Collection;

/**
 * Allows consumables to be saved to a GSON file
 */
public class ConsumableCollection {
    private final Collection<Food> food;
    private final Collection<Drink> drinks;

    public ConsumableCollection(Collection<Food> food, Collection<Drink> drinks) {
        this.food = food;
        this.drinks = drinks;
    }

    public Collection<Food> getFood() {
        return food;
    }

    public Collection<Drink> getDrinks() {
        return drinks;
    }
}
