package gregtech.common.gui.modularui.hatch;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusCompressed;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.synchandler.NBTSerializableSyncHandler;
import gregtech.common.gui.modularui.util.AEItemSlot;
import gregtech.common.inventory.AEInventory;

public class MTEHatchInputBusCompressedGui extends MTEHatchBaseGui<MTEHatchInputBusCompressed> {

    public MTEHatchInputBusCompressedGui(MTEHatchInputBusCompressed hatch) {
        super(hatch);
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return MathHelper.ceiling_double_int(
            Math.sqrt(
                machine.getAEInventory()
                    .getSlots()));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue stackSync = new BooleanSyncValue(
            () -> !machine.disableSort,
            val -> machine.disableSort = !val);
        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !machine.disableLimited,
            val -> machine.disableLimited = !val);

        return super.createBottomLeftCornerFlow(panel, syncManager)
            .child(
                CommonButtons.createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                CommonButtons.createToggleButton(
                    insertionSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"))
            .child(createSettingsButton(syncManager, panel));
    }

    protected IWidget createSettingsButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler settingsPanel = syncManager.syncedPanel(
            "busSettings",
            true,
            (p_syncManager, syncHandler) -> createSettingsPanel(p_syncManager, parent));

        return new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
            .onMousePressed(d -> {
                if (!settingsPanel.isPanelOpen()) {
                    settingsPanel.openPanel();
                } else {
                    settingsPanel.closePanel();
                }
                return true;
            })
            .addTooltipLine(GTUtility.translate("GT5U.gui.button.compressed_bus_settings"))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createSettingsPanel(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue capacitySyncer = new LongSyncValue(
            machine::getStackLimitOverride,
            machine::setStackLimitOverride);

        // spotless:off
        return new ModularPanel("busSettings")
            .relative(parent)
            .rightRel(1)
            .bottom(50)
            .size(150, 60)
            .child(Flow.column()
                .full()
                .padding(4)
                .child(IKey.lang("GT5U.gui.text.bus_settings")
                    .asWidget()
                    .marginBottom(4))
                .child(Flow.row()
                    .size(140, 20)
                    .child(IKey.lang("GT5U.gui.text.stack_capacity")
                        .asWidget()
                        .marginRight(4))
                    .child(new TextFieldWidget()
                        .setNumbersLong(() -> 1L, () -> machine.stackCapacity)
                        .value(capacitySyncer)
                        .setScrollValues(1d, 4d, 64d))));
        // spotless:on
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, SLOT_SIZE * (this.getDimension() - 3));
    }

    @Override
    protected int getBasePanelWidth() {
        return super.getBasePanelWidth() + Math.max(0, SLOT_SIZE * (this.getDimension() - 9));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    protected Grid createSlots(PanelSyncManager syncManager) {
        AEInventory inv = machine.getAEInventory();
        final int width = MathHelper.ceiling_double_int(Math.sqrt(inv.getSlots()));

        syncManager.registerSlotGroup("item_inv", width);
        syncManager.syncValue("inventory", new NBTSerializableSyncHandler<>(machine::getAEInventory));

        return new Grid().coverChildren()
            .gridOfWidthHeight(
                width,
                width,
                ($x, $y, index) -> new AEItemSlot(syncManager, "item_inv", inv, index).setDumpable(true))
            .horizontalCenter();
    }
}
