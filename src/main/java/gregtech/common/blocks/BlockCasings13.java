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
        register(0, ItemList.Casing_Cable, "Cable Casing");
        register(1, ItemList.Casing_Graphite_Moderator, "Graphite Moderator Casing");
        register(2, ItemList.Casing_Insulated_Fluid_Pipe, "Insulated Fluid Pipe Casing");
        register(3, ItemList.Casing_Beryllium_Integrated_Reactor, "Beryllium Integrated Reactor Casing");
        register(4, ItemList.Casing_Refined_Graphite, "Refined Graphite Block");
        register(5, ItemList.PrecisionFieldSyncCasing, "Precision Field Sync Casing");
        register(6, ItemList.MagneticAnchorCasing, "Magnetic Anchor Casing");
        register(7, ItemList.FieldEnergyAbsorberCasing, "Field Energy Absorber Casing");
        register(8, ItemList.LoadbearingDistributionCasing, "Loadbearing Distribution Casing");
        register(9, ItemList.NaniteFramework, "Nanite Replication Framework");
        register(10, ItemList.ColliderCasing, "Collider Casing");
        register(11, ItemList.CMSCasing,"Charged Matter Sensor Casing");
        register(12, ItemList.ATLASCasing,"Advanced Total Lepton Assimilation Snare Casing");
        register(13, ItemList.ALICECasing,"Absolute Lattice Integrated Chromodynamic Encapsulator Casing");
        register(14, ItemList.LHCbCasing,"Localized Horizon Curvature Binder Casing");

    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_CABLE.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_GRAPHITE_MODERATOR.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_INSULATED_FLUID_PIPE.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_BERYLLIUM_INTEGRATED_REACTOR.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_REFINED_GRAPHITE.getIcon();
            case 5 -> Textures.BlockIcons.NANO_FORGE_CASING_1.getIcon();
            case 6 -> Textures.BlockIcons.NANO_FORGE_CASING_2.getIcon();
            case 7 -> Textures.BlockIcons.NANO_FORGE_CASING_3.getIcon();
            case 8 -> Textures.BlockIcons.NANO_FORGE_CASING_4.getIcon();
            case 9 -> Textures.BlockIcons.NANITE_CORE.getIcon();
            case 10 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 11 -> Textures.BlockIcons.CMS_CASING.getIcon();
            case 12 -> Textures.BlockIcons.ATLAS_CASING.getIcon();
            case 13 -> Textures.BlockIcons.ALICE_CASING.getIcon();
            case 14 -> Textures.BlockIcons.LHCB_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
