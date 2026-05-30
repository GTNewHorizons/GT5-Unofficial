package gregtech.api.objects;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;

public class SubstituteFluidStack {

    public static final Materials[] solderingMats = new Materials[] { Materials.SolderingAlloy, Materials.Tin,
        Materials.Lead };
    public final List<FluidStack> fluidStacks;

    /**
     * Helper class for recipes that can accept multiple fluids as substitutes that retain individual fluid amounts!
     * This is useful for recipes that can use different fluids with similar properties, such as soldering alloys of
     * different qualities.
     *
     * <pre>
     * {@code new SubstituteFluidStack(
     *          // Each fluid has its own amount
     *          Materials.Lava.get(1_000),
     *          Materials.Water.get(500)
     *   )
     *   }
     * </pre>
     *
     * @param fluidStacks list of fluids to use as substitutes. Nulls or stacks with null fluid will be ignored.
     */
    public SubstituteFluidStack(FluidStack... fluidStacks) {
        if (fluidStacks == null) {
            this.fluidStacks = Collections.emptyList();
            return;
        }

        List<FluidStack> list = new ArrayList<>();
        for (FluidStack fs : fluidStacks) {
            if (fs != null && fs.getFluid() != null) {
                list.add(fs.copy());
            }
        }
        this.fluidStacks = Collections.unmodifiableList(list);
    }

    /**
     * Soldering helper method that generates a list of fluids that can be used in soldering recipes.
     * The amount of each fluid is controlled by baseAmount, and the multiplier is determined by soldering material
     * quality.
     *
     * @param baseAmount Lowest fluid amount.
     */
    public static SubstituteFluidStack soldering(long baseAmount) {

        List<FluidStack> fluids = new ArrayList<>();

        for (Materials material : solderingMats) {
            int multiplier = material.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : material.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            fluids.add(material.getMolten(baseAmount * multiplier));
        }

        return new SubstituteFluidStack(fluids.toArray(new FluidStack[0]));
    }

    public static SubstituteFluidStack soldering() {
        return soldering(1 * INGOTS);
    }
}
