package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.tileentities.machines.MTEHatchInputME.SLOT_COUNT;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.utils.fluid.FluidInteractions;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import appeng.core.localization.WailaText;
import appeng.me.GridAccessException;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.adapter.MTEHatchInputMESlotAdapter;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.MTEHatchInputME.Slot;

public class MTEHatchInputMEGui extends MTEHatchBaseGui<MTEHatchInputME> {

    private static final String FILTER_INV_NAME = "filter_inv";
    private static final String STOCK_INV_NAME = "stock_inv";
    private static final int FILTER_SLOT_ROW = 4;
    private static final int FILTER_SLOT_PER_ROW = 4;
    private static final int STOCK_SLOT_ROW = 4;
    private static final int STOCK_SLOT_PER_ROW = 4;
    private final Slot[] slots;

    public MTEHatchInputMEGui(MTEHatchInputME hatch, Slot[] slots) {
        super(hatch);
        this.slots = slots;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isAutoPullSyncer = new BooleanSyncValue(
            machine::isAutoPullFluidList,
            machine::setAutoPullFluidList).allowC2S();

        Flow mainRow = Flow.row()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(createFilterSlots(syncManager, isAutoPullSyncer));
        mainRow.child(createMiddleColumn(syncManager, panel, isAutoPullSyncer));
        mainRow.child(createStockSlots(syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private Grid createFilterSlots(PanelSyncManager syncManager, BooleanSyncValue isAutoPullSyncer) {
        syncManager.registerSlotGroup(FILTER_INV_NAME, FILTER_SLOT_ROW);

        return new Grid().coverChildren()
            .gridOfWidthHeight(FILTER_SLOT_PER_ROW, FILTER_SLOT_ROW, ($x, $y, index) -> new FluidSlot() {

                @Override
                protected void addToolTip(RichTooltip tooltip) {
                    FluidStack fluid = getFluidStack();

                    if (fluid != null) {
                        tooltip.addFromFluid(fluid);

                        if (!isAutoPullSyncer.getBoolValue()) {
                            tooltip.addLine(IKey.lang("modularui2.fluid.phantom.clear"));
                        }
                    } else {
                        tooltip.addLine(IKey.lang("modularui2.fluid.empty"));
                    }

                    if (isAutoPullSyncer.getBoolValue()) {
                        tooltip.addLine(IKey.lang("GT5U.machines.stocking_bus.cannot_set_slot"));
                    }
                }
            }.syncHandler(new FluidSlotSyncHandler(new ConfigFluidTank(index)) {

                @Override
                public void tryScrollPhantom(MouseData mouseData) {}

                @Override
                protected void tryClickPhantom(MouseData mouseData, ItemStack cursorStack) {
                    if (mouseData.mouseButton != 0 || isAutoPullSyncer.getBoolValue()) return;

                    FluidStack heldFluid = FluidInteractions.getFluidForItem(cursorStack);

                    if (heldFluid != null && containsSuchStack(heldFluid)) return;

                    machine.setSlotConfig(index, GTUtility.copyAmount(1, heldFluid));

                    if (baseMetaTileEntity.isServerSide()) {
                        try {
                            machine.updateInformationSlot(index);
                        } catch (GridAccessException e) {
                            // :P
                        }
                    }
                }
            }.phantom(true)
                .controlsAmount(false))
                .backgroundOverlay(
                    new DynamicDrawable(
                        () -> !isAutoPullSyncer.getBoolValue() ? GTGuiTextures.SLOT_FLUID_DARK
                            : GuiTextures.SLOT_FLUID),
                    GTGuiTextures.OVERLAY_SLOT_ARROW_ME));
    }

    private boolean containsSuchStack(FluidStack tStack) {
        return Stream.of(slots)
            .filter(Objects::nonNull)
            .anyMatch(slot -> GTUtility.areFluidsEqual(slot.config, tStack));
    }

    private Flow createMiddleColumn(PanelSyncManager syncManager, ModularPanel panel,
        BooleanSyncValue isAutoPullSyncer) {
        Flow mainColumn = Flow.col()
            .width(18)
            .mainAxisAlignment(Alignment.MainAxis.START)
            .coverChildrenHeight();

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
            .tooltip(t -> {
                t.addLine(IKey.lang("GT5U.machines.stocking_hatch.auto_pull.tooltip.1"));
                t.addLine(IKey.lang("GT5U.machines.stocking_hatch.auto_pull.tooltip.2"));
            }));

        // arrow
        mainColumn.child(
            GTGuiTextures.PICTURE_ARROW_DOUBLE.asWidget()
                .size(12)
                .margin(3));

        return mainColumn;
    }

    private ModularPanel createStackSizeConfigurationPanel(ModularPanel parent) {
        IntSyncValue minAutoPullAmountSyncer = new IntSyncValue(
            machine::getMinAutoPullAmount,
            machine::setMinAutoPullAmount).allowC2S();
        IntSyncValue autoPullRefreshTimeSyncer = new IntSyncValue(
            machine::getAutoPullRefreshTime,
            machine::setAutoPullRefreshTime).allowC2S();

        Flow mainColumn = Flow.col()
            .coverChildren()
            .marginTop(15)
            .childPadding(3);

        // stack size label
        mainColumn.child(
            IKey.lang("GT5U.machines.stocking_hatch.min_amount")
                .asWidget());

        // stack size text field
        mainColumn.child(
            new TextFieldWidget().value(minAutoPullAmountSyncer)
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

        return createPopUpPanel("configPanel").coverChildren()
            .relative(parent)
            .padding(5)
            .child(mainColumn)
            .leftRel(1)
            .topRel(0);
    }

    private Grid createStockSlots(PanelSyncManager syncManager) {
        syncManager.registerSlotGroup(STOCK_INV_NAME, STOCK_SLOT_ROW);

        return new Grid().coverChildren()
            .gridOfWidthHeight(STOCK_SLOT_PER_ROW, STOCK_SLOT_ROW, ($x, $y, index) -> new FluidSlot() {

                @Override
                protected void addToolTip(RichTooltip tooltip) {
                    FluidStack fluid = getFluidStack();

                    if (fluid != null) {
                        tooltip.addFromFluid(fluid);
                        tooltip.addLine(
                            IKey.lang(
                                "modularui2.fluid.phantom.amount",
                                this.formatFluidTooltipAmount(fluid.amount),
                                this.getBaseUnit()));
                        addAdditionalFluidInfo(tooltip, fluid);

                        if (!Interactable.hasShiftDown()) {
                            tooltip.addLine(IKey.lang("modularui2.tooltip.shift"));
                        }
                    } else {
                        tooltip.addLine(IKey.lang("modularui2.fluid.empty"));
                    }
                }
            }.syncHandler(new FluidSlotSyncHandler(new ExtractedFluidTank(index)) {

                @Override
                protected void tryClickPhantom(MouseData mouseData, ItemStack cursorStack) {}

                @Override
                public void tryScrollPhantom(MouseData mouseData) {}
            }.phantom(true))
                .backgroundOverlay(GTGuiTextures.SLOT_ITEM_DARK));
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
                    IKey.lang(
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

        GenericListSyncHandler<Slot> slotSyncHandler = GenericListSyncHandler.<Slot>builder()
            .getter(() -> GTDataUtils.mapToList(slots, slot -> slot == null ? null : slot.copy()))
            .setter(slots2 -> System.arraycopy(slots2.toArray(new Slot[0]), 0, slots, 0, SLOT_COUNT))
            .adapter(new MTEHatchInputMESlotAdapter())
            .build();
        syncManager.syncValue("slots", slotSyncHandler);
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + SLOT_SIZE;
    }

    protected class ConfigFluidTank implements IFluidTank {

        private final int slotIndex;

        public ConfigFluidTank(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        @Override
        public FluidStack getFluid() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return null;

            return slot.config;
        }

        @Override
        public int getFluidAmount() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            return slot == null ? 0 : 1;
        }

        @Override
        public int getCapacity() {
            return 1;
        }

        @Override
        public FluidTankInfo getInfo() {
            return new FluidTankInfo(this);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }

    protected class ExtractedFluidTank implements IFluidTank {

        private final int slotIndex;

        public ExtractedFluidTank(int slotIndex) {
            this.slotIndex = slotIndex;
        }

        @Override
        public FluidStack getFluid() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return null;

            return slot.getOriginalExtracted();
        }

        @Override
        public int getFluidAmount() {
            Slot slot = GTDataUtils.getIndexSafe(slots, slotIndex);

            if (slot == null) return 0;

            return GTUtility.longToInt(slot.extractedAmount);
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public FluidTankInfo getInfo() {
            return new FluidTankInfo(this);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }
}
