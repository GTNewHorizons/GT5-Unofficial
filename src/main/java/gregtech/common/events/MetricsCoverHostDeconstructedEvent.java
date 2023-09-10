package gregtech.common.events;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

/**
 * Event fired when the machine housing a Metrics Transmitter cover is deconstructed, but the cover remains
 * attached.
 */
public class MetricsCoverHostDeconstructedEvent extends BaseMetricsCoverEvent {

    public MetricsCoverHostDeconstructedEvent(@NotNull UUID frequency) {
        super(frequency);
    }
}
