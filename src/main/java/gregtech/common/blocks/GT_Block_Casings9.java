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
public class GT_Block_Casings9 extends GT_Block_Casings_Abstract {

    public GT_Block_Casings9() {
        super(GT_Item_Casings9.class, "gt.blockcasings9", GT_Material_Casings.INSTANCE, 16);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "PBI Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Advanced Filter Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".1.tooltip", "Less than five 0.1μm particles per m^3");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Primitive Wooden Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".3.name", "Superplasticizer-Treated High Strength Concrete");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Sterile Water Plant Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".5.name", "Reinforced Sterile Water Plant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Clean Flocculation Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".7.name", "Naquadah Reinforced Water Plant Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".8.name", "Extreme Corrosion Resistant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "High Pressure Resistant Casing");
        // todo: ask deleno for non placeholder name
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Ozonation Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Plasma Heating Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".12.name", "Naquadria-Reinforced Water Plant Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".13.name", "Ultraviolet Backlight Sterilizer Casing");

        ItemList.Casing_Pipe_Polybenzimidazole.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Vent_T2.set(new ItemStack(this, 1, 1));
        ItemList.WoodenCasing.set(new ItemStack(this, 1, 2));
        ItemList.BlockIndustrialStrengthConcrete.set(new ItemStack(this, 1, 3));
        ItemList.BlockIndustrialWaterPlantCasing.set(new ItemStack(this, 1, 4));
        ItemList.BlockSterileWaterPlantCasing.set(new ItemStack(this, 1, 5));
        ItemList.BlockFlocculationCasing.set(new ItemStack(this, 1, 6));
        ItemList.BlockNaquadahReinforcedWaterPlantCasing.set(new ItemStack(this, 1, 7));
        ItemList.BlockExtremeCorrosionResistantCasing.set(new ItemStack(this, 1, 8));
        ItemList.BlockHighPressureResistantCasing.set(new ItemStack(this, 1, 9));
        ItemList.BlockOzoneCasing.set(new ItemStack(this, 1, 10));
        ItemList.BlockPlasmaHeatingCasing.set(new ItemStack(this, 1, 11));
        ItemList.BlockNaquadriaReinforcedWaterPlantCasing.set(new ItemStack(this, 1, 12));
        ItemList.BlockUltraVioletLaserEmitter.set(new ItemStack(this, 1, 13));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYBENZIMIDAZOLE.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_VENT_T2.getIcon();
            case 2 -> ordinalSide >= 2 ? Textures.BlockIcons.TEXTURE_METAL_PANEL_E.getIcon()
                : Textures.BlockIcons.TEXTURE_METAL_PANEL_E_A.getIcon();
            case 3 -> Textures.BlockIcons.INDUSTRIAL_STRENGTH_CONCRETE.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_INDUSTRIAL_WATER_PLANT.getIcon();
            case 5 -> Textures.BlockIcons.WATER_PLANT_CONCRETE_CASING.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_FLOCCULATION.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_NAQUADAH_REINFORCED_WATER_PLANT.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_CORROSION_RESISTANT.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_HIGH_PRESSURE_RESISTANT.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_OZONE.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_PLASMA_HEATER.getIcon();
            case 12 -> Textures.BlockIcons.NAQUADRIA_REINFORCED_WATER_PLANT_CASING.getIcon();
            case 13 -> Textures.BlockIcons.UV_BACKLIGHT_STERILIZER_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
