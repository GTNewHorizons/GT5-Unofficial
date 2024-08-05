package gregtech.common.misc.techtree.gui;

import java.util.ArrayList;
import java.util.Collection;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.gui.modularui2.UITextures;
import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechTreeGui {

    public static ModularPanel buildMainPanel() {
        return ModularPanel.defaultPanel("Technology Tree")
            .full()
            .background(UITextures.BACKGROUND_SCREEN_BLUE);
    }

    public static TextWidget buildTitle() {
        return IKey.lang("gt.gui.techtree.title")
            .alignment(Alignment.Center)
            .color(0xffffff)
            .shadow(true)
            .asWidget()
            .scale(1.2f)
            .align(Alignment.TopCenter)
            .top(5);
    }

    public static IWidget buildTechWidget(ITechnology tech) {
        return new ButtonWidget<>().size(120, 16)
            .overlay(IKey.lang(tech.getUnlocalizedName()));
    }

    public static Column makeTechContainer(int depth) {
        return new Column().align(Alignment.TopLeft)
            .coverChildrenWidth();
    }

    public static Column getTechContainer(int depth, ArrayList<Column> containers) {
        // If this container already exists, simply return it
        if (depth < containers.size()) return containers.get(depth);
        // If it doesn't extend the list with new container objects until the needed depth is reached
        int n = containers.size();
        for (int i = n; i <= depth; ++i) {
            containers.add(i, makeTechContainer(i));
        }
        // Then return the container
        return containers.get(depth);
    }

    public static ModularPanel buildUI(PosGuiData data, GuiSyncManager syncManager) {
        data.getNEISettings()
            .disableNEI();
        ModularPanel mainPanel = buildMainPanel();

        mainPanel.child(buildTitle());

        // Index into the list specifies the depth of the technology
        ArrayList<Column> techContainers = new ArrayList<>();

        Collection<ITechnology> techs = TechnologyRegistry.getTechnologies();
        for (ITechnology tech : techs) {
            IWidget techWidget = buildTechWidget(tech);
            // Find the depth of the tech and make a column for it if it doesn't exist yet
            Column container = getTechContainer(tech.getDepth(), techContainers);
            container.child(techWidget);
        }

        Row treeParent = new Row().topRel(0.2f)
            .heightRel(0.6f);

        // Now add all containers as children of the main row
        for (Column container : techContainers) {
            treeParent.child(container);
        }

        // Then add main row to the main panel
        mainPanel.child(treeParent);

        return mainPanel;
    }
}
