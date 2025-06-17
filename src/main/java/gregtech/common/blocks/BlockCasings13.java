package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings13 extends BlockCasingsAbstract {

    public BlockCasings13() {
        super(ItemCasings.class, "gt.blockcasings13", MaterialCasings.INSTANCE, 16);
        register(8, ItemList.Active_Time_Dilation_System_Solidifier_Modular, "Active Time Dilation System");
        register(9, ItemList.Efficient_Overclocking_Solidifier_Modular, "Efficient Overclocking Module");
        register(10, ItemList.Power_Efficient_Subsystems_Solidifier_Modular, "Power Efficient Subsystems Module");
        register(11, ItemList.Transcendent_Reinforcement_Solidifier_Modular, "Transcendent Reinforcement Module");
        register(12, ItemList.Extra_Casting_Basins_Solidifier_Modular, "Extra Casting Basins Module");
        register(13, ItemList.Hypercooler_Solidifier_Modular, "Hypercooler Module");
        register(14, ItemList.Streamlined_Casters_Solidifier_Modular, "Streamlined Casters Module");
        register(15, ItemList.Casing_Solidifier_Modular, "Modular Solidifier Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 8 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_ACTIVE_TIME_DILATION_SYSTEM.getIcon();
            case 9 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EFFICIENT_OVERCLOCKING.getIcon();
            case 10 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_POWER_EFFICIENT_SUBSYSTEMS.getIcon();
            case 11 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_TRANSCENDENT_REINFORCEMENT.getIcon();
            case 12 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EXTRA_CASTING_BASINS.getIcon();
            case 13 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_HYPERCOOLER.getIcon();
            case 14 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_STREAMLINED_CASTERS.getIcon();
            case 15 -> Textures.BlockIcons.CASING_SOLIDIFIER_ATOMIC.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
