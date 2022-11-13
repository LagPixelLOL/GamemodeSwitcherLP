package atmt.v2ray.gamemodeswitcherlp.mixin;

import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    /**
     * Vanilla Bug Fix - When teleported to another dimension, player's xp bar will not display xp.
     */
    @Inject(at = @At("RETURN"), method = "sendPlayerStatus")
    private void sendPlayerStatus(ServerPlayerEntity player, CallbackInfo ci) {
        player.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(
                player.experienceProgress, player.totalExperience, player.experienceLevel));
    }
}
