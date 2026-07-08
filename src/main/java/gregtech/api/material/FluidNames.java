package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The legacy fluids a material declares, one slot per state; a null slot means the legacy material never
/// registered a fluid for that state. Stages 04 and 06 consume this to recreate the same fluids byte-identical
/// to the legacy system.
@Desugar
public record FluidNames(FluidRef solid, FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {}
