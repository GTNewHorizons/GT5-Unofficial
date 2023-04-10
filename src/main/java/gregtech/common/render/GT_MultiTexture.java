package gregtech.common.render;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.interfaces.ILayeredTexture;
import gregtech.api.interfaces.ITexture;

/**
 * <p>
 * Lets Multiple ITextures Render overlay over each other.<
 * </p>
 * <p>
 * I should have done this much earlier...
 * </p>
 */
public class GT_MultiTexture extends GT_TextureBase implements ILayeredTexture {

    protected final ArrayList<ITexture> mTextures;

    protected GT_MultiTexture(ITexture... aTextures) {
        mTextures = new ArrayList<>(Arrays.asList(aTextures));
    }

    public static GT_MultiTexture get(ITexture... aTextures) {
        return GT_Mod.instance.isClientSide() ? new GT_MultiTexture(aTextures) : null;
    }

    // ITexture methods

    @Override
    @Deprecated
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, EAST);
    }

    @Override
    @Deprecated
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, WEST);
    }

    @Override
    @Deprecated
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, UP);
    }

    @Override
    @Deprecated
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, DOWN);
    }

    @Override
    @Deprecated
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, SOUTH);
    }

    @Override
    @Deprecated
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.render(aRenderer, aBlock, aX, aY, aZ, NORTH);
    }

    @Override
    public boolean isValidTexture() {
        return mTextures.stream()
            .allMatch(ITexture::isValidTexture);
    }

    @Override
    public void render(RenderBlocks renderer, Block block, int x, int y, int z, ForgeDirection sideDirection) {
        for (ITexture iTexture : mTextures) {
            if (iTexture != null && iTexture.isValidTexture()) iTexture.render(renderer, block, x, y, z, sideDirection);
        }
    }

    // ILayeredTexture methods

    @Override
    public ILayeredTexture add(ITexture iTexture) {
        mTextures.add(iTexture);
        return this;
    }

    @Override
    public ILayeredTexture add(int index, ITexture iTexture) {
        mTextures.add(index, iTexture);
        return this;
    }

    @Override
    public ILayeredTexture addAll(ITexture... iTexture) {
        mTextures.addAll(Arrays.asList(iTexture));
        return this;
    }

    @Override
    public ILayeredTexture addAll(int index, ITexture... iTexture) {
        mTextures.addAll(index, Arrays.asList(iTexture));
        return this;
    }

    @Override
    public int size() {
        return mTextures.size();
    }

    @Override
    public boolean isEmpty() {
        return mTextures.size() == 0;
    }

    // Iterable<ITexture> methods

    @Override
    public Iterator<ITexture> iterator() {
        return mTextures.iterator();
    }

    @Override
    public void forEach(Consumer<? super ITexture> action) {
        mTextures.forEach(action);
    }

    @Override
    public Spliterator<ITexture> spliterator() {
        return mTextures.spliterator();
    }
}
