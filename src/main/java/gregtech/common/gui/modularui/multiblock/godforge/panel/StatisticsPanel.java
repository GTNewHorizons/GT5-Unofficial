package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.data.Statistics;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class StatisticsPanel {

    private static final int SIZE = 300;

    private static final int HEIGHT_MINOR = 18;
    private static final int HEIGHT_MAJOR = 30;
    private static final int WIDTH_MINOR = 40;
    private static final int WIDTH_MAJOR = 53;

    private static final int SMELTING_INDEX = 0;
    private static final int MOLTEN_INDEX = 1;
    private static final int PLASMA_INDEX = 2;
    private static final int EXOTIC_INDEX = 3;

    private static final MTEBaseModule[] MODULES = { new MTESmeltingModule("smelting"), new MTEMoltenModule("molten"),
        new MTEPlasmaModule("plasma"), new MTEExoticModule("exotic") };

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.STATISTICS);

        MutableBoolean usingPreview = new MutableBoolean(false);
        MutableInt previewFuelFactor = new MutableInt(1);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .paddingLeft(12)
            .paddingBottom(8)
            .paddingTop(12)
            .child(ForgeOfGodsGuiUtil.panelCloseButton())
            .onCloseAction(() -> {
                usingPreview.setValue(false);
                previewFuelFactor.setValue(1);
            });

        // Title
        panel.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.modulestats")
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.TopCenter)
                .asWidget()
                .height(15)
                .alignX(Alignment.CENTER));

        // Format mode button
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
                .size(16)
                .alignY(Alignment.BottomLeft)
                .left(8)
                .tooltip(t -> t.addLine(translateToLocal("fog.button.formatting.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Preview fuel factor widgets
        panel.child(createPreviewRow(usingPreview, previewFuelFactor));

        // Statistics grid
        Grid grid = new Grid().top(38)
            .row(createHeaderRow());
        for (Statistics stat : Statistics.values()) {
            grid.row(createStatisticsRow(hypervisor, stat, usingPreview, previewFuelFactor));
        }
        panel.child(grid);

        // Vertical grid lines
        // i love pos i love pos i love pos i love pos i love pos
        for (int i = 0; i < 4; i++) {
            panel.child(
                new IDrawable.DrawableWidget(new Rectangle().color(Color.rgb(190, 200, 0))).size(1, 227)
                    .pos(81 + WIDTH_MAJOR * i, 38));
        }

        // Horizontal grid lines
        for (int i = 0; i < 8; i++) {
            panel.child(
                new IDrawable.DrawableWidget(new Rectangle().color(Color.rgb(0, 170, 170))).size(276, 1)
                    .pos(12, 55 + HEIGHT_MAJOR * i));
        }

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.FUEL_FACTOR.registerFor(Panels.STATISTICS, hypervisor);
    }

    private static List<IWidget> createHeaderRow() {
        List<IWidget> returnList = new ArrayList<>();
        returnList.add(
            IDrawable.EMPTY.asWidget()
                .width(68)
                .height(HEIGHT_MINOR));

        returnList.add(createHeaderModuleEntry("gt.blockmachines.multimachine.FOG.powerforge").marginRight(13));
        returnList.add(createHeaderModuleEntry("gt.blockmachines.multimachine.FOG.meltingcore").marginRight(13));
        returnList.add(
            createHeaderModuleEntry("gt.blockmachines.multimachine.FOG.plasmafab").width(WIDTH_MINOR + 4)
                .marginRight(10));
        returnList.add(createHeaderModuleEntry("gt.blockmachines.multimachine.FOG.exoticizer"));

        return returnList;
    }

    private static TextWidget<?> createHeaderModuleEntry(String key) {
        return IKey.lang(key)
            .style(EnumChatFormatting.GOLD)
            .alignment(Alignment.Center)
            .asWidget()
            .size(WIDTH_MINOR, HEIGHT_MINOR)
            .scale(0.8f);
    }

    private static Flow createPreviewRow(MutableBoolean usingPreview, MutableInt previewFuelFactor) {
        Flow previewRow = Flow.row()
            .coverChildren()
            .alignX(0.8f)
            .alignY(1f);

        // Header
        previewRow.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.factorpreview")
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.CenterRight)
                .asWidget()
                .scale(0.9f)
                .size(100, 18)
                .marginRight(4));

        // Preview text field
        previewRow.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .size(70, 18)
                .value(new IntValue.Dynamic(previewFuelFactor::intValue, previewFuelFactor::setValue))
                .setNumbers(1, Integer.MAX_VALUE)
                .setScrollValues(1, 4, 64)
                .setTextAlignment(Alignment.CENTER)
                .tooltip(t -> t.addLine(translateToLocal("fog.text.tooltip.factorpreview")))
                .marginRight(4));

        // Apply button
        previewRow.child(
            new ButtonWidget<>().size(35, 18)
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(IKey.lang("fog.cosmetics.applycolor"))
                .onMousePressed(d -> {
                    usingPreview.setValue(true);
                    return true;
                })
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.text.tooltip.applysimulationchanges")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return previewRow;
    }

    private static List<IWidget> createStatisticsRow(SyncHypervisor hypervisor, Statistics stat,
        MutableBoolean usingPreview, MutableInt previewFuelFactor) {
        List<IWidget> returnList = new ArrayList<>();
        returnList.add(
            IKey.str(stat.toString())
                .alignment(Alignment.Center)
                .asWidget()
                .size(69, HEIGHT_MAJOR));

        // Smelting module
        returnList.add(
            IKey.dynamic(() -> getModuleValue(hypervisor, stat, SMELTING_INDEX, usingPreview, previewFuelFactor))
                .style(EnumChatFormatting.GREEN)
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));

        // Molten module
        returnList.add(
            IKey.dynamic(() -> getModuleValue(hypervisor, stat, MOLTEN_INDEX, usingPreview, previewFuelFactor))
                .style(EnumChatFormatting.GREEN)
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));

        // Plasma module
        returnList.add(
            IKey.dynamic(() -> getModuleValue(hypervisor, stat, PLASMA_INDEX, usingPreview, previewFuelFactor))
                .style(EnumChatFormatting.GREEN)
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));

        // Exotic module
        returnList.add(
            IKey.dynamic(() -> getModuleValue(hypervisor, stat, EXOTIC_INDEX, usingPreview, previewFuelFactor))
                .style(EnumChatFormatting.GREEN)
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));

        return returnList;
    }

    private static String getModuleValue(SyncHypervisor hypervisor, Statistics stat, int moduleIndex,
        MutableBoolean usingPreview, MutableInt previewFuelFactor) {
        ForgeOfGodsData data = hypervisor.getData();
        int fuelFactor = data.getFuelConsumptionFactor();
        Formatters format = data.getFormatter();

        int displayFuelFactor = usingPreview.booleanValue() ? Math.max(1, previewFuelFactor.intValue()) : fuelFactor;
        return stat.calculate(MODULES[moduleIndex], displayFuelFactor, hypervisor.getData(), format);
    }
}
