package gtPlusPlus.core.item.general.matterManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.TextRenderer;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.api.widget.Widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class RadialMenu extends Widget implements Interactable {

    public List<RadialMenuOption> options = new ArrayList<>();
    public float innerRadius = 0.25f, outerRadius = 0.60f;
    public IDrawable innerIcon;

    @Override
    public Pos2d getPos() {
        return new Pos2d(0, 0);
    }

    @Override
    public Size getSize() {
        return getContext().getScaledScreenSize();
    }

    @Override
    public void draw(float partialTicks) {
        Minecraft.getMinecraft().mcProfiler.startSection("radial menu");
        
        double weightSum = 0;

        for(RadialMenuOption option : options) {
            option.isHidden = option.hidden.getAsBoolean();

            if (!option.isHidden) {
                weightSum += option.weight;
            }
        }

        double currentAngle = 0;

        for(int i = 0; i < options.size(); i++) {
            RadialMenuOption option = options.get(i);

            if (option.isHidden) {
                option.startTheta = 0;
                option.endTheta = 0;
                continue;
            }

            double sliceSize = option.weight / weightSum * Math.PI * 2;

            option.startTheta = currentAngle;
            currentAngle += sliceSize;
            option.endTheta = currentAngle;
        }

        if(!options.isEmpty()) {
            double offset = (options.get(0).startTheta - options.get(0).endTheta) / 2;
    
            for(RadialMenuOption option : options) {
                option.startTheta -= offset;
                option.endTheta -= offset;
            }
        }

        this.pos = getPos();
        this.size = getSize();

        GlStateManager.pushMatrix();

        GlStateManager.translate(
            pos.getX() + size.width / 2,
            pos.getY() + size.height /2,
            0
        );

        if(innerIcon != null) {
            innerIcon.draw(0, 0, 0, 0, partialTicks);
        }

        int dim = Math.min(size.width, size.height);

        // convert from screen space into a centered square w/ bounds [-1, 1] space
        GlStateManager.scale(dim / 2, dim / 2, 1);

        Vector2d mouse = getMousePosition();
        double mouseRadius = mouse.x;
        double mouseTheta = mouse.y;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        for(RadialMenuOption option : options) {
            if (option.isHidden) {
                continue;
            }
            
            boolean isHoveredOver =
                mouseRadius >= innerRadius && mouseRadius <= outerRadius &&
                isAngleBetween(mouseTheta, option.startTheta, option.endTheta);

            if(isHoveredOver) {
                GL11.glColor4f(0.25f, 0.25f, 0.25f, 1f);
            } else {
                GL11.glColor4f(0f, 0f, 0f, 1f);
            }

            GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
        
            double step = Math.PI / 32;

            for(int i = 0; true; i++) {
                double t = option.startTheta + i * step;

                radialVertex(outerRadius, Math.min(Math.max(t, option.startTheta), option.endTheta));
                radialVertex(innerRadius, Math.min(Math.max(t, option.startTheta), option.endTheta));

                if(t > option.endTheta) {
                    break;
                }
            }

            GlStateManager.glEnd();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GlStateManager.popMatrix();

        for(RadialMenuOption option : options) {
            if (option.isHidden) {
                continue;
            }

            radialText(
                (innerRadius + outerRadius) / 2,
                (option.startTheta + option.endTheta) / 2,
                50,
                0xFFCCCCCC,
                option.label.get()
            );
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    public ClickResult onClick(int mouseButton, boolean doubleClick) {
        Vector2d mouse = getMousePosition();
        double mouseRadius = mouse.x;
        double mouseTheta = mouse.y;

        for(RadialMenuOption option : options) {
            boolean isHoveredOver =
                mouseRadius >= innerRadius && mouseRadius <= outerRadius &&
                isAngleBetween(mouseTheta, option.startTheta, option.endTheta);

            if(isHoveredOver) {
                if(option.hidden.getAsBoolean()) {
                    return ClickResult.ACKNOWLEDGED;
                } else {
                    option.onClick.onClick(this, option, mouseButton, doubleClick);
                    return ClickResult.ACCEPT;
                }
            }
        }

        return ClickResult.IGNORE;
    }

    private static double fmod(double value, double divisor) {
        return (value % divisor + divisor) % divisor;
    }

    private static boolean isAngleBetween(double target, double angle1, double angle2) {
        target = fmod(target, Math.PI * 2);
        angle1 = fmod(angle1, Math.PI * 2);
        angle2 = fmod(angle2, Math.PI * 2);

        // make the angle from angle1 to angle2 to be <= 180 degrees
        double rAngle = fmod(angle2 - angle1, Math.PI * 2);

        if (rAngle >= Math.PI) {
            double temp = angle1;
            angle1 = angle2;
            angle2 = temp;
        }
        
        // check if it passes through zero
        if (angle1 <= angle2) {
            return target >= angle1 && target <= angle2;  
        } else {
            return target >= angle1 || target <= angle2;
        }
    }

    private Vector2d getMousePosition() {
        Pos2d pos = getPos();
        Size size = getSize();

        int dim = Math.min(size.width, size.height);

        double mx = (double)this.getContext().getMousePos().x - pos.getX();
        double my = (double)this.getContext().getMousePos().y - pos.getY();

        mx = map(mx, size.width / 2 - dim / 2, size.width / 2 + dim / 2, -1, 1);
        my = map(my, size.height / 2 - dim / 2, size.height / 2 + dim / 2, -1, 1);

        double mouseRadius = Math.sqrt(mx * mx + my * my);
        double mouseTheta = (Math.atan2(my, mx) + Math.PI * 2) % (Math.PI * 2);

        return new Vector2d(mouseRadius, mouseTheta);
    }

    private void radialText(double radius, double theta, int wrapWidth, int color, String text) {
        FontRenderer renderer = TextRenderer.getFontRenderer();

        Size size = getSize();

        int dim = Math.min(size.width, size.height);

        int x = (int)map(Math.cos(theta) * radius, -1, 1, size.width / 2 - dim / 2, size.width / 2 + dim / 2);
        int y = (int)map(Math.sin(theta) * radius, -1, 1, size.height / 2 - dim / 2, size.height / 2 + dim / 2);

        List<String> lines = renderer.listFormattedStringToWidth(text, wrapWidth);

        int boundsX = 0;
        int boundsY = lines.size() * renderer.FONT_HEIGHT;

        for(String line : lines) {
            boundsX = Math.max(boundsX, renderer.getStringWidth(line));
        }

        int nextY = y - boundsY / 2;

        for(String line : lines) {
            int width = renderer.getStringWidth(line);
            int height = renderer.FONT_HEIGHT;

            int paddingX = (boundsX - width) / 2;

            renderer.drawString(line, x - boundsX / 2 + paddingX, nextY, color);

            nextY += height;
        }
    }

    private static double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private static void radialVertex(double radius, double theta) {
        double x = Math.cos(theta) * radius;
        double y = Math.sin(theta) * radius;

        GlStateManager.glVertex3f((float)x, (float)y, 1);
    }

    public static class RadialMenuOption {
        public Supplier<String> label;
        public double weight = 1;

        public BooleanSupplier hidden = () -> false;

        boolean isHidden;

        public RadialMenuClickHandler onClick;

        public double startTheta, endTheta;
    }

    public static interface RadialMenuClickHandler {
        void onClick(RadialMenu menu, RadialMenuOption option, int mouseButton, boolean doubleClicked);
    }
}