package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.xlturbines.MTEXLTurbineBase;

public class MTEXLTurbineGui extends MTEMultiBlockBaseGui<MTEXLTurbineBase> {

    private static final String TURBINE_SLOT_GROUP = "xl_turbines";
    private static final int TURBINE_SHIFT_CLICK_PRIORITY = 50;

    public MTEXLTurbineGui(MTEXLTurbineBase multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createTurbineHolderButton(syncManager, parent));
    }

    protected IWidget createTurbineHolderButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler turbinePanel = syncManager.syncedPanel(
            "turbineHolderPanel",
            true,
            (pSyncManager, syncHandler) -> openTurbineHolderPanel(parent, pSyncManager));

        return new ButtonWidget<>().size(18, 18)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                if (!turbinePanel.isPanelOpen()) {
                    turbinePanel.openPanel();
                } else {
                    turbinePanel.closePanel();
                }
                return true;
            })
            .tooltipDynamic(t -> t.addLine(IKey.lang("GT5U.gui.button.turbinemenu")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openTurbineHolderPanel(ModularPanel parent, PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(TURBINE_SLOT_GROUP, 4, TURBINE_SHIFT_CLICK_PRIORITY);

        return GTGuis.createPopUpPanel("turbineHolderPanel")
            .relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(118, 104)
            .child(
                Flow.column()
                    .full()
                    .padding(5)
                    .childPadding(4)
                    .child(
                        new TextWidget<>("Turbines").size(96, 18)
                            .alignment(Alignment.Center))
                    .child(
                        SlotGroupWidget.builder()
                            .matrix("IIII", "IIII", "IIII")
                            .key('I', this::createTurbineSlot)
                            .build()));
    }

    private IWidget createTurbineSlot(int index) {
        return new ItemSlot().slot(
            new ModularSlot(multiblock.turbineHolder, index).slotGroup(TURBINE_SLOT_GROUP)
                .filter(MTEXLTurbineBase::isValidTurbine));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                () -> new ItemSlot()
                    .slot(new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()) {

                        @Override
                        public int getSlotStackLimit() {
                            return multiblock.getInventoryStackLimit();
                        }
                    }.singletonSlotGroup()
                        .filter(stack -> !MTEXLTurbineBase.isValidTurbine(stack)))
                    .marginTop(4))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }
}
