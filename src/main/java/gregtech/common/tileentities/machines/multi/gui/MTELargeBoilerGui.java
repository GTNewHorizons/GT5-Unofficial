package gregtech.common.tileentities.machines.multi.gui;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTELargeBoiler;

public class MTELargeBoilerGui extends MTEMultiBlockBaseGui {

    private final MTELargeBoiler base;

    public MTELargeBoilerGui(MTELargeBoiler base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("overdrive", new BooleanSyncValue(base::isOverdrive, base::setOverdrive));
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createPanelGap(parent, syncManager).child(createOverdriveButton(syncManager, parent));
    }

    protected IWidget createOverdriveButton(PanelSyncManager syncManager, ModularPanel parent) {
        return new ToggleButton().size(18, 18)
            .syncHandler("overdrive")
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_OVERDRIVE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_OVERDRIVE_OFF)
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.machines.overdrive")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
