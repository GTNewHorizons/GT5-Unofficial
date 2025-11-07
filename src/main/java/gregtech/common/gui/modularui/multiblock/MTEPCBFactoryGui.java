package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBFactory;

public class MTEPCBFactoryGui extends MTEMultiBlockBaseGui<MTEPCBFactory> {

    public MTEPCBFactoryGui(MTEPCBFactory multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue traceSizeSyncer = new IntSyncValue(multiblock::getTraceSize, multiblock::setTraceSize);
        syncManager.syncValue("traceSize", traceSizeSyncer);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .align(Alignment.CenterRight)
            .coverChildrenWidth()
            .heightRel(1)
            .child(createInterimTextFieldRow())
            .childIf(multiblock.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
    }

    private Flow createInterimTextFieldRow() {
        return Flow.row()
            .coverChildren()
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .setDefaultNumber(100)
                    .setNumbers(50, 200)
                    .syncHandler("traceSize")
                    .width(40)
                    .align(Alignment.Center))
            // TODO: add tooltip to text field after functionality is added rather than doing this hack.
            // its quite annoying.
            .child(
                new IDrawable.DrawableWidget(IDrawable.EMPTY).size(40, 18)
                    .align(Alignment.Center)
                    .tooltip(t -> t.addLine(translateToLocal("GT5U.MBTT.PCB.Tooltip.5"))));
    }
}
