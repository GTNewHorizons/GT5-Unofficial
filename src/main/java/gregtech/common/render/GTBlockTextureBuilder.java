package gregtech.common.render;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class GTBlockTextureBuilder {
    private Block fromBlock;
    private int fromMeta;
    private ForgeDirection fromSide;
    private Boolean worldCoord = null;
    private short[] rgba;

    public GTBlockTextureBuilder() {
        rgba = Dyes._NULL.getRGBA();
    }

    public GTBlockTextureBuilder setFromBlock(final Block block, final int meta) {
        this.fromBlock = block;
        this.fromMeta = meta;
        this.fromSide = ForgeDirection.UNKNOWN;
        return this;
    }

    public GTBlockTextureBuilder setFromSide(final ForgeDirection side) {
        this.fromSide = side;
        return this;
    }

    public GTBlockTextureBuilder setRGBA(final short[] rgba) {
        this.rgba = rgba;
        return this;
    }

    /**
     * Force using world coord overload of getIcon.
     *
     * @return {@link ITextureBuilder} for chaining
     * @throws IllegalStateException if setFromBlock has never been called.
     */
    public GTBlockTextureBuilder useWorldCoord() {
        this.worldCoord = true;
        return this;
    }

    /**
     * Force using meta overload of getIcon.
     *
     * @return {@link ITextureBuilder} for chaining
     */
    public GTBlockTextureBuilder noWorldCoord() {
        this.worldCoord = false;
        return this;
    }

    public ITexture build() {
        if (fromBlock == null) throw new IllegalArgumentException("Block not specified.");
        if (worldCoord == Boolean.TRUE || (worldCoord == null && isCTMBlock(fromBlock, fromMeta))) {
            return new GTCopiedCTMBlockTexture(fromBlock, fromSide.ordinal(), fromMeta, rgba);
        }
        return new GTCopiedBlockTextureRender(fromBlock, fromSide.ordinal(), fromMeta, rgba);
    }


    private static boolean isCTMBlock(Block fromBlock, int fromMeta) {
        return GTMod.proxy.mCTMBlockCache.computeIfAbsent(fromBlock, fromMeta, GTBlockTextureBuilder::apply);
    }

    private static Boolean apply(Block b, int m) {
        Class<?> clazz = b.getClass();
        while (clazz != Block.class) {
            final String className = clazz.getName();
            if (GTValues.mCTMDisabledBlock.contains(className)) return false;
            if (GTValues.mCTMEnabledBlock.contains(className)) return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}
