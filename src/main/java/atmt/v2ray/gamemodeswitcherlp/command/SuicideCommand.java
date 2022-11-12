package atmt.v2ray.gamemodeswitcherlp.command;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.util.IConsumer;

import static atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker.*;
import static atmt.v2ray.gamemodeswitcherlp.permission.Permissions.*;
import static net.minecraft.server.command.CommandManager.*;

public class SuicideCommand extends Command {

    public SuicideCommand() {
        super("suicide", SUICIDE.toString(), (byte)0);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        registerNode.accept(literal(getName()).requires(require(getPermission(),
                getDefaultPermissionLevel())).executes(this).build());
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        source.getEntityOrThrow().kill();
        source.sendFeedback(Text.literal(GamemodeSwitcherLP.prefix + "§cYou killed yourself!"), false);
    }
}
