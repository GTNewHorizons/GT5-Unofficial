package gregtech.api.enums.materials;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.TextureSet;

/// Resolves the legacy icon-set [TextureSet] for a MaterialLib [Material] from its
/// [StandardProperties#TEXTURE_SET] name. Facade-independent -- reads only the [Material] and its texture-set
/// property -- so it outlives the legacy `Materials` facade. `MaterialUtils#iconSet` resolves every material's icon
/// set through here, bartworks-origin materials included.
public final class MaterialTextures {

    private MaterialTextures() {}

    private static final Map<String, TextureSet> TEXTURE_SETS_BY_NAME = indexTextureSets();

    private static Map<String, TextureSet> indexTextureSets() {
        Map<String, TextureSet> byName = new HashMap<>();
        for (Field field : TextureSet.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != TextureSet.class) continue;
            try {
                TextureSet set = (TextureSet) field.get(null);
                byName.put(set.mSetName, set);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to index " + field, e);
            }
        }
        return byName;
    }

    /// The legacy [TextureSet] for `material`, resolved from its [StandardProperties#TEXTURE_SET] name. Throws if
    /// the texture-set name names no legacy [TextureSet].
    public static TextureSet iconSetOf(Material material) {
        String setName = material.getProperty(StandardProperties.TEXTURE_SET)
            .getName();
        TextureSet resolved = TEXTURE_SETS_BY_NAME.get(setName);
        if (resolved == null) throw new IllegalStateException(
            "No legacy TextureSet named " + setName + " for material " + material.getName());
        return resolved;
    }
}
