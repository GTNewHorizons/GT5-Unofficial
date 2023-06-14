package gregtech.common.tileentities.machines.multiblock.logic;

import gregtech.GT_Mod;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GenericProcessingLogic extends ProcessingLogic {

    public GenericProcessingLogic(GT_Recipe_Map recipeMap) {
        super();
        setRecipeMap(recipeMap);
    }

    @Override
    public boolean process() {
        if (recipeMap == null) {
            return false;
        }

        GT_Recipe recipe = recipeMap.findRecipe(controller, false, false, voltage, inputFluids, inputItems);

        if (recipe == null) {
            return false;
        }

        if (recipe.mSpecialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom && !isCleanroom) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(voltage * ampere)
            .setMaxParallel(maxParallel)
            .enableConsumption()
            .enableOutputCalculation()
            .setMachine(controller, voidProtection, voidProtection)
            .build();

        if (helper.getCurrentParallel() <= 0) {
            return false;
        }

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setDuration(recipe.mDuration)
            .setEUt(voltage)
            .setParallel(helper.getCurrentParallel())
            .setAmperage(ampere);

        if (perfectOverclock) {
            calculator.enablePerfectOC();
        }

        if (calculator.getConsumption() == Long.MAX_VALUE - 1 || calculator.getDuration() == Integer.MAX_VALUE - 1) {
            return false;
        }

        duration = calculator.getDuration();
        eut = calculator.getConsumption();
        outputItems = helper.getItemOutputs();
        outputFluids = helper.getFluidOutputs();

        return true;
    }

}
