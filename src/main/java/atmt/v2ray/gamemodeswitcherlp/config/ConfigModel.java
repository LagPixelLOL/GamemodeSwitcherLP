package atmt.v2ray.gamemodeswitcherlp.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "gslp", wrapperName = "GSLPConfig")
public class ConfigModel {
    public double speedLimit = -1;
    public boolean notifyConsole = true;
    public int notifyServerListPingIntervalSeconds = -1;
    public int packetLimit = 100;
}
