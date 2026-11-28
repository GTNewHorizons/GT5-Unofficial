package gregtech.api.ec;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.IFluidInputLogicComponent;
import gregtech.api.ec.component.IItemInputLogicComponent;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

public interface IECMachine {

    <T extends IComponent> @Nullable T tryGetComponent(@NotNull ComponentKey.UniqueKey<T> key);

    default <T extends IComponent> @NotNull T getComponent(@NotNull ComponentKey.UniqueKey<T> key) {
        T component = tryGetComponent(key);
        if (component == null) {
            throw new IllegalStateException("Missing component: " + key.interfaceType.getName());
        }
        return component;
    }

    <T extends IComponent> boolean hasComponent(@NotNull ComponentKey<T> key);

    <T extends IComponent> @NotNull T[] getComponents(@NotNull ComponentKey.ListKey<T> key);

    <T extends IComponent> IECMachine addComponent(@NotNull T component);

    <T extends IComponent> boolean removeComponent(@NotNull T component);

    default <T extends IUniqueComponent> boolean removeComponent(@NotNull ComponentKey.UniqueKey<T> key) {
        T comp = tryGetComponent(key);
        return comp != null && removeComponent(comp);
    }

    <T extends IComponent> boolean removeComponent(@NotNull ComponentKey.ListKey<T> key);

    void markDirty();

    int getRandomNumber(int aRange);

    void disableWorking();

    void enableWorking();

    void sendLoopStart(byte aIndex);

    IGregTechTileEntity getBaseMetaTileEntity();

    ItemStack getMachineCraftingIcon();

    boolean isActive();

    default long depleteFluidRange(@NotNull FluidStack fluid, long minAmount, long maxAmount, boolean simulate) {
        if (fluid.getFluid() == null || minAmount <= 0 || maxAmount < minAmount) return 0;
        var comps = getComponents(IFluidInputLogicComponent.KEY);
        if (comps.length == 0) return 0;
        if (comps.length == 1) {
            return comps[0].depleteFluidRange(fluid, minAmount, maxAmount, simulate);
        }
        if (minAmount == 1) {
            long remaining = maxAmount;
            for (var comp : comps) {
                remaining -= comp.depleteFluidPartial(fluid, remaining, simulate);
                if (remaining <= 0) return maxAmount;
            }
            return maxAmount - remaining;
        }
        long available = 0;
        for (var comp : comps) {
            available += comp.depleteFluidPartial(fluid, maxAmount - available, true);
            if (available >= maxAmount) break;
        }
        if (available < minAmount) return 0;
        long toDec = Math.min(available, maxAmount);
        if (simulate) return toDec;
        long remaining = toDec;
        for (var comp : comps) {
            long amount = comp.depleteFluidPartial(fluid, remaining, false);
            remaining -= amount;
            if (remaining <= 0) return toDec;
        }
        return toDec - remaining;
    }

    default long depleteFluidPartial(FluidStack aLiquid, long maxAmount, boolean simulate) {
        if (aLiquid.getFluid() == null || maxAmount <= 0) return 0;
        return depleteFluidRange(aLiquid, 1, maxAmount, simulate);
    }

    default boolean depleteFluidAtomic(FluidStack aLiquid, long minAmount, boolean simulate) {
        if (aLiquid.getFluid() == null || minAmount <= 0) return false;
        return depleteFluidRange(aLiquid, minAmount, minAmount, simulate) >= minAmount;
    }

    default @Nullable FluidStack depleteFluidRange(@NotNull Predicate<FluidStack> filter, long minAmount,
        long maxAmount, boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return null;
        var comps = getComponents(IFluidInputLogicComponent.KEY);
        if (comps.length == 0) return null;
        if (comps.length == 1) {
            return comps[0].depleteFluidRange(filter, minAmount, maxAmount, simulate);
        }
        final Set<GTUtility.FluidId> tried = new HashSet<>();
        for (var comp : comps) {
            var target = comp.depleteFluidAtomic(fluid -> {
                if (!filter.test(fluid)) return false;
                var id = GTUtility.FluidId.createNoCopy(fluid);
                if (tried.contains(id)) return false;
                if (depleteFluidAtomic(fluid, minAmount, true)) {
                    return true;
                }
                tried.add(id);
                return false;
            }, 1, true);
            if (target != null) {
                long amount = depleteFluidRange(target, minAmount, maxAmount, simulate);
                if (amount >= minAmount) {
                    return GTUtility.createFluidStack(target, amount);
                }
            }
        }
        return null;
    }

