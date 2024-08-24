package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public class GT_Item_Frames extends ItemBlock {

    public GT_Item_Frames(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    private Block block() {
        return this.field_150939_a;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.block()
            .getUnlocalizedName() + "."
            + getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        if (this.block() instanceof GT_Block_FrameBox) {
            aName = Materials.getLocalizedNameForItem(aName, aStack.getItemDamage());
        }
        return aName;
    }

    @Override
    public int getColorFromItemStack(ItemStack aStack, int aPass) {
        int meta = aStack.getItemDamage();
        Materials material = GT_Block_FrameBox.getMaterial(meta);
        return (material.mRGBa[0] << 16) | (material.mRGBa[1] << 8) | material.mRGBa[2];
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(GT_LanguageManager.getTranslation("gt.blockframes." + aStack.getItemDamage() + ".tooltip"));
        aList.add(GT_LanguageManager.getTranslation("gt.blockmachines.gt_frame.desc.format"));
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
