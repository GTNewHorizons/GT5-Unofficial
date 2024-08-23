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
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Containment Machine Casing HV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Containment Machine Casing EV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Containment Machine Casing IV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Containment Machine Casing ZPM");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Containment Machine Casing UV");

        ItemList.Casing_Electromagnetic_Separator.set(new ItemStack(this, 1, 0));
        ItemList.BlockQuarkContainmentCasing.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Laser.set(new ItemStack(this, 1, 1));
        ItemList.Casing_ContainmentFieldHV.set(new ItemStack(this, 1, 3));
        ItemList.Casing_ContainmentFieldEV.set(new ItemStack(this, 1, 4));
        ItemList.Casing_ContainmentFieldIV.set(new ItemStack(this, 1, 5));
        ItemList.Casing_ContainmentFieldZPM.set(new ItemStack(this, 1, 6));
        ItemList.Casing_ContainmentFieldUV.set(new ItemStack(this, 1, 7));

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
            case 3 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_HV.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_EV.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_IV.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_ZPM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_UV.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
