package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamStabilizer;

public class MTEBeamStabilizerGui extends MTEMultiBlockBaseGui<MTEBeamStabilizer> {

    public MTEBeamStabilizerGui(MTEBeamStabilizer multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "playerTargetBeamRate",
            new IntSyncValue(() -> multiblock.playerTargetBeamRate, i -> multiblock.playerTargetBeamRate = i));
        syncManager.syncValue("inputBeamRate", new IntSyncValue(multiblock::getCachedBeamRate));
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
        IntSyncValue playerTargetBeamRateSync = (IntSyncValue) syncManager
            .getSyncHandlerFromMapKey("playerTargetBeamRate:0");

        return new ModularPanel("statsPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(180, 110)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new Column().size(160, 60)
                            .paddingLeft(40)
                            .child(
                                new TextWidget<>(IKey.dynamic(() -> "Target Beam Rate:")).size(160, 20)
                                    .alignment(Alignment.CENTER))
                            .child(
                                new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                                    .setNumbersLong(() -> 1L, () -> Long.MAX_VALUE)
                                    .width(120)
                                    .height(14)
                                    .marginRight(2)
                                    .value(playerTargetBeamRateSync)
                                    .setDefaultNumber(100))));
    }

}
