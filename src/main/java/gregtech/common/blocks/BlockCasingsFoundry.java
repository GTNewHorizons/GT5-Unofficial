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
        super(ItemCasings.class, "gt.blockcasingsFoundry", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.Casing_Solidifier_Modular, "Modular Solidifier Casing");
        register(1, ItemList.Magnetic_Chassis_T1_Solidifier_Modular, "Infinite Magnetic Chassis");
        register(2, ItemList.Magnetic_Chassis_T2_Solidifier_Modular, "Eternal Magnetic Chassis");
        register(3, ItemList.Magnetic_Chassis_T3_Solidifier_Modular, "Celestial Magnetic Chassis");
        register(4, ItemList.Active_Time_Dilation_System_Solidifier_Modular, "Time Dilation System Casing");
        register(5, ItemList.Efficient_Overclocking_Solidifier_Modular, "Sentient Overclocking Casing");
        register(6, ItemList.Power_Efficient_Subsystems_Solidifier_Modular, "Proto-Volt Stabilizer Casing");
        register(7, ItemList.Transcendent_Reinforcement_Solidifier_Modular, "Transcendental Bolted Shirabon Casing");
        register(8, ItemList.Extra_Casting_Basins_Solidifier_Modular, "Superdense Casting Basin Casing");
        register(9, ItemList.Hypercooler_Solidifier_Modular, "Hypercooler Siphon Casing");
        register(10, ItemList.Streamlined_Casters_Solidifier_Modular, "Streamlined Casting Casing");
        for (int i = 1; i <= 3; i++) {
            GTStructureChannels.MAGNETIC_CHASSIS.registerAsIndicator(new ItemStack(this, 1, i), i);
        }

    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_CASING.getIcon();
            // todo: textures for magnetic chassis
            case 1 -> Textures.BlockIcons.MACHINE_CASING_THAUMIUM.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_VOID.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_ICHORIUM.getIcon();
            case 4 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_ACTIVE_TIME_DILATION_SYSTEM.getIcon();
            case 5 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EFFICIENT_OVERCLOCKING.getIcon();
            case 6 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_POWER_EFFICIENT_SUBSYSTEMS.getIcon();
            case 7 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_TRANSCENDENT_REINFORCEMENT.getIcon();
            case 8 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EXTRA_CASTING_BASINS.getIcon();
            case 9 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_HYPERCOOLER.getIcon();
            case 10 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_STREAMLINED_CASTERS.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();

        };
    }
}
