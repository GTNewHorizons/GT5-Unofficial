package gregtech.common.items.toolbox.pickblock;

import java.util.function.BiPredicate;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Allows the {@link gregtech.common.items.toolbox.ToolboxPickBlockDecider} to define blocks that will suggest
 * deselecting any active tool that's currently equipped.
 */
public class ForceDeselectAction<V> {

    private final Class<V> targetClass;
    private final BiPredicate<V, ForgeDirection> predicate;

    public ForceDeselectAction(final Class<V> targetClass, final BiPredicate<V, ForgeDirection> predicate) {
        this.targetClass = targetClass;
        this.predicate = predicate;
    }

    public boolean test(Object mte, ForgeDirection side) {
        if (targetClass.isInstance(mte)) {
            return predicate.test(targetClass.cast(mte), side);
        }

        return false;
    }
}
