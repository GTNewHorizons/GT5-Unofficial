package gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The glass is split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * <p>
 * This class hosts various special types of tiered glass with not many tiers.
 */
public class BlockGlass1 extends BlockCasingsAbstract {

    public BlockGlass1() {
        super(ItemCasings.class, "gt.blockglass1", Material.glass, 4);
        this.opaque = false;

        register(0, ItemList.GlassPHResistant, "Chemical Grade Glass", translatedText("gt.casing.chemical-resistant"));
        register(1, ItemList.GlassUVResistant, "Electron-Permeable Neutronium Coated Glass");
        register(2, ItemList.GlassOmniPurposeInfinityFused, "Omni-Purpose Infinity Fused Glass");
        register(3, ItemList.GlassQuarkContainment, "Non-Photonic Matter Exclusion Glass");
        register(
            4,
            ItemList.Hawking_Glass,
            "Hawking Radiation Realignment Focus",
            translatedText("gt.casing.hawking-focus"));
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
            case 0 -> Textures.BlockIcons.GLASS_PH_RESISTANT.getIcon();
            case 1 -> Textures.BlockIcons.NEUTRONIUM_COATED_UV_RESISTANT_GLASS.getIcon();
            case 2 -> Textures.BlockIcons.OMNI_PURPOSE_INFINITY_FUSED_GLASS.getIcon();
            case 3 -> Textures.BlockIcons.GLASS_QUARK_CONTAINMENT.getIcon();
            case 4 -> Textures.BlockIcons.HAWKING_GLASS.getIcon();
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
