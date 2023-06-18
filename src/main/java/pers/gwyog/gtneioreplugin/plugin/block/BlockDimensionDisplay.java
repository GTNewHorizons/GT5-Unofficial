package pers.gwyog.gtneioreplugin.plugin.block;

import static pers.gwyog.gtneioreplugin.plugin.renderer.ItemDimensionDisplayRenderer.getPrefix;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;

public class BlockDimensionDisplay extends Block {

    private final String dimension;

    @SuppressWarnings("unused")
    public long getDimensionRocketTier() {
        return dimensionRocketTier;
    }

    private final long dimensionRocketTier;
    private IIcon iconTop;
    private IIcon iconRight;
    private IIcon iconLeft;

    public BlockDimensionDisplay(String dimension) {
        super(Material.rock);
        this.dimension = dimension;
        this.dimensionRocketTier = getPrefix(dimension);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        EnumFacing direction = EnumFacing.getFront(side);
        return switch (direction) {
            case NORTH, SOUTH -> iconRight;
            case WEST, EAST -> iconLeft;
            default -> iconTop;
        };
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.iconTop = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_top");
        this.iconRight = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_right");
        this.iconLeft = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_left");
    }

    public String getDimension() {
        return dimension;
    }
}
