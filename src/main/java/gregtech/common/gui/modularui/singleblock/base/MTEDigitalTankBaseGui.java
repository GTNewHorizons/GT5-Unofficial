package gregtech.common.gui.modularui.singleblock.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

public class MTEDigitalTankBaseGui<T extends MTEDigitalTankBase> extends MTEBasicTankBaseGui<T> {

    public MTEDigitalTankBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow row = super.createBottomLeftCornerFlow(panel, syncManager);

        // auto output
        row.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isOutputFluid, machine::setOutputFluid))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitaltank.autooutput.tooltip"))));

        // lock
        row.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isFluidLocked, machine::lockFluid))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_LOCK)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip.1"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip.2"));
                }));

        // allow input
        row.child(
            new ToggleButton()
                .value(new BooleanSyncValue(machine::isAllowInputFromOutputSide, machine::setAllowInputFromOutputSide))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.machines.digitaltank.inputfromoutput.tooltip"))));

        return row;
    }

    @Override
    protected FluidSlot createFluidSlot(ModularPanel panel, PanelSyncManager syncManager, IFluidTank fluidTank) {
        FluidSlotSyncHandler fluidSlotSH = new FluidSlotSyncHandler(fluidTank);
        fluidSlotSH.setChangeListener(machine::setLockIfEmpty);

        return new FluidSlot().syncHandler(fluidSlotSH)
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow buttonRow = Flow.row()
            .coverChildren()
            .marginRight(SLOT_SIZE);

        // overflow
        buttonRow.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isVoidFluidPart, machine::setVoidFluidPart))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidoverflow.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidoverflow.tooltip.1"));
                }));

        // void
        buttonRow.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isVoidFluidFull, machine::setVoidFluidFull))
                .overlay(GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                .tooltip(t -> {
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidfull.tooltip"));
                    t.addLine(translateToLocal("GT5U.machines.digitaltank.voidfull.tooltip.1"));
                }));

        return super.createBottomRightCornerFlow(panel, syncManager).child(buttonRow);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        machine.getFluidTank()
            .setPreventDraining(true);
    }
}
