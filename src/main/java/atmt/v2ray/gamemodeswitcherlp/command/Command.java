package atmt.v2ray.gamemodeswitcherlp.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.util.IConsumer;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements com.mojang.brigadier.Command<ServerCommandSource> {
    private static final List<Command> commands = new ArrayList<>();
    private final String commandName;
    private final String commandPermission;
    private final byte defaultPermissionLevel;

    public Command(String commandName, String commandPermission, byte defaultPermissionLevel) {
        this.commandName = commandName;
        this.commandPermission = commandPermission;
        this.defaultPermissionLevel = defaultPermissionLevel;
    }

    public static void add(Command command) {
        commands.add(command);
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        execute(context);
        return SINGLE_SUCCESS;
    }

    public abstract void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode);

    public abstract void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException;

    public static List<Command> getCommands() {
        return commands;
    }

    public String getName() {
        return commandName;
    }

    public String getPermission() {
        return commandPermission;
    }

    public byte getDefaultPermissionLevel() {
        return defaultPermissionLevel;
    }
}