    default @Nullable FluidStack depleteFluidAtomic(@NotNull Predicate<FluidStack> filter, long minAmount,
        boolean simulate) {
        return depleteFluidRange(filter, minAmount, minAmount, simulate);
    }

    default @Nullable FluidStack depleteFluidPartial(@NotNull Predicate<FluidStack> filter, long maxAmount,
        boolean simulate) {
        return depleteFluidRange(filter, 1, maxAmount, simulate);
    }

    default long depleteItemRange(@NotNull ItemStack item, long minAmount, long maxAmount, boolean simulate) {
        if (item.getItem() == null || minAmount <= 0 || maxAmount < minAmount) return 0;
        var comps = getComponents(IItemInputLogicComponent.KEY);
        if (comps.length == 0) return 0;
        if (comps.length == 1) {
            return comps[0].depleteItemRange(item, minAmount, maxAmount, simulate);
        }
        if (minAmount == 1) {
            long remaining = maxAmount;
            for (var comp : comps) {
                remaining -= comp.depleteItemPartial(item, remaining, simulate);
                if (remaining <= 0) return maxAmount;
            }
            return maxAmount - remaining;
        }
        long available = 0;
        for (var comp : comps) {
            available += comp.depleteItemPartial(item, maxAmount - available, true);
            if (available >= maxAmount) break;
        }
        if (available < minAmount) return 0;
        long toDec = Math.min(available, maxAmount);
        if (simulate) return toDec;
        long remaining = toDec;
        for (var comp : comps) {
            long amount = comp.depleteItemPartial(item, remaining, false);
            remaining -= amount;
            if (remaining <= 0) return toDec;
        }
        return toDec - remaining;
    }

    default long depleteItemPartial(@NotNull ItemStack item, long maxAmount, boolean simulate) {
        if (item.getItem() == null || maxAmount <= 0) return 0;
        return depleteItemRange(item, 1, maxAmount, simulate);
    }

    default boolean depleteItemAtomic(@NotNull ItemStack item, long minAmount, boolean simulate) {
        if (item.getItem() == null || minAmount <= 0) return false;
        return depleteItemRange(item, minAmount, minAmount, simulate) >= minAmount;
    }

    default @Nullable ItemStack depleteItemRange(@NotNull Predicate<ItemStack> filter, long minAmount, long maxAmount,
        boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return null;
        var comps = getComponents(IItemInputLogicComponent.KEY);
        if (comps.length == 0) return null;
        if (comps.length == 1) {
            return comps[0].depleteItemRange(filter, minAmount, maxAmount, simulate);
        }
        final Set<GTUtility.ItemId> tried = new HashSet<>();
        for (var comp : comps) {
            var target = comp.depleteItemAtomic(item -> {
                if (!filter.test(item)) return false;
                var id = GTUtility.ItemId.createNoCopy(item);
                if (tried.contains(id)) return false;
                if (depleteItemAtomic(item, minAmount, true)) {
                    return true;
                }
                tried.add(id);
                return false;
            }, 1, true);
            if (target != null) {
                long amount = depleteItemRange(target, minAmount, maxAmount, simulate);
                if (amount >= minAmount) {
                    // todo support long
                    var result = target.copy();
                    result.stackSize = (int) Math.min(amount, Integer.MAX_VALUE);
                    return result;
                }
            }
        }
        return null;
    }

    default @Nullable ItemStack depleteItemAtomic(@NotNull Predicate<ItemStack> filter, long minAmount,
        boolean simulate) {
        return depleteItemRange(filter, minAmount, minAmount, simulate);
    }

    default @Nullable ItemStack depleteItemPartial(@NotNull Predicate<ItemStack> filter, long maxAmount,
        boolean simulate) {
        return depleteItemRange(filter, 1, maxAmount, simulate);
    }
}
