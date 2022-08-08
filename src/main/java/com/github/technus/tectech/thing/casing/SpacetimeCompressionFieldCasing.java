package com.github.technus.tectech.thing.casing;

import com.github.technus.tectech.thing.CustomItemList;
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

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class SpacetimeCompressionFieldCasing extends GT_Block_Casings_Abstract {
    private static IIcon[] tM0 = new IIcon[2];
    private static IIcon[] tM1 = new IIcon[2];
    private static IIcon[] tM2 = new IIcon[2];
    private static IIcon[] tM3 = new IIcon[2];
    private static IIcon[] tM4 = new IIcon[2];
    private static IIcon[] tM5 = new IIcon[2];
    private static IIcon[] tM6 = new IIcon[2];
    private static IIcon tM7;
    private static IIcon[] tM8 = new IIcon[2];
    private static IIcon[] tM9 = new IIcon[2];

    private static final byte START_INDEX = 16;

    public SpacetimeCompressionFieldCasing() {
        super(GT_Item_CasingsBA0.class, "gt.spacetime_compression_field_generator", GT_Material_Casings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[texturePage][b + START_INDEX] = new GT_CopiedBlockTexture(this, 6, b);
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "TEST Crude Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "TEST Primitive Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "TEST Stable Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "TEST Superb Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "TEST Resplendent Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "TEST Perfect Spacetime Compression Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "TEST Tipler Grade Spacetime Compression Field Generator");

        CustomItemList.SpacetimeCompressionFieldGeneratorTier0.set(new ItemStack(this, 1, 0));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier1.set(new ItemStack(this, 1, 1));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier2.set(new ItemStack(this, 1, 2));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier3.set(new ItemStack(this, 1, 3));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier4.set(new ItemStack(this, 1, 4));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier5.set(new ItemStack(this, 1, 5));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier6.set(new ItemStack(this, 1, 6));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier7.set(new ItemStack(this, 1, 7));

    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        tM0[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_0");
        tM0[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_0");
        tM1[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_1");
        tM1[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_1");
        tM2[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_2");
        tM2[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_2");
        tM3[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_3");
        tM3[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_3");
        tM4[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_4");
        tM4[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_4");
        tM5[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_5");
        tM5[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_5");
        tM9[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_TOP_BOTTOM_6");
        tM9[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_PRIMARY_SIDES_6");

        tM6[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_BASE_TOP_BOTTOM");
        tM6[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_BASE_SIDES");
        tM7 = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_TOROID");
        tM8[0] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_SECONDARY_TOP_BOTTOM");
        tM8[1] = aIconRegister.registerIcon("gregtech:iconsets/TM_TESLA_WINDING_SECONDARY_SIDES");
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM0[0];
                    default:
                        return tM0[1];
                }
            case 1:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM1[0];
                    default:
                        return tM1[1];
                }
            case 2:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM2[0];
                    default:
                        return tM2[1];
                }
            case 3:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM3[0];
                    default:
                        return tM3[1];
                }
            case 4:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM4[0];
                    default:
                        return tM4[1];
                }
            case 5:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM5[0];
                    default:
                        return tM5[1];
                }
            case 6:
                switch (aSide) {
                    case 0:
                    case 1:
                        return tM6[0];
                    default:
                        return tM6[1];
                }
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 6; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
