package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;

public class ItemStorage extends ItemBlock {

    public ItemStorage(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (this.field_150939_a instanceof BlockStorage storage) {
            return storage.getLocalizedName(stack.getItemDamage());
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }
}
