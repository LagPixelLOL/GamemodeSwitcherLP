package atmt.v2ray.gamemodeswitcherlp.permission;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import me.lucko.fabric.api.permissions.v0.Permissions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PermissionChecker {

    private static boolean isSuperAdmin(CommandSource source) {
        return source.hasPermissionLevel(4);
    }

    public static @NotNull Predicate<ServerCommandSource> require(@NotNull String permission, int defaultRequireLevel) {
        return player -> check(player, permission, defaultRequireLevel);
    }

    public static @NotNull Predicate<ServerCommandSource> requireAny(@NotNull String[] permissions, int defaultRequireLevel) {
        return player -> checkAny(player, permissions, defaultRequireLevel);
    }

    public static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequireLevel) {
        return Permissions.getPermissionValue(source, permission)
                .orElse(source.hasPermissionLevel(Math.max(0, defaultRequireLevel)));
    }

    public static boolean checkAny(@NotNull CommandSource source, @NotNull String[] permissions, int defaultRequireLevel) {
        for (String permission : permissions) {
            if (check(source, permission, defaultRequireLevel)) {
                return true;
            }
        }
        return false;
    }
}
