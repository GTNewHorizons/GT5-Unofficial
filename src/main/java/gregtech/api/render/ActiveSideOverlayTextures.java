package gregtech.api.render;

import static gregtech.api.interfaces.IBlockWithActiveOffset.ACTIVE_OFFSET;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.IntPredicate;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;

/**
 * Builds and caches the textures of a 16-subtype block whose selected subtypes gain a glowing overlay on their
 * horizontal faces while the active offset is applied to their client metadata.
 */
public class ActiveSideOverlayTextures {

    private final Block block;
    private final IntPredicate overlayMetadata;
    private final IIconContainer sideOverlay;
    private final AtomicReferenceArray<ITexture[][]> cache = new AtomicReferenceArray<>(ACTIVE_OFFSET * 2);

    public ActiveSideOverlayTextures(Block block, IntPredicate overlayMetadata, IIconContainer sideOverlay) {
        this.block = block;
        this.overlayMetadata = overlayMetadata;
        this.sideOverlay = sideOverlay;
    }

    public boolean hasOverlay(int baseMetadata) {
        return overlayMetadata.test(baseMetadata);
    }

    public ITexture[][] getTextures(int metadata) {
        int baseMetadata = Math.floorMod(metadata, ACTIVE_OFFSET);
        boolean active = metadata >= ACTIVE_OFFSET && hasOverlay(baseMetadata);
        int cacheIndex = active ? baseMetadata + ACTIVE_OFFSET : baseMetadata;

        ITexture[][] cached = cache.get(cacheIndex);
        if (cached != null) return cached;

        ITexture[][] built = build(baseMetadata, active);
        return cache.compareAndSet(cacheIndex, null, built) ? built : cache.get(cacheIndex);
    }

    private ITexture[][] build(int baseMetadata, boolean active) {
        ITexture[] bottom = { TextureFactory.of(block, baseMetadata, ForgeDirection.DOWN) };
        ITexture[] top = { TextureFactory.of(block, baseMetadata, ForgeDirection.UP) };
        ITexture side = TextureFactory.of(block, baseMetadata, ForgeDirection.NORTH);
        ITexture[] sides = active ? new ITexture[] { side, buildOverlay() } : new ITexture[] { side };
        return new ITexture[][] { bottom, top, sides, sides, sides, sides };
    }

    private ITexture buildOverlay() {
        return TextureFactory.builder()
            .addIcon(sideOverlay)
            .glow()
            .build();
    }
}
