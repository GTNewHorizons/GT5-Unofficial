package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget.Direction;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Syncers;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;

public class MilestonePanel {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final int MILESTONE_BUTTON_SIZE_W = 130;
    private static final int MILESTONE_BUTTON_SIZE_H = 100;

    private static final int MILESTONE_BUTTON_MARGIN_X = 37;
    private static final int MILESTONE_BUTTON_MARGIN_Y = 24;

    private static final int MILESTONE_PROGRESS_BAR_H = 7;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.MILESTONE);

        registerSyncValues(hypervisor);

        panel.size(WIDTH, HEIGHT)
            .background(GTGuiTextures.BACKGROUND_SPACE)
            .disableHoverBackground();

        panel.child(createMilestone(Milestones.CHARGE, hypervisor));
        panel.child(createMilestone(Milestones.CONVERSION, hypervisor));
        panel.child(createMilestone(Milestones.CATALYST, hypervisor));
        panel.child(createMilestone(Milestones.COMPOSITION, hypervisor));

        panel.child(ForgeOfGodsGuiUtil.panelCloseButton()); // todo check position
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        Syncers.MILESTONE_CLICKED.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CHARGE_PROGRESS.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CHARGE_PROGRESS_INVERTED.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CONVERSION_PROGRESS.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CONVERSION_PROGRESS_INVERTED.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CATALYST_PROGRESS.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_CATALYST_PROGRESS_INVERTED.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_COMPOSITION_PROGRESS.registerFor(Panels.MILESTONE, hypervisor);
        Syncers.MILESTONE_COMPOSITION_PROGRESS_INVERTED.registerFor(Panels.MILESTONE, hypervisor);
    }

    private static ParentWidget<?> createMilestone(Milestones milestone, SyncHypervisor hypervisor) {
        ParentWidget<?> parent = new ParentWidget<>().size(MILESTONE_BUTTON_SIZE_W, MILESTONE_BUTTON_SIZE_H)
            .align(milestone.getPosition())
            .margin(MILESTONE_BUTTON_MARGIN_X, MILESTONE_BUTTON_MARGIN_Y);

        IPanelHandler individualPanel = Panels.INDIVIDUAL_MILESTONE.getFrom(Panels.MILESTONE, hypervisor);
        DoubleSyncValue progressSyncer = milestone.getProgressSyncer()
            .lookupFrom(Panels.MILESTONE, hypervisor);
        DoubleSyncValue invertedProgressSyncer = milestone.getProgressInvertedSyncer()
            .lookupFrom(Panels.MILESTONE, hypervisor);

        // Background image and individual milestone button
        parent.child(
            new ButtonWidget<>().alignX(Alignment.CENTER)
                .size(milestone.getMainWidth(), milestone.getMainHeight())
                .background(milestone.getMainBackground())
                .disableHoverBackground()
                .onMousePressed($ -> {
                    EnumSyncValue<Milestones> syncer = Syncers.MILESTONE_CLICKED
                        .lookupFrom(Panels.MILESTONE, hypervisor);
                    syncer.setValue(milestone);
                    if (!individualPanel.isPanelOpen()) {
                        individualPanel.openPanel();
                    }
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
