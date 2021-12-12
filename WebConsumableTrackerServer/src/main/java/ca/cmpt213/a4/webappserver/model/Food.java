package ca.cmpt213.a4.webappserver.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * Consumable object that keeps track of food by expiry date
 */
public class Food implements Consumable{
    private final Integer type;
    private final String name;
    private final String notes;
    private final double price;
    private final double quantity;
    private final LocalDateTime expiryDate;

    public Food(String name, String notes, double price, double quantity, LocalDateTime expiryDate) {
        this.type = Consumable.FOOD;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * Enable the object to be passed as a string in the shell.
     * @return The fine details of the consumable
     */
    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        return  "<html>" +
                "<br/>Name: " + name +
                "<br/>Notes: " + notes +
                "<br/>Price: $" + decimalFormat.format(price) +
                "<br/>Weight: " + quantity +
                "<br/>Expiry date: " + formattedExpiryDate() +
                "<br/>" + getExpiringString()
                + "<html>";
    }
}
