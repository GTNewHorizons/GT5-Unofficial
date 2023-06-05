package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Casings3 extends GT_Block_Casings_Abstract {

    public GT_Block_Casings3() {
        super(GT_Item_Casings3.class, "gt.blockcasings3", GT_Material_Casings.INSTANCE, 16);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Yellow Stripes Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Yellow Stripes Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Radioactive Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Bio Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Explosion Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Fire Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Acid Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Magic Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Frost Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Noise Hazard Sign Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Grate Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Filter Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Radiation Proof Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Bronze Firebox Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Steel Firebox Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Tungstensteel Firebox Casing");
        ItemList.Casing_Stripes_A.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Stripes_B.set(new ItemStack(this, 1, 1));
        ItemList.Casing_RadioactiveHazard.set(new ItemStack(this, 1, 2));
        ItemList.Casing_BioHazard.set(new ItemStack(this, 1, 3));
        ItemList.Casing_ExplosionHazard.set(new ItemStack(this, 1, 4));
        ItemList.Casing_FireHazard.set(new ItemStack(this, 1, 5));
        ItemList.Casing_AcidHazard.set(new ItemStack(this, 1, 6));
        ItemList.Casing_MagicHazard.set(new ItemStack(this, 1, 7));
        ItemList.Casing_FrostHazard.set(new ItemStack(this, 1, 8));
        ItemList.Casing_NoiseHazard.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Grate.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Vent.set(new ItemStack(this, 1, 11));
        ItemList.Casing_RadiationProof.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Firebox_Bronze.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Firebox_Steel.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Firebox_TungstenSteel.set(new ItemStack(this, 1, 15));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta + 32;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_STRIPES_A.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_STRIPES_B.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_BIOHAZARD.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_EXPLOSIONHAZARD.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_FIREHAZARD.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_MAGICHAZARD.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_FROSTHAZARD.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_NOISEHAZARD.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_GRATE.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VENT.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
            case 13 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon()
                : Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
            case 14 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
            case 15 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TUNGSTENSTEEL.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }
}
