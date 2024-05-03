package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

public class GT_Block_TintedIndustrialGlass extends GT_Block_Casings_Abstract {

    public GT_Block_TintedIndustrialGlass() {
        super(GT_Item_Glass1.class, "gt.blocktintedglass", Material.glass, 4);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Tinted Industrial Glass (White)");
        GT_LanguageManager
            .addStringLocalization(getUnlocalizedName() + ".1.name", "Tinted Industrial Glass (Light Gray)");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Tinted Industrial Glass (Gray)");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Tinted Industrial Glass (Black)");
        ItemList.GlassTintedIndustrialWhite.set(new ItemStack(this, 1, 0));
        ItemList.GlassTintedIndustrialLightGray.set(new ItemStack(this, 1, 1));
        ItemList.GlassTintedIndustrialGray.set(new ItemStack(this, 1, 2));
        ItemList.GlassTintedIndustrialBlack.set(new ItemStack(this, 1, 3));
        this.opaque = false;
    }

    @Override
    public int getTextureIndex(int aMeta) {
        // Page 16, 32-47
        return (16 << 7) | (aMeta + 32);
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
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.GLASS_TINTED_INDUSTRIAL_WHITE.getIcon();
            case 1 -> Textures.BlockIcons.GLASS_TINTED_INDUSTRIAL_LIGHT_GRAY.getIcon();
            case 2 -> Textures.BlockIcons.GLASS_TINTED_INDUSTRIAL_GRAY.getIcon();
            case 3 -> Textures.BlockIcons.GLASS_TINTED_INDUSTRIAL_BLACK.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        Block block = worldIn.getBlock(x, y, z);

        if (worldIn.getBlockMetadata(x, y, z) != worldIn.getBlockMetadata(
            x - Facing.offsetsXForSide[side],
            y - Facing.offsetsYForSide[side],
            z - Facing.offsetsZForSide[side])) {
            return true;
        }

        if (block == this) {
            return false;
        }

        return super.shouldSideBeRendered(worldIn, x, y, z, side);
    }
}
