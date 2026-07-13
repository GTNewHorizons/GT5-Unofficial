package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The legacy fluids a material declares, one slot per state; a null slot means the legacy material never
/// registered a fluid for that state. Stages 04 and 06 consume this to recreate the same fluids byte-identical
/// to the legacy system.
@Desugar
public record FluidNames(FluidRef solid, FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {

    /// The name a legacy gtPlusPlus `Material#performFluidAndCellRegistration` would have registered this
    /// material's non-plasma fluid under -- `molten` for a solid/liquid material's `molten.<name>` fluid,
    /// else `fluid`, else `gas` (verified against every gtpp-carrying material's pinned dump to reproduce the
    /// legacy `Material.vFluidName`/`fluidName` value exactly, with zero exceptions). Null when none of the
    /// three slots are set.
    public String legacyGtppFluidName() {
        if (molten != null) return molten.name();
        if (fluid != null) return fluid.name();
        if (gas != null) return gas.name();
        return null;
    }
}
