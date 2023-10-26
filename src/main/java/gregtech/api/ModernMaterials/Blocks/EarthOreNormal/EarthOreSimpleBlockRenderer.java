package gregtech.api.ModernMaterials.Blocks.EarthOreNormal;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;
import static gregtech.api.ModernMaterials.Render.Utilities.drawBlock;

import java.awt.*;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.ModernMaterial;

public class EarthOreSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

    public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

    public EarthOreSimpleBlockRenderer() {
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        Tessellator tessellator = Tessellator.instance;

        final int materialID = ((BaseMaterialBlock) block).getMaterialID(metadata);
        final ModernMaterial material = materialIDToMaterial.getOrDefault(materialID, materialIDToMaterial.get(2));

        if (material == null) return;

        final Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        drawBlock(Blocks.stone, materialID, renderer);
        tessellator.draw();

        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

        tessellator.startDrawingQuads();
        drawBlock(block, materialID, renderer);
        tessellator.draw();

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {

        BaseMaterialBlock baseMaterialBlock = (BaseMaterialBlock) block;

        int ID = baseMaterialBlock.getMaterialID(world.getBlockMetadata(x, y, z));
        if (baseMaterialBlock.getBlockEnum()
            .getSpecialBlockRenderAssociatedMaterials()
            .contains(materialIDToMaterial.get(ID))) return true;
        // True tells minecraft that we have handled this and
        // to not do anymore rendering here. This is then handled by a TESR instead.

        renderer.renderStandardBlock(Blocks.stone, x, y, z);
        renderer.renderStandardBlock(block, x, y, z);

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
