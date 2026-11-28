package gregtech.api.util;

public class RecipeRegistrationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    private final int recipeCount;
    private final int componentCount;

    public RecipeRegistrationException(int recipeCount, int componentCount) {
        super(
            "Recipe component validation found " + recipeCount
                + " recipe definition(s) with "
                + componentCount
                + " invalid component(s). See the suppressed exceptions and recipe diagnostics for details.");
        this.recipeCount = recipeCount;
        this.componentCount = componentCount;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public int getComponentCount() {
        return componentCount;
    }
}
