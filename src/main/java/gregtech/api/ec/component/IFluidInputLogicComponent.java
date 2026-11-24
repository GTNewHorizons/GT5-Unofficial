package gregtech.api.ec.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.util.GTUtility;

public interface IFluidInputLogicComponent extends IComponent {

    ComponentKey.ListKey<IFluidInputLogicComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IFluidInputLogicComponent::getFluidInputPriority));

    enum FluidInputPriority {

        STANDARD_BUS(1000);

        public final int priority;

        FluidInputPriority(int priority) {
            this.priority = priority;
        }
    }

    int getFluidInputPriority();

    long depleteFluidRange(@NotNull FluidStack stack, long minAmount, long maxAmount, boolean simulate);

    default boolean depleteFluidAtomic(@NotNull FluidStack stack, long amount, boolean simulate) {
        return depleteFluidRange(stack, amount, amount, simulate) >= amount;
    }

    default long depleteFluidPartial(@NotNull FluidStack stack, long maxAmount, boolean simulate) {
        return depleteFluidRange(stack, 1, maxAmount, simulate);
    }

    @Nullable
    FluidStack depleteFluidRange(@NotNull Predicate<FluidStack> filter, long minAmount, long maxAmount,
        boolean simulate);

    default @Nullable FluidStack depleteFluidAtomic(Predicate<FluidStack> filter, long amount, boolean simulate) {
        return depleteFluidRange(filter, amount, amount, simulate);
    }

    default @Nullable FluidStack depleteFluidPartial(Predicate<FluidStack> filter, long maxAmount, boolean simulate) {
        return depleteFluidRange(filter, 1, maxAmount, simulate);
    }

    default void appendStoredFluids(@NotNull List<FluidStack> list) {
        appendStoredFluidsForColor(list, null);
    }

    void appendStoredFluidsForColor(@NotNull List<FluidStack> list, @Nullable Byte color);

    default List<FluidStack> getStoredFluidsForColor(@Nullable Byte color) {
        List<FluidStack> list = new ArrayList<>();
        appendStoredFluidsForColor(list, color);
        return list;
    }

    static long depleteFromFluidList(@NotNull List<FluidStack> list, @NotNull FluidStack target, long minAmount,
        long maxAmount, boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return 0;
        long available = 0;
        for (var fluid : list) {
            long fluidAmount = GTUtility.getFluidAmount(fluid);
            if (fluidAmount > 0 && GTUtility.areFluidsEqual(fluid, target)) {
                available = GTUtility.addSafe(available, fluidAmount);
                if (available >= maxAmount) break;
            }
        }
        if (available < minAmount) return 0;
        long toDec = Math.min(available, maxAmount);
        if (simulate) {
            return toDec;
        }
        long remaining = toDec;
        for (var fluid : list) {
            long fluidAmount = GTUtility.getFluidAmount(fluid);
            if (fluidAmount > 0 && GTUtility.areFluidsEqual(fluid, target)) {
                long decAmount = Math.min(remaining, fluidAmount);
                GTUtility.decFluidAmount(fluid, decAmount);
                remaining -= decAmount;
                if (remaining <= 0) break;
            }
        }
        return toDec;
    }

    static @Nullable FluidStack depleteFromFluidList(@NotNull List<FluidStack> list,
        @NotNull Predicate<FluidStack> filter, long minAmount, long maxAmount, boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return null;
        Set<GTUtility.FluidId> failedFluids = null;
        for (var fluid : list) {
            long fluidAmount = GTUtility.getFluidAmount(fluid);
            if (fluidAmount > 0) {
                GTUtility.FluidId id = null;
                if (failedFluids != null) {
                    id = GTUtility.FluidId.createNoCopy(fluid);
                    if (failedFluids.contains(id)) continue;
                }
                if (filter.test(fluid)) {
                    long amount = depleteFromFluidList(list, fluid, minAmount, maxAmount, simulate);
                    if (amount >= minAmount) {
                        return GTUtility.createFluidStack(fluid, amount);
                    }
                    if (failedFluids == null) failedFluids = new HashSet<>();
                    if (id == null) id = GTUtility.FluidId.createNoCopy(fluid);
                    failedFluids.add(id);
                }
            }
        }
        return null;
    }
}
