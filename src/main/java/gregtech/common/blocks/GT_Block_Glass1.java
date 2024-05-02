package gregtech.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

/**
 * The glass is split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Glass1 extends GT_Block_Casings_Abstract {

    public GT_Block_Glass1() {
        super(GT_Item_Glass1.class, "gt.blockglass1", Material.glass, 2);
        this.opaque = false;

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Acid Resistant Glass");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".0.tooltip", "Able to resist very low pH values");

        ItemList.GlassAcidResistant.set(new ItemStack(this, 1, 0));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        // Page 16, 0-16
        return (16 << 7) | (aMeta);
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.GLASS_ACID_RESISTANT.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
