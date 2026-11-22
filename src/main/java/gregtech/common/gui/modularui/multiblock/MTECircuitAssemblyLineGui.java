package gregtech.common.gui.modularui.multiblock;

import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTECircuitAssemblyLineGui extends MTEMultiBlockBaseGui<MTECircuitAssemblyLine> {

    public MTECircuitAssemblyLineGui(MTECircuitAssemblyLine multiblock) {
        super(multiblock);
        withMachineModeIcons(GTGuiTextures.OVERLAY_BUTTON_ASSEMBLER_MODE, GTGuiTextures.OVERLAY_BUTTON_PRECISE_MODE);
    }

    @Override
    public boolean showMaxParallelRow() {
        return false;
    }
}
