package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.GTValues.formattingCodes;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.coolingStrOrder;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createModuleLimitText;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createTierLine;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.objects.XSTR;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import gregtech.common.tileentities.machines.multi.foundry.MTEExoFoundry;

public class MTEExoFoundryGui extends MTEMultiBlockBaseGui<MTEExoFoundry> {

    private final FoundryData calculatorData;
    private boolean usingPreview = false;

    public MTEExoFoundryGui(MTEExoFoundry base) {
        super(base);
        calculatorData = base.foundryData.copy();
        calculatorData.tier = 3; // to allow testing with any module combination regardless of structure
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "Module1",
            new IntSyncValue(() -> multiblock.getModuleSynced(0), ordinal -> multiblock.setModule(0, ordinal)));
        syncManager.syncValue(
            "Module2",
            new IntSyncValue(() -> multiblock.getModuleSynced(1), ordinal -> multiblock.setModule(1, ordinal)));
        syncManager.syncValue(
            "Module3",
            new IntSyncValue(() -> multiblock.getModuleSynced(2), ordinal -> multiblock.setModule(2, ordinal)));
        syncManager.syncValue(
            "Module4",
            new IntSyncValue(() -> multiblock.getModuleSynced(3), ordinal -> multiblock.setModule(3, ordinal)));
        syncManager.syncValue(
            "Tier",
            new IntSyncValue(() -> multiblock.foundryData.tier, val -> multiblock.foundryData.tier = val));

