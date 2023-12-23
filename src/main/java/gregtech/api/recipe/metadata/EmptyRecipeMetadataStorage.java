package gregtech.api.recipe.metadata;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Contract;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class EmptyRecipeMetadataStorage implements IRecipeMetadataStorage {

    public static EmptyRecipeMetadataStorage INSTANCE = new EmptyRecipeMetadataStorage();

    private EmptyRecipeMetadataStorage() {}

    @Override
    public <T> void store(RecipeMetadataKey<T> key, @Nullable T value) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public <T> T getMetadata(RecipeMetadataKey<T> key) {
        return null;
    }

    @Contract("_, !null -> !null")
    @Nullable
    @Override
    public <T> T getMetadataOrDefault(RecipeMetadataKey<T> key, @Nullable T defaultValue) {
        return defaultValue;
    }

    @Override
    public Set<Map.Entry<RecipeMetadataKey<?>, Object>> getEntries() {
        return Collections.emptySet();
    }

    @Override
    public IRecipeMetadataStorage copy() {
        throw new UnsupportedOperationException();
    }
}
