package gregtech.api.recipe.lookup;

import java.util.LinkedHashMap;
import java.util.Map;

final class GTRecipeLookupBranch {

    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodes;
    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> specialNodes;

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> getNodes() {
        if (nodes == null) {
            nodes = new LinkedHashMap<>(2);
        }
        return nodes;
    }

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> getSpecialNodes() {
        if (specialNodes == null) {
            specialNodes = new LinkedHashMap<>(2);
        }
        return specialNodes;
    }

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodesFor(GTRecipeLookupIngredient ingredient) {
        if (ingredient.isSpecialIngredient()) {
            return getSpecialNodes();
        }
        return getNodes();
    }

    GTRecipeLookup.Node getNode(GTRecipeLookupIngredient ingredient) {
        Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> selectedNodes = ingredient.isSpecialIngredient()
            ? specialNodes
            : nodes;
        if (selectedNodes == null) {
            return null;
        }
        return selectedNodes.get(ingredient);
    }

    boolean isEmpty() {
        return (nodes == null || nodes.isEmpty()) && (specialNodes == null || specialNodes.isEmpty());
    }

    void clear() {
        nodes = null;
        specialNodes = null;
    }
}
