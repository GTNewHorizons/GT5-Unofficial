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
        return new RecipeIterator(rootBranch, flattenIngredients(ingredients));
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

    private static List<GTRecipeLookupIngredient> flattenIngredients(List<List<GTRecipeLookupIngredient>> ingredients) {
        if (ingredients.size() == 1) {
            return ingredients.get(0);
        }

        int size = 0;
        for (List<GTRecipeLookupIngredient> group : ingredients) {
            size += group.size();
        }

        List<GTRecipeLookupIngredient> flattened = new ArrayList<>(size);
        for (List<GTRecipeLookupIngredient> group : ingredients) {
            flattened.addAll(group);
        }
        return flattened;
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

        private final GTRecipeLookupBranch branch;
        private int ingredientIndex = 0;

        private SearchFrame(GTRecipeLookupBranch branch) {
            this.branch = branch;
        }
    }

    private static final class RecipeIterator implements Iterator<GTRecipe> {

        private final List<GTRecipeLookupIngredient> ingredients;
        private final Deque<SearchFrame> stack = new ArrayDeque<>();
        private Iterator<GTRecipe> leafIterator = Collections.emptyIterator();
        private GTRecipe nextRecipe;
        private boolean hasCachedNext;

        private RecipeIterator(GTRecipeLookupBranch rootBranch, List<GTRecipeLookupIngredient> ingredients) {
            this.ingredients = ingredients;
            stack.push(new SearchFrame(rootBranch));
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
                if (frame.ingredientIndex >= ingredients.size()) {
                    stack.pop();
                    continue;
                }

                GTRecipeLookupIngredient ingredient = ingredients.get(frame.ingredientIndex++);
                Node result = frame.branch.getNode(ingredient);
                if (result == null) {
                    continue;
                }

                if (result.hasBranch()) {
                    stack.push(new SearchFrame(result.branch));
                }
                if (result.hasRecipes()) {
                    leafIterator = result.recipes.iterator();
                }
            }
        }
    }
}
