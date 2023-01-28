package atmt.v2ray.gamemodeswitcherlp.config;

public class Config {
    private static atmt.v2ray.gamemodeswitcherlp.config.GSLPConfig config;

    public static void initialize() {
        config = atmt.v2ray.gamemodeswitcherlp.config.GSLPConfig.createAndLoad();

        //deprecate-update
        if (!config.notifyConsole()) {
            config.slNotifyConsole(false);
            config.notifyConsole(true);
        }
    }

    public static void reload() {
        config.load();
    }

    public static atmt.v2ray.gamemodeswitcherlp.config.GSLPConfig getConfig() {
        return config;
    }
}
