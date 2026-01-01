package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

public class WireFrameTesseractRenderer implements IItemRenderer {

    private final int red;
    private final int green;
    private final int blue;

    public WireFrameTesseractRenderer(int red, int green, int blue) {
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

        ItemRenderUtil.undoStandardItemTransform(type);

        // Transform based on context
        final float lineWidth;
        switch (type) {
            case EQUIPPED -> {
                GL11.glTranslatef(0.8f, 0.6f, 0.6f);
                lineWidth = 4.0F;
            }
            case EQUIPPED_FIRST_PERSON -> {
                GL11.glTranslatef(0.8f, 0.6f, 0.6f);
                lineWidth = 10.0F;
            }
            case INVENTORY -> lineWidth = 2.0F;
            default -> lineWidth = 4.0F;
        }
        GL11.glScalef(0.7f, 0.7f, 0.7f);

        drawWireframeTesseract(lineWidth);

        GL11.glPopMatrix();
    }

    private void drawWireframeTesseract(float lineWidth) {
        final long tick = Minecraft.getSystemTime();
        final float angleA = (float) ((tick % 8000) / 8000D * Math.PI * 2D);
        final float angleB = (float) ((tick % 6000) / 6000D * Math.PI * 2D);

        final double cosA = MathHelper.cos(angleA);
        final double sinA = MathHelper.sin(angleA);
        final double cosB = MathHelper.cos(angleB);
        final double sinB = MathHelper.sin(angleB);

        // Save GL state
        final boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        if (lighting) {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(lineWidth);

        Tessellator t = Tessellator.instance;
        t.startDrawing(GL11.GL_LINES);
        t.setColorOpaque(red, green, blue);

        // Iterate over 16 vertices
        for (int i = 0; i < 16; i++) {
            // Original 4D coordinates from bits
            final double x = (i & 1) == 0 ? -1 : 1;
            final double y = (i & 2) == 0 ? -1 : 1;
            final double z = (i & 4) == 0 ? -1 : 1;
            final double w = (i & 8) == 0 ? -1 : 1;

            // Rotate XW
            final double xRot = x * cosA - w * sinA;
            final double wRot = x * sinA + w * cosA;

            // Rotate YZ
            final double yRot = y * cosB - z * sinB;
            final double zRot = y * sinB + z * cosB;

            // Perspective divide
            final double distance = 3.0;
            final double wp = 1.0 / (distance - wRot);
            final double px = xRot * wp;
            final double py = yRot * wp;
            final double pz = zRot * wp;

            // Emit edges: only connect to higher-index neighbors
            for (int j = 0; j < 4; j++) {
                final int neighbor = i ^ (1 << j);
                if (i < neighbor) {
                    // Compute neighbor vertex and transformed position
                    final double xn = (neighbor & 1) == 0 ? -1 : 1;
                    final double yn = (neighbor & 2) == 0 ? -1 : 1;
                    final double zn = (neighbor & 4) == 0 ? -1 : 1;
                    final double wn = (neighbor & 8) == 0 ? -1 : 1;

                    final double xRotN = xn * cosA - wn * sinA;
                    final double wRotN = xn * sinA + wn * cosA;

                    final double yRotN = yn * cosB - zn * sinB;
                    final double zRotN = yn * sinB + zn * cosB;

                    final double wpN = 1.0 / (distance - wRotN);
                    final double pxN = xRotN * wpN;
                    final double pyN = yRotN * wpN;
                    final double pzN = zRotN * wpN;

                    t.addVertex(px, py, pz);
                    t.addVertex(pxN, pyN, pzN);
                }
            }
        }
        t.draw();

        // Restore GL states
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1F, 1F, 1F);
        if (lighting) GL11.glEnable(GL11.GL_LIGHTING);
    }
}
