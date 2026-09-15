package gregtech.api.recipe;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Contract;

import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Unique key for the {@link IRecipeMetadataStorage}. It's also responsible for drawing metadata info on NEI.
 * <p>
 * You can use {@link gregtech.api.recipe.metadata.SimpleRecipeMetadataKey} if your metadata does not need NEI handling.
 *
 * @param <T> Type of the metadata to use.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public abstract class RecipeMetadataKey<T> {

    private static final Set<RecipeMetadataKey<?>> allIdentifiers = new HashSet<>();
    private final Class<T> clazz;
    private final String identifier;

    protected RecipeMetadataKey(Class<T> clazz, String identifier) {
        this.clazz = clazz;
        this.identifier = identifier;
        if (allIdentifiers.contains(this)) {
            throw new IllegalArgumentException(
                "Cannot register metadata key with exact same properties: " + identifier + "@" + clazz);
        }
        allIdentifiers.add(this);
    }

    /**
     * Draws info about the metadata.
     *
     * @param recipeInfo Object to use for drawing text.
     * @param value      Metadata stored in the recipe. Can be safely {@link #cast}ed to the desired type.
     */
    public abstract void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value);

    @Nullable
    public T cast(@Nullable Object o) {
        return clazz.cast(o);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public T cast(@Nullable Object o, @Nullable T defaultValue) {
        T val = cast(o);
        return val != null ? val : defaultValue;
    }

    @Override
    public String toString() {
        return "RecipeMetadataKey{" + "clazz=" + clazz.getName() + ", identifier=" + identifier + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeMetadataKey<?> that = (RecipeMetadataKey<?>) o;

        if (!clazz.equals(that.clazz)) return false;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }
}
