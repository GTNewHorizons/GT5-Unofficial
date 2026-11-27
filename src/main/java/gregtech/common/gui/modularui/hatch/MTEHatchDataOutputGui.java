package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataOutput;

public class MTEHatchDataOutputGui extends MTEHatchBaseGui<MTEHatchDataOutput> {

    public MTEHatchDataOutputGui(MTEHatchDataOutput machine) {
        super(machine);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        LongSyncValue computationSyncer = new LongSyncValue(
            machine::getRequestedComputation,
            machine::setRequestedComputation).allowC2S();

        Flow mainCol = Flow.col()
            .coverChildren()
            .center()
            .childPadding(4);

        // computation label
        mainCol.child(
            IKey.lang("GT5U.machines.computation_hatch.computation")
                .asWidget());

        // computation input box
        mainCol.child(
            new TextFieldWidget().value(computationSyncer)
                .setTextAlignment(Alignment.CENTER)
                .width(135)
                .numbersLong(1, Long.MAX_VALUE)
                .scrollValues(1, 4, 16, 64));

        return super.createContentSection(panel, syncManager).child(mainCol);
    }
}
