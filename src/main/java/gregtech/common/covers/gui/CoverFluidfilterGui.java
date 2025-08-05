package gregtech.common.covers.gui;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverFluidfilter;
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.covers.modes.FilterDirectionMode;
import gregtech.common.covers.modes.FilterType;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverFluidfilterGui extends CoverGui<CoverFluidfilter> {

    public CoverFluidfilterGui(CoverFluidfilter cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.fluid_filter";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<FilterDirectionMode> ioModeSyncValue = new EnumSyncValue<>(
            FilterDirectionMode.class,
            cover::getFilterDirection,
            cover::setFilterDirection);
        syncManager.syncValue("io_mode", ioModeSyncValue);
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            cover::getFilterType,
            cover::setFilterType);
        syncManager.syncValue("filter_type", filterTypeSyncValue);
        EnumSyncValue<BlockMode> blockModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            cover::getBlockMode,
            cover::setBlockMode);
        syncManager.syncValue("block_mode", blockModeSyncValue);

        IFluidTank filterTank = new FluidStackTank(() -> {
            Fluid fluid = FluidRegistry.getFluid(cover.getFluidId());
            if (fluid != null) {
                return new FluidStack(fluid, 1);
            }
            return null;
        }, fluidStack -> {
            int fluidId = fluidStack != null ? FluidRegistry.getFluidID(fluidStack.getFluid()) : -1;
            cover.setFluidId(fluidId);
        }, 1);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(FilterDirectionMode.class).value(ioModeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_IMPORT, GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                        .build(),
                    IKey.str(GTUtility.trans("238", "Filter Direction"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                        .build(),
                    IKey.str(GTUtility.trans("239", "Filter Type"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(BlockMode.class).value(blockModeSyncValue)
                        .overlay(
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == FilterDirectionMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT),
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == FilterDirectionMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT))
                        .tooltip(
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == FilterDirectionMode.INPUT
                                    ? GTUtility.trans("312", "Allow Output")
                                    : GTUtility.trans("314", "Allow Input")),
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == FilterDirectionMode.INPUT
                                    ? GTUtility.trans("311", "Block Output")
                                    : GTUtility.trans("313", "Block Input")))
                        .build(),
                    IKey.str(GTUtility.trans("240", "Block Flow"))
                        .asWidget()))
            .child(
                Flow.row()
                    .marginLeft(WIDGET_MARGIN)
                    .marginTop(WIDGET_MARGIN)
                    .coverChildren()
                    .childPadding(WIDGET_MARGIN)
                    .child(
                        new FluidSlot().syncHandler(
                            new FluidSlotSyncHandler(filterTank).phantom(true)
                                .controlsAmount(false)))
                    .child(IKey.dynamic(() -> {
                        FluidStack fluidStack = filterTank.getFluid();
                        if (fluidStack != null) {
                            return fluidStack.getLocalizedName();
                        }
                        return GTUtility.trans("315", "Filter Empty");
                    })
                        .asWidget()));
    }

}
