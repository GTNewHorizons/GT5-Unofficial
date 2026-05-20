package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

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
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;

public class MTEBasicTankBaseGui<T extends MTEBasicTank> extends MTETieredMachineBlockBaseGui<T> {

    public MTEBasicTankBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.childIf(supportsFluidScreen(), () -> createScreen(panel, syncManager));
        mainRow.childIf(supportsFluidIOColumn(), () -> createIO(panel, syncManager));
        mainRow.childIf(supportsFluidFilterScreen(), () -> createFilterScreen(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected boolean supportsFluidScreen() {
        return true;
    }

    protected int getFluidScreenWidth() {
        return 71;
    }

    protected int getFluidScreenHeight() {
        return 54;
    }

    protected ParentWidget<?> createScreen(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> screen = CommonWidgets.createFluidScreen(getFluidScreenWidth(), getFluidScreenHeight());

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // liquid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.basic_tank.liquid_amount")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // liquid amount
        textColumn.child(
            IKey.dynamic(
                () -> formatNumber(
                    machine.getFluidTank()
                        .getFluidAmount()))
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        screen.child(textColumn);

        screen.child(createFluidSlot(panel, syncManager));

        return screen;
    }

    protected FluidSlot createFluidSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new FluidSlot().syncHandler(new FluidSlotSyncHandler(machine.getFluidTank()))
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    protected boolean supportsFluidIOColumn() {
        return true;
    }

    protected Flow createIO(ModularPanel panel, PanelSyncManager syncManager) {
        Flow ioColumn = Flow.column()
            .coverChildrenWidth()
            .fullHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        ioColumn.child(createInputSlot(panel, syncManager));
        ioColumn.child(createOutputSlot(panel, syncManager));

        return ioColumn;
    }

    protected ItemSlot createInputSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new ItemSlot()
            .slot(new ModularSlot(machine.inventoryHandler, machine.getInputSlot()).singletonSlotGroup())
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD);
    }

    protected ItemSlot createOutputSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getOutputSlot()).canPut(false))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD);
    }

    protected boolean supportsFluidFilterScreen() {
        return machine instanceof IFluidLockableMui2;
    }

    protected int getFluidFilterScreenWidth() {
        return 71;
    }

    protected int getFluidFilterScreenHeight() {
        return 54;
    }

    protected ParentWidget<?> createFilterScreen(ModularPanel panel, PanelSyncManager syncManager) {
        if (!(machine instanceof IFluidLockableMui2))
            throw new UnsupportedOperationException("Machine must be IFluidLockableMui2");

        ParentWidget<?> screen = CommonWidgets
            .createFluidScreen(getFluidFilterScreenWidth(), getFluidFilterScreenHeight());

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        FluidLockSlotWidget fluidLockSlotWidget = new FluidLockSlotWidget((IFluidLockableMui2) machine);

        // fluid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.digitaltank.lockfluid.label")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // fluid name
        textColumn.child(IKey.dynamic(() -> {
            if (fluidLockSlotWidget.getFluid() != null) {
                return fluidLockSlotWidget.getFluid()
                    .getLocalizedName();
            }
            return translateToLocal("GT5U.machines.digitaltank.lockfluid.empty");
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

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
