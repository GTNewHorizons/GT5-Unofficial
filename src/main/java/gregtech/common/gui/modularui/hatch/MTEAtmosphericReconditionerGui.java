package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner;

public class MTEAtmosphericReconditionerGui extends MTEBasicMachineBaseGui<MTEAtmosphericReconditioner> {

    public MTEAtmosphericReconditionerGui(MTEAtmosphericReconditioner machine, BasicUIProperties properties) {
        super(machine, properties);
        useGregTechLogo(true);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue reductionSyncer = new IntSyncValue(machine::getPollutionReduction);
        syncManager.syncValue("pollutionReduction", reductionSyncer);

        return super.createContentSection(panel, syncManager).child(
            GTGuiTextures.INFORMATION_SYMBOL.asWidget()
                .topRel(0)
                .leftRel(0)
                .size(7, 18)
                .tooltipAutoUpdate(true)
                .tooltipDynamic(
                    t -> t.addLine(
                        StatCollector.translateToLocalFormatted(
                            "gtpp.gui.atmospheric_reconditioner.tooltip.reduction",
                            formatNumber(reductionSyncer.getIntValue())))));
    }

    @Override
    protected ProgressWidget createProgressBar(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActiveSyncer = new BooleanSyncValue(baseMetaTileEntity::isActive);
        syncManager.syncValue("isActive", isActiveSyncer);

        return new ProgressWidget()
            .value(new DoubleSyncValue(() -> isActiveSyncer.getBoolValue() ? (double) machine.getProgress() / 20 : 0))
            .texture(properties.progressBarMUI2, properties.progressBarWidthMUI2)
            .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
            .direction(properties.progressBarDirectionMUI2)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomRightCornerFlow(panel, syncManager).child(
            new CycleButtonWidget().stateCount(2)
                .value(new BooleanSyncValue(machine::isSaveRotor, machine::setSaveRotor).allowC2S())
                .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                .tooltip(
                    0,
                    t -> t.addLine(
                        StatCollector.translateToLocalFormatted(
                            "gtpp.chat.atmospheric_reconditioner.efficiency",
                            StatCollector.translateToLocal("gtpp.chat.atmospheric_reconditioner.efficiency.low"))))
                .tooltip(
                    1,
                    t -> t.addLine(
                        StatCollector.translateToLocalFormatted(
                            "gtpp.chat.atmospheric_reconditioner.efficiency",
                            StatCollector.translateToLocal("gtpp.chat.atmospheric_reconditioner.efficiency.high")))));
    }

    @Override
    protected ItemSlot createSpecialSlot() {
        return super.createSpecialSlot().tooltip(
            t -> t.clearText()
                .addLine(StatCollector.translateToLocal("gtpp.gui.atmospheric_reconditioner.slot.conveyor"))
                .addLine(StatCollector.translateToLocal("gtpp.gui.atmospheric_reconditioner.slot.conveyor.1")));
    }
}
