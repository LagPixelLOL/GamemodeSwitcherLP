package atmt.v2ray.gamemodeswitcherlp.command.gamemode;

import atmt.v2ray.gamemodeswitcherlp.command.Command;
import atmt.v2ray.gamemodeswitcherlp.data.MinecraftLocation;
import atmt.v2ray.gamemodeswitcherlp.data.PlayerSpecContainer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.util.IConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP.*;
import static atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker.*;
import static atmt.v2ray.gamemodeswitcherlp.permission.Permissions.*;
import static net.minecraft.server.command.CommandManager.*;

public class SpecCommand extends Command {
    private final SCommand sCommand = new SCommand();
    /**
     * Key = player's uuid, Value = player's location and prev gamemode.
     */
    private static final Map<UUID, PlayerSpecContainer> playerMap = new HashMap<>();

    public SpecCommand() {
        super("spec", SPECTATE.toString(), (byte)4);
    }

    @Override
    public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
        registerNode.accept(literal(getName()).requires(require(getPermission(),
                getDefaultPermissionLevel())).executes(this).build());
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        sCommand.execute(context);
    }

    /**
     * @return /s command instance.
     */
    public SCommand getSCommand() {
        return sCommand;
    }

    public static boolean isPlayerSpectating(ServerPlayerEntity player) {
        return playerMap.containsKey(player.getGameProfile().getId());
    }

    public static class SCommand extends Command {

        public SCommand() {
            super("s", SPECTATE.toString(), (byte)4);
        }

        @Override
        public void register(IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode) {
            registerNode.accept(literal(getName()).requires(require(getPermission(),
                    getDefaultPermissionLevel())).executes(this).build());
        }

        @Override
        public void execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerCommandSource source = context.getSource();
            ServerPlayerEntity player = source.getPlayerOrThrow();
            UUID uuid = player.getGameProfile().getId();
            if (!playerMap.containsKey(uuid)) {
                GameMode prevGameMode = player.interactionManager.getGameMode();
                if (player.changeGameMode(GameMode.SPECTATOR)) {
                    playerMap.put(uuid, new PlayerSpecContainer(new MinecraftLocation(player), prevGameMode));
                    source.sendMessage(Text.literal(prefix + "§6Start spectating, " +
                            "you will be teleported back and stop spectating if you use this command again."));
                } else {
                    source.sendMessage(Text.literal(prefix + "§cYou are already a spectator!"));
                }
                return;
            }
            PlayerSpecContainer playerSpecContainer = playerMap.remove(uuid);
            MinecraftLocation location = playerSpecContainer.location();
            Vec3d pos = location.getPos();
            try {
                player.teleport(Objects.requireNonNull(player.getServer()).getWorld(location.getDimension()),
                        pos.x, pos.y, pos.z, location.getHeadYaw(), location.getPitch());
                player.changeGameMode(playerSpecContainer.prevGameMode());
                source.sendMessage(Text.literal(prefix + "§6Stopped spectating and teleported back."));
            } catch (NullPointerException e) {
                source.sendMessage(Text.literal(prefix + "§cAn error occurred when running this command!"));
                logger.error("Error when trying to teleport a player back.", e);
            }
        }
    }
}
