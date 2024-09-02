package gregtech.common.tileentities.machines;

import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;

/**
 * Implement this interface for {@link MTEHatch}
 * if it does special stuff while multiblock controller is processing recipe.
 */
public interface IRecipeProcessingAwareHatch {

    /**
     * Called when multiblock controller starts processing.
     * {@link #endRecipeProcessing} is called on the same tick.
     */
    void startRecipeProcessing();

    /**
     * Called when multiblock controller ends processing. {@link #startRecipeProcessing} is called on the same tick.
     *
     * @param controller Caller of this method.
     * @return Result of the process of this method. {@code !wasSuccessful()} means the returned result should
     *         overwrite the result calculated on multiblock whatever the reason is.
     */
    CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller);
}
