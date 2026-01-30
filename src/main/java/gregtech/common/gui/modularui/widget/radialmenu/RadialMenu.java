package gregtech.common.gui.modularui.widget.radialmenu;

import static java.lang.Math.PI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.sizer.Area;

import cpw.mods.fml.relauncher.Side;

/**
 * A radial menu widget that fills the whole screen.
 */
public class RadialMenu extends Widget<RadialMenu> implements Interactable {

    private static final double TAU = PI * 2;

    public List<RadialMenuOption> options = new ArrayList<>();
    public float innerRadius = 0.25f, outerRadius = 0.60f;
    /**
     * An icon to draw in the centre of the menu, or null to skip it.
     */
    public IDrawable innerIcon;

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        Minecraft.getMinecraft().mcProfiler.startSection("gt5u native radial menu");

        double weightSum = 0;

        // calculate the total weight sum
        for (RadialMenuOption option : options) {
            option.isHidden = option.hidden.getAsBoolean();

            if (!option.isHidden) {
                weightSum += option.weight;
            }
        }

        double currentAngle = 0;

        // lay out the options
        for (RadialMenuOption option : options) {
            if (option.isHidden) {
                option.startTheta = 0;
                option.endTheta = 0;
                continue;
            }

            double sliceSize = option.weight / weightSum * TAU;

            option.startTheta = currentAngle;
            currentAngle += sliceSize;
            option.endTheta = currentAngle;
        }

        RadialMenuOption firstShown = null;

        for (RadialMenuOption option : options) {
            if (!option.isHidden) {
                firstShown = option;
                break;
            }
        }

        // shift the options by half the width of the first option, to make it look better
        if (firstShown != null) {
            double offset = Math.abs(firstShown.startTheta - firstShown.endTheta) / 2;

            for (RadialMenuOption option : options) {
                if (!option.isHidden) {
                    option.startTheta -= offset;
                    option.endTheta -= offset;
                }
            }
        }

        Area area = this.getArea();

        GL11.glPushMatrix();

        GL11.glTranslatef(area.x + area.width / 2f, area.y + area.height / 2f, 0);

        if (innerIcon != null) {
            innerIcon.draw(context, 0, 0, 0, 0, widgetTheme.getTheme());
        }

        int dim = Math.min(area.width, area.height);

        // convert from screen space into a centered square w/ bounds [-1, 1] space
        GL11.glScalef(dim / 2f, dim / 2f, 1);

