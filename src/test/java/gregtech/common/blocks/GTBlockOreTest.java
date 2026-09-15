package gregtech.common.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GTBlockOreTest {

    @Test
    void naturalMetadataUsesTheSameTextureCacheKey() {
        assertEquals(42, GTBlockOre.getTextureCacheKey(42));
        assertEquals(42, GTBlockOre.getTextureCacheKey(8042));
        assertEquals(16042, GTBlockOre.getTextureCacheKey(16042));
        assertEquals(16042, GTBlockOre.getTextureCacheKey(24042));
    }
}
