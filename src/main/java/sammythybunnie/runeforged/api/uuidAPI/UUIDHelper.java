package sammythybunnie.runeforged.api.uuidAPI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.net.PlayerProfile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class UUIDHelper implements ClientModInitializer {
    private static final HashMap<String, UUID> playerDataMapping = new HashMap<>();
    private static final Map<UUID, PerWorldState> playerStateMap = new HashMap<>();

    @Override
    public void onInitializeClient() {
        init();
    }

    public static void onJoin(PlayerProfile player) {
        String playerName = player.getDisplayName();
        UUID uuid = playerDataMapping.get(playerName);

        if (uuid == null) {
            uuid = getUUIDFromMojang(playerName);
            if (uuid != null) {
                playerDataMapping.put(playerName, uuid);
                System.out.println("Player Data Cached: Username - " + playerName + ", UUID - " + uuid);

                // Create a per-world state for the player
                PerWorldState perWorldState = new PerWorldState();
                playerStateMap.put(uuid, perWorldState);
            }
        }
    }

    private static UUID getUUIDFromMojang(String username) {
        try {
            URL mojangApiURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) mojangApiURL.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String jsonResponse = scanner.useDelimiter("\\A").next();
                scanner.close();

                String playerUUID = parseUUIDFromMojangResponse(jsonResponse);
                if (playerUUID != null) {
                    UUID uuid = UUID.fromString(insertHyphens(playerUUID));
                    System.out.println("UUID from Mojang: " + uuid);
                    return uuid;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseUUIDFromMojangResponse(String response) {
        try {
            JsonObject json = new JsonParser().parse(response).getAsJsonObject();
            return json.get("id").getAsString();
        } catch (JsonSyntaxException | IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init() {
        // Initialization logic
        // Example: You might want to load player data from files or databases here
    }

    public static void setScore(UUID uuid, String worldName, int score) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            perWorldState.setScore(worldName, score);
        }
    }

    public static int getScore(UUID uuid, String worldName) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            return perWorldState.getScore(worldName);
        }
        return 0;
    }

    public static void setStatus(UUID uuid, String worldName, boolean status) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            perWorldState.setStatus(worldName, status);
        }
    }

    public static boolean getStatus(UUID uuid, String worldName) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            return perWorldState.getStatus(worldName);
        }
        return false;
    }

    // A simple class to represent per-world state for a player
    private static class PerWorldState {
        private Map<String, Integer> playerScores = new HashMap<>();
        private Map<String, Boolean> playerStatus = new HashMap<>();

        public void setScore(String worldName, int score) {
            playerScores.put(worldName, score);
        }

        public int getScore(String worldName) {
            return playerScores.getOrDefault(worldName, 0);
        }

        public void setStatus(String worldName, boolean status) {
            playerStatus.put(worldName, status);
        }

        public boolean getStatus(String worldName) {
            return playerStatus.getOrDefault(worldName, false);
        }
    }

    private static String insertHyphens(String input) {
        return input.substring(0, 8) + "-" + input.substring(8, 12) + "-"
                + input.substring(12, 16) + "-" + input.substring(16, 20) + "-"
                + input.substring(20, 32);
    }
}
