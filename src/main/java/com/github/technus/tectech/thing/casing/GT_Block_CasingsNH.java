package com.github.technus.tectech.thing.casing;

import static com.github.technus.tectech.TecTech.creativeTabTecTech;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINECASINGS_BOTTOM_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINECASINGS_SIDE_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINECASINGS_TOP_TT;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Block_CasingsNH extends GT_Block_Casings_Abstract {

    public static boolean mConnectedMachineTextures = true;

    public GT_Block_CasingsNH() {
        super(GT_Item_CasingsNH.class, "gt.blockcasingsNH", GT_Material_Casings.INSTANCE);
        setCreativeTab(creativeTabTecTech);

        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[8][b + 64] = new GT_CopiedBlockTexture(this, 6, b);
            /* IMPORTANT for block recoloring */
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "UEV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "UIV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "UMV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "UXV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "MAX Machine Casing"); // adding

        CustomItemList.Casing_UEV.set(new ItemStack(this, 1, 10));
        CustomItemList.Casing_UIV.set(new ItemStack(this, 1, 11));
        CustomItemList.Casing_UMV.set(new ItemStack(this, 1, 12));
        CustomItemList.Casing_UXV.set(new ItemStack(this, 1, 13));
        CustomItemList.Casing_MAXV.set(new ItemStack(this, 1, 14));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        // super.registerBlockIcons(aIconRegister);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if (ordinalSide == 0) {
            return MACHINECASINGS_BOTTOM_TT[aMeta].getIcon();
        }
        if (ordinalSide == 1) {
            return MACHINECASINGS_TOP_TT[aMeta].getIcon();
        }
        return MACHINECASINGS_SIDE_TT[aMeta].getIcon();
    }
}