        BooleanSyncValue usingPreviewSync = new BooleanSyncValue(() -> usingPreview, val -> usingPreview = val);
        syncManager.syncValue("UsingPreview", usingPreviewSync);
        syncManager.syncValue("Module1Calc", new IntSyncValue(() -> calculatorData.modules[0].ordinal(), val -> {
            calculatorData.setModule(0, val);
            usingPreviewSync.setBoolValue(calculatorData.shouldUsePreview(multiblock.foundryData));
        }));
        syncManager.syncValue("Module2Calc", new IntSyncValue(() -> calculatorData.modules[1].ordinal(), val -> {
            calculatorData.setModule(1, val);
            usingPreviewSync.setBoolValue(calculatorData.shouldUsePreview(multiblock.foundryData));
        }));
        syncManager.syncValue("Module3Calc", new IntSyncValue(() -> calculatorData.modules[2].ordinal(), val -> {
            calculatorData.setModule(2, val);
            usingPreviewSync.setBoolValue(calculatorData.shouldUsePreview(multiblock.foundryData));
        }));
        syncManager.syncValue("Module4Calc", new IntSyncValue(() -> calculatorData.modules[3].ordinal(), val -> {
            calculatorData.setModule(3, val);
            usingPreviewSync.setBoolValue(calculatorData.shouldUsePreview(multiblock.foundryData));
        }));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createConfigButton());
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.panel(
            "statsPanel",
            (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager),
            true);
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.FOUNDRY_CALCULATOR)
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

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler contribPanel = syncManager
            .panel("contributorsPanel", (p_syncManager, syncHandler) -> openContributorsPanel(parent), true);
        return new ButtonWidget<>().size(18)
            .marginTop(4)
            .overlay(IDrawable.EMPTY)
            .tooltip(
                t -> t.addLine(EnumChatFormatting.AQUA + translateToLocal("GT5U.gui.text.contributors.panel.open")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .background(GTGuiTextures.PICTURE_EXOFOUNDRY_LOGO)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!contribPanel.isPanelOpen()) {
                    contribPanel.openPanel();
                } else {
                    contribPanel.closePanel();
                }
                return true;
            });
    }

    private ModularPanel openContributorsPanel(ModularPanel parent) {
        ModularPanel panel = new ModularPanel("contributorsPanel").relative(parent)
            .size(getBasePanelWidth(), getBasePanelHeight() + 20)
            .background(GTGuiTextures.FOUNDRY_BACKGROUND_CONTRIBUTORS);
        panel.child(
            IKey.lang("GT5U.gui.text.contributors.header")
                .asWidget()
                .style(EnumChatFormatting.GOLD)
                .marginTop(8)
                .align(Alignment.TopCenter))
            .child(
                ButtonWidget.panelCloseButton()
                    .background(GTGuiTextures.BUTTON_FOUNDRY));

        Flow contributorColumn = Flow.column()
            .coverChildren()
            .marginLeft(14)
            .marginTop(24);

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.contributors.projectlead",
                createContributorEntry("Chrom", Color.PURPLE.brighterSafe(2))));
        contributorColumn
            .child(createContributorSection("GT5U.gui.text.contributors.programming", createSerenibyssEntry()));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.contributors.textures",
                createContributorEntry("Auynonymous", 0xFFFD80CF).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "Dev my foundry...")),
                createContributorEntry("June", Color.PINK_ACCENT.main).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "I Eat Lawnbasers"))));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.contributors.rendering",
                createContributorEntry("Sisyphus", 0xFF2BCAD9)));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.structure",
                createContributorEntry("IX", 0xFFE12120).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "Good for Health, Bad for Education"))));

        final int numbers = 100 + XSTR.XSTR_INSTANCE.nextInt(900);
        final StringBuilder formattedNumbers = new StringBuilder();
        for (char c : Integer.toString(numbers)
            .toCharArray()) {
            int randIndex = XSTR.XSTR_INSTANCE.nextInt(formattingCodes.length);
            String prepend = formattingCodes[randIndex] + c + EnumChatFormatting.RESET;
            formattedNumbers.append(prepend);
        }
        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.idea",
                createContributorEntry("TheEpicGamer" + formattedNumbers, 0xFF2BCAD9).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "Dyson rework soon™")),
                createContributorEntry(
                    EnumChatFormatting.DARK_AQUA + "Ruling" + EnumChatFormatting.DARK_GRAY + "_0",
                    -1).tooltip(
                        t -> t.scale(0.8f)
                            .addLine(EnumChatFormatting.DARK_GRAY + "Spreadsheets..."))));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.contributors.playtesting",
                createContributorEntry("Scam Run", Color.DEEP_ORANGE.main).tooltip(t -> {
                    t.addLine(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Members");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "Working");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "RamseySpace");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "Ducked");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "Inphysible");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "TheEpicGamer");
                    t.addLine(EnumChatFormatting.DARK_GRAY + "Chrom");
                })));

        panel.child(contributorColumn);

        return panel;
    }

    private static Flow createContributorSection(String titleKey, Widget<?>... entries) {
        return new Column().coverChildren()
            .marginBottom(5)
            .alignX(0)
            .child(
                IKey.lang(titleKey)
                    .style(EnumChatFormatting.UNDERLINE)
                    .alignment(Alignment.CenterLeft)
                    .asWidget()
                    .marginBottom(2)
                    .alignX(0))
            .children(Arrays.asList(entries));
    }

    private static TextWidget<?> createContributorEntry(String name, int color) {
        IKey key = IKey.str(name)
            .alignment(Alignment.CenterLeft);
        if (color != -1) key.color(color);
        return key.asWidget()
            .anchorLeft(0);
    }

    private static Widget<?> createSerenibyssEntry() {
        IKey key = IKey.str("serenibyss")
            .alignment(Alignment.CenterLeft)
            .color(0xFFFFA3FB);
        String url = "https://github.com/Roadhog360/Et-Futurum-Requiem/pull/673#issuecomment-3649833976";
        return new ButtonWidget<>().background(key)
            .anchorLeft(0)
            .size(80, 9)
            .onMousePressed(d -> {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI(url));
                        } catch (IOException | URISyntaxException ignored) {}
                    }
                }
                return true;
            })
            .tooltip(t -> {
                t.scale(0.8f);
                t.addLine(EnumChatFormatting.DARK_GRAY + "Click to open a Github link");
            });
    }

    private ModularPanel openInfoPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        IntSyncValue moduleSync0 = syncManager.findSyncHandler("Module1", IntSyncValue.class);
        IntSyncValue moduleSync1 = syncManager.findSyncHandler("Module2", IntSyncValue.class);
        IntSyncValue moduleSync2 = syncManager.findSyncHandler("Module3", IntSyncValue.class);
        IntSyncValue moduleSync3 = syncManager.findSyncHandler("Module4", IntSyncValue.class);

        BooleanSyncValue usingPreviewSync = syncManager.findSyncHandler("UsingPreview", BooleanSyncValue.class);
        IntSyncValue moduleCalc0 = syncManager.findSyncHandler("Module1Calc", IntSyncValue.class);
        IntSyncValue moduleCalc1 = syncManager.findSyncHandler("Module2Calc", IntSyncValue.class);
        IntSyncValue moduleCalc2 = syncManager.findSyncHandler("Module3Calc", IntSyncValue.class);
        IntSyncValue moduleCalc3 = syncManager.findSyncHandler("Module4Calc", IntSyncValue.class);

        IntValue.Dynamic tierDyn = new IntValue.Dynamic(() -> calculatorData.tier, val -> calculatorData.tier = val);

        return new ModularPanel("statsPanel").relative(parent)
            .rightRel(1)
            .topRel(0)
            .size(150, 150)
            .widgetTheme("backgroundPopup")
            .onCloseAction(() -> {
                // Reset preview for next time in case the panel is reopened before the GUI is closed
                moduleCalc0.setIntValue(moduleSync0.getIntValue());
                moduleCalc1.setIntValue(moduleSync1.getIntValue());
                moduleCalc2.setIntValue(moduleSync2.getIntValue());
                moduleCalc3.setIntValue(moduleSync3.getIntValue());
                usingPreviewSync.setBoolValue(false);
            })
            .child(
                new Column().sizeRel(1)
                    .paddingTop(4)
                    .child(
                        new TextWidget<>("Stats").alignment(Alignment.TopCenter)
                            .height(9))
                    .widgetTheme("backgroundPopup")
                    .child(IKey.dynamic(() -> {
                        FoundryData data = usingPreviewSync.getBoolValue() ? calculatorData : multiblock.foundryData;
                        return "Speed: " + TooltipHelper.SPEED_COLOR + data.getSpeedStr();
                    })
                        .asWidget()
                        .left(4)
                        .size(120, 20)
                        .marginBottom(2))

                    .child(IKey.dynamic(() -> {
                        FoundryData data = usingPreviewSync.getBoolValue() ? calculatorData : multiblock.foundryData;
                        return "Parallels Per Tier: " + TooltipHelper.PARALLEL_COLOR + data.getParallelsString();
                    })
                        .asWidget()
                        .size(120, 20)
                        .marginBottom(2)
                        .left(4))
                    .child(IKey.dynamic(() -> {
                        FoundryData data = usingPreviewSync.getBoolValue() ? calculatorData : multiblock.foundryData;
                        return "EU Consumption: " + TooltipHelper.EFF_COLOR + data.getEuEFFString();
                    })
                        .asWidget()
                        .size(120, 20)
                        .marginBottom(2)
                        .left(4))
                    .child(IKey.dynamic(() -> {
                        FoundryData data = usingPreviewSync.getBoolValue() ? calculatorData : multiblock.foundryData;
                        return "OC Factor: " + EnumChatFormatting.LIGHT_PURPLE + data.getOCFactorString();
                    })
                        .asWidget()
                        .size(120, 20)
                        .marginBottom(2)
                        .left(4))
                    .child(
                        new Row().size(120, 20)
                            .childPadding(1)
                            .marginBottom(2)
                            .child(createModuleSelectButton(p_syncManager, parent, 0, moduleCalc0, tierDyn, true))
                            .child(createModuleSelectButton(p_syncManager, parent, 1, moduleCalc1, tierDyn, true))
                            .child(createModuleSelectButton(p_syncManager, parent, 2, moduleCalc2, tierDyn, true))
                            .child(createModuleSelectButton(p_syncManager, parent, 3, moduleCalc3, tierDyn, true)))
                    .child(IKey.dynamic(() -> {
                        boolean isPreview = usingPreviewSync.getBoolValue();
                        EnumChatFormatting color = isPreview ? EnumChatFormatting.RED : EnumChatFormatting.GREEN;
                        String key = isPreview ? "GT5U.gui.text.foundry.modules.preview"
                            : "GT5U.gui.text.foundry.modules.installed";
                        return color + StatCollector.translateToLocal(key);
                    })
                        .scale(0.9f)
                        .asWidget()
                        .size(120, 20)
                        .alignX(0.5f))
                    .child(
                        createPairHoldingColumn(calculatorData, true).right(4)
                            .alignY(0.2f)));
    }

    protected IWidget createConfigButton() {
        return new ButtonWidget<>().size(18, 18)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                multiblock.terminalSwitch = !multiblock.terminalSwitch;
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.foundrymoduleselect")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .paddingTop(4)
                    .paddingBottom(4)
                    .paddingLeft(4)
                    .paddingRight(0)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel).collapseDisabledChild()
                            .setEnabledIf(widget -> !multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8))
                    .child(
                        createModuleTerminalTextWidget(syncManager, panel)
                            .setEnabledIf(widget -> multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild())
                    .childIf(
                        multiblock.supportsTerminalRightCornerColumn(),
                        createTerminalRightCornerColumn(panel, syncManager)));
    }

    protected IWidget createModuleSelectButton(PanelSyncManager syncManager, ModularPanel parent, int index,
        IIntValue<Integer> moduleSync, IIntValue<Integer> tierSync, boolean isStats) {
        IPanelHandler selectPanel = syncManager.panel(
            "moduleSelectPanel" + index + (isStats ? "stats" : ""),
            (p_syncManager, syncHandler) -> openModuleConfigPanel(parent, index, moduleSync, isStats),
            true);

        return new Row().size(30, 16)
            .marginBottom(index != 0 ? 2 : 0)
            .child(
                new ButtonWidget<>().size(16, 16)
                    .tooltipBuilder(t -> {
                        t.addLine("Select Module " + (index + 1));

                        int reqTier = Math.max(1, index);
                        if (tierSync.getIntValue() >= reqTier) {
                            createTooltipForModule(t, moduleSync.getIntValue());
                            t.addLine(EnumChatFormatting.GRAY + "Shift-click to unset");
                        } else {
                            t.addLine(EnumChatFormatting.RED + "Requires Tier " + reqTier + " Chassis");
                        }
                        t.setAutoUpdate(true);
                    })
                    .overlay(new DynamicDrawable(() -> {
                        if (moduleSync.getIntValue() == FoundryModule.UNSET.ordinal()) {
                            int reqTier = Math.max(1, index);
                            if (tierSync.getIntValue() < reqTier) {
                                return GTGuiTextures.PICTURE_PLUS_RED;
                            }
                            return GuiTextures.ADD;
                        }
                        return new ItemDrawable(FoundryModule.values()[moduleSync.getIntValue()].getItemIcon());
                    }))
                    .onMousePressed(d -> {
                        int reqTier = Math.max(1, index);
                        if (tierSync.getIntValue() < reqTier) {
                            return true;
                        }
                        if (Interactable.hasShiftDown()) {
                            moduleSync.setIntValue(FoundryModule.UNSET.ordinal());
                        } else {
                            if (!selectPanel.isPanelOpen()) {
                                selectPanel.openPanel();
                            } else {
                                selectPanel.closePanel();
                                selectPanel.closeSubPanels();
                            }
                        }
                        return true;
                    })
                    .marginRight(2))
            .child(new TextWidget<>(IKey.dynamic(() -> {
                int reqTier = Math.max(1, index);
                if (tierSync.getIntValue() < reqTier) {
                    return EnumChatFormatting.WHITE + "N/A";
                }
                return EnumChatFormatting.WHITE + FoundryModule.values()[moduleSync.getIntValue()].shorthand;
            })).scale(0.5f)
                .size(20, 16));

    }

    private ModularPanel openModuleConfigPanel(ModularPanel parent, int index, IIntValue<Integer> moduleSync,
        boolean isStats) {
        ModularPanel panel = new ModularPanel("moduleSelectPanel" + index + (isStats ? "stats" : "")) {

            @Override
            public boolean disablePanelsBelow() {
                return true;
            }

            @Override
            public boolean closeOnOutOfBoundsClick() {
                return true;
            }
        };

        panel.relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(140, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Column().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new TextWidget<>("Select Module " + (index + 1)).size(80, 18)
                            .align(Alignment.TopCenter)
                            .marginBottom(2))
                    .child(
                        SlotGroupWidget.builder()
                            .row(" I I ")
                            .row("I   I")
                            .row("     ")
                            .row("I   I")
                            .row(" I I ")
                            .key(
                                'I',
                                i -> new ButtonWidget<>().size(18)
                                    .overlay(
                                        new ItemDrawable(
                                            Objects.requireNonNull(FoundryModule.values()[i].getItemIcon())))
                                    .tooltipBuilder(t -> createTooltipForModule(t, i))
                                    .onMouseTapped(mouseButton -> {
                                        moduleSync.setIntValue(i);
                                        panel.closeIfOpen();
                                        return true;
                                    }))
                            .build()
                            .topRel(0.5f)
                            .leftRel(0.4f)));
        return panel;
    }

    private void createTooltipForModule(RichTooltip t, int moduleIndex) {
        FoundryModule module = FoundryModule.values()[moduleIndex];
        String name = module.color + module.displayName;
        t.addLine(name);
        t.textColor(Color.GREY.main);
        switch (module) {
            case UNSET -> t.addLine("Empty");
            case POWER_EFFICIENT_SUBSYSTEMS -> {
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip1",
                        TooltipHelper.EFF_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip2",
                        TooltipHelper.EFF_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip3",
                        TooltipHelper.EFF_COLOR));
                t.addLine(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.powerefficient.tooltip4"));
            }
            case EXTRA_CASTING_BASINS -> {
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.parallel.tooltip1",
                        TooltipTier.VOLTAGE.getValue()));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.parallel.tooltip2"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.parallel.tooltip3",
                        TooltipTier.VOLTAGE.getValue()));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.parallel.tooltip4"));
            }
            case STREAMLINED_CASTERS -> {
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip1",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip2",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector
                        .translateToLocal("gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip3"));
            }
            case EFFICIENT_OC -> {
                t.addLine(createModuleLimitText());
                t.addLine(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip1"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.efficientoc.tooltip2",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip3"));
                t.addLine(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip4"));

            }
            case HYPERCOOLER -> {
                t.addLine(createModuleLimitText());
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip1"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.hypercooler.tooltip2",
                        coolingStrOrder("100", "50"),
                        coolingStrOrder("Super Coolant", "Spacetime"),
                        coolingStrOrder("1", "2")));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip3"));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip4"));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip5"));
                t.addLine(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip6"));

            }
            case HELIOCAST_REINFORCEMENT -> {
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip1"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip2"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip3"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip4",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip5",
                        TooltipHelper.EFF_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip6"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip7",
                        TooltipHelper.PARALLEL_COLOR,
                        TooltipHelper.TIER_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip8"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip9"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip10"));
            }
            case UNIVERSAL_COLLAPSER -> {
                t.addLine(createModuleLimitText());
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip1",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip2"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip3"));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip4",
                        TooltipHelper.SPEED_COLOR));
                t.addLine(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip5"));
            }
        }
        if (module != FoundryModule.UNSET) t.addLine(createTierLine(module.voltageTier));
    }

    protected Flow createModuleTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue moduleSync0 = syncManager.findSyncHandler("Module1", IntSyncValue.class);
        IntSyncValue moduleSync1 = syncManager.findSyncHandler("Module2", IntSyncValue.class);
        IntSyncValue moduleSync2 = syncManager.findSyncHandler("Module3", IntSyncValue.class);
        IntSyncValue moduleSync3 = syncManager.findSyncHandler("Module4", IntSyncValue.class);
        IntSyncValue tierSync = syncManager.findSyncHandler("Tier", IntSyncValue.class);

        return new Row().sizeRel(1)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .background(IDrawable.EMPTY)
            .child(createFoundryDisplay(syncManager))
            // module selecting
            .child(
                new Column().size(40, 80)
                    .background(IDrawable.EMPTY)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .padding(4, 4, 5, 5)
                    .child(createModuleSelectButton(syncManager, parent, 3, moduleSync3, tierSync, false))
                    .child(createModuleSelectButton(syncManager, parent, 2, moduleSync2, tierSync, false))
                    .child(createModuleSelectButton(syncManager, parent, 1, moduleSync1, tierSync, false))
                    .child(createModuleSelectButton(syncManager, parent, 0, moduleSync0, tierSync, false)))
            .child(createPairHoldingColumn(multiblock.foundryData, false).alignX(0.5f));
    }

    private Flow createPairHoldingColumn(FoundryData data, boolean hasBackground) {
        Flow column = Flow.column()
            .height(80)
            .width(22)
            .paddingTop(3)
            .marginTop(3)
            .background(hasBackground ? GTGuiTextures.BACKGROUND_GRAY_BORDER : IDrawable.EMPTY);
        column.child(
            new DynamicDrawable(
                () -> data.isProductionPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_ECB_SLC_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_ECB_SLC).asWidget()
                        .marginBottom(1)
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.optimumproduction.title"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.optimumproduction.required"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    data.isProductionPairPresent ? "GT5U.gui.text.foundry.pairings.activity.on"
                                        : "GT5U.gui.text.foundry.pairings.activity.off"));
                            t.addLine(
                                StatCollector
                                    .translateToLocalFormatted("GT5U.gui.text.foundry.pairings.activity.bonus"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.optimumproduction.tooltip1",
                                    TooltipHelper.SPEED_COLOR));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.optimumproduction.tooltip2",
                                    TooltipHelper.PARALLEL_COLOR,
                                    TooltipHelper.TIER_COLOR));
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isEfficiencyPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_PES_EOC_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_PES_EOC).asWidget()
                        .marginBottom(1)
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.harmonicefficiency.title"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.harmonicefficiency.required"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    data.isEfficiencyPairPresent ? "GT5U.gui.text.foundry.pairings.activity.on"
                                        : "GT5U.gui.text.foundry.pairings.activity.off"));
                            t.addLine(
                                StatCollector
                                    .translateToLocalFormatted("GT5U.gui.text.foundry.pairings.activity.bonus"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.harmonicefficiency.tooltip1"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.harmonicefficiency.tooltip2",
                                    TooltipHelper.EFF_COLOR));
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isHRPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_HR_SELF_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_HR_SELF).asWidget()
                        .marginBottom(1)
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(
                                StatCollector
                                    .translateToLocalFormatted("GT5U.gui.text.foundry.pairings.superstablecore.title"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.required"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    data.isHRPairPresent ? "GT5U.gui.text.foundry.pairings.activity.on"
                                        : "GT5U.gui.text.foundry.pairings.activity.off"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip1"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip2"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip3",
                                    TooltipHelper.SPEED_COLOR));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip4",
                                    TooltipHelper.EFF_COLOR));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip5"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip6",
                                    TooltipHelper.PARALLEL_COLOR,
                                    TooltipHelper.TIER_COLOR));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip7"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip8"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.superstablecore.tooltip9"));
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isEndPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_UC_HC_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_UC_HC).asWidget()
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.realizedpotential.title"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.realizedpotential.required"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    data.isEndPairPresent ? "GT5U.gui.text.foundry.pairings.activity.on"
                                        : "GT5U.gui.text.foundry.pairings.activity.off"));
                            t.addLine(
                                StatCollector
                                    .translateToLocalFormatted("GT5U.gui.text.foundry.pairings.activity.bonus"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.realizedpotential.tooltip1"));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.realizedpotential.tooltip2",
                                    TooltipHelper.SPEED_COLOR));
                            t.addLine(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.gui.text.foundry.pairings.realizedpotential.tooltip3"));

                        }));
        return column;
    }

    private ParentWidget<?> createFoundryDisplay(PanelSyncManager syncManager) {
        ParentWidget<?> parentWidget = new ParentWidget<>().coverChildren();
        IntSyncValue module1Sync = syncManager.findSyncHandler("Module1", IntSyncValue.class);
        IntSyncValue module2Sync = syncManager.findSyncHandler("Module2", IntSyncValue.class);
        IntSyncValue module3Sync = syncManager.findSyncHandler("Module3", IntSyncValue.class);
        IntSyncValue module4Sync = syncManager.findSyncHandler("Module4", IntSyncValue.class);
        parentWidget.child(
            GTGuiTextures.EXOFOUNDRY_BASE.asWidget()
                .size(59, 80)
                .marginRight(60))
            .child(
                new Column().size(59, 80)
                    .pos(0, 0)
                    .child(
                        new Column().size(59, 40)
                            .paddingTop(8)
                            // module 4 and 3
                            .child(
                                new Widget<>().size(27, 11)
                                    .marginBottom(3)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModule.values()[module4Sync.getIntValue()].texture)))
                            .child(
                                new Widget<>().size(27, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModule.values()[module3Sync.getIntValue()].texture))))
                    .child(
                        new Column().size(59, 40)
                            .paddingTop(7)
                            // module 2 and 1
                            .child(
                                new Widget<>().size(27, 11)
                                    .marginBottom(3)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModule.values()[module2Sync.getIntValue()].texture)))
                            .child(
                                new Widget<>().size(27, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModule.values()[module1Sync.getIntValue()].texture)))));
        return parentWidget;
    }
}
