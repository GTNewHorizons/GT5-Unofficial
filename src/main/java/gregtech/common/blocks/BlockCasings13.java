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
        register(5, ItemList.PrecisionFieldSyncCasing, "Precision Field Sync Casing");
        register(6, ItemList.MagneticAnchorCasing, "Magnetic Anchor Casing");
        register(7, ItemList.FieldEnergyAbsorberCasing, "Field Energy Absorber Casing");
        register(8, ItemList.LoadbearingDistributionCasing, "Loadbearing Distribution Casing");
        register(9, ItemList.NaniteFramework, "Nanite Replication Framework");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 5 -> Textures.BlockIcons.NANO_FORGE_CASING_1.getIcon();
            case 6 -> Textures.BlockIcons.NANO_FORGE_CASING_2.getIcon();
            case 7 -> Textures.BlockIcons.NANO_FORGE_CASING_3.getIcon();
            case 8 -> Textures.BlockIcons.NANO_FORGE_CASING_4.getIcon();
            case 9 -> Textures.BlockIcons.NANITE_CORE.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
