package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The Forge fluid a [gregtech.api.enums.materials.MaterialFluidNames] entry declares for each state. A null slot
/// means GregTech's fluid lookups find no fluid for the material in that state.
/// [gregtech.api.enums.materials.FluidShapes] registers fluids under exactly these names, and saved fluid stacks
/// resolve against them.
@Desugar
public record FluidNames(FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {}
