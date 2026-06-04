package gregtech.common.gui.modularui.hatch;

import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_SLAVE;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui.BASE_HEIGHT;
import static gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui.BASE_WIDTH;
import static gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui.BORDER_RADIUS;
import static gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui.SLOT_SIZE;

import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import appeng.api.implementations.ICraftingPatternItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.api.util.Lazy;
import gregtech.common.gui.modularui.util.PatternSlot;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;

/**
 * This GUI may be opened when the corresponding TileEntity is not loaded on the client!
 * We must not sync the value to the corresponding TileEntity of the client as it may not be loaded
 * On the server side, the value should be synced to the actual tile entity
 */
public final class MTEHatchCraftingInputSlaveGui implements IGuiHolder<MTEHatchCraftingInputSlaveGui.RemoteGuiData> {

    public static final CraftingInputGuiFactory GUI = new CraftingInputGuiFactory();
    public static Lazy<MTEHatchCraftingInputSlave> MOCK_MTE = new Lazy<>(
        () -> (MTEHatchCraftingInputSlave) GregTechAPI.METATILEENTITIES[CRAFTING_INPUT_SLAVE.ID]);

    private static final String PATTERN_INV_NAME = "pattern_inv";
    private static final String MANUAL_ITEM_INV_NAME = "manual_item_inv";
    private static final int PATTERN_SLOT_ROW = 4;
    private static final int PATTERN_SLOT_PER_ROW = 9;
    private static final int MANUAL_SLOT_ROW = 3;
    private static final int MANUAL_SLOT_PER_ROW = 3;

    // This is null on client and non-null on server
    private final @Nullable MTEHatchCraftingInputME serverMTE;

    // fields to sync to client, or reference to the real Object on the server
    private final ItemStackHandler inventoryHandler;

    public MTEHatchCraftingInputSlaveGui(@Nullable MTEHatchCraftingInputME serverMTE) {
        if (NetworkUtils.isClient()) {
            this.serverMTE = null;
            this.inventoryHandler = new ItemStackHandler(MTEHatchCraftingInputME.MAX_INV_COUNT);
        } else {
            Objects.requireNonNull(serverMTE);
            this.serverMTE = serverMTE;
            this.inventoryHandler = serverMTE.inventoryHandler;
        }
    }

    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(RemoteGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }

    public ModularPanel buildUI(RemoteGuiData pos, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = ModularPanel
            .defaultPanel("hatch.crafting_input.proxy", BASE_WIDTH, BASE_HEIGHT + SLOT_SIZE);
        panel.bindPlayerInventory();
        if (serverMTE == null) {
            panel.child(
                CommonWidgets.createMachineTitle(ItemList.Hatch_CraftingInput_Bus_Slave.getDisplayName(), BASE_WIDTH));
        }
        syncManager.addCloseListener($ -> {
            if (serverMTE != null) {
                serverMTE.markDirty();
            }
        });
        registerSyncValues(syncManager);
        return panel.child(
            Flow.column()
                .full()
                .padding(BORDER_RADIUS)
                .child(createContentHolder(panel, syncManager)));
    }

    private void registerSyncValues(PanelSyncManager syncManager) {}

    private ParentWidget<?> createContentHolder(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> holder = Flow.column()
            .childPadding(2);

        return holder.size(BASE_WIDTH - BORDER_RADIUS * 2, BASE_HEIGHT - BORDER_RADIUS * 2 - 80)
            .padding(3, 2)
            .child(createContentSection(panel, syncManager))
            .child(createBottomSection(panel, syncManager));
    }

