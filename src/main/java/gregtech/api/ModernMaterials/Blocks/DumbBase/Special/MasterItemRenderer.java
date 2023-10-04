package gregtech.api.ModernMaterials.Blocks.DumbBase.Special;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class MasterItemRenderer implements IItemRenderer {


    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    private void repositionItem(ItemRenderType type) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON, EQUIPPED, INVENTORY -> GL11.glTranslated(0.5, 0.5, 0.5);
            case FIRST_PERSON_MAP, ENTITY -> {
                return;
            }
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        // We will do this for each block, to make life more simple.
        repositionItem(type);

        int materialID = itemStack.getItemDamage();
        BaseBlock dumbBlock = (BaseBlock) Block.getBlockFromItem(itemStack.getItem());

        BlocksEnum blockEnum = dumbBlock.getBlockEnum();
        IItemRenderer itemRenderer = blockEnum.getItemRenderer(materialID);

        // Delegate this to the appropriate renderer depending on the material.
        itemRenderer.renderItem(type, itemStack, data);
    }
}
