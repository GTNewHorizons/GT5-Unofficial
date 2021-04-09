package gregtech.api.threads;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_Recipe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GT_Runnable_RecipeAsyncHandler implements Runnable {

    private final GT_MetaTileEntity_BasicMachine aTileEntity;
    private final GT_Recipe lastRecipe;
    private final boolean aNotUnificated;
    private final long voltage;
    private final FluidStack[] fluidStacks;
    private final ItemStack aSpecialSlot;
    private final GT_Recipe.GT_Recipe_Map gt_recipe_map;
    private final ItemStack[] itemStacks;

    public static void requestRecipe(GT_MetaTileEntity_BasicMachine aTileEntity,
                                     GT_Recipe aRecipe,
                                     boolean aNotUnificated,
                                     long aVoltage,
                                     FluidStack[] aFluids,
                                     ItemStack aSpecialSlot,
                                     GT_Recipe.GT_Recipe_Map gt_recipe_map,
                                     ItemStack... aInputs) {
        GT_Threads.getEXECUTOR_SERVICE_MAP().get(GT_Runnable_RecipeAsyncHandler.class)
                .submit(
                        new GT_Runnable_RecipeAsyncHandler(
                                aTileEntity,
                                aRecipe,
                                aNotUnificated,
                                aVoltage,
                                aFluids,
                                aSpecialSlot,
                                gt_recipe_map,
                                aInputs
                        )
                );
        aTileEntity.getRecipeStatus().set(RECIPE_REQUESTED);
    }

    public static final int
            RECIPE_NOT_REQUESTED = 0,
            RECIPE_REQUESTED = 1,
            RECIPE_RETURNED_NULL = 2,
            RECIPE_RETURNED = 3;

    @Override
    public void run() {
        GT_Recipe recipe;
        for(;;) {
            try {
                recipe = gt_recipe_map.findRecipe(
                        aTileEntity.getBaseMetaTileEntity(),
                        lastRecipe,
                        aNotUnificated,
                        voltage,
                        fluidStacks,
                        aSpecialSlot,
                        itemStacks
                );
                aTileEntity.getRecipeAtomicReference().set(recipe);
                aTileEntity.getRecipeStatus().set(recipe == null ? RECIPE_RETURNED_NULL : RECIPE_RETURNED);
                break;
            } catch (Exception ignored) {}
        }
    }
}
