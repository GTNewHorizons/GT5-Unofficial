package gregtech.common.gui.modularui.hatch;

import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate2by2;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchMultiInputGui extends MTEHatchBaseGui<MTEHatchMultiInput> {

    public MTEHatchMultiInputGui(MTEHatchMultiInput hatch) {
        super(hatch);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        FluidStackTank[] fluidTanks = hatch.getFluidTanks();

        return GTGuis.mteTemplatePanelBuilder(hatch, guiData, syncManager, uiSettings)
            .build()
            .child(
                gridTemplate2by2(
                    index -> new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTanks[index]))
                        .background(GTGuiTextures.SLOT_FLUID_STANDARD)));
    }
}
