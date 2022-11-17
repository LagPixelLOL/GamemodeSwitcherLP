package atmt.v2ray.gamemodeswitcherlp.command;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.util.IConsumer;

import static atmt.v2ray.gamemodeswitcherlp.permission.Permissions.*;
import static net.minecraft.server.command.CommandManager.*;

public class GSLPCommand extends Command {

    public GSLPCommand() {
        super("gslp", GSLP.toString(), (byte)0);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        LiteralCommandNode<ServerCommandSource> gslpNode = literal(getName()).requires(
                PermissionChecker.require(getPermission(), getDefaultPermissionLevel())).executes(this).build();
        LiteralArgumentBuilder<ServerCommandSource> infoBuilder = literal("info");
        LiteralArgumentBuilder<ServerCommandSource> reloadBuilder = literal("reload");
        infoBuilder.requires(PermissionChecker.require(getPermission(), getDefaultPermissionLevel())).executes(this);
        reloadBuilder.requires(PermissionChecker.require(GSLP_ADMIN.toString(), 4)).executes(context -> {
            Config.reload();
            ServerCommandSource source = context.getSource();
            source.sendMessage(Text.literal(GamemodeSwitcherLP.prefix + "§6Reload config success!"));
            GamemodeSwitcherLP.logger.info(String.format("%s reloaded GamemodeSwitcherLP config.",
                    source.getDisplayName().getString()));
            return 1;
        });
        gslpNode.addChild(infoBuilder.build());
        gslpNode.addChild(reloadBuilder.build());
        registerNode.accept(gslpNode);
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.literal(GamemodeSwitcherLP.prefix + "§eGamemodeSwitcher§bL§3P" +
                "\n §eDescription §3- §6Serverside mod for switching gamemodes with luckperms support." +
                "\n §eVersion §3- §6" + GamemodeSwitcherLP.MOD_VERSION));
    }
}
