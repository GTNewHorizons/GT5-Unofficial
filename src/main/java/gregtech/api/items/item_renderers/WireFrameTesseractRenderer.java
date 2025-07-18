package gregtech.api.items.item_renderers;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.common.render.GTRenderUtil;

public class WireFrameTesseractRenderer implements IItemRenderer {

    float red;
    float green;
    float blue;

    public WireFrameTesseractRenderer(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        GTRenderUtil.undoStandardItemTransform(type);

        // Transform based on context
        switch (type) {
            case EQUIPPED:
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.8f, 0.6f, 0.6f);
        }
        GL11.glScalef(0.7f, 0.7f, 0.7f);

        drawWireframeTesseract();

        GL11.glPopMatrix();
    }

    public void drawWireframeTesseract() {
        // 16 vertices of a 4D tesseract
        double[][] points4d = new double[16][4];
        for (int i = 0; i < 16; i++) {
            points4d[i][0] = (i & 1) == 0 ? -1 : 1;
            points4d[i][1] = (i & 2) == 0 ? -1 : 1;
            points4d[i][2] = (i & 4) == 0 ? -1 : 1;
            points4d[i][3] = (i & 8) == 0 ? -1 : 1;
        }

        // Animate with time
        long tick = System.currentTimeMillis();
        double angleA = (tick % 8000) / 8000.0 * Math.PI * 2;
        double angleB = (tick % 6000) / 6000.0 * Math.PI * 2;

        // 4D rotation: rotate in (XW), and (YZ) planes for effect
        double[][] proj3d = new double[16][3];
        for (int i = 0; i < 16; i++) {
            double[] v = points4d[i].clone();

            // Rotate XW plane
            double x = v[0], w = v[3];
            v[0] = x * Math.cos(angleA) - w * Math.sin(angleA);
            v[3] = x * Math.sin(angleA) + w * Math.cos(angleA);

            // Rotate YZ plane
            double y = v[1], z = v[2];
            v[1] = y * Math.cos(angleB) - z * Math.sin(angleB);
            v[2] = y * Math.sin(angleB) + z * Math.cos(angleB);

            // Perspective project to 3D from 4D (simple perspective)
            double distance = 3.0;
            double wPersp = 1 / (distance - v[3]);
            proj3d[i][0] = v[0] * wPersp;
            proj3d[i][1] = v[1] * wPersp;
            proj3d[i][2] = v[2] * wPersp;
        }

        // 32 edges: connect vertices differing by one bit
        int[][] edges = new int[32][2];
        int e = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                int b = i ^ (1 << j);
                if (i < b) edges[e++] = new int[] { i, b };
            }
        }

        // --- BRIGHT COLOR and FULLBRIGHT RENDERING ---
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(2.0F);

        // Determines tesseract colour
        GL11.glColor3f(red, green, blue);

        Tessellator t = Tessellator.instance;
        t.startDrawing(GL11.GL_LINES);
        for (int[] edge : edges) {
            for (int k = 0; k < 2; k++) {
                double[] v = proj3d[edge[k]];
                t.addVertex(v[0], v[1], v[2]);
            }
        }
        t.draw();

        // Restore GL states
        GL11.glLineWidth(2.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1, 1, 1);
        if (lighting) GL11.glEnable(GL11.GL_LIGHTING);
    }
}
