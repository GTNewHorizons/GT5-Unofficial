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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GTRecipeLookup {

    private final GTRecipeLookupBranch rootBranch = new GTRecipeLookupBranch();
    private boolean frozen;

    public void add(GTRecipe recipe, List<List<GTRecipeLookupIngredient>> ingredients) {
        if (frozen) {
            throw new UnsupportedOperationException("built recipe lookup is immutable");
        }
        addRecursive(recipe, ingredients, rootBranch, 0);
    }

    public Iterator<GTRecipe> iterator(List<GTRecipeLookupIngredient> ingredients) {
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
            } else if (node.branch != null) {
                childBranch = node.branch;
            } else {
                childBranch = new GTRecipeLookupBranch();
                node.branch = childBranch;
            }

            addRecursive(recipe, ingredients, childBranch, index + 1);
        }
    }

    static final class Node {

        private @Nullable List<GTRecipe> recipes;
        private @Nullable GTRecipeLookupBranch branch;

        static Node leaf(GTRecipe recipe) {
            Node node = new Node();
            List<GTRecipe> recipes = new ArrayList<>(1);
            recipes.add(recipe);
            node.recipes = recipes;
            return node;
        }

        static Node branch(GTRecipeLookupBranch branch) {
            Node node = new Node();
            node.branch = branch;
            return node;
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
        private @Nullable GTRecipe nextRecipe;

        private RecipeIterator(GTRecipeLookupBranch rootBranch, List<GTRecipeLookupIngredient> ingredients) {
            this.ingredients = new ArrayList<>(new LinkedHashSet<>(ingredients));
            stack.push(new SearchFrame(rootBranch));
        }

        @Override
        public boolean hasNext() {
            if (nextRecipe == null) {
                nextRecipe = findNext();
            }
            return nextRecipe != null;
        }

        @Override
        public GTRecipe next() {
            if (nextRecipe == null) {
                nextRecipe = findNext();
            }
            if (nextRecipe == null) {
                throw new NoSuchElementException();
            }
            GTRecipe result = nextRecipe;
            nextRecipe = null;
            return result;
        }

        private @Nullable GTRecipe findNext() {
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
                Node node = frame.branch.getNode(ingredient);

                if (node == null) {
                    continue;
                }

                if (node.branch != null) {
                    stack.push(new SearchFrame(node.branch));
                }

                if (node.recipes != null && !node.recipes.isEmpty()) {
                    leafIterator = node.recipes.iterator();
                }
            }
        }
    }
}
