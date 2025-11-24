package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;

public abstract class MTEBaseModuleGui<T extends MTEBaseModule> extends TTMultiblockBaseGui<T> {

    private final SyncHypervisor hypervisor;

    public MTEBaseModuleGui(T multiblock) {
        super(multiblock);
        this.hypervisor = new SyncHypervisor(multiblock);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -3, 1)
            .childPadding(3)
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .slotGroup("item_inv"))
                    .marginTop(4))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager))
            .child(createVoltageConfigButton(syncManager))
            .childIf(usesExtraButton(), createExtraButton(syncManager));
    }

    @Override
    protected ToggleButton createPowerSwitchButton() {
        return super.createPowerSwitchButton().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        return super.createStructureUpdateButton(syncManager); // todo
    }

    protected IWidget createVoltageConfigButton(PanelSyncManager syncManager) {
        return new Widget<>(); // todo
    }

    protected boolean usesExtraButton() {
        return false;
    }

    protected IWidget createExtraButton(PanelSyncManager syncManager) {
        return new Widget<>();
    }
}
