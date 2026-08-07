package gregtech.api.recipe.lookup;

import java.util.LinkedHashMap;
import java.util.Map;

final class GTRecipeLookupBranch {

    private Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodes;

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> getNodes() {
        if (nodes == null) {
            nodes = new LinkedHashMap<>(2);
        }
        return nodes;
    }

    Map<GTRecipeLookupIngredient, GTRecipeLookup.Node> nodesFor(GTRecipeLookupIngredient ingredient) {
        return getNodes();
    }

    GTRecipeLookup.Node getNode(GTRecipeLookupIngredient ingredient) {
        if (nodes == null) {
            return null;
        }
        return nodes.get(ingredient);
    }

    boolean isEmpty() {
        return nodes == null || nodes.isEmpty();
    }

    void clear() {
        nodes = null;
    }
}
