package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The Forge fluid a material declares for each state; a null slot means the material has no fluid in that
/// state. [gregtech.api.enums.materials2.Materials2FluidShapes] and
/// `gregtech.loaders.preload.LoaderGTBlockFluid` register fluids under exactly these names, which world NBT
/// resolves fluid stacks against.
@Desugar
public record FluidNames(FluidRef solid, FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {

    /// The name of a gtPlusPlus-originated material's non-plasma fluid: `molten` first, else `fluid`, else
    /// `gas`. Null when none of the three slots is set.
    public String legacyGtppFluidName() {
        if (molten != null) return molten.name();
        if (fluid != null) return fluid.name();
        if (gas != null) return gas.name();
        return null;
    }
}