        Vector2d mouse = getMousePosition();
        double mouseRadius = mouse.x;
        double mouseTheta = mouse.y;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        // draw the options
        for (RadialMenuOption option : options) {
            if (option.isHidden) {
                continue;
            }

            boolean isHoveredOver = mouseRadius >= innerRadius && mouseRadius <= outerRadius
                && isAngleBetween(mouseTheta, option.startTheta, option.endTheta);

            if (isHoveredOver) {
                GL11.glColor4f(0.25f, 0.25f, 0.25f, 1f);
            } else {
                GL11.glColor4f(0f, 0f, 0f, 1f);
            }

            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            double step = PI / 32;

            for (int i = 0; true; i++) {
                double t = option.startTheta + i * step;

                radialVertex(outerRadius, Math.min(Math.max(t, option.startTheta), option.endTheta));
                radialVertex(innerRadius, Math.min(Math.max(t, option.startTheta), option.endTheta));

                if (t > option.endTheta) {
                    break;
                }
            }

            GL11.glEnd();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();

        // draw the options' labels
        for (RadialMenuOption option : options) {
            if (option.isHidden) {
                continue;
            }

            // spotless:off
            double radius = (innerRadius + outerRadius) / 2;
            double theta = (option.startTheta + option.endTheta) / 2;
            int x = (int) map(Math.cos(theta) * radius, -1, 1, area.width / 2f - dim / 2f, area.width / 2f + dim / 2f);
            int y = (int) map(Math.sin(theta) * radius, -1, 1, area.height / 2f - dim / 2f, area.height / 2f + dim / 2f);
            // spotless:on

            option.label.draw(context, x, y, 0, 0, WidgetTheme.whiteTextShadow(18, 18, null));
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        Vector2d mouse = getMousePosition();
        double mouseRadius = mouse.x;
        double mouseTheta = mouse.y;

        for (RadialMenuOption option : options) {
            boolean isHoveredOver = mouseRadius >= innerRadius && mouseRadius <= outerRadius
                && isAngleBetween(mouseTheta, option.startTheta, option.endTheta);

            if (isHoveredOver) {
                if (!option.hidden.getAsBoolean()) {
                    // SoundResource.RANDOM_CLICK.playClient(0.5f, 1f);
                    option.onClick.onClick(this, option, mouseButton, Side.CLIENT);
                }

                return Result.SUCCESS;
            }
        }

        return Result.IGNORE;
    }

    private static double mod_tau(double angle) {
        return ((angle % TAU) + TAU) % TAU;
    }

    private static boolean isAngleBetween(double target, double angle1, double angle2) {
        if (angle2 < angle1) return false;

        while (angle1 < 0) {
            angle1 += TAU;
            angle2 += TAU;
        }

        while (angle1 > TAU) {
            angle1 -= TAU;
            angle2 -= TAU;
        }

        return mod_tau(target - angle1) < angle2 - angle1;
    }

    /**
     * Gets the mouse position in terms of theta and radius, instead of x,y.
     */
    private Vector2d getMousePosition() {
        Area area = getArea();

        int dim = Math.min(area.width, area.height);

        double mx = (double) this.getContext()
            .getMouseX() - area.x();
        double my = (double) this.getContext()
            .getMouseY() - area.y();

        // spotless:off
        mx = map(mx, area.width / 2f - dim / 2f, area.width / 2f + dim / 2f, -1, 1);
        my = map(my, area.height / 2f - dim / 2f, area.height / 2f + dim / 2f, -1, 1);
        // spotless:on

        double mouseRadius = Math.sqrt(mx * mx + my * my);
        double mouseTheta = mod_tau(Math.atan2(my, mx));

        return new Vector2d(mouseRadius, mouseTheta);
    }

    private void radialText(double radius, double theta, int wrapWidth, int color, String text) {
        FontRenderer renderer = TextRenderer.getFontRenderer();

        Area area = getArea();

        int dim = Math.min(area.width, area.height);

        // spotless:off
        int x = (int) map(Math.cos(theta) * radius, -1, 1, area.width / 2f - dim / 2f, area.width / 2f + dim / 2f);
        int y = (int) map(Math.sin(theta) * radius, -1, 1, area.height / 2f - dim / 2f, area.height / 2f + dim / 2f);
        // spotless:on

        List<String> lines = renderer.listFormattedStringToWidth(text, wrapWidth);

        int boundsX = 0;
        int boundsY = lines.size() * renderer.FONT_HEIGHT;

        for (String line : lines) {
            boundsX = Math.max(boundsX, renderer.getStringWidth(line));
        }

        int nextY = y - boundsY / 2;

        for (String line : lines) {
            int width = renderer.getStringWidth(line);
            int height = renderer.FONT_HEIGHT;

            int paddingX = (boundsX - width) / 2;

            renderer.drawString(line, x - boundsX / 2 + paddingX, nextY, color);

            nextY += height;
        }
    }

    // spotless:off
    private static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    // spotless:on

    private static void radialVertex(double radius, double theta) {
        double x = Math.cos(theta) * radius;
        double y = Math.sin(theta) * radius;

        GL11.glVertex3f((float) x, (float) y, 1);
    }

    public static class RadialMenuOption {

        public IIcon label;
        public double weight = 1;

        public BooleanSupplier hidden = () -> false;

        boolean isHidden;

        public RadialMenuClickHandler onClick;

        public double startTheta, endTheta;
    }

    public interface RadialMenuClickHandler {

        void onClick(RadialMenu menu, @Nullable RadialMenuOption option, int mouseButton, Side side);
    }
}
