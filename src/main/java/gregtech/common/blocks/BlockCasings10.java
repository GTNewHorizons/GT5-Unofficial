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
public class BlockCasings10 extends BlockCasingsAbstract {

    public BlockCasings10() {
        super(ItemCasings.class, "gt.blockcasings10", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Electromagnetic_Separator, "MagTech Casing");
        register(1, ItemList.Casing_Laser, "Laser Containment Casing");
        register(2, ItemList.BlockQuarkContainmentCasing, "Quark Exclusion Casing");
        register(3, ItemList.Casing_Autoclave, "Pressure Containment Casing");
        register(4, ItemList.Compressor_Casing, "Electric Compressor Casing");
        register(5, ItemList.Compressor_Pipe_Casing, "Compression Pipe Casing");
        register(6, ItemList.Neutronium_Casing, "Neutronium Casing");
        register(7, ItemList.Neutronium_Active_Casing, "Active Neutronium Casing");
        register(8, ItemList.Neutronium_Stable_Casing, "Neutronium Stabilization Casing");
        register(9, ItemList.Coolant_Duct_Casing, "Coolant Duct");
        register(10, ItemList.Heating_Duct_Casing, "Heating Duct");
        register(11, ItemList.Extreme_Density_Casing, "Extreme Density Space-Bending Casing");
        register(12, ItemList.Background_Radiation_Casing, "Background Radiation Absorbent Casing");
        register(13, ItemList.Casing_Fluid_Solidifier, "Solidifier Casing");
        register(14, ItemList.Radiator_Fluid_Solidifier, "Solidifier Radiator");
        register(15, ItemList.Casing_Reinforced_Wood, "Reinforced Wooden Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 48);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_EMS.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_LASER.getIcon();
            case 2 -> Textures.BlockIcons.BLOCK_QUARK_CONTAINMENT_CASING.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_AUTOCLAVE.getIcon();
            case 4 -> Textures.BlockIcons.COMPRESSOR_CASING.getIcon();
            case 5 -> Textures.BlockIcons.COMPRESSOR_PIPE_CASING.getIcon();
            case 6 -> Textures.BlockIcons.NEUTRONIUM_CASING.getIcon();
            case 7 -> Textures.BlockIcons.NEUTRONIUM_ACTIVE_CASING.getIcon();
            case 8 -> Textures.BlockIcons.NEUTRONIUM_STABLE_CASING.getIcon();
            case 9 -> Textures.BlockIcons.COOLANT_DUCT_CASING.getIcon();
            case 10 -> Textures.BlockIcons.HEATING_DUCT_CASING.getIcon();
            case 11 -> Textures.BlockIcons.EXTREME_DENSITY_CASING.getIcon();
            case 12 -> Textures.BlockIcons.RADIATION_ABSORBENT_CASING.getIcon();
            case 13 -> Textures.BlockIcons.MASS_SOLIDIFIER_CASING.getIcon();
            case 14 -> Textures.BlockIcons.MASS_SOLIDIFIER_RADIATOR_CASING.getIcon();
            case 15 -> ordinalSide > 1 ? Textures.BlockIcons.CASING_REINFORCED_WOOD.getIcon()
                : Textures.BlockIcons.CASING_REINFORCED_WOOD_TOP.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
