package gregtech.common.gui.modularui.widget;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.GlStateManager;
import com.cleanroommc.modularui.utils.Platform;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InterpolatedUITexture implements IDrawable {

    private final UITexture first;
    private final UITexture second;

    private Interpolation interpolation = Interpolation.FADE_OVER;
    private UITexture selected;
    private int duration;
    private int timer;

    public InterpolatedUITexture(UITexture first, UITexture second) {
        this.first = first;
        this.second = second;

        // Start with first selected
        this.selected = this.first;
    }

    public InterpolatedUITexture fadeOutThenIn() {
        this.interpolation = Interpolation.FADE_OUT_THEN_IN;
        return this;
    }

    public InterpolatedUITexture fadeOver() {
        this.interpolation = Interpolation.FADE_OVER;
        return this;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void draw(GuiContext context, int x, int y, int width, int height, WidgetTheme widgetTheme) {
        if (duration > 0) {
            if (timer == 0) {
                timer = duration;
            }

            // Calculate alpha
            float oldAlpha = interpolation.getOldAlpha(duration, timer);
            float newAlpha = interpolation.getNewAlpha(duration, timer);

            if (selected == first) {
                // Fade the first into the second
                drawWithAlpha(context, x, y, width, height, widgetTheme, first, oldAlpha);
                drawWithAlpha(context, x, y, width, height, widgetTheme, second, newAlpha);
            } else {
                // Fade the second into the first
                drawWithAlpha(context, x, y, width, height, widgetTheme, second, oldAlpha);
                drawWithAlpha(context, x, y, width, height, widgetTheme, first, newAlpha);
            }

            timer--;
            if (timer == 0) {
                // Swap the texture when the timer is done
                selected = selected == first ? second : first;
                // And reset the duration
                duration = 0;
            }
        } else {
            selected.draw(context, x, y, width, height, widgetTheme);
        }
    }

    private void drawWithAlpha(GuiContext context, int x, int y, int width, int height, WidgetTheme theme,
        UITexture tex, float alpha) {
        Platform.setupDrawTex(tex.location, true);
        float x1 = x + width, y1 = y + height;
        float u0 = tex.u0, v0 = tex.v0;
        float u1 = tex.u1, v1 = tex.v1;

        // spotless:off
        Platform.startDrawing(Platform.DrawMode.QUADS, Platform.VertexFormat.POS_TEX, buffer -> {
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            buffer.pos(x, y1, 0).tex(u0, v1).color(1, 1, 1, alpha).endVertex();
            buffer.pos(x1, y1, 0).tex(u1, v1).color(1, 1, 1, alpha).endVertex();
            buffer.pos(x1, y, 0).tex(u1, v0).color(1, 1, 1, alpha).endVertex();
            buffer.pos(x, y, 0).tex(u0, v0).color(1, 1, 1, alpha).endVertex();
        });
        // spotless:on
    }

    public void toggle(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean canApplyTheme() {
        return selected.canApplyTheme();
    }

    private enum Interpolation {

        FADE_OVER {

            @Override
            float getOldAlpha(int duration, int timer) {
                return 1;
            }

            @Override
            float getNewAlpha(int duration, int timer) {
                return (duration - timer) * 1.0f / duration;
            }
        },
        FADE_OUT_THEN_IN {

            @Override
            float getOldAlpha(int duration, int timer) {
                float halfDuration = duration / 2.0f;
                if (timer <= halfDuration) return 0;
                return (timer - halfDuration) / halfDuration;
            }

            @Override
            float getNewAlpha(int duration, int timer) {
                float halfDuration = duration / 2.0f;
                if (timer >= halfDuration) return 0;
                return (halfDuration - timer) / halfDuration;
            }
        };

        abstract float getOldAlpha(int duration, int timer);

        abstract float getNewAlpha(int duration, int timer);
    }
}
