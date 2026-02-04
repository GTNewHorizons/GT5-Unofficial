package gregtech.common.tileentities.machines.multi.drone.production;

import net.minecraft.network.PacketBuffer;

import gnu.trove.map.TLongLongMap;
import gnu.trove.map.hash.TLongLongHashMap;

public class StatsBundle {

    public final TLongLongMap data = new TLongLongHashMap();
    private static final int MAX_SIZE = 10000;
    private long revision;

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public void merge(long key, long amount) {
        if (amount == 0) return;
        long oldValue = data.get(key);
        if (!data.containsKey(key) && data.size() >= MAX_SIZE) {
            data.put(key, amount);
        } else {
            data.put(key, oldValue + amount);
        }
    }

    public StatsBundle copy() {
        StatsBundle n = new StatsBundle();
        n.data.putAll(this.data);
        return n;
    }

    public StatsBundle diff(StatsBundle start) {
        StatsBundle res = new StatsBundle();
        for (long key : this.data.keys()) {
            long v = this.data.get(key);
            long sv = start.data.get(key);
            if (!start.data.containsKey(key)) {
                sv = 0L;
            }
            long d = v - sv;
            if (d != 0) res.data.put(key, d);
        }
        return res;
    }

    public void clear() {
        data.clear();
    }

    public static void writeToBuf(PacketBuffer buf, StatsBundle statsBundle) {
        buf.writeLong(statsBundle.revision);
        buf.writeVarIntToBuffer(statsBundle.data.size());
        statsBundle.data.forEachEntry((k, v) -> {
            buf.writeLong(k);
            buf.writeLong(v);
            return true;
        });
    }

    public static StatsBundle readFromBuf(PacketBuffer buf) {
        StatsBundle bundle = new StatsBundle();
        bundle.setRevision(buf.readLong());
        int size = buf.readVarIntFromBuffer();
        for (int i = 0; i < size; i++) {
            bundle.data.put(buf.readLong(), buf.readLong());
        }
        return bundle;
    }

    public static boolean areEqual(StatsBundle a, StatsBundle b) {
        return a != null && b != null && a.revision == b.revision;
    }
}
