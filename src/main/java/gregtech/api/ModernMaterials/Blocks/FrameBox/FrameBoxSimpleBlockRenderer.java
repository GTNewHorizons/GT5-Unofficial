package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumb;
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

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;
import static gregtech.api.enums.Mods.GregTech;

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

        //tessellator.startDrawingQuads();
        //drawBlock(Blocks.stone, ID, renderer);
        //tessellator.draw();

        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

        tessellator.startDrawingQuads();
        drawBlock(block, ID, renderer);
        tessellator.draw();

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        NewDumb newDumb = (NewDumb) world.getBlock(x, y, z);
        int ID = newDumb.getMaterialID(world.getBlockMetadata(x, y, z));

        ModernMaterial material = materialIDToMaterial.get(ID);
        if (material == null) return true;

        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();
        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);

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
