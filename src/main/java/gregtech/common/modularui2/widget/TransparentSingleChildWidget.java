package gregtech.common.modularui2.widget;

import com.cleanroommc.modularui.widget.SingleChildWidget;

public class TransparentSingleChildWidget extends SingleChildWidget<TransparentSingleChildWidget> {

    public TransparentSingleChildWidget() {
        super();
    }

    @Override
    public boolean canHover() {
        return false;
    }

}
