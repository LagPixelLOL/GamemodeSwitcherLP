package atmt.v2ray.gamemodeswitcherlp;

import atmt.v2ray.gamemodeswitcherlp.command.*;
import atmt.v2ray.gamemodeswitcherlp.command.gamemode.GmCommand;
import atmt.v2ray.gamemodeswitcherlp.command.gamemode.SpecCommand;
import atmt.v2ray.gamemodeswitcherlp.event.ServerTickListener;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import atmt.v2ray.gamemodeswitcherlp.util.PermissionUtils;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.util.IConsumer;

public class GSLPRegistry {
    private static boolean permissionRegistered = false;

    public static void registerEvents() {
        ServerTickEvents.START_SERVER_TICK.register(new ServerTickListener());
    }

    public static void addCommands() {
        Command.add(new GmCommand());
        Command.add(new GmCommand.GmsCommand());
        Command.add(new GmCommand.GmcCommand());
        Command.add(new GmCommand.GmspCommand());
        Command.add(new GmCommand.GmaCommand());
        SpecCommand specCommand = new SpecCommand();
        Command.add(specCommand);
        Command.add(specCommand.getSCommand());
        Command.add(new GSLPCommand());
        Command.add(new SuicideCommand());
        Command.add(new SpeedLimitCommand());
        Command.add(new SLPingNotifierCommand());
        Command.add(new PacketLimitCommand());
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RootCommandNode<ServerCommandSource> rootNode = dispatcher.getRoot();
            IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode = rootNode::addChild;
            Command.getCommands().forEach(command -> command.register(registerNode));
        });
    }

    public static void registerPermission(ServerPlayerEntity player) {
        if (permissionRegistered) {
            return;
        }
        for (Permissions permission : Permissions.values()) {
            PermissionUtils.register(permission, player);
        }
        permissionRegistered = true;
    }
}
