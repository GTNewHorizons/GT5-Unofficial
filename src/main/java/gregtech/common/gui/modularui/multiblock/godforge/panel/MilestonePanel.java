package gregtech.common.gui.modularui.multiblock.godforge.panel;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneIcon;

public class MilestonePanel {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final int MILESTONE_BUTTON_WIDTH = 130;
    private static final int MILESTONE_BUTTON_HEIGHT = 100;

    public static ModularPanel openPanel(PanelSyncManager syncManager) {
        ModularPanel panel = new ModularPanel("").size(WIDTH, HEIGHT)
            .background(GTGuiTextures.BACKGROUND_SPACE)
            .disableHoverBackground();

        panel.child(createChargeMilestone());
        panel.child(createConversionMilestone());
        panel.child(createCatalystMilestone());
        panel.child(createCompositionMilestone());

        return panel;
    }

    public static void registerSyncValues(PanelSyncManager syncManager) {}

    private static ParentWidget<?> createChargeMilestone() {
        ParentWidget<?> parent = new ParentWidget<>().size(MILESTONE_BUTTON_WIDTH, MILESTONE_BUTTON_HEIGHT);

        parent.child(
            new ButtonWidget<>().alignX(Alignment.CENTER)
                .background(GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW)
                .disableHoverBackground()
                .onMousePressed(d -> {
                    onMilestoneClick(MilestoneIcon.CHARGE);
                    return true;
                }));
        parent.child(

        );
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

    private static void onMilestoneClick(MilestoneIcon milestone) {

    }
}
