package ca.cmpt213.a4.client.model;

import java.time.LocalDateTime;

/**
 * Uses the factory design pattern to create instances of Food and Drink objects
 */
public class ConsumableFactory {

    public Consumable getInstance(Integer consumableType, String name, String notes, double price, double quantity, LocalDateTime expiryDate) {
        if (consumableType == null) return null;
        if (consumableType == Consumable.FOOD) return new Food(name, notes, price, quantity, expiryDate);
        if (consumableType == Consumable.DRINK) return new Drink(name, notes, price, quantity, expiryDate);
        return null;
    }
}
