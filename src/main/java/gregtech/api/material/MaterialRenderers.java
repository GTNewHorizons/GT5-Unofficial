package gregtech.api.material;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.common.render.items.GeneratedMaterialRenderer;

/// The per-material special item renderers, registered from `GTClient#onPreInitialization` alongside
/// [com.ruling_0.materiallib.api.MaterialLibClient#setItemRenderer] so this store and MaterialLib's own
/// renderer lookup agree.
///
/// Held apart from [MaterialUtils] because a [GeneratedMaterialRenderer] is a client-side type and the
/// material read layer is common-side.
public class MaterialRenderers {

    private MaterialRenderers() {}

    private static final Map<Material, GeneratedMaterialRenderer> materialRenderers = new HashMap<>();

    /// Registers `renderer` as the special item renderer for `material`, keyed by the MaterialLib [Material].
    /// Called once from `GTClient#onPreInitialization`, alongside
    /// [com.ruling_0.materiallib.api.MaterialLibClient#setItemRenderer] -- both stores are populated
    /// together so [#rendererOf] and MaterialLib's own renderer lookup agree.
    public static void recordRenderer(Material material, GeneratedMaterialRenderer renderer) {
        materialRenderers.put(material, renderer);
    }

    /// The [GeneratedMaterialRenderer] [#recordRenderer] registered for `material`, or null when it has no
    /// special renderer. Read by the generated-item, fluid-display, and electrode renderers.
    public static @Nullable GeneratedMaterialRenderer rendererOf(@Nullable Material material) {
        return material == null ? null : materialRenderers.get(material);
    }
}
