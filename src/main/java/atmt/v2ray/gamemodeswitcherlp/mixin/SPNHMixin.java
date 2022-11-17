package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.feature.SpeedLimiter;
import atmt.v2ray.gamemodeswitcherlp.util.SpeedUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class SPNHMixin {
    @Shadow public ServerPlayerEntity player;
    @Shadow private double lastTickX;
    @Shadow private double lastTickY;
    @Shadow private double lastTickZ;
    @Shadow private double lastTickRiddenX;
    @Shadow private double lastTickRiddenY;
    @Shadow private double lastTickRiddenZ;
    @Shadow @Final public ClientConnection connection;
    @Shadow public abstract void requestTeleport(double x, double y, double z, float yaw, float pitch);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;" +
            "getBoundingBox()Lnet/minecraft/util/math/Box;", shift = At.Shift.BEFORE),
            method = "onPlayerMove", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci,
                              ServerWorld serverWorld, double packetX, double packetY, double packetZ) {
        SpeedLimiter.updatePlayerPositionPacket(player, new Vec3d(packetX, packetY, packetZ));
        Vec3d velocity = new Vec3d(packetX - lastTickX, packetY - lastTickY, packetZ - lastTickZ);
        if (SpeedLimiter.exceededSpeedLimit(velocity, player)) {
            double blocksPerSecond = SpeedUtils.bptToBps(velocity.length());
            if (SpeedLimiter.playerNotRecentlyTeleported(player)) {
                player.sendMessage(Text.literal(GamemodeSwitcherLP.prefix +
                        String.format("§cYou are moving too fast! §3(BPS:%f)", blocksPerSecond)));
                Text displayName = player.getDisplayName();
                SpeedLimiter.notifyOps(Text.literal(GamemodeSwitcherLP.prefix + "§c").append(displayName)
                        .append(Text.literal(String.format("§c is moving too fast! §3(BPS:%f)", blocksPerSecond))));
                if (Config.getConfig().notifyConsole()) {
                    GamemodeSwitcherLP.logger.warn(String.format("%s is moving too fast! (BPS:%f)",
                            displayName.getString(), blocksPerSecond));
                }
            }
            requestTeleport(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;" +
            "isSpaceEmpty(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z", ordinal = 0,
            shift = At.Shift.BEFORE), method = "onVehicleMove", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onVehicleMove(VehicleMoveC2SPacket packet, CallbackInfo ci, Entity entityRiding, ServerWorld serverWorld,
                               double entityX, double entityY, double entityZ, double packetX, double packetY, double packetZ) {
        SpeedLimiter.updatePlayerPositionPacket(player, new Vec3d(packetX, packetY, packetZ));
        Vec3d velocity = new Vec3d(packetX - lastTickRiddenX, packetY - lastTickRiddenY, packetZ - lastTickRiddenZ);
        if (SpeedLimiter.exceededSpeedLimit(velocity, player)) {
            double blocksPerSecond = SpeedUtils.bptToBps(velocity.length());
            if (SpeedLimiter.playerNotRecentlyTeleported(player)) {
                player.sendMessage(Text.literal(GamemodeSwitcherLP.prefix +
                        String.format("§cYour vehicle is moving too fast! §3(BPS:%f)", blocksPerSecond)));
                Text displayName = player.getDisplayName();
                SpeedLimiter.notifyOps(Text.literal(GamemodeSwitcherLP.prefix + "§c").append(displayName)
                        .append(Text.literal(String.format("§c's vehicle is moving too fast! §3(BPS:%f)", blocksPerSecond))));
                if (Config.getConfig().notifyConsole()) {
                    GamemodeSwitcherLP.logger.warn(String.format("%s's vehicle is moving too fast! (BPS:%f)",
                            displayName.getString(), blocksPerSecond));
                }
            }
            connection.send(new VehicleMoveS2CPacket(entityRiding));
            ci.cancel();
        }
    }

    @Inject(at = @At("RETURN"), method = "requestTeleport(DDDFFLjava/util/Set;Z)V")
    private void requestTeleport(double x, double y, double z, float yaw, float pitch,
                                 Set<PlayerPositionLookS2CPacket.Flag> flags, boolean shouldDismount, CallbackInfo ci) {
        SpeedLimiter.onPlayerTeleport(player, new Vec3d(x, y, z));
    }
}
