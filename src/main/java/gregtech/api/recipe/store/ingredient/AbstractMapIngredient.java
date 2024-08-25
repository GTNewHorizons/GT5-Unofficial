package gregtech.api.recipe.store.ingredient;

public abstract class AbstractMapIngredient {

    private int hash;
    private boolean hashed;

    protected AbstractMapIngredient() {}

    /**
     * @return the hashcode of this ingredient
     */
    protected abstract int hash();

    @Override
    public final int hashCode() {
        if (!hashed) {
            this.hash = hash();
            this.hashed = true;
        }
        return hash;
    }
}
