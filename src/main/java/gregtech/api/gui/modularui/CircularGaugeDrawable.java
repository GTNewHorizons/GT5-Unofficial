package gregtech.api.gui.modularui;

import java.util.function.DoubleSupplier;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Color;

/**
 * Draws a gauge pointer hand, rotating around the x&y coordinates of the widget.
 */
public class CircularGaugeDrawable implements IDrawable {

    private DoubleSupplier progressSupplier;
    private float minAngle = (float) Math.toRadians(-235.0);
    private float maxAngle = (float) Math.toRadians(45.0);
    private int color = 0xff_431d00;
    private float lastAngle = Float.NaN;

    public CircularGaugeDrawable(DoubleSupplier progressSupplier) {
        this.progressSupplier = progressSupplier;
    }

    @Override
    public void draw(float x0, float y0, float width, float height, float partialTicks) {
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
        height /= 2.0f;

        tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);

        tessellator
            .setColorRGBA(Color.getRed(color), Color.getGreen(color), Color.getBlue(color), Color.getAlpha(color));
        tessellator.addVertex(x0 + width * cosA, y0 - width * sinA, 0.0f);
        tessellator.addVertex(x0 - height * sinA, y0 - height * cosA, 0.0f);
        tessellator.addVertex(x0 + height * sinA, y0 + height * cosA, 0.0f);
        tessellator.addVertex(x0 - height * cosA, y0 + height * sinA, 0.0f);
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
