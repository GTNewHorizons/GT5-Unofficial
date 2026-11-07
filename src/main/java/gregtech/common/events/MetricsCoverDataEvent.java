package gregtech.common.events;

import java.util.List;
import java.util.UUID;

import net.minecraft.util.IChatComponent;

import org.jetbrains.annotations.NotNull;

import gregtech.common.misc.GlobalMetricsCoverDatabase;

/**
 * Event fired when the Metrics Transmitter cover sends an information packet.
 */
public class MetricsCoverDataEvent extends BaseMetricsCoverEvent {

    @NotNull
    private final List<String> payload;
    @NotNull
    private final List<IChatComponent> chatPayload;

    @NotNull
    private final GlobalMetricsCoverDatabase.Coordinates coordinates;

    public MetricsCoverDataEvent(@NotNull UUID frequency, @NotNull List<String> payload,
        @NotNull List<IChatComponent> chatPayload, @NotNull GlobalMetricsCoverDatabase.Coordinates coordinates) {
        super(frequency);
        this.payload = payload;
        this.chatPayload = chatPayload;
        this.coordinates = coordinates;
    }

    @NotNull
    public List<String> getPayload() {
        return payload;
    }

    @NotNull
    public List<IChatComponent> getChatPayload() {
        return chatPayload;
    }

    @NotNull
    public GlobalMetricsCoverDatabase.Coordinates getCoordinates() {
        return coordinates;
    }
}
