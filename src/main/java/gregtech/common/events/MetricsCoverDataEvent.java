package gregtech.common.events;

import java.util.List;
import java.util.UUID;

/**
 * Event fired when the Metrics Transmitter cover sends an information packet.
 */
public class MetricsCoverDataEvent extends BaseMetricsCoverEvent {

    private final List<String> payload;

    /**
     *
     * @param frequency The frequency of the cover.
     * @param payload   A list containing panel texts for the machine's dimension and coordinates, along with whatever
     *                  information it can derive from
     *                  {@link gregtech.api.interfaces.tileentity.IGregTechDeviceInformation#getInfoData()}.
     */
    public MetricsCoverDataEvent(UUID frequency, List<String> payload) {
        super(frequency);
        this.payload = payload;
    }

    public List<String> getPayload() {
        return payload;
    }

}
