package gregtech.common.tileentities.machines;

/**
 * Why a hatch (or module) is asking its controller for a recipe check. This decides whether the check is allowed to be
 * throttled when checks keep failing.
 */
public enum RecipeCheckReason {

    /**
     * A change that should always be acted on next tick: new inputs, a drained output, a CRIB/config change, a user
     * action or a completed structure. Never throttled.
     */
    IMMEDIATE(false),

    /**
     * A change from a source that can fire excessively and whose checks are expensive or usually fail - an ME stocking
     * input restocking, or power trickling into a buffer. These are deferred while a post-failure cooldown is active
     * (see {@code recipeCheckFailCooldown}); with the default cooldown of 0 they still run instantly.
     */
    THROTTLED(true);

    public final boolean throttled;

    RecipeCheckReason(boolean throttled) {
        this.throttled = throttled;
    }
}
