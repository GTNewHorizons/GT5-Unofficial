package gregtech.common.gui.modularui2;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.layout.Flow;

public class FlowActions {

    public static void resize(Flow column) {
        if (NetworkUtils.isClient()) {
            WidgetTree.resize(column);
        }
    }
}
