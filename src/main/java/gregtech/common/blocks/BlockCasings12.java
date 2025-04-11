package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings12 extends BlockCasingsAbstract {

    public BlockCasings12() {
        super(ItemCasings.class, "gt.blockcasings12", MaterialCasings.INSTANCE, 16);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Simple Vat Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Advanced Vat Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Biologically Enhanced Vat Casing");

        ItemList.Casing_Vat_T1.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Vat_T2.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Vat_T3.set(new ItemStack(this, 1, 2));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.STERILE_VAT_CASING_T1.getIcon();
            case 1 -> Textures.BlockIcons.STERILE_VAT_CASING_T2.getIcon();
            case 2 -> Textures.BlockIcons.STERILE_VAT_CASING_T3.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
