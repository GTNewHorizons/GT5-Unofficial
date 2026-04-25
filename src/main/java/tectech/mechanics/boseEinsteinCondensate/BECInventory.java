package tectech.mechanics.boseEinsteinCondensate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import appeng.api.storage.data.IAEFluidStack;

/// Something that stores condensate in a BEC network. AE fluid stacks are used since most BEC machines use longs and
/// not machines for storing condensate. It's very easy to put more than 2.1b condensate in a storage.
@SuppressWarnings("UnstableApiUsage")
public interface BECInventory {

    @Contract(pure = true)
    @NotNull
    CondensateList getContents();

    /// Gets the max amount of condensate this storage can store. This is used to control the distribution weights for
    /// condensate when it is inserted into the network.
    double getCondensateCapacity();

    /// Adds condensate to this storage, and decrements the stack size by the corresponding amount
    @Contract(mutates = "this,param1")
    void addCondensate(IAEFluidStack stack);

    /// Removes condensate from this storage. This operation is not atomic, condensate is removed so long as it is
    /// present, and the stack's amount is decremented by the amount removed.
    @Contract(mutates = "this,param1")
    boolean removeCondensate(IAEFluidStack stack);
}
