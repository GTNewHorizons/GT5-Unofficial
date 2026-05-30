package gregtech.api.recipe.lookup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

    public Iterator<GTRecipe> iterator(List<GTRecipeLookupIngredient> ingredients) {
        validateRuntimeIngredients(ingredients);
        return new RecipeIterator(rootBranch, ingredients);
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
                    nodes.put(ingredient, Node.leaf(recipe, branch.allocateNodeOrder()));
                    continue;
                }
                if (!node.containsRecipe(recipe)) {
                    node.addRecipe(recipe, branch.allocateNodeOrder());
                }
                continue;
            }

            GTRecipeLookupBranch childBranch;
            if (node == null) {
                childBranch = new GTRecipeLookupBranch();
                nodes.put(ingredient, Node.branch(childBranch, branch.allocateNodeOrder()));
            } else if (node.hasBranch()) {
                childBranch = node.branch;
            } else {
                childBranch = new GTRecipeLookupBranch();
                node.branch = childBranch;
                node.branchOrder = branch.allocateNodeOrder();
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

    private static void validateRuntimeIngredients(List<GTRecipeLookupIngredient> ingredients) {
        Objects.requireNonNull(ingredients, "ingredients");
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("ingredients must not be empty");
        }
        for (GTRecipeLookupIngredient ingredient : ingredients) {
            Objects.requireNonNull(ingredient, "ingredient");
        }
    }

    static final class Node {

        private List<GTRecipe> recipes;
        private GTRecipeLookupBranch branch;
        private int recipeOrder = -1;
        private int branchOrder = -1;

        static Node leaf(GTRecipe recipe, int order) {
            Node node = new Node();
            List<GTRecipe> recipes = new ArrayList<>(1);
            recipes.add(recipe);
            node.recipes = recipes;
            node.recipeOrder = order;
            return node;
        }

        static Node branch(GTRecipeLookupBranch branch, int order) {
            Node node = new Node();
            node.branch = branch;
            node.branchOrder = order;
            return node;
        }

        boolean hasBranch() {
            return branch != null;
        }

        boolean hasRecipes() {
            return recipes != null && !recipes.isEmpty();
        }

        void addRecipe(GTRecipe recipe, int order) {
            if (recipes == null) {
                recipes = new ArrayList<>(1);
                recipeOrder = order;
            }
            recipes.add(recipe);
        }

        boolean containsRecipe(GTRecipe recipe) {
            return recipes != null && recipes.contains(recipe);
        }
    }

    private static final class SearchFrame {

        private final List<SearchAction> actions;
        private int actionIndex = 0;

        private SearchFrame(GTRecipeLookupBranch branch, List<GTRecipeLookupIngredient> ingredients) {
            this.actions = new ArrayList<>(ingredients.size());
            for (GTRecipeLookupIngredient ingredient : ingredients) {
                Node node = branch.getNode(ingredient);
                if (node == null) {
                    continue;
                }

                if (node.hasBranch()) {
                    actions.add(SearchAction.branch(node));
                }
                if (node.hasRecipes()) {
                    actions.add(SearchAction.recipes(node));
                }
            }
            actions.sort((first, second) -> Integer.compare(first.order, second.order));
        }
    }

    private static final class SearchAction {

        private final Node node;
        private final boolean branch;
        private final int order;

        private SearchAction(Node node, boolean branch, int order) {
            this.node = node;
            this.branch = branch;
            this.order = order;
        }

        static SearchAction branch(Node node) {
            return new SearchAction(node, true, node.branchOrder);
        }

        static SearchAction recipes(Node node) {
            return new SearchAction(node, false, node.recipeOrder);
        }
    }

    private static final class RecipeIterator implements Iterator<GTRecipe> {

        private final List<GTRecipeLookupIngredient> ingredients;
        private final Deque<SearchFrame> stack = new ArrayDeque<>();
        private Iterator<GTRecipe> leafIterator = Collections.emptyIterator();
        private GTRecipe nextRecipe;
        private boolean hasCachedNext;

        private RecipeIterator(GTRecipeLookupBranch rootBranch, List<GTRecipeLookupIngredient> ingredients) {
            this.ingredients = new ArrayList<>(new LinkedHashSet<>(ingredients));
            stack.push(new SearchFrame(rootBranch, this.ingredients));
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
                if (frame.actionIndex >= frame.actions.size()) {
                    stack.pop();
                    continue;
                }

                SearchAction action = frame.actions.get(frame.actionIndex++);
                Node result = action.node;

                if (action.branch) {
                    stack.push(new SearchFrame(result.branch, ingredients));
                } else {
                    leafIterator = result.recipes.iterator();
                }
            }
        }
    }
}
