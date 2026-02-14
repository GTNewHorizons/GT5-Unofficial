package gregtech.common.items.toolbox.pickblock;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolboxSlot;

/**
 * An action that requires another mod to function. Used to isolate optional mod related pick block actions.
 */
abstract public class DependentAction implements IDeciderAction {

    private final Mods requiredMod;
    private final SimpleAction<?> delegate;

    protected DependentAction(final Mods requiredMod, final SimpleAction<?> delegate) {
        this.requiredMod = requiredMod;
        this.delegate = delegate;
    }

    @Override
    public boolean isValid(final Object obj) {
        return requiredMod.isModLoaded() && delegate.isValid(obj);
    }

    @Override
    public List<ToolboxSlot> apply(final Object obj, final ForgeDirection side) {
        return delegate.apply(obj, side);
    }
}
