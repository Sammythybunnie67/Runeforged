package sammythybunnie.runeforged.api.uuidAPI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.net.HttpURLConnection;
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
    private static final Map<UUID, Integer> playerManaMap = new HashMap<>();

    @Override
    public void onInitializeClient() {
        init();
    }

    public static void onJoin(EntityPlayerMP playerMP) {
        String playerName = playerMP.username;
        UUID uuid = getUUIDFromMojang(playerName);

        if (uuid != null) {
            playerDataMapping.put(playerName, uuid);
            System.out.println("Player Data Cached: Username - " + playerName + ", UUID - " + uuid);

            // Create a per-world state for the player
            PerWorldState perWorldState = new PerWorldState();
            playerStateMap.put(uuid, perWorldState);

            // Initialize mana value for the player
            playerManaMap.put(uuid, 0); // Initial mana value
        } else {
            System.out.println("Failed to obtain UUID for player: " + playerName);
            // Create a per-world state for the player without UUID
            PerWorldState perWorldState = new PerWorldState();
            playerStateMap.put(UUID.randomUUID(), perWorldState);
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
                    return UUID.fromString(insertHyphens(playerUUID));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  UUID getPlayerUUIDFor(EntityPlayer player) {
        String playerName = player.username;
        return playerDataMapping.get(playerName);
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

    private static String insertHyphens(String input) {
        return input.substring(0, 8) + "-" + input.substring(8, 12) + "-"
                + input.substring(12, 16) + "-" + input.substring(16, 20) + "-"
                + input.substring(20, 32);
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

    public static void setMana(UUID uuid, String worldName, int mana) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            perWorldState.setMana(worldName, mana);
        }
    }

    public static int getMana(UUID uuid, String worldName) {
        PerWorldState perWorldState = playerStateMap.get(uuid);
        if (perWorldState != null) {
            return perWorldState.getMana(worldName);
        }
        return 0;
    }

    // A simple class to represent per-world state for a player
    private static class PerWorldState {
        private Map<String, Integer> playerScores = new HashMap<>();
        private Map<String, Boolean> playerStatus = new HashMap<>();
        private Map<String, Integer> playerMana = new HashMap<>();

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

        public void setMana(String worldName, int mana) {
            playerMana.put(worldName, mana);
        }

        public int getMana(String worldName) {
            return playerMana.getOrDefault(worldName, 0);
        }

    }
}

