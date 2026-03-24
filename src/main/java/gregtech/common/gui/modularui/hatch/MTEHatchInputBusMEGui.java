package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.tileentities.machines.MTEHatchInputBusME.SLOT_COUNT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.text.MessageFormat;
import java.util.Arrays;
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
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.adapter.MTEHatchInputBusMESlotAdapter;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.StockingSlot;
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
        BooleanSyncValue isAutoPullSyncer = new BooleanSyncValue(hatch::isAutoPullItemList, hatch::setAutoPullItemList);

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

                hatch.setSlotConfig(slotIndex, GTUtility.copyAmount(1, stack));

                if (baseMetaTileEntity.isServerSide()) {
                    try {
                        hatch.updateInformationSlot(slotIndex);
                    } catch (GridAccessException e) {
                        // :P
                    }
                }
            }
        };

        Flow mainRow = Flow.row()
            .paddingLeft(3)
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.END);

        mainRow.child(createFilterSlots(syncManager, configItemHandler, isAutoPullSyncer));
        mainRow.child(createMiddleColumn(syncManager, panel, isAutoPullSyncer));
        mainRow.child(createStockSlots(syncManager, configItemHandler));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private SlotGroupWidget createFilterSlots(PanelSyncManager syncManager, IItemHandlerModifiable configItemHandler,
        BooleanSyncValue isAutoPullSyncer) {
        syncManager.registerSlotGroup(FILTER_INV_NAME, FILTER_SLOT_ROW);

        String[] matrix = new String[FILTER_SLOT_ROW];
        String repeat = StringUtils.getRepetitionOf('s', FILTER_SLOT_PER_ROW);
        Arrays.fill(matrix, repeat);

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new StockingSlot(isAutoPullSyncer).slot(
                    new ModularSlot(configItemHandler, index)
                        .filter(is -> doesNotContainsSuchStack(is) && !isAutoPullSyncer.getBoolValue())
                        .slotGroup(FILTER_INV_NAME)))
            .build()
            .coverChildren();
    }

    private boolean doesNotContainsSuchStack(ItemStack tStack) {
        return Stream.of(slots)
            .filter(Objects::nonNull)
            .noneMatch(slot -> GTUtility.areStacksEqual(slot.config, tStack));
    }

    private SlotGroupWidget createStockSlots(PanelSyncManager syncManager, IItemHandlerModifiable configItemHandler) {
        syncManager.registerSlotGroup(STOCK_INV_NAME, STOCK_SLOT_ROW);

        String[] matrix = new String[STOCK_SLOT_ROW];
        String repeat = StringUtils.getRepetitionOf('s', STOCK_SLOT_PER_ROW);
        Arrays.fill(matrix, repeat);

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new ItemSlot()
                    .slot(new ModularSlot(configItemHandler, index + SLOT_COUNT).accessibility(false, false))
                    .background(GTGuiTextures.SLOT_ITEM_DARK))
            .build()
            .coverChildren();
    }

    private Flow createMiddleColumn(PanelSyncManager syncManager, ModularPanel panel,
        BooleanSyncValue isAutoPullSyncer) {
        Flow mainColumn = Flow.col()
            .width(18)
            .reverseLayout()
            .mainAxisAlignment(Alignment.MainAxis.END);

        // circuit slot
        syncManager.registerSlotGroup("item_inv", 1);
        mainColumn.child(createCircuitSlot(syncManager));

        // manual slot
        mainColumn.child(
            new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, hatch.getManualSlot()).slotGroup("item_inv")));

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
            .setEnabledIf(b -> hatch.autoPullAvailable)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_AUTOPULL_ME)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_AUTOPULL_ME_DISABLED)
            .tooltip(t -> {
                t.addLine(translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip.1"));
                t.addLine(translateToLocal("GT5U.machines.stocking_bus.auto_pull.tooltip.2"));
            }));

        return mainColumn;
    }

    private ModularPanel createStackSizeConfigurationPanel(ModularPanel parent) {
        BooleanSyncValue isRecipeCheckSyncer = new BooleanSyncValue(hatch::doFastRecipeCheck, hatch::setRecipeCheck);
        IntSyncValue minAutoPullStackSizeSyncer = new IntSyncValue(
            hatch::getMinAutoPullStackSize,
            hatch::setMinAutoPullStackSize);
        IntSyncValue autoPullRefreshTimeSyncer = new IntSyncValue(
            hatch::getAutoPullRefreshTime,
            hatch::setAutoPullRefreshTime);

        Flow mainColumn = Flow.col()
            .padding(3)
            .childPadding(3);

        // stack size label
        mainColumn.child(
            IKey.lang("GT5U.machines.stocking_bus.min_stack_size")
                .asWidget());

        // stack size text field
        mainColumn.child(
            new TextFieldWidget().value(minAutoPullStackSizeSyncer)
                .setNumbers(1, Integer.MAX_VALUE)
                .setFormatAsInteger(true)
                .setMaxLength(10)
                .setTextAlignment(Alignment.CENTER)
                .width(72));

        // refresh time label
        mainColumn.child(
            IKey.lang("GT5U.machines.stocking_bus.refresh_time")
                .asWidget());

        // refresh time text field
        mainColumn.child(
            new TextFieldWidget().value(autoPullRefreshTimeSyncer)
                .setNumbers(1, Integer.MAX_VALUE)
                .setFormatAsInteger(true)
                .setMaxLength(10)
                .setTextAlignment(Alignment.CENTER)
                .width(72));

        Flow recipeRow = Flow.row()
            .coverChildren();

        // recipe check label
        recipeRow.child(
            IKey.lang("GT5U.machines.stocking_bus.force_check")
                .asWidget()
                .width(50));

        // recipe check toggle button
        recipeRow.child(
            new ToggleButton().value(isRecipeCheckSyncer)
                .background(true, GTGuiTextures.BUTTON_STANDARD)
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS));

        mainColumn.child(recipeRow);

        return createPopUpPanel("configPanel").size(85, 125)
            .relative(parent)
            .paddingTop(15)
            .child(mainColumn)
            .leftRel(1)
            .topRel(0);
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + 16;
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .reverseLayout(true)
            .align(Alignment.BottomRight)
            .paddingBottom(2)
            .paddingRight(3)
            .childIf(this.doesAddGregTechLogo(), this::createLogo);
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActiveSyncer = new BooleanSyncValue(hatch::isActive);
        BooleanSyncValue isPoweredSyncer = new BooleanSyncValue(hatch::isPowered);
        BooleanSyncValue isBootingSyncer = new BooleanSyncValue(hatch::isBooting);
        BooleanSyncValue isAllowedToWorkSyncer = new BooleanSyncValue(hatch::isAllowedToWork);

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
                    "{0}{1}§f ({2})",
                    EnumChatFormatting.GREEN,
                    state,
                    translateToLocal(
                        isAllowedToWorkSyncer.getBoolValue() ? "GT5U.gui.text.enabled" : "GT5U.gui.text.disabled"));
            } else {
                return EnumChatFormatting.DARK_RED + state;
            }
        })
            .asWidget();

        return super.createLeftCornerFlow(panel, syncManager).child(status);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        GenericListSyncHandler<Slot> slotSyncHandler = GenericListSyncHandler.<Slot>builder()
            .getter(() -> GTDataUtils.mapToList(slots, slot -> slot == null ? null : slot.copy()))
            .setter(slots2 -> System.arraycopy(slots2.toArray(new Slot[0]), 0, slots, 0, SLOT_COUNT))
            .adapter(new MTEHatchInputBusMESlotAdapter())
            .build();
        syncManager.syncValue("slots", slotSyncHandler);
    }
}
