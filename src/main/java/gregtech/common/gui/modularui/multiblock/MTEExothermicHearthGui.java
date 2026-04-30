package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEExothermicHearth;

public class MTEExothermicHearthGui extends MTEMultiBlockBaseGui<MTEExothermicHearth> {

    public MTEExothermicHearthGui(MTEExothermicHearth multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "pyrotheum",
            new BooleanSyncValue(() -> multiblock.isPyroSupplied, val -> multiblock.isPyroSupplied = val));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(
            new ToggleButton().syncHandler("pyrotheum")
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_PYROTHEUM_ON)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_PYROTHEUM_OFF)
                .tooltip(true, t -> t.add(translateToLocal("GT5U.gui.text.button.pyrotheum.enabled")))
                .tooltip(false, t -> t.add(translateToLocal("GT5U.gui.text.button.pyrotheum.disabled"))));
    }
}
