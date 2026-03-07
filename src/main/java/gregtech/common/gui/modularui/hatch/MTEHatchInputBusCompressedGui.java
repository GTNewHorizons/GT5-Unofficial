package gregtech.common.gui.modularui.hatch;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.metatileentity.implementations.MTEHatchInputBusCompressed;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.synchandler.NBTSerializableSyncHandler;
import gregtech.common.gui.modularui.util.AEItemSlot;
import gregtech.common.inventory.AEInventory;

public class MTEHatchInputBusCompressedGui extends MTEHatchBaseGui<MTEHatchInputBusCompressed> {

    public MTEHatchInputBusCompressedGui(MTEHatchInputBusCompressed hatch) {
        super(hatch);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return MathHelper.ceiling_double_int(
            Math.sqrt(
                hatch.getAEInventory()
                    .getSlots()));
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !hatch.disableSort, val -> hatch.disableSort = !val);
        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !hatch.disableLimited,
            val -> hatch.disableLimited = !val);
        return super.createLeftCornerFlow(panel, syncManager)
            .child(
                createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                createToggleButton(
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
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
            .onMousePressed(d -> {
                if (!settingsPanel.isPanelOpen()) {
                    settingsPanel.openPanel();
                } else {
                    settingsPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.compressed_bus_settings")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel createSettingsPanel(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue capacitySyncer = new LongSyncValue(hatch::getStackLimitOverride, hatch::setStackLimitOverride);

        // spotless:off
        return new ModularPanel("busSettings")
            .relative(parent)
            .rightRel(1)
            .bottom(50)
            .size(150, 60)
            .child(new Column()
                .sizeRel(1)
                .padding(3)
                .align(Alignment.TopCenter)
                .child(IKey.lang("GT5U.gui.text.bus_settings")
                    .asWidget()
                    .marginBottom(4))
                .child(new Row()
                    .alignX(Alignment.Center)
                    .size(140, 20)
                    .child(IKey.lang("GT5U.gui.text.stack_capacity")
                        .asWidget()
                        .marginRight(4))
                    .child(new TextFieldWidget()
                        .setNumbersLong(() -> 1L, () -> hatch.stackCapacity)
                        .value(capacitySyncer)
                        .setScrollValues(1d, 4d, 64d))));
        // spotless:on
    }

    private final int BUTTON_SIZE = 18;

    @Override
    protected int getBasePanelHeight() {
        // we subtract 4 from the dimension before adding this value as a 4x4 slot grid is the maximum that fits on the
        // default panel
        return super.getBasePanelHeight() + Math.max(0, BUTTON_SIZE * (this.getDimension() - 4) + 18);
    }

    @Override
    protected int getBasePanelWidth() {
        // we subtract 9 from the dimension before adding this value as a 9x9 slot grid is the maximum that fits on the
        // default width panel
        return super.getBasePanelWidth() + Math.max(0, BUTTON_SIZE * (this.getDimension() - 9));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    protected SlotGroupWidget createSlots(PanelSyncManager syncManager) {

        AEInventory inv = hatch.getAEInventory();

        int slotCount = inv.getSlots();

        final int width = MathHelper.ceiling_double_int(Math.sqrt(slotCount));
        syncManager.registerSlotGroup("item_inv", width);

        List<String> matrix = new ArrayList<>();

        for (int i = 0; i < slotCount; i += width) {
            matrix.add(StringUtils.getRepetitionOf('s', Math.min(slotCount - i, width)));
        }

        syncManager.syncValue("inventory", new NBTSerializableSyncHandler<>(hatch::getAEInventory));

        return SlotGroupWidget.builder()
            .matrix(matrix.toArray(new String[0]))
            .key('s', index -> new AEItemSlot(syncManager, "item_inv", inv, index).setDumpable(true))
            .build()
            .coverChildren()
            .marginTop(BUTTON_SIZE / 2 * (4 - this.getDimension()))
            .horizontalCenter();
    }

    private ToggleButton createToggleButton(BooleanSyncValue syncValue, UITexture texture, String key) {
        return new ToggleButton().value(syncValue)
            .overlay(texture)
            .addTooltipLine(GTUtility.translate(key));
    }
}
