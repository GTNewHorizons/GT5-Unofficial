package gregtech.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDGui;

@SideOnly(Side.CLIENT)
public class ChamferedRectElement extends WidgetElement<ChamferedRectElement> implements Configurable {

    public int cornerCut = 3;
    public float outlineThickness = 1.0f;

    public ChamferedRectElement() {
        this.red = 0.2f;
        this.green = 0.2f;
        this.blue = 0.2f;
        this.alpha = 0.8f;
        hoverHighlight = false;
    }

    public ChamferedRectElement setCornerCut(int size) {
        this.cornerCut = size;
        updateAfterConfigChange();
        return this;
    }

    public ChamferedRectElement setOutlineThickness(float thickness) {
        this.outlineThickness = Math.max(0f, thickness);
        updateAfterConfigChange();
        return this;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderChamferedRect(baseX, baseY, width, height, cornerCut, red, green, blue, alpha, outlineThickness);
    }

    private void renderChamferedRect(int x, int y, int width, int height, int cut, float r, float g, float b, float a,
        float outlineW) {
        if (width <= 0 || height <= 0 || a == 0) return;

        int c = Math.max(0, Math.min(cut, Math.min(width / 2, height / 2)));
        float cx = x + width * 0.5f;
        float cy = y + height * 0.5f;

        Tessellator tess = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(r, g, b, a);

        tess.startDrawing(GL11.GL_TRIANGLE_FAN);
        tess.addVertex(cx, cy, 0);
        tess.addVertex(x + c, y, 0);
        tess.addVertex(x + width - c, y, 0);
        tess.addVertex(x + width, y + c, 0);
        tess.addVertex(x + width, y + height - c, 0);
        tess.addVertex(x + width - c, y + height, 0);
        tess.addVertex(x + c, y + height, 0);
        tess.addVertex(x, y + height - c, 0);
        tess.addVertex(x, y + c, 0);
        tess.addVertex(x + c, y, 0);
        tess.draw();
        if (outlineW > 0f) {
            float or = Math.max(0f, r * 0.6f);
            float og = Math.max(0f, g * 0.6f);
            float ob = Math.max(0f, b * 0.6f);

            GL11.glLineWidth(outlineW);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glColor4f(or, og, ob, a);

            tess.startDrawing(GL11.GL_LINE_LOOP);
            tess.addVertex(x + c, y, 0);
            tess.addVertex(x + width - c, y, 0);
            tess.addVertex(x + width, y + c, 0);
            tess.addVertex(x + width, y + height - c, 0);
            tess.addVertex(x + width - c, y + height, 0);
            tess.addVertex(x + c, y + height, 0);
            tess.addVertex(x, y + height - c, 0);
            tess.addVertex(x, y + c, 0);
            tess.draw();

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1.0f);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Corner Cut:", this.cornerCut, val -> { this.cornerCut = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Outline Thickness:", this.outlineThickness, val -> { this.outlineThickness = (float) val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createColorConfig(gui, "Color", this.red, this.green, this.blue, this.alpha,
            val -> { this.red = (float) val; updateAfterConfigChange(); },
            val -> { this.green = (float) val; updateAfterConfigChange(); },
            val -> { this.blue = (float) val; updateAfterConfigChange(); },
            val -> { this.alpha = (float) val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }
}
