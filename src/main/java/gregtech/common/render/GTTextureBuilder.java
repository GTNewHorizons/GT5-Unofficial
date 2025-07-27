package gregtech.common.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.util.ColorUtil;

@SuppressWarnings({ "unused", "ClassWithTooManyFields" })
public class GTTextureBuilder implements ITextureBuilder {

    private @Nullable List<IIconContainer> iconContainerList;
    private @Nullable List<ITexture> textureLayers;
    private Block fromBlock;
    private int fromMeta;
    private ForgeDirection fromSide;
    private int colorRGB;
    private boolean stdOrient;
    private boolean extFacing;
    private boolean glow;
    private Boolean worldCoord = null;

    public GTTextureBuilder() {
        colorRGB = Dyes._NULL.colorRGB;
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
    public ITextureBuilder addIcon(final IIconContainer iconContainer) {
        if (iconContainerList == null) iconContainerList = new ArrayList<>(1);
        iconContainerList.add(iconContainer);
        return this;
    }

    @Override
    public ITextureBuilder addIcon(final IIconContainer... iconContainers) {
        if (iconContainerList == null) iconContainerList = new ArrayList<>(6);
        Collections.addAll(iconContainerList, iconContainers);
        return this;
    }

    @Override
    public ITextureBuilder setRGBA(final short[] rgba) {
        this.colorRGB = ColorUtil.fromRGBAToRGB(rgba);
        return this;
    }

    @Override
    public ITextureBuilder setRGB(final int colorRGB) {
        this.colorRGB = colorRGB;
        return this;
    }

    @Override
    public ITextureBuilder addLayer(final ITexture iTexture) {
        if (textureLayers == null) textureLayers = new ArrayList<>(1);
        textureLayers.add(iTexture);
        return this;
    }

    @Override
    public ITextureBuilder addLayer(final ITexture... iTextures) {
        if (textureLayers == null) textureLayers = new ArrayList<>();
        Collections.addAll(textureLayers, iTextures);
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
                return new GTCopiedCTMBlockTexture(fromBlock, fromSide.ordinal(), fromMeta);
            else return new GTCopiedBlockTextureRender(fromBlock, fromSide.ordinal(), fromMeta);
        }
        if (worldCoord != null) throw new IllegalStateException("worldCoord without from block");
        if (textureLayers != null && !textureLayers.isEmpty()) {
            return new GTMultiTextureRender(textureLayers.toArray(new ITexture[0]));
        }
        if (iconContainerList == null) {
            throw new IllegalStateException("Invalid sideIconContainer count");
        }
        return switch (iconContainerList.size()) {
            case 1 -> new GTRenderedTexture(iconContainerList.get(0), colorRGB, glow, stdOrient, extFacing);
            case 6 -> new GTSidedTextureRender(
                iconContainerList.get(ForgeDirection.DOWN.ordinal()),
                iconContainerList.get(ForgeDirection.UP.ordinal()),
                iconContainerList.get(ForgeDirection.NORTH.ordinal()),
                iconContainerList.get(ForgeDirection.SOUTH.ordinal()),
                iconContainerList.get(ForgeDirection.WEST.ordinal()),
                iconContainerList.get(ForgeDirection.EAST.ordinal()),
                colorRGB);
            default -> throw new IllegalStateException("Invalid sideIconContainer count");
        };
    }

    private boolean isCTMBlock(Block fromBlock, int fromMeta) {
        return GTMod.proxy.mCTMBlockCache.computeIfAbsent(fromBlock, fromMeta, GTTextureBuilder::apply);
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
