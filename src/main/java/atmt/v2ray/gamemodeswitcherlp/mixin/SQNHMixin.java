package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.feature.ServerListPingNotifier;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(ServerQueryNetworkHandler.class)
public class SQNHMixin {
    @Shadow @Final private ClientConnection connection;

    @Inject(at = @At("TAIL"), method = "onRequest")
    private void onRequest(QueryRequestC2SPacket packet, CallbackInfo ci) {
        SocketAddress socketAddress = connection.getAddress();
        if (socketAddress instanceof InetSocketAddress) {
            ServerListPingNotifier.onClientPing(ServerListPingNotifier.ClientVersion.CURRENT,
                    ((InetSocketAddress)socketAddress).getAddress().getHostAddress());
        }
    }
}
