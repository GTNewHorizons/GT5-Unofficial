package gregtech.api.recipe.store;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.store.ingredient.AbstractMapIngredient;
import gregtech.api.util.GT_Recipe;

// TODO not functional
final class RecipeTrieSpliterator implements Spliterator<GT_Recipe> {

    private static final int CHARACTERISTICS = Spliterator.NONNULL | Spliterator.IMMUTABLE;

    private final RecipeTrie trie;
    private final AbstractMapIngredient[][] ingredients;
    private final Predicate<GT_Recipe> predicate;

    private final Deque<Integer> posStack = new ArrayDeque<>();
    private final Deque<Integer> endStack = new ArrayDeque<>();
    private final Deque<TrieBranch> branchStack = new ArrayDeque<>();
    private final Deque<Integer> depthStack = new ArrayDeque<>();
    private final Deque<Integer> indexStack = new ArrayDeque<>();
    private final Deque<BitSet> skipListStack = new ArrayDeque<>();

    RecipeTrieSpliterator(@NotNull RecipeTrie trie, @NotNull AbstractMapIngredient @NotNull [][] ingredients,
        @NotNull Predicate<GT_Recipe> predicate) {
        this.trie = trie;
        this.ingredients = ingredients;
        this.predicate = predicate;
        this.posStack.push(0);
        this.endStack.push(ingredients.length == 0 ? 0 : ingredients[0].length);
        this.branchStack.push(trie.rootBranch);
        this.depthStack.push(0);
        this.indexStack.push(0);
        BitSet skipList = new BitSet();
        skipList.set(0);
        this.skipListStack.push(skipList);
    }

    private RecipeTrieSpliterator(@NotNull RecipeTrieSpliterator spliterator, int start, int end) {
        this.trie = spliterator.trie;
        this.ingredients = spliterator.ingredients;
        this.predicate = spliterator.predicate;
        this.posStack.addLast(start);
        this.endStack.addLast(end);
        this.branchStack.addLast(spliterator.branchStack.getFirst());
        this.depthStack.addLast(spliterator.depthStack.getFirst());
        this.indexStack.addLast(spliterator.indexStack.getFirst());
        this.skipListStack.addLast(
            BitSet.valueOf(
                spliterator.skipListStack.getFirst()
                    .toLongArray()));
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
        int depth = depthStack.getLast();
        if (depth == ingredients.length) {
            return null;
        }

        int index = indexStack.getLast();
        int end = endStack.getLast();
        TrieBranch branch = branchStack.getLast();
        var arr = ingredients[index];
        for (int i = posStack.getLast(); i < end; i++) {
            var ingredient = arr[i];
            var result = branch.getNodes()
                .get(ingredient);
            if (result == null) {
                // keep searching at this depth
                continue;
            }

            GT_Recipe recipe = result.left();
            if (recipe != null) {
                if (predicate.test(recipe)) {
                    if (i + 1 < end) {
                        // store the position and branch to resume from next time
                        indexStack.addLast(index);
                        depthStack.addLast(depth);
                        endStack.addLast(end);
                        posStack.addLast(i + 1); // need to advance to the next position
                        branchStack.addLast(branch);
                    } else {
                        depthStack.removeLast();
                        indexStack.removeLast();
                        endStack.removeLast();
                        branchStack.removeLast();
                        posStack.removeLast();
                    }
                    return recipe;
                }

                // found a recipe, but the predicate fails, so look for more
                continue;
            }

            TrieBranch nextBranch = result.right();
            assert nextBranch != null;

            BitSet skipList = skipListStack.getLast();
            int j = (index + 1) % ingredients.length;
            while (j != index) {
                if (skipList.get(j)) {
                    j = (j + 1) % ingredients.length;
                    continue;
                }
                skipList.set(j);

                indexStack.addLast(j);
                depthStack.addLast(depth + 1);
                endStack.addLast(ingredients[j].length);
                posStack.addLast(0);
                branchStack.addLast(nextBranch);
                skipListStack.addLast(BitSet.valueOf(skipList.toLongArray()));

                // recursion: try every unused ingredient as the next branch in the route
                recipe = seek();
                skipList.clear(j);

                if (recipe != null) {
                    return recipe;
                }

                j = (j + 1) % ingredients.length;
            }
            skipListStack.removeLast();
        }

        depthStack.removeLast();
        indexStack.removeLast();
        endStack.removeLast();
        branchStack.removeLast();
        posStack.removeLast();

        return seek();
    }

    @Override
    public @Nullable Spliterator<GT_Recipe> trySplit() {
        if (isEmpty()) {
            return null;
        }

        return trySplit(
            new ArrayDeque<>(posStack),
            new ArrayDeque<>(endStack),
            new ArrayDeque<>(branchStack),
            new ArrayDeque<>(depthStack),
            new ArrayDeque<>(indexStack),
            new ArrayDeque<>(skipListStack));
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
        @NotNull Deque<TrieBranch> branches, @NotNull Deque<Integer> depths, @NotNull Deque<Integer> indices,
        @NotNull Deque<BitSet> skipLists) {
        if (isEmpty()) {
            return null;
        }

        int pos = poses.getFirst();
        int end = ends.getFirst();

        int mid = (pos + end) >>> 1;
        if (pos > mid) {
            // cannot split here, split higher up
            poses.removeFirst();
            ends.removeFirst();
            branches.removeFirst();
            depths.removeFirst();
            indices.removeFirst();
            skipLists.removeFirst();
            return trySplit(poses, ends, branches, depths, indices, skipLists);
        }

        Spliterator<GT_Recipe> spliterator = new RecipeTrieSpliterator(this, pos, mid);
        this.posStack.addFirst(mid); // current starts in the middle now
        return spliterator;
    }

    private boolean isEmpty() {
        return posStack.isEmpty() || endStack.isEmpty()
            || branchStack.isEmpty()
            || depthStack.isEmpty()
            || indexStack.isEmpty()
            || skipListStack.isEmpty();
    }

    @Override
    public long estimateSize() {
        return (long) (endStack.getLast() - posStack.getLast()) * depthStack.size();
    }

    @Override
    public int characteristics() {
        return CHARACTERISTICS;
    }
}
