package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP;
import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.feature.SpeedLimiter;
import atmt.v2ray.gamemodeswitcherlp.interfaces.SPNHMixinAccessor;
import atmt.v2ray.gamemodeswitcherlp.permission.Permissions;
import atmt.v2ray.gamemodeswitcherlp.util.ServerUtils;
import atmt.v2ray.gamemodeswitcherlp.util.SpeedUtils;
import atmt.v2ray.gamemodeswitcherlp.util.Utils;
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
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class SPNHMixin implements SPNHMixinAccessor {
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
                ServerUtils.notifyOps(Text.literal(GamemodeSwitcherLP.prefix + "§c").append(displayName)
                        .append(Text.literal(String.format("§c is moving too fast! §3(BPS:%f)", blocksPerSecond))),
                        Permissions.SPEEDLIMIT_NOTIFY, 4);
                if (Config.getConfig().slNotifyConsole()) {
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
                ServerUtils.notifyOps(Text.literal(GamemodeSwitcherLP.prefix + "§c").append(displayName)
                        .append(Text.literal(String.format("§c's vehicle is moving too fast! §3(BPS:%f)", blocksPerSecond))),
                        Permissions.SPEEDLIMIT_NOTIFY, 4);
                if (Config.getConfig().slNotifyConsole()) {
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

    private final AtomicInteger packetsPerSecond = new AtomicInteger();

    public boolean onPlayerPacketReceived() {
        packetsPerSecond.incrementAndGet();
        long packetLimit = Config.getConfig().packetLimit();
        return packetLimit > 0 && packetsPerSecond.get() > packetLimit;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (Utils.getTicks() % 20 != 0) {
            return;
        }
        packetsPerSecond.set(0);
    }
}
