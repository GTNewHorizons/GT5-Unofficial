package gregtech.api.recipe.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GTRecipeLookupBuilder {

    private final List<RecipeEntry> entries = new ArrayList<>();

    public GTRecipeLookupBuilder add(GTRecipe recipe) {
        Objects.requireNonNull(recipe, "recipe");
        if (!recipe.mFakeRecipe) {
            List<List<GTRecipeLookupIngredient>> ingredients = flatten(recipe);
            if (!ingredients.isEmpty()) {
                entries.add(new RecipeEntry(recipe, ingredients));
            }
        }
        return this;
    }

    public GTRecipeLookup build() {
        GTRecipeLookup lookup = buildMutable();
        lookup.freeze();
        return lookup;
    }

    public GTRecipeLookup buildMutable() {
        Object2IntMap<GTRecipeLookupIngredient> frequencies = computeFrequencies();
        Map<GTRecipeLookupIngredient, GTRecipeLookupIngredient> pool = new HashMap<>();
        GTRecipeLookup lookup = new GTRecipeLookup();

        for (RecipeEntry entry : entries) {
            List<List<GTRecipeLookupIngredient>> pooled = poolAndSort(entry.ingredients, frequencies, pool);
            lookup.add(entry.recipe, pooled);
        }

        return lookup;
    }

    public static void addToLookup(GTRecipeLookup lookup, GTRecipe recipe) {
        Objects.requireNonNull(lookup, "lookup");
        Objects.requireNonNull(recipe, "recipe");
        if (recipe.mFakeRecipe) {
            return;
        }

        List<List<GTRecipeLookupIngredient>> ingredients = flatten(recipe);
        if (ingredients.isEmpty()) {
            return;
        }
        lookup.add(recipe, ingredients);
    }

    private Object2IntMap<GTRecipeLookupIngredient> computeFrequencies() {
        Object2IntMap<GTRecipeLookupIngredient> frequencies = new Object2IntOpenHashMap<>();
        for (RecipeEntry entry : entries) {
            for (List<GTRecipeLookupIngredient> group : entry.ingredients) {
                for (GTRecipeLookupIngredient ingredient : group) {
                    frequencies.put(ingredient, frequencies.getInt(ingredient) + 1);
                }
            }
        }
        return frequencies;
    }

    private static List<List<GTRecipeLookupIngredient>> poolAndSort(List<List<GTRecipeLookupIngredient>> ingredients,
        Object2IntMap<GTRecipeLookupIngredient> frequencies,
        Map<GTRecipeLookupIngredient, GTRecipeLookupIngredient> pool) {
        List<IngredientGroup> groups = new ArrayList<>(ingredients.size());
        for (int i = 0; i < ingredients.size(); i++) {
            List<GTRecipeLookupIngredient> pooledGroup = new ArrayList<>(
                ingredients.get(i)
                    .size());
            for (GTRecipeLookupIngredient ingredient : ingredients.get(i)) {
                GTRecipeLookupIngredient pooled = pool.get(ingredient);
                if (pooled == null) {
                    pooled = ingredient;
                    pool.put(ingredient, ingredient);
                }
                addIfAbsent(pooledGroup, pooled);
            }
            groups.add(new IngredientGroup(pooledGroup, maxFrequency(pooledGroup, frequencies), i));
        }

        groups.sort((first, second) -> {
            int frequencyCompare = Integer.compare(first.maxFrequency, second.maxFrequency);
            if (frequencyCompare != 0) return frequencyCompare;
            return Integer.compare(first.originalIndex, second.originalIndex);
        });

        List<List<GTRecipeLookupIngredient>> sorted = new ArrayList<>(groups.size());
        for (IngredientGroup group : groups) {
            sorted.add(group.ingredients);
        }
        return sorted;
    }

    private static int maxFrequency(List<GTRecipeLookupIngredient> group,
        Object2IntMap<GTRecipeLookupIngredient> frequencies) {
        int result = 0;
        for (GTRecipeLookupIngredient ingredient : group) {
            result = Math.max(result, frequencies.getInt(ingredient));
        }
        return result;
    }

    private static List<List<GTRecipeLookupIngredient>> flatten(GTRecipe recipe) {
        List<List<GTRecipeLookupIngredient>> ingredients = new ArrayList<>();
        flattenItemInputs(recipe, ingredients);
        flattenFluidInputs(recipe, ingredients);
        return ingredients;
    }

    public static List<List<GTRecipeLookupIngredient>> flattenForValidation(GTRecipe recipe) {
        return flatten(recipe);
    }

    private static void flattenItemInputs(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
        ItemStack[] inputs = recipe.mInputs;
        if (inputs == null) {
            return;
        }

        GTRecipe.GTRecipe_WithAlt recipeWithAlt = recipe instanceof GTRecipe.GTRecipe_WithAlt
            ? (GTRecipe.GTRecipe_WithAlt) recipe
            : null;

        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null) {
                continue;
            }

            List<GTRecipeLookupIngredient> group = new ArrayList<>(1);
            int oreDictId = oreDictIdFor(recipeWithAlt, i);
            if (oreDictId >= 0) {
                group.add(new GTOreDictLookupIngredient(oreDictId));
                addItemAlternativesMissingOreDictId(recipe, recipeWithAlt, i, oreDictId, group);
                addRepresentativeItemIngredient(recipe, i, group);
            } else {
                addItemAlternatives(recipe, recipeWithAlt, i, group);
                addRepresentativeItemIngredient(recipe, i, group);
            }
            if (!group.isEmpty()) {
                ingredients.add(group);
            }
        }
    }

    private static int oreDictIdFor(GTRecipe.GTRecipe_WithAlt recipeWithAlt, int index) {
        if (recipeWithAlt == null || recipeWithAlt.mOreDictIds == null || index >= recipeWithAlt.mOreDictIds.length) {
            return -1;
        }
        return recipeWithAlt.mOreDictIds[index];
    }

    private static void addItemAlternatives(GTRecipe recipe, GTRecipe.GTRecipe_WithAlt recipeWithAlt, int index,
        List<GTRecipeLookupIngredient> group) {
        if (recipeWithAlt != null && recipeWithAlt.mOreDictAlt != null && index < recipeWithAlt.mOreDictAlt.length) {
            ItemStack[] alternatives = recipeWithAlt.mOreDictAlt[index];
            if (alternatives != null && alternatives.length > 0) {
                for (ItemStack alternative : alternatives) {
                    addItemIngredient(recipe, alternative, group);
                }
                return;
            }
        }

        if (recipe.mInputs != null && index < recipe.mInputs.length) {
            addItemIngredient(recipe, recipe.mInputs[index], group);
        }
    }

    private static void addItemAlternativesMissingOreDictId(GTRecipe recipe, GTRecipe.GTRecipe_WithAlt recipeWithAlt,
        int index, int oreDictId, List<GTRecipeLookupIngredient> group) {
        if (recipeWithAlt != null && recipeWithAlt.mOreDictAlt != null && index < recipeWithAlt.mOreDictAlt.length) {
            ItemStack[] alternatives = recipeWithAlt.mOreDictAlt[index];
            if (alternatives != null && alternatives.length > 0) {
                for (ItemStack alternative : alternatives) {
                    addItemIngredientIfMissingOreDictId(recipe, alternative, oreDictId, group);
                }
                return;
            }
        }

        if (recipe.mInputs != null && index < recipe.mInputs.length) {
            addItemIngredientIfMissingOreDictId(recipe, recipe.mInputs[index], oreDictId, group);
        }
    }

    private static void addRepresentativeItemIngredient(GTRecipe recipe, int index,
        List<GTRecipeLookupIngredient> group) {
        if (recipe.mInputs != null && index < recipe.mInputs.length) {
            addItemIngredient(recipe, recipe.mInputs[index], group);
        }
    }

    private static void addItemIngredientIfMissingOreDictId(GTRecipe recipe, ItemStack stack, int oreDictId,
        List<GTRecipeLookupIngredient> group) {
        if (stack == null || hasOreDictId(stack, oreDictId)) {
            return;
        }
        addItemIngredient(recipe, stack, group);
    }

    private static boolean hasOreDictId(ItemStack stack, int oreDictId) {
        try {
            for (int id : OreDictionary.getOreIDs(stack)) {
                if (id == oreDictId) {
                    return true;
                }
            }
        } catch (ExceptionInInitializerError | NoClassDefFoundError ignored) {
            return false;
        }
        return false;
    }

    private static void addItemIngredient(GTRecipe recipe, ItemStack stack, List<GTRecipeLookupIngredient> group) {
        if (stack == null) {
            return;
        }
        GTRecipeLookupIngredient ingredient = GTItemStackLookupIngredient.fromRecipe(stack, recipe.isNBTSensitive);
        addIfAbsent(group, ingredient);
    }

    private static void flattenFluidInputs(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
        FluidStack[] inputs = recipe.mFluidInputs;
        if (inputs == null) {
            return;
        }

        for (int i = 0; i < inputs.length; i++) {
            List<GTRecipeLookupIngredient> group = new ArrayList<>(1);
            addFluidIngredient(inputs[i], group);
            if (recipe.mAltFluidInputs != null && i < recipe.mAltFluidInputs.length) {
                FluidStack[] alternatives = recipe.mAltFluidInputs[i];
                if (alternatives != null) {
                    for (FluidStack alternative : alternatives) {
                        addFluidIngredient(alternative, group);
                    }
                }
            }
            if (!group.isEmpty()) {
                ingredients.add(group);
            }
        }
    }

    private static void addFluidIngredient(FluidStack stack, List<GTRecipeLookupIngredient> group) {
        if (stack == null || stack.getFluid() == null) {
            return;
        }
        addIfAbsent(group, new GTFluidLookupIngredient(stack));
    }

    private static void addIfAbsent(List<GTRecipeLookupIngredient> ingredients, GTRecipeLookupIngredient ingredient) {
        if (!ingredients.contains(ingredient)) {
            ingredients.add(ingredient);
        }
    }

    private static final class RecipeEntry {

        private final GTRecipe recipe;
        private final List<List<GTRecipeLookupIngredient>> ingredients;

        private RecipeEntry(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
            this.recipe = recipe;
            this.ingredients = ingredients;
        }
    }

    private static final class IngredientGroup {

        private final List<GTRecipeLookupIngredient> ingredients;
        private final int maxFrequency;
        private final int originalIndex;

        private IngredientGroup(List<GTRecipeLookupIngredient> ingredients, int maxFrequency, int originalIndex) {
            this.ingredients = ingredients;
            this.maxFrequency = maxFrequency;
            this.originalIndex = originalIndex;
        }
    }
}
