package gregtech.common.events;

import java.util.UUID;

/**
 * Event fired when a Metrics Transmitter cover is detached from its machine with a crowbar.
 */
public class MetricsCoverSelfDestructEvent extends BaseMetricsCoverEvent {

    public MetricsCoverSelfDestructEvent(UUID frequency) {
        super(frequency);
    }
}
