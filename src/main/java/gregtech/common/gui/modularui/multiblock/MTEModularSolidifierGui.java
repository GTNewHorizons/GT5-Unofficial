package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Objects;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.solidifier.MTEModularSolidifier;
import gregtech.common.tileentities.machines.multi.solidifier.SolidifierModules;

public class MTEModularSolidifierGui extends MTEMultiBlockBaseGui {

    private final MTEModularSolidifier base;
    private final IItemHandlerModifiable itemHandler = new ItemStackHandler(8);

    public MTEModularSolidifierGui(MTEModularSolidifier base) {
        super(base);
        this.base = base;
        // manual init :P
        for (int i = 0; i < 8; i++) {
            itemHandler.setStackInSlot(
                i,
                SolidifierModules.getModule(i)
                    .getItemIcon());
        }
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        // all 4 module slots(enum values?) and values modified
        // values modified include: Parallels, Speed Bonus, Eu EFF, OC Factor.
        syncManager.syncValue("Speed", new StringSyncValue(base::getSpeedStr));
        syncManager.syncValue("Parallels", new StringSyncValue(base::getParallelsString));
        syncManager.syncValue("EuEFF", new StringSyncValue(base::getEuEFFString));
        syncManager.syncValue("OCFactor", new StringSyncValue(base::getOCFactorString));
        syncManager.syncValue(
            "Module1",
            new IntSyncValue(() -> base.getModuleSynced(0), ordinal -> base.setModule(0, ordinal)));
        syncManager.syncValue(
            "Module2",
            new IntSyncValue(() -> base.getModuleSynced(1), ordinal -> base.setModule(1, ordinal)));
        syncManager.syncValue(
            "Module3",
            new IntSyncValue(() -> base.getModuleSynced(2), ordinal -> base.setModule(2, ordinal)));
        syncManager.syncValue(
            "Module4",
            new IntSyncValue(() -> base.getModuleSynced(3), ordinal -> base.setModule(3, ordinal)));
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createPanelGap(parent, syncManager).child(createConfigButton(syncManager, parent));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    // 2 buttons on the panelGap, one opens stats info, other opens module config.
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
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        StringSyncValue speedSync = syncManager.findSyncHandler("Speed", StringSyncValue.class);
        StringSyncValue parallelSync = syncManager.findSyncHandler("Parallels", StringSyncValue.class);
        StringSyncValue euEffBaseSync = syncManager.findSyncHandler("EuEFF", StringSyncValue.class);
        StringSyncValue ocFactorSync = syncManager.findSyncHandler("OCFactor", StringSyncValue.class);
        return new ModularPanel("statsPanel").pos(x, y)
            .size(130, 120)
            .widgetTheme("backgroundPopup")
            .child(
                new Column().sizeRel(1)
                    .paddingTop(4)
                    .child(
                        new TextWidget<>("Stats").alignment(Alignment.TopCenter)
                            .height(9))
                    .widgetTheme("backgroundPopup")
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> EnumChatFormatting.DARK_PURPLE + "Speed: "
                                    + EnumChatFormatting.WHITE
                                    + speedSync.getValue())).size(120, 20)
                                        .marginBottom(2))
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> EnumChatFormatting.DARK_PURPLE + "Parallels Per Tier: "
                                    + EnumChatFormatting.WHITE
                                    + parallelSync.getValue())).size(120, 20)
                                        .marginBottom(2))
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> EnumChatFormatting.DARK_PURPLE + "EU Consumption: "
                                    + EnumChatFormatting.WHITE
                                    + euEffBaseSync.getValue())).size(120, 20)
                                        .marginBottom(2))
                    .child(
                        new TextWidget<>(
                            IKey.dynamic(
                                () -> EnumChatFormatting.DARK_PURPLE + "OC Factor: "
                                    + EnumChatFormatting.WHITE
                                    + ocFactorSync.getValue())).size(120, 20)
                                        .marginBottom(2)));

    }

    protected IWidget createConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        return new ButtonWidget<>().size(18, 18)
            .right(2 + 18 + 4)
            .marginTop(4)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                base.terminalSwitch = !base.terminalSwitch;
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
                        (IWidget) createTerminalTextWidget(syncManager, panel).collapseDisabledChild()
                            .setEnabledIf(widget -> !base.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8))
                    .child(
                        createModuleTerminalTextWidget(syncManager, panel).setEnabledIf(widget -> base.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild())
                    .childIf(multiblock.supportsTerminalCornerColumn(), createTerminalCornerColumn(panel, syncManager))

            );
    }

    protected IWidget createModuleSelectButton(PanelSyncManager syncManager, ModularPanel parent, int index) {
        IPanelHandler selectPanel = syncManager.panel(
            "moduleSelectPanel" + index,
            (p_syncManager, syncHandler) -> openModuleConfigPanel(p_syncManager, parent, syncManager, index),
            true);
        IntSyncValue moduleSync = syncManager.findSyncHandler("Module" + (index + 1), IntSyncValue.class);
        return new Row().size(30, 16)
            .marginBottom(index != 0 ? 2 : 0)
            .child(
                new ButtonWidget<>().size(16, 16)
                    .tooltipBuilder(
                        t -> t.addLine("Select Module " + (index + 1))
                            .addLine(SolidifierModules.getModule(moduleSync.getIntValue()).displayName)
                            .setAutoUpdate(true))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .overlay(GuiTextures.ADD)
                    .onMousePressed(d -> {
                        if (!selectPanel.isPanelOpen()) {
                            selectPanel.openPanel();
                        } else {
                            selectPanel.closePanel();
                        }
                        return true;
                    })
                    .marginRight(2))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE
                            + SolidifierModules.getModule(moduleSync.getIntValue()).shorthand)).scale(0.5f)
                                .size(20, 16));

    }

    private ModularPanel openModuleConfigPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager, int index) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        IntSyncValue moduleSync = syncManager.findSyncHandler("Module" + (index + 1), IntSyncValue.class);
        return new ModularPanel("moduleSelectPanel" + index).pos(x, y)
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
                                        new ItemDrawable(Objects.requireNonNull(this.itemHandler.getStackInSlot(i))))
                                    .tooltipBuilder(
                                        t -> t.add(SolidifierModules.getModule(i).displayName)
                                            .newLine())
                                    .onMouseTapped(mouseButton -> {
                                        moduleSync.setIntValue(i);
                                        return true;
                                    }))
                            .build()
                            .topRel(0.5f)
                            .leftRel(0.4f)));
    }

    protected Flow createModuleTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue module1Sync = syncManager.findSyncHandler("Module1", IntSyncValue.class);
        IntSyncValue module2Sync = syncManager.findSyncHandler("Module2", IntSyncValue.class);
        IntSyncValue module3Sync = syncManager.findSyncHandler("Module3", IntSyncValue.class);
        IntSyncValue module4Sync = syncManager.findSyncHandler("Module4", IntSyncValue.class);
        return new Row().sizeRel(1)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .background(IDrawable.EMPTY)
            .child(
                GTGuiTextures.MODULAR_SOLIDIFIER_BASE.asWidget()
                    .size(60, 80)
                    .marginRight(60))
            .child(
                new Column().size(60, 80)
                    .pos(0, 0)

                    .child(
                        new Column().size(60, 40)
                            .paddingTop(17)
                            // module 4 and 3
                            .child(
                                new Widget<>().size(28, 11)
                                    .marginBottom(1)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> SolidifierModules.getModule(module4Sync.getIntValue()).texture)))
                            .child(
                                new Widget<>().size(28, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> SolidifierModules.getModule(module3Sync.getIntValue()).texture)))

                    )

                    .child(
                        new Column().size(60, 40)
                            .paddingTop(6)
                            // module 2 and 1
                            .child(
                                new Widget<>().size(28, 11)
                                    .marginBottom(1)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> SolidifierModules.getModule(module2Sync.getIntValue()).texture)))
                            .child(
                                new Widget<>().size(28, 11)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> SolidifierModules.getModule(module1Sync.getIntValue()).texture))))

            )
            // module selecting
            .child(
                new Column().size(40, 80)
                    .background(IDrawable.EMPTY)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .padding(4, 4, 5, 5)
                    .child(createModuleSelectButton(syncManager, parent, 3))
                    .child(createModuleSelectButton(syncManager, parent, 2))
                    .child(createModuleSelectButton(syncManager, parent, 1))
                    .child(createModuleSelectButton(syncManager, parent, 0)))

        ;

    }
}
