package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockCompressibleComputationCore extends BlockCasingsAbstract {

    public BlockCompressibleComputationCore() {
        super(ItemCasings.class, "gt.blockcasings.compcore", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.CompressibleComputationCoreULV);
        register(1, ItemList.CompressibleComputationCoreLV);
        register(2, ItemList.CompressibleComputationCoreMV);
        register(3, ItemList.CompressibleComputationCoreHV);
        register(4, ItemList.CompressibleComputationCoreEV);
        register(5, ItemList.CompressibleComputationCoreIV);
        register(6, ItemList.CompressibleComputationCoreLuV);
        register(7, ItemList.CompressibleComputationCoreZPM);
        register(8, ItemList.CompressibleComputationCoreUV);
        register(9, ItemList.CompressibleComputationCoreUHV);
        register(10, ItemList.CompressibleComputationCoreUEV);
        register(11, ItemList.CompressibleComputationCoreUIV);
        register(12, ItemList.CompressibleComputationCoreUMV);
        register(13, ItemList.CompressibleComputationCoreUXV);
        register(14, ItemList.CompressibleComputationCoreMAX);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_ULV.getIcon();
            case 1 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_LV.getIcon();
            case 2 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_MV.getIcon();
            case 3 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_HV.getIcon();
            case 4 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_EV.getIcon();
            case 5 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_IV.getIcon();
            case 6 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_LUV.getIcon();
            case 7 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_ZPM.getIcon();
            case 8 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UV.getIcon();
            case 9 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UHV.getIcon();
            case 10 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UEV.getIcon();
            case 11 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UIV.getIcon();
            case 12 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UMV.getIcon();
            case 13 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_UXV.getIcon();
            case 14 -> Textures.BlockIcons.COMPRESSIBLE_COMPUTATION_CORE_MAX.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (17 << 7) | (aMeta + 16);
    }
}
