package atmt.v2ray.gamemodeswitcherlp.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MinecraftLocation {
    private Vec3d pos;
    private float pitch;
    private float headYaw;
    private RegistryKey<World> dimension;

    public MinecraftLocation(ServerPlayerEntity player) {
        this(player.getWorld().getRegistryKey(), Vec3d.ZERO.add(player.getPos()), player.getHeadYaw(), player.getPitch());
    }

    public MinecraftLocation(RegistryKey<World> dimension, double x, double y, double z) {
        this(dimension, x, y, z, 0, 0);
    }

    public MinecraftLocation(RegistryKey<World> dimension, double x, double y, double z, float headYaw, float pitch) {
        this(dimension, new Vec3d(x, y, z), headYaw, pitch);
    }

    public MinecraftLocation(RegistryKey<World> dimension, Vec3d pos, float headYaw, float pitch) {
        this.dimension = dimension;
        this.pos = pos;
        this.headYaw = headYaw;
        this.pitch = pitch;
    }

    public MinecraftLocation(NbtCompound tag) {
        loadNbt(tag);
    }

    public static MinecraftLocation fromNbt(NbtCompound tag) {
        return new MinecraftLocation(tag);
    }

    private void loadNbt(NbtCompound tag) {
        dimension = RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(tag.getString("WorldRegistryKey")));
        pos = new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        headYaw = tag.getFloat("headYaw");
        pitch = tag.getFloat("pitch");
    }

    public NbtCompound asNbt() {
        return writeNbt(new NbtCompound());
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putString("WorldRegistryKey", getDimension().getValue().toString());
        tag.putDouble("x", getPos().x);
        tag.putDouble("y", getPos().y);
        tag.putDouble("z", getPos().z);
        tag.putFloat("headYaw", getHeadYaw());
        tag.putFloat("pitch", getPitch());
        return tag;
    }

    public Vec3d getPos() {
        return pos;
    }

    public float getPitch() {
        return pitch;
    }

    public float getHeadYaw() {
        return headYaw;
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }
}
