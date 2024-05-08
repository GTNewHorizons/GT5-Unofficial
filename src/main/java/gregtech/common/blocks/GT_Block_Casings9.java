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
        super(GT_Item_Casings9.class, "gt.blockcasings9", GT_Material_Casings.INSTANCE, 9);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "PBI Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Advanced Filter Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".1.tooltip", "Less than five 0.1μm particles per m^3");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Industrial Strength Concrete");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.tooltip", "An industrial flooring.");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Industrial Water Plant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Sterile Water Plant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Clean Flocculation Casing");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".6.name", "Naquadah Reinforced Water Plant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Extreme Base Resistant Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Extreme Acid Resistant Casing");

        ItemList.Casing_Pipe_Polybenzimidazole.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Vent_T2.set(new ItemStack(this, 1, 1));
        ItemList.BlockIndustrialStrengthConcrete.set(new ItemStack(this, 1, 2));
        ItemList.BlockIndustrialWaterPlantCasing.set(new ItemStack(this, 1, 3));
        ItemList.BlockSterileWaterPlantCasing.set(new ItemStack(this, 1, 4));
        ItemList.BlockFlocculationCasing.set(new ItemStack(this, 1, 5));
        ItemList.BlockNaquadahReinforcedWaterPlantCasing.set(new ItemStack(this, 1, 6));
        ItemList.BlockExtremeBaseResistantCasing.set(new ItemStack(this, 1, 7));
        ItemList.BlockExtremeAcidResistantCasing.set(new ItemStack(this, 1, 8));
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
            case 2 -> Textures.BlockIcons.INDUSTRIAL_STRENGTH_CONCRETE.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_INDUSTRIAL_WATER_PLANT.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_STERILE_WATER_PLANT.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_COAGULATION.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_NAQUADAH_REINFORCED_WATER_PLANT.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_BASE_RESISTANT.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_ACID_RESISTANT.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
