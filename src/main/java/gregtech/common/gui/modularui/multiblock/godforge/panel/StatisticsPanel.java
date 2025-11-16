package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Statistics;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public class StatisticsPanel {

    private static final int SIZE_W = 300;
    private static final int SIZE_H = 300;
    private static boolean usingPreview = false;
    private static final String[] moduleValues = new String[28];

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {

        ModularPanel panel = hypervisor.getModularPanel(Panels.STATISTICS);

        registerSyncValues(hypervisor);
        usingPreview = false;
        panel.size(SIZE_W, SIZE_H)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .paddingLeft(12)
            .paddingBottom(8)
            .paddingTop(12);

        panel.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.modulestats"))
                .alignment(Alignment.TopCenter)
                .asWidget()
                .height(15)
                .alignX(Alignment.CENTER));

        EnumSyncValue<Formatters> formatSyncer = SyncValues.FORMATTER.lookupFrom(Panels.MAIN, hypervisor);

        panel.child(
            new ButtonWidget<>().background(GTGuiTextures.TT_OVERLAY_CYCLIC_BLUE)
                .disableHoverBackground()
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .onMousePressed(d -> {
                    Formatters current = formatSyncer.getValue();
                    formatSyncer.setValue(current.cycle());
                    populateModuleValues(hypervisor);
                    return true;
                })
                .size(16)
                .alignY(Alignment.BottomLeft)
                .left(8)
                .tooltip(t -> t.addLine(translateToLocal("fog.button.formatting.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));
        panel.child(createPreviewRow(hypervisor));

        populateModuleValues(hypervisor);
        Grid grid = new Grid().top(38)
            .row(createHeaderRow());
        for (Statistics stat : Statistics.values()) {
            grid.row(createStatisticsRow(stat));
        }
        panel.child(grid);
        panel.child(ForgeOfGodsGuiUtil.panelCloseButton());
        // Vertical grid lines
        // i love pos i love pos i love pos i love pos i love pos
        for (int i = 0; i < 4; i++) {
            panel.child(
                new IDrawable.DrawableWidget(new Rectangle().setColor(Color.rgb(190, 200, 0))).size(1, 227)
                    .pos(81 + WIDTH_MAJOR * i, 38));
        }

        // Horizontal grid lines
        for (int i = 0; i < 8; i++) {
            panel.child(
                new IDrawable.DrawableWidget(new Rectangle().setColor(Color.rgb(0, 170, 170))).size(276, 1)
                    .pos(12, 55 + HEIGHT_MAJOR * i));
        }

        return panel;
    }

    public static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.PREVIEW_FUEL_FACTOR.registerFor(Panels.STATISTICS, hypervisor);
        SyncValues.FUEL_FACTOR.registerFor(Panels.STATISTICS, hypervisor);
    }

    private static final int HEIGHT_MINOR = 18;
    private static final int HEIGHT_MAJOR = 30;
    private static final int WIDTH_MINOR = 40;
    private static final int WIDTH_MAJOR = 53;

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
        return IKey.str(EnumChatFormatting.GOLD + translateToLocal(key))
            .alignment(Alignment.Center)
            .asWidget()
            .size(WIDTH_MINOR, HEIGHT_MINOR)
            .scale(0.8f);
    }

    private static Flow createPreviewRow(SyncHypervisor hypervisor) {
        Flow previewRow = Flow.row()
            .coverChildren()
            .alignX(0.8f)
            .alignY(1f);
        previewRow.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.factorpreview"))
                .alignment(Alignment.CenterRight)
                .asWidget()
                .scale(0.9f)
                .size(100, 18)
                .marginRight(4));
        previewRow.child(
            new TextFieldWidget().addTooltipLine(translateToLocal("fog.text.tooltip.factorpreview"))
                .size(70, 18)
                .setFormatAsInteger(true)
                .value(SyncValues.PREVIEW_FUEL_FACTOR.create(hypervisor))
                .setNumbers(1, Integer.MAX_VALUE)
                .setScrollValues(1, 4, 64)
                .setTextAlignment(Alignment.CENTER)
                .marginRight(4));
        previewRow.child(
            new ButtonWidget<>().clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(
                    t -> t.addLine(StatCollector.translateToLocal("fog.text.tooltip.applysimulationchanges"))
                        .showUpTimer(TOOLTIP_DELAY))
                .size(35, 18)
                .overlay(IKey.lang("fog.cosmetics.applycolor"))
                .onMousePressed(d -> {
                    if (d == 0) {
                        usingPreview = true;
                        populateModuleValues(hypervisor);
                    }
                    return true;
                }));
        return previewRow;
    }

    private static List<IWidget> createStatisticsRow(Statistics statistic) {
        List<IWidget> returnList = new ArrayList<>();
        int baseIndex = statistic.displayIndex * 4;
        returnList.add(
            IKey.str(statistic.toString())
                .alignment(Alignment.Center)
                .asWidget()
                .size(69, HEIGHT_MAJOR));
        returnList.add(
            IKey.dynamic(() -> EnumChatFormatting.GREEN + moduleValues[baseIndex + SMELTING_INDEX])
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));
        returnList.add(
            IKey.dynamic(() -> EnumChatFormatting.GREEN + moduleValues[baseIndex + MOLTEN_INDEX])
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));
        returnList.add(
            IKey.dynamic(() -> EnumChatFormatting.GREEN + moduleValues[baseIndex + PLASMA_INDEX])
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));
        returnList.add(
            IKey.dynamic(() -> EnumChatFormatting.GREEN + moduleValues[baseIndex + EXOTIC_INDEX])
                .alignment(Alignment.CENTER)
                .asWidget()
                .size(WIDTH_MAJOR, HEIGHT_MAJOR));

        return returnList;
    }

    private static final int SMELTING_INDEX = 0;
    private static final int MOLTEN_INDEX = 1;
    private static final int PLASMA_INDEX = 2;
    private static final int EXOTIC_INDEX = 3;
    private static final MTEBaseModule[] MODULES = new MTEBaseModule[] { new MTESmeltingModule("smelting"),
        new MTEMoltenModule("molten"), new MTEPlasmaModule("plasma"), new MTEExoticModule("exotic") };

    private static void populateModuleValues(SyncHypervisor hypervisor) {
        IntSyncValue fuelFactorSyncer = SyncValues.FUEL_FACTOR.lookupFrom(Panels.STATISTICS, hypervisor);
        IntSyncValue previewFuelFactorSyncer = SyncValues.PREVIEW_FUEL_FACTOR
            .lookupFrom(Panels.STATISTICS, hypervisor);
        EnumSyncValue<Formatters> formatSyncer = SyncValues.FORMATTER.lookupFrom(Panels.MAIN, hypervisor);

        for (Statistics stat : Statistics.values()) {
            for (int moduleIndex = 0; moduleIndex < MODULES.length; moduleIndex++) {
                int currentIndex = stat.displayIndex * 4 + moduleIndex;
                MTEBaseModule currentModule = MODULES[moduleIndex];
                moduleValues[currentIndex] = stat.calculate(
                    currentModule,
                    usingPreview ? Math.max(1, previewFuelFactorSyncer.getIntValue())
                        : fuelFactorSyncer.getIntValue(),
                    hypervisor.getData(),
                    formatSyncer.getValue());
            }
        }
    }

}
