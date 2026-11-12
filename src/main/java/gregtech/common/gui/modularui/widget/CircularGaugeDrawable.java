package gregtech.common.gui.modularui.widget;

import java.util.function.DoubleSupplier;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.Widget;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.math.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Draws a gauge pointer hand, rotating around the x&y coordinates of the widget.
 * This is a direct copy of the mui1 gauge arrow because throwing both IDrawables at it makes asWidget clash
 */
public class CircularGaugeDrawable implements IDrawable {

    private final DoubleSupplier progressSupplier;
    private final double minAngle = Math.toRadians(-230.0);
    private final double maxAngle = Math.toRadians(47.0);
    private double lastAngle = Double.NaN;

    public CircularGaugeDrawable(DoubleSupplier progressSupplier) {
        this.progressSupplier = progressSupplier;
    }

    @Override
    @SideOnly(Side.CLIENT)
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

        final double progress = Math.clamp(progressSupplier.getAsDouble(), 0.0, 1.0);
        final double newDialAngle = minAngle + progress * (maxAngle - minAngle);
        if (Double.isNaN(lastAngle)) {
            lastAngle = newDialAngle;
        } else {
            lastAngle = (lastAngle + newDialAngle) / 2.0f;
        }
        final double angle = lastAngle;
        final double sinA = Math.sin(-angle);
        final double cosA = Math.cos(-angle);
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

    @Override
    public Widget<?> asWidget() {
        return new DrawableWidget(this) {

            @Override
            public boolean canHoverThrough() {
                return true;
            }
        };
    }
}
