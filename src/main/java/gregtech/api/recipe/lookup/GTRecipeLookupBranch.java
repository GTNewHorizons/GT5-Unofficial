package gregtech.api.recipe.lookup;

import java.util.LinkedHashMap;
import java.util.Map;

final class GTRecipeLookupBranch {

    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodes;
    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nbtSensitiveNodes;

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> getNodes() {
        if (nodes == null) {
            nodes = new LinkedHashMap<>(2);
        }
        return nodes;
    }

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> getNbtSensitiveNodes() {
        if (nbtSensitiveNodes == null) {
            nbtSensitiveNodes = new LinkedHashMap<>(2);
        }
        return nbtSensitiveNodes;
    }

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodesFor(GTRecipeLookupIngredient ingredient) {
        if (ingredient.isNbtSensitive()) {
            return getNbtSensitiveNodes();
        }
        return getNodes();
    }

    GTRecipeLookup.Node getNode(GTRecipeLookupIngredient ingredient) {
        Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> selectedNodes = ingredient.isNbtSensitive()
            ? nbtSensitiveNodes
            : nodes;
        if (selectedNodes == null) {
            return null;
        }
        return selectedNodes.get(ingredient);
    }

    boolean isEmpty() {
        return (nodes == null || nodes.isEmpty()) && (nbtSensitiveNodes == null || nbtSensitiveNodes.isEmpty());
    }

    void clear() {
        nodes = null;
        nbtSensitiveNodes = null;
    }
}
