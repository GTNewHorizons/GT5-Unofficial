package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData.DEFAULT_FORMATTER;
import static tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData.FUEL_MILESTONE_T7_CONSTANT;
import static tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData.POWER_MILESTONE_T7_CONSTANT;
import static tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData.RECIPE_MILESTONE_T7_CONSTANT;

import java.math.BigInteger;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.google.common.math.LongMath;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class IndividualMilestonePanel {

    private static final int SIZE = 150;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.INDIVIDUAL_MILESTONE);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // registered on the Milestone panel, look up from there
        EnumSyncValue<Milestones> milestoneSyncer = SyncValues.MILESTONE_CLICKED
            .lookupFrom(Panels.MILESTONE, hypervisor);

        // Background symbol
        for (Milestones milestone : Milestones.VALUES) {
            panel.child(getBackgroundImage(milestone, milestoneSyncer));
        }

        // Formatting mode button
        EnumSyncValue<Formatters> formatSyncer = SyncValues.FORMATTER.lookupFrom(Panels.MAIN, hypervisor);
        panel.child(
            new ButtonWidget<>().background(GTGuiTextures.TT_OVERLAY_CYCLIC_BLUE)
                .disableHoverBackground()
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .onMousePressed(d -> {
                    Formatters current = formatSyncer.getValue();
                    formatSyncer.setValue(current.cycle());
                    return true;
                })
                .size(10)
                .marginLeft(5)
                .marginBottom(5)
                .align(Alignment.BottomLeft)
                .tooltip(t -> t.addLine(translateToLocal("fog.button.formatting.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        Flow column = new Column().coverChildren()
            .alignX(0.5f)
            .marginTop(12);

        // Header
        column.child(IKey.dynamic(() -> {
            Milestones milestone = milestoneSyncer.getValue();
            return translateToLocal(milestone.getTitleLangKey());
        })
            .style(EnumChatFormatting.GOLD)
            .alignment(Alignment.CENTER)
            .asWidget()
            .alignX(0.5f)
            .marginBottom(16));

        // Info texts
        BooleanSyncValue inversionSyncer = SyncValues.INVERSION
            .lookupFrom(Modules.CORE, Panels.INDIVIDUAL_MILESTONE, hypervisor);

        column.child(
            createInfoWidget(() -> getTotalProgress(milestoneSyncer.getValue(), formatSyncer.getValue(), hypervisor)));
        column.child(
            createInfoWidget(() -> getLevel(milestoneSyncer.getValue(), inversionSyncer.getBoolValue(), hypervisor))
                .marginTop(10));
        column.child(
            createInfoWidget(
                () -> getLevelProgress(
                    milestoneSyncer.getValue(),
                    formatSyncer.getValue(),
                    inversionSyncer.getBoolValue(),
                    hypervisor)).marginTop(10));
        column.child(
            createInfoWidget(
                () -> getShardsGained(
                    milestoneSyncer.getValue(),
                    formatSyncer.getValue(),
                    inversionSyncer.getBoolValue(),
                    hypervisor)).marginTop(10));

        // Inversion status
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.inversionactive")
                .style(EnumChatFormatting.WHITE, EnumChatFormatting.BOLD)
                .alignment(Alignment.CENTER)
                .scale(0.8f)
                .asWidget()
                .width(150)
                .alignX(0.5f)
                .marginTop(10)
                .setEnabledIf($ -> inversionSyncer.getBoolValue()));

        panel.child(column);
        return panel;
    }

    public static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.INVERSION.registerFor(Modules.CORE, Panels.INDIVIDUAL_MILESTONE, hypervisor);

        SyncValues.TOTAL_RECIPES_PROCESSED.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
        SyncValues.TOTAL_POWER_CONSUMED.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
        SyncValues.TOTAL_FUEL_CONSUMED.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);

        SyncValues.MILESTONE_CHARGE_LEVEL.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
        SyncValues.MILESTONE_CONVERSION_LEVEL.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
        SyncValues.MILESTONE_CATALYST_LEVEL.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
        SyncValues.MILESTONE_COMPOSITION_LEVEL.registerFor(Panels.INDIVIDUAL_MILESTONE, hypervisor);
    }

    private static Widget<?> getBackgroundImage(Milestones milestone, EnumSyncValue<Milestones> syncer) {
        // Cannot simply be DynamicDrawable as the widget width/height also needs to change
        return milestone.getSymbolBackground()
            .asWidget()
            .size(milestone.getSymbolWidth(), milestone.getSymbolHeight())
            .align(Alignment.CENTER)
            .setEnabledIf($ -> syncer.getValue() == milestone);
    }

    private static Widget<?> createInfoWidget(Supplier<String> textSupplier) {
        return IKey.dynamic(textSupplier)
            .alignment(Alignment.CENTER)
            .scale(0.7f)
            .asWidget()
            .width(140)
            .alignX(0.5f);
    }

    private static String getTotalProgress(Milestones milestone, Formatters formatter, SyncHypervisor hypervisor) {
        Number progress = milestone.getTotalSyncer()
            .lookupFrom(Panels.INDIVIDUAL_MILESTONE, hypervisor)
            .getValue();

        return EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress")
            + ": "
            + EnumChatFormatting.GRAY
            + formatter.format(progress)
            + " "
            + translateToLocal(milestone.getProgressLangKey());
    }

    private static String getLevel(Milestones milestone, boolean inversion, SyncHypervisor hypervisor) {
        int rawLevel = milestone.getLevelSyncer()
            .lookupFrom(Panels.INDIVIDUAL_MILESTONE, hypervisor)
            .getValue();
        int level = inversion ? rawLevel : Math.min(rawLevel, 7);

        return EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.milestoneprogress")
            + ": "
            + EnumChatFormatting.GRAY
            + level;
    }

    private static String getLevelProgress(Milestones milestone, Formatters formatter, boolean inversion,
        SyncHypervisor hypervisor) {
        int level = milestone.getLevelSyncer()
            .lookupFrom(Panels.INDIVIDUAL_MILESTONE, hypervisor)
            .getValue();

        if (level >= 7 && !inversion) {
            return EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.milestonecomplete")
                + (formatter != DEFAULT_FORMATTER ? EnumChatFormatting.DARK_RED + "?" : "");
        }

        Number max = switch (milestone) {
            case CHARGE -> {
                if (inversion) {
                    yield POWER_MILESTONE_T7_CONSTANT.multiply(BigInteger.valueOf(level - 5));
                }
                yield BigInteger.valueOf(LongMath.pow(9, level))
                    .multiply(BigInteger.valueOf(LongMath.pow(10, 15)));
            }
            case CONVERSION -> {
                if (inversion) {
                    yield RECIPE_MILESTONE_T7_CONSTANT * (level - 5);
                }
                yield LongMath.pow(4, level) * LongMath.pow(10, 7);
            }
            case CATALYST -> {
                if (inversion) {
                    yield FUEL_MILESTONE_T7_CONSTANT * (level - 5);
                }
                yield LongMath.pow(3, level) * LongMath.pow(10, 4);
            }
            case COMPOSITION -> level + 1;
        };

        return EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.progress")
            + ": "
            + EnumChatFormatting.GRAY
            + formatter.format(max)
            + " "
            + translateToLocal(milestone.getProgressLangKey());
    }

    public static String getShardsGained(Milestones milestone, Formatters formatter, boolean inversion,
        SyncHypervisor hypervisor) {
        int rawLevel = milestone.getLevelSyncer()
            .lookupFrom(Panels.INDIVIDUAL_MILESTONE, hypervisor)
            .getValue();
        int level = inversion ? rawLevel : Math.min(rawLevel, 7);
        int sum = level * (level + 1) / 2;

        return EnumChatFormatting.WHITE + translateToLocal("gt.blockmachines.multimachine.FOG.shardgain")
            + ": "
            + EnumChatFormatting.GRAY
            + formatter.format(sum);
    }
}
