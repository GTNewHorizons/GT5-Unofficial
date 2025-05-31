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
        register(4, ItemList.Hyper_Cooler, "Hyper Cooler");
        register(5, ItemList.Extra_Casing_Basins, "Extra Casing Basins");
        register(6, ItemList.Transcendent_Reinforcement, "Transcendent Reinforcement");
        register(7, ItemList.Streamlined_Casters, "Streamlined Casters");
        register(8, ItemList.Power_Efficient_Subsystems, "Power Efficient Subsystems");
        register(9, ItemList.Active_Time_Dilation_System, "Active Time Dilation Systems");
        register(10, ItemList.CasingThaumium, "Alchemically Resistant Thaumium Casing");
        register(11, ItemList.CasingVoid, "Alchemically Inert Void Casing");
        register(12, ItemList.CasingIchorium, "Alchemically Immune Ichorium Casing");
        register(13, ItemList.Efficient_Overclocking, "Efficient Overclocking Module");
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
            case 10 -> Textures.BlockIcons.MACHINE_CASING_THAUMIUM.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VOID.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_ICHORIUM.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
