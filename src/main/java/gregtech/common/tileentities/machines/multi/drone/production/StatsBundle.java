package gregtech.common.tileentities.machines.multi.drone.production;

import gnu.trove.map.TLongLongMap;
import gnu.trove.map.hash.TLongLongHashMap;

class StatsBundle {

    public final TLongLongMap data = new TLongLongHashMap();

    public void merge(long key, long amount) {
        if (amount == 0) return;
        long oldValue = data.get(key);
        if (!data.containsKey(key)) {
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
            // 如果start.data中没有这个key，则sv会是默认值0L
            if (!start.data.containsKey(key)) {
                sv = 0L;
            }
            long d = v - sv;
            if (d != 0) res.data.put(key, d);
        }
        return res;
    }
}
