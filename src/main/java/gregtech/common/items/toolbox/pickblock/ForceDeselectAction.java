package gregtech.common.items.toolbox.pickblock;

import java.util.function.Predicate;

/**
 * Allows the {@link gregtech.common.items.toolbox.ToolboxPickBlockDecider} to define blocks that will suggest
 * deselecting any active tool that's currently equipped.
 */
public class ForceDeselectAction<V> {

    private final Class<V> targetClass;
    private final Predicate<V> predicate;

    public ForceDeselectAction(final Class<V> targetClass, final Predicate<V> predicate) {
        this.targetClass = targetClass;
        this.predicate = predicate;
    }

    public boolean test(Object mte) {
        if (targetClass.isInstance(mte)) {
            return predicate.test(targetClass.cast(mte));
        }

        return false;
    }
}
