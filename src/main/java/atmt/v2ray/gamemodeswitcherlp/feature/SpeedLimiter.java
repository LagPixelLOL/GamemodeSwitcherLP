package atmt.v2ray.gamemodeswitcherlp.feature;

import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import atmt.v2ray.gamemodeswitcherlp.util.ServerUtils;
import atmt.v2ray.gamemodeswitcherlp.util.SpeedUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpeedLimiter {
    /**
     * Key = Player entity, Value = Teleport information.
     */
    private static final Map<ServerPlayerEntity, TeleportInfo> playerMap = new ConcurrentHashMap<>();

    public static boolean exceededSpeedLimit(Vec3d velocity, ServerPlayerEntity player) {
        if (PermissionChecker.check(player, Permissions.SPEEDLIMIT_BYPASS.toString(), 4)
                || player.isInTeleportationState()) {
            return false;
        }
        double speedLimit = Config.getConfig().speedLimit();
        double blocksPerSecond = SpeedUtils.bptToBps(velocity.length());
        return speedLimit >= 0 && blocksPerSecond > speedLimit;
    }

    public static void onPlayerTeleport(ServerPlayerEntity player, Vec3d teleportTarget) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        playerMap.put(player, new TeleportInfo(teleportTarget, server.getTicks(), Integer.MAX_VALUE));
    }

    public static void updatePlayerPositionPacket(ServerPlayerEntity player, Vec3d packetPosition) {
        TeleportInfo teleportInfo = playerMap.get(player);
        MinecraftServer server = player.getServer();
        if (teleportInfo == null || server == null) {
            return;
        }
        if (teleportInfo.removeTick <= server.getTicks()) {
            playerMap.remove(player);
            return;
        }
        if (server.getTicks() - teleportInfo.teleportTick > 60
                || teleportInfo.targetPosition.distanceTo(packetPosition) < 3.5) {
            teleportInfo.removeTick = server.getTicks() + 1;
        }
    }

    public static boolean playerNotRecentlyTeleported(ServerPlayerEntity player) {
        return !playerMap.containsKey(player);
    }

    public static void notifyOps(Text message) {
        ServerUtils.getPlayers().forEach(player -> {
            if (PermissionChecker.check(player, Permissions.SPEEDLIMIT_NOTIFY.toString(), 4)) {
                player.sendMessage(message);
            }
        });
    }

    private static class TeleportInfo {
        private final Vec3d targetPosition;
        private final int teleportTick;
        private int removeTick;

        private TeleportInfo(Vec3d targetPosition, int teleportTick, int removeTick) {
            this.targetPosition = targetPosition;
            this.teleportTick = teleportTick;
            this.removeTick = removeTick;
        }
    }
}
