package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The Forge fluid a material declares for each state. A null slot means the material has no fluid in that state.
/// [gregtech.api.enums.materials.FluidShapes] and `gregtech.loaders.preload.LoaderGTBlockFluid` register fluids
/// under exactly these names, and saved fluid stacks resolve against them.
@Desugar
public record FluidNames(FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {}
