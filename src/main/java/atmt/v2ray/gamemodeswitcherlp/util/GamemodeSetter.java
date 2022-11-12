package atmt.v2ray.gamemodeswitcherlp.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;

import java.util.Collection;
import java.util.Collections;

public class GamemodeSetter {

    public static int setGamemode(CommandContext<ServerCommandSource> context, GameMode gamemode) throws CommandSyntaxException {
        return setGamemode(context, Collections.singleton(context.getSource().getPlayerOrThrow()), gamemode);
    }

    public static int setGamemode(CommandContext<ServerCommandSource> context,
                                    Collection<ServerPlayerEntity> targets, GameMode gameMode) {
        int i = 0;
        for (ServerPlayerEntity serverPlayerEntity : targets) {
            if (serverPlayerEntity.changeGameMode(gameMode)) {
                sendFeedback(context.getSource(), serverPlayerEntity, gameMode);
                ++i;
            }
        }
        return i;
    }

    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode) {
        Text text = Text.translatable("gameMode." + gameMode.getName());
        if (source.getEntity() == player) {
            source.sendFeedback(Text.translatable("commands.gamemode.success.self", text), true);
        } else {
            if (source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
                player.sendMessage(Text.translatable("gameMode.changed", text));
            }
            source.sendFeedback(Text.translatable("commands.gamemode.success.other", player.getDisplayName(), text),
                    true);
        }
    }
}
