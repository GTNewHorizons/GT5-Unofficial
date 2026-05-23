package gregtech.common.gui.modularui.util;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public final class WirelessEnergyHatchGuiHelper {

    public static Flow createMainColumn(IntSyncValue amperageSyncer, int maxAmperage) {
        Flow mainColumn = Flow.column()
            .coverChildren()
            .center()
            .childPadding(4);

        // amperage label
        mainColumn.child(
            IKey.lang("GT5U.machines.laser_hatch.amperage")
                .asWidget());

        // amperage text field
        mainColumn.child(
            new TextFieldWidget().value(amperageSyncer)
                .setNumbers(0, maxAmperage)
                .setFormatAsInteger(true)
                .setTextAlignment(Alignment.Center)
                .setMaxLength((int) Math.ceil(Math.log10(maxAmperage)))
                .setScrollValues(1, 4, 64)
                .width(70));

        return mainColumn;
    }
}
