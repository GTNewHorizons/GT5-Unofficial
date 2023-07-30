package pers.gwyog.gtneioreplugin.plugin.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import pers.gwyog.gtneioreplugin.plugin.renderer.ItemDimensionDisplayRenderer;

public class BlockDimensionDisplay extends Block {

    private final String dimension;

    @SuppressWarnings("unused")
    public long getDimensionRocketTier() {
        return this.dimensionRocketTier;
    }

    private final long dimensionRocketTier;
    private final IIcon[] icons = new IIcon[6];

    public BlockDimensionDisplay(String dimension) {
        super(Material.rock);
        this.dimension = dimension;
        this.dimensionRocketTier = ItemDimensionDisplayRenderer.getPrefix(dimension);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[MathHelper.clamp_int(side, 0, 5)];
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.icons[0] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_bottom");
        this.icons[1] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_top");
        this.icons[2] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_back");
        this.icons[3] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_front");
        this.icons[4] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_left");
        this.icons[5] = iconRegister.registerIcon("gtneioreplugin:" + dimension + "_right");
    }

    public String getDimension() {
        return this.dimension;
    }
}
