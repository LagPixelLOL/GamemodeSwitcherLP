package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.feature.PacketLimiter;
import atmt.v2ray.gamemodeswitcherlp.interfaces.SPNHMixinAccessor;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (!(listener instanceof ServerPlayNetworkHandler nwHandler)) {
            return;
        }
        if (((SPNHMixinAccessor)nwHandler).onPlayerPacketReceived()) {
            PacketLimiter.onPlayerExceedLimit(nwHandler.player);
            ci.cancel();
        }
    }
}
