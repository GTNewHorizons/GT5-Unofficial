package gregtech.api.logic;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.recipe.check.RecipeValidator;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

/**
 * Processing logic class, dedicated for MetaTileEntities.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingLogic extends AbstractProcessingLogic<ProcessingLogic> {

    protected IRecipeLockable recipeLockableMachine;
    protected ItemStack specialSlotItem;
    protected ItemStack[] inputItems;
    protected FluidStack[] inputFluids;
    protected boolean isRecipeLocked;

    public ProcessingLogic() {}

    // #region Setters

    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return this;
    }

    public ProcessingLogic setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return this;
    }

    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return this;
    }

    public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return this;
    }

    public ProcessingLogic setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return this;
    }

    /**
     * Enables single recipe locking mode.
     */
    public ProcessingLogic setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
        this.recipeLockableMachine = recipeLockableMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    @Override
    public ProcessingLogic clear() {
        super.clear();
        this.inputItems = null;
        this.inputFluids = null;
        this.specialSlotItem = null;
        this.outputItems = null;
        this.outputFluids = null;
        return this;
    }

    // #endregion

    // #region Logic

    @Nonnull
    @Override
    public CheckRecipeResult process() {
        GT_Recipe_Map recipeMap = preProcess();

        if (isRecipeLocked && recipeLockableMachine != null && recipeLockableMachine.getSingleRecipeCheck() != null) {
            // Recipe checker is already built, we'll use it
            SingleRecipeCheck singleRecipeCheck = recipeLockableMachine.getSingleRecipeCheck();
            // Validate recipe here, otherwise machine will show "not enough output space"
            // even if recipe cannot be found
            if (singleRecipeCheck.checkRecipeInputs(false, 1, inputItems, inputFluids) == 0) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            return processRecipe(
                recipeLockableMachine.getSingleRecipeCheck()
                    .getRecipe());
        }

        FindRecipeResult findRecipeResult = findRecipe(recipeMap);

        CheckRecipeResult recipeValidatorResult = processRecipeValidator(findRecipeResult);
        if (recipeValidatorResult != null) {
            return recipeValidatorResult;
        }

        if (!findRecipeResult.isSuccessful()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        return processRecipe(findRecipeResult.getRecipeNonNull());
    }

    /**
     * Checks if supplied recipe is valid for process.
     * If so, additionally performs input consumption, output calculation with parallel, and overclock calculation.
     *
     * @param recipe The recipe which will be checked and processed
     */
    @Nonnull
    protected CheckRecipeResult processRecipe(@Nonnull GT_Recipe recipe) {
        CheckRecipeResult result = validateRecipe(recipe);
        if (!result.wasSuccessful()) {
            return result;
        }

        GT_ParallelHelper helper = createParallelHelper(recipe);
        GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
        helper.setCalculator(calculator);
        helper.build();

        return applyRecipe(recipe, helper, calculator, result);
    }

    /**
     * Override if you don't work with regular gt recipe maps
     */
    @Nonnull
    protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
        if (map == null) return FindRecipeResult.NOT_FOUND;

        RecipeValidator recipeValidator = new RecipeValidator(
            this::validateRecipe,
            this::createParallelHelper,
            this::createOverclockCalculator);

        FindRecipeResult findRecipeResult = map.findRecipeWithResult(
            lastRecipe,
            recipeValidator,
            false,
            false,
            amperageOC ? availableVoltage * availableAmperage : availableVoltage,
            inputFluids,
            specialSlotItem,
            inputItems);

        findRecipeResult.setRecipeValidator(recipeValidator);

        return findRecipeResult;
    }

    /**
     * Override to tweak parallel logic if needed.
     */
    @Nonnull
    protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMachine(machine, protectItems, protectFluids)
            .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
            .setMaxParallel(maxParallel)
            .setEUtModifier(euModifier)
            .enableBatchMode(batchSize)
            .setConsumption(true)
            .setOutputCalculation(true);
    }

    // #endregion
}
