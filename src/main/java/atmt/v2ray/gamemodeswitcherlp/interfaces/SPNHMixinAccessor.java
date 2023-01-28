package atmt.v2ray.gamemodeswitcherlp.interfaces;

public interface SPNHMixinAccessor {

    /**
     * @return True if the player has exceeded the packet limit.
     */
    boolean onPlayerPacketReceived();
}
