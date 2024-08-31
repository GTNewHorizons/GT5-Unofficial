package gregtech.api.recipe.store;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.store.ingredient.AbstractMapIngredient;
import gregtech.api.recipe.store.ingredient.MapFluidStackIngredient;
import gregtech.api.recipe.store.ingredient.MapIngredientComparator;
import gregtech.api.recipe.store.ingredient.MapItemStackIngredient;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.function.Either;
import gregtech.api.util.item.ItemHolder;
import gregtech.common.config.gregtech.ConfigGeneral;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class RecipeTrie {

    // debug
    private static final boolean CRASH_ON_EMPTY = false;

    private static final Map<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> itemIngredientRoot = new WeakHashMap<>();
    private static final Map<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> fluidIngredientRoot = new WeakHashMap<>();

    private final TrieBranch rootBranch = new TrieBranch();

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

            // tail recursion: add each ingredient on the right branch path, and insert the recipe on the left at the end
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
                        GT_Log.recipe.printf("Conflicting recipes:%n1: %s%n2: %s", existing, recipe);
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
                StringBuilder builder = new StringBuilder("Conflicting recipes:%n1: ")
                    .append(recipe);
                int i = 2;
                for (GT_Recipe r : existingEdge.getAll().toArray(GT_Recipe[]::new)) {
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
    private static @NotNull AbstractMapIngredient @Nullable [] createIngredients(@NotNull GT_Recipe recipe) {
        int length = recipe.mInputs.length + recipe.mFluidInputs.length;
        if (length == 0) {
            return null;
        }

        ObjectArrayList<AbstractMapIngredient> list = new ObjectArrayList<>(length);
        if (recipe.mInputs.length > 0) {
            var unique = uniqueIngredients(recipe.mInputs);
            unique.sort(ItemHolder.COMPARATOR);
            for (var input : unique) {
                list.add(deduplicateIngredient(itemIngredientRoot, new MapItemStackIngredient(input)));
            }
        }
        if (recipe.mFluidInputs.length > 0) {
            var unique = uniqueIngredients(recipe.mFluidInputs);
            unique.sort(MapFluidStackIngredient.FLUID_COMPARATOR);
            for (var input : unique) {
                list.add(deduplicateIngredient(fluidIngredientRoot, new MapFluidStackIngredient(input)));
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
    private static @NotNull List<@NotNull ItemHolder> uniqueIngredients(@Nullable ItemStack @NotNull [] stacks) {
        List<ItemHolder> list = new ObjectArrayList<>(stacks.length);
        for (var stack : stacks) {
            if (stack == null) {
                continue;
            }

            ItemHolder itemHolder = new ItemHolder(stack);
            boolean isEqual = false;
            for (var seen : list) {
                if (seen.equals(itemHolder)) {
                    isEqual = true;
                    break;
                }
            }
            if (isEqual) {
                continue;
            }

            list.add(itemHolder);
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
    public @Nullable GT_Recipe find(@NotNull AbstractMapIngredient @NotNull [] ingredients,
        @NotNull Predicate<GT_Recipe> predicate) {
        for (int i = 0; i < ingredients.length; i++) {
            GT_Recipe recipe = find(ingredients, rootBranch, predicate, i);
            if (recipe != null) {
                return recipe;
            }
        }

        return null;
    }

    /**
     * @param items  the items to convert
     * @param fluids the fluids to convert
     * @return an array of ingredients
     */
    private static @NotNull AbstractMapIngredient @NotNull [] toIngredients(@NotNull ItemHolder @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids) {
        AbstractMapIngredient[] ingredients = new AbstractMapIngredient[items.length + fluids.length];
        int index = 0;
        for (ItemHolder item : items) {
            ingredients[index++] = new MapItemStackIngredient(item);
        }

        for (FluidStack fluid : fluids) {
            ingredients[index++] = new MapFluidStackIngredient(fluid);
        }

        ObjectArrays.stableSort(ingredients, MapIngredientComparator.INSTANCE);
        return ingredients;
    }

    /**
     * @param items  the items to convert
     * @param fluids the fluids to convert
     * @return an array of ingredients
     */
    private static @NotNull AbstractMapIngredient @NotNull [] toIngredients(@NotNull ItemStack @NotNull [] items,
        @NotNull FluidStack @NotNull [] fluids) {
        AbstractMapIngredient[] ingredients = new AbstractMapIngredient[items.length + fluids.length];
        int index = 0;
        for (ItemStack item : items) {
            ingredients[index++] = new MapItemStackIngredient(item);
        }

        for (FluidStack fluid : fluids) {
            ingredients[index++] = new MapFluidStackIngredient(fluid);
        }

        ObjectArrays.stableSort(ingredients, MapIngredientComparator.INSTANCE);
        return ingredients;
    }

    /**
     * @param ingredients the ingredients potentially leading to a recipe
     * @param branch      the branch to search
     * @param predicate   the predicate to determine if a found recipe is valid
     * @param index       the ingredient index
     * @return the found recipe
     */
    private static @Nullable GT_Recipe find(@NotNull AbstractMapIngredient @NotNull [] ingredients,
        @NotNull TrieBranch branch, @NotNull Predicate<GT_Recipe> predicate, int index) {
        if (index == ingredients.length) {
            // ingredients exhausted
            return null;
        }

        var result = branch.getNodes()
            .get(ingredients[index]);
        if (result == null) {
            // no branch to continue with
            return null;
        }

        GT_Recipe recipe = result.left();
        if (recipe != null) {
            if (predicate.test(recipe)) {
                return recipe;
            }
            return null;
        }

        TrieBranch nextBranch = result.right();
        assert nextBranch != null;

        // recursion: try every unused ingredient as the next branch in the route
        for (int i = index + 1; i < ingredients.length; i++) {
            recipe = find(ingredients, nextBranch, predicate, i);
            if (recipe != null) {
                return recipe;
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
        for (int i = 0; i < ingredients.length; i++) {
            findAll(ingredients, rootBranch, set, i);
        }
        return set;
    }

    /**
     * @param ingredients the ingredients potentially leading to a recipe
     * @param branch      the branch to search
     * @param set         the set to store recipes in
     * @param index       the ingredient index
     */
    private static void findAll(@NotNull AbstractMapIngredient @NotNull [] ingredients, @NotNull TrieBranch branch,
        @NotNull Set<GT_Recipe> set, int index) {
        if (index == ingredients.length) {
            // ingredients exhausted
            return;
        }

        var result = branch.getNodes()
            .get(ingredients[index]);
        if (result == null) {
            // no branch to continue with
            return;
        }

        GT_Recipe recipe = result.left();
        if (recipe != null) {
            set.add(recipe);
            return;
        }

        TrieBranch nextBranch = result.right();
        assert nextBranch != null;

        // recursion: try every unused ingredient as the next branch in the route
        for (int i = index + 1; i < ingredients.length; i++) {
            findAll(ingredients, nextBranch, set, i);
        }
    }

    private static final class RecipeTrieSpliterator implements Spliterator<GT_Recipe> {

        private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE;

        private final RecipeTrie trie;
        private final AbstractMapIngredient[] ingredients;
        private final Predicate<GT_Recipe> predicate;

        private final Deque<Integer> posStack = new ArrayDeque<>();
        private final Deque<Integer> endStack = new ArrayDeque<>();
        private final Deque<TrieBranch> branchStack = new ArrayDeque<>();

        private RecipeTrieSpliterator(@NotNull RecipeTrie trie, @NotNull AbstractMapIngredient @NotNull [] ingredients,
            @NotNull Predicate<GT_Recipe> predicate) {
            this.trie = trie;
            this.ingredients = ingredients;
            this.predicate = predicate;
            this.posStack.push(0);
            this.endStack.push(ingredients.length);
            this.branchStack.push(trie.rootBranch);
        }

        private RecipeTrieSpliterator(@NotNull RecipeTrieSpliterator spliterator, int start, int end,
            @NotNull TrieBranch branch) {
            this.trie = spliterator.trie;
            this.ingredients = spliterator.ingredients;
            this.predicate = spliterator.predicate;
            this.posStack.addLast(start);
            this.posStack.addAll(spliterator.posStack);
            this.endStack.addLast(end);
            this.endStack.addAll(spliterator.endStack);
            this.branchStack.addLast(branch);
            this.branchStack.addAll(spliterator.branchStack);
        }

        @Override
        public boolean tryAdvance(Consumer<? super GT_Recipe> action) {
            GT_Recipe recipe = seek();
            if (recipe != null) {
                action.accept(recipe);
                return true;
            }

            return false;
        }

        /**
         * @return the next recipe
         */
        private @Nullable GT_Recipe seek() {
            if (isEmpty()) {
                return null;
            }

            final int end = endStack.removeLast();
            TrieBranch branch = branchStack.removeLast();
            for (int i = posStack.removeLast(); i < end; i++) {
                var result = branch.getNodes()
                    .get(ingredients[i]);
                if (result == null) {
                    // keep searching at this depth
                    continue;
                }

                GT_Recipe recipe = result.left();
                if (recipe != null && predicate.test(recipe)) {
                    // store the position and branch to resume from next time
                    posStack.addLast(i + 1); // need to advance to the next position
                    endStack.addLast(end);
                    branchStack.addLast(branch);
                    return recipe;
                }

                TrieBranch nextBranch = result.right();
                assert nextBranch != null;

                // recursion: start at the next ingredient in the new branch
                posStack.addLast(i + 1);
                endStack.addLast(ingredients.length);
                branchStack.addLast(nextBranch);
                return seek();
            }
            return null;
        }

        @Override
        public @Nullable Spliterator<GT_Recipe> trySplit() {
            if (isEmpty()) {
                return null;
            }

            return trySplit(new ArrayDeque<>(posStack), new ArrayDeque<>(endStack), new ArrayDeque<>(branchStack));
        }

        /**
         * Recursively attempt to split the workload in half and give the other half to a new spliterator.
         *
         * @param poses    the starting positions
         * @param ends     the end positions
         * @param branches the branches
         * @return a spliterator with half of the current one's work.
         */
        private @Nullable Spliterator<GT_Recipe> trySplit(@NotNull Deque<Integer> poses, @NotNull Deque<Integer> ends,
            @NotNull Deque<TrieBranch> branches) {
            if (isEmpty()) {
                return null;
            }

            int pos = poses.removeLast();
            int end = ends.getFirst();
            TrieBranch branch = branches.getFirst();

            int mid = (pos + end) >>> 1;
            if (pos > mid) {
                // cannot split here, need to split higher up in the trie
                ends.removeLast();
                branches.removeFirst();
                return trySplit(poses, ends, branches);
            }

            Spliterator<GT_Recipe> spliterator = new RecipeTrieSpliterator(this, pos, mid, branch);
            this.posStack.addFirst(mid); // current starts in the middle now
            return spliterator;
        }

        private boolean isEmpty() {
            return posStack.isEmpty() || endStack.isEmpty() || branchStack.isEmpty();
        }

        @Override
        public long estimateSize() {
            return endStack.getLast() - posStack.getLast();
        }

        @Override
        public int characteristics() {
            return CHARACTERISTICS;
        }
    }
}
