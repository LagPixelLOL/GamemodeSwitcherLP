package atmt.v2ray.gamemodeswitcherlp.command;

import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.util.Utils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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

public class PacketLimitCommand extends Command {

    public PacketLimitCommand() {
        super("packetlimit", GSLP_ADMIN.toString(), (byte)4);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        Predicate<ServerCommandSource> infoPermissionChecker = PermissionChecker
                .requireAny(new String[]{getPermission(), PACKETLIMIT_INFO.toString()}, 0);
        Predicate<ServerCommandSource> adminPermissionChecker = PermissionChecker
                .require(getPermission(), getDefaultPermissionLevel());
        LiteralCommandNode<ServerCommandSource> packetLimitNode = literal(getName()).requires(infoPermissionChecker)
                .executes(this).build();
        LiteralArgumentBuilder<ServerCommandSource> infoBuilder = literal("info");
        infoBuilder.requires(infoPermissionChecker).executes(this);
        packetLimitNode.addChild(infoBuilder.build());
        RequiredArgumentBuilder<ServerCommandSource, Long> setPacketLimitArgument =
                argument("packetsPerSecond", LongArgumentType.longArg()).executes(this::setPacketLimit);
        LiteralCommandNode<ServerCommandSource> setNode = literal("set").requires(adminPermissionChecker)
                .then(setPacketLimitArgument).build();
        LiteralCommandNode<ServerCommandSource> setPacketLimitNode = literal("packetlimit").requires(adminPermissionChecker)
                .then(setPacketLimitArgument).build();
        LiteralCommandNode<ServerCommandSource> setNotifyConsoleNode = literal("notifyconsole").requires(adminPermissionChecker)
                .then(argument("shouldNotifyConsole", BoolArgumentType.bool()).executes(this::setNotifyConsole)).build();
        LiteralCommandNode<ServerCommandSource> setPacketExceedCommandNode = literal("packetexceedcommand")
                .requires(adminPermissionChecker).then(argument("command", StringArgumentType.greedyString())
                        .executes(this::setPacketExceedCommand)).build();
        setNode.addChild(setPacketLimitNode);
        setNode.addChild(setNotifyConsoleNode);
        setNode.addChild(setPacketExceedCommandNode);
        packetLimitNode.addChild(setNode);
        registerNode.accept(packetLimitNode);
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        long packetLimit = Config.getConfig().packetLimit();
        context.getSource().sendMessage(Text.literal(prefix + String.format("Current packet limit: %s",
                packetLimit > 0 ? String.format("§e%d PPS", packetLimit) : "§cCancelled")));
    }

    private int setPacketLimit(CommandContext<ServerCommandSource> context) {
        long packetsPerSecond = LongArgumentType.getLong(context, "packetsPerSecond");
        Config.getConfig().packetLimit(packetsPerSecond);
        ServerCommandSource source = context.getSource();
        if (packetsPerSecond > 0) {
            source.sendMessage(Text.literal(prefix +
                    String.format("§6Set packet limit to §e%d§6 packets per second.", packetsPerSecond)));
            logger.info(String.format("%s set packet limit to %d packets per second.",
                    source.getDisplayName().getString(), packetsPerSecond));
        } else {
            source.sendMessage(Text.literal(prefix + "§cCancelled §6packet limit."));
            logger.info(String.format("%s cancelled packet limit.", source.getDisplayName().getString()));
        }
        return 1;
    }

    private int setNotifyConsole(CommandContext<ServerCommandSource> context) {
        boolean shouldNotifyConsole = BoolArgumentType.getBool(context, "shouldNotifyConsole");
        Config.getConfig().plNotifyConsole(shouldNotifyConsole);
        ServerCommandSource source = context.getSource();
        source.sendMessage(Text.literal(prefix +
                String.format("§6Set packet limit notify console to %s§6.", Utils.boolToColouredString(shouldNotifyConsole))));
        logger.info(String.format("%s set packet limit notify console to %b.",
                source.getDisplayName().getString(), shouldNotifyConsole));
        return 1;
    }

    private int setPacketExceedCommand(CommandContext<ServerCommandSource> context) {
        String command = StringArgumentType.getString(context, "command");
        Config.getConfig().packetExceedCommand(command);
        ServerCommandSource source = context.getSource();
        source.sendMessage(Text.literal(prefix + String.format("§6Set packet exceed command to \"§e%s§6\".", command)));
        logger.info(String.format("%s set packet exceed command to \"%s\".", source.getDisplayName().getString(), command));
        return 1;
    }
}
