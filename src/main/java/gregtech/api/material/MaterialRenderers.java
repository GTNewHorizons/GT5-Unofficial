package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibClient;

import gregtech.common.render.items.GeneratedMaterialRenderer;

/// Reads the special item renderer a material carries through [MaterialLibClient#setItemRenderer]. Client-side only.
public class MaterialRenderers {

    private MaterialRenderers() {}

    /// Returns the material's renderer when it is a [GeneratedMaterialRenderer], otherwise null.
    public static @Nullable GeneratedMaterialRenderer rendererOf(@Nullable Material material) {
        if (material == null) return null;
        return MaterialLibClient.getItemRenderer(material) instanceof GeneratedMaterialRenderer renderer ? renderer
            : null;
    }
}
