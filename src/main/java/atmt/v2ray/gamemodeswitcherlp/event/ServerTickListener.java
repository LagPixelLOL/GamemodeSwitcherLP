package atmt.v2ray.gamemodeswitcherlp.event;

import atmt.v2ray.gamemodeswitcherlp.util.ServerUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class ServerTickListener implements ServerTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftServer server) {
        ServerUtils.setServer(server);
    }
}
