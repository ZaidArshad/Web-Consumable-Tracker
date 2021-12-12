package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Parents of Food and Drink
 * Keep track of expiry dates of consumables
 */
public interface Consumable extends Comparable<Consumable> {
    int FOOD = 1;
    int DRINK = 2;
    Integer getType();
    String getName();
    String getNotes();
    double getPrice();
    double getQuantity();
    LocalDateTime getExpiryDate();

    default boolean isExpired() {
        return getExpiryDate().isBefore(LocalDateTime.now().minusDays(1));
    }

    default boolean doesExpiresIn7Days() {
        return getExpiryDate().isAfter(LocalDateTime.now().minusDays(1)) &&
                getExpiryDate().isBefore(LocalDateTime.now().minusDays(-8));
    }

    /**
     * Gets the number of days till the expiry date.
     * If negative that means how many days past the expiry date.
     * @return How many days until the expiry date
     */
    private long getDaysTillExpiry() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), getExpiryDate());
    }

    /**
     * Gets the human-readable version of the expiry date.
     * @return Description of the status of the consumable's expiry date
     */
    default String getExpiringString() {
        long days = getDaysTillExpiry();
        if (days == 0) return "This item will expire today.\n";
        else if (days > 0) return "This item will expire in " + (days+1) + " day(s).\n";
        else return "The item has been expired for " + -days + " day(s).\n";
    }

    /**
     * Formats the expiry date to be more readable.
     * @return String of the expiry date in "yyyy-MM-dd"
     */
    default String formattedExpiryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(getExpiryDate());
    }

    /**
     * Checks if the given consumable is expires before the caller
     * @param consumable Consumable object to be compared
     * @return 1 if the caller expires before the given consumable
     *         0 if the caller expires after the given consumable or same day
     */
    @Override
    default int compareTo(Consumable consumable) {
        if (consumable.getExpiryDate().isBefore(getExpiryDate())) return 1;
        else return 0;
    }
}
