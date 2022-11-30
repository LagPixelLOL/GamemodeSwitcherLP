package atmt.v2ray.gamemodeswitcherlp.mixin;

import atmt.v2ray.gamemodeswitcherlp.feature.ServerListPingNotifier;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.LegacyQueryHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.InetSocketAddress;

@Mixin(LegacyQueryHandler.class)
public class LQHMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getServerMotd()Ljava/lang/String;",
            ordinal = 0, shift = At.Shift.BEFORE), method = "channelRead", locals = LocalCapture.CAPTURE_FAILHARD)
    private void channelRead_O0(ChannelHandlerContext ctx, Object msg, CallbackInfo ci, ByteBuf byteBuf,
                                boolean bl, InetSocketAddress inetSocketAddress) {
        ServerListPingNotifier.onClientPing(
                ServerListPingNotifier.ClientVersion.LEGACY_1, inetSocketAddress.getAddress().getHostAddress());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getServerMotd()Ljava/lang/String;",
            ordinal = 1, shift = At.Shift.BEFORE), method = "channelRead", locals = LocalCapture.CAPTURE_FAILHARD)
    private void channelRead_O1(ChannelHandlerContext ctx, Object msg, CallbackInfo ci, ByteBuf byteBuf,
                                boolean bl, InetSocketAddress inetSocketAddress) {
        ServerListPingNotifier.onClientPing(
                ServerListPingNotifier.ClientVersion.LEGACY_2, inetSocketAddress.getAddress().getHostAddress());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getServerMotd()Ljava/lang/String;",
            ordinal = 2, shift = At.Shift.BEFORE), method = "channelRead", locals = LocalCapture.CAPTURE_FAILHARD)
    private void channelRead_O2(ChannelHandlerContext ctx, Object msg, CallbackInfo ci, ByteBuf byteBuf,
                                boolean bl, InetSocketAddress inetSocketAddress) {
        ServerListPingNotifier.onClientPing(
                ServerListPingNotifier.ClientVersion.LEGACY_3, inetSocketAddress.getAddress().getHostAddress());
    }
}
