package gregtech.common.modularui2.util;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.layout.Flow;

public class FlowActions {

    // TODO: Inline this method once https://github.com/CleanroomMC/ModularUI/pull/132 is backported
    public static void resize(Flow column) {
        if (NetworkUtils.isClient()) {
            WidgetTree.resize(column);
        }
    }
}
