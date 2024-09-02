package gregtech.api.recipe.store;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.store.ingredient.*;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.function.Either;
import gregtech.api.util.item.ItemHolder;
import gregtech.common.config.gregtech.ConfigGeneral;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class RecipeTrie {

    // debug
    private static final boolean CRASH_ON_EMPTY = false;

    private static final Map<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> ingredientCache = new WeakHashMap<>();

    final TrieBranch rootBranch = new TrieBranch();

    private boolean hasOreDictInputs;
    private boolean hasNBTMatchInputs;

    private int size;

    /**
     * @param recipe the recipe to add
     * @return if addition was successful
     */
    public boolean add(@NotNull GT_Recipe recipe) {
        var ingredients = createIngredients(recipe);
        if (ingredients == null) {
            if (ConfigGeneral.loggingRecipes) {
                GT_Log.recipe.printf("Recipe has no inputs: %s", recipe);
                if (ConfigGeneral.loggingRecipesStackTrace) {
                    new Throwable().printStackTrace(GT_Log.recipe);
                }
            }
            if (CRASH_ON_EMPTY) {
                throw new IllegalArgumentException("Cannot add recipe with no inputs");
            }
            return false;
        }

        if (add(recipe, ingredients)) {
            this.size++;
            return true;
        }
        return false;
    }

    /**
     * @param recipe      the recipe to add
     * @param ingredients the ingredients the recipe contains
     * @return if addition was successful
     */
    private boolean add(@NotNull GT_Recipe recipe, @NotNull AbstractMapIngredient @NotNull [] ingredients) {
        TrieBranch branch = rootBranch;
        for (int i = 0; i < ingredients.length; i++) {
            final boolean isLast = i == ingredients.length - 1;

            final AbstractMapIngredient ingredient = ingredients[i];
            var inserted = insertIntoBranch(recipe, branch, ingredient, isLast);

            if (inserted == null) {
                // some kind of conflict
                return false;
            }

            var addedRecipe = inserted.left();
            if (addedRecipe != null) {
                // successful if the added recipe was the one that is attempting to be added
                return addedRecipe == recipe;
            }

            var nextBranch = inserted.right();
            assert nextBranch != null;

            // tail recursion: add each ingredient on the right branch path, and insert the recipe on the left at the
            // end
            branch = nextBranch;
        }

        return false;
    }

    /**
     * @param recipe     the recipe to insert
     * @param branch     the current branch to insert into
     * @param ingredient the ingredient to associate with the branch
     * @param isLast     if this is the final branch in the route
     * @return the inserted element, or an existing one, or null if there is a conflict with many recipes
     */
    private static @Nullable Either<GT_Recipe, TrieBranch> insertIntoBranch(@NotNull GT_Recipe recipe,
        @NotNull TrieBranch branch, @NotNull AbstractMapIngredient ingredient, boolean isLast) {
        var nodes = branch.getNodes();
        var value = nodes.get(ingredient);
        if (isLast) {
            if (value == null) {
                // no recipe, so put one here
                value = Either.left(recipe);
                nodes.put(ingredient, value);
                return value;
            }

            GT_Recipe existing = value.left();
            if (existing != null) {
                if (ConfigGeneral.loggingRecipes) {
                    if (existing.equals(recipe)) {
                        GT_Log.recipe.printf("Duplicate recipe: %s%n", recipe);
                    } else {
                        GT_Log.recipe.printf("Conflicting recipes:%nExisting: %s%nConflict: %s%n", existing, recipe);
                    }

                    if (ConfigGeneral.loggingRecipesStackTrace) {
                        new Throwable().printStackTrace(GT_Log.recipe);
                    }
                }
                // return the existing recipes on conflicts so earlier recurses can use it
                return value;
            }

            // this recipe is a subset of one or more recipes, which is a conflict
            var existingEdge = value.right();
            assert existingEdge != null;
            if (ConfigGeneral.loggingRecipes) {
                StringBuilder builder = new StringBuilder("Conflicting recipes:%n1: ").append(recipe);
                int i = 2;
                for (GT_Recipe r : existingEdge.getAll()
                    .toArray(GT_Recipe[]::new)) {
                    builder.append("%n")
                        .append(i++)
                        .append(": ")
                        .append(r);
                }
                GT_Log.recipe.printf(builder.toString());
            }
            if (ConfigGeneral.loggingRecipesStackTrace) {
                new Throwable().printStackTrace(GT_Log.recipe);
            }

            return null;
        } else if (value == null) {
            // no existing ingredient is present, so use a new one
            value = Either.right(new TrieBranch());
            nodes.put(ingredient, value);
            return value;
        }

        // an existing ingredient is present, so use it
        return value;
    }

    /**
     * @param recipe the recipe
     * @return the trie ingredients for the recipe
     */
    private @NotNull AbstractMapIngredient @Nullable [] createIngredients(@NotNull GT_Recipe recipe) {
        int length = recipe.mInputs.length + recipe.mFluidInputs.length;
        if (length == 0) {
            return null;
        }

        ObjectArrayList<AbstractMapIngredient> list = new ObjectArrayList<>(length);
        if (recipe.mInputs.length > 0) {
            var unique = uniqueIngredients(recipe.mInputs);
            for (var input : unique) {
                list.add(deduplicateIngredient(ingredientCache, MapIngredientFactory.from(input)));

                // TODO partial nbt match inputs here
                // this.hasNBTMatchInputs = true;
            }
        }
        if (recipe instanceof GT_Recipe.GT_Recipe_WithAlt oreDictRecipe) {
            // TODO oredict goes here
            this.hasOreDictInputs = true;
        }
        if (recipe.mFluidInputs.length > 0) {
            var unique = uniqueIngredients(recipe.mFluidInputs);
            for (var input : unique) {
                list.add(deduplicateIngredient(ingredientCache, new MapFluidStackIngredient(input)));
            }
        }

        list.sort(MapIngredientComparator.INSTANCE);

        return list.toArray(new AbstractMapIngredient[0]);
    }

    /**
     * Deduplicates all item ingredients
     *
     * @param stacks the stacks to deduplicate
     * @return the deduplicated list of stacks
     */
    private static @NotNull List<@NotNull ItemStack> uniqueIngredients(@Nullable ItemStack @NotNull [] stacks) {
        List<ItemStack> list = new ObjectArrayList<>(stacks.length);
        for (var stack : stacks) {
            if (stack == null) {
                continue;
            }

            boolean isEqual = false;
            for (var seen : list) {
                if (seen.getItem() != stack.getItem()) {
                    continue;
                }
                if (seen.getItemDamage() != stack.getItemDamage()) {
                    continue;
                }
                if (Objects.equals(seen.getTagCompound(), stack.getTagCompound())) {
                    isEqual = true;
                    break;
                }
            }

            if (isEqual) {
                continue;
            }

            list.add(stack);
        }

        return list;
    }

    /**
     * Deduplicates all fluid ingredients
     *
     * @param stacks the stacks to deduplicate
     * @return the deduplicated list of fluids
     */
    private static @NotNull List<@NotNull Fluid> uniqueIngredients(@Nullable FluidStack @NotNull [] stacks) {
        List<Fluid> list = new ObjectArrayList<>(stacks.length);
        for (var stack : stacks) {
            if (stack == null) {
                continue;
            }

            Fluid fluid = stack.getFluid();
            boolean isEqual = false;
            for (var seen : list) {
                if (FluidRegistry.getFluidID(seen) == FluidRegistry.getFluidID(fluid)) {
                    isEqual = true;
                    break;
                }
            }
            if (isEqual) {
                continue;
            }

            list.add(fluid);
        }

        return list;
    }

    /**
     * Deduplicates ingredients using a cache
     *
     * @param cache      the cache to use
     * @param ingredient the ingredient to deduplicate
     * @return the deduplicated ingredient
     */
    private static @NotNull AbstractMapIngredient deduplicateIngredient(
        @NotNull Map<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> cache,
        @NotNull AbstractMapIngredient ingredient) {
        var cached = cache.get(ingredient);
        if (cached != null) {
            AbstractMapIngredient i = cached.get();
            if (i != null) {
                return i;
            }
        }

        cache.put(ingredient, new WeakReference<>(ingredient));
        return ingredient;
    }

    /**
     * @param recipe the recipe to remove
     * @return if removal was successful
     */
    public boolean remove(@NotNull GT_Recipe recipe) {
        var ingredients = createIngredients(recipe);
        if (ingredients == null) {
            throw new IllegalArgumentException("Cannot remove recipe with no inputs");
        }

        if (remove(recipe, ingredients, rootBranch, 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * @param recipe      the recipe to remove
     * @param ingredients the ingredients the recipe contains
     * @param branch      the branch to remove from
     * @param index       the ingredient index
     * @return the removed recipe
     */
    private static @Nullable GT_Recipe remove(@NotNull GT_Recipe recipe,
        @NotNull AbstractMapIngredient @NotNull [] ingredients, @NotNull TrieBranch branch, int index) {
        AbstractMapIngredient ingredient = ingredients[index];
        var nodes = branch.getNodes();

        var node = nodes.get(ingredient);
        if (node == null) {
            return null;
        }

        GT_Recipe found;
        var left = node.left();
        if (left != null) {
            // if a recipe is in this node, end immediately with it as found
            found = left;
        } else {
            var right = node.right();
            assert right != null;

            // recursion: remove the next ingredient up the trie
            found = remove(recipe, ingredients, right, index + 1);
        }

        if (found == recipe) {
            // disassociate this step in the route
            undoInsert(nodes, ingredient, index == ingredients.length - 1);
            return found;
        } else if (found != null) {
            if (ConfigGeneral.loggingRecipes) {
                GT_Log.recipe.printf("Failed to remove recipe %s%n, found: %s%n", recipe, found);
                if (ConfigGeneral.loggingRecipesStackTrace) {
                    new Throwable().printStackTrace(GT_Log.err);
                }
            }
            return null;
        }

        return null;
    }

    /**
     * Reverts an insertion operation into a branch
     *
     * @param nodes      the nodes to remove from
     * @param ingredient the added ingredient to undo
     * @param isLast     if the ingredient was the final ingredient in the route
     */
    private static void undoInsert(@NotNull Map<AbstractMapIngredient, Either<GT_Recipe, TrieBranch>> nodes,
        @NotNull AbstractMapIngredient ingredient, boolean isLast) {
        // undo the changes made
        if (isLast) {
            // last ingredient needs ingredient->recipe mapping removed
            nodes.remove(ingredient);
        } else {
            TrieBranch branch = nodes.get(ingredient)
                .right();
            if (branch != null && branch.isEmpty()) {
                // remove added empty branches
                nodes.remove(ingredient);
            }
        }
    }

    /**
     * Clear the Trie
     */
    public void clear() {
        this.rootBranch.getNodes()
            .clear();
    }

    /**
     * @return the amount of values in the trie
     */
    public int size() {
        return this.size;
    }

    public @NotNull Stream<GT_Recipe> findStream(@NotNull ItemStack @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids, @NotNull Predicate<GT_Recipe> predicate, boolean parallel) {
        if (items.length == 0 && fluids.length == 0) {
            return Stream.empty();
        }

        predicate = predicate.and(r -> r.isRecipeInputEqual(false, false, fluids, items));
        return StreamSupport.stream(new RecipeTrieSpliterator(this, toIngredients(items, fluids), predicate), parallel);
    }

    /**
     * Finds a recipe where the input quantities must sufficiently match
     *
     * @param items     the items potentially in the recipe inputs
     * @param fluids    the fluids potentially in the recipe inputs
     * @param predicate the predicate to determine if the recipe matches
     * @return the recipe
     */
    public @Nullable GT_Recipe findMatching(@NotNull ItemStack @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids, @NotNull Predicate<GT_Recipe> predicate) {
        if (items.length == 0 && fluids.length == 0) {
            return null;
        }

        var ingredients = toIngredients(items, fluids);
        predicate = predicate.and(r -> r.isRecipeInputEqual(false, false, fluids, items));
        return find(ingredients, predicate);
    }

    /**
     * @param items     the items potentially in the recipe inputs
     * @param fluids    the fluids potentially in the recipe inputs
     * @param predicate the predicate to determine if the recipe matches
     * @return the recipe
     */
    public @Nullable GT_Recipe find(@NotNull ItemHolder @NotNull [] items, @NotNull FluidStack @NotNull [] fluids,
        @NotNull Predicate<GT_Recipe> predicate) {
        if (items.length == 0 && fluids.length == 0) {
            return null;
        }

        var ingredients = toIngredients(items, fluids);
        return find(ingredients, predicate);
    }

    /**
     * @param ingredients the ingredients potentially in the recipe inputs
     * @param predicate   the predicate to determine if the recipe matches
     * @return the recipe
     */
    public @Nullable GT_Recipe find(@NotNull AbstractMapIngredient @NotNull [] @NotNull [] ingredients,
        @NotNull Predicate<GT_Recipe> predicate) {
        BitSet skipList = new BitSet();
        for (int i = 0; i < ingredients.length; i++) {
            skipList.set(i);
            GT_Recipe recipe = find(ingredients, rootBranch, predicate, i, 0, skipList);
            if (recipe != null) {
                return recipe;
            }

            skipList.clear(i);
        }

        return null;
    }

    /**
     * @param items  the items to convert
     * @param fluids the fluids to convert
     * @return an array of ingredients
     */
    private @NotNull AbstractMapIngredient @NotNull [] @NotNull [] toIngredients(@NotNull ItemHolder @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids) {
        List<AbstractMapIngredient[]> list = new ArrayList<>(items.length + fluids.length);
        for (ItemHolder stack : items) {
            List<AbstractMapIngredient> inner = new ObjectArrayList<>(1);
            // regular input
            inner.add(MapIngredientFactory.from(stack));

            if (hasOreDictInputs) {
                for (int i : stack.getOreDictTagIDs()) {
                    inner.add(new MapOreDictIngredient(i));
                }
            }

            if (hasNBTMatchInputs) {
                // TODO partial nbt match
                // inner.add(new MapPartialNBTIngredient(stack));
            }
            inner.sort(MapIngredientComparator.INSTANCE);
            list.add(inner.toArray(new AbstractMapIngredient[0]));
        }

        for (FluidStack stack : fluids) {
            list.add(new AbstractMapIngredient[] { new MapFluidStackIngredient(stack) });
        }

        return list.toArray(new AbstractMapIngredient[0][]);
    }

    /**
     * @param items  the items to convert
     * @param fluids the fluids to convert
     * @return an array of ingredients
     */
    private @NotNull AbstractMapIngredient @NotNull [] @NotNull [] toIngredients(@NotNull ItemStack @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids) {
        List<AbstractMapIngredient[]> list = new ArrayList<>(items.length + fluids.length);
        for (ItemStack stack : items) {
            List<AbstractMapIngredient> inner = new ObjectArrayList<>(1);
            // regular input
            inner.add(MapIngredientFactory.from(stack));

            if (hasOreDictInputs) {
                for (int i : OreDictionary.getOreIDs(stack)) {
                    inner.add(new MapOreDictIngredient(i));
                }
            }

            if (hasNBTMatchInputs) {
                // TODO partial nbt match
                // inner.add(new MapPartialNBTIngredient(stack));
            }
            inner.sort(MapIngredientComparator.INSTANCE);
            list.add(inner.toArray(new AbstractMapIngredient[0]));
        }

        for (FluidStack stack : fluids) {
            list.add(new AbstractMapIngredient[] { new MapFluidStackIngredient(stack) });
        }

        return list.toArray(new AbstractMapIngredient[0][]);
    }

    /**
     * @param ingredients the ingredients potentially leading to a recipe
     * @param branch      the branch to search
     * @param predicate   the predicate to determine if a found recipe is valid
     * @param index       the ingredient index
     * @return the found recipe
     */
    private static @Nullable GT_Recipe find(@NotNull AbstractMapIngredient @NotNull [] @NotNull [] ingredients,
        @NotNull TrieBranch branch, @NotNull Predicate<GT_Recipe> predicate, int index, int count,
        @NotNull BitSet skipList) {
        if (count == ingredients.length) {
            // ingredients exhausted
            return null;
        }

        for (var ingredient : ingredients[index]) {
            var result = branch.getNodes()
                .get(ingredient);
            if (result == null) {
                // no branch to continue with, try the next possible ingredient
                continue;
            }

            GT_Recipe recipe = result.left();
            if (recipe != null) {
                if (predicate.test(recipe)) {
                    return recipe;
                }

                // found a recipe, but the predicate fails, so look for more
                continue;
            }

            TrieBranch nextBranch = result.right();
            assert nextBranch != null;

            int i = (index + 1) % ingredients.length;
            while (i != index) {
                if (skipList.get(i)) {
                    i = (i + 1) % ingredients.length;
                    continue;
                }
                skipList.set(i);

                // recursion: try every unused ingredient as the next branch in the route
                recipe = find(ingredients, nextBranch, predicate, i, count + 1, skipList);
                skipList.clear(i);

                if (recipe != null) {
                    return recipe;
                }

                i = (i + 1) % ingredients.length;
            }
        }

        return null;
    }

    /**
     * Exhaustively gathers all recipes that can be crafted with the given ingredients into a set.
     *
     * @param items  the input items
     * @param fluids the input fluids
     * @return all the recipes that can be run with these inputs
     */
    public @NotNull Set<GT_Recipe> findAll(@NotNull ItemStack @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids) {
        Set<GT_Recipe> set = new ObjectOpenHashSet<>();
        var ingredients = toIngredients(items, fluids);
        BitSet skipList = new BitSet();

        for (int i = 0; i < ingredients.length; i++) {
            skipList.set(i);
            findAll(ingredients, rootBranch, set, i, 0, skipList);
            skipList.clear(i);
        }
        return set;
    }

    /**
     * @param ingredients the ingredients potentially leading to a recipe
     * @param branch      the branch to search
     * @param set         the set to store recipes in
     * @param index       the ingredient index
     */
    private static void findAll(@NotNull AbstractMapIngredient @NotNull [] @NotNull [] ingredients,
        @NotNull TrieBranch branch, @NotNull Set<GT_Recipe> set, int index, int count, @NotNull BitSet skipList) {
        if (count == ingredients.length) {
            // ingredients exhausted
            return;
        }

        for (var ingredient : ingredients[index]) {
            var result = branch.getNodes()
                .get(ingredient);
            if (result == null) {
                continue;
            }

            GT_Recipe recipe = result.left();
            if (recipe != null) {
                set.add(recipe);
                continue;
            }

            TrieBranch nextBranch = result.right();
            assert nextBranch != null;

            int i = (index + 1) % ingredients.length;
            while (i != index) {
                if (skipList.get(i)) {
                    i = (i + 1) % ingredients.length;
                    continue;
                }
                skipList.set(i);

                // recursion: try every unused ingredient as the next branch in the route
                findAll(ingredients, nextBranch, set, i, count + 1, skipList);
                skipList.clear(i);

                i = (i + 1) % ingredients.length;
            }
        }
    }
}
