package gregtech.common.misc.techtree.gui;

import java.util.ArrayList;
import java.util.Collection;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.scroll.HorizontalScrollData;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
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

    public static IWidget buildTechWidget(TechTreeGuiData data, ITechnology tech) {
        return new TechSelectorButton<>().size(60, 16)
            .overlay(IKey.lang(tech.getUnlocalizedName()))
            .onMousePressed(mouseButton -> {
                // Make this tech the selected technology
                if (mouseButton == 0) {
                    data.setSelectedTechnology(tech);
                }
                return true;
            })
            // Dynamically set background of this button based on the currently selected tech
            .background(new DynamicDrawable(() -> {
                if (data.getSelectedTechnology() == tech) {
                    return UITextures.BUTTON_STANDARD_TOGGLE_DOWN;
                }
                return UITextures.BUTTON_STANDARD_TOGGLE_UP;
            }));
    }

    public static Flow makeTechContainer(int depth) {
        return new Column().marginRight(40)
            .coverChildrenWidth();
    }

    public static Flow getTechContainer(int layer, ArrayList<Flow> containers) {
        // If this container already exists, simply return it
        if (layer < containers.size()) return containers.get(layer);
        // If it doesn't extend the list with new container objects until the needed layer is reached
        int n = containers.size();
        for (int i = n; i <= layer; ++i) {
            containers.add(i, makeTechContainer(i));
        }
        // Then return the container
        return containers.get(layer);
    }

    public static ModularPanel buildUI(TechTreeGuiData data, PanelSyncManager syncManager) {
        data.getNEISettings()
            .disableNEI();
        ModularPanel mainPanel = buildMainPanel();

        mainPanel.child(buildTitle());

        TechTreeLayout layout = TechTreeLayout.constructOrGet();

        // Index into the list specifies the depth of the technology
        ArrayList<Flow> techContainers = new ArrayList<>();

        Collection<ITechnology> techs = TechnologyRegistry.getTechnologies();
        for (ITechnology tech : techs) {
            IWidget techWidget = buildTechWidget(data, tech);
            Flow container = getTechContainer(layout.layerInfo.getDisplayLayer(tech), techContainers);
            container.child(techWidget);
        }

        Flow treeParent = new Row().align(Alignment.TopLeft)
            // Cover all columns with width (at least)
            .coverChildrenWidth();

        // Now add all containers as children of the main row
        for (Flow container : techContainers) {
            treeParent.child(container);
        }

        // Put the row into a horizontally scrollable region
        IWidget scroll = new ScrollWidget<>(new HorizontalScrollData()).align(Alignment.TopLeft)
            .marginLeft(20)
            .marginRight(20)
            .topRel(0.2f)
            .widthRel(1.0f)
            .heightRel(0.6f)
            .child(treeParent);

        // Then add main row to the main panel
        mainPanel.child(scroll);

        return mainPanel;
    }
}
