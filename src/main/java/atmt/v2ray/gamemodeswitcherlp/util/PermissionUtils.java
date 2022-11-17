package atmt.v2ray.gamemodeswitcherlp.util;

import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionUtils {

    public static void register(Permissions permission, ServerPlayerEntity player) {
        me.lucko.fabric.api.permissions.v0.Permissions.check(player, permission.toString());
    }
}
