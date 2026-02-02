package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.screen.ModularPanel;
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
        return super.createRightPanelGapRow(parent, syncManager).child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setDefaultNumber(100)
                .setNumbers(50, 200)
                .syncHandler("traceSize")
                .width(40)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.MBTT.PCB.Tooltip.5"))));
    }

}
