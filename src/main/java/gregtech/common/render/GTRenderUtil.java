package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

public class GTRenderUtil {

    public static void colorGTItem(ItemStack aStack) {
        if (aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem) {

            short[] tModulation = aItem.getRGBa(aStack);
            GL11.glColor4f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F, 255.0f);
        } else {
            System.out.println("WARNING: " + aStack.getDisplayName() + " does not have an associated GT colour.");
        }
    }

    public static void renderBlockIcon(RenderBlocks aRenderer, Block aBlock, double aX, double aY, double aZ,
        IIcon aIcon, ForgeDirection side) {
        switch (side) {
            case DOWN -> aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, aIcon);
            case UP -> aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, aIcon);
            case NORTH -> aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, aIcon);
            case SOUTH -> aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, aIcon);
            case WEST -> aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, aIcon);
            case EAST -> aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, aIcon);
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
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setNormal(nx, ny, nz);
        if (nz > 0.0F) {
            tess.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
            tess.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            tess.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            tess.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
        } else {
            tess.addVertexWithUV(xStart, yEnd, z, icon.getMinU(), icon.getMaxV());
            tess.addVertexWithUV(xEnd, yEnd, z, icon.getMaxU(), icon.getMaxV());
            tess.addVertexWithUV(xEnd, yStart, z, icon.getMaxU(), icon.getMinV());
            tess.addVertexWithUV(xStart, yStart, z, icon.getMinU(), icon.getMinV());
        }
        tess.draw();
    }

    @SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
    public static void renderItem(IItemRenderer.ItemRenderType type, IIcon icon) {
        Tessellator tess = Tessellator.instance;
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float minU = icon.getMinU();
        float maxV = icon.getMaxV();

        switch (type) {
            case ENTITY -> {
                if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                    ItemRenderer.renderItemIn2D(
                        tess,
                        maxU,
                        minV,
                        minU,
                        maxV,
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        0.0625F);
                } else {
                    GL11.glPushMatrix();

                    if (!RenderItem.renderInFrame) {
                        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                    }

                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    tess.addVertexWithUV(0.0F - 0.5F, 0.0F - 0.25F, 0.0D, minU, maxV);
                    tess.addVertexWithUV(1.0F - 0.5F, 0.0F - 0.25F, 0.0D, maxU, maxV);
                    tess.addVertexWithUV(1.0F - 0.5F, 1.0F - 0.25F, 0.0D, maxU, minV);
                    tess.addVertexWithUV(0.0F - 0.5F, 1.0F - 0.25F, 0.0D, minU, minV);
                    tess.draw();

                    GL11.glPopMatrix();
                }
            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                ItemRenderer
                    .renderItemIn2D(tess, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
            }
            case INVENTORY -> {
                renderItemIcon(icon, 16.0D, 0.001, 0.0F, 0.0F, -1.0F);
            }
            default -> {}
        }
    }

    public static void applyStandardItemTransform(IItemRenderer.ItemRenderType type) {
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                // Magic numbers calculated from vanilla code
                GL11.glScalef(1.025641F, 1.025641F, 1.025641F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
            }

            if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                if (RenderItem.renderInFrame) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }
                // Magic numbers calculated from vanilla code
                GL11.glTranslatef(-0.5F, -0.25F, 0.0421875F);
            }
        }
    }

    public static void undoStandardItemTransform(IItemRenderer.ItemRenderType type) {
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                GL11.glTranslatef(0.5F, 0.25F, -0.0421875F); // negative of pre-transform
                // if RenderItem.renderInFrame: rotate -180 (undo the frame rotation)
                if (RenderItem.renderInFrame) {
                    GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
                }
            }
            if (RenderItem.renderInFrame) {
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                GL11.glScalef(1F / 1.025641F, 1F / 1.025641F, 1F / 1.025641F);
            }
        }
        // You can add more undo logic for other types if needed (EQUIPPED, etc)
    }
}
