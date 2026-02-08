package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTELargeHadronCollider;

public class MTELargeHadronColliderGui extends MTEMultiBlockBaseGui<MTELargeHadronCollider> {

    public MTELargeHadronColliderGui(MTELargeHadronCollider multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_ACCELERATOR,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_COLLIDER);
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "playerTargetBeamEnergyeV",
            new DoubleSyncValue(
                () -> multiblock.playerTargetBeamEnergyeV,
                dub -> multiblock.playerTargetBeamEnergyeV = dub));
        syncManager.syncValue(
            "playerTargetAccelerationCycles",
            new IntSyncValue(
                () -> multiblock.playerTargetAccelerationCycles,
                i -> multiblock.playerTargetAccelerationCycles = i));
        syncManager.syncValue("cachedOutputBeamEnergy", new DoubleSyncValue(multiblock::getCachedBeamEnergy));
        syncManager.syncValue("cachedOutputBeamRate", new IntSyncValue(multiblock::getCachedBeamRate));
        syncManager
            .syncValue("machineMode", new IntSyncValue(() -> multiblock.machineMode, i -> multiblock.machineMode = i));
        syncManager.syncValue(
            "accelerationCycleCounter",
            new IntSyncValue(() -> multiblock.accelerationCycleCounter, i -> multiblock.accelerationCycleCounter = i));
        syncManager.syncValue("EUt", new LongSyncValue(() -> multiblock.lEUt));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    static DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        return new DecimalFormat("0.00E0", dfs);
    }

    protected static DecimalFormat standardFormat = createDecimalFormat();

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>(
        ImmutableMap.of(1_000L, "k", 1_000_000L, "M", 1_000_000_000L, "G", 1_000_000_000_000L, "T"));

    public static String format(long value) {
        // This formats so that we use the nearest metric prefix. If the 3-digit number before the prefix is under 10
        // AND has non-zero digit after the decimal point, then show it.
        // examples:
        // 10_000 -> 10k
        // 10_100 -> 10k
        // 9_900 -> 9.9k
        // 9_000 -> 9k
        // This format was chosen purely out of personal preference - decimal points look cumbersome on the GUI.

        // Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); // deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncatedx10 = value / (divideBy / 10); // the number part of the output times 10 - i.e. first few digits
                                                     // up to one decimal point, but drop the decimal for now
        boolean truncatedValueUnderTen = truncatedx10 < 100;
        boolean truncatedValueHasNonZeroDecimal = (truncatedx10 / 10d) != (truncatedx10 / 10);

        if (truncatedValueUnderTen && truncatedValueHasNonZeroDecimal) {
            return (truncatedx10 / 10d) + " " + suffix;
        }
        return (truncatedx10 / 10) + " " + suffix;
    }

    public String getMachineModeText(int machineMode) {
        return machineMode == 0 ? "GT5U.gui.text.LHC.acceleratormode" : "GT5U.gui.text.LHC.collidermode";
    }

    private String formatBeamEnergyText(DoubleSyncValue cachedOutputBeamEnergy, DoubleSyncValue playerTargetBeamEnergyeV){
        // *1000 because cached is in keV, but formatting expects eV
        double dCachedOutputBeamEnergy = cachedOutputBeamEnergy.getDoubleValue() * 1000;
        double dPlayerTargetBeamEnergy = playerTargetBeamEnergyeV.getDoubleValue();

        EnumChatFormatting colour = EnumChatFormatting.RED;

        if (dCachedOutputBeamEnergy > dPlayerTargetBeamEnergy){
            colour = EnumChatFormatting.GREEN;
        }

        return EnumChatFormatting.WHITE +
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.LHC.beamenergykeV",
                colour + format((long) dCachedOutputBeamEnergy)
            );

    }

    private String formatBeamRateText(IntSyncValue cachedOutputBeamRate) {
        return EnumChatFormatting.WHITE +
            StatCollector.translateToLocal("GT5U.gui.text.LHC.beamrate") +
            ": " + EnumChatFormatting.YELLOW +
            cachedOutputBeamRate.getIntValue();

    }

    private String formatPowerCostTextKey(DoubleSyncValue cachedOutputBeamEnergy, LongSyncValue EUt){

        String colourText = EnumChatFormatting.GOLD + "0";

        if (cachedOutputBeamEnergy.getDoubleValue() > 0){
            colourText = EnumChatFormatting.GOLD + standardFormat.format(EUt.getLongValue());
        }

        return EnumChatFormatting.WHITE +
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.LHC.powercost", colourText)
            + EnumChatFormatting.WHITE;
    }

    private String formatAccelCycleTextKey(IntSyncValue accelerationCycleCounter){
        return EnumChatFormatting.WHITE
            + StatCollector.translateToLocal("GT5U.gui.text.LHC.completedcycles")
            + ": "
            + EnumChatFormatting.GRAY
            + accelerationCycleCounter.getIntValue();
    }

    private String formatStatusTextKey(DoubleSyncValue cachedOutputBeamEnergy, IntSyncValue machineMode){
        String colourText = EnumChatFormatting.GRAY + StatCollector.translateToLocal("GT5U.gui.text.LHC.off");

        if (cachedOutputBeamEnergy.getDoubleValue() > 0) {
            colourText = EnumChatFormatting.AQUA
                + StatCollector.translateToLocal(getMachineModeText(machineMode.getIntValue()));
        }

        return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
            "GT5U.gui.text.LHC.status",colourText);
    }


    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        DoubleSyncValue playerTargetBeamEnergyeV = syncManager
            .findSyncHandler("playerTargetBeamEnergyeV", DoubleSyncValue.class);
        DoubleSyncValue cachedOutputBeamEnergy = syncManager
            .findSyncHandler("cachedOutputBeamEnergy", DoubleSyncValue.class);
        IntSyncValue cachedOutputBeamRate = syncManager.findSyncHandler("cachedOutputBeamRate", IntSyncValue.class);
        IntSyncValue machineMode = syncManager.findSyncHandler("machineMode", IntSyncValue.class);
        IntSyncValue accelerationCycleCounter = syncManager
            .findSyncHandler("accelerationCycleCounter", IntSyncValue.class);
        LongSyncValue EUt = syncManager.findSyncHandler("EUt", LongSyncValue.class);


        IKey beamEnergyTextKey = IKey.dynamic(() -> formatBeamEnergyText(cachedOutputBeamEnergy,playerTargetBeamEnergyeV));
        IKey beamRateTextKey = IKey.dynamic(() -> formatBeamRateText(cachedOutputBeamRate));
        IKey powerCostTextKey = IKey.dynamic(() -> formatPowerCostTextKey(cachedOutputBeamEnergy,EUt));
        IKey accelCycleTextKey = IKey.dynamic(() -> formatAccelCycleTextKey(accelerationCycleCounter));
        IKey statusTextKey = IKey.dynamic(() -> formatStatusTextKey(cachedOutputBeamEnergy,machineMode));


        return new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(new TextWidget<>(beamEnergyTextKey).marginBottom(9))
            .child(new TextWidget<>(beamRateTextKey).marginBottom(9))
            .child(new TextWidget<>(powerCostTextKey).marginBottom(9))
            .child(new TextWidget<>(accelCycleTextKey).marginBottom(9))
            .child(new TextWidget<>(statusTextKey));
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.panel(
            "statsPanel",
            (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager),
            true);
        return new ButtonWidget<>().size(18, 18)
            .topRel(0)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/cyclic"))
            .onMousePressed(d -> {
                if (!statsPanel.isPanelOpen()) {
                    statsPanel.openPanel();
                } else {
                    statsPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.machineinfo")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openInfoPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        DoubleSyncValue playerTargetBeamEnergyeVSync = (DoubleSyncValue) syncManager
            .getSyncHandlerFromMapKey("playerTargetBeamEnergyeV:0");
        IntSyncValue playerTargetAccelerationCyclesSync = (IntSyncValue) syncManager
            .getSyncHandlerFromMapKey("playerTargetAccelerationCycles:0");

        return new ModularPanel("statsPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(180, 110)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new Column().size(160, 60)
                            .paddingLeft(40)
                            .child(
                                new TextWidget<>(IKey.dynamic(() -> "Target Beam Energy (eV):")).size(160, 20)
                                    .alignment(Alignment.CENTER))
                            .child(
                                new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                                    .setNumbersLong(() -> 1L, () -> Long.MAX_VALUE)
                                    .width(120)
                                    .height(14)
                                    .marginRight(2)
                                    .value(playerTargetBeamEnergyeVSync)
                                    .setDefaultNumber(1_000_000_000))
                            .child(
                                new TextWidget<>(IKey.dynamic(() -> "Max Acceleration Cycles:")).size(160, 20)
                                    .alignment(Alignment.CENTER))
                            .child(
                                new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                                    .setFormatAsInteger(true)
                                    .width(40)
                                    .height(14)
                                    .marginRight(2)
                                    .value(playerTargetAccelerationCyclesSync)
                                    .setDefaultNumber(10))));
    }

}
