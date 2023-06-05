package gregtech.api.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.fluid.IGT_Fluid;
import gregtech.api.interfaces.fluid.IGT_FluidBuilder;
import gregtech.common.fluid.GT_FluidBuilder;

/**
 * <p>
 * This class contains helpers factory methods to:
 * </p>
 * <ol>
 * <li>
 * <p>
 * Build {@link IGT_Fluid} instances.
 * </p>
 * </li>
 * <li>
 * <p>
 * Register the corresponding {@link Fluid}, built from an {@link IGT_Fluid}, to the {@link FluidRegistry}:
 * </p>
 * <ul>
 * <li>
 * <p>
 * Register the optionally associated containers to the {@link FluidContainerRegistry}.
 * </p>
 * </li>
 * <li>
 * <p>
 * Add the needed Fluid Canner recipes.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ol>
 */
@SuppressWarnings("unused") // API might legitimately expose unused methods within this local project's scope
public class GT_FluidFactory {

    /**
     * Helper for quick fluid creation and registration
     *
     * @param fluidName     The name key of the {@link Fluid} to register in the {@link FluidRegistry}
     * @param localizedName The localized name of this {@link IGT_Fluid}
     * @param material      The {@link Materials} of this {@link IGT_Fluid}
     * @param state         The {@link FluidState} of this {@link IGT_Fluid}
     * @param temperature   The fluid temperature in Kelvin
     * @return the registered {@link Fluid}
     */
    public static Fluid of(final String fluidName, final String localizedName, final Materials material,
        final FluidState state, final int temperature) {
        return builder(fluidName).withLocalizedName(localizedName)
            .withStateAndTemperature(state, temperature)
            .buildAndRegister()
            .configureMaterials(material)
            .asFluid();
    }

    /**
     * Helper for quick fluid creation and registration
     *
     * @param fluidName     The name key of the {@link Fluid} to register in the {@link FluidRegistry}
     * @param localizedName The localized name of this {@link IGT_Fluid}
     * @param state         The {@link FluidState} of this {@link IGT_Fluid}
     * @param temperature   The fluid temperature in Kelvin
     * @return the registered {@link Fluid}
     */
    public static Fluid of(final String fluidName, final String localizedName, final FluidState state,
        final int temperature) {
        return builder(fluidName).withLocalizedName(localizedName)
            .withStateAndTemperature(state, temperature)
            .buildAndRegister()
            .asFluid();
    }

    /**
     * Gets an {@link IGT_Fluid} builder instance
     *
     * @param fluidName The name key of the {@link Fluid} to register in the {@link FluidRegistry}
     * @return the {@link IGT_FluidBuilder} instance
     */
    public static IGT_FluidBuilder builder(final String fluidName) {
        return new GT_FluidBuilder(fluidName);
    }
}
