package gregtech.api.interfaces.fluid;

import net.minecraftforge.fluids.FluidRegistry;

@SuppressWarnings("unused") // API might legitimately expose unused methods within this local project's scope
public interface IGTFluid {

    /**
     * Adds this {@link IGTFluid} to the {@link FluidRegistry} and internally-implemented registrations
     *
     * @return {@link IGTRegisteredFluid} The GregTech registered fluid
     */
    IGTRegisteredFluid addFluid();
}
