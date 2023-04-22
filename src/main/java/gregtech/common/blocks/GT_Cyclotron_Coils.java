package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

public class GT_Cyclotron_Coils extends GT_Block_Casings_Abstract {

    public GT_Cyclotron_Coils() {
        super(GT_Cyclotron_Item_Casings.class, "gt.blockcasings.cyclotron_coils", GT_Material_Casings.INSTANCE, 16);

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "MV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "HV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "EV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "IV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "LuV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "ZPM Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "UV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "UHV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "UEV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "UIV Solenoid Superconductor Coil");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "UMV Solenoid Superconductor Coil");

        ItemList.Superconducting_Magnet_Solenoid_MV.set(new ItemStack(this, 1, 0));
        ItemList.Superconducting_Magnet_Solenoid_HV.set(new ItemStack(this, 1, 1));
        ItemList.Superconducting_Magnet_Solenoid_EV.set(new ItemStack(this, 1, 2));
        ItemList.Superconducting_Magnet_Solenoid_IV.set(new ItemStack(this, 1, 3));
        ItemList.Superconducting_Magnet_Solenoid_LuV.set(new ItemStack(this, 1, 4));
        ItemList.Superconducting_Magnet_Solenoid_ZPM.set(new ItemStack(this, 1, 5));
        ItemList.Superconducting_Magnet_Solenoid_UV.set(new ItemStack(this, 1, 6));
        ItemList.Superconducting_Magnet_Solenoid_UHV.set(new ItemStack(this, 1, 7));
        ItemList.Superconducting_Magnet_Solenoid_UEV.set(new ItemStack(this, 1, 8));
        ItemList.Superconducting_Magnet_Solenoid_UIV.set(new ItemStack(this, 1, 9));
        ItemList.Superconducting_Magnet_Solenoid_UMV.set(new ItemStack(this, 1, 10));
    }

    @Override // Magic numbers...
    public int getTextureIndex(int aMeta) {
        return 192 + aMeta;
    }

    @Override
    public IIcon getIcon(int aSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.MV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.MV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 1 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.HV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.HV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 2 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.EV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.EV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 3 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.IV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.IV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 4 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.LuV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.LuV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 5 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.ZPM_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.ZPM_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 6 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.UV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 7 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.UHV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UHV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 8 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.UEV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UEV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 9 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.UIV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UIV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 10 -> {
                    if (aSide == 0 || aSide == 1) {
                        return Textures.BlockIcons.UMV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UMV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
            }
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Override
    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ) > 9 ? super.colorMultiplier(aWorld, aX, aY, aZ)
            : gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[0] << 16 | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[1] << 8
                | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[2];
    }
}
