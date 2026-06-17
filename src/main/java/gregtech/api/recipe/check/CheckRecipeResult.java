package gregtech.api.recipe.check;

import gregtech.api.util.IMachineMessage;

/**
 * Class to indicate the result of recipe check in the machine. It doesn't need to be actual result of recipemap check,
 * but can also be status of whether to start the machine. Examples can be found at {@link CheckRecipeResultRegistry}.
 * <p>
 * Sample instance must be registered to {@link CheckRecipeResultRegistry}.
 */
public interface CheckRecipeResult extends IMachineMessage<CheckRecipeResult> {

    /**
     * @return If recipe check is successful
     */
    boolean wasSuccessful();

    /**
     * @return If this message should stay on GUI when the machine is shut down.
     */
    default boolean persistsOnShutdown() {
        return false;
    }

    /**
     * @return If this failure is due to insufficient power/voltage. Power accumulation has no event to push from, so an
     *         idle controller blocked on power polls its stored energy until it recovers; this flags when to do so.
     */
    default boolean isInsufficientPower() {
        return false;
    }
}
