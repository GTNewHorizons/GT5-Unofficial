package gregtech.api.graphs.paths;

/**
 * Immutable snapshot of what a {@link PowerNodePath} carried on the previous tick, plus what it is rated for.
 * <p>
 * Shared by the Portable Scanner and the Waila tooltip so both display the exact same numbers. All voltages are
 * <b>after</b> the cumulated loss of the path segment, so {@code eut == amps * voltage} and
 * {@code maxEut == maxAmps * maxVoltage} hold.
 *
 * @param amps       amperes that went through on the previous tick
 * @param maxAmps    amperes the weakest cable of the segment can carry
 * @param voltage    highest voltage seen on the previous tick, after loss
 * @param maxVoltage voltage the weakest cable of the segment can take, after loss
 * @param eut        energy that went through on the previous tick
 * @param maxEut     energy the segment can carry
 * @param avgAmps    amperes per tick, averaged over the last 20 ticks
 * @param avgEut     energy per tick, averaged over the last 20 ticks
 */
public record CableReadout(long amps, long maxAmps, long voltage, long maxVoltage, long eut, long maxEut,
    double avgAmps, double avgEut) {}
