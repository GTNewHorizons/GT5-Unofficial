package gregtech.api.enums.materials2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.MaterialIconRegistry.IconType;
import gregtech.api.enums.TextureSet;

/// Resolves the legacy `Materials#mIconSet` [TextureSet] for a MaterialLib [Material] from its
/// [StandardProperties#TEXTURE_SET] name, plus the five materials whose icon set overlays a handful of icons
/// onto a base set. Facade-independent -- reads only the [Material] and its texture-set property -- so it
/// outlives the legacy `Materials` facade. `MaterialUtils#iconSet` resolves every material's icon set through here,
/// bartworks-origin materials included.
public final class Materials2Textures {

    private Materials2Textures() {}

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

    /// The five legacy materials whose icon set overlays a handful of icons onto a base set
    /// ([TextureSet#withCustomTextures]) rather than reusing a plain named constant. MaterialLib's texture
    /// dump only retains the resolved set name (`"CUSTOM/iron"`), not which icons were overridden or what the
    /// base set was, so these five are reproduced directly from the original `MaterialsInit` declarations
    /// rather than derived.
    private static TextureSet customIconSet(String name) {
        return switch (name) {
            case "Copper" -> TextureSet.SET_DULL
                .withCustomTextures("copper", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Gold" -> TextureSet.SET_SHINY
                .withCustomTextures("gold", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Iron" -> TextureSet.SET_METALLIC
                .withCustomTextures("iron", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Diamond" -> TextureSet.SET_DIAMOND.withCustomTextures("diamond", IconType.ORE, IconType.ORE_SMALL);
            case "Emerald" -> TextureSet.SET_EMERALD.withCustomTextures("emerald", IconType.ORE, IconType.ORE_SMALL);
            default -> null;
        };
    }

    /// The legacy [TextureSet] for `ml`, resolved from its [StandardProperties#TEXTURE_SET] name (or the
    /// custom overlay for one of the five special-cased materials). Throws if the texture-set name names no
    /// legacy [TextureSet].
    public static TextureSet iconSetOf(Material ml) {
        TextureSet custom = customIconSet(ml.getName());
        if (custom != null) return custom;
        String setName = ml.getProperty(StandardProperties.TEXTURE_SET)
            .getName();
        TextureSet resolved = TEXTURE_SETS_BY_NAME.get(setName);
        if (resolved == null)
            throw new IllegalStateException("No legacy TextureSet named " + setName + " for material " + ml.getName());
        return resolved;
    }
}
