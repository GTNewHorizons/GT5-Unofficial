package com.github.technus.tectech.thing.casing;

import static com.github.technus.tectech.TecTech.creativeTabTecTech;
import static com.github.technus.tectech.TecTech.tectechTexturePage1;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.github.technus.tectech.thing.CustomItemList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Block_CasingsTT extends GT_Block_Casings_Abstract {

    public static final byte texturePage = tectechTexturePage1;
    public static final short textureOffset = texturePage << 7; // Start of PAGE 8 (which is the 9th page) (8*128)
    private static IIcon eM0, eM1, eM1s, eM2, eM2s, eM3, eM3s, eM4, eM5, eM6, eM7, eM7s, eM8, eM9, eM10, eM11, eM12,
            eM13, eM14;
    private static IIcon[] debug = new IIcon[6];

    public GT_Block_CasingsTT() {
        super(GT_Item_CasingsTT.class, "gt.blockcasingsTT", GT_Material_Casings.INSTANCE);
        setCreativeTab(creativeTabTecTech);

        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[texturePage][b] = new GT_CopiedBlockTexture(this, 6, b);
            /* IMPORTANT for block recoloring **/
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "High Power Casing");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Computer Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Computer Heat Vent");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Advanced Computer Casing");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Molecular Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Advanced Molecular Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Containment Field Generator");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Molecular Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Hollow Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Spacetime Altering Casing");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Teleportation Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Dimensional Bridge Generator");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Ultimate Molecular Casing");
        GT_LanguageManager
                .addStringLocalization(getUnlocalizedName() + ".13.name", "Ultimate Advanced Molecular Casing");
        GT_LanguageManager
                .addStringLocalization(getUnlocalizedName() + ".14.name", "Ultimate Containment Field Generator");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Debug Sides"); // NOT REGISTER AS
                                                                                                    // TEXTURE FOR
                                                                                                    // HATCHES!

        CustomItemList.eM_Power.set(new ItemStack(this, 1, 0));

        CustomItemList.eM_Computer_Casing.set(new ItemStack(this, 1, 1));
        CustomItemList.eM_Computer_Vent.set(new ItemStack(this, 1, 2));
        CustomItemList.eM_Computer_Bus.set(new ItemStack(this, 1, 3));

        CustomItemList.eM_Containment.set(new ItemStack(this, 1, 4));
        CustomItemList.eM_Containment_Advanced.set(new ItemStack(this, 1, 5));
        CustomItemList.eM_Containment_Field.set(new ItemStack(this, 1, 6));

        CustomItemList.eM_Coil.set(new ItemStack(this, 1, 7));
        CustomItemList.eM_Hollow.set(new ItemStack(this, 1, 8));
        CustomItemList.eM_Spacetime.set(new ItemStack(this, 1, 9));

        CustomItemList.eM_Teleportation.set(new ItemStack(this, 1, 10));
        CustomItemList.eM_Dimensional.set(new ItemStack(this, 1, 11));

        CustomItemList.eM_Ultimate_Containment.set(new ItemStack(this, 1, 12));
        CustomItemList.eM_Ultimate_Containment_Advanced.set(new ItemStack(this, 1, 13));
        CustomItemList.eM_Ultimate_Containment_Field.set(new ItemStack(this, 1, 14));

        CustomItemList.debugBlock.set(new ItemStack(this, 1, 15));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        // super.registerBlockIcons(aIconRegister);
        eM0 = aIconRegister.registerIcon("gregtech:iconsets/EM_POWER");

        eM1 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_NONSIDE");
        eM1s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC");
        eM2 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT_NONSIDE");
        eM2s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_VENT");
        eM3 = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV_NONSIDE");
        eM3s = aIconRegister.registerIcon("gregtech:iconsets/EM_PC_ADV");

        eM4 = aIconRegister.registerIcon("gregtech:iconsets/EM_CASING");
        eM5 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD_CASING");
        eM6 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD");

        eM7 = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL_NONSIDE");
        eM7s = aIconRegister.registerIcon("gregtech:iconsets/EM_COIL");
        eM8 = aIconRegister.registerIcon("gregtech:iconsets/EM_HOLLOW");
        eM9 = aIconRegister.registerIcon("gregtech:iconsets/EM_TIMESPACE");

        eM10 = aIconRegister.registerIcon("gregtech:iconsets/EM_TELE");
        eM11 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM");

        eM12 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_CASING");
        eM13 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_FIELD_CASING");
        eM14 = aIconRegister.registerIcon("gregtech:iconsets/EM_ULTIMATE_FIELD");

        debug[0] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_0");
        debug[1] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_1");
        debug[2] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_2");
        debug[3] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_3");
        debug[4] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_4");
        debug[5] = aIconRegister.registerIcon("gregtech:iconsets/DEBUG_5");
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return eM0;
            case 1:
                if (ordinalSide < 2) {
                    return eM1;
                }
                return eM1s;
            case 2:
                if (ordinalSide < 2) {
                    return eM2;
                }
                return eM2s;
            case 3:
                if (ordinalSide < 2) {
                    return eM3;
                }
                return eM3s;
            case 4:
                return eM4;
            case 5:
                return eM5;
            case 6:
                return eM6;
            case 7:
                if (ordinalSide < 2) {
                    return eM7;
                }
                return eM7s;
            case 8:
                return eM8;
            case 9:
                return eM9;
            case 10:
                return eM10;
            case 11:
                return eM11;
            case 12:
                return eM12;
            case 13:
                return eM13;
            case 14:
                return eM14;
            case 15:
                return debug[ordinalSide];
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(ordinalSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (int i = 0; i <= 15; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
