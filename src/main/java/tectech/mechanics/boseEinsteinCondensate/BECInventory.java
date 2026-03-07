package tectech.mechanics.boseEinsteinCondensate;

import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import appeng.api.storage.data.IAEFluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

@SuppressWarnings("UnstableApiUsage")
public interface BECInventory {

    @Contract(pure = true)
    @NotNull
    Object2LongMap<Fluid> getContents();

    /// Adds condensate to this storage, and decrements the stack size by the corresponding amount
    @Contract(mutates = "this,param1")
    void addCondensate(IAEFluidStack stack);

    /// Removes condensate from this storage. This operation is not atomic, condensate is removed so long as it is
    /// present, and the stack's amount is decremented by the amount removed.
    @Contract(mutates = "this,param1")
    boolean removeCondensate(IAEFluidStack stack);
}
