package gregtech.common.misc.techtree.gui;

import static com.cleanroommc.modularui.drawable.BufferBuilder.bufferbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TreePanel extends ModularPanel {

    private final ArrayList<TechLayer> containers;
    private final HashMap<String, IWidget> widgetForTech;

    public TreePanel(@NotNull String name, ArrayList<TechLayer> containers, HashMap<String, IWidget> widgetForTech) {
        super(name);
        this.containers = containers;
        this.widgetForTech = widgetForTech;
    }

    public static TreePanel defaultPanel(@NotNull String name, ArrayList<TechLayer> containers,
        HashMap<String, IWidget> widgetForTech) {
        return defaultPanel(name, 176, 166, containers, widgetForTech);
    }

    public static TreePanel defaultPanel(@NotNull String name, int width, int height, ArrayList<TechLayer> containers,
        HashMap<String, IWidget> widgetForTech) {

        return (TreePanel) new TreePanel(name, containers, widgetForTech).size(width, height);
    }

    Pos2d getWidgetCenterRight(IWidget widget, int thickness) {
        Area area = widget.getArea();
        return new Pos2d(area.x + area.width - thickness, area.y + area.height / 2);
    }

    Pos2d getWidgetCenterLeft(IWidget widget, int thickness) {
        Area area = widget.getArea();
        return new Pos2d(area.x + thickness, area.y + area.height / 2);
    }

    private void drawLine(GuiContext context, WidgetTheme widgetTheme, IWidget start, IWidget end, int thickness) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        // Start pos is the center right pixel of the widget
        Pos2d startPos = getWidgetCenterRight(start, thickness);
        Pos2d endPos = getWidgetCenterLeft(end, thickness);

        // Create coordinates for a diagonal line quad
        // Source: https://forums.ogre3d.org/viewtopic.php?t=57710
        Pos2d diff = endPos.subtract(startPos);
        double distance = startPos.distance(endPos);
        // x/y swap is not a typo
        double dx = (-diff.y / distance) * thickness;
        double dy = (diff.x / distance) * thickness;
        Pos2d q1 = new Pos2d(startPos.x - dx, startPos.y - dy);
        Pos2d q2 = new Pos2d(startPos.x + dx, startPos.y + dy);
        Pos2d q3 = new Pos2d(endPos.x - dx, endPos.y - dy);
        Pos2d q4 = new Pos2d(endPos.x + dx, endPos.y + dy);

        Tessellator.instance.startDrawingQuads();
        bufferbuilder.pos(q1.x, q1.y, 0.0f)
            .color(255, 255, 255, 255)
            .endVertex();
        bufferbuilder.pos(q2.x, q2.y, 0.0f)
            .color(255, 255, 255, 255)
            .endVertex();
        // Not a typo, CCW order needed
        bufferbuilder.pos(q4.x, q4.y, 0.0f)
            .color(255, 255, 255, 255)
            .endVertex();
        bufferbuilder.pos(q3.x, q3.y, 0.0f)
            .color(255, 255, 255, 255)
            .endVertex();
        Tessellator.instance.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void draw(GuiContext context, WidgetTheme widgetTheme) {
        super.draw(context, widgetTheme);
        // Draw the connecting lines (very crudely, lol)
        for (TechLayer container : containers) {
            ArrayList<ITechnology> techs = container.getTechnologies();
            List<IWidget> children = container.getChildren();
            for (int i = 0; i < techs.size(); ++i) {
                ITechnology tech = techs.get(i);
                IWidget child = children.get(i);
                // For each dependent technology, draw a line to it
                for (ITechnology depTech : tech.getChildren()) {
                    IWidget depWidget = widgetForTech.get(depTech.getInternalName());
                    drawLine(context, widgetTheme, child, depWidget, 2);
                }
            }
        }
    }
}
