package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

public class BlockCasingsFoundry extends BlockCasingsAbstract {

    public BlockCasingsFoundry() {
        super(ItemCasingsFoundry.class, "gt.foundrycasings", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.Casing_ExoFoundry, "Primary Exo-Foundry Casing");
        register(1, ItemList.Magnetic_Chassis_T1_ExoFoundry, "Infinite Magnetic Chassis");
        register(2, ItemList.Magnetic_Chassis_T2_ExoFoundry, "Eternal Magnetic Chassis");
        register(3, ItemList.Magnetic_Chassis_T3_ExoFoundry, "Celestial Magnetic Chassis");
        register(4, ItemList.Active_Time_Dilation_System_ExoFoundry, "Time Dilation System Casing");
        register(5, ItemList.Efficient_Overclocking_ExoFoundry, "Sentient Overclocker Casing");
        register(6, ItemList.Power_Efficient_Subsystems_ExoFoundry, "Proto-Volt Stabilizer Casing");
        register(7, ItemList.Harmonic_Reinforcement_ExoFoundry, "Harmonic Reinforcement Casing");
        register(8, ItemList.Extra_Casting_Basins_ExoFoundry, "Superdense Casting Basin Casing");
        register(9, ItemList.Hypercooler_ExoFoundry, "Hypercooler Casing");
        register(10, ItemList.Streamlined_Casters_ExoFoundry, "Streamlined Casting Casing");
        register(11, ItemList.Secondary_Casing_ExoFoundry, "Inner Exo-Foundry Siphon Casing");
        register(12, ItemList.Central_Casing_ExoFoundry, "Central Exo-Foundry Regulation Casing");
        for (int i = 1; i <= 3; i++) {
            GTStructureChannels.MAGNETIC_CHASSIS.registerAsIndicator(new ItemStack(this, 1, i), i);
        }

    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (8 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        switch (aMeta) {
            case 0 -> {
                return Textures.BlockIcons.EXOFOUNDRY_CASING.getIcon();
            }
            case 1 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS.getIcon();
            }
            case 2 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS.getIcon();
            }
            case 3 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS.getIcon();
            }
            case 4 -> {
                return Textures.BlockIcons.EXOFOUNDRY_ACTIVE_TIME_DILATION_SYSTEM.getIcon();
            }
            case 5 -> {
                return Textures.BlockIcons.EXOFOUNDRY_EFFICIENT_OVERCLOCKING.getIcon();
            }
            case 6 -> {
                return Textures.BlockIcons.EXOFOUNDRY_POWER_EFFICIENT_SUBSYSTEMS.getIcon();
            }
            case 7 -> {
                return Textures.BlockIcons.EXOFOUNDRY_HARMONIC_REINFORCEMENT.getIcon();
            }
            case 8 -> {
                return Textures.BlockIcons.EXOFOUNDRY_EXTRA_CASTING_BASINS.getIcon();
            }
            case 9 -> {
                return Textures.BlockIcons.EXOFOUNDRY_HYPERCOOLER.getIcon();
            }
            case 10 -> {
                return Textures.BlockIcons.EXOFOUNDRY_STREAMLINED_CASTERS.getIcon();
            }
            case 11 -> {
                return Textures.BlockIcons.EXOFOUNDRY_SECONDARY_CASING.getIcon();
            }
            case 12 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING.getIcon();
            }
            default -> {
                return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            }

        }
    }
}
