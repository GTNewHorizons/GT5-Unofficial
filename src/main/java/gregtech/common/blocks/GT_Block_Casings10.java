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

        ItemList.Casing_Electromagnetic_Separator.set(new ItemStack(this, 1, 0));
        ItemList.BlockQuarkContainmentCasing.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Laser.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Autoclave.set(new ItemStack(this, 1, 3));
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
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
