package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

/// A reference to a GregTech material by bare name, resolved lazily.
///
/// Property values that point at other materials store refs rather than [Material] instances, because the
/// referenced material may not be registered yet at the point the reference is created.
@Desugar
public record MaterialRef(String name) {

    public Material resolve() {
        return MaterialLibAPI.getMaterial("gregtech", name);
    }
}
