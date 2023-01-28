package atmt.v2ray.gamemodeswitcherlp.feature;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import atmt.v2ray.gamemodeswitcherlp.util.ServerUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PacketLimiter {

    public static void onPlayerExceedLimit(ServerPlayerEntity player) {
        if (PermissionChecker.check(player, Permissions.PACKETLIMIT_BYPASS.toString(), 4)) {
            return;
        }
        ServerUtils.executeAsConsole(Config.getConfig().packetExceedCommand().replace("%player%", player.getEntityName()));
        Text displayName = player.getDisplayName();
        ServerUtils.notifyOps(Text.literal(GamemodeSwitcherLP.prefix + "§c").append(displayName)
                .append(Text.literal("§c sent too many packets!")), Permissions.PACKETLIMIT_NOTIFY, 4);
        if (Config.getConfig().plNotifyConsole()) {
            GamemodeSwitcherLP.logger.warn(String.format("%s sent too many packets!", displayName.getString()));
        }
    }
}
