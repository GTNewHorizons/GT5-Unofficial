package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The base class for casings. Casings are the blocks that are mainly used to build multiblocks.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCyclotronCoils extends BlockCasingsAbstract {

    public BlockCyclotronCoils() {
        super(ItemCasings.class, "gt.blockcasings.cyclotron_coils", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Superconducting_Magnet_Solenoid_MV, "MV Solenoid Superconductor Coil");
        register(1, ItemList.Superconducting_Magnet_Solenoid_HV, "HV Solenoid Superconductor Coil");
        register(2, ItemList.Superconducting_Magnet_Solenoid_EV, "EV Solenoid Superconductor Coil");
        register(3, ItemList.Superconducting_Magnet_Solenoid_IV, "IV Solenoid Superconductor Coil");
        register(4, ItemList.Superconducting_Magnet_Solenoid_LuV, "LuV Solenoid Superconductor Coil");
        register(5, ItemList.Superconducting_Magnet_Solenoid_ZPM, "ZPM Solenoid Superconductor Coil");
        register(6, ItemList.Superconducting_Magnet_Solenoid_UV, "UV Solenoid Superconductor Coil");
        register(7, ItemList.Superconducting_Magnet_Solenoid_UHV, "UHV Solenoid Superconductor Coil");
        register(8, ItemList.Superconducting_Magnet_Solenoid_UEV, "UEV Solenoid Superconductor Coil");
        register(9, ItemList.Superconducting_Magnet_Solenoid_UIV, "UIV Solenoid Superconductor Coil");
        register(10, ItemList.Superconducting_Magnet_Solenoid_UMV, "UMV Solenoid Superconductor Coil");

        for (int i = 0; i < 11; i++) {
            GTStructureChannels.SOLENOID.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (2 << 7) | (aMeta);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.MV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.MV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 1 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.HV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.HV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 2 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.EV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.EV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 3 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.IV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.IV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 4 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.LuV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.LuV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 5 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.ZPM_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.ZPM_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 6 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.UV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 7 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.UHV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UHV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 8 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.UEV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UEV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 9 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.UIV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UIV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
                case 10 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return Textures.BlockIcons.UMV_TOP_CYCLOTRON_SOLENOID.getIcon();
                    }
                    return Textures.BlockIcons.UMV_SIDE_CYCLOTRON_SOLENOID.getIcon();
                }
            }
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    public int getVoltageTier(int meta) {
        return meta + 2;
    }
}
