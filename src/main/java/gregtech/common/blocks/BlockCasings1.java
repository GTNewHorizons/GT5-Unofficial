package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
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
        super(ItemCasings.class, "gt.blockcasings", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_ULV);
        register(1, ItemList.Casing_LV);
        register(2, ItemList.Casing_MV);
        register(3, ItemList.Casing_HV);
        register(4, ItemList.Casing_EV);
        register(5, ItemList.Casing_IV);
        register(6, ItemList.Casing_LuV);
        register(7, ItemList.Casing_ZPM);
        register(8, ItemList.Casing_UV);
        register(9, ItemList.Casing_MAX);
        register(10, ItemList.Casing_BronzePlatedBricks);
        register(11, ItemList.Casing_HeatProof);
        register(12, ItemList.Casing_Dim_Trans);
        register(13, ItemList.Casing_Dim_Injector);
        register(14, ItemList.Casing_Dim_Bridge);
        register(15, ItemList.Casing_Coil_Superconductor);

        for (int i = 0; i < 10; i++) {
            GTStructureChannels.TIER_MACHINE_CASING.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
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
        if (aWorld.getBlockMetadata(aX, aY, aZ) > 9) {
            return 0xFFFFFF;
        }

        return super.colorMultiplier(aWorld, aX, aY, aZ);
    }
}
