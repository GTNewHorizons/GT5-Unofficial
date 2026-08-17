package gregtech.api.material;

import com.github.bsideup.jabel.Desugar;

/// The Forge fluid a material declares for each state; a null slot means the material has no fluid in that
/// state. [gregtech.api.enums.materials.FluidShapes] and `gregtech.loaders.preload.LoaderGTBlockFluid`
/// register fluids under exactly these names, which world NBT resolves fluid stacks against.
@Desugar
public record FluidNames(FluidRef fluid, FluidRef gas, FluidRef plasma, FluidRef molten) {}
