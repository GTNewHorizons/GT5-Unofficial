package gregtech.common.tileentities.machines;

import java.util.Iterator;

import gregtech.api.logic.ProcessingLogic;

public interface IDualInputHatchWithPattern extends IDualInputHatch {

    @Override
    Iterator<? extends IDualInputInventoryWithPattern> inventories();

    /**
     * Make the hatch aware of the related {@link ProcessingLogic}, to clear the recipe caches when the pattern is
     * changed.
     * <p>
     * Example: MTEHatchCraftingInputME#onPatternChange.
     *
     * @param pl the processing logic
     */
    void setProcessingLogic(ProcessingLogic pl);

    default void resetCraftingInputRecipeMap() {}

    default void resetCraftingInputRecipeMap(ProcessingLogic pl) {}

}
