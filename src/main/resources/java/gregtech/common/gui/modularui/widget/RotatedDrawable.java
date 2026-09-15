package gregtech.common.gui.modularui.widget;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.GlStateManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RotatedDrawable implements IDrawable {

    private final IDrawable drawable;
    private float rotation;

    public RotatedDrawable(IDrawable drawable) {
        this.drawable = drawable;
    }

    /**
     * @param degree 0° - 360°, clockwise
     */
    public RotatedDrawable rotationDegree(float degree) {
        this.rotation = degree;
        return this;
    }

    /**
     * @param rad 0 rad - 2π rad, clockwise
     */
    public RotatedDrawable rotationRadian(float rad) {
        return rotationDegree(180.0f / (float) Math.PI * rad);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiContext context, int x, int y, int width, int height, WidgetTheme widgetTheme) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2.0f, height / 2.0f, 0);
        GlStateManager.rotate(rotation, 0, 0, 1);
        GlStateManager.translate(-width / 2.0f, -height / 2.0f, 0);
        drawable.draw(context, x, y, width, height, widgetTheme);
        GlStateManager.popMatrix();
    }
}
