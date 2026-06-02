package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner.SLOT_CONVEYOR;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner.SLOT_FILTER;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner.SLOT_ROTOR;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner;

public class MTEAtmosphericReconditionerGui extends MTETieredMachineBlockBaseGui<MTEAtmosphericReconditioner> {

    public MTEAtmosphericReconditionerGui(MTEAtmosphericReconditioner machine) {
        super(machine);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(SLOT_SIZE)
            .center();

        mainRow.child(
            new ItemSlot().slot(
                new ModularSlot(machine.inventoryHandler, SLOT_ROTOR).filter(machine::hasRotor)
                    .singletonSlotGroup())
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_TURBINE)
                .addTooltipLine(GTUtility.translate("gtpp.gui.atmospheric_reconditioner.slot.rotor")));
        mainRow.child(
            new ItemSlot().slot(
                new ModularSlot(machine.inventoryHandler, SLOT_FILTER).filter(machine::hasAirFilter)
                    .singletonSlotGroup())
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_RECYCLE)
                .addTooltipLine(GTUtility.translate("gtpp.gui.atmospheric_reconditioner.slot.filter")));
        mainRow
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(machine.inventoryHandler, SLOT_CONVEYOR).filter(this::isCorrectConveyor)
                            .singletonSlotGroup())
                    .addTooltipLine(GTUtility.translate("gtpp.gui.atmospheric_reconditioner.slot.conveyor")));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue reductionSyncer = new IntSyncValue(machine::getPollutionReduction);
        syncManager.syncValue("pollutionReduction", reductionSyncer);

        return super.createBottomLeftCornerFlow(panel, syncManager).childPadding(6)
            .child(
                GTGuiTextures.INFORMATION_SYMBOL.asWidget()
                    .size(7, 18)
                    .marginLeft(5)
                    .tooltipAutoUpdate(true)
                    .tooltipDynamic(
                        t -> t.addLine(
                            GTUtility.translate(
                                "gtpp.gui.atmospheric_reconditioner.tooltip.reduction",
                                formatNumber(reductionSyncer.getIntValue())))))
            .child(
                new CycleButtonWidget().stateCount(2)
                    .value(new BooleanSyncValue(machine::isSaveRotor, machine::setSaveRotor).allowC2S())
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                    .tooltip(
                        0,
                        t -> t.addLine(
                            GTUtility.translate(
                                "gtpp.chat.atmospheric_reconditioner.efficiency",
                                GTUtility.translate("gtpp.chat.atmospheric_reconditioner.efficiency.low"))))
                    .tooltip(
                        1,
                        t -> t.addLine(
                            GTUtility.translate(
                                "gtpp.chat.atmospheric_reconditioner.efficiency",
                                GTUtility.translate("gtpp.chat.atmospheric_reconditioner.efficiency.high")))));
    }

    private boolean isCorrectConveyor(ItemStack stack) {
        return (switch (machine.mTier) {
            case 1 -> ItemList.Conveyor_Module_LV;
            case 2 -> ItemList.Conveyor_Module_MV;
            case 3 -> ItemList.Conveyor_Module_HV;
            case 4 -> ItemList.Conveyor_Module_EV;
            case 5 -> ItemList.Conveyor_Module_IV;
            case 6 -> ItemList.Conveyor_Module_LuV;
            case 7 -> ItemList.Conveyor_Module_ZPM;
            case 8 -> ItemList.Conveyor_Module_UV;
            case 9 -> ItemList.Conveyor_Module_UHV;
            default -> throw new IllegalArgumentException("Tier not allowed for Atmospheric Reconditioner!");
        }).isStackEqual(stack, false, true);
    }
}
