package gregtech.common.misc.techtree.gui;

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

    public static ModularPanel buildUI(PosGuiData data, GuiSyncManager syncManager) {
        data.getNEISettings()
            .disableNEI();
        ModularPanel mainPanel = buildMainPanel();

        mainPanel.child(buildTitle());

        // Temporary layout: scrollable list of all techs
        Column techList = new Column();
        Collection<ITechnology> techs = TechnologyRegistry.getTechnologies();
        for (ITechnology tech : techs) {
            IWidget techWidget = buildTechWidget(tech);
            techList.child(techWidget);
        }

        techList.align(Alignment.TopLeft)
            .leftRel(0.1f)
            .topRel(0.2f)
            .heightRel(0.6f)
            .coverChildrenWidth();

        mainPanel.child(techList);

        return mainPanel;
    }
}
