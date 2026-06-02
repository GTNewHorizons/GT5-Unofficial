package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import bartworks.common.tileentities.multis.MTEWindmill;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTEWindmillGui extends MTEMultiBlockBaseGui<MTEWindmill> {

    public MTEWindmillGui(MTEWindmill windmill) {
        super(windmill);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {

        return super.build(guiData, syncManager, uiSettings);
    }

    @Override
    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .height(76)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                () -> SlotGroupWidget.playerInventory(false)
                    .center());
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .paddingRight(2)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .child(createRightButtons(syncManager))
            .child(createSlots());
    }

    protected Flow createRightButtons(PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .reverseLayout(true)
            .heightRel(1f)
            .rightRel(0)
            .coverChildrenWidth()
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }

    private Flow createSlots() {
        return Flow.row()
            .heightRel(1f)
            .verticalCenter()
            .coverChildrenWidth()
            .child(createInvSlot());
    }

    private IWidget createInvSlot() {
        return new ItemSlot().slot(new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()) {

            @Override
            public int getSlotStackLimit() {
                return multiblock.getInventoryStackLimit();
            }
        }.singletonSlotGroup());
    }

    @Override
    protected boolean shouldDisplayVoidExcess() {
        return false;
    }

    @Override
    protected boolean shouldDisplayInputSeparation() {
        return false;
    }

    @Override
    protected boolean shouldDisplayBatchMode() {
        return false;
    }

    @Override
    protected boolean shouldDisplayRecipeLock() {
        return false;
    }

    @Override
    protected int getTextBoxToInventoryGap() {
        return 30;
    }
}
