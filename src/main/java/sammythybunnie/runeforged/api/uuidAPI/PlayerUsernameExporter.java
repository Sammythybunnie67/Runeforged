package sammythybunnie.runeforged.api.uuidAPI;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.net.PlayerProfile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerUsernameExporter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Object init;

    public static void exportPlayerUsername(PlayerProfile player) {
        File gameDirectory = FabricLoader.getInstance().getGameDir().toFile();
        File configDirectory = new File(gameDirectory, "config");

        String username = player.playerName;
        //create a json object for player usernames
        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("username", username);



        //convert json object to json string
        String jsonString = gson.toJson(playerJson);

        //specify the path to the json file
        String modFolder = "Runeforged";
        String uuidFolder = "UUIDs";
        String fileName = "player_username.json";

        File usernameFile = new File(configDirectory, modFolder + "/" + uuidFolder);
        File uuidFolderFile = new File(configDirectory, modFolder + "/" + uuidFolder);
        uuidFolderFile.mkdirs();

        File folder = new File(configDirectory, modFolder + "/" + uuidFolder);
        folder.mkdirs();

        String filePath = new File(folder, fileName).getAbsolutePath();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonString);
            System.out.println("player Username Exported to Json File");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error exporting players username");
        }
    }


    public static void init() {
    }
}



