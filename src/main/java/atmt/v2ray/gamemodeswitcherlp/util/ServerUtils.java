package atmt.v2ray.gamemodeswitcherlp.util;

import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ServerUtils {
    private static MinecraftServer server;

    /**
     * Execute command as console.
     * @param command Command to execute.
     */
    public static void executeAsConsole(String command) {
        getServer().getCommandManager().executeWithPrefix(getServer().getCommandSource(), Utils.parseColour(command));
    }

    /**
     * Notify all ops with a message.
     * @param message Message to send.
     * @param permission Permission to check.
     * @param defaultRequireLevel Default vanilla permission level.
     */
    public static void notifyOps(Text message, Permissions permission, int defaultRequireLevel) {
        getPlayers().forEach(player -> {
            if (PermissionChecker.check(player, permission.toString(), defaultRequireLevel)) {
                player.sendMessage(message);
            }
        });
    }

    /**
     * Get all players on the server.
     * @return List of players.
     */
    public static List<ServerPlayerEntity> getPlayers() {
        List<ServerPlayerEntity> players = new ArrayList<>();
        getServer().getWorlds().forEach(world -> players.addAll(world.getPlayers()));
        return players;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        ServerUtils.server = server;
    }

    /**
     * Convert seconds to tick count.
     * @param seconds Seconds to convert.
     * @return Tick count.
     */
    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }
}
