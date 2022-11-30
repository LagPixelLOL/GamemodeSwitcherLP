package atmt.v2ray.gamemodeswitcherlp.command;

import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.util.IConsumer;

import java.util.function.Predicate;

import static atmt.v2ray.gamemodeswitcherlp.permission.Permissions.*;
import static net.minecraft.server.command.CommandManager.*;
import static atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP.*;

public class SLPingNotifierCommand extends Command {

    public SLPingNotifierCommand() {
        super("serverlistpingnotifier", GSLP_ADMIN.toString(), (byte)4);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        Predicate<ServerCommandSource> adminPermissionChecker = PermissionChecker
                .require(getPermission(), getDefaultPermissionLevel());
        LiteralCommandNode<ServerCommandSource> SLPNNode = literal(getName()).requires(adminPermissionChecker)
                .executes(this).build();
        LiteralArgumentBuilder<ServerCommandSource> infoBuilder = literal("info");
        infoBuilder.requires(adminPermissionChecker).executes(this);
        SLPNNode.addChild(infoBuilder.build());
        RequiredArgumentBuilder<ServerCommandSource, Integer> setIntervalArgument =
                argument("seconds", IntegerArgumentType.integer()).executes(this::setInterval);
        LiteralCommandNode<ServerCommandSource> setNode = literal("set").requires(adminPermissionChecker)
                .then(setIntervalArgument).build();
        LiteralCommandNode<ServerCommandSource> setIntervalNode = literal("interval").requires(adminPermissionChecker)
                .then(setIntervalArgument).build();
        setNode.addChild(setIntervalNode);
        SLPNNode.addChild(setNode);
        registerNode.accept(SLPNNode);
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        int interval = Config.getConfig().notifyServerListPingIntervalSeconds();
        context.getSource().sendMessage(Text.literal(prefix +
                String.format("Current Server List Ping Notifier interval: %s", interval > 0 ?
                        String.format("§e%d second%s", interval, interval != 1 ? "s" : "") : "§cOFF")));
    }

    private int setInterval(CommandContext<ServerCommandSource> context) {
        int interval = IntegerArgumentType.getInteger(context, "seconds");
        Config.getConfig().notifyServerListPingIntervalSeconds(interval);
        ServerCommandSource source = context.getSource();
        if (interval > 0) {
            String plural = interval != 1 ? "s" : "";
            source.sendMessage(Text.literal(prefix +
                    String.format("§6Set Server List Ping Notifier interval to §e%d§6 second%s.", interval, plural)));
            logger.info(String.format("%s set Server List Ping Notifier interval to %d second%s.",
                    source.getDisplayName().getString(), interval, plural));
        } else {
            source.sendMessage(Text.literal(prefix + "§6Turned §cOFF §6Server List Ping Notifier."));
            logger.info(String.format("%s turned OFF Server List Ping Notifier.", source.getDisplayName().getString()));
        }
        return 1;
    }
}
