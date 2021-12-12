package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableCollection;
import ca.cmpt213.a4.webappserver.model.Drink;
import ca.cmpt213.a4.webappserver.model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Static class that allows saving and loading of consumable objects
 */
public class ConsumableFileManager {

    private static final String CONSUMABLE_ITEMS_JSON_FILE_LOCATION = "src/main/java/ca/cmpt213/a4/webappserver/control/itemsList.json";

    /**
     * Loads the json file itemsList.json to the returned object
     * @return The loaded items from the json file
     */
    public static ArrayList<Consumable> loadConsumables() {

        // Creates the gson parser
        Gson gson = createGson();
        ArrayList<Consumable> consumableItems = new ArrayList<>();
        ConsumableCollection consumableCollection = null;

        try {
            // Reads the file to the collection
            Reader reader = Files.newBufferedReader(Paths.get(CONSUMABLE_ITEMS_JSON_FILE_LOCATION));
            consumableCollection = gson.fromJson(reader, ConsumableCollection.class);
            reader.close();
        }
        // This catch block allows the program to ignore the error when there is no file to load
        catch (IOException ignored) {
        }

        // Splits up the collection into 2 types
        if (consumableCollection != null) {
            if (consumableCollection.getDrinks() != null) consumableItems.addAll(consumableCollection.getDrinks());
            if (consumableCollection.getFood() != null) consumableItems.addAll(consumableCollection.getFood());
        }
        else return new ArrayList<>();

        return sortByExpiry(consumableItems);
    }

    public static String getConsumablesJson() {
        Gson gson = createGson();
        return gson.toJson(splitConsumableItems(loadConsumables()));
    }

    public static ConsumableCollection splitConsumableItems(ArrayList<Consumable> consumableItems) {
        ArrayList<Food> foodList = new ArrayList<>();
        ArrayList<Drink> drinkList = new ArrayList<>();

        for (Consumable consumable: consumableItems) {
            if (consumable.getType() == Consumable.FOOD) foodList.add((Food) consumable);
            else if (consumable.getType() == Consumable.DRINK) drinkList.add((Drink) consumable);
        }
        return new ConsumableCollection(foodList, drinkList);
    }

    /**
     * Saves the given list of consumables to itemsList.json
     * @param consumableItems A list of items
     */
    public static void saveConsumables(ArrayList<Consumable> consumableItems)  {

        // Creates the gson parser and writes the list to json format
        Gson gson = createGson();
        String consumableJson = gson.toJson(splitConsumableItems(consumableItems));

        try {
            // Write the json to file
            FileWriter file = new FileWriter(CONSUMABLE_ITEMS_JSON_FILE_LOCATION);
            file.write(consumableJson);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows LocalDateTime to be saved into the Json file
     * @return A Gson object to parse LocalDateTime objects
     */
    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
    }

    /**
     * Sorts the consumables in the list by oldest
     * @param consumableList List of consumables to be ordered
     * @return The ordered list
     */
    public static ArrayList<Consumable> sortByExpiry(ArrayList<Consumable> consumableList) {
        Consumable[] consumableArr = consumableListToArray(consumableList);

        if (consumableArr.length == 0) return consumableList;

        for (int i = 1; i < consumableArr.length; i++) {
            Consumable oldest = consumableArr[i];
            int j = i - 1;
            while (j >= 0 && consumableArr[j].getExpiryDate().isAfter(oldest.getExpiryDate())) {
                consumableArr[j+1] = consumableArr[j];
                j = j - 1;
            }
            consumableArr[j + 1] = oldest;
        }

        return new ArrayList<>(List.of(consumableArr));
    }

    /**
     * Converts a list to an array
     * Using built-in functions causes errors
     * @param consumableItems List to be copied
     * @return Array of consumables
     */
    private static Consumable[] consumableListToArray(ArrayList<Consumable> consumableItems) {
        Consumable[] consumableArr = new Consumable[consumableItems.size()];
        for (int i = 0; i < consumableItems.size(); i++) {
            consumableArr[i] = consumableItems.get(i);
        }
        return consumableArr;
    }
}
