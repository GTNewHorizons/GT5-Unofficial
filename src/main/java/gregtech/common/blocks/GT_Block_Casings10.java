package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Casings10 extends GT_Block_Casings_Abstract {

    public GT_Block_Casings10() {
        super(GT_Item_Casings10.class, "gt.blockcasings10", GT_Material_Casings.INSTANCE, 16);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "MagTech Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Laser Containment Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Quark Exclusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Pressure Containment Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Containment Machine Casing HV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Containment Machine Casing EV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Containment Machine Casing IV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Containment Machine Casing ZPM");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Containment Machine Casing UV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Electric Compressor Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Compression Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Neutronium Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Active Neutronium Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Neutronium Stabilization Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Coolant Duct");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Heating Duct");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".16.name", "Extreme Density Space-Bending Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".17.name", "Background Radiation Absorbent Casing");

        ItemList.Casing_Electromagnetic_Separator.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Laser.set(new ItemStack(this, 1, 1));
        ItemList.BlockQuarkContainmentCasing.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Autoclave.set(new ItemStack(this, 1, 3));
        ItemList.Casing_ContainmentFieldHV.set(new ItemStack(this, 1, 4));
        ItemList.Casing_ContainmentFieldEV.set(new ItemStack(this, 1, 5));
        ItemList.Casing_ContainmentFieldIV.set(new ItemStack(this, 1, 6));
        ItemList.Casing_ContainmentFieldZPM.set(new ItemStack(this, 1, 7));
        ItemList.Casing_ContainmentFieldUV.set(new ItemStack(this, 1, 8));
        ItemList.Compressor_Casing.set(new ItemStack(this, 1, 9));
        ItemList.Compressor_Pipe_Casing.set(new ItemStack(this, 1, 10));
        ItemList.Neutronium_Casing.set(new ItemStack(this, 1, 11));
        ItemList.Neutronium_Active_Casing.set(new ItemStack(this, 1, 12));
        ItemList.Neutronium_Stable_Casing.set(new ItemStack(this, 1, 13));
        ItemList.Coolant_Duct_Casing.set(new ItemStack(this, 1, 14));
        ItemList.Heating_Duct_Casing.set(new ItemStack(this, 1, 15));
        ItemList.Extreme_Density_Casing.set(new ItemStack(this, 1, 16));
        ItemList.Background_Radiation_Casing.set(new ItemStack(this, 1, 17));
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
            case 4 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_HV.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_EV.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_IV.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_ZPM.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_UV.getIcon();
            case 9 -> Textures.BlockIcons.COMPRESSOR_CASING.getIcon();
            case 10 -> Textures.BlockIcons.COMPRESSOR_PIPE_CASING.getIcon();
            case 11 -> Textures.BlockIcons.NEUTRONIUM_CASING.getIcon();
            case 12 -> Textures.BlockIcons.NEUTRONIUM_ACTIVE_CASING.getIcon();
            case 13 -> Textures.BlockIcons.NEUTRONIUM_STABLE_CASING.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_PIPE_TUNGSTENSTEEL.getIcon();
            case 15 -> Textures.BlockIcons.MACHINE_CASING_PIPE_BRONZE.getIcon();
            case 16 -> Textures.BlockIcons.EXTREME_DENSITY_CASING.getIcon();
            case 17 -> Textures.BlockIcons.RADIATION_ABSORBENT_CASING.getIcon();

            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
