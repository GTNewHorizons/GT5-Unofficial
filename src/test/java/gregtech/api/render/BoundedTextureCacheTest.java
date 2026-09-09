package gregtech.api.render;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import gregtech.api.interfaces.ITexture;

class BoundedTextureCacheTest {

    @Test
    void staysBoundedAndDoesNotReturnTexturesForOtherMetadata() {
        BoundedTextureCache cache = new BoundedTextureCache();
        ITexture[][][] textures = new ITexture[513][][];

        for (int metadata = 0; metadata < textures.length; metadata++) {
            textures[metadata] = new ITexture[0][];
            cache.put(metadata, textures[metadata]);
        }

        int cached = 0;
        for (int metadata = 0; metadata < textures.length; metadata++) {
            ITexture[][] value = cache.get(metadata);
            if (value != null) {
                assertSame(textures[metadata], value);
                cached++;
            }
        }
        assertSame(textures[512], cache.get(512));
        assertTrue(cached <= 512);
    }

    @Test
    void retainsEightCollidingMetadataValues() {
        BoundedTextureCache cache = new BoundedTextureCache();
        int[] metadata = new int[9];
        ITexture[][][] textures = new ITexture[metadata.length][][];

        for (int candidate = 0, found = 0; found < metadata.length; candidate++) {
            if (BoundedTextureCache.bucketStart(candidate) == 0) metadata[found++] = candidate;
        }
        for (int i = 0; i < 8; i++) {
            textures[i] = new ITexture[0][];
            cache.put(metadata[i], textures[i]);
            assertSame(textures[i], cache.get(metadata[i]));
        }

        textures[8] = new ITexture[0][];
        cache.put(metadata[8], textures[8]);
        assertNull(cache.get(metadata[0]));
        for (int i = 1; i < metadata.length; i++) {
            assertSame(textures[i], cache.get(metadata[i]));
        }
    }
}
