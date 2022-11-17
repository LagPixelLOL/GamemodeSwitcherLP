package atmt.v2ray.gamemodeswitcherlp.command;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.util.Utils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
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

public class SpeedLimitCommand extends Command {

    public SpeedLimitCommand() {
        super("speedlimit", GSLP_ADMIN.toString(), (byte)4);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        Predicate<ServerCommandSource> infoPermissionChecker = PermissionChecker
                .requireAny(new String[]{getPermission(), SPEEDLIMIT_INFO.toString()}, 0);
        Predicate<ServerCommandSource> adminPermissionChecker = PermissionChecker
                .require(getPermission(), getDefaultPermissionLevel());
        LiteralCommandNode<ServerCommandSource> speedLimitNode = literal(getName()).requires(infoPermissionChecker)
                .executes(this).build();
        LiteralArgumentBuilder<ServerCommandSource> infoBuilder = literal("info");
        infoBuilder.requires(infoPermissionChecker).executes(this);
        speedLimitNode.addChild(infoBuilder.build());
        RequiredArgumentBuilder<ServerCommandSource, Double> setSpeedLimitArgument =
                argument("blocksPerSecond", DoubleArgumentType.doubleArg()).executes(this::setSpeedLimit);
        LiteralCommandNode<ServerCommandSource> setNode = literal("set").requires(adminPermissionChecker)
                .then(setSpeedLimitArgument).build();
        LiteralCommandNode<ServerCommandSource> setSpeedLimitNode = literal("speedlimit").requires(adminPermissionChecker)
                .then(setSpeedLimitArgument).build();
        LiteralCommandNode<ServerCommandSource> setNotifyConsoleNode = literal("notifyconsole").requires(adminPermissionChecker)
                .then(argument("shouldNotifyConsole", BoolArgumentType.bool()).executes(this::setNotifyConsole)).build();
        setNode.addChild(setSpeedLimitNode);
        setNode.addChild(setNotifyConsoleNode);
        speedLimitNode.addChild(setNode);
        registerNode.accept(speedLimitNode);
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        double speedLimit = Config.getConfig().speedLimit();
        context.getSource().sendMessage(Text.literal(GamemodeSwitcherLP.prefix +
                String.format("Current speed limit: %s",
                        speedLimit >= 0 ? String.format("§e%f BPS", speedLimit) : "§cCancelled")));
    }

    private int setSpeedLimit(CommandContext<ServerCommandSource> context) {
        double blocksPerSecond = DoubleArgumentType.getDouble(context, "blocksPerSecond");
        Config.getConfig().speedLimit(blocksPerSecond);
        ServerCommandSource source = context.getSource();
        if (blocksPerSecond >= 0) {
            source.sendMessage(Text.literal(prefix +
                    String.format("§6Set speed limit to §e%f§6 blocks per second.", blocksPerSecond)));
            logger.info(String.format("%s set speed limit to %f blocks per second.",
                    source.getDisplayName().getString(), blocksPerSecond));
        } else {
            source.sendMessage(Text.literal(prefix + "§cCancelled §6speed limit."));
            logger.info(String.format("%s cancelled speed limit.", source.getDisplayName().getString()));
        }
        return 1;
    }

    private int setNotifyConsole(CommandContext<ServerCommandSource> context) {
        boolean shouldNotifyConsole = BoolArgumentType.getBool(context, "shouldNotifyConsole");
        Config.getConfig().notifyConsole(shouldNotifyConsole);
        ServerCommandSource source = context.getSource();
        source.sendMessage(Text.literal(prefix +
                String.format("§6Set notify console to %s§6.", Utils.boolToColouredString(shouldNotifyConsole))));
        logger.info(String.format("%s set notify console to %b.", source.getDisplayName().getString(), shouldNotifyConsole));
        return 1;
    }
}
