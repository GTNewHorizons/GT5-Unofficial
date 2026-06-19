package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPad extends ItemBlock {

    public ItemBlockPad(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + damageDropped(getDamage(aStack));
    }

    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }
}
