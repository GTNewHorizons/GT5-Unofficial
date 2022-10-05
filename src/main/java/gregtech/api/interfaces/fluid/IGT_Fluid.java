package gregtech.api.interfaces.fluid;

import net.minecraftforge.fluids.FluidRegistry;

@SuppressWarnings("unused") // API might legitimately expose unused methods within this local project's scope
public interface IGT_Fluid {

    /**
     * Adds this {@link IGT_Fluid} to the {@link FluidRegistry} and internally-implemented registrations
     *
     * @return {@link IGT_RegisteredFluid} The GregTech registered fluid
     */
    IGT_RegisteredFluid addFluid();
}
