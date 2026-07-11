package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.metatileentity.IFluidLockableMui2;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;

public class MTEMachineWithFluidScreenBaseGui<T extends MTETieredMachineBlock> extends MTETieredMachineBlockBaseGui<T> {

    protected static final int FLUID_SCREEN_WIDTH = 71;
    protected static final int FLUID_SCREEN_HEIGHT = 54;
    protected static final int FLUID_FILTER_SCREEN_WIDTH = 71;
    protected static final int FLUID_FILTER_SCREEN_HEIGHT = 54;

    public MTEMachineWithFluidScreenBaseGui(T machine) {
        super(machine);
    }

    protected boolean supportsFluidScreen() {
        return true;
    }

    protected ParentWidget<?> createScreen(ModularPanel panel, PanelSyncManager syncManager, IFluidTank fluidTank) {
        ParentWidget<?> screen = CommonWidgets.createFluidScreen(FLUID_SCREEN_WIDTH, FLUID_SCREEN_HEIGHT);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // liquid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.basic_tank.liquid_amount")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        // liquid amount
        textColumn.child(
            IKey.dynamic(() -> formatNumber(fluidTank.getFluidAmount()))
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        screen.child(textColumn);

        screen.child(createFluidSlot(panel, syncManager, fluidTank));

        return screen;
    }

    protected FluidSlot createFluidSlot(ModularPanel panel, PanelSyncManager syncManager, IFluidTank fluidTank) {
        return new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTank).filter(getFluidSlotFilter()))
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    protected Predicate<FluidStack> getFluidSlotFilter() {
        return _ -> true;
    }

    protected boolean supportsFluidIOColumn() {
        return true;
    }

    protected Flow createIO(ModularPanel panel, PanelSyncManager syncManager, int inputSlot, int outputSlot,
        IFluidTank fluidTank) {
        Flow ioColumn = Flow.column()
            .coverChildrenWidth()
            .fullHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        ioColumn.child(createInputSlot(panel, syncManager, inputSlot));
        ioColumn.child(
            new FluidSlot().size(16)
                .syncHandler(
                    new FluidSlotSyncHandler(fluidTank).controlsAmount(false)
                        .canDrainSlot(false)
                        .canFillSlot(false))
                .alwaysShowFull(false)
                .background()
                .hoverBackground()
                .overlay(GTGuiTextures.PICTURE_GAUGE));
        ioColumn.child(createOutputSlot(panel, syncManager, outputSlot));

        return ioColumn;
    }

    protected ItemSlot createInputSlot(ModularPanel panel, PanelSyncManager syncManager, int inputSlot) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, inputSlot).singletonSlotGroup())
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD);
    }

    protected ItemSlot createOutputSlot(ModularPanel panel, PanelSyncManager syncManager, int outputSlot) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, outputSlot).canPut(false))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD);
    }

    protected boolean supportsFluidFilterScreen() {
        return machine instanceof IFluidLockableMui2;
    }

    protected ParentWidget<?> createFilterScreen(ModularPanel panel, PanelSyncManager syncManager) {
        if (!(machine instanceof IFluidLockableMui2))
            throw new UnsupportedOperationException("Machine must be IFluidLockableMui2");

        ParentWidget<?> screen = CommonWidgets.createFluidScreen(FLUID_FILTER_SCREEN_WIDTH, FLUID_FILTER_SCREEN_HEIGHT);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        FluidLockSlotWidget fluidLockSlotWidget = new FluidLockSlotWidget((IFluidLockableMui2) machine);

        // fluid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.digitaltank.lockfluid.label")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        // fluid name
        textColumn.child(IKey.dynamic(() -> {
            if (fluidLockSlotWidget.getFluid() != null) {
                return fluidLockSlotWidget.getFluid()
                    .getLocalizedName();
            }
            return translateToLocal("GT5U.machines.digitaltank.lockfluid.empty");
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        screen.child(textColumn);

        // fluid lock slot
        screen.child(
            fluidLockSlotWidget.syncHandler(new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true))
                .bottomRel(0)
                .rightRel(0)
                .background(GTGuiTextures.SLOT_FLUID_TANK));

        return screen;
    }
}
