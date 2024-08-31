package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

public final class MapFluidStackIngredient extends AbstractMapIngredient {

    public static final Comparator<Fluid> FLUID_COMPARATOR = Comparator.comparingInt(FluidRegistry::getFluidID);
    public static final Comparator<MapFluidStackIngredient> COMPARATOR = (a, b) -> FLUID_COMPARATOR
        .compare(a.fluid, b.fluid);

    private final Fluid fluid;

    public MapFluidStackIngredient(@NotNull Fluid fluid) {
        this.fluid = fluid;
    }

    public MapFluidStackIngredient(@NotNull FluidStack stack) {
        this(stack.getFluid());
    }

    @Override
    protected int hash() {
        return fluid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapFluidStackIngredient that)) return false;

        return fluid.equals(that.fluid);
    }

    @Override
    public String toString() {
        return "MapFluidStackIngredient{" + FluidRegistry.getFluidName(fluid) + '}';
    }
}
