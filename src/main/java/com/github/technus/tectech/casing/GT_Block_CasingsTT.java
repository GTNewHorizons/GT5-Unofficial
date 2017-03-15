package com.github.technus.tectech.casing;

import com.github.technus.tectech.CustomItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
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
public class GT_Block_CasingsTT
        extends GT_Block_Casings_Abstract {

    private static IIcon eM3,eM4,eM5,eM6,eM7,eM8,eM9;
    private static IIcon debug[]=new IIcon[6];

    public GT_Block_CasingsTT() {

        super(GT_Item_CasingsTT.class, "gt.blockcasingsTT", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.CASING_BLOCKS[(i + 96)] = new GT_CopiedBlockTexture(this, 6, i);
            /*IMPORTANT for block recoloring*/
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Molecular Containment Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Containment Field Generator");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Containment Field Generator Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Molecular Containment Coil");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Teleportation Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Spacetime Altering Casing");//adding

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Debug Sides");//adding


        CustomItemList.eM_Casing.set(new ItemStack(this, 1, 3));//adding
        CustomItemList.eM_Field.set(new ItemStack(this, 1, 4));//adding
        CustomItemList.eM_Field_Casing.set(new ItemStack(this, 1, 5));//adding
        CustomItemList.eM_Coil.set(new ItemStack(this, 1, 6));//adding
        CustomItemList.eM_Tele.set(new ItemStack(this, 1, 7));//adding
        CustomItemList.eM_TimeSpaceWarp.set(new ItemStack(this, 1, 8));

        CustomItemList.debugBlock.set(new ItemStack(this, 1, 9));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        //super.registerBlockIcons(aIconRegister);
        eM3=aIconRegister.registerIcon("gregtech:iconsets/EM_CASING");
        eM4=aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD");
        eM5=aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD_CASING");
        eM6=aIconRegister.registerIcon("gregtech:iconsets/EM_COIL");
        eM7=aIconRegister.registerIcon("gregtech:iconsets/EM_COIL_NONSIDE");
        eM8=aIconRegister.registerIcon("gregtech:iconsets/EM_TELE");
        eM8=aIconRegister.registerIcon("gregtech:iconsets/EM_TELE");
        eM9=aIconRegister.registerIcon("gregtech:iconsets/EM_TIMESPACE");

        debug[0]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_0");
        debug[1]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_1");
        debug[2]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_2");
        debug[3]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_3");
        debug[4]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_4");
        debug[5]=aIconRegister.registerIcon("gregtech:iconsets/DEBUG_5");
    }

    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 3:
                return eM3;
            case 4:
                return eM4;
            case 5:
                return eM5;
            case 6:
                if(aSide<2)return eM7;
                return eM6;
            case 7:
                return eM8;
            case 8:
                return eM9;
            case 9:
                return debug[aSide];
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide,tMeta);
    }

    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return super.colorMultiplier(aWorld, aX, aY, aZ);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for(int i=3;i<=9;i++){
            aList.add(new ItemStack(aItem,1,i));
        }
    }
}
