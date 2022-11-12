package atmt.v2ray.gamemodeswitcherlp;

import atmt.v2ray.gamemodeswitcherlp.command.Command;
import atmt.v2ray.gamemodeswitcherlp.command.GSLPCommand;
import atmt.v2ray.gamemodeswitcherlp.command.GmCommand;
import atmt.v2ray.gamemodeswitcherlp.command.SuicideCommand;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.util.IConsumer;

public class GSLPRegistry {

    public static void addCommands() {
        Command.add(new GmCommand());
        Command.add(new GmCommand.GmsCommand());
        Command.add(new GmCommand.GmcCommand());
        Command.add(new GmCommand.GmspCommand());
        Command.add(new GmCommand.GmaCommand());
        Command.add(new GSLPCommand());
        Command.add(new SuicideCommand());
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RootCommandNode<ServerCommandSource> rootNode = dispatcher.getRoot();
            IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode = rootNode::addChild;
            Command.getCommands().forEach(command -> command.register(registerNode));
        });
    }
}