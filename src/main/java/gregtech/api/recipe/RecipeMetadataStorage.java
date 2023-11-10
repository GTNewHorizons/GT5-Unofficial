package gregtech.api.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Contract;

import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Stores set of metadata of the recipe with key {@link RecipeMetadataKey}. More explicit way to store various info
 * on recipe than special value or special object. Type of the metadata can be anything.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class RecipeMetadataStorage {

    private final Map<RecipeMetadataKey<?>, Object> metadata = new HashMap<>();

    public RecipeMetadataStorage() {}

    private RecipeMetadataStorage(Map<RecipeMetadataKey<?>, Object> metadata) {
        this.metadata.putAll(metadata);
    }

    public <T> void store(RecipeMetadataKey<T> key, @Nullable T value) {
        metadata.put(key, key.cast(value));
    }

    @Nullable
    public <T> T getMetadata(RecipeMetadataKey<T> key) {
        return key.cast(metadata.get(key));
    }

    @Contract("_, !null -> !null")
    @Nullable
    public <T> T getMetadata(RecipeMetadataKey<T> key, @Nullable T defaultValue) {
        return key.cast(metadata.getOrDefault(key, defaultValue));
    }

    public Set<Map.Entry<RecipeMetadataKey<?>, Object>> getEntries() {
        return metadata.entrySet();
    }

    public void clear() {
        metadata.clear();
    }

    public RecipeMetadataStorage copy() {
        return new RecipeMetadataStorage(metadata);
    }
}
