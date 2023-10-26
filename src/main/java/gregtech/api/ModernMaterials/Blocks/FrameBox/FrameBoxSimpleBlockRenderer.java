package gregtech.api.ModernMaterials.Blocks.FrameBox;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;

import java.awt.*;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumb;
import gregtech.api.ModernMaterials.ModernMaterial;

public class FrameBoxSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

    public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

    public FrameBoxSimpleBlockRenderer() {
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        Tessellator tessellator = Tessellator.instance;

        final int materialID = ((NewDumb) block).getMaterialID(metadata);
        final ModernMaterial material = materialIDToMaterial.getOrDefault(materialID, materialIDToMaterial.get(2));

        if (material == null) return;

        final Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

        tessellator.startDrawingQuads();
        drawBlock(block, materialID, renderer);
        tessellator.draw();

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {

        NewDumb newDumb = (NewDumb) block;

        int ID = newDumb.getMaterialID(world.getBlockMetadata(x, y, z));
        if (newDumb.getBlockEnum().getSpecialBlockRenderAssociatedMaterials().contains(materialIDToMaterial.get(ID))) return true; // True tells minecraft that we have handled this and to not do anymore rendering here.

        renderer.renderStandardBlock(block, x, y, z);
        /*
         * Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         * NewDumb newDumb = (NewDumb) world.getBlock(x, y, z);
         * int ID = newDumb.getMaterialID(world.getBlockMetadata(x, y, z));
         * ModernMaterial material = materialIDToMaterial.get(ID);
         * if (material == null) return true;
         * Color color = material.getColor();
         * int red = color.getRed();
         * int green = color.getGreen();
         * int blue = color.getBlue();
         * GL11.glPushMatrix();
         * GL11.glTranslated(1, 1, 1);
         * //GL11.glColor4f(red / 255f, blue / 255f, green / 255f, 1f);
         * GL11.glColor4f(1.0F, 0.5F, 0.5F, 1.0F);
         * renderer.renderStandardBlock(block, x, y, z);
         * GL11.glPopMatrix();
         */
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelID) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

}
