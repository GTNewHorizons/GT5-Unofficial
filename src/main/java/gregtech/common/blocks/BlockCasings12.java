package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings12 extends BlockCasingsAbstract {

    public BlockCasings12() {
        super(ItemCasings.class, "gt.blockcasings12", MaterialCasings.INSTANCE, 16);
        register(2, ItemList.Active_Time_Dilation_System_Solidifier_Modular, "Active Time Dilation System");
        register(3, ItemList.Efficient_Overclocking_Solidifier_Modular, "Efficient Overclocking Module");
        register(4, ItemList.Power_Efficient_Subsystems_Solidifier_Modular, "Power Efficient Subsystems Module");
        register(5, ItemList.Transcendent_Reinforcement_Solidifier_Modular, "Transcendent Reinforcement Module");
        register(6, ItemList.Extra_Casting_Basins_Solidifier_Modular, "Extra Casting Basins Module");
        register(7, ItemList.Hypercooler_Solidifier_Modular, "Hypercooler Module");
        register(8, ItemList.Streamlined_Casters_Solidifier_Modular, "Streamlined Casters Module");
        register(9, ItemList.Casing_Solidifier_Modular, "Modular Solidifier Casing");
        register(10, ItemList.CasingThaumium, "Alchemically Resistant Thaumium Casing");
        register(11, ItemList.CasingVoid, "Alchemically Inert Void Casing");
        register(12, ItemList.CasingIchorium, "Alchemically Immune Ichorium Casing");
        for (int i = 0; i < 3; i++) {
            GTStructureChannels.METAL_MACHINE_CASING.registerAsIndicator(new ItemStack(this, 1, i + 10), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 2 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_ACTIVE_TIME_DILATION_SYSTEM.getIcon();
            case 3 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EFFICIENT_OVERCLOCKING.getIcon();
            case 4 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_POWER_EFFICIENT_SUBSYSTEMS.getIcon();
            case 5 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_TRANSCENDENT_REINFORCEMENT.getIcon();
            case 6 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_EXTRA_CASTING_BASINS.getIcon();
            case 7 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_HYPERCOOLER.getIcon();
            case 8 -> Textures.BlockIcons.MODULAR_SOLIDIFIER_STREAMLINED_CASTERS.getIcon();
            case 9 -> Textures.BlockIcons.CASING_SOLIDIFIER_ATOMIC.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_THAUMIUM.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VOID.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_ICHORIUM.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
