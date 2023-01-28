package atmt.v2ray.gamemodeswitcherlp.permission;

public enum Permissions {
    GSLP("gslp"),
    GSLP_ADMIN(GSLP + ".admin"),
    GAMEMODE(GSLP + ".gamemode"),
    GAMEMODE_SURVIVAL(GAMEMODE + ".survival"),
    GAMEMODE_CREATIVE(GAMEMODE + ".creative"),
    GAMEMODE_SPECTATOR(GAMEMODE + ".spectator"),
    GAMEMODE_ADVENTURE(GAMEMODE + ".adventure"),
    SPECTATE(GSLP + ".spectate"),
    SUICIDE(GSLP + ".suicide"),
    SPEEDLIMIT(GSLP + ".speedlimit"),
    SPEEDLIMIT_INFO(SPEEDLIMIT + ".info"),
    SPEEDLIMIT_BYPASS(SPEEDLIMIT + ".bypass"),
    SPEEDLIMIT_NOTIFY(SPEEDLIMIT + ".notify"),
    PACKETLIMIT(GSLP + ".packetlimit"),
    PACKETLIMIT_INFO(PACKETLIMIT + ".info"),
    PACKETLIMIT_BYPASS(PACKETLIMIT + ".bypass"),
    PACKETLIMIT_NOTIFY(PACKETLIMIT + ".notify"),
    MINECRAFT("minecraft"),
    MINECRAFT_ME(MINECRAFT + ".me"),
    MINECRAFT_SAY(MINECRAFT + ".say");

    private final String permissionID;

    Permissions(String permissionID) {
        this.permissionID = permissionID;
    }

    public String getPermissionID() {
        return permissionID;
    }

    @Override
    public String toString() {
        return getPermissionID();
    }
}
