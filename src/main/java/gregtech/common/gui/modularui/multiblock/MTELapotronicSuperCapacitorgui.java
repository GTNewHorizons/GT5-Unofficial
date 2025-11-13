package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.BigDecimalSyncValue;
import com.cleanroommc.modularui.value.sync.BigIntSyncValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import kekztech.util.Util;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import scala.tools.nsc.interpreter.Formatting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.Format;

public class MTELapotronicSuperCapacitorgui extends MTEMultiBlockBaseGui<MTELapotronicSuperCapacitor> {


    public MTELapotronicSuperCapacitorgui(MTELapotronicSuperCapacitor multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        return super.createTerminalTextWidget(syncManager, parent)
            .child(IKey.dynamic(
                    () -> {
                        BigIntSyncValue capacity = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("capacity:0");
                        String cap = EnumChatFormatting.BLUE + formatNumber(capacity.getValue());
                        return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
                            "kekztech.infodata.lapotronic_super_capacitor.total_capacity", cap);
                    })
                .asWidget())
            .child(IKey.dynamic(() ->
                {
                    BigIntSyncValue storedEu = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("stored:0");

                    String storedString = EnumChatFormatting.GREEN + formatNumber(storedEu.getValue());
                    return EnumChatFormatting.WHITE + StatCollector
                        .translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.stored", storedString);
                })
                .asWidget())
            .child(IKey.dynamic(() -> {
                    BigIntSyncValue storedEu = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("stored:0");
                    BigIntSyncValue capacity = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("capacity:0");
                    Float percent = getPercentage(storedEu.getValue(), capacity.getValue());
                    String cap = EnumChatFormatting.RED + percent.toString() + "%";
                return  EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.used_capacity", cap);
                })
                .asWidget())
            .child(IKey.dynamic(() -> {
                LongSyncValue loss =  (LongSyncValue) syncManager.getSyncHandlerFromMapKey("loss:0");
                String lost = EnumChatFormatting.RED + loss.getValue().toString();
                return EnumChatFormatting.WHITE + StatCollector.
                    translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.passive_loss", lost);
            })
                .asWidget())
            .child(IKey.dynamic(()-> {
                LongSyncValue avg = (LongSyncValue) syncManager.findSyncHandler("avgin");
                String avgString = EnumChatFormatting.GREEN+ avg.getValue().toString() + EnumChatFormatting.WHITE;
                 return EnumChatFormatting.WHITE  + StatCollector.translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_in", avgString);
                })
                .asWidget())
            .child(IKey.dynamic(() -> {
                LongSyncValue avg = (LongSyncValue) syncManager.findSyncHandler("avgEuOut");
                String euOut =  EnumChatFormatting.RED + avg.getValue().toString()  + EnumChatFormatting.WHITE ;
                return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted("kekztech.gui.lapotronic_super_capacitor.text.avg_eu_out", euOut);
            })
                .asWidget());
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue showWarning =  (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("warning:0");
        BooleanSyncValue canRebalance =  (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("canRebalance:0");
        IPanelHandler warningPanel = syncManager.panel("energy panel", (
                (a,b) -> warningPanel()),
            true );


        return super.createLeftPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>()
                .overlay(new DynamicDrawable(() -> {
                    if(multiblock.isWireless_mode()){
                        return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_ON;
                    }
                    else{
                        return GTGuiTextures.OVERLAY_BUTTON_WIRELESS_OFF;
                    }
                }))
                .onMousePressed((a) -> {
                    if(showWarning.getValue() && !multiblock.isWireless_mode()){
                        multiblock.setWireless_mode(true);
                            warningPanel.openPanel();
                            showWarning.setValue(false);
                        return true;
                    }
                    else if(!showWarning.getValue() && !multiblock.isWireless_mode()){
                        multiblock.setWireless_mode(true);
                        return true;
                    }
                    else if (multiblock.isWireless_mode()){
                        multiblock.setWireless_mode(false);
                        return true;
                    }
                    return false;
                })
            )
                .child(new ButtonWidget<>()
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_WIRELESS_REBALANCE)
                    .onMousePressed( (a) -> {
                        multiblock.setCounter(multiblock.rebalance());
                        canRebalance.setBoolValue(false);
                        return true;
                    }
                    ).setEnabledIf((a) -> canRebalance.getBoolValue())
                    );
    }

    private ModularPanel warningPanel(){

        return ModularPanel.defaultPanel("warning")
            .coverChildren()
            .padding(4)
            .paddingBottom(8)
            .child(Column.column()
                .height(100)
                .width(90)
                .child(ButtonWidget.panelCloseButton().top(0).right(0))
                .child(IKey
                    .str( EnumChatFormatting.RED +
                        StatCollector.translateToLocalFormatted("gui.kekztech_lapotronicenergyunit.warning.header")
                    ).asWidget().marginBottom(4))
                .child(IKey
                    .str(StatCollector
                        .translateToLocalFormatted("gui.kekztech_lapotronicenergyunit.warning.text"))
                    .asWidget()
                )
            );
    }



    private String formatNumber(BigInteger aNumber) {
        //lol random random number thats big (pulled from old ui)
        return aNumber.compareTo(BigInteger.valueOf(1_000_000_000L)) > 0 ? GTUtility.scientificFormat(aNumber)
            : GTUtility.formatNumbers(aNumber);

    }


    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {

        IPanelHandler EnergyPanel = syncManager.panel("energy panel", (
            (a,b) -> createEnergyPopup(syncManager)),
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


        BooleanSyncValue isActive = new BooleanSyncValue(() -> multiblock.getBaseMetaTileEntity().isActive());
        BigIntSyncValue capacity = new BigIntSyncValue( multiblock::getEnergyCapacity, multiblock::setCapacity);
        BigIntSyncValue stored = new BigIntSyncValue(multiblock::getStored, multiblock::setStored);
        BooleanSyncValue wirelessSync = new BooleanSyncValue(multiblock::isWireless_mode, multiblock::setWireless_mode);
        LongSyncValue passiveDischargeSync = new LongSyncValue(multiblock::getPassiveDischargeAmount);
        LongSyncValue avgEuIn = new LongSyncValue(() -> multiblock.getEnergyInputValues5m().avgLong());
        LongSyncValue avgEuOut = new LongSyncValue(multiblock::getPassiveDischargeAmount);
        BooleanSyncValue wirelessMode = new BooleanSyncValue(multiblock::isWireless_mode, multiblock::setWireless_mode);
        BooleanSyncValue warningSync = new BooleanSyncValue(multiblock::getShowWarning, multiblock::setShowWarning);
        BooleanSyncValue canRebalance = new BooleanSyncValue(multiblock::getCanRebalance, multiblock::setCanRebalance);
        syncManager.syncValue("loss", passiveDischargeSync);
        syncManager.syncValue("wireless", wirelessSync);
        syncManager.syncValue("active", isActive);
        syncManager.syncValue("capacity", capacity);
        syncManager.syncValue("stored", stored);
        syncManager.syncValue("avgin", avgEuIn);
        syncManager.syncValue("avgEuOut", avgEuOut);
        syncManager.syncValue("wirelessMode", wirelessMode);
        syncManager.syncValue("warning", warningSync);
        syncManager.syncValue("canRebalance", canRebalance);

    }

    public static Float getPercentage(BigInteger value, BigInteger maxValue) {
        if (BigInteger.ZERO.equals(maxValue)) {
            return 0f;
        }
        BigDecimal result = new BigDecimal(value)
            .setScale(6, RoundingMode.HALF_UP)
            .divide(new BigDecimal(maxValue), RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));

        return result.floatValue();
    }




    private ModularPanel createEnergyPopup(PanelSyncManager syncManager) {


        BigIntSyncValue stored = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("stored:0");
        BigIntSyncValue capacity = (BigIntSyncValue) syncManager.getSyncHandlerFromMapKey("capacity:0");
        return new ModularPanel("energy")
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .coverChildren()
            .child(Flow.column()
                .coverChildren()
                .child(
                    IKey.str(StatCollector.translateToLocal("GT5U.multiblock.energy")).asWidget()
                        .margin(4,4,7,4)
                )
                .child(new TextFieldWidget()
                    .value(stored)
                    .setNumbersLong(() -> 0L, () -> Long.MAX_VALUE)
                    .margin(4,0)
                    .marginBottom(4).width(80))
            );

    }
}
