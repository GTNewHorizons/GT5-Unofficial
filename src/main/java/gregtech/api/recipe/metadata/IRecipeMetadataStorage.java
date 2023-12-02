package gregtech.api.recipe.metadata;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Contract;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Stores set of metadata for the recipe with key {@link RecipeMetadataKey}. More explicit way to store various info
 * on recipe than special value or special object. Type of the metadata can be anything.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IRecipeMetadataStorage {

    <T> void store(RecipeMetadataKey<T> key, @Nullable T value);

    @Nullable
    <T> T getMetadata(RecipeMetadataKey<T> key);

    @Contract("_, !null -> !null")
    @Nullable
    <T> T getMetadataOrDefault(RecipeMetadataKey<T> key, @Nullable T defaultValue);

    Set<Map.Entry<RecipeMetadataKey<?>, Object>> getEntries();

    IRecipeMetadataStorage copy();
}
