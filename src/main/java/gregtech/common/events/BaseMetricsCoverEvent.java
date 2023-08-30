package gregtech.common.events;

import java.util.UUID;

import cpw.mods.fml.common.eventhandler.Event;

public class BaseMetricsCoverEvent extends Event {

    protected final UUID frequency;

    public BaseMetricsCoverEvent(UUID frequency) {
        this.frequency = frequency;
    }

    public UUID getFrequency() {
        return frequency;
    }
}
