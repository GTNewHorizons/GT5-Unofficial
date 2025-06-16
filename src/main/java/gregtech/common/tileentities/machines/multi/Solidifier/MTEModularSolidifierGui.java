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
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;

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
    }

    @Override
    protected IWidget createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
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

            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
    }

    // 2 buttons on the panelGap, one opens stats info, other opens module config.
    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager // calls the panel itself.
            .panel(
                "statsPanel",
                (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager),
                true);
        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 28, 0)
            .marginTop(4)
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
        // IntSyncValue RPSync = (IntSyncValue) syncManager.getSyncHandler("RP:0");
        // IntSyncValue paraSync = (IntSyncValue) syncManager.getSyncHandler("Parallels:0");
        // StringSyncValue speedSync = (StringSyncValue) syncManager.getSyncHandler("Speed:0");
        // StringSyncValue typeSTRSync = (StringSyncValue) syncManager.getSyncHandler("modeString:0");
        // DoubleSyncValue typeVALSync = (DoubleSyncValue) syncManager.getSyncHandler("modeValue:0");
        return new ModularPanel("statsPanel").pos(x, y)
            .size(160, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new Column().size(140, 120)
                            .paddingRight(40)
                    /*
                     * .child(new TextWidget(IKey.dynamic(() -> "Mode: " + typeSTRSync.getValue())).size(80, 20))
                     * .child(
                     * new TextWidget(IKey.dynamic(() -> "Speed Bonus: " + speedSync.getValue())).size(80, 20))
                     * .child(
                     * new TextWidget(IKey.dynamic(() -> "Rotational Power: " + RPSync.getValue()))
                     * .size(80, 20))
                     * .child(
                     * new TextWidget(IKey.dynamic(() -> "Parallels: " + paraSync.getValue())).size(80, 20)))
                     */

                    ));

    }

    protected IWidget createConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler moduleConfigPanel = syncManager // calls the panel itself.
            .panel(
                "moduleConfigPanel",
                (p_syncManager, syncHandler) -> openModuleConfigPanel(p_syncManager, parent, syncManager),
                true);
        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 6, 0)
            .marginTop(4)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
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

    private ModularPanel openModuleConfigPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        return new ModularPanel("moduleConfigPanel").pos(x, y)
            .size(160, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .padding(3)
                    .widgetTheme("backgroundPopup")

                    .child( // column that displays the modules
                        new Column().size(80, 120)
                            .padding(1)
                            .widgetTheme("backgroundPopup")
                            .marginRight(20)
                            .child(
                                new TextWidget("Modules").size(60, 18)
                                    .alignment(Alignment.Center)
                                    .marginBottom(5))
                    /* .child( ) .build()) */ // here will be the modules and what name
                    // to the right of them should be a button that opens a new panel to select modules

                    )

                    .crossAxisAlignment(Alignment.CrossAxis.START)

            );
    }
}
