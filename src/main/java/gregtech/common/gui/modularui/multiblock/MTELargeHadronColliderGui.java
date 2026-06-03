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
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.LHCModule;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTELargeHadronCollider;
import gtnhlanth.common.beamline.Particle;

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
                dub -> multiblock.playerTargetBeamEnergyeV = dub).allowC2S());
        syncManager.syncValue(
            "playerTargetAccelerationCycles",
            new IntSyncValue(
                () -> multiblock.playerTargetAccelerationCycles,
                i -> multiblock.playerTargetAccelerationCycles = i).allowC2S());
        syncManager.syncValue(
            "calcInputBeamEnergyeV",
            new DoubleSyncValue(() -> multiblock.calcInputBeamEnergyeV, d -> multiblock.calcInputBeamEnergyeV = d)
                .allowC2S());
        syncManager.syncValue(
            "calcInputBeamRate",
            new IntSyncValue(() -> multiblock.calcInputBeamRate, i -> multiblock.calcInputBeamRate = i).allowC2S());
        syncManager.syncValue(
            "calcTargetBeamEnergyeV",
            new DoubleSyncValue(() -> multiblock.calcTargetBeamEnergyeV, d -> multiblock.calcTargetBeamEnergyeV = d)
                .allowC2S());
        syncManager.syncValue(
            "calcNumCycles",
            new IntSyncValue(() -> multiblock.calcNumCycles, i -> multiblock.calcNumCycles = i).allowC2S());
        syncManager.syncValue(
            "probTableCollisionEnergyeV",
            new DoubleSyncValue(
                () -> multiblock.probTableCollisionEnergyeV,
                d -> multiblock.probTableCollisionEnergyeV = d).allowC2S());
        syncManager.syncValue("cachedOutputBeamEnergy", new DoubleSyncValue(multiblock::getCachedBeamEnergy));
        syncManager.syncValue("cachedOutputBeamRate", new IntSyncValue(multiblock::getCachedBeamRate));
        syncManager.syncValue(
            "machineMode",
            new IntSyncValue(() -> multiblock.machineMode, i -> multiblock.machineMode = i).allowC2S());
        syncManager.syncValue(
            "accelerationCycleCounter",
            new IntSyncValue(() -> multiblock.accelerationCycleCounter, i -> multiblock.accelerationCycleCounter = i));
        syncManager.syncValue("EUt", new LongSyncValue(() -> multiblock.lEUt));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(panel, syncManager).child(createProbTableButton(syncManager, panel))
            .child(createCalculatorButton(syncManager, panel));
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    static DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        return new DecimalFormat("0.00E0", dfs);
    }

    protected static DecimalFormat standardFormat = createDecimalFormat();

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>(
        ImmutableMap.<Long, String>builder()
            .put(1_000L, "k")
            .put(1_000_000L, "M")
            .put(1_000_000_000L, "G")
            .put(1_000_000_000_000L, "T")
            .put(1_000_000_000_000_000L, "P")
            .put(1_000_000_000_000_000_000L, "E")
            .build());

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

    private String formatBeamEnergyText(DoubleSyncValue cachedOutputBeamEnergy,
        DoubleSyncValue playerTargetBeamEnergyeV) {
        // *1000 because cached is in keV, but formatting expects eV
        double dCachedOutputBeamEnergy = cachedOutputBeamEnergy.getDoubleValue() * 1000;
        double dPlayerTargetBeamEnergy = playerTargetBeamEnergyeV.getDoubleValue();

        EnumChatFormatting colour = EnumChatFormatting.RED;

        if (dCachedOutputBeamEnergy > dPlayerTargetBeamEnergy) {
            colour = EnumChatFormatting.GREEN;
        }

        return EnumChatFormatting.WHITE + StatCollector.translateToLocalFormatted(
            "GT5U.gui.text.LHC.beamenergykeV",
            colour + format((long) dCachedOutputBeamEnergy));

    }

    private String formatBeamRateText(IntSyncValue cachedOutputBeamRate) {
        return EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.gui.text.LHC.beamrate")
            + ": "
            + EnumChatFormatting.YELLOW
            + cachedOutputBeamRate.getIntValue();

    }

    private String formatPowerCostTextKey(DoubleSyncValue cachedOutputBeamEnergy, LongSyncValue EUt) {

        String colourText = EnumChatFormatting.GOLD + "0";

        if (cachedOutputBeamEnergy.getDoubleValue() > 0) {
            colourText = EnumChatFormatting.GOLD + standardFormat.format(-EUt.getLongValue());
        }

        return EnumChatFormatting.WHITE
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.LHC.powercost", colourText)
            + EnumChatFormatting.WHITE;
    }

    private String formatAccelCycleTextKey(IntSyncValue accelerationCycleCounter) {
        return EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.gui.text.LHC.completedcycles")
            + ": "
            + EnumChatFormatting.GRAY
            + accelerationCycleCounter.getIntValue();
    }

    private String formatStatusTextKey(DoubleSyncValue cachedOutputBeamEnergy, IntSyncValue machineMode) {
        String colourText = EnumChatFormatting.GRAY + StatCollector.translateToLocal("GT5U.gui.text.LHC.off");

        if (cachedOutputBeamEnergy.getDoubleValue() > 0) {
            colourText = EnumChatFormatting.AQUA
                + StatCollector.translateToLocal(getMachineModeText(machineMode.getIntValue()));
        }

        return EnumChatFormatting.WHITE
            + StatCollector.translateToLocalFormatted("GT5U.gui.text.LHC.status", colourText);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> outputWidget = super.createTerminalTextWidget(syncManager, parent);

        DoubleSyncValue playerTargetBeamEnergyeV = syncManager
            .findSyncHandler("playerTargetBeamEnergyeV", DoubleSyncValue.class);
        DoubleSyncValue cachedOutputBeamEnergy = syncManager
            .findSyncHandler("cachedOutputBeamEnergy", DoubleSyncValue.class);
        IntSyncValue cachedOutputBeamRate = syncManager.findSyncHandler("cachedOutputBeamRate", IntSyncValue.class);
        IntSyncValue machineMode = syncManager.findSyncHandler("machineMode", IntSyncValue.class);
        IntSyncValue accelerationCycleCounter = syncManager
            .findSyncHandler("accelerationCycleCounter", IntSyncValue.class);
        LongSyncValue EUt = syncManager.findSyncHandler("EUt", LongSyncValue.class);

        IKey beamEnergyTextKey = IKey
            .dynamic(() -> formatBeamEnergyText(cachedOutputBeamEnergy, playerTargetBeamEnergyeV));
        IKey beamRateTextKey = IKey.dynamic(() -> formatBeamRateText(cachedOutputBeamRate));
        IKey powerCostTextKey = IKey.dynamic(() -> formatPowerCostTextKey(cachedOutputBeamEnergy, EUt));
        IKey accelCycleTextKey = IKey.dynamic(() -> formatAccelCycleTextKey(accelerationCycleCounter));
        IKey statusTextKey = IKey.dynamic(() -> formatStatusTextKey(cachedOutputBeamEnergy, machineMode));

        outputWidget.child(
            new TextWidget<>(beamEnergyTextKey).marginBottom(9)
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT)
                .setEnabledIf(w -> multiblock.mMachine))
            .child(
                new TextWidget<>(beamRateTextKey).marginBottom(9)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT)
                    .setEnabledIf(w -> multiblock.mMachine))
            .child(
                new TextWidget<>(powerCostTextKey).marginBottom(9)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT)
                    .setEnabledIf(w -> multiblock.mMachine))
            .child(
                new TextWidget<>(accelCycleTextKey).marginBottom(9)
                    .widgetTheme(GTWidgetThemes.DISPLAY_TEXT)
                    .setEnabledIf(w -> multiblock.mMachine))
            .child(
                new TextWidget<>(statusTextKey).widgetTheme(GTWidgetThemes.DISPLAY_TEXT)
                    .setEnabledIf(w -> multiblock.mMachine));

        return outputWidget;
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.syncedPanel(
            "statsPanel",
            true,
            (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .topRel(0)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/fluid_regulation_panel"))
            .onMousePressed(d -> {
                if (!statsPanel.isPanelOpen()) {
                    statsPanel.openPanel();
                } else {
                    statsPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.text.LHC.setparams")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createCalculatorButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler calcPanel = syncManager.syncedPanel(
            "calculatorPanel",
            true,
            (p_syncManager, syncHandler) -> openCalculatorPanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/lhc_calculator"))
            .onMousePressed(d -> {
                if (!calcPanel.isPanelOpen()) {
                    calcPanel.openPanel();
                } else {
                    calcPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.calculator")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected IWidget createProbTableButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler probTablePanel = syncManager.syncedPanel(
            "probTablePanel",
            true,
            (p_syncManager, syncHandler) -> openProbTablePanel(p_syncManager, parent, syncManager));
        return new ButtonWidget<>().size(18, 18)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/lhc_probtable"))
            .onMousePressed(d -> {
                if (!probTablePanel.isPanelOpen()) {
                    probTablePanel.openPanel();
                } else {
                    probTablePanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.probtable")))
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
            .coverChildren()
            .padding(6)
            .widgetTheme("backgroundPopup")
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(6)
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> StatCollector.translateToLocalFormatted("GT5U.gui.text.LHC.targetbeamenergyeV")))
                                    .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .numbersLong(() -> 1L, () -> Long.MAX_VALUE)
                            .size(120, 14)
                            .marginRight(2)
                            .value(playerTargetBeamEnergyeVSync)
                            .defaultNumber(1_000_000_000))
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> StatCollector
                                    .translateToLocalFormatted("GT5U.gui.text.LHC.maxaccelerationcycles")))
                                        .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .formatAsInteger(true)
                            .size(40, 14)
                            .marginRight(2)
                            .value(playerTargetAccelerationCyclesSync)
                            .defaultNumber(10)));
    }

    private ModularPanel openCalculatorPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {

        DoubleSyncValue calcInputBeamEnergyeVSync = (DoubleSyncValue) syncManager
            .getSyncHandlerFromMapKey("calcInputBeamEnergyeV:0");
        IntSyncValue calcInputBeamRateSync = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("calcInputBeamRate:0");
        DoubleSyncValue calcTargetBeamEnergyeVSync = (DoubleSyncValue) syncManager
            .getSyncHandlerFromMapKey("calcTargetBeamEnergyeV:0");
        IntSyncValue calcNumCyclesSync = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("calcNumCycles:0");

        IKey resultKey = IKey.dynamic(
            () -> formatCalcResult(
                calcInputBeamEnergyeVSync != null ? calcInputBeamEnergyeVSync.getDoubleValue()
                    : multiblock.calcInputBeamEnergyeV,
                calcInputBeamRateSync != null ? calcInputBeamRateSync.getIntValue() : multiblock.calcInputBeamRate,
                calcTargetBeamEnergyeVSync != null ? calcTargetBeamEnergyeVSync.getDoubleValue()
                    : multiblock.calcTargetBeamEnergyeV,
                calcNumCyclesSync != null ? calcNumCyclesSync.getIntValue() : multiblock.calcNumCycles));

        IKey resultHeaderKey = IKey.lang("GT5U.gui.text.LHC.calc.resultheader");

        return new ModularPanel("calculatorPanel").relative(parent)
            .rightRel(1)
            .topRel(0)
            .coverChildren()
            .padding(6)
            .widgetTheme("backgroundPopup")
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(4)
                    .child(new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.calc.title")).textAlign(Alignment.CENTER))

                    .child(
                        new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.calc.inputbeamenergyeV"))
                            .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .setNumbersLong(() -> 0L, () -> Long.MAX_VALUE)
                            .size(120, 14)
                            .value(calcInputBeamEnergyeVSync)
                            .setDefaultNumber(0))

                    .child(
                        new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.calc.inputbeamrate")).textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .setFormatAsInteger(true)
                            .size(60, 14)
                            .value(calcInputBeamRateSync)
                            .setDefaultNumber(1))

                    .child(
                        new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.targetbeamenergyeV")).textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .setNumbersLong(() -> 1L, () -> Long.MAX_VALUE)
                            .size(120, 14)
                            .value(calcTargetBeamEnergyeVSync)
                            .setDefaultNumber(1_000_000_000))

                    .child(
                        new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.maxaccelerationcycles"))
                            .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .setFormatAsInteger(true)
                            .size(40, 14)
                            .value(calcNumCyclesSync)
                            .setDefaultNumber(1))

                    .child(new TextWidget<>(resultHeaderKey).textAlign(Alignment.CENTER))
                    .child(new TextWidget<>(resultKey).textAlign(Alignment.CENTER)));
    }

    private String formatCalcResult(double inputEnergyeV, int inputRate, double targetEnergyeV, int numCycles) {
        double[] result = MTELargeHadronCollider.simulateAccelerator(
            inputEnergyeV / 1000.0, // eV -> keV
            inputRate,
            targetEnergyeV / 1000.0,
            numCycles,
            multiblock.mMaxProgresstime);
        double finalBeamEnergyKeV = result[0];
        int finalRate = (int) result[1];
        long finalEUt = (long) result[2];

        long collisionEnergyeV = (long) (finalBeamEnergyKeV * 2 * 1000); // x2 for collision, and conversion keV->eV

        return EnumChatFormatting.GREEN + format(collisionEnergyeV)
            + "eV"
            + EnumChatFormatting.RESET
            + " | "
            + EnumChatFormatting.YELLOW
            + finalRate
            + EnumChatFormatting.RESET
            + " | "
            + EnumChatFormatting.GOLD
            + format(finalEUt)
            + "EU/t";
    }

    private ModularPanel openProbTablePanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {

        DoubleSyncValue probTableCollisionEnergyeVSync = (DoubleSyncValue) syncManager
            .getSyncHandlerFromMapKey("probTableCollisionEnergyeV:0");

        // one row per particle
        ListWidget<IWidget, ?> tableBody = new ListWidget<>();

        final LHCModule[] columns = new LHCModule[] { LHCModule.EM, LHCModule.Weak, LHCModule.Strong, LHCModule.Grav };

        for (final Particle p : Particle.VALUES) {
            Flow row = Flow.row()
                .coverChildren();

            row.child(
                new ButtonWidget<>().size(18, 18)
                    .overlay(p.getTexture())
                    .tooltipBuilder(t -> t.addLine(IKey.str(p.getLocalisedName())))
                    .tooltipShowUpTimer(TOOLTIP_DELAY));

            // one column per lhc module
            for (final LHCModule module : columns) {
                row.child(
                    new TextWidget<>(
                        IKey.dynamic(
                            () -> formatProbability(
                                p,
                                module,
                                probTableCollisionEnergyeVSync != null ? probTableCollisionEnergyeVSync.getDoubleValue()
                                    : multiblock.probTableCollisionEnergyeV))).width(40)
                                        .height(18)
                                        .textAlign(Alignment.CENTER));
            }

            tableBody.child(row);
        }

        // headers
        Flow headerRow = Flow.row()
            .coverChildren()
            .child(
                new TextWidget<>(IKey.str("")).width(18)
                    .height(14)
                    .textAlign(Alignment.CENTER))
            .child(
                new TextWidget<>(IKey.str("E")).width(40)
                    .height(14)
                    .textAlign(Alignment.CENTER))
            .child(
                new TextWidget<>(IKey.str("W")).width(40)
                    .height(14)
                    .textAlign(Alignment.CENTER))
            .child(
                new TextWidget<>(IKey.str("S")).width(40)
                    .height(14)
                    .textAlign(Alignment.CENTER))
            .child(
                new TextWidget<>(IKey.str("G")).width(40)
                    .height(14)
                    .textAlign(Alignment.CENTER));

        return new ModularPanel("probTablePanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .coverChildren()
            .padding(6)
            .widgetTheme("backgroundPopup")
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(4)
                    .child(new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.probtable.title")).textAlign(Alignment.CENTER))

                    .child(
                        new TextWidget<>(IKey.lang("GT5U.gui.text.LHC.probtable.collisionenergyeV"))
                            .textAlign(Alignment.CENTER))
                    .child(
                        new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                            .setNumbersLong(() -> 0L, () -> Long.MAX_VALUE)
                            .size(120, 14)
                            .value(probTableCollisionEnergyeVSync)
                            .setDefaultNumber(0))

                    .child(headerRow)
                    .child(tableBody.size(196, 200)));
    }

    private String formatProbability(Particle particle, LHCModule module, double collisionEnergyeV) {
        if (!module.acceptedParticles.contains(particle)) {
            return "--";
        }
        double collisionEnergyKeV = collisionEnergyeV / 1000.0;
        double prob = MTELargeHadronCollider.particleProbability(particle, module, collisionEnergyKeV);
        return String.format(Locale.US, "%.2f%%", prob * 100.0);
    }
}
