package sammythybunnie.runeforged;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sammythybunnie.runeforged.api.uuidAPI.PlayerUUIDRetreiver;
import sammythybunnie.runeforged.api.uuidAPI.PlayerUsernameExporter;

import java.io.File;



public class RuneforgedMod implements ModInitializer {

    @SuppressWarnings("ReassignedVariable")
    public static void ModInitialisation() {
            PlayerUsernameExporter.init(); // Initialize PlayerUsernameExporter first
            PlayerUUIDRetreiver.init(); // Initialize PlayerUUIDRetriever
        }

    public static final String MOD_ID = "runeforged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("runeforged initialized.");
    }
}
