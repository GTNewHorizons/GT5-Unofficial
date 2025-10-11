package gregtech.common.tileentities.machines.multi.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
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
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("RP", new IntSyncValue(base::getRP));
        syncManager.syncValue("Parallels", new IntSyncValue(base::getTrueParallel));
        syncManager.syncValue("Speed", new StringSyncValue(base::getSpeedStr));
        syncManager.syncValue("modeString", new StringSyncValue(base::modeToString));
        syncManager.syncValue("modeValue", new DoubleSyncValue(() -> base.mMode, dub -> base.mMode = dub));
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createPanelGap(parent, syncManager).child(createConfigButton(syncManager, parent));
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
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        IntSyncValue RPSync = (IntSyncValue) syncManager.getSyncHandler("RP:0");
        IntSyncValue paraSync = (IntSyncValue) syncManager.getSyncHandler("Parallels:0");
        StringSyncValue speedSync = (StringSyncValue) syncManager.getSyncHandler("Speed:0");
        StringSyncValue typeSTRSync = (StringSyncValue) syncManager.getSyncHandler("modeString:0");
        DoubleSyncValue typeVALSync = (DoubleSyncValue) syncManager.getSyncHandler("modeValue:0");
        return new ModularPanel("statsPanel").pos(x, y)
            .size(160, 130)
            .widgetTheme("backgroundPopup")
            .child(
                new Row().sizeRel(1)
                    .widgetTheme("backgroundPopup")
                    .child(
                        new Column().size(100, 120)
                            .paddingRight(40)
                            .child(new TextWidget<>(IKey.dynamic(() -> "Mode: " + typeSTRSync.getValue())).size(80, 20))
                            .child(new TextWidget<>(IKey.dynamic(() -> "Speed: " + speedSync.getValue())).size(80, 20))
                            .child(
                                new TextWidget<>(IKey.dynamic(() -> "Rotational Power: " + RPSync.getValue()))
                                    .size(80, 20))
                            .child(
                                new TextWidget<>(IKey.dynamic(() -> "Parallels: " + paraSync.getValue())).size(80, 20)))
                    .child(
                        new SliderWidget().size(15, 110)
                            .background(GuiTextures.MC_BUTTON)

                            .setAxis(GuiAxis.Y)
                            .stopper(0, 1, 2)
                            .bounds(0, 2)
                            .sliderSize(9, 20)

                            .value(typeVALSync))

            );

    }

    protected IWidget createConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler turbinePanel = syncManager // calls the panel itself.
            .panel("turbinePanel", (p_syncManager, syncHandler) -> openTurbinePanel(p_syncManager, parent), true);
        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 28, 0)
            .marginTop(4)
            .overlay(GuiTextures.GEAR)
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
                                new TextWidget<>("Turbines").size(60, 18)
                                    .alignment(Alignment.Center)
                                    .marginBottom(5))
                            .child(
                                SlotGroupWidget.builder()
                                    .matrix("II", "II", "II", "II")
                                    .key(
                                        'I',
                                        index -> {
                                            return new ItemSlot().slot(
                                                new ModularSlot(base.turbineHolder, index).filter(base::isTurbine));
                                        })
                                    .build())

                    )

                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child( // column that holds a button to toggle Tier1Fluid
                        new Column().size(50, 60)
                            .padding(x = 0, y = 3)
                            .widgetTheme("backgroundPopup")
                            .child(
                                new TextWidget<>("T2\nFluid").size(40, 18)
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
