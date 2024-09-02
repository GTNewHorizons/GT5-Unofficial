package gregtech.api.recipe.store;

import java.util.Map;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.store.ingredient.AbstractMapIngredient;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.function.Either;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

final class TrieBranch {

    private Map<AbstractMapIngredient, Either<GT_Recipe, TrieBranch>> nodes;

    public @NotNull Stream<GT_Recipe> getAll() {
        if (nodes == null) {
            return Stream.empty();
        }
        return nodes.values()
            .stream()
            .flatMap(e -> e.map(Stream::of, TrieBranch::getAll));
    }

    public boolean isEmpty() {
        return nodes == null || nodes.isEmpty();
    }

    public @NotNull Map<AbstractMapIngredient, Either<GT_Recipe, TrieBranch>> getNodes() {
        if (nodes == null) {
            nodes = new Object2ObjectOpenHashMap<>(2);
        }
        return nodes;
    }
}
