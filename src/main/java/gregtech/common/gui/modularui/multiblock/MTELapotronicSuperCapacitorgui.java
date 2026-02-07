package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BigIntSyncValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import kekztech.util.Util;

public class MTELapotronicSuperCapacitorgui extends MTEMultiBlockBaseGui<MTELapotronicSuperCapacitor> {

    public MTELapotronicSuperCapacitorgui(MTELapotronicSuperCapacitor multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        // weary amoutn of syncers
        LongSyncValue avgOut = syncManager.findSyncHandler("avgEuOut", LongSyncValue.class);
        LongSyncValue avgIn = syncManager.findSyncHandler("avgin", LongSyncValue.class);
        BigIntSyncValue capacity = syncManager.findSyncHandler("capacity", BigIntSyncValue.class);
        BigIntSyncValue storedEu = syncManager.findSyncHandler("stored", BigIntSyncValue.class);
        BigIntSyncValue wireless = syncManager.findSyncHandler("wireless value", BigIntSyncValue.class);
        LongSyncValue loss = syncManager.findSyncHandler("loss", LongSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(IKey.dynamic(() -> {
            String cap = EnumChatFormatting.BLUE + formatNumber(capacity.getValue());
            return EnumChatFormatting.WHITE + StatCollector
                .translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.total_capacity", cap);
        })
            .asWidget())

            .child(IKey.dynamic(() -> {
                String percent = EnumChatFormatting.BLUE
                    + Util.toPercentageFrom(storedEu.getValue(), capacity.getValue());
                return EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.used_capacity", percent);
            })
                .asWidget())
            .child(IKey.dynamic(() -> {
                String storedString = EnumChatFormatting.GREEN + formatNumber(storedEu.getValue());
                return EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.stored", storedString);
            })
                .asWidget())

            .child(IKey.dynamic(() -> {
                String avgString = EnumChatFormatting.GREEN + formatNumber(avgIn.getValue()).toString()
                    + EnumChatFormatting.WHITE;
                return EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_in", avgString);
            })
                .asWidget())
            .child(IKey.dynamic(() -> {
                String euOut = EnumChatFormatting.RED + formatNumber(avgOut.getValue()) + EnumChatFormatting.WHITE;
                return EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_out", euOut);
            })
                .asWidget())
            .child(IKey.dynamic(() -> {
                String TTF = EnumChatFormatting.AQUA + getTimeTo(
                    avgIn.getValue(),
                    avgOut.getValue(),
                    loss.getValue(),
                    capacity.getValue()
                        .doubleValue(),
                    storedEu.getValue()
                        .doubleValue());
                return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(TTF);
            })
                .asWidget())
            .child(IKey.dynamic(() -> {
                String lost = EnumChatFormatting.RED + formatNumber(loss.getValue()).toString();
                return EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.passive_loss", lost);
            })
                .asWidget())
            .child(IKey.dynamic(() -> {
                String wirelessFormat = EnumChatFormatting.LIGHT_PURPLE + formatNumber(wireless.getValue());
                return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                    "kekztech.infodata.lapotronic_super_capacitor.wireless_eu",
                    wirelessFormat);
            })
                .asWidget()
                .setEnabledIf((w) -> multiblock.isWireless_mode()));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue showWarning = syncManager.findSyncHandler("warning", BooleanSyncValue.class);
        BooleanSyncValue canRebalance = syncManager.findSyncHandler("canRebalance", BooleanSyncValue.class);
        BooleanSyncValue rebalanced = syncManager.findSyncHandler("rebalanced", BooleanSyncValue.class);
        BooleanSyncValue wireless = syncManager.findSyncHandler("wirelessMode", BooleanSyncValue.class);
        IPanelHandler warningPanel = syncManager.syncedPanel("warning panel", true,((a, b) -> warningPanel()));

        return super.createLeftPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
                if (multiblock.isWireless_mode()) {
                    return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_ON;
                } else {
                    return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_OFF;
                }
            }))
                .tooltip(
                    new RichTooltip().add(StatCollector.translateToLocal("gui.kekztech_lapotronicenergyunit.wireless")))
                .onMousePressed((a) -> {
                    if (showWarning.getBoolValue() && !wireless.getBoolValue()) {
                        multiblock.setWireless_mode(false);
                        warningPanel.openPanel();
                        showWarning.setValue(false);
                        return true;
                    }

                    if (!wireless.getValue() && !rebalanced.getValue()) {
                        wireless.setBoolValue(true);
                        canRebalance.setBoolValue(true);
                        showWarning.setValue(false);
                        return true;
                    }
                    if (!showWarning.getValue() && !wireless.getValue()) {
                        wireless.setBoolValue(true);
                        return true;
                    }
                    if (wireless.getValue()) {
                        wireless.setBoolValue(false);
                        return true;
                    }

                    return false;
                })

            )
            .child(
                new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_WIRELESS_REBALANCE)
                    .tooltip(
                        new RichTooltip().add(
                            StatCollector.translateToLocal("gui.kekztech_lapotronicenergyunit.wireless_rebalance")))
                    .onMousePressed((a) -> {
                        multiblock.setCounter(multiblock.rebalance());
                        canRebalance.setBoolValue(false);
                        rebalanced.setBoolValue(true);
                        return true;
                    })
                    .setEnabledIf((w) -> canRebalance.getBoolValue()));
    }

    private ModularPanel warningPanel() {

        return ModularPanel.defaultPanel("warning")
            .coverChildren()
            .padding(4)
            .paddingBottom(8)
            .child(
                Column.column()
                    .height(100)
                    .width(90)
                    .child(
                        ButtonWidget.panelCloseButton()
                            .top(0)
                            .right(0))
                    .child(
                        IKey.str(
                            EnumChatFormatting.RED + StatCollector
                                .translateToLocalFormatted("gui.kekztech_lapotronicenergyunit.warning.header"))
                            .asWidget()
                            .marginBottom(4))
                    .child(
                        IKey.str(
                            StatCollector.translateToLocalFormatted("gui.kekztech_lapotronicenergyunit.warning.text"))
                            .asWidget()));
    }

    public String getTimeTo(double avgIn, double avgOut, double passLoss, double cap, double sto) {
        if (avgIn >= avgOut + passLoss) {
            // Calculate time to full if charging
            if (avgIn - passLoss > 0) {
                double timeToFull = (cap - sto) / (avgIn - (passLoss + avgOut)) / 20;
                return EnumChatFormatting.WHITE + translateToLocalFormatted(
                    "kekztech.infodata.lapotronic_super_capacitor.time_to.full",
                    EnumChatFormatting.AQUA + formatTime(timeToFull, true));
            }
            return translateToLocal("kekztech.infodata.lapotronic_super_capacitor.time_to.sth");
        } else {
            // Calculate time to empty if discharging
            double timeToEmpty = sto / ((avgOut + passLoss) - avgIn) / 20;
            return EnumChatFormatting.WHITE + translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.time_to.empty",
                EnumChatFormatting.AQUA + formatTime(timeToEmpty, false));
        }
    }

    private String formatTime(double time, boolean fill) {
        if (time < 1) {
            return "Completely " + (fill ? "full" : "empty");
        } else if (time < 60) {
            return String.format("%.2f seconds", time);
        } else if (time < 3600) {
            return String.format("%.2f minutes", time / 60);
        } else if (time < 86400) {
            return String.format("%.2f hours", time / 3600);
        } else if (time < 31536000) {
            return String.format("%.2f days", time / 86400);
        } else {
            double y = time / 31536000;
            return y < 9_000 ? String.format("%.2f years", y) : "Over 9000 years";
        }
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {

        IPanelHandler EnergyPanel = syncManager.syncedPanel("energy panel", true,((a, b) -> createEnergyPopup(syncManager)));

        return super.createButtonColumn(panel, syncManager).child(
            new ButtonWidget<>().setEnabledIf((w) -> Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
                .onMousePressed((widget) -> {
                    if (EnergyPanel.isPanelOpen()) {
                        EnergyPanel.closePanel();
                    } else {
                        EnergyPanel.openPanel();
                    }
                    return true;
                })
                .overlay(GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        BooleanSyncValue isActive = new BooleanSyncValue(
            () -> multiblock.getBaseMetaTileEntity()
                .isActive());
        BigIntSyncValue capacity = new BigIntSyncValue(multiblock::getEnergyCapacity, multiblock::setCapacity);
        BigIntSyncValue stored = new BigIntSyncValue(multiblock::getStored, multiblock::setStored);
        LongSyncValue passiveDischargeSync = new LongSyncValue(multiblock::getPassiveDischargeAmount);
        LongSyncValue avgEuIn = new LongSyncValue(
            () -> multiblock.getEnergyInputValues()
                .avgLong());
        LongSyncValue avgEuOut = new LongSyncValue(
            () -> multiblock.getEnergyOutputValues()
                .avgLong());
        BooleanSyncValue wirelessMode = new BooleanSyncValue(multiblock::isWireless_mode, multiblock::setWireless_mode);
        BooleanSyncValue warningSync = new BooleanSyncValue(multiblock::getShowWarning, multiblock::setShowWarning);
        BooleanSyncValue canRebalance = new BooleanSyncValue(multiblock::getCanRebalance, multiblock::setCanRebalance);
        BooleanSyncValue hasRebalanced = new BooleanSyncValue(
            multiblock::getHasRebalanced,
            multiblock::setHasRebalanced);
        BigIntSyncValue wirelessValueSync = new BigIntSyncValue(
            multiblock::getWirelessValue,
            multiblock::setWirelessValue);
        syncManager.syncValue("rebalanced", hasRebalanced);
        syncManager.syncValue("loss", passiveDischargeSync);
        syncManager.syncValue("active", isActive);
        syncManager.syncValue("capacity", capacity);
        syncManager.syncValue("stored", stored);
        syncManager.syncValue("avgin", avgEuIn);
        syncManager.syncValue("avgEuOut", avgEuOut);
        syncManager.syncValue("wirelessMode", wirelessMode);
        syncManager.syncValue("warning", warningSync);
        syncManager.syncValue("canRebalance", canRebalance);
        syncManager.syncValue("wireless value", wirelessValueSync);

    }

    private ModularPanel createEnergyPopup(PanelSyncManager syncManager) {

        BigIntSyncValue stored = syncManager.findSyncHandler("stored", BigIntSyncValue.class);
        return new ModularPanel("energy").background(GTGuiTextures.BACKGROUND_STANDARD)
            .coverChildren()
            .child(
                Flow.column()
                    .coverChildren()
                    .child(
                        IKey.str(StatCollector.translateToLocal("GT5U.multiblock.energy"))
                            .asWidget()
                            .margin(4, 4, 7, 4))
                    .child(
                        new TextFieldWidget().value(stored)
                            .setNumbersLong(() -> 0L, () -> Long.MAX_VALUE)
                            .marginLeft(4)
                            .marginRight(4)
                            .marginBottom(4)
                            .width(80)));

    }
}
