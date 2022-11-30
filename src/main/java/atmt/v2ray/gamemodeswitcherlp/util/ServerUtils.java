package atmt.v2ray.gamemodeswitcherlp.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ServerUtils {
    private static MinecraftServer server;

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

    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }
}
