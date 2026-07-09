package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// A legacy fluid name and its temperature in Kelvin, as registered with Forge's fluid registry, plus the
/// still-icon texture path its `GTFluid` registered (captured by the dump, see
/// `gregtech.common.fluid.GTFluid#DUMP_TEXTURES`), or null if the dump never captured one (an other-mod-owned
/// or texture-set-driven fluid).
@Desugar
public record FluidRef(String name, int temperature, String texture) {}
