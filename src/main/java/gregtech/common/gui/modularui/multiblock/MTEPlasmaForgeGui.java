package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Supplier;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.FluidDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEPlasmaForge;
import org.apache.commons.lang3.tuple.Pair;

public class MTEPlasmaForgeGui extends MTEMultiBlockBaseGui<MTEPlasmaForge> {
    private PanelSyncManager mainSyncManager;
    private static final long[] FUEL_ENERGY_VALUES = { 14_514_983L, 66_768_460L, 269_326_451L, 1_073_007_393L,
        4_276_767_521L };

    private static final String[] CATALYST_SHORT = { "DTCC", "DTPC", "DTRC", "DTEC", "DTSC" };

    private static final int WIDTH = 260;
    private static final int HEIGHT = 280;
    private static final int PADDING = 8;

    public MTEPlasmaForgeGui(MTEPlasmaForge multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        this.mainSyncManager = syncManager;
        //spotless:off
        BooleanSyncValue convergenceSyncer = new BooleanSyncValue(multiblock::getConvergenceStatus, multiblock::setConvergenceStatus);
        IntSyncValue catalystTypeSyncer = new IntSyncValue(multiblock::getCatalystTypeForRecipesWithoutCatalyst, multiblock::setCatalystTypeForRecipesWithoutCatalyst);

        IntSyncValue modeSync = new IntSyncValue(() -> multiblock.calcMode, v -> multiblock.calcMode = v);
        LongSyncValue recipeEUtSync = new LongSyncValue(() -> multiblock.calcRecipeEUt, v -> multiblock.calcRecipeEUt = v);
        LongSyncValue recipeDurationSync = new LongSyncValue(() -> multiblock.calcRecipeDuration, v -> multiblock.calcRecipeDuration = v);
        IntSyncValue tierIndexSync = new IntSyncValue(() -> multiblock.calcTierIndex, v -> multiblock.calcTierIndex = v);
        LongSyncValue inputValueSync = new LongSyncValue(() -> multiblock.calcInputValue, v -> multiblock.calcInputValue = v);
        IntSyncValue calcCatalystTypeSync = new IntSyncValue(() -> multiblock.calcCatalystType, v -> multiblock.calcCatalystType = v);
        IntSyncValue numberFormatSync = new IntSyncValue(() -> multiblock.calcNumberFormat, v -> multiblock.calcNumberFormat = v);
        LongSyncValue calcEUtSync = new LongSyncValue(() -> multiblock.calcFinalEUt, v -> multiblock.calcFinalEUt = v);
        LongSyncValue calcAmpsSync = new LongSyncValue(() -> multiblock.calcAmps, v -> multiblock.calcAmps = v);
        IntSyncValue calcOCSync = new IntSyncValue(() -> multiblock.calcOverclocks, v -> multiblock.calcOverclocks = v);

        syncManager.syncValue("convergence", convergenceSyncer);
        syncManager.syncValue("catalystType", catalystTypeSyncer);

        syncManager.syncValue("calc_mode", modeSync);
        syncManager.syncValue("calc_recipeEUt", recipeEUtSync);
        syncManager.syncValue("calc_recipeDuration", recipeDurationSync);
        syncManager.syncValue("calc_tierIndex", tierIndexSync);
        syncManager.syncValue("calc_inputValue", inputValueSync);
        syncManager.syncValue("calc_catalystType", calcCatalystTypeSync);
        syncManager.syncValue("calc_numberFormat", numberFormatSync);

        syncManager.syncValue("calc_finalEUt", calcEUtSync);
        syncManager.syncValue("calc_amps", calcAmpsSync);
        syncManager.syncValue("calc_ocs", calcOCSync);
        //spotless:on
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createConvergenceButton(syncManager, panel))
            .child(createCalculatorButton(syncManager, panel));
    }

    protected IWidget createConvergenceButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler catalystSelectPanel = syncManager
            .panel("catalystPanel", (p_syncManager, syncHandler) -> openCatalystPanel(mainSyncManager, parent), true);

        BooleanSyncValue convergenceSyncer = syncManager.findSyncHandler("convergence", BooleanSyncValue.class);
        return new ButtonWidget<>().size(18)
            .marginBottom(2)
            .tooltip(
                t -> t.addLine(translateToLocal("GT5U.DTPF.convergencebutton"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.0"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.1")))
            .overlay(new DynamicDrawable(() -> {
                boolean convergenceActive = convergenceSyncer.getBoolValue();
                if (convergenceActive) {
                    return GTGuiTextures.TT_SAFE_VOID_ON;
                }
                return GTGuiTextures.TT_SAFE_VOID_OFF;
            }))
            .onMousePressed(mouseButton -> {
                if (mouseButton == 1) {
                    if (!catalystSelectPanel.isPanelOpen()) {
                        catalystSelectPanel.openPanel();
                    } else {
                        catalystSelectPanel.closePanel();
                    }
                } else if (mouseButton == 0) {
                    ItemStack controllerStack = multiblock.getControllerSlot();
                    if (controllerStack == null) return false;
                    if (!controllerStack.isItemEqual(ItemList.Transdimensional_Alignment_Matrix.get(1))) return false;

                    convergenceSyncer.setBoolValue(!convergenceSyncer.getBoolValue());
                }
                return true;
            });
    }

    protected IWidget createCalculatorButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler calculatorPanelHandler = syncManager
            .panel("calculatorPanel", (p_syncManager, syncHandler) -> openCalculatorPanel(mainSyncManager, parent), true);

        return new ButtonWidget<>().size(18)
            .marginBottom(2)
            .tooltip(
                t -> t.addLine(translateToLocal("GT5U.DTPF.calculatorbutton"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.calculatorbuttontooltip.0"))
                    .addLine(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.calculatorbuttontooltip.1")))
            .overlay(GTGuiTextures.INFORMATION_SYMBOL)
            .onMousePressed(mouseButton -> {
                if (!calculatorPanelHandler.isPanelOpen()) {
                    calculatorPanelHandler.openPanel();
                } else {
                    calculatorPanelHandler.closePanel();
                }
                return true;
            });
    }

    private ModularPanel openCatalystPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel returnPanel = new ModularPanel("catalystPanel").size(WIDTH, HEIGHT)
            .relative(parent)
            .leftRel(1)
            .topRel(0.9f);
        IntSyncValue catalystSyncer = syncManager.findSyncHandler("catalystType", IntSyncValue.class);
        Flow holdingColumn = Flow.column()
            .sizeRel(1)
            .paddingTop(4);
        holdingColumn.child(
            IKey.lang("GT5U.DTPF.catalysttier")
                .alignment(Alignment.Center)
                .asWidget()
                .marginBottom(2));
        holdingColumn.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, 5)
                .setTextAlignment(Alignment.CENTER)
                .setDefaultNumber(1)
                .value(catalystSyncer)
                .size(WIDTH - PADDING * 2, 18));

        returnPanel.child(holdingColumn);

        return returnPanel;
    }

    private ModularPanel openCalculatorPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel calcPanel = new ModularPanel("calculatorPanel").size(WIDTH, HEIGHT)
            .relative(parent)
            .leftRel(1)
            .topRel(0.5f);

        IntSyncValue modeSync = syncManager.findSyncHandler("calc_mode", IntSyncValue.class);
        LongSyncValue recipeEUtSync = syncManager.findSyncHandler("calc_recipeEUt", LongSyncValue.class);
        LongSyncValue durationSync = syncManager.findSyncHandler("calc_recipeDuration", LongSyncValue.class);
        IntSyncValue tierSync = syncManager.findSyncHandler("calc_tierIndex", IntSyncValue.class);
        LongSyncValue inputSync = syncManager.findSyncHandler("calc_inputValue", LongSyncValue.class);
        IntSyncValue catalystSync = syncManager.findSyncHandler("calc_catalystType", IntSyncValue.class);
        IntSyncValue fmtSync = syncManager.findSyncHandler("calc_numberFormat", IntSyncValue.class);

        // auto-detect tier if not set
        if (tierSync.getIntValue() == 0) {
            tierSync.setIntValue(multiblock.hasExoticEnergyHatch() ? 14 : multiblock.getEnergyHatchTier());
        }

        Flow col = Flow.column()
            .sizeRel(1)
            .padding(PADDING);

        col.child(
            IKey.lang("GT5U.DTPF.calculator_title")
                .alignment(Alignment.Center)
                .asWidget()
                .marginBottom(8));

        Flow modeRow = Flow.row()
            .sizeRel(1, 0.05f)
            .marginBottom(6);

        String[] modeNames = { "EU/t", "Amps", "OC" };
        String[] modeHints = { "Final EU/t + Tier", "Amps + Tier", "Overclocks + Tier" };

        for (int i = 0; i < 3; i++) {
            final int m = i;
            modeRow.child(
                new ButtonWidget<>().sizeRel(1f / 3f, 1)
                    .overlay(
                        new DynamicDrawable(
                            () -> modeSync.getIntValue() == m ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                                : GTGuiTextures.BUTTON_STANDARD))
                    .tooltip(t -> t.addLine(modeHints[m]))
                    .overlay(
                        IKey.lang(modeNames[m])
                            .alignment(Alignment.CENTER))
                    .onMousePressed(btn -> {
                        modeSync.setIntValue(m);
                        return true;
                    }));
        }
        col.child(modeRow);

        addLabeledTextField(col, "Recipe EU/t", recipeEUtSync, () -> 1L, () -> Long.MAX_VALUE);
        addLabeledTextField(col, "Recipe duration (ticks)", durationSync, () -> 1L, () -> (long) Integer.MAX_VALUE);

        col.child(
            IKey.lang("Voltage Tier")
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .marginBottom(2)
        );

        Flow tierRow = Flow.row()
            .sizeRel(1, 0)
            .coverChildrenHeight()
            .marginBottom(8);

        tierRow.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(0, GTValues.V.length - 1)
                .setTextAlignment(Alignment.Center)
                .value(tierSync)
                .width(40)
                .marginRight(4)
        );

        tierRow.child(
            new ButtonWidget<>().size(WIDTH - PADDING * 2 - 44, 20)
                .overlay(new DynamicDrawable(() -> {
                    int idx = Math.max(0, Math.min(tierSync.getIntValue(), GTValues.V.length - 1));
                    return IKey.lang(GTValues.VN[idx] + " | " + formatNumber(GTValues.V[idx], fmtSync.getIntValue()) + " EU/t");
                }))
                .onMousePressed(mouseButton -> {
                    // consume click so nothing else triggers
                    return true;
                })
        );

        col.child(tierRow);

        col.child(IKey.dynamic(() -> switch (modeSync.getIntValue()) {
            case 0 -> "Final EU/t (enter final EU/t you want):";
            case 1 -> "Input amps (enter how many amps you'll provide):";
            case 2 -> "Overclocks (enter number of overclocks):";
            default -> "";
        })
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .marginBottom(2));

        col.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbersLong(() -> 1L, () -> Long.MAX_VALUE)
                .setTextAlignment(Alignment.CenterLeft)
                .value(inputSync)
                .size(WIDTH - PADDING * 2, 20)
                .marginBottom(8));

        IPanelHandler catPanelHandler = syncManager.panel("catPanel", (p_sm, sh) -> {
            ModularPanel catPanel = new ModularPanel("catPanel").size(180, 120)
                .relative(calcPanel)
                .center();

            Flow row1 = Flow.row().padding(8);
            Flow row2 = Flow.row().padding(8);

            for (int i = 0; i < 3; i++) {
                row1.child(createCatalystButton(i, catalystSync, fmtSync, sh));
            }
            for (int i = 3; i < 6; i++) {
                row2.child(createCatalystButton(i, catalystSync, fmtSync, sh));
            }

            catPanel.child(row1);
            catPanel.child(row2);
            return catPanel;
        }, true);

        col.child(
            IKey.lang("Catalyst")
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .marginBottom(2));
        col.child(
            new ButtonWidget<>().size(56, 56)
                .overlay(new DynamicDrawable(() -> getCatalystIcon(catalystSync.getIntValue())))
                .overlay(
                    IKey.dynamic(() -> getCatalystLabel(catalystSync.getIntValue(), fmtSync.getIntValue()))
                        .alignment(Alignment.BottomCenter))
                .tooltip(t -> t.addLine(getCatalystTooltip(catalystSync.getIntValue())))
                .onMousePressed(mouseButton -> {
                    if (catPanelHandler.isPanelOpen()) {
                        catPanelHandler.closePanel();
                    } else {
                        catPanelHandler.openPanel();
                    }
                    return true;
                })
                .marginBottom(12));

        col.child(
            IKey.dynamic(
                () -> calculateResult(
                    recipeEUtSync.getLongValue(),
                    durationSync.getLongValue(),
                    tierSync.getIntValue(),
                    inputSync.getLongValue(),
                    catalystSync.getIntValue(),
                    modeSync.getIntValue(),
                    fmtSync.getIntValue()))
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .sizeRel(1)
                .marginBottom(8));

        col.child(
            new ButtonWidget<>().size(140, 20)
                .overlay(IKey.lang("Copy to Clipboard"))
                .onMousePressed(mouseButton -> {
                    GuiScreen.setClipboardString(
                        calculateResult(
                            recipeEUtSync.getLongValue(),
                            durationSync.getLongValue(),
                            tierSync.getIntValue(),
                            inputSync.getLongValue(),
                            catalystSync.getIntValue(),
                            modeSync.getIntValue(),
                            fmtSync.getIntValue()));
                    return true;
                })
                .marginBottom(8));

        calcPanel.child(
            new ButtonWidget<>().pos(WIDTH - 40, HEIGHT - 40)
                .size(32, 32)
                .overlay(GTGuiTextures.INFORMATION_SYMBOL)
                .tooltip(t -> t.addLine("Number format: " + getFormatName(fmtSync.getIntValue())))
                .onMousePressed(mouseButton -> {
                    fmtSync.setIntValue((fmtSync.getIntValue() + 1) % 3);
                    return true;
                }));

        calcPanel.child(col);
        return calcPanel;
    }

    private String getFormatName(int fmt) {
        return switch (fmt) {
            case 1 -> "Numeric";
            case 2 -> "Scientific";
            default -> "Prefix";
        };
    }

    private ButtonWidget<?> createCatalystButton(int type, IntSyncValue catalystSync, IntSyncValue fmtSync, IPanelHandler panelHandler) {
        return new ButtonWidget<>()
            .size(50, 50)
            .overlay(new DynamicDrawable(() -> getCatalystIcon(type)))
            .tooltip(t -> t.addLine(getCatalystTooltip(type)))
            .onMousePressed(mouseButton -> {
                catalystSync.setValue(type);
                panelHandler.closePanel();
                return true;
            });
    }

    private DynamicDrawable getCatalystIcon(int type) {
        if (type <= 0 || type > MTEPlasmaForge.valid_fuels.length) {
            return new DynamicDrawable(() -> GTGuiTextures.BUTTON_STANDARD_DISABLED);
        }
        final FluidStack plasma = MTEPlasmaForge.valid_fuels[type - 1];
        return new DynamicDrawable(() -> new FluidDrawable().setFluid(plasma));
    }

    private String getCatalystLabel(int type, int fmt) {
        if (type <= 0 || type > MTEPlasmaForge.valid_fuels.length) return "None";
        long catalystEUperL = MTEPlasmaForge.FUEL_ENERGY_VALUES.get(MTEPlasmaForge.valid_fuels[type - 1].getFluid()).getLeft();
        return formatNumber(catalystEUperL, fmt) + " EU/L";
    }

    private String getCatalystTooltip(int type) {
        if (type <= 0 || type > MTEPlasmaForge.valid_fuels.length)
            return "No extra catalyst (for recipes without catalyst)";
        Pair<Long, Float> pair = MTEPlasmaForge.FUEL_ENERGY_VALUES.get(MTEPlasmaForge.valid_fuels[type - 1].getFluid());
        return CATALYST_SHORT[type - 1] + " Catalyst\nEnergy per L: "
            + GTUtility.formatNumbers(pair.getLeft())
            + " EU";
    }

    private String formatNumber(long value, int fmt) {
        if (fmt == 1) return GTUtility.formatNumbers(value);
        if (fmt == 2) return GTUtility.scientificFormat(value);
        if (value < 1000) return String.valueOf(value);
        int exp = (int) (Math.log10(value) / 3);
        String[] prefixes = { "", "k", "M", "B", "T", "P", "E", "Z", "Y" };
        if (exp >= prefixes.length) return String.format("%.2fY", value / Math.pow(1000, prefixes.length - 1));
        return String.format("%.2f%s", value / Math.pow(1000, exp), prefixes[exp]);
    }

    private String calculateResult(long recipeEUt, long duration, int tierIdx, long inputValue, int catalystType,
        int mode, int fmt) {
        if (recipeEUt <= 0 || duration <= 0 || tierIdx < 0 || tierIdx >= GTValues.V.length) {
            return EnumChatFormatting.RED + "Invalid parameters" + EnumChatFormatting.RESET;
        }

        long tierEUt = GTValues.V[tierIdx];
        long providedEU = 0L;
        long usedAmps = 0L;
        long oc = 0L;

        switch (mode) {
            case 0: // Final EU/t + tier
                providedEU = inputValue;
                usedAmps = (providedEU + tierEUt - 1) / tierEUt;
                break;
            case 1: // Amps + tier
                usedAmps = inputValue;
                providedEU = tierEUt * usedAmps;
                break;
            case 2: // Overclocks + tier
                oc = Math.min(inputValue, 62);
                long ratio = (1L << (2 * oc));
                providedEU = recipeEUt * ratio;
                usedAmps = (providedEU + tierEUt - 1) / tierEUt;
                break;
        }

        if (mode != 2) {
            long realRatio = providedEU / recipeEUt;
            while (realRatio >= 4) {
                realRatio /= 4;
                oc++;
            }
        }

        long numerator = recipeEUt * ((1L << oc) - 1);
        double extraCatalystL = 0.0;
        if (catalystType > 0) {
            long catalystEUperL = MTEPlasmaForge.FUEL_ENERGY_VALUES
                .get(MTEPlasmaForge.valid_fuels[catalystType - 1].getFluid())
                .getLeft();
            extraCatalystL = (double) numerator * duration / catalystEUperL;
        }

        StringBuilder sb = new StringBuilder("Plasma Forge catalyst calculator:\n");
        sb.append("Overclocks: ")
            .append(formatNumber(oc, fmt))
            .append("\n");
        sb.append("Required amps: ")
            .append(formatNumber(usedAmps, fmt))
            .append("\n");
        sb.append("Provided EU/t: ")
            .append(formatNumber(providedEU, fmt))
            .append("\n");
        if (catalystType > 0) {
            sb.append("Extra catalyst (L): ")
                .append(String.format("%.6f", extraCatalystL))
                .append("\n");
            sb.append("Rounded up (L): ")
                .append(formatNumber((long) Math.ceil(extraCatalystL), fmt))
                .append("\n");
        } else {
            sb.append("No extra catalyst needed\n");
        }
        return sb.toString();
    }

    private void addLabeledTextField(Flow column, String label, LongSyncValue value, Supplier<Long> min,
        Supplier<Long> max) {
        column.child(
            IKey.lang(label)
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .marginBottom(2));
        column.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbersLong(min, max)
                .setTextAlignment(Alignment.CenterLeft)
                .value(value)
                .size(WIDTH - PADDING * 2, 20)
                .marginBottom(2));
    }
}
