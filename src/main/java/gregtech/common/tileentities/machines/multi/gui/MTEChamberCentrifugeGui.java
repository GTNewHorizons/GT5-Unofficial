package gregtech.common.tileentities.machines.multi.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTEChamberCentrifuge;

public class MTEChamberCentrifugeGui extends MTEMultiBlockBaseGui {

    private final MTEChamberCentrifuge base;

    public MTEChamberCentrifugeGui(MTEChamberCentrifuge base) {
        super(base);
        this.base = base;
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
            .childIf(!machineModeIcons.isEmpty(), createModeSwitchButton(syncManager))
            .child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))

            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
    }

    protected IWidget createConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler turbinePanel = syncManager // calls the panel itself.
            .panel("turbinePanel", (p_syncManager, syncHandler) -> openTurbinePanel(p_syncManager, parent), true);
        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 28, 0)
            .marginTop(4)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/cyclic"))
            .onMousePressed(d -> {
                if (!turbinePanel.isPanelOpen()) {
                    turbinePanel.openPanel();
                } else {
                    turbinePanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.turbinemenu")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openTurbinePanel(PanelSyncManager syncManager, ModularPanel parent) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        return new ModularPanel("turbinePanel").pos(x, y)
            .size(160, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .padding(3)
                    .widgetTheme("backgroundPopup")

                    .child( // column that holds the turbines
                        new Column().size(80, 120)
                            .padding(1)
                            .widgetTheme("backgroundPopup")
                            .marginRight(20)
                            .child(
                                new TextWidget("Turbines").size(60, 18)
                                    .alignment(Alignment.Center)
                                    .marginBottom(5))
                            .child(
                                SlotGroupWidget.builder()
                                    .matrix("II", "II", "II", "II")
                                    .key(
                                        'I',
                                        index -> {
                                            return new ItemSlot().slot(
                                                new ModularSlot(base.inventoryHandler, index)
                                                    .filter(base::isTurbine));
                                        })
                                    .build())

                    )

                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child( // column that holds a button to toggle Tier1Fluid
                        new Column().size(50, 60)
                            .padding(x = 0, y = 3)
                            .widgetTheme("backgroundPopup")
                            .child(
                                new TextWidget("T2\nFluid").size(40, 18)
                                    .alignment(Alignment.Center))
                            .marginBottom(5)
                            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
                            .child(
                                new ToggleButton()
                                    .value(new BooleanSyncValue(() -> base.tier2Fluid, bool -> base.tier2Fluid = bool))
                                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF))

                    ));

    }

}
