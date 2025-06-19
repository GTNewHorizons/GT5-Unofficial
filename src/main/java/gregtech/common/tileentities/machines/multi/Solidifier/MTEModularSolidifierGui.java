package gregtech.common.tileentities.machines.multi.Solidifier;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import net.minecraft.util.StatCollector;

public class MTEModularSolidifierGui extends MTEMultiBlockBaseGui {

    private final MTEModularSolidifier base;

    public MTEModularSolidifierGui(MTEModularSolidifier base) {
        super(base);
        this.base = base;
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
        syncManager.syncValue("uevActive", new StringSyncValue(base::getTrReStatus));
        syncManager.syncValue("hypercoolerActive", new StringSyncValue(base::getHCStatus));

    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        /*return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager))
            .child(createConfigButton(syncManager, parent))
            .child(createOverviewButton(syncManager, parent))
            .childIf(!machineModeIcons.isEmpty(), createModeSwitchButton(syncManager))
            .child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))

            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));*/
        return super.createPanelGap(parent,syncManager)
            .child(createConfigButton(syncManager, parent));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager)
            .child(createOverviewButton(syncManager,panel));
    }

    // 2 buttons on the panelGap, one opens stats info, other opens module config.
    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager // calls the panel itself.
            .panel(
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
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.centrifugestatsmenu")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openInfoPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
         StringSyncValue speedSync = (StringSyncValue) syncManager.getSyncHandler("Speed:0");
         StringSyncValue parallelSync = (StringSyncValue) syncManager.getSyncHandler("Parallels:0");
         StringSyncValue euEffBaseSync = (StringSyncValue) syncManager.getSyncHandler("EuEFF:0");
         StringSyncValue ocFactorSync = (StringSyncValue) syncManager.getSyncHandler("OCFactor:0");
         StringSyncValue trReSync = (StringSyncValue) syncManager.getSyncHandler("uevActive:0");
         StringSyncValue hcSync = (StringSyncValue) syncManager.getSyncHandler("hypercoolerActive:0");
        return new ModularPanel("statsPanel").pos(x, y)
            .size(160, 140)
            .widgetTheme("backgroundPopup")
            .child(
                new Column().sizeRel(1)
                    .child(
                        new TextWidget("Stats").size(60, 18)
                            .alignment(Alignment.Center))
                    .widgetTheme("backgroundPopup")
                      .child(new TextWidget(IKey.dynamic(() -> "Speed Bonus: " + speedSync.getValue())).size(150, 20))
                      .child(new TextWidget(IKey.dynamic(() -> "Parallels Per Tier: " + parallelSync.getValue())).size(150, 20))
                            .child(new TextWidget(IKey.dynamic(() -> "EU Consumption: " + euEffBaseSync.getValue())).size(150, 20))
                            .child(new TextWidget(IKey.dynamic(() -> "OC Factor: " + ocFactorSync.getValue())).size(150, 20))
                            .child(new TextWidget(IKey.dynamic(() -> "Transcendent Reinforcement Installed: " + trReSync.getValue())).size(150, 20))
                            .child(new TextWidget(IKey.dynamic(() -> "Hypercooler Installed: " + hcSync.getValue())).size(150, 20))





                    );

    }

    protected IWidget createConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler moduleConfigPanel = syncManager // calls the panel itself.
            .panel(
                "moduleConfigPanel",
                (p_syncManager, syncHandler) -> openModuleConfigPanel(p_syncManager, parent, syncManager),
                true);
        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 28, 0)
            .marginTop(4)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                base.terminalSwitch = !base.terminalSwitch;
                if (!moduleConfigPanel.isPanelOpen()) {
                    moduleConfigPanel.openPanel();
                } else {
                    moduleConfigPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.turbinemenu")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .padding(4)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child( base.terminalSwitch ?
                        createModuleTerminalTextWidget(syncManager,panel) .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                        .collapseDisabledChild().child(
                                new SingleChildWidget<>().bottomRel(0, 10, 0)
                                    .rightRel(0, 10, 0)
                                    .size(18, 18)
                                    .widgetTheme(GTWidgetThemes.PICTURE_LOGO))
                        :
                        createTerminalTextWidget(syncManager, panel)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild())
                    );
    }
    private ModularPanel openModuleConfigPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        return new ModularPanel("moduleConfigPanel").pos(x, y)
            .size(140, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Column().sizeRel(1)
                    .padding(3)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new TextWidget("Modules").size(60, 18)
                            .alignment(Alignment.Center)
                            .marginBottom(5))

            /* .child( ) .build()) */ // here will be the modules and what name
            // to the right of them should be a button that opens a new panel to select modules

            );
    }
    protected ListWidget<IWidget, ?> createModuleTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new ListWidget<>()
            .child(
                new TextWidget("bello")
                    .marginBottom(2)
                    .widthRel(1));

    }
}
