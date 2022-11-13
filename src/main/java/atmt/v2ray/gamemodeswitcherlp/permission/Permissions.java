package atmt.v2ray.gamemodeswitcherlp.permission;

public enum Permissions {
    GSLP("gslp"),
    GAMEMODE(GSLP + ".gamemode"),
    GAMEMODE_SURVIVAL(GAMEMODE + ".survival"),
    GAMEMODE_CREATIVE(GAMEMODE + ".creative"),
    GAMEMODE_SPECTATOR(GAMEMODE + ".spectator"),
    GAMEMODE_ADVENTURE(GAMEMODE + ".adventure"),
    SPECTATE(GSLP + ".spectate"),
    SUICIDE(GSLP + ".suicide"),
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
