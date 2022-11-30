package atmt.v2ray.gamemodeswitcherlp.data;

import net.minecraft.world.GameMode;

public record PlayerSpecContainer(MinecraftLocation location, GameMode prevGameMode) {}
