package gregtech.api.recipe.lookup;

/**
 * Base class for recipe lookup keys.
 * <p>
 * Equality is intentionally class-scoped. Item, ore-dictionary, and fluid keys may collide by hash, but they must never
 * compare equal across key types.
 */
public abstract class GTRecipeLookupIngredient {

    private final int hashCode;

    protected GTRecipeLookupIngredient(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        return equalsSameClass((GTRecipeLookupIngredient) obj);
    }

    protected abstract boolean equalsSameClass(GTRecipeLookupIngredient other);

    public boolean isSpecialIngredient() {
        return false;
    }
}
