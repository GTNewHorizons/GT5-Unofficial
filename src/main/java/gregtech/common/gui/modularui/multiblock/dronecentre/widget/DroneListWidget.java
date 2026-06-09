package gregtech.common.gui.modularui.multiblock.dronecentre.widget;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.ListWidget;

public class DroneListWidget<I extends IWidget, W extends ListWidget<I, W>> extends ListWidget<I, W> {

    private int lastScroll;

    public DroneListWidget(int lastScroll) {
        super();
        this.lastScroll = lastScroll;
    }

    @Override
    public void postResize() {
        super.postResize();
        if (lastScroll > 0) {
            if (getScrollArea().getScrollY() != null) {
                getScrollArea().getScrollY()
                    .scrollTo(getScrollArea(), lastScroll);
            }
            lastScroll = 0;
        }
    }
}
