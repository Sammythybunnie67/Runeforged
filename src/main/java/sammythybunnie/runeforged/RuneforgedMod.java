package sammythybunnie.runeforged;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sammythybunnie.runeforged.api.uuidAPI.UUIDHelper;


public class RuneforgedMod implements ModInitializer {

    public static final String MOD_ID = "runeforged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UUIDHelper.init();
        LOGGER.info("runeforged initialized.");
        LOGGER.info("uuids and usernames obtained sucsessfully");
    }
}
