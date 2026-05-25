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

    public boolean add(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
        if (frozen) {
            throw new UnsupportedOperationException("built recipe lookup is immutable");
        }
        Objects.requireNonNull(recipe, "recipe");
        validateIngredients(ingredients);

        List<Runnable> rollbackActions = new ArrayList<>();
        if (addRecursive(recipe, ingredients, rootBranch, 0, rollbackActions)) {
            return true;
        }

        for (int i = rollbackActions.size() - 1; i >= 0; i--) {
            rollbackActions.get(i)
                .run();
        }
        return false;
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

    private boolean addRecursive(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients,
        GTRecipeLookupBranch branch, int index, List<Runnable> rollbackActions) {
        boolean lastIngredient = index == ingredients.size() - 1;
        for (GTRecipeLookupIngredient ingredient : ingredients.get(index)) {
            Map<GTRecipeLookupIngredient, Node> nodes = branch.nodesFor(ingredient);
            Node node = nodes.get(ingredient);

            if (lastIngredient) {
                if (node == null) {
                    Node newNode = Node.leaf(recipe);
                    nodes.put(ingredient, newNode);
                    rollbackActions.add(() -> removeNode(nodes, ingredient, newNode));
                    continue;
                }
                if (!node.containsRecipe(recipe)) {
                    int recipeIndex = node.recipeCount();
                    node.addRecipe(recipe);
                    rollbackActions.add(() -> removeRecipeAt(node, recipeIndex, recipe));
                }
                continue;
            }

            GTRecipeLookupBranch childBranch;
            if (node == null) {
                childBranch = new GTRecipeLookupBranch();
                Node newNode = Node.branch(childBranch);
                nodes.put(ingredient, newNode);
                rollbackActions.add(() -> removeNode(nodes, ingredient, newNode));
            } else if (node.isBranch()) {
                childBranch = node.branch;
            } else {
                childBranch = new GTRecipeLookupBranch();
                node.branch = childBranch;
                rollbackActions.add(() -> removeBranch(node, childBranch));
            }

            if (!addRecursive(recipe, ingredients, childBranch, index + 1, rollbackActions)) {
                return false;
            }
        }
        return true;
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

    private static void removeNode(Map<GTRecipeLookupIngredient, Node> nodes, GTRecipeLookupIngredient ingredient,
        Node expectedNode) {
        if (nodes.get(ingredient) == expectedNode) {
            nodes.remove(ingredient);
        }
    }

    private static void removeBranch(Node node, GTRecipeLookupBranch expectedBranch) {
        if (node.branch == expectedBranch && expectedBranch.isEmpty()) {
            node.branch = null;
        }
    }

    private static void removeRecipeAt(Node node, int index, GTRecipe recipe) {
        List<GTRecipe> recipes = node.recipes;
        if (recipes == null) {
            return;
        }
        if (index < recipes.size() && recipes.get(index) == recipe) {
            recipes.remove(index);
            if (recipes.isEmpty()) {
                node.recipes = null;
            }
            return;
        }
        for (int i = recipes.size() - 1; i >= 0; i--) {
            if (recipes.get(i) == recipe) {
                recipes.remove(i);
                if (recipes.isEmpty()) {
                    node.recipes = null;
                }
                return;
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

        boolean isBranch() {
            return branch != null;
        }

        boolean hasRecipes() {
            return recipes != null && !recipes.isEmpty();
        }

        GTRecipeLookupBranch getBranch() {
            return branch;
        }

        int recipeCount() {
            return recipes == null ? 0 : recipes.size();
        }

        void addRecipe(GTRecipe recipe) {
            if (recipes == null) {
                recipes = new ArrayList<>(1);
            }
            recipes.add(recipe);
        }

        boolean containsRecipe(GTRecipe recipe) {
            if (recipes == null) {
                return false;
            }
            for (GTRecipe existingRecipe : recipes) {
                if (existingRecipe == recipe) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class SearchFrame {

        private final int index;
        private final GTRecipeLookupBranch branch;
        private int ingredientIndex;

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

                if (result.isBranch()) {
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
