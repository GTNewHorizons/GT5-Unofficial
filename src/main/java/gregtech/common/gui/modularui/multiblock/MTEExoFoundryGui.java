package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.GTValues.formattingCodes;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Arrays;
import java.util.Objects;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
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
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.foundry.FoundryData;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModules;
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
            usingPreviewSync.setBoolValue(!calculatorData.areModulesEqual(multiblock.foundryData));
        }));
        syncManager.syncValue("Module2Calc", new IntSyncValue(() -> calculatorData.modules[1].ordinal(), val -> {
            calculatorData.setModule(1, val);
            usingPreviewSync.setBoolValue(!calculatorData.areModulesEqual(multiblock.foundryData));
        }));
        syncManager.syncValue("Module3Calc", new IntSyncValue(() -> calculatorData.modules[2].ordinal(), val -> {
            calculatorData.setModule(2, val);
            usingPreviewSync.setBoolValue(!calculatorData.areModulesEqual(multiblock.foundryData));
        }));
        syncManager.syncValue("Module4Calc", new IntSyncValue(() -> calculatorData.modules[3].ordinal(), val -> {
            calculatorData.setModule(3, val);
            usingPreviewSync.setBoolValue(!calculatorData.areModulesEqual(multiblock.foundryData));
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

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler contribPanel = syncManager
            .panel("contributorsPanel", (p_syncManager, syncHandler) -> openContributorsPanel(parent), true);
        return new ButtonWidget<>().size(18)
            .marginTop(4)
            .overlay(IDrawable.EMPTY)
            .tooltip(
                t -> t.addLine(EnumChatFormatting.AQUA + translateToLocal("GT5U.gui.button.foundry.contributorpanel")))
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
            IKey.lang("gt.blockmachines.multimachine.FOG.contributors")
                .asWidget()
                .style(EnumChatFormatting.GOLD)
                .marginTop(8)
                .align(Alignment.TopCenter))
            .child(ButtonWidget.panelCloseButton());

        Flow contributorColumn = Flow.column()
            .coverChildren()
            .marginLeft(14)
            .marginTop(24);

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.projectlead",
                createContributorEntry("Chrom", Color.PURPLE.brighterSafe(2))));
        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.programming",
                createContributorEntry("Serenibyss", Color.PURPLE.brighterSafe(2))));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.textures",
                createContributorEntry("Auynonymous", 0xFFFD80CF).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "Dev my foundry...")),
                createContributorEntry("June", Color.PINK_ACCENT.main).tooltip(
                    t -> t.scale(0.8f)
                        .addLine(EnumChatFormatting.DARK_GRAY + "I Eat Lawnbasers"))));

        contributorColumn.child(
            createContributorSection(
                "GT5U.gui.text.foundry.rendering",
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
                "GT5U.gui.text.foundry.playtest",
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
                        if (usingPreviewSync.getBoolValue()) {
                            return EnumChatFormatting.RED + "Showing Preview Modules";
                        }
                        return EnumChatFormatting.GREEN + "Showing Installed Modules";
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
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .overlay(new DynamicDrawable(() -> {
                        // todo make red when required tier is not available
                        if (moduleSync.getIntValue() == FoundryModules.UNSET.ordinal()) {
                            return GuiTextures.ADD;
                        }
                        return new ItemDrawable(
                            FoundryModules.getModule(moduleSync.getIntValue())
                                .getItemIcon());
                    }))
                    .onMousePressed(d -> {
                        int reqTier = Math.max(1, index);
                        if (tierSync.getIntValue() < reqTier) {
                            return true;
                        }
                        if (Interactable.hasShiftDown()) {
                            moduleSync.setIntValue(FoundryModules.UNSET.ordinal());
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
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + FoundryModules.getModule(moduleSync.getIntValue()).shorthand))
                            .scale(0.5f)
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
                                            Objects.requireNonNull(FoundryModules.values()[i].getItemIcon())))
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
        FoundryModules module = FoundryModules.getModule(moduleIndex);
        String name = module.color + module.displayName;
        t.addLine(name);
        t.textColor(Color.WHITE.main);
        switch (module) {
            case UNSET -> t.addLine("Empty");
            case POWER_EFFICIENT_SUBSYSTEMS -> {
                t.addLine(
                    "Subtracts " + TooltipHelper.EFF_COLOR
                        + "10%"
                        + EnumChatFormatting.RESET
                        + " from Initial EU Cost");
                t.addLine("Multiplies EU cost by " + TooltipHelper.EFF_COLOR + "0.8x");
            }
            case EXTRA_CASTING_BASINS -> {
                t.addLine(
                    "Adds " + TooltipHelper.PARALLEL_COLOR
                        + "12"
                        + EnumChatFormatting.RESET
                        + " Parallels per "
                        + TooltipTier.VOLTAGE.getValue()
                        + EnumChatFormatting.RESET
                        + " tier");
                t.addLine("Increases Hatch Space by " + EnumChatFormatting.GOLD + "36");
            }
            case STREAMLINED_CASTERS -> {
                t.addLine("Increases Base Speed by " + TooltipHelper.SPEED_COLOR + "150%");
            }
            case EFFICIENT_OC -> {
                t.addLine(moduleLimitText);
                t.addLine("Increases Overclock Factor by " + EnumChatFormatting.LIGHT_PURPLE + "0.25");
            }
            case HYPERCOOLER -> {
                t.addLine(moduleLimitText);
                t.addLine(
                    "Consumes " + EnumChatFormatting.AQUA
                        + "Cooling Fluid"
                        + EnumChatFormatting.RESET
                        + " for "
                        + EnumChatFormatting.LIGHT_PURPLE
                        + "Extra Overclocks"
                        + EnumChatFormatting.RESET);
                t.addLine(
                    "Drains " + coolingStrOrder("100", "50", "25")
                        + " L/s of "
                        + coolingStrOrder("Super Coolant", "Spacetime", "Eternity")
                        + " to gain "
                        + coolingStrOrder("1", "2", "3")
                        + " Maximum Overclocks");
                t.addLine(
                    "Only drains " + EnumChatFormatting.AQUA
                        + "cooling fluid"
                        + EnumChatFormatting.GRAY
                        + " when the "
                        + EnumChatFormatting.GOLD
                        + "Foundry"
                        + EnumChatFormatting.GRAY
                        + " is active");
                t.addLine(
                    EnumChatFormatting.DARK_AQUA + "Requires an input hatch on any Hypercooler Casing to drain from!");

            }
            case HELIOCAST_REINFORCEMENT -> {
                t.addLine(
                    "Allows for " + EnumChatFormatting.LIGHT_PURPLE
                        + "UIV+ Recipes"
                        + EnumChatFormatting.RESET
                        + " to be processed");
            }
            case UNIVERSAL_COLLAPSER -> {
                t.addLine(moduleLimitText);
                t.addLine("Multiplies Speed by " + TooltipHelper.SPEED_COLOR + "4x");
                t.addLine("Multiplies EU Consumption by " + EnumChatFormatting.RED + "8x");
                t.addLine("Reduces Hatch Space by " + EnumChatFormatting.GOLD + "20");
            }
        }
        if (module != FoundryModules.UNSET) t.addLine(createTierLine(module.voltageTier));
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

                            t.addLine(EnumChatFormatting.AQUA + "Optimum Production");
                            t.addLine(
                                "This pairing is " + (data.isProductionPairPresent ? EnumChatFormatting.GREEN + "active"
                                    : EnumChatFormatting.RED + "inactive"));
                            t.addLine("When active, this pairing provides the following bonuses:");
                            t.addLine(TooltipHelper.SPEED_COLOR + "+75%" + EnumChatFormatting.RESET + " speed");
                            t.addLine(
                                TooltipHelper.PARALLEL_COLOR + "+6"
                                    + EnumChatFormatting.RESET
                                    + " parallels per "
                                    + EnumChatFormatting.WHITE
                                    + "Voltage"
                                    + EnumChatFormatting.RESET
                                    + " tier");
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isEfficiencyPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_PES_EOC_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_PES_EOC).asWidget()
                        .marginBottom(1)
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(EnumChatFormatting.GREEN + "Harmonic Efficiency");
                            t.addLine(
                                "This pairing is " + (data.isEfficiencyPairPresent ? EnumChatFormatting.GREEN + "active"
                                    : EnumChatFormatting.RED + "inactive"));
                            t.addLine("When active, this pairing provides the following bonuses:");
                            t.addLine(
                                EnumChatFormatting.LIGHT_PURPLE + "+0.1" + EnumChatFormatting.RESET + " OC Factor");
                            t.addLine(TooltipHelper.EFF_COLOR + "-50%" + EnumChatFormatting.RESET + " Initial EU Cost");
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isHRPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_HR_SELF_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_HR_SELF).asWidget()
                        .marginBottom(1)
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {
                            t.addLine(EnumChatFormatting.LIGHT_PURPLE + "Superstable Core");
                            t.addLine(
                                "This pairing is " + (data.isHRPairPresent ? EnumChatFormatting.GREEN + "active"
                                    : EnumChatFormatting.RED + "inactive"));
                            t.addLine(
                                "This pairings bonuses scale with the amount of " + EnumChatFormatting.LIGHT_PURPLE
                                    + "Heliocast Reinforcement"
                                    + EnumChatFormatting.RESET
                                    + " Modules");
                            t.addLine(
                                EnumChatFormatting.WHITE + "When 2 or more"
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + " Heliocast Reinforcement"
                                    + EnumChatFormatting.WHITE
                                    + " modules are present:");
                            t.addLine(
                                "Increases Base Speed by " + TooltipHelper.SPEED_COLOR
                                    + "75%"
                                    + EnumChatFormatting.RESET
                                    + " per module");
                            t.addLine(
                                "Subtracts " + TooltipHelper.EFF_COLOR
                                    + "10%"
                                    + EnumChatFormatting.RESET
                                    + " from Initial EU Cost per module");
                            t.addLine(
                                EnumChatFormatting.WHITE + "When 3 or more "
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + "Heliocast Reinforcement"
                                    + EnumChatFormatting.WHITE
                                    + " modules are present:");
                            t.addLine(
                                "Adds " + TooltipHelper.PARALLEL_COLOR
                                    + "6"
                                    + EnumChatFormatting.RESET
                                    + " parallels per "
                                    + TooltipHelper.TIER_COLOR
                                    + "Voltage"
                                    + EnumChatFormatting.RESET
                                    + " Tier per module");
                            t.addLine("   Increases Overclock Factor by " + EnumChatFormatting.LIGHT_PURPLE + "0.1");
                            t.addLine(
                                EnumChatFormatting.WHITE + "When 4 "
                                    + EnumChatFormatting.LIGHT_PURPLE
                                    + "Heliocast Reinforcement"
                                    + EnumChatFormatting.WHITE
                                    + " modules are present:");
                            t.addLine("   Grants 2 maximum " + EnumChatFormatting.LIGHT_PURPLE + "overclocks");
                        }));
        column.child(
            new DynamicDrawable(
                () -> data.isEndPairPresent ? GTGuiTextures.EXOFOUNDRY_PAIR_UC_HC_ACTIVE
                    : GTGuiTextures.EXOFOUNDRY_PAIR_UC_HC).asWidget()
                        .tooltipTextColor(Color.GREY.main)
                        .tooltipAutoUpdate(true)
                        .tooltipDynamic(t -> {

                            t.addLine(
                                EnumChatFormatting.DARK_PURPLE.toString() + EnumChatFormatting.OBFUSCATED
                                    + "0"
                                    + EnumChatFormatting.RESET
                                    + EnumChatFormatting.DARK_RED
                                    + "Realized Potential"
                                    + EnumChatFormatting.DARK_PURPLE
                                    + EnumChatFormatting.OBFUSCATED
                                    + "0");
                            t.addLine(
                                "This pairing is " + (data.isEndPairPresent ? EnumChatFormatting.GREEN + "active!"
                                    : EnumChatFormatting.RED + "inactive."));
                            t.addLine("When active, this pairing provides the following bonuses:");
                            t.addLine(EnumChatFormatting.RED + "2x" + EnumChatFormatting.RESET + " EU Cost");
                            t.addLine(TooltipHelper.SPEED_COLOR + "2x" + EnumChatFormatting.RESET + " Speed");
                            t.addLine(
                                "Allows the " + EnumChatFormatting.AQUA
                                    + "Hypercooler"
                                    + EnumChatFormatting.RESET
                                    + " to utilize Eternity");
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
                                            () -> FoundryModules.getModule(module4Sync.getIntValue()).texture)))
                            .child(
                                new Widget<>().size(27, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModules.getModule(module3Sync.getIntValue()).texture))))
                    .child(
                        new Column().size(59, 40)
                            .paddingTop(7)
                            // module 2 and 1
                            .child(
                                new Widget<>().size(27, 11)
                                    .marginBottom(3)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModules.getModule(module2Sync.getIntValue()).texture)))
                            .child(
                                new Widget<>().size(27, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> FoundryModules.getModule(module1Sync.getIntValue()).texture)))));
        return parentWidget;
    }

    // copied methods so I can avoid a public static in MTEExoFoundry
    private String coolingStrOrder(String val1, String val2, String val3) {
        return EnumChatFormatting.BLUE + val1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + val2
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.GREEN
            + val3
            + EnumChatFormatting.RESET;
    }

    private String createTierLine(int tier) {
        return "Tier: " + GTUtility.getColoredTierNameFromTier((byte) tier);
    }

    private final static String moduleLimitText = "Limit of " + EnumChatFormatting.WHITE
        + "1"
        + EnumChatFormatting.RESET
        + " Per "
        + EnumChatFormatting.GOLD
        + "Foundry";
}
