package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.tileentities.machines.MTEHatchInputBusME.SLOT_COUNT;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.adapter.MTEHatchInputBusMESlotAdapter;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.StockingSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME.Slot;

public class MTEHatchInputBusMEGui extends MTEHatchBaseGui<MTEHatchInputBusME> {

    private static final String FILTER_INV_NAME = "filter_inv";
    private static final String STOCK_INV_NAME = "stock_inv";
    private static final int FILTER_SLOT_ROW = 4;
    private static final int FILTER_SLOT_PER_ROW = 4;
    private static final int STOCK_SLOT_ROW = 4;
    private static final int STOCK_SLOT_PER_ROW = 4;
    private final Slot[] slots;

    public MTEHatchInputBusMEGui(MTEHatchInputBusME hatch, Slot[] slots) {
        super(hatch);
        this.slots = slots;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isAutoPullSyncer = new BooleanSyncValue(
            machine::isAutoPullItemList,
            machine::setAutoPullItemList).allowC2S();

        IItemHandlerModifiable configItemHandler = new IItemHandlerModifiable() {

            @Override
            public int getSlots() {
                return SLOT_COUNT * 2;
            }

            @Override
            public ItemStack getStackInSlot(int slotIndex) {
                boolean forConfig = slotIndex < SLOT_COUNT;
                slotIndex %= SLOT_COUNT;

                Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

                if (slot == null) return null;

                return forConfig ? slot.config : GTUtility.copyAmountUnsafe(slot.extractedAmount, slot.extracted);
            }

            @Override
            public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
                return null;
            }

            @Override
            public @Nullable ItemStack extractItem(int slot, int amount, boolean simulate) {
                return null;
            }

            @Override
            public int getSlotLimit(int slot) {
                return Integer.MAX_VALUE;
            }

            @Override
            public void setStackInSlot(int slotIndex, ItemStack stack) {
                if (slotIndex >= SLOT_COUNT) return;

                machine.setSlotConfig(slotIndex, GTUtility.copyAmount(1, stack));

                if (baseMetaTileEntity.isServerSide()) {
                    try {
                        machine.updateInformationSlot(slotIndex);
                    } catch (GridAccessException e) {
                        // :P
                    }
                }
            }
        };

        Flow mainRow = Flow.row()
            .coverChildren();

        mainRow.child(createFilterSlots(syncManager, configItemHandler, isAutoPullSyncer));
        mainRow.child(createMiddleColumn(syncManager, panel, isAutoPullSyncer));
        mainRow.child(createStockSlots(syncManager, configItemHandler));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private Grid createFilterSlots(PanelSyncManager syncManager, IItemHandlerModifiable configItemHandler,
        BooleanSyncValue isAutoPullSyncer) {
        return new ItemSlotGridBuilder(configItemHandler, syncManager).size(FILTER_SLOT_PER_ROW, FILTER_SLOT_ROW)
            .slotGroupKey(FILTER_INV_NAME)
            .filter(is -> doesNotContainsSuchStack(is) && !isAutoPullSyncer.getBoolValue())
            .itemSlotSupplier(() -> new StockingSlot(isAutoPullSyncer))
            .build();
    }

    private boolean doesNotContainsSuchStack(ItemStack tStack) {
        return Stream.of(slots)
            .filter(Objects::nonNull)
            .noneMatch(slot -> GTUtility.areStacksEqual(slot.config, tStack));
    }

