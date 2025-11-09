package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget.Direction;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.MTEForgeOfGodsGui;
import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class MilestonePanel {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final int MILESTONE_BUTTON_SIZE_W = 130;
    private static final int MILESTONE_BUTTON_SIZE_H = 100;

    private static final int MILESTONE_BUTTON_MARGIN_X = 37;
    private static final int MILESTONE_BUTTON_MARGIN_Y = 24;

    private static final int MILESTONE_PROGRESS_BAR_H = 7;

    public static ModularPanel openPanel(PanelSyncManager syncManager, ForgeOfGodsData data, ModularPanel panel,
        ModularPanel parent) {
        registerSyncValues(syncManager, data);

        panel.size(WIDTH, HEIGHT)
            .background(GTGuiTextures.BACKGROUND_SPACE)
            .disableHoverBackground();

        panel.child(createMilestone(Milestones.CHARGE, syncManager, panel, data));
        panel.child(createMilestone(Milestones.CONVERSION, syncManager, panel, data));
        panel.child(createMilestone(Milestones.CATALYST, syncManager, panel, data));
        panel.child(createMilestone(Milestones.COMPOSITION, syncManager, panel, data));

        return panel;
    }

    private static void registerSyncValues(PanelSyncManager syncManager, ForgeOfGodsData data) {
        AtomicInteger i = new AtomicInteger();
        syncManager.syncValue(MTEForgeOfGodsGui.SYNC_MILESTONE_CLICKED, new IntSyncValue(i::intValue, i::set));

        // Charge
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CHARGE_PROGRESS,
            new DoubleSyncValue(
                data::getPowerMilestonePercentage,
                val -> data.setPowerMilestonePercentage((float) val)));
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CHARGE_PROGRESS_INVERTED,
            new DoubleSyncValue(
                data::getInvertedPowerMilestonePercentage,
                val -> data.setInvertedPowerMilestonePercentage((float) val)));

        // Conversion
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CONVERSION_PROGRESS,
            new DoubleSyncValue(
                data::getRecipeMilestonePercentage,
                val -> data.setRecipeMilestonePercentage((float) val)));
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CONVERSION_PROGRESS_INVERTED,
            new DoubleSyncValue(
                data::getInvertedRecipeMilestonePercentage,
                val -> data.setInvertedRecipeMilestonePercentage((float) val)));

        // Catalyst
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CATALYST_PROGRESS,
            new DoubleSyncValue(data::getFuelMilestonePercentage, val -> data.setFuelMilestonePercentage((float) val)));
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CATALYST_PROGRESS_INVERTED,
            new DoubleSyncValue(
                data::getInvertedFuelMilestonePercentage,
                val -> data.setInvertedFuelMilestonePercentage((float) val)));

        // Composition
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_COMPOSITION_PROGRESS,
            new DoubleSyncValue(
                data::getStructureMilestonePercentage,
                val -> data.setStructureMilestonePercentage((float) val)));
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_COMPOSITION_PROGRESS_INVERTED,
            new DoubleSyncValue(
                data::getInvertedStructureMilestonePercentage,
                val -> data.setInvertedStructureMilestonePercentage((float) val)));
    }

    private static ParentWidget<?> createMilestone(Milestones milestone, PanelSyncManager syncManager,
        ModularPanel panel, ForgeOfGodsData data) {
        ParentWidget<?> parent = new ParentWidget<>().size(MILESTONE_BUTTON_SIZE_W, MILESTONE_BUTTON_SIZE_H)
            .align(milestone.getPosition())
            .margin(MILESTONE_BUTTON_MARGIN_X, MILESTONE_BUTTON_MARGIN_Y);

        DoubleSyncValue progressSyncer = syncManager
            .findSyncHandler(milestone.getProgressSyncKey(), DoubleSyncValue.class);
        DoubleSyncValue invertedProgressSyncer = syncManager
            .findSyncHandler(milestone.getProgressInvertedSyncKey(), DoubleSyncValue.class);

        // Background image and individual milestone button
        parent.child(
            new ButtonWidget<>().alignX(Alignment.CENTER)
                .size(milestone.getWidth(), milestone.getHeight())
                .background(milestone.getMainBackground())
                .disableHoverBackground()
                .onMousePressed($ -> {
                    IPanelHandler individualPanel = Panels.INDIVIDUAL_MILESTONE.get(panel, syncManager, data);

                    if (individualPanel.isPanelOpen()) {
                        individualPanel.closePanel();
                    }

                    IntSyncValue individualMilestoneSyncer = syncManager
                        .findSyncHandler(MTEForgeOfGodsGui.SYNC_MILESTONE_CLICKED, IntSyncValue.class);
                    individualMilestoneSyncer.setIntValue(milestone.ordinal());
                    individualPanel.openPanel();
                    return true;
                })
                .tooltip(t -> t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.milestoneinfo"))));

        // Milestone progress bar
        parent.child(
            new ProgressWidget().progress(progressSyncer::getDoubleValue)
                .texture(
                    GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND,
                    milestone.getProgressBarMainOverlay(),
                    -1)
                .direction(Direction.RIGHT)
                .alignY(Alignment.CENTER)
                .widthRel(1.0f)
                .height(MILESTONE_PROGRESS_BAR_H));
        parent.child(
            new ProgressWidget().progress(invertedProgressSyncer::getDoubleValue)
                .texture(GTGuiTextures.TRANSPARENT, milestone.getProgressBarInvertedOverlay(), -1)
                .direction(Direction.LEFT)
                .alignY(Alignment.CENTER)
                .widthRel(1.0f)
                .height(MILESTONE_PROGRESS_BAR_H));

        // Milestone title
        parent.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal(milestone.getTitleLangKey()))
                .asWidget()
                .alignY(0.35f)
                .alignX(Alignment.CENTER));

        return parent;
    }
}
