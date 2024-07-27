package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Casings11 extends GT_Block_Casings_Abstract {

    public GT_Block_Casings11() {
        super(GT_Item_Casings11.class, "gt.blockcasings11", GT_Material_Casings.INSTANCE, 16);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Tin Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Brass Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Electrum Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Platinum Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Osmium Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Quantium Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Fluxed Electrum Item Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Black Plutonium Item Pipe Casing");

        ItemList.Casing_Item_Pipe_Tin.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Item_Pipe_Brass.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Item_Pipe_Electrum.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Item_Pipe_Platinum.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Item_Pipe_Osmium.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Item_Pipe_Quantium.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Item_Pipe_Fluxed_Electrum.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Item_Pipe_Black_Plutonium.set(new ItemStack(this, 1, 7));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 48);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_BRASS.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_ELECTRUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_PLATINUM.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_OSMIUM.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_QUANTIUM.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_FLUXED_ELECTRUM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_BLACK_PLUTONIUM.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_TIN.getIcon();
        };
    }
}
