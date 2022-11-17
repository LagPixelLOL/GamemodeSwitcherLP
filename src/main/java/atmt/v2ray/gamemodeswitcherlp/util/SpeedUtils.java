package atmt.v2ray.gamemodeswitcherlp.util;

public class SpeedUtils {

    /**
     * Convert blocks per tick to blocks per second.
     * @param blocksPerTick Blocks per game tick(1/20 second).
     * @return Blocks per second(20 ticks).
     */
    public static double bptToBps(double blocksPerTick) {
        return blocksPerTick * 20;
    }
}
