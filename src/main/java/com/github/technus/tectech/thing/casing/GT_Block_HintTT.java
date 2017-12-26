package com.github.technus.tectech.thing.casing;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.thing.CustomItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Block_HintTT extends GT_Block_Casings_Abstract {
    private static IIcon hint[] = new IIcon[16];

    public GT_Block_HintTT() {
        super(GT_Item_HintTT.class, "gt.blockhintTT", GT_Material_Casings.INSTANCE);

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Hint 1 dot");//id is -1
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Hint 2 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Hint 3 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Hint 4 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Hint 5 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Hint 6 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Hint 7 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Hint 8 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Hint 9 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Hint 10 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Hint 11 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Hint 12 dots");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Hint general");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Hint air");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Hint no air");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Hint error");


        CustomItemList.hint_0.set(new ItemStack(this, 1, 0));
        CustomItemList.hint_1.set(new ItemStack(this, 1, 1));
        CustomItemList.hint_2.set(new ItemStack(this, 1, 2));
        CustomItemList.hint_3.set(new ItemStack(this, 1, 3));
        CustomItemList.hint_4.set(new ItemStack(this, 1, 4));
        CustomItemList.hint_5.set(new ItemStack(this, 1, 5));
        CustomItemList.hint_6.set(new ItemStack(this, 1, 6));
        CustomItemList.hint_7.set(new ItemStack(this, 1, 7));
        CustomItemList.hint_8.set(new ItemStack(this, 1, 8));
        CustomItemList.hint_9.set(new ItemStack(this, 1, 9));
        CustomItemList.hint_10.set(new ItemStack(this, 1, 10));
        CustomItemList.hint_11.set(new ItemStack(this, 1, 11));
        CustomItemList.hint_general.set(new ItemStack(this, 1, 12));
        CustomItemList.hint_air.set(new ItemStack(this, 1, 13));
        CustomItemList.hint_noAir.set(new ItemStack(this, 1, 14));
        CustomItemList.hint_error.set(new ItemStack(this, 1, 15));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        //super.registerBlockIcons(aIconRegister);
        hint[0] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_0");
        hint[1] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_1");
        hint[2] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_2");
        hint[3] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_3");
        hint[4] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_4");
        hint[5] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_5");
        hint[6] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_6");
        hint[7] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_7");
        hint[8] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_8");
        hint[9] =  aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_9");
        hint[10] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_10");
        hint[11] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_11");
        hint[12] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_DEFAULT");
        hint[13] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_AIR");
        hint[14] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_NOAIR");
        hint[15] = aIconRegister.registerIcon(Reference.MODID+":iconsets/HINT_ERROR");
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        return hint[aMeta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 15; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
