package gregtech.common.render.items;

import static gregtech.common.render.GT_RenderUtil.colourGTItem;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.GT_Mod;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class GlitchRenderer extends GT_GeneratedMaterial_Renderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    private static final Pos2d[] points = {
        new Pos2d(2.0, 0.0),
        new Pos2d(1.996, 0.127),
        new Pos2d(1.984, 0.253),
        new Pos2d(1.964, 0.379),
        new Pos2d(1.936, 0.502),
        new Pos2d(1.9, 0.624),
        new Pos2d(1.857, 0.743),
        new Pos2d(1.806, 0.86),
        new Pos2d(1.748, 0.972),
        new Pos2d(1.683, 1.081),
        new Pos2d(1.611, 1.186),
        new Pos2d(1.532, 1.286),
        new Pos2d(1.447, 1.38),
        new Pos2d(1.357, 1.469),
        new Pos2d(1.261, 1.552),
        new Pos2d(1.16, 1.629),
        new Pos2d(1.054, 1.699),
        new Pos2d(0.945, 1.763),
        new Pos2d(0.831, 1.819),
        new Pos2d(0.714, 1.868),
        new Pos2d(0.594, 1.91),
        new Pos2d(0.472, 1.944),
        new Pos2d(0.347, 1.97),
        new Pos2d(0.222, 1.988),
        new Pos2d(0.095, 1.998),
        new Pos2d(-0.032, 2.0),
        new Pos2d(-0.158, 1.994),
        new Pos2d(-0.285, 1.98),
        new Pos2d(-0.41, 1.958),
        new Pos2d(-0.533, 1.928),
        new Pos2d(-0.654, 1.89),
        new Pos2d(-0.773, 1.845),
        new Pos2d(-0.888, 1.792),
        new Pos2d(-1.0, 1.732),
        new Pos2d(-1.108, 1.665),
        new Pos2d(-1.211, 1.592),
        new Pos2d(-1.31, 1.511),
        new Pos2d(-1.403, 1.425),
        new Pos2d(-1.491, 1.334),
        new Pos2d(-1.572, 1.236),
        new Pos2d(-1.647, 1.134),
        new Pos2d(-1.716, 1.027),
        new Pos2d(-1.778, 0.916),
        new Pos2d(-1.832, 0.802),
        new Pos2d(-1.879, 0.684),
        new Pos2d(-1.919, 0.563),
        new Pos2d(-1.951, 0.441),
        new Pos2d(-1.975, 0.316),
        new Pos2d(-1.991, 0.19),
        new Pos2d(-1.999, 0.063),
        new Pos2d(-1.999, -0.063),
        new Pos2d(-1.991, -0.19),
        new Pos2d(-1.975, -0.316),
        new Pos2d(-1.951, -0.441),
        new Pos2d(-1.919, -0.563),
        new Pos2d(-1.879, -0.684),
        new Pos2d(-1.832, -0.802),
        new Pos2d(-1.778, -0.916),
        new Pos2d(-1.716, -1.027),
        new Pos2d(-1.647, -1.134),
        new Pos2d(-1.572, -1.236),
        new Pos2d(-1.491, -1.334),
        new Pos2d(-1.403, -1.425),
        new Pos2d(-1.31, -1.511),
        new Pos2d(-1.211, -1.592),
        new Pos2d(-1.108, -1.665),
        new Pos2d(-1.0, -1.732),
        new Pos2d(-0.888, -1.792),
        new Pos2d(-0.773, -1.845),
        new Pos2d(-0.654, -1.89),
        new Pos2d(-0.533, -1.928),
        new Pos2d(-0.41, -1.958),
        new Pos2d(-0.285, -1.98),
        new Pos2d(-0.158, -1.994),
        new Pos2d(-0.032, -2.0),
        new Pos2d(0.095, -1.998),
        new Pos2d(0.222, -1.988),
        new Pos2d(0.347, -1.97),
        new Pos2d(0.472, -1.944),
        new Pos2d(0.594, -1.91),
        new Pos2d(0.714, -1.868),
        new Pos2d(0.831, -1.819),
        new Pos2d(0.945, -1.763),
        new Pos2d(1.054, -1.699),
        new Pos2d(1.16, -1.629),
        new Pos2d(1.261, -1.552),
        new Pos2d(1.357, -1.469),
        new Pos2d(1.447, -1.38),
        new Pos2d(1.532, -1.286),
        new Pos2d(1.611, -1.186),
        new Pos2d(1.683, -1.081),
        new Pos2d(1.748, -0.972),
        new Pos2d(1.806, -0.86),
        new Pos2d(1.857, -0.743),
        new Pos2d(1.9, -0.624),
        new Pos2d(1.936, -0.502),
        new Pos2d(1.964, -0.379),
        new Pos2d(1.984, -0.253),
        new Pos2d(1.996, -0.127),
        new Pos2d(2.0, -0.0)
    };

    private static final int circleRadius = 1;

    public Random rand = new Random();

    private final ArrayList<Integer> index = new ArrayList<Integer>() {
        {
            add(12);
            add(42);
            add(90);
            add(51);
        }
    };

    @Override
    public void renderRegularItem(ItemRenderType type, ItemStack item, IIcon icon, boolean shouldModulateColor) {

        long animationTicks = (GT_Mod.gregtechproxy.getAnimationTicks() * 4);

        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        colourGTItem(item);

        {
            // Draw actual item item.

            //            t.startDrawingQuads();
            //            t.addVertexWithUV(circleRadius+points[index.get(0)].x, circleRadius+points[index.get(0)].y, 0,
            // icon.getMinU(), icon.getMinV());
            //            t.addVertexWithUV(circleRadius+points[index.get(1)].x, 16-circleRadius+points[index.get(1)].y,
            // 0, icon.getMinU(), icon.getMaxV());
            //            t.addVertexWithUV(16-circleRadius+points[index.get(2)].x,
            // 16-circleRadius+points[index.get(2)].y, 0, icon.getMaxU(), icon.getMaxV());
            //            t.addVertexWithUV(16-circleRadius+points[index.get(3)].x, circleRadius+points[index.get(3)].y,
            // 0, icon.getMaxU(), icon.getMinV());
            //            t.draw();

            double padding = 0.0d;

            t.startDrawingQuads();
            t.addVertexWithUV(
                    -padding + circleRadius + points[(int) ((animationTicks + index.get(0)) % points.length)].x,
                    -padding + circleRadius + points[(int) ((animationTicks + index.get(0)) % points.length)].y,
                    0,
                    icon.getMinU(),
                    icon.getMinV());
            t.addVertexWithUV(
                    -padding + circleRadius + points[(int) ((animationTicks + index.get(1)) % points.length)].x,
                    padding + 16 - circleRadius + points[(int) ((animationTicks + index.get(1)) % points.length)].y,
                    0,
                    icon.getMinU(),
                    icon.getMaxV());
            t.addVertexWithUV(
                    padding + 16 - circleRadius + points[(int) ((animationTicks + index.get(2)) % points.length)].x,
                    padding + 16 - circleRadius + points[(int) ((animationTicks + index.get(2)) % points.length)].y,
                    0,
                    icon.getMaxU(),
                    icon.getMaxV());
            t.addVertexWithUV(
                    padding + 16 - circleRadius + points[(int) ((animationTicks + index.get(3)) % points.length)].x,
                    -padding + circleRadius + points[(int) ((animationTicks + index.get(3)) % points.length)].y,
                    0,
                    icon.getMaxU(),
                    icon.getMinV());
            t.draw();

            ////             Increment around the circle for each point.
            //            for (int i = 0; i < 4; i++) {
            //                index.set(i, index.get(i) + 1);
            //                if (index.get(i) > points.length - 1) {
            //                    index.set(i, 0);
            //                }
            //            }

        }

        GL11.glPopMatrix();
    }
}
