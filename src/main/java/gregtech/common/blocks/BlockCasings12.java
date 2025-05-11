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
public class BlockCasings12 extends BlockCasingsAbstract {

    public BlockCasings12() {
        super(ItemCasings.class, "gt.blockcasings12", MaterialCasings.INSTANCE, 16);
        register(4, ItemList.Hyper_Cooler, "Hyper Cooler");
        register(5, ItemList.Extra_Casing_Basins, "Extra Casing Basins");
        register(6, ItemList.Transcendent_Reinforcement, "Transcendent Reinforcement");
        register(7, ItemList.Streamlined_Casters, "Streamlined Casters");
        register(8, ItemList.Power_Efficient_Subsystems, "Power Efficient Subsystems");
        register(9, ItemList.Active_Time_Dilation_System, "Active Time Dilation Systems");
        register(13, ItemList.Efficient_Overclocking, "Efficient Overclocking Module");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
