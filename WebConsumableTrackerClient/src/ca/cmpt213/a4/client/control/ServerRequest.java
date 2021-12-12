package ca.cmpt213.a4.client.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Class that makes use of static functions to send requests to server
 * Receives and sends objects in JSON format
 */
public class ServerRequest {
    public static String requestGET(String mapping) {
        String received = "";
        String command = "curl -X GET localhost:8080/" + mapping;
        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            received = scanner.nextLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return received;
    }

    public static String requestPOST(String mapping, Object consumable) {
        Gson gson = createGson();
        String received = "";
        String command = "curl -H \"Content-Type: application/json\" -X POST" +
                " -d "  + addEscapeQuotes(gson.toJson(consumable)) + " localhost:8080/" + mapping;
        try {
            Process process = Runtime.getRuntime().exec(command);
            Scanner scanner = new Scanner(process.getInputStream());
            received = scanner.nextLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return received;
    }

    private static String addEscapeQuotes(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\"') {
                stringBuilder.append("\\");
            }
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
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
}
