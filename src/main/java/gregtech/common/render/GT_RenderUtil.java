package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

public class GT_RenderUtil {

    public static void colorGTItem(ItemStack aStack) {
        if (aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem) {

            short[] tModulation = aItem.getRGBa(aStack);
            GL11.glColor4f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F, 255.0f);
        } else {
            System.out.println("WARNING: " + aStack.getDisplayName() + " does not have an associated GT colour.");
        }
    }

    public static void renderBlockIcon(RenderBlocks aRenderer, Block aBlock, double aX, double aY, double aZ,
        IIcon aIcon, byte aSide) {
        switch (aSide) {
            case 0 -> {
                aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, aIcon);
                return;
            }
            case 1 -> {
                aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, aIcon);
                return;
            }
            case 2 -> {
                aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, aIcon);
                return;
            }
            case 3 -> {
                aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, aIcon);
                return;
            }
            case 4 -> {
                aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, aIcon);
                return;
            }
            case 5 -> {
                aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, aIcon);
                return;
            }
        }
    }

    public static void renderItemIcon(IIcon icon, double size, double z, float nx, float ny, float nz) {
        renderItemIcon(icon, 0.0D, 0.0D, size, size, z, nx, ny, nz);
    }

    public static void renderItemIcon(IIcon icon, double xStart, double yStart, double xEnd, double yEnd, double z,
        float nx, float ny, float nz) {
        if (icon == null) {
            return;
        }
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(nx, ny, nz);
        if (nz > 0.0F) {
            Tessellator.instance.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
        } else {
            Tessellator.instance.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
        }
        Tessellator.instance.draw();
    }
}
