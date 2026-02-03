package kubatech.tileentity.gregtech.multiblock.modularui2;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.ListWidget;

//from PR#5320 DroneListWidget.java
public class ListWidgetWithDefaultScroll<I extends IWidget, W extends ListWidget<I, W>> extends ListWidget<I, W> {

    private int lastScroll;
    public ListWidgetWithDefaultScroll(int lastScroll) {
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

    public final static class FinalListWidgetWithDefaultScroll<I extends IWidget> extends ListWidgetWithDefaultScroll<I, FinalListWidgetWithDefaultScroll<I>>{
        public FinalListWidgetWithDefaultScroll(int lastScroll) {
            super(lastScroll);
        }
    }
}
