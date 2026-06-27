package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessComputationInput;

public class MTEHatchWirelessComputationInputGui extends MTEHatchBaseGui<MTEHatchWirelessComputationInput> {

    public MTEHatchWirelessComputationInputGui(MTEHatchWirelessComputationInput hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        LongSyncValue computationSyncer = new LongSyncValue(
            machine::getRequiredComputation,
            machine::setRequiredComputation).allowC2S();

        Flow mainColumn = Flow.column()
            .horizontalCenter()
            .coverChildren()
            .childPadding(2);

        // config label
        mainColumn.child(
            new TextWidget<>(translateToLocal("tt.wirelessInputData.config.text")).width(140)
                .textAlign(Alignment.Center));

        // config text field
        mainColumn.child(
            new TextFieldWidget().value(computationSyncer)
                .width(75)
                .setMaxLength(10)
                .scrollValues(1, 64, 4, 16)
                .setTextAlignment(Alignment.Center)
                .numbersInt(1, Integer.MAX_VALUE)
                .formatAsInteger(true));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }
}
