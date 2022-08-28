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

public class TimeAccelerationFieldCasing extends GT_Block_Casings_Abstract {
    private static IIcon texture_tier_0;

    private static final byte START_INDEX = 16;

    public TimeAccelerationFieldCasing() {
        super(GT_Item_CasingsBA0.class, "gt.time_acceleration_field_generator", GT_Material_Casings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[texturePage][b + START_INDEX] = new GT_CopiedBlockTexture(this, 6, b);
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Crude Time Dilation Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Primitive Time Dilation Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Stable Time Dilation Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Superb Time Dilation Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Resplendent Time Dilation Field Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Perfect Time Temporal Dilation Generator");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Tipler Grade Time Dilation Field Generator");

        CustomItemList.TimeAccelerationFieldGeneratorTier0.set(new ItemStack(this, 1, 0));
        CustomItemList.TimeAccelerationFieldGeneratorTier1.set(new ItemStack(this, 1, 1));
        CustomItemList.TimeAccelerationFieldGeneratorTier2.set(new ItemStack(this, 1, 2));
        CustomItemList.TimeAccelerationFieldGeneratorTier3.set(new ItemStack(this, 1, 3));
        CustomItemList.TimeAccelerationFieldGeneratorTier4.set(new ItemStack(this, 1, 4));
        CustomItemList.TimeAccelerationFieldGeneratorTier5.set(new ItemStack(this, 1, 5));
        CustomItemList.TimeAccelerationFieldGeneratorTier6.set(new ItemStack(this, 1, 6));

    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        texture_tier_0 = aIconRegister.registerIcon("gregtech:iconsets/EM_FIELD");
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return texture_tier_0;
            case 1:
                return texture_tier_0;
            case 2:
                return texture_tier_0;
            case 3:
                return texture_tier_0;
            case 4:
                return texture_tier_0;
            case 5:
                return texture_tier_0;
            case 6:
                return texture_tier_0;
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
