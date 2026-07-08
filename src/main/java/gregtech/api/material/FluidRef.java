package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// A legacy fluid name and its temperature in Kelvin, as registered with Forge's fluid registry.
@Desugar
public record FluidRef(String name, int temperature) {}
