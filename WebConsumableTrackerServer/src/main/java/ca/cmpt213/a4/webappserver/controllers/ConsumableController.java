package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableFileManager;
import ca.cmpt213.a4.webappserver.model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Controller class for REST API to communicate with client
 * Sends and receives objects in JSON format
 */
@RestController
public class ConsumableController {
    public final static Integer SHOW_ALL = 0;
    public final static Integer SHOW_EXPIRED = 1;
    public final static Integer SHOW_NON_EXPIRED = 2;
    public final static Integer SHOW_EXPIRES_IN_7_DAYS = 3;

    ArrayList<Consumable> consumables = ConsumableFileManager.loadConsumables();

    @GetMapping("/ping")
    public String ping() {
        return "System is up!";
    }

    @GetMapping("/listAll")
    public ConsumableCollection listAllItems() {
        return listItems(SHOW_ALL);
    }

    @PostMapping("/addItem/food")
    public ConsumableCollection addFood(@RequestBody Food food) {
        consumables.add(food);
        return ConsumableFileManager.splitConsumableItems(consumables);
    }

    @PostMapping("/addItem/drink")
    public ConsumableCollection addDrink(@RequestBody Drink drink) {
        consumables.add(drink);
        return ConsumableFileManager.splitConsumableItems(consumables);
    }

    @PostMapping("/removeItem")
    public ConsumableCollection removeConsumable(@RequestBody int index) {
        consumables.remove(index);
        return ConsumableFileManager.splitConsumableItems(consumables);
    }

    @GetMapping("/listExpired")
    public ConsumableCollection listExpiredItems() {
        return listItems(SHOW_EXPIRED);
    }

    @GetMapping("/listNonExpired")
    public ConsumableCollection listNonExpiredItems() {
        return listItems(SHOW_NON_EXPIRED);
    }

    @GetMapping("/listExpiringIn7Days")
    public ConsumableCollection listExpiringIn7DaysItems() {
        return listItems(SHOW_EXPIRES_IN_7_DAYS);
    }

    @GetMapping("/exit")
    public String exitSave() {
        ConsumableFileManager.saveConsumables(consumables);
        return "Server has saved all changes.";
    }

    private ConsumableCollection listItems(Integer condition) {
        ArrayList<Consumable> listedItems = new ArrayList<>();

        /* Condition 1: Show all
           Condition 2: Show expired
           Condition 3: Show non-expired
           Condition 4: Show expires in 7 days
        */
        for (Consumable consumable : consumables) {
            if (condition.equals(SHOW_ALL)) {
                listedItems.add(consumable);
            }
            else if (condition.equals(SHOW_EXPIRED) && consumable.isExpired()) {
                listedItems.add(consumable);
            }
            else if (condition.equals(SHOW_NON_EXPIRED) && !consumable.isExpired()) {
                listedItems.add(consumable);
            }
            else if (condition.equals(SHOW_EXPIRES_IN_7_DAYS) && consumable.doesExpiresIn7Days()) {
                listedItems.add(consumable);
            }
        }
        return ConsumableFileManager.splitConsumableItems(listedItems);
    }
}
