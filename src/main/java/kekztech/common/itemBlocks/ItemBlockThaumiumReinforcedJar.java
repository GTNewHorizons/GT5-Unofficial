package kekztech.common.itemBlocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockThaumiumReinforcedJar extends ItemBlock {

    public ItemBlockThaumiumReinforcedJar(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
