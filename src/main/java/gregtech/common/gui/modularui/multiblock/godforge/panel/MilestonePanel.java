package gregtech.common.gui.modularui.multiblock.godforge.panel;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import com.cleanroommc.modularui.widgets.ProgressWidget;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.MTEForgeOfGodsGui;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneIcon;

import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.util.StatCollector.translateToLocal;

public class MilestonePanel {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final int MILESTONE_BUTTON_WIDTH = 130;
    private static final int MILESTONE_BUTTON_HEIGHT = 100;

    private static final int MILESTONE_BUTTON_MARGIN_X = 37;
    private static final int MILESTONE_BUTTON_MARGIN_Y = 24;

    public static ModularPanel openPanel(PanelSyncManager syncManager, MTEForgeOfGods multiblock) {
        registerSyncValues(syncManager);

        ModularPanel panel = new ModularPanel("").size(WIDTH, HEIGHT)
            .background(GTGuiTextures.BACKGROUND_SPACE)
            .disableHoverBackground();

        panel.child(createChargeMilestone(syncManager, multiblock));
        panel.child(createConversionMilestone());
        panel.child(createCatalystMilestone());
        panel.child(createCompositionMilestone());

        return panel;
    }

    private static void registerSyncValues(PanelSyncManager syncManager) {
        AtomicInteger i = new AtomicInteger();
        syncManager.syncValue(
            MTEForgeOfGodsGui.SYNC_MILESTONE_CLICKED,
            new IntSyncValue(i::get, i::set));
    }

    private static ParentWidget<?> createChargeMilestone(PanelSyncManager syncManager, MTEForgeOfGods multiblock) {
        ParentWidget<?> parent = new ParentWidget<>().size(MILESTONE_BUTTON_WIDTH, MILESTONE_BUTTON_HEIGHT)
            .marginLeft(MILESTONE_BUTTON_MARGIN_X)
            .marginTop(MILESTONE_BUTTON_MARGIN_Y);

        // Background image and individual milestone button
        parent.child(
            new ButtonWidget<>().alignX(Alignment.CENTER)
                .heightRel(1.0f)
                .background(GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW)
                .disableHoverBackground()
                .onMousePressed($ -> onMilestoneClick(syncManager, MilestoneIcon.CHARGE))
                .tooltip(t -> t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.milestoneinfo"))));

        // Milestone progress bar
        parent.child(
            new ProgressWidget().progress(() -> )
        );

        // Milestone title
        parent.child(

        );

        return parent;
    }

    private static ParentWidget<?> createConversionMilestone() {

    }

    private static ParentWidget<?> createCatalystMilestone() {

    }

    private static ParentWidget<?> createCompositionMilestone() {

    }

    private static boolean onMilestoneClick(PanelSyncManager syncManager, MilestoneIcon milestone) {
        IPanelHandler individualPanel = syncManager.panel(
            MTEForgeOfGodsGui.PANEL_INDIVIDUAL_MILESTONE,
            (p_syncManager, syncHandler) -> IndividualMilestonePanel.openPanel(),
            true);

        if (individualPanel.isPanelOpen()) {
            individualPanel.closePanel();
        }

        IntSyncValue individualMilestoneSyncer = syncManager.findSyncHandler(MTEForgeOfGodsGui.SYNC_MILESTONE_CLICKED, IntSyncValue.class);
        individualMilestoneSyncer.setIntValue(milestone.ordinal());
        individualPanel.openPanel();
        return true;
    }
}
