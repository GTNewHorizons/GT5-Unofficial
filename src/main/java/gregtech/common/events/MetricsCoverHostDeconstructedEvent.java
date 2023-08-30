package gregtech.common.events;

import java.util.UUID;

/**
 * Event fired when the machine housing a Metrics Transmitter cover is deconstructed, but the cover remains
 * attached.
 */
public class MetricsCoverHostDeconstructedEvent extends BaseMetricsCoverEvent {

    public MetricsCoverHostDeconstructedEvent(UUID frequency) {
        super(frequency);
    }
}
