package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.feature.PacketLimiter;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(at = @At("HEAD"), method = "handlePacket")
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        PacketLimiter.onPacketReceive(listener);
    }
}
