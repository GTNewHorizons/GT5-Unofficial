package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.LightingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;

public class FrameBoxSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

    public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

    public FrameBoxSimpleBlockRenderer() {
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int ID, int modelId, RenderBlocks renderer) {

        Tessellator tessellator = Tessellator.instance;

        ModernMaterial material = materialIDToMaterial.getOrDefault(ID, materialIDToMaterial.get(2));

        if (material == null) return;

        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tessellator.startDrawingQuads();
        drawBlock(Blocks.stone, ID, renderer);
        tessellator.draw();

        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

        tessellator.startDrawingQuads();
        drawBlock(block, ID, renderer);
        tessellator.draw();

        GL11.glPopMatrix();
    }

    public static void drawBlock(Block block, int meta, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));

    }


    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return true;
        FrameBoxTileEntity frameBoxTile = (FrameBoxTileEntity) tileEntity;

        int ID = frameBoxTile.getMaterialID();

        ModernMaterial material = materialIDToMaterial.get(ID);
        if (material == null) return true;

        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();
        renderer.renderStandardBlock(Blocks.stone, x, y, z);
        GL11.glColor4f(red / 255f, blue / 255f, green / 255f, 1f);
        renderer.renderStandardBlock(block, x, y, z);
        GL11.glPopMatrix();

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
