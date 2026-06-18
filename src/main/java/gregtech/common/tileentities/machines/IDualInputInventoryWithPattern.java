package gregtech.common.tileentities.machines;

import gregtech.api.objects.GTDualInputPattern;

public interface IDualInputInventoryWithPattern extends IDualInputInventory {

    /**
     * The items and fluids that will be provided in the pattern. They are only used to search the possible recipes, and
     * won't be consumed.
     * <p>
     * The actual items and fluids to be consumed is returned in {@link #getItemInputs()} and {@link #getFluidInputs()}.
     *
     * @return the items and fluids that will be provided in the pattern.
     */
    GTDualInputPattern getPatternInputs();

    default boolean shouldBeCached() {
        return true;
    }
}
