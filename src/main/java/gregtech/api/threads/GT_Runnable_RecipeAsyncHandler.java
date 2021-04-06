package gregtech.api.threads;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_Recipe;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.concurrent.CountDownLatch;


public class GT_Runnable_RecipeAsyncHandler implements Runnable {



    private GT_MetaTileEntity_BasicMachine aTileEntity;
    private ItemStack aSpecialSlot;
    private ItemStack[] itemStacks;
    private FluidStack[] fluidStacks;
    private long voltage;
    private GT_Recipe lastRecipe;
    private GT_Recipe.GT_Recipe_Map gt_recipe_map;
    private boolean aNotUnificated;

    private GT_Runnable_RecipeAsyncHandler(GT_MetaTileEntity_BasicMachine aTileEntity,
                                           GT_Recipe aRecipe,
                                           boolean aNotUnificated,
                                           long aVoltage,
                                           FluidStack[] aFluids,
                                           ItemStack aSpecialSlot,
                                           GT_Recipe.GT_Recipe_Map gt_recipe_map,
                                           ItemStack[] aInputs

    ) {
        this.aTileEntity = aTileEntity;
        this.aSpecialSlot = aSpecialSlot;
        this.itemStacks = aInputs;
        this.fluidStacks = aFluids;
        this.voltage = aVoltage;
        this.lastRecipe = aRecipe;
        this.gt_recipe_map = gt_recipe_map;
        this.aNotUnificated = aNotUnificated;
        aTileEntity.getRecipeStatus().set(RECIPE_REQUESTED);
    }

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static CountDownLatch getLatch() {
        return latch;
    }

    public static void registerRecipes(Collection<GT_Proxy.OreDictEventContainer> mEvents) {
        GT_Threads.getExecutorServiceMap()
                .get(GT_Runnable_RecipeAdder.class)
                .submit(new GT_Runnable_RecipeAdder(mEvents));
    }

    public static void requestRecipe(GT_MetaTileEntity_BasicMachine aTileEntity,
                                     GT_Recipe aRecipe,
                                     boolean aNotUnificated,
                                     long aVoltage,
                                     FluidStack[] aFluids,
                                     ItemStack aSpecialSlot,
                                     GT_Recipe.GT_Recipe_Map gt_recipe_map,
                                     ItemStack... aInputs) {
        GT_Threads.getExecutorServiceMap().get(GT_Runnable_RecipeAsyncHandler.class)
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
    }

    public static final int
            RECIPE_NOT_REQUESTED = 0,
            RECIPE_REQUESTED = 1,
            RECIPE_RETURNED_NULL = 2,
            RECIPE_RETURNED = 3;

    @Override
    public void run() {
        for(;;) {
            try {
                GT_Recipe recipe = gt_recipe_map.findRecipe(
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
