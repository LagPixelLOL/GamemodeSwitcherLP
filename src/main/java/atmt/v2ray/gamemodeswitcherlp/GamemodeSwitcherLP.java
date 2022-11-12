package atmt.v2ray.gamemodeswitcherlp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GamemodeSwitcherLP implements ModInitializer {
    private static final String MOD_CONTAINER_ID = "gamemodeswitcherlp";
    public static final ModMetadata MOD_METADATA = FabricLoader.getInstance().getModContainer(MOD_CONTAINER_ID)
            .map(ModContainer::getMetadata).orElse(null);
    public static final String MOD_VERSION = MOD_METADATA == null ? "unknown" : MOD_METADATA.getVersion().getFriendlyString();
    public static final String prefix = "§3[§e§lGS§b§lL§3§lP§3] ";
    public static final Logger logger = LogManager.getLogger("GSLP");

    /**
     * Initialize.
     */
    @Override
    public void onInitialize() {
        GSLPRegistry.addCommands();
        GSLPRegistry.registerCommands();
        logger.info("GSLP Loaded.");
    }
}
