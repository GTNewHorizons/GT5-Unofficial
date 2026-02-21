package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.config.Client;

public class ItemFrames extends ItemBlock {

    public ItemFrames(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
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
        if (this.block() instanceof BlockFrameBox) {
            aName = Materials.getLocalizedNameForItem(aName, aStack.getItemDamage());
        }
        return aName;
    }

    @Override
    public int getColorFromItemStack(ItemStack aStack, int aPass) {
        int meta = aStack.getItemDamage();
        Materials material = BlockFrameBox.getMaterial(meta);
        if (material == null) return 0;
        return (material.mRGBa[0] << 16) | (material.mRGBa[1] << 8) | material.mRGBa[2];
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        if (Client.tooltip.showFormula) {
            aList.add(GTLanguageManager.getTranslation("gt.blockframes." + aStack.getItemDamage() + ".tooltip"));
        }
        aList.add(GTLanguageManager.getTranslation("gt.blockmachines.gt_frame.desc.format"));
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
