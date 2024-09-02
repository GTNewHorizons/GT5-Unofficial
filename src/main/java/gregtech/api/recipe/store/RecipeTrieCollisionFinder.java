package gregtech.api.recipe.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class RecipeTrieCollisionFinder {

    private static final Logger LOGGER = LogManager.getLogger("GregTech Recipe Checker");

    private RecipeTrieCollisionFinder() {}

    /**
     * Finds collisions in each RecipeMap's RecipeTrie
     */
    public static void findCollisions() {
        Object2ObjectMap<RecipeMap<?>, Object2ObjectMap<GT_Recipe, Set<GT_Recipe>>> mismatchedRecipes = new Object2ObjectOpenHashMap<>();
        Object2ObjectMap<RecipeMap<?>, Set<GT_Recipe>> emptyInputRecipes = new Object2ObjectOpenHashMap<>();

        LOGGER.info("Starting recipe issue checks...");
        for (var recipeMap : RecipeMap.ALL_RECIPE_MAPS.values()) {
            mismatchedRecipes.put(recipeMap, new Object2ObjectOpenHashMap<>());
            emptyInputRecipes.put(recipeMap, new ObjectOpenHashSet<>());
            LOGGER.info("Checking Recipe Map: {}", recipeMap.unlocalizedName);
            for (GT_Recipe currentRecipe : recipeMap.getAllRecipes()) {
                List<ItemStack> itemInputs = new ArrayList<>();
                for (ItemStack input : currentRecipe.mInputs) {
                    if (input == null) {
                        // check for any empty/null inputs
                        emptyInputRecipes.get(recipeMap)
                            .add(currentRecipe);
                    } else {
                        // set count to Integer.MAX_VALUE to detect conflicts which only occur in large batches
                        ItemStack stack = input.copy();
                        stack.stackSize = Integer.MAX_VALUE;
                        itemInputs.add(stack);
                    }
                }

                List<FluidStack> fluidInputs = new ArrayList<>();
                for (FluidStack input : currentRecipe.mFluidInputs) {
                    if (input == null) {
                        // check for any empty/null inputs
                        emptyInputRecipes.get(recipeMap)
                            .add(currentRecipe);
                    } else {
                        // set count to Integer.MAX_VALUE to detect conflicts which only occur in large batches
                        fluidInputs.add(new FluidStack(input, Integer.MAX_VALUE));
                    }
                }

                Set<GT_Recipe> collidingRecipes = recipeMap.getBackend()
                    .trie()
                    .findAll(itemInputs.toArray(new ItemStack[0]), fluidInputs.toArray(new FluidStack[0]));

                if (collidingRecipes.isEmpty()) {
                    LOGGER.error("Recipe had no matches for findRecipeCollisions: {}", currentRecipe);
                    continue;
                }

                if (collidingRecipes.size() > 1) {
                    // remove the current recipe from the list of recipes, as it's not a conflict
                    collidingRecipes.remove(currentRecipe);
                    var conflicting = mismatchedRecipes.get(recipeMap);

                    // if the conflict was iterated over before, and the current recipe is in the list, remove it
                    collidingRecipes.removeIf(
                        cf -> conflicting.get(cf) != null && conflicting.get(cf)
                            .contains(currentRecipe));

                    if (!collidingRecipes.isEmpty()) {
                        mismatchedRecipes.get(recipeMap)
                            .put(currentRecipe, collidingRecipes);
                    }
                }
            }

            if (mismatchedRecipes.get(recipeMap)
                .isEmpty()) {
                LOGGER.info("No mismatched recipes found for recipe map: {}", recipeMap.unlocalizedName);
                mismatchedRecipes.remove(recipeMap);
            } else {
                LOGGER.error("Mismatched recipes found for recipe map: {}", recipeMap.unlocalizedName);
            }

            if (emptyInputRecipes.get(recipeMap)
                .isEmpty()) {
                emptyInputRecipes.remove(recipeMap);
            } else {
                LOGGER.error("Recipes with empty inputs found in recipe map: {}", recipeMap.unlocalizedName);
            }
        }

        LOGGER.info("Completed recipe issue checks!");

        int count = 0;
        if (mismatchedRecipes.isEmpty()) {
            LOGGER.info("No recipe conflicts found in all recipe maps!");
        } else {
            count = (int) mismatchedRecipes.values()
                .stream()
                .mapToLong(
                    s -> s.values()
                        .stream()
                        .mapToLong(Set::size)
                        .sum())
                .sum();
            LOGGER.info("Found {} potential conflicts...", count);
            for (var entry : mismatchedRecipes.entrySet()) {
                LOGGER.error("\n[In Recipe map] : \"{}\"", entry.getKey().unlocalizedName);
                for (var mismatch : mismatchedRecipes.get(entry.getKey())
                    .entrySet()) {
                    StringBuilder conflictingRecipes = new StringBuilder();
                    conflictingRecipes.append("\n[Tried matching]: ")
                        .append(mismatch.getKey());
                    for (var additional : mismatch.getValue()) {
                        conflictingRecipes.append("\n[Also Found]: ")
                            .append(additional);
                    }

                    LOGGER.error(conflictingRecipes.toString());
                }
            }
        }

        int emptyCount = 0;
        if (!emptyInputRecipes.isEmpty()) {
            emptyCount = (int) emptyInputRecipes.values()
                .stream()
                .mapToLong(Set::size)
                .sum();
            LOGGER.info("Found {} recipes with empty inputs", emptyCount);
            for (var recipeMap : emptyInputRecipes.entrySet()) {
                LOGGER.error("\n[In Recipe map] : \"{}\"", recipeMap.getKey().unlocalizedName);
                for (var recipe : recipeMap.getValue()) {
                    LOGGER.error("\n{}", recipe);
                }
            }
        }

        LOGGER.info("Found {} conflicts", count);
        LOGGER.info("Found {} empty inputs", emptyCount);
    }
}
