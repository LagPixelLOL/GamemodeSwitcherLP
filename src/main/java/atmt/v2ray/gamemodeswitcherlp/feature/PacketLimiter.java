package atmt.v2ray.gamemodeswitcherlp.feature;

import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.util.Utils;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketLimiter {
    /**
     * Map that stores the UUID and the number of packets sent in the last 20 ticks.
     */
    private static final Map<UUID, Integer> playerPacketMap = new ConcurrentHashMap<>();

    /**
     * Method that listens to packet receive.
     * @param packetListener the listener for the packet.
     */
    public static void onPacketReceive(PacketListener packetListener) {
        if (!(packetListener instanceof ServerPlayNetworkHandler nwHandler)) {
            return;
        }
        ServerPlayerEntity player = nwHandler.player;
        playerPacketMap.compute(player.getGameProfile().getId(), (uuid, count) -> {
            int currentCount = count == null ? 0 : count;
            if (isExceeded(player)) {
                onPlayerExceedLimit(player);
            }
            return ++currentCount;
        });
    }

    /**
     * Method that ticks every 20 ticks.
     */
    public static void onServerTick() {
        if (Utils.getTicks() % 20 != 0) {
            return;
        }
        playerPacketMap.replaceAll((uuid, count) -> 0);
    }

    /**
     * Method that gets the packet count for the given player.
     * @param player the server player.
     * @return the packet count for the given player.
     */
    private static int getPacketCount(ServerPlayerEntity player) {
        return playerPacketMap.getOrDefault(player.getGameProfile().getId(), 0);
    }

    /**
     * Method that checks if the player exceeded the packet limit.
     * @param player the server player.
     * @return true if the player exceeded the packet limit, false otherwise.
     */
    private static boolean isExceeded(ServerPlayerEntity player) {
        return getPacketCount(player) > Config.getConfig().packetLimit();
    }

    /**
     * Method that executes when the player exceeded the packet limit.
     * @param player the server player.
     */
    private static void onPlayerExceedLimit(ServerPlayerEntity player) {
        player.sendMessage(Text.literal(String.valueOf(getPacketCount(player)))
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))); //del
    }
}
