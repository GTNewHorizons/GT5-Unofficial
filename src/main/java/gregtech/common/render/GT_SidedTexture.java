package gregtech.common.render;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class GT_SidedTexture extends GT_TextureBase implements ITexture, IColorModulationContainer {

    protected final EnumMap<ForgeDirection, ITexture> directionTextures = new EnumMap<>(ForgeDirection.class);
    /**
     * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
     * <p/>
     * Just set this variable to another different Array instead. Otherwise, some colored things will get Problems.
     */
    private final short[] mRGBa;

    /**
     * @deprecated use {@link #GT_SidedTexture(List, short[], boolean)} instead
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    @Deprecated
    protected GT_SidedTexture(IIconContainer iconDown, IIconContainer iconUp, IIconContainer iconNorth,
        IIconContainer iconSouth, IIconContainer iconWest, IIconContainer iconEast, short[] aRGBa,
        boolean aAllowAlpha) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
        directionTextures.put(DOWN, TextureFactory.of(iconDown, aRGBa, aAllowAlpha));
        directionTextures.put(UP, TextureFactory.of(iconUp, aRGBa, aAllowAlpha));
        directionTextures.put(NORTH, TextureFactory.of(iconNorth, aRGBa, aAllowAlpha));
        directionTextures.put(SOUTH, TextureFactory.of(iconSouth, aRGBa, aAllowAlpha));
        directionTextures.put(WEST, TextureFactory.of(iconWest, aRGBa, aAllowAlpha));
        directionTextures.put(EAST, TextureFactory.of(iconEast, aRGBa, aAllowAlpha));
        mRGBa = aRGBa;
    }

    protected GT_SidedTexture(List<IIconContainer> sideIcons, short[] aRGBa, boolean aAllowAlpha) {
        if (sideIcons.size() != 6)
            throw new IllegalArgumentException("sideIcons doesn't have 6 icons @ GT_SidedTexture");
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_SidedTexture");
        for (int side = 0; side < sideIcons.size(); side++) {
            directionTextures
                .put(ForgeDirection.VALID_DIRECTIONS[side], TextureFactory.of(sideIcons.get(side), aRGBa, aAllowAlpha));
        }
        mRGBa = aRGBa;
    }

    @Override
    @Deprecated
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(EAST)
            .render(aRenderer, aBlock, aX, aY, aZ, EAST);
    }

    @Override
    @Deprecated
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(WEST)
            .render(aRenderer, aBlock, aX, aY, aZ, WEST);
    }

    @Override
    @Deprecated
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(UP)
            .render(aRenderer, aBlock, aX, aY, aZ, UP);
    }

    @Override
    @Deprecated
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(DOWN)
            .render(aRenderer, aBlock, aX, aY, aZ, DOWN);
    }

    @Override
    @Deprecated
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(SOUTH)
            .render(aRenderer, aBlock, aX, aY, aZ, SOUTH);
    }

    @Override
    @Deprecated
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        directionTextures.get(NORTH)
            .render(aRenderer, aBlock, aX, aY, aZ, NORTH);
    }

    @Override
    public boolean isValidTexture() {
        return directionTextures.values()
            .stream()
            .allMatch(ITexture::isValidTexture);
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void render(RenderBlocks renderer, Block block, int x, int y, int z, ForgeDirection sideDirection) {
        directionTextures.get(sideDirection)
            .render(renderer, block, x, y, z, sideDirection);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }
}
