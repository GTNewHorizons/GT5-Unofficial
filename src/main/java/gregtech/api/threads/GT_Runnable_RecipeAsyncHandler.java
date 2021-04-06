package gregtech.api.threads;

import gregtech.GT_Mod;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_Recipe;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static gregtech.api.enums.GT_Values.D2;


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

    public static Future<?> registerRecipes(Collection<GT_Proxy.OreDictEventContainer> mEvents){
        return GT_Threads.getExecutorServiceMap()
                .get(GT_Runnable_RecipeAsyncHandler.class)
                .submit(() -> {
                    System.out.println("Async Processing Started!");
                    int size = 5;
                    int sizeStep = mEvents.size() / 20 - 1;
                    int max = mEvents.size();
//                    int container = 0;
                    CountDownLatch latch = new CountDownLatch(max);
                    if (D2) {
                        System.out.println("Initial latch @ " + latch.getCount());
                    }
                    for (GT_Proxy.OreDictEventContainer tEvent : mEvents) {
                        sizeStep--;
//                        if (D2) {
//                            container++;
//                            System.out.println("OreDictEventContainer: " + container + " of " + max);
//                        }
                        if (sizeStep == 0) {
                            System.out.println("Async-Baking : " + size + "%");
                            sizeStep = mEvents.size() / 20 - 1;
                            size += 5;
                        }
                        GT_Threads.getExecutorServiceMap()
                                .get(GT_Runnable_RecipeAsyncHandler.class)
                                .submit(() -> {
                                    if (D2) {
                                        System.out.println("Start " + tEvent.mEvent.Name);
                                    }

                                    GT_Proxy.registerRecipesAsync(tEvent);
                                    latch.countDown();

                                    if (D2) {
                                        //TODO: Research why this stops @ 1146, 1147 or 1148
                                        System.out.println("Latch @ :" + latch.getCount());
                                        System.out.println(tEvent.mEvent.Name+" done!");
                                    }
                                });
                    }

                    System.out.println("Await Async Processing!");
                    try {
                        latch.await();
                        getLatch().countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Async Processing done!");
                });
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
