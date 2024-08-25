package gregtech.common.misc.techtree.gui;

import static gregtech.api.enums.Mods.GregTech;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TreePanel extends ModularPanel {

    private final ArrayList<TechLayer> containers;
    private final HashMap<String, IWidget> widgetForTech;

    private ShaderProgram arrowShader;
    private boolean initialized = false;
    private int vbo_pID = -1;
    private int vbo_vID = -1;
    private int a_p0 = -1, a_p1 = -1, a_p2 = -1;
    private int u_dist = -1;
    private int u_ScreenSize = -1;

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

    private void init() {

        try {
            arrowShader = new ShaderProgram(
                GregTech.resourceDomain,
                "shaders/techTreeArrow.vert.glsl",
                "shaders/techTreeArrow.frag.glsl");

            a_p0 = arrowShader.getAttribLocation("a_p0");
            a_p1 = arrowShader.getAttribLocation("a_p1");
            a_p2 = arrowShader.getAttribLocation("a_p2");
            u_dist = arrowShader.getUniformLocation("u_dist");
            u_ScreenSize = arrowShader.getUniformLocation("u_ScreenSize");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        GL20.glUniform1f(u_dist, 1);

        vbo_pID = GL15.glGenBuffers();
        vbo_vID = GL15.glGenBuffers();
        initialized = true;
    }

    @Override
    public void draw(GuiContext context, WidgetTheme widgetTheme) {
        super.draw(context, widgetTheme);

        if (!initialized) {
            init();
            if (!initialized) return;
        }

        int vertexCount = regenerateBuffers();

        arrowShader.use();

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL20.glEnableVertexAttribArray(a_p0);
        GL20.glEnableVertexAttribArray(a_p1);
        GL20.glEnableVertexAttribArray(a_p2);

        GL20.glUniform2f(u_ScreenSize, context.mc.displayWidth, context.mc.displayHeight);
        GL20.glUniform1f(u_dist, 2);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_pID);
        int stride = 2 * 3 * Float.BYTES; // 3 vec2 attributes

        GL20.glVertexAttribPointer(a_p0, 2, GL11.GL_FLOAT, false, stride, 0);
        GL20.glVertexAttribPointer(a_p1, 2, GL11.GL_FLOAT, false, stride, 2 * Float.BYTES);
        GL20.glVertexAttribPointer(a_p2, 2, GL11.GL_FLOAT, false, stride, 4 * Float.BYTES);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vID);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

        GL20.glDisableVertexAttribArray(a_p0);
        GL20.glDisableVertexAttribArray(a_p1);
        GL20.glDisableVertexAttribArray(a_p2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        ShaderProgram.clear();

        GL11.glPopAttrib();
    }

    // Collects all bezier curves needed to be drawn in a buffers
    // in order to minimize the amount of draw calls done
    private int regenerateBuffers() {
        int lineCount = 0;
        // Collect draw count
        for (TechLayer container : containers) {
            ArrayList<ITechnology> techs = container.getTechnologies();
            for (ITechnology tech : techs) {
                lineCount += tech.getChildren()
                    .size();
            }
        }

        // Each line requires a quad which means 6 vertices, which means 6 vec3's, which means 18 floats per face
        int vertexCount = lineCount * 6;
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);

        // Each vertex requires 3 control points, which is a vec2, which results in 36(!!) floats per face
        // Compress floats?
        FloatBuffer bezierBuffer = BufferUtils.createFloatBuffer(vertexCount * 6);

        for (TechLayer container : containers) {
            ArrayList<ITechnology> techs = container.getTechnologies();
            List<IWidget> children = container.getChildren();
            for (int i = 0; i < techs.size(); ++i) {
                ITechnology tech = techs.get(i);
                IWidget child = children.get(i);
                for (ITechnology depTech : tech.getChildren()) {
                    IWidget depWidget = widgetForTech.get(depTech.getInternalName());
                    bufferLineData(child, depWidget, vertexBuffer, bezierBuffer);
                }
            }
        }

        vertexBuffer.flip();
        bezierBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_pID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bezierBuffer, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return vertexCount;
    }

    private void bufferLineData(IWidget child, IWidget depWidget, FloatBuffer vertexBuffer, FloatBuffer bezierPoints) {
        Pos2d start = getWidgetCenterRight(child, 2);
        Pos2d end = getWidgetCenterLeft(depWidget, 2);

        float[] quad = getQuad(start, end);

        vertexBuffer.put(quad); // Adds 18

        float[] vertexBezier1 = { start.x, start.y, (start.x * 3 + end.x) / 4f, start.y, // a_p1
            (start.x + end.x) / 2f, (start.y + end.y) / 2f };
        bezierPoints.put(vertexBezier1);
        bezierPoints.put(vertexBezier1);
        bezierPoints.put(vertexBezier1);

        float[] vertexBezier2 = { (start.x + end.x) / 2f, (start.y + end.y) / 2f, (start.x + end.x * 3) / 4f, end.y,
            end.x, end.y, };
        bezierPoints.put(vertexBezier2);
        bezierPoints.put(vertexBezier2);
        bezierPoints.put(vertexBezier2);
    }

    private static float @NotNull [] getQuad(Pos2d start, Pos2d end) {
        float dx = 4;
        float dy = 4;

        float p0x = start.x - dx;
        float p0y = Math.min(start.y, end.y) - dy;
        float p1x = end.x + dx;
        float p1y = Math.min(start.y, end.y) - dy;
        float p2x = end.x + dx;
        float p2y = Math.max(start.y, end.y) + dy;
        float p3x = start.x - dx;
        float p3y = Math.max(start.y, end.y) + dy;

        float[] quad;
        if (start.y > end.y) {
            quad = new float[] { p0x, p0y, 0, p2x, p2y, 0, p3x, p3y, 0, // Top left tri
                p0x, p0y, 0, p1x, p1y, 0, p2x, p2y, 0, // Bottom right tri
            };
        } else {
            quad = new float[] { p0x, p0y, 0, p1x, p1y, 0, p3x, p3y, 0, // Bottom left tri
                p1x, p1y, 0, p2x, p2y, 0, p3x, p3y, 0, // Top right tri
            };
        }
        return quad;
    }

}
