package gregtech.common.events;

import cpw.mods.fml.common.eventhandler.Event;

public class PickBlockEvent extends Event {

    @Override
    public boolean isCancelable() {
        return true;
    }
}
