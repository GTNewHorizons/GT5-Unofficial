package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// A fluid's frozen Forge registry name and its temperature in Kelvin, plus the still-icon texture path it
/// registers with. The texture is null for a fluid whose icon comes from elsewhere -- another mod's
/// registration, or a texture set.
@Desugar
public record FluidRef(String name, int temperature, String texture) {}
