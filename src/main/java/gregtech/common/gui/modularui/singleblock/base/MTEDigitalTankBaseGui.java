package gregtech.common.gui.modularui.singleblock.base;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.text.DynamicKey;
import com.cleanroommc.modularui.drawable.text.StringKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.FluidLockSlot;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

public class MTEDigitalTankBaseGui {

    protected final MTEDigitalTankBase machine;

    public MTEDigitalTankBaseGui(MTEDigitalTankBase machine) {
        this.machine = machine;
    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(machine, guiData, syncManager, uiSettings)
            .build();

        machine.fluidTank.setAllowOverflow(machine.allowOverflow());
        machine.fluidTank.setPreventDraining(machine.mLockFluid);

        FluidSlotSyncHandler fluidTankSyncHandler = new FluidSlotSyncHandler(machine.fluidTank);
        FluidSlot fluidSlotWidget = new FluidSlot().syncHandler(fluidTankSyncHandler)
            .pos(58, 41)
            .background(GTGuiTextures.TRANSPARENT);

        FluidLockSlot fluidLockSlot = new FluidLockSlot(machine);
        FluidSlotSyncHandler lockSlotSyncHandler = new FluidSlotSyncHandler(fluidLockSlot).phantom(true);
        fluidLockSlot.syncHandler(lockSlotSyncHandler)
            .background(GTGuiTextures.TRANSPARENT)
            .pos(149, 41);

        final boolean isServer = GTUtility.isServer();

        // Left black panel
        panel.child(
            new Rectangle().setColor(0)
                .asWidget()
                .background(GTGuiTextures.BACKGROUND_SCREEN_BLACK)
                .pos(7, 16)
                .size(71, 45))
            .child(fluidSlotWidget)
            .child(
                new TextWidget<>(StatCollector.translateToLocal("GT5U.machines.digitaltank.fluid.amount"))
                    .color(machine.getColorTextWhite())
                    .pos(10, 20))
            .child(new TextWidget<>(new DynamicKey(() -> {
                if (fluidTankSyncHandler.getValue() != null && fluidTankSyncHandler.getValue()
                    .getFluid() != null) {
                    return IKey.str(NumberFormatUtil.formatNumber(fluidTankSyncHandler.getValue().amount));
                }
                return IKey.str("0");
            })).color(machine.getColorTextWhite())
                .pos(10, 30))

            // Item slots
            .child(
                new ItemSlot().background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_IN_STANDARD)
                    .slot(new ModularSlot(machine.inventoryHandler, machine.getInputSlot()))
                    .pos(79, 16))
            .child(
                new ItemSlot().background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD)
                    .slot(new ModularSlot(machine.inventoryHandler, machine.getOutputSlot()).accessibility(false, true))
                    .pos(79, 43))

            // Right black panel
            .child(
                new Rectangle().setColor(0)
                    .asWidget()
                    .background(GTGuiTextures.BACKGROUND_SCREEN_BLACK)
                    .pos(98, 16)
                    .size(71, 45))
            .child(
                new TextWidget<>(StatCollector.translateToLocal("GT5U.machines.digitaltank.lockfluid.label"))
                    .color(machine.getColorTextWhite())
                    .pos(101, 20))
            .child(fluidLockSlot)
            .child(new TextWidget<>(new DynamicKey(() -> {
                if (fluidLockSlot.getFluid() != null) {
                    return new StringKey(
                        fluidLockSlot.getFluid()
                            .getLocalizedName());
                }
                return IKey.str(StatCollector.translateToLocal("GT5U.machines.digitaltank.lockfluid.empty"));
            })).maxWidth(65)
                .color(machine.getColorTextWhite())
                .pos(101, 30))

            // Bottom buttons
            .child(
                new ToggleButton()
                    .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                    .selectedBackground(
                        GTGuiTextures.BUTTON_STANDARD_PRESSED,
                        GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                    .value(new BooleanSyncValue(() -> machine.mOutputFluid, val -> {
                        machine.mOutputFluid = val;
                        if (isServer) {
                            if (!machine.mOutputFluid) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("262", "Fluid Auto Output Disabled"));
                            } else {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("263", "Fluid Auto Output Enabled"));
                            }
                        }
                    }))
                    .pos(7, 63)
                    .size(18))
            .child(
                new ToggleButton().background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .selectedBackground(GTGuiTextures.BUTTON_STANDARD_PRESSED, GTGuiTextures.OVERLAY_BUTTON_LOCK)
                    .value(new BooleanSyncValue(() -> machine.mLockFluid, val -> {
                        machine.lockFluid(val);
                        machine.fluidTank.setPreventDraining(machine.mLockFluid);

                        String inBrackets;
                        if (machine.mLockFluid) {
                            if (machine.mFluid == null) {
                                machine.setLockedFluid(null);
                                inBrackets = GTUtility
                                    .trans("264", "currently none, will be locked to the next that is put in");
                            } else {
                                machine.setLockedFluid(
                                    machine.getDrainableStack()
                                        .getFluid());
                                inBrackets = machine.getDrainableStack()
                                    .getLocalizedName();
                            }
                            if (isServer) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    String.format("%s (%s)", GTUtility.trans("265", "1 specific Fluid"), inBrackets));
                            }
                        } else {
                            machine.fluidTank.drain(0, true);
                            if (isServer) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("266", "Lock Fluid Mode Disabled"));
                            }
                        }
                    }))
                    .pos(25, 63)
                    .size(18))
            .child(
                new ToggleButton()
                    .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                    .selectedBackground(
                        GTGuiTextures.BUTTON_STANDARD_PRESSED,
                        GTGuiTextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                    .value(new BooleanSyncValue(() -> machine.mAllowInputFromOutputSide, val -> {
                        machine.mAllowInputFromOutputSide = val;
                        if (isServer) {
                            if (!machine.mAllowInputFromOutputSide) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    StatCollector.translateToLocal("gt.interact.desc.input_from_output_off"));
                            } else {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    StatCollector.translateToLocal("gt.interact.desc.input_from_output_on"));
                            }
                        }
                    }))
                    .pos(43, 63)
                    .size(18))

            .child(
                new ToggleButton()
                    .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                    .selectedBackground(
                        GTGuiTextures.BUTTON_STANDARD_PRESSED,
                        GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                    .value(new BooleanSyncValue(() -> machine.mVoidFluidPart, val -> {
                        machine.mVoidFluidPart = val;
                        machine.fluidTank.setAllowOverflow(machine.allowOverflow());
                        if (isServer) {
                            if (!machine.mVoidFluidPart) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("267", "Overflow Voiding Mode Disabled"));
                            } else {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("268", "Overflow Voiding Mode Enabled"));
                            }
                        }
                    }))
                    .pos(98, 63)
                    .size(18))
            .child(
                new ToggleButton().background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .selectedBackground(
                        GTGuiTextures.BUTTON_STANDARD_PRESSED,
                        GTGuiTextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                    .value(new BooleanSyncValue(() -> machine.mVoidFluidFull, val -> {
                        machine.mVoidFluidFull = val;
                        machine.fluidTank.setAllowOverflow(machine.allowOverflow());
                        if (isServer) {
                            if (!machine.mVoidFluidFull) {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("269", "Void Full Mode Disabled"));
                            } else {
                                GTUtility.sendChatToPlayer(
                                    guiData.getPlayer(),
                                    GTUtility.trans("270", "Void Full Mode Enabled"));
                            }
                        }
                    }))
                    .pos(116, 63)
                    .size(18));

        return panel;
    }
}
