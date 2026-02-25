package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings13 extends BlockCasingsAbstract {

    public BlockCasings13() {
        super(ItemCasings.class, "gt.blockcasings13", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.Casing_Cable);
        register(1, ItemList.Casing_Graphite_Moderator);
        register(2, ItemList.Casing_Insulated_Fluid_Pipe);
        register(3, ItemList.Casing_Beryllium_Integrated_Reactor);
        register(4, ItemList.Casing_Refined_Graphite);
        register(5, ItemList.PrecisionFieldSyncCasing);
        register(6, ItemList.MagneticAnchorCasing);
        register(7, ItemList.FieldEnergyAbsorberCasing);
        register(8, ItemList.LoadbearingDistributionCasing);
        register(9, ItemList.NaniteFramework);

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
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
