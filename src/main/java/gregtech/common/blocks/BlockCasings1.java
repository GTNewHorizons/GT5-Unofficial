package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings1 extends BlockCasingsAbstract {

    /**
     * Texture Index Information Textures.BlockIcons.casingTexturePages[0][0-63] - Gregtech
     * Textures.BlockIcons.casingTexturePages[0][64-127] - GT++ Textures.BlockIcons.casingTexturePages[1][0-127] -
     * Gregtech Textures.BlockIcons.casingTexturePages[2][0-127] - Free Textures.BlockIcons.casingTexturePages[3][0-127]
     * - Free Textures.BlockIcons.casingTexturePages[4][0-127] - Free Textures.BlockIcons.casingTexturePages[5][0-127] -
     * Free Textures.BlockIcons.casingTexturePages[6][0-127] - Free Textures.BlockIcons.casingTexturePages[7][0-127] -
     * TecTech Textures.BlockIcons.casingTexturePages[8][0-127] - TecTech
     */
    public BlockCasings1() {
        super(ItemCasings1.class, "gt.blockcasings", MaterialCasings.INSTANCE, 16);

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "ULV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "LV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "MV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "HV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "EV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "IV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "LuV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "ZPM Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "UV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "UHV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Bronze Plated Bricks");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Heat Proof Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Dimensionally Transcendent Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Dimensional Injection Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Dimensional Bridge");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Superconducting Coil Block");
        ItemList.Casing_ULV.set(new ItemStack(this, 1, 0));
        ItemList.Casing_LV.set(new ItemStack(this, 1, 1));
        ItemList.Casing_MV.set(new ItemStack(this, 1, 2));
        ItemList.Casing_HV.set(new ItemStack(this, 1, 3));
        ItemList.Casing_EV.set(new ItemStack(this, 1, 4));
        ItemList.Casing_IV.set(new ItemStack(this, 1, 5));
        ItemList.Casing_LuV.set(new ItemStack(this, 1, 6));
        ItemList.Casing_ZPM.set(new ItemStack(this, 1, 7));
        ItemList.Casing_UV.set(new ItemStack(this, 1, 8));
        ItemList.Casing_MAX.set(new ItemStack(this, 1, 9));
        ItemList.Casing_BronzePlatedBricks.set(new ItemStack(this, 1, 10));
        ItemList.Casing_HeatProof.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Dim_Trans.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Dim_Injector.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Dim_Bridge.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Coil_Superconductor.set(new ItemStack(this, 1, 15));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 10 -> {
                    return Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
                }
                case 11 -> {
                    return Textures.BlockIcons.MACHINE_HEATPROOFCASING.getIcon();
                }
                case 12 -> {
                    return Textures.BlockIcons.MACHINE_DIM_TRANS_CASING.getIcon();
                }
                case 13 -> {
                    return Textures.BlockIcons.MACHINE_DIM_INJECTOR.getIcon();
                }
                case 14 -> {
                    return Textures.BlockIcons.MACHINE_DIM_BRIDGE.getIcon();
                }
                case 15 -> {
                    return Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR.getIcon();
                }
            }
            if (ordinalSide == 0) {
                return Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
            }
            if (ordinalSide == 1) {
                return Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
            }
            return Textures.BlockIcons.MACHINECASINGS_SIDE[aMeta].getIcon();
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
