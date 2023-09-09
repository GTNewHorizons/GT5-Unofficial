package gregtech.common.tileentities.machines.multiblock.logic;

import gregtech.api.logic.ComplexParallelProcessingLogic;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class AdvChemicalProcessorProcessingLogic
    extends ComplexParallelProcessingLogic<AdvChemicalProcessorProcessingLogic> {

    public AdvChemicalProcessorProcessingLogic() {
        setRecipeMap(GT_Recipe_Map.sMultiblockChemicalRecipes);
    }
}
