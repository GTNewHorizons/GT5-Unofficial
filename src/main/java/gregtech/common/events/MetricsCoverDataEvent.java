package gregtech.common.events;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.common.misc.GlobalMetricsCoverDatabase;

/**
 * Event fired when the Metrics Transmitter cover sends an information packet.
 */
public class MetricsCoverDataEvent extends BaseMetricsCoverEvent {

    @NotNull
    private final List<String> payload;

    @Nullable
    private final GlobalMetricsCoverDatabase.Coordinates coordinates;

    public MetricsCoverDataEvent(@NotNull UUID frequency, @NotNull List<String> payload,
        @Nullable GlobalMetricsCoverDatabase.Coordinates coordinates) {
        super(frequency);
        this.payload = payload;
        this.coordinates = coordinates;
    }

    @NotNull
    public List<String> getPayload() {
        return payload;
    }

    @NotNull
    public Optional<GlobalMetricsCoverDatabase.Coordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }
}
