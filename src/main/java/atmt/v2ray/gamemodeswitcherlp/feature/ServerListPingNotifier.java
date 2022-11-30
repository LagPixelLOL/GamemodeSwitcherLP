package atmt.v2ray.gamemodeswitcherlp.feature;

import atmt.v2ray.gamemodeswitcherlp.config.Config;
import atmt.v2ray.gamemodeswitcherlp.config.GSLPConfig;
import atmt.v2ray.gamemodeswitcherlp.util.ServerUtils;
import atmt.v2ray.gamemodeswitcherlp.util.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static atmt.v2ray.gamemodeswitcherlp.GamemodeSwitcherLP.logger;

public class ServerListPingNotifier {
    private static final ConcurrentHashMap<PingInfo, Integer> pingCountMap = new ConcurrentHashMap<>();

    public static void onClientPing(ClientVersion version, String ip) {
        PingInfo pingInfo = new PingInfo(version, ip);
        if (pingCountMap.computeIfPresent(pingInfo, (info, pingCount) -> ++pingCount) != null) {
            return;
        }
        pingCountMap.putIfAbsent(pingInfo, 1);
    }

    public static void onServerTick() {
        int ticks = Utils.getTicks();
        GSLPConfig config = Config.getConfig();
        int intervalSeconds = config.notifyServerListPingIntervalSeconds();
        if (intervalSeconds > 0 && ticks % ServerUtils.secondsToTicks(intervalSeconds) == 0 && !pingCountMap.isEmpty()) {
            logger.info(Utils.getSeparator());
            logger.info(String.format("Server List Ping(s) in the last %d seconds:", intervalSeconds));
            List<Map.Entry<PingInfo, Integer>> pingedInfo = new ArrayList<>(pingCountMap.entrySet());
            pingCountMap.clear();
            pingedInfo.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
            pingedInfo.forEach(entry -> {
                PingInfo pingInfo = entry.getKey();
                logger.info(String.format(
                        "%s pinged %d time(s) using mc version %s.", pingInfo.ip(), entry.getValue(), pingInfo.version()));
            });
            logger.info(Utils.getSeparator());
        }
    }

    private record PingInfo(ClientVersion version, String ip) {}

    public enum ClientVersion {
        LEGACY_1("<= 1.3.*"),
        LEGACY_2("1.4 - 1.5.*"),
        LEGACY_3("1.6.*"),
        CURRENT(">= 1.7.*");

        private final String message;

        ClientVersion(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
