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
                .numbersInt(0, maxAmperage)
                .formatAsInteger(true)
                .setTextAlignment(Alignment.Center)
                .setMaxLength((int) Math.ceil(Math.log10(maxAmperage)))
                .scrollValues(1, 64, 4, 16)
                .width(70));

        return mainColumn;
    }
}
