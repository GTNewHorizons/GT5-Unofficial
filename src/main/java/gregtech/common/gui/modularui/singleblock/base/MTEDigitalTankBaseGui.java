package gregtech.common.gui.modularui.singleblock.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

public class MTEDigitalTankBaseGui<T extends MTEDigitalTankBase> extends MTEBasicTankBaseGui<T> {

    public MTEDigitalTankBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsGauge() {
        return false;
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }

    @Override
    protected boolean supportsPowerSwitch() {
        return false;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .paddingLeft(4)
            .paddingTop(10)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(createLeftSide(panel, syncManager));
        mainRow.child(createIO(panel, syncManager));
        mainRow.child(createRightSide(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected Flow createLeftSide(ModularPanel panel, PanelSyncManager syncManager) {
        Flow leftSide = Flow.col()
            .childPadding(2)
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow buttonRow = Flow.row()
            .coverChildren();

        // auto output
        buttonRow.child(
            new ToggleButton().size(18)
                .value(new BooleanSyncValue(machine::isOutputFluid, machine::setOutputFluid))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitaltank.autooutput.tooltip"))));

        // lock
        buttonRow.child(
            new ToggleButton().size(18)
                .value(new BooleanSyncValue(machine::isFluidLocked, machine::lockFluid))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip.1"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip.2"));
                }));

        // allow input
        buttonRow.child(
            new ToggleButton().size(18)
                .value(new BooleanSyncValue(machine::isAllowInputFromOutputSide, machine::setAllowInputFromOutputSide))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitaltank.inputfromoutput.tooltip"))));

        leftSide.child(createScreen(panel, syncManager));
        leftSide.child(buttonRow);

        return leftSide;
    }

    protected Flow createRightSide(ModularPanel panel, PanelSyncManager syncManager) {
        Flow rightSide = Flow.col()
            .childPadding(2)
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow buttonRow = Flow.row()
            .coverChildren();

        // overflow
        buttonRow.child(
            new ToggleButton().size(18)
                .value(new BooleanSyncValue(machine::isVoidFluidPart, machine::setVoidFluidPart))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidoverflow.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidoverflow.tooltip.1"));
                }));

        // void
        buttonRow.child(
            new ToggleButton().size(18)
                .value(new BooleanSyncValue(machine::isVoidFluidFull, machine::setVoidFluidFull))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidfull.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidfull.tooltip.1"));
                }));

        rightSide.child(createFilterScreen(panel, syncManager));
        rightSide.child(buttonRow);

        return rightSide;
    }

    protected Flow createFilterScreen(ModularPanel panel, PanelSyncManager syncManager) {
        Flow screen = Flow.col()
            .size(71, 45)
            .padding(3, 2, 3, 2)
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK);

        FluidLockSlotWidget fluidLockSlotWidget = new FluidLockSlotWidget(machine);

        // fluid amount label
        screen.child(
            IKey.lang("GT5U.machines.digitaltank.lockfluid.label")
                .asWidget()
                .color(Color.WHITE.main));

        // fluid name
        screen.child(IKey.dynamic(() -> {
            if (fluidLockSlotWidget.getFluid() != null) {
                return fluidLockSlotWidget.getFluid()
                    .getLocalizedName();
            }
            return translateToLocal("GT5U.machines.digitaltank.lockfluid.empty");
        })
            .asWidget()
            .color(Color.WHITE.main));

        // fluid lock slot
        screen.child(
            fluidLockSlotWidget.syncHandler(new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true))
                .align(Alignment.BottomRight)
                .background(IDrawable.EMPTY));

        return screen;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        machine.getFluidTank()
            .setPreventDraining(true);
    }
}
