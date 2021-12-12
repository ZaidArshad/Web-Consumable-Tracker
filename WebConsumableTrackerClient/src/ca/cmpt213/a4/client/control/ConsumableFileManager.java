package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.control.ServerRequest;
import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Static class that allows saving and loading of consumable objects
 */
public class ConsumableFileManager {

    /**
     * Loads the json file from the web server
     * @return The loaded items from the json file on the web server
     */
    public static ArrayList<Consumable> loadConsumables() {
        return parseConsumableList("listAll");
    }

    public static ArrayList<Consumable> parseConsumableList(String command) {
        Gson gson = createGson();
        ConsumableCollection consumableCollection =
                gson.fromJson(ServerRequest.requestGET(command), ConsumableCollection.class);
        ArrayList<Consumable> consumableItems = new ArrayList<>();

        // Splits up the collection into 2 types
        if (consumableCollection != null) {
            if (consumableCollection.getDrinks() != null) consumableItems.addAll(consumableCollection.getDrinks());
            if (consumableCollection.getFood() != null) consumableItems.addAll(consumableCollection.getFood());
        }
        else return new ArrayList<>();

        return sortByExpiry(consumableItems);
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
