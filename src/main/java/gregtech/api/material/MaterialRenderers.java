package gregtech.api.material;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.common.render.items.GeneratedMaterialRenderer;

/// The per-material special item renderers. Each is registered together with
/// [com.ruling_0.materiallib.api.MaterialLibClient#setItemRenderer] so this store and MaterialLib's lookup agree.
///
/// Kept apart from [MaterialUtils] because [GeneratedMaterialRenderer] is client-side.
public class MaterialRenderers {

    private MaterialRenderers() {}

    private static final Map<Material, GeneratedMaterialRenderer> materialRenderers = new HashMap<>();

    /// Registers `renderer` as the special item renderer for `material`.
    public static void recordRenderer(Material material, GeneratedMaterialRenderer renderer) {
        materialRenderers.put(material, renderer);
    }

    /// The [GeneratedMaterialRenderer] [#recordRenderer] registered for `material`, or null when it has no
    /// special renderer.
    public static @Nullable GeneratedMaterialRenderer rendererOf(@Nullable Material material) {
        return material == null ? null : materialRenderers.get(material);
    }
}
