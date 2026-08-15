package gregtech.common.tileentities.machines;

public interface IHatchWatcher {

    /** Requests a recipe check on the controller's next tick. {@code reason} decides whether it can be throttled. */
    void scheduleRecipeCheck(RecipeCheckReason reason);

    /** Convenience for the common always-run case (new inputs, drained outputs, user/structure changes). */
    default void scheduleRecipeCheckImmediate() {
        scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
    }
}
