package gregtech.common.events;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.eventhandler.Event;

abstract public class BaseMetricsCoverEvent extends Event {

    protected final UUID frequency;

    public BaseMetricsCoverEvent(@NotNull UUID frequency) {
        this.frequency = frequency;
    }

    @NotNull
    public UUID getFrequency() {
        return frequency;
    }
}
