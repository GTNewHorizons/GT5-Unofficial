package gregtech.common.tileentities.machines.multiblock.logic;

import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.recipe.RecipeMaps;

public class AdvChemicalProcessorProcessingLogic
    extends ComplexParallelProcessingLogic<AdvChemicalProcessorProcessingLogic> {

    public AdvChemicalProcessorProcessingLogic() {
        setRecipeMap(RecipeMaps.multiblockChemicalReactorRecipes);
    }
}
