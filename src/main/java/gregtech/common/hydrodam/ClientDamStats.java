package gregtech.common.hydrodam;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.GTValues;

/// The client's view of one volume's litres and net change, fetched from the server on demand for WAILA. Only the
/// most recently requested volume is kept.
public final class ClientDamStats {

    /// How often the stats of the volume being looked at are requested again, in ticks.
    private static final long REQUEST_INTERVAL = 10;

    /// One volume's stats, as last received. Immutable.
    public static final class Stats {

        public final int dimension;
        public final int volumeId;
        public final double litres;
        public final double netLitresPerTick;

        public Stats(int dimension, int volumeId, double litres, double netLitresPerTick) {
            this.dimension = dimension;
            this.volumeId = volumeId;
            this.litres = litres;
            this.netLitresPerTick = netLitresPerTick;
        }
    }

    private static volatile Stats latest;

    private static int requestedDimension;
    private static int requestedVolumeId;
    private static long lastRequestTime = Long.MIN_VALUE;

    private ClientDamStats() {}

    /// Asks the server for the stats of the volume owning the dam water at the given position, unless they were
    /// requested recently. Assumption: called on the client thread.
    public static void request(int dimension, int volumeId, int x, int y, int z, long worldTime) {
        boolean sameVolume = dimension == requestedDimension && volumeId == requestedVolumeId;

        if (sameVolume && worldTime - lastRequestTime < REQUEST_INTERVAL && worldTime >= lastRequestTime) return;

        requestedDimension = dimension;
        requestedVolumeId = volumeId;
        lastRequestTime = worldTime;

        GTValues.NW.sendToServer(new GTPacketDamStatsRequest(x, y, z));
    }

    /// The last stats received for the volume, if any.
    @Nullable
    public static Stats get(int dimension, int volumeId) {
        Stats stats = latest;

        return stats != null && stats.dimension == dimension && stats.volumeId == volumeId ? stats : null;
    }

    static void set(Stats stats) {
        latest = stats;
    }
}
