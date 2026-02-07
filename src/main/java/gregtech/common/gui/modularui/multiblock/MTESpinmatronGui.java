package gregtech.common.gui.modularui.multiblock;

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

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTESpinmatron;

public class MTESpinmatronGui extends MTEMultiBlockBaseGui<MTESpinmatron> {

    public MTESpinmatronGui(MTESpinmatron multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("RP", new IntSyncValue(multiblock::getRP));
        syncManager.syncValue("Parallels", new IntSyncValue(multiblock::getTrueParallel));
        syncManager.syncValue("Speed", new StringSyncValue(multiblock::getSpeedStr));
        syncManager.syncValue("modeString", new StringSyncValue(multiblock::modeToString));
        syncManager.syncValue("modeValue", new DoubleSyncValue(() -> multiblock.mode, dub -> multiblock.mode = dub));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createConfigButton(syncManager, parent));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.syncedPanel(
            "statsPanel", true,
            (p_syncManager, syncHandler) -> openInfoPanel(p_syncManager, parent, syncManager));
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

    private ModularPanel openInfoPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {
        IntSyncValue RPSync = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("RP:0");
        IntSyncValue paraSync = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("Parallels:0");
        StringSyncValue speedSync = (StringSyncValue) syncManager.getSyncHandlerFromMapKey("Speed:0");
        StringSyncValue typeSTRSync = (StringSyncValue) syncManager.getSyncHandlerFromMapKey("modeString:0");
        DoubleSyncValue typeVALSync = (DoubleSyncValue) syncManager.getSyncHandlerFromMapKey("modeValue:0");
        return new ModularPanel("statsPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
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
            .syncedPanel("turbinePanel", true, (p_syncManager, syncHandler) -> openTurbinePanel(p_syncManager, parent));
        return new ButtonWidget<>().size(18, 18)
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
        return new ModularPanel("turbinePanel").relative(parent)
            .leftRel(1)
            .topRel(0)
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
                                    .key('I', index -> {
                                        return new ItemSlot().slot(
                                            new ModularSlot(multiblock.turbineHolder, index)
                                                .singletonSlotGroup(50 + index)
                                                .filter(multiblock::isTurbine));
                                    })
                                    .build())

                    )

                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child( // column that holds a button to toggle Tier1Fluid
                        new Column().size(50, 60)
                            .padding(0, 3)
                            .widgetTheme("backgroundPopup")
                            .child(
                                new TextWidget<>("T2\nFluid").size(40, 18)
                                    .alignment(Alignment.Center))
                            .marginBottom(5)
                            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
                            .child(
                                new ToggleButton()
                                    .value(
                                        new BooleanSyncValue(
                                            () -> multiblock.tier2Fluid,
                                            bool -> multiblock.tier2Fluid = bool))
                                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF))

                    ));

    }

}
