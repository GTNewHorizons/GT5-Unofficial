package gregtech.api.recipe.lookup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class GTRecipeLookupBranch {

    // Most branches have at most two entries, so store those entries inline.
    private GTRecipeLookupIngredient ingredient1;
    private GTRecipeLookup.Node node1;
    private GTRecipeLookupIngredient ingredient2;
    private GTRecipeLookup.Node node2;
    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodes;

    GTRecipeLookup.Node getNode(GTRecipeLookupIngredient ingredient) {
        if (nodes != null) {
            return nodes.get(ingredient);
        }
        if (node1 != null && Objects.equals(ingredient, ingredient1)) {
            return node1;
        }
        if (node2 != null && Objects.equals(ingredient, ingredient2)) {
            return node2;
        }
        return null;
    }

    void putNode(GTRecipeLookupIngredient ingredient, GTRecipeLookup.Node node) {
        if (nodes != null) {
            nodes.put(ingredient, node);
            return;
        }
        if (node1 == null || Objects.equals(ingredient, ingredient1)) {
            ingredient1 = ingredient;
            node1 = node;
            return;
        }
        if (node2 == null || Objects.equals(ingredient, ingredient2)) {
            ingredient2 = ingredient;
            node2 = node;
            return;
        }

        Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> promotedNodes = new HashMap<>(4);
        promotedNodes.put(ingredient1, node1);
        promotedNodes.put(ingredient2, node2);
        promotedNodes.put(ingredient, node);
        ingredient1 = null;
        node1 = null;
        ingredient2 = null;
        node2 = null;
        nodes = promotedNodes;
    }

    boolean isEmpty() {
        return nodes == null ? node1 == null && node2 == null : nodes.isEmpty();
    }

    void clear() {
        ingredient1 = null;
        node1 = null;
        ingredient2 = null;
        node2 = null;
        nodes = null;
    }
}
