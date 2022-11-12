package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.permission.PermissionChecker;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.SayCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(SayCommand.class)
public class SayCommandMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;" +
            "requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", remap = false),
            method = "register")
    private static ArgumentBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>> requires(
            LiteralArgumentBuilder<ServerCommandSource> builder, Predicate<ServerCommandSource> predicate) {
        return builder.requires(PermissionChecker.require(Permissions.MINECRAFT_SAY.toString(), 2));
    }
}
