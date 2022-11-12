package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.MeCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MeCommand.class)
public class MeCommandMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;" +
            "literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;"), method = "register")
    private static LiteralArgumentBuilder<ServerCommandSource> literal(String literal) {
        return CommandManager.literal(literal).requires(PermissionChecker
                .require(Permissions.MINECRAFT_ME.toString(), 0));
    }
}
