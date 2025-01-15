package gregtech.api.events;

import cpw.mods.fml.common.eventhandler.EventBus;

public final class GTEventBus {

    private static final EventBus BUS = new EventBus();

    private GTEventBus() {}

    public static EventBus bus() {
        return BUS;
    }
}
