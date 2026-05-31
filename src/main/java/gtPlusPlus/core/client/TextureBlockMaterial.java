package gtPlusPlus.core.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

@SideOnly(Side.CLIENT)
public class TextureBlockMaterial extends TextureAtlasSprite {

    private static final List<TextureBlockMaterial> INSTANCES = new ArrayList<>();

    protected Material material;
    /// Greyscale ARGB pixels per mipmap level, snapshotted at stitch time. Read-only after generateMipmaps.
    private int[][] basePixels;
    /// Tinted output buffer; uploaded to the atlas each tick. Used since the base attribute
    /// is cleared when hasAnimation is false (alternative to no-oping things called when it's true)
    private int[][] tintedPixels;

    private int lastTintInt = Integer.MIN_VALUE;
    private final int[] tintLUT = new int[256];

    public TextureBlockMaterial(String iconName, Material material) {
        super(iconName);
        this.material = material;
        INSTANCES.add(this);
    }

    /// Driven from {@code TextureBlockMaterialTickHandler} to update the tint every client tick.
    public static void tickAll() {
        if (INSTANCES.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.getTextureManager() == null) return;
        mc.getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);
        for (TextureBlockMaterial t : INSTANCES) {
            if (t.basePixels == null) continue;
            t.tickTint();
        }
    }

    @Override
    public boolean hasAnimationMetadata() {
        return false;
    }

    @Override
    public void generateMipmaps(int mipmapLevels) {
        super.generateMipmaps(mipmapLevels);
        int[][] frame0 = framesTextureData.get(0);
        basePixels = new int[frame0.length][];
        tintedPixels = new int[frame0.length][];
        for (int level = 0; level < frame0.length; level++) {
            if (frame0[level] != null) {
                basePixels[level] = frame0[level].clone();
                tintedPixels[level] = new int[frame0[level].length];
            }
        }
    }

    private void tickTint() {
        final int tintInt = BaseItemComponent.getMaterialCustomColor(material);
        if (tintInt == lastTintInt) return;
        lastTintInt = tintInt;

        final int tR = (tintInt >> 16) & 0xFF;
        final int tG = (tintInt >> 8) & 0xFF;
        final int tB = tintInt & 0xFF;
        for (int v = 0; v < 256; v++) {
            tintLUT[v] = ((v * tR / 255) << 16) | ((v * tG / 255) << 8) | (v * tB / 255);
        }

        for (int row = 0; row < tintedPixels.length; row++) {
            int[] src = basePixels[row];
            if (src == null) continue;
            int[] out = tintedPixels[row];
            for (int i = 0; i < src.length; i++) {
                final int s = src[i];
                out[i] = (s & 0xFF000000) | tintLUT[s & 0xFF];
            }
        }
        TextureUtil.uploadTextureMipmap(tintedPixels, width, height, originX, originY, false, false);
    }
}
