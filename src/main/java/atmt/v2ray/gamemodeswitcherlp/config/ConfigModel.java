package atmt.v2ray.gamemodeswitcherlp.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "gslp", wrapperName = "GSLPConfig")
public class ConfigModel {
    public double speedLimit = -1;
    public boolean slNotifyConsole = true;
    public int notifyServerListPingIntervalSeconds = -1;
    public long packetLimit = 100;
    public String packetExceedCommand = "kick %player% &cYou are sending too many packets!";
    public boolean plNotifyConsole = true;

    //deprecate-update
    public boolean notifyConsole = true;
}
