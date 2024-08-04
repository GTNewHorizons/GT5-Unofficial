package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTieredCasingBlocks1 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < 10; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }

    public static class TieredCasingItemBlock extends GregtechMetaCasingItems {

        public TieredCasingItemBlock(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int aMeta = aStack.getItemDamage();
            if (aMeta < 10) {
                aList.add("Tier: " + GT_Values.VN[aMeta]);
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaTieredCasingBlocks1() {
        super(TieredCasingItemBlock.class, "gtplusplus.blocktieredcasings.1", GT_Material_Casings.INSTANCE);
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Integral Encasement I");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Integral Encasement II");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Integral Encasement III");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Integral Encasement IV");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Integral Encasement V");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Integral Framework I");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Integral Framework II");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Integral Framework III");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Integral Framework IV");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Integral Framework V");

        GregtechItemList.GTPP_Casing_ULV.set(new ItemStack(this, 1, 0));
        GregtechItemList.GTPP_Casing_LV.set(new ItemStack(this, 1, 1));
        GregtechItemList.GTPP_Casing_MV.set(new ItemStack(this, 1, 2));
        GregtechItemList.GTPP_Casing_HV.set(new ItemStack(this, 1, 3));
        GregtechItemList.GTPP_Casing_EV.set(new ItemStack(this, 1, 4));
        GregtechItemList.GTPP_Casing_IV.set(new ItemStack(this, 1, 5));
        GregtechItemList.GTPP_Casing_LuV.set(new ItemStack(this, 1, 6));
        GregtechItemList.GTPP_Casing_ZPM.set(new ItemStack(this, 1, 7));
        GregtechItemList.GTPP_Casing_UV.set(new ItemStack(this, 1, 8));
        GregtechItemList.GTPP_Casing_UHV.set(new ItemStack(this, 1, 9));
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if (aMeta < 10) {
            return TexturesGtBlock.TIERED_MACHINE_HULLS[aMeta].getIcon();
        }
        return switch (aMeta) {
            case 10 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            case 11 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            case 12 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            case 13 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            case 14 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            case 15 -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
