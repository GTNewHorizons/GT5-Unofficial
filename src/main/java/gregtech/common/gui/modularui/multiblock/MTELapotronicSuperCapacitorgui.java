package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import kekztech.util.Util;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.text.Format;

public class MTELapotronicSuperCapacitorgui extends MTEMultiBlockBaseGui<MTELapotronicSuperCapacitor> {

    public MTELapotronicSuperCapacitorgui(MTELapotronicSuperCapacitor multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent)
            .child(IKey.str(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal(
                        "kekztech.infodata.lapotronic_super_capacitor.total_capacity") + " " +
                        EnumChatFormatting.RED + multiblock.getUsedPercentCache())
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            .child(IKey.str(StatCollector.translateToLocal("kekztech.gui.lapotronic_super_capacitor.text.stored"))
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText())).child(IKey.str("fungus")
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            .child(IKey.str(StatCollector.translateToLocal("kekztech.infodata.lapotronic_super_capacitor.used_capacity"))
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            .child(IKey.str(StatCollector.translateToLocal("kekztech.infodata.lapotronic_super_capacitor.passive_loss"))
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            .child(IKey.str(StatCollector.translateToLocal("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_in"))
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            .child(IKey.str(StatCollector.translateToLocal("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_out"))
                .asWidget()
                .setEnabledIf((a) -> multiblock.hasRunningText()))
            ;
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager)
            .child(new ToggleButton());
    }






    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {

        IPanelHandler EnergyPanel = syncManager.panel("energy panel", (
            (a,b) -> createEnergyPopup()),
            true );

        return super.createButtonColumn(panel, syncManager)
            .child(new ButtonWidget().onMousePressed(( widget) -> {
                if (EnergyPanel.isPanelOpen()) {
                    EnergyPanel.closePanel();
                }else{
                    EnergyPanel.openPanel();
                }
                return true;
            }).overlay(GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        BooleanSyncValue wirelessSync = new BooleanSyncValue(multiblock::isWireless_mode, multiblock::setWireless_mode);
        syncManager.syncValue("wireless", wirelessSync);
    }

    private ModularPanel createEnergyPopup(){

        return new ModularPanel("energy")
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .coverChildren()
            .height(158)
            .width(52)
            .child(Flow.column()
                .child(IKey.str(StatCollector.translateToLocal("GT5U.multiblock.energy")).asWidget()
                )
                .child(new TextFieldWidget())
            );

    }
}
