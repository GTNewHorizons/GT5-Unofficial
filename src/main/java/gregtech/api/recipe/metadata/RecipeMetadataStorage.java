package gregtech.api.recipe.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Contract;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class RecipeMetadataStorage implements IRecipeMetadataStorage {

    private final Map<RecipeMetadataKey<?>, Object> metadata = new HashMap<>();

    public RecipeMetadataStorage() {}

    private RecipeMetadataStorage(Map<RecipeMetadataKey<?>, Object> metadata) {
        this.metadata.putAll(metadata);
    }

    @Override
    public <T> void store(RecipeMetadataKey<T> key, @Nullable T value) {
        metadata.put(key, key.cast(value));
    }

    @Nullable
    @Override
    public <T> T getMetadata(RecipeMetadataKey<T> key) {
        return key.cast(metadata.get(key));
    }

    @Contract("_, !null -> !null")
    @Nullable
    @Override
    public <T> T getMetadataOrDefault(RecipeMetadataKey<T> key, @Nullable T defaultValue) {
        return key.cast(metadata.getOrDefault(key, defaultValue));
    }

    @Override
    public Set<Map.Entry<RecipeMetadataKey<?>, Object>> getEntries() {
        return metadata.entrySet();
    }

    @Override
    public IRecipeMetadataStorage copy() {
        return new RecipeMetadataStorage(metadata);
    }
}
