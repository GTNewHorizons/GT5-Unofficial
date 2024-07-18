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

        ItemList.Casing_Pipe_Polybenzimidazole.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Vent_T2.set(new ItemStack(this, 1, 1));
        ItemList.WoodenCasing.set(new ItemStack(this, 1, 2));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta + 64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYBENZIMIDAZOLE.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_VENT_T2.getIcon();
            case 2 -> ordinalSide >= 2 ? Textures.BlockIcons.TEXTURE_METAL_PANEL_E.getIcon()
                : Textures.BlockIcons.TEXTURE_METAL_PANEL_E_A.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
