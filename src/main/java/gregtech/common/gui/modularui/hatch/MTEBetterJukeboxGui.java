package gregtech.common.gui.modularui.hatch;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.List;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gregtech.common.gui.modularui.util.MachineModularSlot;
import gregtech.common.tileentities.machines.basic.MTEBetterJukebox;

public class MTEBetterJukeboxGui extends MTEBasicMachineBaseGui<MTEBetterJukebox> {

    private static final int INPUT_WIDTH = 7;
    private static final int INPUT_HEIGHT = 3;

    public MTEBetterJukeboxGui(MTEBetterJukebox machine, BasicUIProperties properties) {
        super(machine, properties);
        useGregTechLogo(true);
    }

    @Override
    protected Flow createItemRecipeArea(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .horizontalCenter()
            .child(createItemInputSlots(panel, syncManager))
            .child(createProgressBar(panel, syncManager))
            .child(createItemOutputSlots(panel, syncManager));
    }

    @Override
    protected ParentWidget<?> createItemInputSlots(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue selectedIndexSyncer = new IntSyncValue(machine::getPlaybackSlot);
        syncManager.syncValue("selectedIndex", selectedIndexSyncer);
        syncManager.registerSlotGroup("item_inv", INPUT_HEIGHT);

        Grid grid = new Grid().coverChildren()
            .gridOfWidthHeight(
                INPUT_WIDTH,
                INPUT_HEIGHT,
                (_, _, i) -> new ItemSlot().slot(
                    new MachineModularSlot(machine.inventoryHandler, machine.getInputSlot() + i, baseMetaTileEntity)
                        .slotGroup("item_inv")));

        selectedIndexSyncer.changeListener(() -> {
            int selectedIndex = selectedIndexSyncer.getIntValue();
            List<IWidget> children = grid.getChildren();

            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) instanceof ItemSlot slot) {
                    if (i == selectedIndex) slot.background(GTGuiTextures.SLOT_ITEM_DARK);
                    else slot.background()
                        .disableThemeBackground(false);
                }
            }
        });

        return new ParentWidget<>().coverChildren()
            .child(grid);
    }

    @Override
    protected ParentWidget<?> createItemOutputSlots(ModularPanel panel, PanelSyncManager syncManager) {
        Flow outputColumn = Flow.column()
            .coverChildren();

        // loop button
        outputColumn.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isLoopMode, machine::setLoopMode).allowC2S())
                .overlay(GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .addTooltipLine(GTUtility.translate("GT5U.machines.betterjukebox.loop.tooltip"))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // output slot
        outputColumn.child(
            new ItemSlot().backgroundOverlay(slotOverlayFunction.apply(0, false, true, false))
                .slot(
                    new MachineModularSlot(machine.inventoryHandler, machine.getOutputSlot(), baseMetaTileEntity)
                        .accessibility(false, true)));

        // shuffle button
        outputColumn.child(
            new ToggleButton().value(new BooleanSyncValue(machine::isShuffleMode, machine::setShuffleMode).allowC2S())
                .overlay(GTGuiTextures.OVERLAY_BUTTON_SHUFFLE)
                .addTooltipLine(GTUtility.translate("GT5U.machines.betterjukebox.shuffle.tooltip"))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return outputColumn;
    }

    @Override
    protected ProgressWidget createProgressBar(ModularPanel panel, PanelSyncManager syncManager) {
        LongSyncValue progressSyncer = new LongSyncValue(machine::getDiscProgressMs);
        syncManager.syncValue("progress", progressSyncer);
        LongSyncValue durationSyncer = new LongSyncValue(machine::getDiscDurationMs);
        syncManager.syncValue("duration", durationSyncer);

        return new ProgressWidget()
            .tooltipBuilder(
                t -> t.addLine(
                    String.format(
                        "%,.2f / %,.2f",
                        progressSyncer.getLongValue() / 1000f,
                        durationSyncer.getLongValue() / 1000f)))
            .tooltipAutoUpdate(true)
            .value(
                new DoubleSyncValue(
                    () -> (double) progressSyncer.getLongValue() / Math.max(1, durationSyncer.getLongValue())))
            .texture(GTGuiTextures.PROGRESSBAR_ARROW_STANDARD, 18)
            .size(18)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected ItemSlot createSpecialSlot() {
        return super.createSpecialSlot().marginRight(0);
    }

    @Override
    protected ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().fullWidth()
            .coverChildrenHeight()
            .child(createBottomLeftCornerFlow(panel, syncManager))
            .child(createLogo().rightRel(0));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager).child(createSliderColumn(panel, syncManager))
            .child(createChargerSlot())
            .child(createSpecialSlot());
    }

    private Flow createSliderColumn(ModularPanel panel, PanelSyncManager syncManager) {
        Flow sliderColumn = Flow.column()
            .coverChildren()
            .padding(1);

        // normal sound attenuation slider
        sliderColumn.child(
            new SliderWidget().background(GTGuiTextures.SLOT_ITEM_STANDARD)
                .size(52, 8)
                .sliderSize(8, 8)
                .value(new DoubleSyncValue(machine::getPlaybackBlockRange, machine::setPlaybackBlockRange).allowC2S())
                .bounds(
                    0,
                    MTEBetterJukebox.BalanceMath
                        .volumeToAttenuationDistance(MTEBetterJukebox.BalanceMath.listeningVolume(machine.mTier)))
                .tooltipBuilder(
                    t -> t.addLine(
                        GTUtility.translate(
                            "GT5U.machines.betterjukebox.attenuationDistance.tooltip",
                            (int) machine.getPlaybackBlockRange())))
                .tooltipAutoUpdate(true));

        // p2p sound attenuation slider
        sliderColumn.child(
            new SliderWidget().background(GTGuiTextures.SLOT_ITEM_STANDARD)
                .size(52, 8)
                .sliderSize(8, 8)
                .value(new DoubleSyncValue(machine::getP2PBlockRange, machine::setP2PBlockRange).allowC2S())
                .bounds(
                    0,
                    MTEBetterJukebox.BalanceMath
                        .volumeToAttenuationDistance(MTEBetterJukebox.BalanceMath.listeningVolume(machine.mTier)))
                .tooltipBuilder(
                    t -> t.addLine(
                        GTUtility.translate(
                            "GT5U.machines.betterjukebox.p2pAttenuationDistance.tooltip",
                            (int) machine.getP2PBlockRange())))
                .tooltipAutoUpdate(true));

        return sliderColumn;
    }

    @Override
    protected boolean supportsBottomRightCornerFlow() {
        return false;
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }
}
