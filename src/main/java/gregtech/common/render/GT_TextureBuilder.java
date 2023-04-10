package gregtech.common.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ILayeredTexture;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;

@SuppressWarnings({ "unused", "ClassWithTooManyFields" })
public class GT_TextureBuilder implements ITextureBuilder {

    private final List<IIconContainer> iconContainerList;
    private final ILayeredTexture textureLayers;
    private Block fromBlock;
    private int fromMeta;
    private ForgeDirection fromSide;
    private short[] rgba;
    private boolean allowAlpha;
    private boolean stdOrient;
    private boolean extFacing;
    private boolean glow;
    private Boolean worldCoord = null;

    public GT_TextureBuilder() {
        textureLayers = new GT_MultiTexture();
        iconContainerList = new ArrayList<>();
        rgba = Dyes._NULL.mRGBa;
        allowAlpha = true;
        stdOrient = false;
        glow = false;
    }

    @Override
    public ITextureBuilder setFromBlock(final Block block, final int meta) {
        this.fromBlock = block;
        this.fromMeta = meta;
        this.fromSide = ForgeDirection.UNKNOWN;
        return this;
    }

    @Override
    public ITextureBuilder setFromSide(final ForgeDirection side) {
        this.fromSide = side;
        return this;
    }

    @Override
    public ITextureBuilder addIcon(final IIconContainer... iconContainers) {
        this.iconContainerList.addAll(Arrays.asList(iconContainers));
        return this;
    }

    @Override
    public ITextureBuilder setRGBA(final short[] rgba) {
        this.rgba = rgba;
        return this;
    }

    @Override
    public ITextureBuilder addLayer(final ITexture... iTextures) {
        this.textureLayers.addAll(iTextures);
        return this;
    }

    @Override
    public ITextureBuilder setAllowAlpha(final boolean allowAlpha) {
        this.allowAlpha = allowAlpha;
        return this;
    }

    @Override
    public ITextureBuilder stdOrient() {
        this.stdOrient = true;
        return this;
    }

    @Override
    public ITextureBuilder useWorldCoord() {
        this.worldCoord = true;
        return this;
    }

    @Override
    public ITextureBuilder noWorldCoord() {
        this.worldCoord = false;
        return this;
    }

    @Override
    public ITextureBuilder extFacing() {
        this.extFacing = true;
        return this;
    }

    @Override
    public ITextureBuilder glow() {
        glow = true;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ITexture build() {
        if (fromBlock != null) {
            if (worldCoord == Boolean.TRUE || worldCoord == null && isCTMBlock(fromBlock, fromMeta))
                return new GT_CopiedCTMBlockTexture(fromBlock, fromSide.ordinal(), fromMeta, rgba, allowAlpha);
            else return new GT_CopiedBlockTexture(fromBlock, fromSide.ordinal(), fromMeta, rgba, allowAlpha);
        }
        if (worldCoord != null) throw new IllegalStateException("worldCoord without from block");
        if (!textureLayers.isEmpty()) return textureLayers;
        return switch (iconContainerList.size()) {
            case 1 -> new GT_RenderedTexture(iconContainerList.get(0), rgba, allowAlpha, glow, stdOrient, extFacing);
            case 6 -> new GT_SidedTexture(iconContainerList, rgba, allowAlpha);
            default -> throw new IllegalStateException("Invalid sideIconContainer count");
        };
    }

    private boolean isCTMBlock(Block fromBlock, int fromMeta) {
        return GT_Mod.gregtechproxy.mCTMBlockCache
            .computeIfAbsent(fromBlock, (byte) fromMeta, GT_TextureBuilder::apply);
    }

    private static Boolean apply(Block b, Byte m) {
        Class<?> clazz = b.getClass();
        while (clazz != Block.class) {
            final String className = clazz.getName();
            if (GT_Values.mCTMDisabledBlock.contains(className)) return false;
            if (GT_Values.mCTMEnabledBlock.contains(className)) return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}
