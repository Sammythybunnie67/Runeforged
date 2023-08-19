package sammythybunnie.runeforged.api.uuidAPI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class PlayerUUIDRetreiver implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        init();
    }

    public static void init() {
        File gameDirectory = FabricLoader.getInstance().getGameDir().toFile();
        File configDirectory = new File(gameDirectory, "config");

        String modFolder = "Runeforged";
        String uuidFolder = "UUIDs";
        String usernameFileName = "player_username.json";
        String uuidFileName = "UUID.json";

        File usernameFile = new File(configDirectory, modFolder + "/" + usernameFileName);
        File uuidFolderFile = new File(configDirectory, modFolder + "/" + uuidFolder);
        uuidFolderFile.mkdirs();

        if (!usernameFile.exists()) {
            System.err.println("Username Json File Not Found");
        }
        try {
            JsonParser parser = new JsonParser();
            JsonObject usernameJson = (JsonObject) parser.parse(new FileReader(usernameFile));
            String username = usernameJson.get("username").getAsString(); // Assuming the key is "username"

            // Query the Mojang API
            URL mojangApiURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) mojangApiURL.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String jsonResponse = scanner.useDelimiter("\\A").next();
                scanner.close();

                JsonObject uuidJson = parser.parse(jsonResponse).getAsJsonObject();
                String playerUUID = uuidJson.get("id").getAsString();

                JsonObject playerUUIDJson = new JsonObject();
                playerUUIDJson.addProperty("uuid", playerUUID);

                // Convert to JSON string
                String jsonString = playerUUIDJson.toString();

                // Save the UUID to a JSON file
                String uuidFilePath = uuidFolderFile.getAbsolutePath() + "/" + uuidFileName;
                try (FileWriter writer = new FileWriter(uuidFilePath)) {
                    writer.write(jsonString);
                    System.out.println("Player UUID exported to json file");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error reading or connecting to files/api");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
