package gregtech.api.recipe.lookup;

import java.util.Objects;
import java.util.function.Consumer;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public final class GTFluidLookupIngredient extends GTRecipeLookupIngredient {

    private final Fluid fluid;

    public GTFluidLookupIngredient(FluidStack stack) {
        this(
            Objects.requireNonNull(stack, "stack")
                .getFluid());
    }

    public GTFluidLookupIngredient(Fluid fluid) {
        super(System.identityHashCode(fluid));
        this.fluid = Objects.requireNonNull(fluid, "fluid");
    }

    public static void fromRuntime(Consumer<? super GTFluidLookupIngredient> ingredients, FluidStack stack) {
        ingredients.accept(new GTFluidLookupIngredient(stack));
    }

    public Fluid getFluid() {
        return fluid;
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        return fluid == ((GTFluidLookupIngredient) other).fluid;
    }
}
