package gregtech.api.ModernMaterials.Blocks.BlockTypes.OreNormal;

import static gregtech.api.ModernMaterials.Render.Utilities.drawBlock;

import java.awt.*;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;

public class NormalOreSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

    public final int renderID = RenderingRegistry.getNextAvailableRenderId();
    private final Block underlyingBlock;
    private final int underlyingBlockMeta;

    public NormalOreSimpleBlockRenderer(@NotNull Block underlyingBlock, int meta) {
        RenderingRegistry.registerBlockHandler(renderID, this);
        this.underlyingBlock = underlyingBlock;
        this.underlyingBlockMeta = meta;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        Tessellator tessellator = Tessellator.instance;

        final int materialID = ((BaseMaterialBlock) block).getMaterialID(metadata);
        final ModernMaterial material = ModernMaterialUtilities.getMaterialFromID(materialID);

        final Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        drawBlock(underlyingBlock, underlyingBlockMeta, renderer);
        tessellator.draw();

        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

        tessellator.startDrawingQuads();
        drawBlock(block, 0, renderer);
        tessellator.draw();

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {

        BaseMaterialBlock baseMaterialBlock = (BaseMaterialBlock) block;

        int ID = baseMaterialBlock.getMaterialID(world.getBlockMetadata(x, y, z));
        ModernMaterial material = ModernMaterialUtilities.getMaterialFromID(ID);

        if (baseMaterialBlock.getBlockEnum()
            .getSpecialBlockRenderAssociatedMaterials()
            .contains(material)) return true;
        // True tells minecraft that we have handled this and
        // to not do anymore rendering here. This is then handled by a TESR instead.

        renderer.overrideBlockTexture = underlyingBlock.getIcon(0, underlyingBlockMeta);

        renderer.renderStandardBlock(underlyingBlock, x, y, z);
        renderer.overrideBlockTexture = null;

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
