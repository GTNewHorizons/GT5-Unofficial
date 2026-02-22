package gregtech.common.gui.modularui.multiblock;

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
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import goodgenerator.blocks.tileEntity.AntimatterGenerator;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class AntimatterGeneratorGui extends MTEMultiBlockBaseGui<AntimatterGenerator> {

    public AntimatterGeneratorGui(AntimatterGenerator base) {
        super(base);
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
        syncManager.syncValue("canUseWireless", new BooleanSyncValue(multiblock::canUseWireless));
        syncManager.syncValue(
            "wirelessMode",
            new BooleanSyncValue(multiblock::getWirelessMode, multiblock::setWirelessEnabled));
        syncManager.syncValue("energyProduced", new LongSyncValue(multiblock::getEnergyProduced));
        syncManager.syncValue("efficiencyCur", new DoubleSyncValue(multiblock::getEfficiency));
        syncManager.syncValue("efficiencyAvg", new DoubleSyncValue(multiblock::getAvgEfficiency));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue energyProducedSync = syncManager.findSyncHandler("energyProduced", LongSyncValue.class);
        DoubleSyncValue curEfficiencySync = syncManager.findSyncHandler("efficiencyCur", DoubleSyncValue.class);
        DoubleSyncValue avgEfficiencySync = syncManager.findSyncHandler("efficiencyAvg", DoubleSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.0")
                            + ": "
                            + EnumChatFormatting.BLUE
                            + standardFormat.format(energyProducedSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.1")
                            + ": "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(curEfficiencySync.getDoubleValue() * 100))
                            + EnumChatFormatting.WHITE
                            + " %")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterGenerator.1")
                            + ": ⟨ "
                            + EnumChatFormatting.RED
                            + numberFormat.format(Math.ceil(avgEfficiencySync.getDoubleValue() * 100))
                            + EnumChatFormatting.WHITE
                            + " % ⟩₁₀")));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue canUseWireless = syncManager.findSyncHandler("canUseWireless", BooleanSyncValue.class);
        BooleanSyncValue wirelessMode = syncManager.findSyncHandler("wirelessMode", BooleanSyncValue.class);
        return super.createLeftPanelGapRow(parent, syncManager).child(
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
