package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// A fluid's frozen Forge registry name and the still-icon texture path it registers with. The texture is null when
/// the icon comes from another mod or a texture set.
@Desugar
public record FluidRef(String name, String texture) {}
