package gttests;

import java.lang.reflect.Field;
import java.util.Map;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.postea.utility.TransformerRegistry;

/// Postea resolves its id-keyed transformers only when it loads a saved world, so tests of migrated saves skip in a
/// world's first session.
public final class PosteaTestSupport {

    private PosteaTestSupport() {}

    /// Skips the running test unless Postea's id-keyed transformers are active.
    public static void assumeSavedWorld(GameTestHelper helper) {
        boolean active;
        try {
            Field field = TransformerRegistry.class.getDeclaredField("RUNTIME_BLOCK_REPLACEMENT_MAP");
            field.setAccessible(true);
            active = !((Map<?, ?>) field.get(null)).isEmpty();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Postea's runtime transformer table is unreadable", e);
        }
        helper.assumeTrue(active, "Postea's id-keyed transformers are inactive in a world's first session");
    }
}
