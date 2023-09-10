package gregtech.common.events;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a Metrics Transmitter cover is detached from its machine with a crowbar.
 */
public class MetricsCoverSelfDestructEvent extends BaseMetricsCoverEvent {

    public MetricsCoverSelfDestructEvent(@NotNull UUID frequency) {
        super(frequency);
    }
}