    private Grid createStockSlots(PanelSyncManager syncManager, IItemHandlerModifiable configItemHandler) {
        return new ItemSlotGridBuilder(configItemHandler, syncManager).size(STOCK_SLOT_PER_ROW, STOCK_SLOT_ROW)
            .slotGroupKey(STOCK_INV_NAME)
            .indexOffset(SLOT_COUNT)
            .accessibility(false, false)
            .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.SLOT_ITEM_DARK))
            .build();
    }

    private Flow createMiddleColumn(PanelSyncManager syncManager, ModularPanel panel,
        BooleanSyncValue isAutoPullSyncer) {
        Flow mainColumn = Flow.col()
            .coverChildren()
            .reverseLayout()
            .mainAxisAlignment(Alignment.MainAxis.END);

        // circuit slot
        mainColumn.child(createCircuitSlot(syncManager));

        // manual slot
        mainColumn.child(
            new ItemSlot()
                .slot(new ModularSlot(machine.inventoryHandler, machine.getManualSlot()).slotGroup("item_inv"))
                .tooltip(t -> {
                    t.addLine(GTUtility.translate("GT5U.machines.stocking_bus.manual_slot.tooltip.1"));
                    t.addLine(
                        EnumChatFormatting.GRAY
                            + GTUtility.translate("GT5U.machines.stocking_bus.manual_slot.tooltip.2")
                            + EnumChatFormatting.RESET);
                }));

        // arrow
        mainColumn.child(
            GTGuiTextures.PICTURE_ARROW_DOUBLE.asWidget()
                .size(12)
                .margin(3));

        // toggle button for config panel
        IPanelHandler settingsPanel = syncManager
            .syncedPanel("configPanel", true, (manager, handler) -> createStackSizeConfigurationPanel(panel));
        mainColumn.child(new ToggleButton() {

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                switch (mouseButton) {
                    case 0:
                        next();
                        playClickSound();
                        return Result.SUCCESS;
                    case 1:
                        if (!settingsPanel.isPanelOpen()) settingsPanel.openPanel();
                        else settingsPanel.closePanel();
                        playClickSound();
                        return Result.SUCCESS;
                }
                return Result.IGNORE;
            }
        }.value(isAutoPullSyncer)
            .size(16)
            .margin(1)
            .setEnabledIf(b -> machine.autoPullAvailable)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_AUTOPULL_ME)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED)
            .addTooltipLine(GTUtility.translate("GT5U.machines.stocking_bus.auto_pull.tooltip.1"))
            .addTooltipLine(GTUtility.translate("GT5U.machines.stocking_bus.auto_pull.tooltip.2")));

        return mainColumn;
    }

    private ModularPanel createStackSizeConfigurationPanel(ModularPanel parent) {
        BooleanSyncValue isRecipeCheckSyncer = new BooleanSyncValue(machine::doFastRecipeCheck, machine::setRecipeCheck)
            .allowC2S();
        IntSyncValue minAutoPullStackSizeSyncer = new IntSyncValue(
            machine::getMinAutoPullStackSize,
            machine::setMinAutoPullStackSize).allowC2S();
        IntSyncValue autoPullRefreshTimeSyncer = new IntSyncValue(
            machine::getAutoPullRefreshTime,
            machine::setAutoPullRefreshTime).allowC2S();

        Flow mainColumn = Flow.col()
            .coverChildren()
            .marginTop(15)
            .childPadding(3);

        // stack size label
        mainColumn.child(
            IKey.lang("GT5U.machines.stocking_bus.min_stack_size")
                .asWidget());

        // stack size text field
        mainColumn.child(
            new TextFieldWidget().value(minAutoPullStackSizeSyncer)
                .numbersInt(1, Integer.MAX_VALUE)
                .formatAsInteger(true)
                .setMaxLength(10)
                .setTextAlignment(Alignment.CENTER)
                .width(72));

        // refresh time label
        mainColumn.child(
            IKey.lang("GT5U.machines.stocking_bus.refresh_time")
                .asWidget()
                .maxWidth(72)
                .textAlign(Alignment.Center));

        // refresh time text field
        mainColumn.child(
            new TextFieldWidget().value(autoPullRefreshTimeSyncer)
                .numbersInt(1, Integer.MAX_VALUE)
                .formatAsInteger(true)
                .setMaxLength(10)
                .setTextAlignment(Alignment.CENTER)
                .width(72));

        Flow recipeRow = Flow.row()
            .coverChildren()
            .childPadding(4);

        // recipe check label
        recipeRow.child(
            IKey.lang("GT5U.machines.stocking_bus.force_check")
                .asWidget()
                .maxWidth(50));

        // recipe check toggle button
        recipeRow.child(
            new ToggleButton().value(isRecipeCheckSyncer)
                .background(true, GTGuiTextures.BUTTON_STANDARD)
                .background(false, GTGuiTextures.BUTTON_STANDARD)
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS));

        mainColumn.child(recipeRow);

        return createPopUpPanel("configPanel").coverChildren()
            .relative(parent)
            .padding(5)
            .child(mainColumn)
            .leftRel(1)
            .topRel(0);
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + SLOT_SIZE;
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .coverChildren()
            .verticalCenter()
            .rightRel(0)
            .child(createLogo());
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActiveSyncer = new BooleanSyncValue(machine::isActive);
        BooleanSyncValue isPoweredSyncer = new BooleanSyncValue(machine::isPowered);
        BooleanSyncValue isBootingSyncer = new BooleanSyncValue(machine::isBooting);
        BooleanSyncValue isAllowedToWorkSyncer = new BooleanSyncValue(machine::isAllowedToWork);

        syncManager.syncValue("isActive", isActiveSyncer);
        syncManager.syncValue("isPowered", isPoweredSyncer);
        syncManager.syncValue("isBooting", isBootingSyncer);
        syncManager.syncValue("isAllowedToWork", isAllowedToWorkSyncer);

        // status label
        TextWidget<?> status = IKey.dynamic(() -> {
            boolean isActive = isActiveSyncer.getBoolValue();
            boolean isPowered = isPoweredSyncer.getBoolValue();
            boolean isBooting = isBootingSyncer.getBoolValue();

            String state = WailaText.getPowerState(isActive, isPowered, isBooting);

            if (isActive && isPowered) {
                return MessageFormat.format(
                    "{0} ({1})",
                    EnumChatFormatting.GREEN + state + EnumChatFormatting.RESET,
                    GTUtility.translate(
                        isAllowedToWorkSyncer.getBoolValue() ? "GT5U.gui.text.enabled" : "GT5U.gui.text.disabled"));
            } else {
                return EnumChatFormatting.DARK_RED + state + EnumChatFormatting.RESET;
            }
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);

        return super.createBottomLeftCornerFlow(panel, syncManager).child(status);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 1);

        GenericListSyncHandler<Slot> slotSyncHandler = GenericListSyncHandler.<Slot>builder()
            .getter(() -> GTDataUtils.mapToList(slots, slot -> slot == null ? null : slot.copy()))
            .setter(slots2 -> System.arraycopy(slots2.toArray(new Slot[0]), 0, slots, 0, SLOT_COUNT))
            .adapter(new MTEHatchInputBusMESlotAdapter())
            .build();
        syncManager.syncValue("slots", slotSyncHandler);
    }
}
