package gregtech.api.recipe.lookup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import gregtech.api.util.GTRecipe;

public final class GTRecipeLookup {

    private final GTRecipeLookupBranch rootBranch = new GTRecipeLookupBranch();
    private boolean frozen;

    public void add(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
        if (frozen) {
            throw new UnsupportedOperationException("built recipe lookup is immutable");
        }
        Objects.requireNonNull(recipe, "recipe");
        validateIngredients(ingredients);

        addRecursive(recipe, ingredients, rootBranch, 0);
    }

    public Iterator<GTRecipe> iterator(List<List<GTRecipeLookupIngredient>> ingredients) {
        validateIngredients(ingredients);
        return new RecipeIterator(rootBranch, ingredients);
    }

    GTRecipeLookupBranch getRootBranch() {
        return rootBranch;
    }

    void freeze() {
        frozen = true;
    }

    private void addRecursive(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients,
        GTRecipeLookupBranch branch, int index) {
        boolean lastIngredient = index == ingredients.size() - 1;
        for (GTRecipeLookupIngredient ingredient : ingredients.get(index)) {
            Map<GTRecipeLookupIngredient, Node> nodes = branch.nodesFor(ingredient);
            Node node = nodes.get(ingredient);

            if (lastIngredient) {
                if (node == null) {
                    nodes.put(ingredient, Node.leaf(recipe));
                    continue;
                }
                if (!node.containsRecipe(recipe)) {
                    node.addRecipe(recipe);
                }
                continue;
            }

            GTRecipeLookupBranch childBranch;
            if (node == null) {
                childBranch = new GTRecipeLookupBranch();
                nodes.put(ingredient, Node.branch(childBranch));
            } else if (node.hasBranch()) {
                childBranch = node.branch;
            } else {
                childBranch = new GTRecipeLookupBranch();
                node.branch = childBranch;
            }

            addRecursive(recipe, ingredients, childBranch, index + 1);
        }
    }

    private static void validateIngredients(List<List<GTRecipeLookupIngredient>> ingredients) {
        Objects.requireNonNull(ingredients, "ingredients");
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("ingredients must not be empty");
        }
        for (List<GTRecipeLookupIngredient> group : ingredients) {
            Objects.requireNonNull(group, "ingredient group");
            if (group.isEmpty()) {
                throw new IllegalArgumentException("ingredient group must not be empty");
            }
            for (GTRecipeLookupIngredient ingredient : group) {
                Objects.requireNonNull(ingredient, "ingredient");
            }
        }
    }

    static final class Node {

        private List<GTRecipe> recipes;
        private GTRecipeLookupBranch branch;

        private Node(List<GTRecipe> recipes, GTRecipeLookupBranch branch) {
            this.recipes = recipes;
            this.branch = branch;
        }

        static Node leaf(GTRecipe recipe) {
            List<GTRecipe> recipes = new ArrayList<>(1);
            recipes.add(recipe);
            return new Node(recipes, null);
        }

        static Node branch(GTRecipeLookupBranch branch) {
            return new Node(null, branch);
        }

        boolean hasBranch() {
            return branch != null;
        }

        boolean hasRecipes() {
            return recipes != null && !recipes.isEmpty();
        }

        GTRecipeLookupBranch getBranch() {
            return branch;
        }

        void addRecipe(GTRecipe recipe) {
            if (recipes == null) {
                recipes = new ArrayList<>(1);
            }
            recipes.add(recipe);
        }

        boolean containsRecipe(GTRecipe recipe) {
            return recipes != null && recipes.contains(recipe);
        }
    }

    private static final class SearchFrame {

        private final int index;
        private final GTRecipeLookupBranch branch;
        private int ingredientIndex = 0;

        private SearchFrame(int index, GTRecipeLookupBranch branch) {
            this.index = index;
            this.branch = branch;
        }
    }

    private static final class RecipeIterator implements Iterator<GTRecipe> {

        private final List<List<GTRecipeLookupIngredient>> ingredients;
        private final Deque<SearchFrame> stack = new ArrayDeque<>();
        private Iterator<GTRecipe> leafIterator = Collections.emptyIterator();
        private GTRecipe nextRecipe;
        private boolean hasCachedNext;

        private RecipeIterator(GTRecipeLookupBranch rootBranch, List<List<GTRecipeLookupIngredient>> ingredients) {
            this.ingredients = ingredients;
            for (int i = ingredients.size() - 1; i >= 0; i--) {
                stack.push(new SearchFrame(i, rootBranch));
            }
        }

        @Override
        public boolean hasNext() {
            if (!hasCachedNext) {
                nextRecipe = findNext();
                hasCachedNext = true;
            }
            return nextRecipe != null;
        }

        @Override
        public GTRecipe next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            GTRecipe result = nextRecipe;
            nextRecipe = null;
            hasCachedNext = false;
            return result;
        }

        private GTRecipe findNext() {
            while (true) {
                if (leafIterator.hasNext()) {
                    return leafIterator.next();
                }

                if (stack.isEmpty()) {
                    return null;
                }

                SearchFrame frame = stack.peek();
                List<GTRecipeLookupIngredient> ingredientGroup = ingredients.get(frame.index);
                if (frame.ingredientIndex >= ingredientGroup.size()) {
                    stack.pop();
                    continue;
                }

                GTRecipeLookupIngredient ingredient = ingredientGroup.get(frame.ingredientIndex);
                frame.ingredientIndex++;
                Node result = frame.branch.getNode(ingredient);
                if (result == null) {
                    continue;
                }

                if (result.hasBranch()) {
                    for (int i = ingredients.size() - 1; i >= 0; i--) {
                        stack.push(new SearchFrame(i, result.branch));
                    }
                }
                if (result.hasRecipes()) {
                    leafIterator = result.recipes.iterator();
                }
            }
        }
    }
}
