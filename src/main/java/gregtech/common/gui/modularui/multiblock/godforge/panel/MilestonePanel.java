package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

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
import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Syncers;
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
        Syncers.MILESTONE_CLICKED.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CHARGE_PROGRESS.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CHARGE_PROGRESS_INVERTED.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CONVERSION_PROGRESS.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CONVERSION_PROGRESS_INVERTED.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CATALYST_PROGRESS.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_CATALYST_PROGRESS_INVERTED.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_COMPOSITION_PROGRESS.register(syncManager, data, Panels.MILESTONE);
        Syncers.MILESTONE_COMPOSITION_PROGRESS_INVERTED.register(syncManager, data, Panels.MILESTONE);
    }

    private static ParentWidget<?> createMilestone(Milestones milestone, PanelSyncManager syncManager,
        ModularPanel panel, ForgeOfGodsData data) {
        ParentWidget<?> parent = new ParentWidget<>().size(MILESTONE_BUTTON_SIZE_W, MILESTONE_BUTTON_SIZE_H)
            .align(milestone.getPosition())
            .margin(MILESTONE_BUTTON_MARGIN_X, MILESTONE_BUTTON_MARGIN_Y);

        DoubleSyncValue progressSyncer = milestone.getProgressSyncer()
            .lookup(syncManager, Panels.MILESTONE);
        DoubleSyncValue invertedProgressSyncer = milestone.getProgressInvertedSyncer()
            .lookup(syncManager, Panels.MILESTONE);

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

                    IntSyncValue individualMilestoneSyncer = Syncers.MILESTONE_CLICKED
                        .lookup(syncManager, Panels.MILESTONE);
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
