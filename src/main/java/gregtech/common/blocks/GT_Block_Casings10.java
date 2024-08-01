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
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Solidifier Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Solidifier Radiator");
        ItemList.Casing_Electromagnetic_Separator.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Fluid_Solidifier.set(new ItemStack(this, 1, 3));
        ItemList.Radiator_Fluid_Solidifier.set(new ItemStack(this, 1, 4));
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
            case 3 -> Textures.BlockIcons.MACHINE_CASING_MS160.getIcon();
            case 4 -> Textures.BlockIcons.RADIATOR_MS160.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
