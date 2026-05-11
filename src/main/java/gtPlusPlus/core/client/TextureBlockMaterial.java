package gtPlusPlus.core.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class TextureBlockMaterial extends TextureAtlasSprite {

    protected Material material;
    /// Greyscale ARGB pixels per mipmap level, snapshotted at stitch time.
    private int[][] basePixels;

    public TextureBlockMaterial(String iconName, Material material) {
        super(iconName);
        this.material = material;
    }

    @Override
    public boolean hasAnimationMetadata() {
        return true;
    }

    @Override
    public void generateMipmaps(int mipmapLevels) {
        super.generateMipmaps(mipmapLevels);
        int[][] frame0 = framesTextureData.get(0);
        basePixels = new int[frame0.length][];
        for (int level = 0; level < frame0.length; level++) {
            if (frame0[level] != null) {
                basePixels[level] = frame0[level].clone();
            }
        }
    }

    @Override
    public void updateAnimation() {
        final int tintInt = BaseItemComponent.getMaterialCustomColor(material);
        final int tR = (tintInt >> 16) & 0xFF;
        final int tG = (tintInt >> 8) & 0xFF;
        final int tB = tintInt & 0xFF;

        int[][] dest = this.framesTextureData.get(0);
        for (int row = 0; row < dest.length; row++) {
            int[] src = this.basePixels[row];
            if (src == null) continue;
            int[] out = dest[row];
            for (int i = 0; i < src.length; i++) {
                final int s = src[i];
                final int r = ((s >> 16) & 0xFF) * tR / 255;
                final int g = ((s >> 8) & 0xFF) * tG / 255;
                final int b = (s & 0xFF) * tB / 255;
                out[i] = (s & 0xFF000000) | (r << 16) | (g << 8) | b;
            }
        }
        TextureUtil.uploadTextureMipmap(dest, width, height, originX, originY, false, false);
    }
}
