package gregtech.common.text;

import net.minecraft.util.StatCollector;

public final class ClientTickRateFormatter {

    /**
     * A translation key for the type of time units being used (e.g.: "tick", "seconds".)
     */
    private final String unitI18NKey;
    /**
     * A number representing a quantity of time.
     */
    private final int tickRate;

    /**
     * Converts a given tick rate into a human-friendly format.
     *
     * @param tickRate The rate at which something ticks, in ticks per operation.
     */
    public ClientTickRateFormatter(final int tickRate) {
        if (tickRate < 20) {
            this.unitI18NKey = tickRate == 1 ? "gt.time.tick.singular" : "gt" + ".time.tick.plural";
            this.tickRate = tickRate;
        } else {
            this.unitI18NKey = tickRate == 20 ? "gt.time.second.singular" : "gt.time.second.plural";
            this.tickRate = tickRate / 20;
        }
    }

    public String toString() {
        return StatCollector.translateToLocalFormatted(
            "gt.cover.info.format.tick_rate",
            tickRate,
            StatCollector.translateToLocal(unitI18NKey));
    }
}
