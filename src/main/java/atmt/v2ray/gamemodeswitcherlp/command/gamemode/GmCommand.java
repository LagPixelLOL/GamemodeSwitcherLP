package atmt.v2ray.gamemodeswitcherlp.command.gamemode;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.command.Command;
import atmt.v2ray.gamemodeswitcherlp.util.GamemodeSetter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.util.IConsumer;

import static atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker.*;
import static atmt.v2ray.gamemodeswitcherlp.permission.Permissions.*;
import static net.minecraft.server.command.CommandManager.*;

public class GmCommand extends Command {

    public GmCommand() {
        super("gm", GAMEMODE.toString(), (byte)4);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        LiteralArgumentBuilder<ServerCommandSource> gmBuilder = literal(getName());
        LiteralArgumentBuilder<ServerCommandSource> survivalBuilder = literal("s");
        LiteralArgumentBuilder<ServerCommandSource> creativeBuilder = literal("c");
        LiteralArgumentBuilder<ServerCommandSource> spectatorBuilder = literal("sp");
        LiteralArgumentBuilder<ServerCommandSource> adventureBuilder = literal("a");
        LiteralCommandNode<ServerCommandSource> gmNode = gmBuilder.requires(requireAny(new String[]{
                getPermission(), GAMEMODE_SURVIVAL.toString(), GAMEMODE_CREATIVE.toString(),
                        GAMEMODE_SPECTATOR.toString(), GAMEMODE_ADVENTURE.toString()},
                getDefaultPermissionLevel())).executes(this).build();
        survivalBuilder.requires(require(GAMEMODE_SURVIVAL.toString(), getDefaultPermissionLevel()))
                .executes(context -> GamemodeSetter.setGamemode(context, GameMode.SURVIVAL));
        creativeBuilder.requires(require(GAMEMODE_CREATIVE.toString(), getDefaultPermissionLevel()))
                .executes(context -> GamemodeSetter.setGamemode(context, GameMode.CREATIVE));
        spectatorBuilder.requires(require(GAMEMODE_SPECTATOR.toString(), getDefaultPermissionLevel()))
                .executes(context -> GamemodeSetter.setGamemode(context, GameMode.SPECTATOR));
        adventureBuilder.requires(require(GAMEMODE_ADVENTURE.toString(), getDefaultPermissionLevel()))
                .executes(context -> GamemodeSetter.setGamemode(context, GameMode.ADVENTURE));
        gmNode.addChild(survivalBuilder.build());
        gmNode.addChild(creativeBuilder.build());
        gmNode.addChild(spectatorBuilder.build());
        gmNode.addChild(adventureBuilder.build());
        registerNode.accept(gmNode);
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.literal(GamemodeSwitcherLP.prefix
                + "§6s §3- §esurvival §3| §6c §3- §ecreative §3| §6sp §3- §espectator §3| §6a §3- §eadventure"));
    }

    public static class GmsCommand extends Command {

        public GmsCommand() {
            super("gms", GAMEMODE_SURVIVAL.toString(), (byte)4);
        }

        @Override
        public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
            registerNode.accept(literal(getName()).requires(require(getPermission(),
                    getDefaultPermissionLevel())).executes(this).build());
        }

        @Override
        public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            GamemodeSetter.setGamemode(context, GameMode.SURVIVAL);
        }
    }

    public static class GmcCommand extends Command {

        public GmcCommand() {
            super("gmc", GAMEMODE_CREATIVE.toString(), (byte)4);
        }

        @Override
        public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
            registerNode.accept(literal(getName()).requires(require(getPermission(),
                    getDefaultPermissionLevel())).executes(this).build());
        }

        @Override
        public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            GamemodeSetter.setGamemode(context, GameMode.CREATIVE);
        }
    }

    public static class GmspCommand extends Command {

        public GmspCommand() {
            super("gmsp", GAMEMODE_SPECTATOR.toString(), (byte)4);
        }

        @Override
        public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
            registerNode.accept(literal(getName()).requires(require(getPermission(),
                    getDefaultPermissionLevel())).executes(this).build());
        }

        @Override
        public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            GamemodeSetter.setGamemode(context, GameMode.SPECTATOR);
        }
    }

    public static class GmaCommand extends Command {

        public GmaCommand() {
            super("gma", GAMEMODE_ADVENTURE.toString(), (byte)4);
        }

        @Override
        public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
            registerNode.accept(literal(getName()).requires(require(getPermission(),
                    getDefaultPermissionLevel())).executes(this).build());
        }

        @Override
        public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            GamemodeSetter.setGamemode(context, GameMode.ADVENTURE);
        }
    }
}
