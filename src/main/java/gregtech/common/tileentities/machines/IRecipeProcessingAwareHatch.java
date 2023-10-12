package gregtech.common.tileentities.machines;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public interface IRecipeProcessingAwareHatch {

    /**
     * Called when multiblock controller starts processing.
     * {@link #endRecipeProcessing(GT_MetaTileEntity_MultiBlockBase)} is called on the same tick.
     */
    default void startRecipeProcessing() {}

    /**
     * Called when multiblock controller ends processing. {@link #startRecipeProcessing()} is called on the same tick.
     *
     * @param controller Caller of this method.
     * @return Result of the process of this method. {@code !wasSuccessful()} means the returned result should
     *         overwrite the result calculated on multiblock whatever the reason is.
     */
    default CheckRecipeResult endRecipeProcessing(GT_MetaTileEntity_MultiBlockBase controller) {
        endRecipeProcessing();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Simple version of {@link #endRecipeProcessing(GT_MetaTileEntity_MultiBlockBase)}. Maybe use it instead.
     */
    default void endRecipeProcessing() {}
}
