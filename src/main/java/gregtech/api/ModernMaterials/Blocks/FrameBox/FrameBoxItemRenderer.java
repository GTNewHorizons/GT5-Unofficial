package gregtech.api.ModernMaterials.Blocks.FrameBox;

import codechicken.lib.render.BlockRenderer;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;

public class FrameBoxItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderBlocks renderer = (RenderBlocks) data[0];

        GL11.glPushMatrix();

        final int ID = itemStack.getItemDamage();

        // Get colour and apply it.
        ModernMaterial material = materialIDToMaterial.get(ID);
        Color color = material.getColor();
        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();
        GL11.glColor4f(red, blue, green, 255);
        renderer.renderBlockAsItem(Block.getBlockFromItem(itemStack.getItem()), ID, 1.0f);

        GL11.glPopMatrix();
    }
}