    private ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().expanded()
            .fullWidth()
            .child(createSlots(syncManager));
    }

    private ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().fullWidth()
            .coverChildrenHeight()
            .child(createBottomLeftCornerFlow(panel, syncManager))
            .child(createBottomRightCornerFlow(panel, syncManager));
    }

    private Grid createSlots(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(PATTERN_INV_NAME, PATTERN_SLOT_ROW);

        return new Grid().coverChildren()
            .gridOfWidthHeight(
                PATTERN_SLOT_PER_ROW,
                PATTERN_SLOT_ROW,
                ($x, $y, index) -> new PatternSlot().slot(
                    new ModularSlot(inventoryHandler, index)
                        .filter(itemStack -> itemStack.getItem() instanceof ICraftingPatternItem)
                        .changeListener((itemStack, onlyAmount, client, init) -> {
                            if (serverMTE != null) {
                                serverMTE.onPatternChange(index, itemStack);
                            }
                        })
                        .slotGroup(PATTERN_INV_NAME)));
    }

    private Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .verticalCenter()
            .leftRel(0);
        // .child(createOptimizerButton())
        // .child(createShowPatternButton())
        // .child(createExportButton())
        // .child(createDoublePatternButton())
        // .child(createManualItemsButton(syncManager));
    }

    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .coverChildren()
            .verticalCenter()
            .rightRel(0)
            .child(CommonWidgets.createCircuitSlot(syncManager, MOCK_MTE.get()));
    }
    //
    // private ToggleButton createOptimizerButton() {
    // BooleanSyncValue optimizerSync = new BooleanSyncValue(
    // () -> !machine.disablePatternOptimization,
    // val -> machine.disablePatternOptimization = !val).allowC2S();
    //
    // return new ToggleButton().value(optimizerSync)
    // .overlay(GTGuiTextures.OVERLAY_BUTTON_PATTERN_OPTIMIZE)
    // .addTooltipLine(GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern"))
    // .addTooltip(true, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.enable"))
    // .addTooltip(false, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.optimize_pattern.disabled"));
    // }
    //
    // private ToggleButton createShowPatternButton() {
    // BooleanSyncValue showPatternSync = new BooleanSyncValue(
    // () -> machine.showPattern,
    // val -> machine.showPattern = val).allowC2S();
    //
    // return new ToggleButton().value(showPatternSync)
    // .overlay(true, GTGuiTextures.OVERLAY_BUTTON_WHITELIST)
    // .overlay(false, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
    // .addTooltip(true, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.show_pattern.enable"))
    // .addTooltip(false, GTUtility.translate("GT5U.infodata.hatch.crafting_input_me.show_pattern.disabled"));
    // }
    //
    // private ButtonWidget<?> createExportButton() {
    // InteractionSyncHandler exportSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
    // if (!mouseDelta.isClient() && mouseDelta.mouseButton == 0) {
    // machine.refundAll(false);
    // }
    // });
    //
    // return new ButtonWidget<>().syncHandler(exportSyncHandler)
    // .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT)
    // .addTooltipLine(GTUtility.translate("GT5U.gui.tooltip.hatch.crafting_input_me.export"));
    // }
    //
    // private ButtonWidget<?> createDoublePatternButton() {
    // InteractionSyncHandler doubleSyncHandler = new InteractionSyncHandler().setOnMousePressed(mouseDelta -> {
    // if (!mouseDelta.isClient()) {
    // int val = mouseDelta.shift ? 1 : 0;
    // if (mouseDelta.mouseButton == 1) val |= 0b10;
    // machine.doublePatterns(val);
    // }
    // });
    //
    // return new ButtonWidget<>().syncHandler(doubleSyncHandler)
    // .overlay(GTGuiTextures.OVERLAY_BUTTON_X2)
    // .addTooltipLine(GTUtility.translate("gui.tooltips.appliedenergistics2.DoublePatterns"));
    // }
    //
    // private ButtonWidget<?> createManualItemsButton(PanelSyncManager syncManager) {
    // IPanelHandler popupPanel = syncManager
    // .syncedPanel("manual_slots_panel", true, (manager, handler) -> createManualSlotUI(manager));
    //
    // return new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_PLUS_LARGE)
    // .addTooltipLine(GTUtility.translate("GT5U.gui.tooltip.hatch.crafting_input_me.place_manual_items"))
    // .onMousePressed(mouseButton -> {
    // popupPanel.openPanel();
    // return popupPanel.isPanelOpen();
    // });
    // }
    //
    // private ModularPanel createManualSlotUI(PanelSyncManager syncManager) {
    // return createPopUpPanel("manual_slots_panel").size(68, 76)
    // .bottomRelOffset(0.5f, 52)
    // .child(
    // new ItemSlotGridBuilder(machine.inventoryHandler, syncManager)
    // .size(MANUAL_SLOT_PER_ROW, MANUAL_SLOT_ROW)
    // .slotGroupKey(MANUAL_ITEM_INV_NAME)
    // .indexOffset(SLOT_MANUAL_START)
    // .build()
    // .marginTop(16)
    // .horizontalCenter());
    // }

    final public static class RemoteGuiData extends GuiData {

        @Nullable
        final MTEHatchCraftingInputME serverMTE;
        final int x;
        final int y;
        final int z;

        public RemoteGuiData(@NotNull EntityPlayer player, @Nullable MTEHatchCraftingInputME serverMTE, int x, int y,
            int z) {
            super(player);
            this.serverMTE = serverMTE;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public @Nullable MTEHatchCraftingInputME getServerMTE() {
            return serverMTE;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }

    final public static class CraftingInputGuiFactory extends AbstractUIFactory<RemoteGuiData> {

        CraftingInputGuiFactory() {
            super("gregtech:crafting_input");
        }

        public void open(MTEHatchCraftingInputME serverMTE, EntityPlayerMP player) {
            var base = serverMTE.getBaseMetaTileEntity();
            GuiManager.open(
                this,
                new RemoteGuiData(player, serverMTE, base.getXCoord(), base.getYCoord(), base.getZCoord()),
                player);
        }

        @Override
        public @NotNull IGuiHolder<RemoteGuiData> getGuiHolder(RemoteGuiData data) {
            return new MTEHatchCraftingInputSlaveGui(data.getServerMTE());
        }

        @Override
        public void writeGuiData(RemoteGuiData guiData, PacketBuffer buffer) {
            buffer.writeInt(guiData.getX());
            buffer.writeInt(guiData.getY());
            buffer.writeInt(guiData.getZ());
        }

        @Override
        public @NotNull MTEHatchCraftingInputSlaveGui.RemoteGuiData readGuiData(EntityPlayer player,
            PacketBuffer buffer) {
            return new RemoteGuiData(player, null, buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    }
}
