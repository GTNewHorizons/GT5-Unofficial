package gregtech.client.hud.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class GraphElement extends WidgetElement<GraphElement> implements Configurable {

    private final Supplier<Double> valueSupplier;

    private int delay = 20, dataSize = 10;
    private boolean dynamicColor = true, renderDots = true, renderLines = true;
    private float graphRed = 1f, graphGreen = 1f, graphBlue = 1f, graphAlpha = 1f;

    private final List<Double> data = new ArrayList<>();
    private long lastUpdate = 0;

    public GraphElement(Supplier<Double> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public GraphElement setDelay(int ticks) {
        delay = Math.max(1, ticks);
        return this;
    }

    public GraphElement setDataSize(int size) {
        dataSize = Math.max(1, size);
        while (data.size() > dataSize) data.remove(0);
        return this;
    }

    public GraphElement setDynamicColor(boolean dynamic) {
        this.dynamicColor = dynamic;
        return this;
    }

    public GraphElement setRenderDots(boolean render) {
        this.renderDots = render;
        return this;
    }

    public GraphElement setRenderLines(boolean render) {
        this.renderLines = render;
        return this;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        long tick = mc.theWorld.getTotalWorldTime();
        if (tick - lastUpdate >= delay) {
            data.add(valueSupplier.get());
            if (data.size() > dataSize) data.remove(0);
            lastUpdate = tick;
        }
        if (data.isEmpty()) return;

        double min = data.stream()
            .mapToDouble(d -> d)
            .min()
            .orElse(0);
        double max = data.stream()
            .mapToDouble(d -> d)
            .max()
            .orElse(1);
        double range = Math.max(1, max - min);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (renderLines) drawGraph(GL11.GL_LINE_STRIP, min, range, baseX, baseY);
        if (renderDots) drawGraph(GL11.GL_POINTS, min, range, baseX, baseY);

        GL11.glPopAttrib();
    }

    private void drawGraph(int mode, double min, double range, int baseX, int baseY) {
        GL11.glBegin(mode);
        for (int i = 0; i < data.size(); i++) {
            double val = data.get(i);
            double x = absLeft(baseX) + i * (width / (double) Math.max(1, data.size() - 1));
            double y = absBottom(baseY) - ((val - min) / range * height);
            y = Math.max(absTop(baseY), Math.min(absBottom(baseY), y)); // clamp

            getColor(val, i).apply(graphRed, graphGreen, graphBlue, graphAlpha);
            GL11.glVertex2d(x, y);
        }
        GL11.glEnd();
    }

    private GraphColorState getColor(double cur, int i) {
        if (!dynamicColor || i == 0) return GraphColorState.CUSTOM;
        double prev = data.get(i - 1);
        return cur > prev ? GraphColorState.UP : cur < prev ? GraphColorState.DOWN : GraphColorState.NEUTRAL;
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset H:", offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset V:", offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Delay:", delay, val -> { this.delay = Math.max(1,val); updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Data Size:", dataSize, this::setDataSize, yOff);
        yOff = HUDGui.GuiConfigureElement.createBooleanConfig(gui, "Render Dots", renderDots, this::setRenderDots, yOff);
        yOff = HUDGui.GuiConfigureElement.createBooleanConfig(gui, "Render Lines", renderLines, this::setRenderLines, yOff);
        yOff = HUDGui.GuiConfigureElement.createBooleanConfig(gui, "Dynamic Color", dynamicColor, this::setDynamicColor, yOff);
        yOff = HUDGui.GuiConfigureElement.createColorConfig(gui, "Color", this.red, this.green, this.blue, this.alpha,
            val -> { this.red = (float) val; updateAfterConfigChange(); },
            val -> { this.green = (float) val; updateAfterConfigChange(); },
            val -> { this.blue = (float) val; updateAfterConfigChange(); },
            val -> { this.alpha = (float) val; updateAfterConfigChange(); }, yOff);
        // spotless:on
        return yOff;
    }

    private enum GraphColorState {

        UP {

            void apply(float r, float g, float b, float a) {
                GL11.glColor4f(
                    HUDManager.UIConstants.GRAPH_UP_R,
                    HUDManager.UIConstants.GRAPH_UP_G,
                    HUDManager.UIConstants.GRAPH_UP_B,
                    HUDManager.UIConstants.GRAPH_UP_ALPHA);
            }
        },
        DOWN {

            void apply(float r, float g, float b, float a) {
                GL11.glColor4f(
                    HUDManager.UIConstants.GRAPH_DOWN_R,
                    HUDManager.UIConstants.GRAPH_DOWN_G,
                    HUDManager.UIConstants.GRAPH_DOWN_B,
                    HUDManager.UIConstants.GRAPH_DOWN_ALPHA);
            }
        },
        NEUTRAL {

            void apply(float r, float g, float b, float a) {
                GL11.glColor4f(
                    HUDManager.UIConstants.GRAPH_NEUTRAL_R,
                    HUDManager.UIConstants.GRAPH_NEUTRAL_G,
                    HUDManager.UIConstants.GRAPH_NEUTRAL_B,
                    HUDManager.UIConstants.GRAPH_NEUTRAL_ALPHA);
            }
        },
        CUSTOM {

            void apply(float r, float g, float b, float a) {
                GL11.glColor4f(r, g, b, a);
            }
        };

        abstract void apply(float r, float g, float b, float a);
    }
}
