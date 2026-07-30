package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.github.bsideup.jabel.Desugar;

/// The Forge fluid a material declares for each state; a null slot means the material has no fluid in that
/// state. [FluidShapes] and
/// `gregtech.loaders.preload.LoaderGTBlockFluid` register fluids under exactly these names, which world NBT
/// resolves fluid stacks against.
@Desugar
public record FluidNames(FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {

    /// The name of a material's non-plasma fluid: `molten` first, else `fluid`, else `gas`. The order is
    /// fixed, and matches the shape order [MaterialUtils#anyFluidOf] resolves the same fluid in.
    public @Nullable String legacyGtppFluidName() {
        if (molten != null) return molten.name();
        if (fluid != null) return fluid.name();
        if (gas != null) return gas.name();
        return null;
    }
}
