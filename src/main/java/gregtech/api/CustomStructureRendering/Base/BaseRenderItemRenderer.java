package gregtech.api.CustomStructureRendering.Base;

import gregtech.api.CustomStructureRendering.Base.Util.RenderHelper;
import gregtech.api.CustomStructureRendering.Structures.BaseModelStructure;
import gregtech.api.CustomStructureRendering.Trophies.Trophies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import static gregtech.api.CustomStructureRendering.Base.BaseRenderTESR.MODEL_NAME_NBT_TAG;


public class BaseRenderItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    protected BaseModelStructure getModel(String modelName) {
        return Trophies.getModel(modelName);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        RenderBlocks renderBlocks = (RenderBlocks) data[0];
        World world = (World) renderBlocks.blockAccess;

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        if (item.hasTagCompound() && item.getTagCompound().hasKey(MODEL_NAME_NBT_TAG)) {
            BaseModelStructure modelToRender = getModel(item.getTagCompound().getString(MODEL_NAME_NBT_TAG));

            if (type.equals(ItemRenderType.INVENTORY)) {
                RenderHelper.renderModel(world, 0, -0.1, 0, modelToRender);
            } else if (type.equals(ItemRenderType.ENTITY)) {
                RenderHelper.renderModel(world, -0.5, 0, -0.5, modelToRender);
            } else {
                RenderHelper.renderModel(world, 0, 0, 0, modelToRender);
            }
        }
    }
}
