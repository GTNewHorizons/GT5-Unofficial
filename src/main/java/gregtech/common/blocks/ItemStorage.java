package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;

public class ItemStorage extends ItemBlock {

    public final BlockMetal blockMetal;

    public ItemStorage(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);

        if (block instanceof BlockMetal) {
            this.blockMetal = (BlockMetal) block;
        } else {
            this.blockMetal = null;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        if (this.field_150939_a instanceof BlockMetal) {
            int aDamage = aStack.getItemDamage();
            if (aDamage >= 0 && aDamage < ((BlockMetal) this.field_150939_a).mMats.length) {
                aName = ((BlockMetal) this.field_150939_a).mMats[aDamage].getLocalizedNameForItem(aName);
            }
        }
        return aName;
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);

        blockMetal.addInformation(aStack, aPlayer, aList, aF3_H);
    }
}
