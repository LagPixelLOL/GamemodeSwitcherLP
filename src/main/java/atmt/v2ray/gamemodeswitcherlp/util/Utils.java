package atmt.v2ray.gamemodeswitcherlp.util;

public class Utils {

    public static int getTicks() {
        return ServerUtils.getServer().getTicks();
    }

    public static String boolToColouredString(boolean bool) {
        return bool ? "§atrue" : "§cfalse";
    }

    public static String getSeparator() {
        return "--------------------------------------------------";
    }
}
