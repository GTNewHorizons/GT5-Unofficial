package gregtech.common.gui.modularui.widget;

import java.util.function.DoubleSupplier;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.math.Color;

/**
 * Draws a gauge pointer hand, rotating around the x&y coordinates of the widget.
 * This is a direct copy of the mui1 gauge arrow because throwing both IDrawables at it makes asWidget clash
 */
public class CircularGaugeDrawable implements IDrawable {

    private DoubleSupplier progressSupplier;
    private float minAngle = (float) Math.toRadians(-235.0);
    private float maxAngle = (float) Math.toRadians(45.0);
    private float lastAngle = Float.NaN;

    public CircularGaugeDrawable(DoubleSupplier progressSupplier) {
        this.progressSupplier = progressSupplier;
    }

    @Override
    public void draw(GuiContext context, int x, int y, int width, int height, WidgetTheme widgetTheme) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        final Tessellator tessellator = Tessellator.instance;

        final float progress = MathHelper.clamp_float((float) progressSupplier.getAsDouble(), 0.0f, 1.0f);
        final float newDialAngle = minAngle + progress * (maxAngle - minAngle);
        if (Float.isNaN(lastAngle)) {
            lastAngle = newDialAngle;
        } else {
            lastAngle = (lastAngle + newDialAngle) / 2.0f;
        }
        final float angle = lastAngle;
        final float sinA = (float) Math.sin(-angle);
        final float cosA = (float) Math.cos(-angle);
        height /= 2;

        tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);

        int color = widgetTheme.getColor();
        tessellator
            .setColorRGBA(Color.getRed(color), Color.getGreen(color), Color.getBlue(color), Color.getAlpha(color));
        tessellator.addVertex(x + width * cosA, y - width * sinA, 0.0f);
        tessellator.addVertex(x - height * sinA, y - height * cosA, 0.0f);
        tessellator.addVertex(x + height * sinA, y + height * cosA, 0.0f);
        tessellator.addVertex(x - height * cosA, y + height * sinA, 0.0f);
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public boolean canApplyTheme() {
        return true;
    }
}
