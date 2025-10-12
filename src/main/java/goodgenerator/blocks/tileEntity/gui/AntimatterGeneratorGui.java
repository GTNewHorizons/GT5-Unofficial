package goodgenerator.blocks.tileEntity.gui;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import goodgenerator.blocks.tileEntity.AntimatterGenerator;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;

public class AntimatterGeneratorGui extends MTEMultiBlockBaseGui {

    private final AntimatterGenerator base;

    public AntimatterGeneratorGui(AntimatterGenerator base) {
        super(base);
        this.base = base;
    }

    protected static DecimalFormat standardFormat;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        standardFormat = new DecimalFormat("0.00E0", dfs);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("canUseWireless", new BooleanSyncValue(base::canUseWireless));
        syncManager.syncValue("wirelessMode", new BooleanSyncValue(base::getWirelessMode, base::setWirelessEnabled));
        syncManager.syncValue("energyProduced", new LongSyncValue(base::getEnergyProduced));
        syncManager.syncValue("efficiencyCur", new DoubleSyncValue(base::getEfficiency));
        syncManager.syncValue("efficiencyAvg", new DoubleSyncValue(base::getAvgEfficiency));
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .padding(4)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild()));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue energyProducedSync = (LongSyncValue) syncManager.getSyncHandler("energyProduced:0");
        DoubleSyncValue curEfficiencySync = (DoubleSyncValue) syncManager.getSyncHandler("efficiencyCur:0");
        DoubleSyncValue avgEfficiencySync = (DoubleSyncValue) syncManager.getSyncHandler("efficiencyAvg:0");
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.0")
                            + ": "
                            + EnumChatFormatting.BLUE
                            + standardFormat.format(energyProducedSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU")))
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.1")
                            + ": "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(curEfficiencySync.getDoubleValue() * 100))
                            + EnumChatFormatting.WHITE
                            + " %")))
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.1")
                            + ": ⟨ "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(avgEfficiencySync.getDoubleValue() * 100))
                            + EnumChatFormatting.WHITE
                            + " % ⟩₁₀")));
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue canUseWireless = (BooleanSyncValue) syncManager.getSyncHandler("canUseWireless:0");
        BooleanSyncValue wirelessMode = (BooleanSyncValue) syncManager.getSyncHandler("wirelessMode:0");
        return super.createPanelGap(parent, syncManager).child(
            new ButtonWidget<>().size(18, 18)
                .playClickSound(true)
                .overlay(new DynamicDrawable(() -> {

                    if (canUseWireless.getBoolValue()) {
                        if (wirelessMode.getBoolValue()) {
                            return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_ON;
                        }
                        return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_OFF;
                    } else {
                        return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_DISABLED;
                    }

                }))
                .onMousePressed(d -> {

                    if (canUseWireless.getBoolValue()) {
                        wirelessMode.setBoolValue(!wirelessMode.getBoolValue());
                    }

                    return false;
                })
                .tooltipBuilder(
                    t -> t.addLine(StatCollector.translateToLocal("gui.kekztech_lapotronicenergyunit.wireless")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

    }
}
