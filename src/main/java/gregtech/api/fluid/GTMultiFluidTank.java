package gregtech.api.fluid;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * A fluid tank that can contain more than one fluid stacks.
 * <p>
 * Overrides are used to reuse the existing backends, like mFluid in machines. But be aware that the persistence of
 * backend fields won't be handled by this.
 * <p>
 * And because this is expected to be used internally, the direction parameters are all ignored. If you want to have
 * restriction on it, you need to handle it in the class that uses this.
 *
 * @author Taskeren
 */
public class GTMultiFluidTank implements IFluidTank, IFluidHandler {

    public interface FluidTankSlotView {

        @Nullable
        FluidStack get();

        void set(@Nullable FluidStack fluidStack);

        static FluidTankSlotView of(Supplier<FluidStack> getter, Consumer<FluidStack> setter) {
            return new FluidTankSlotView() {

                @Nullable
                @Override
                public FluidStack get() {
                    return getter.get();
                }

                @Override
                public void set(@Nullable FluidStack fluidStack) {
                    setter.accept(fluidStack);
                }
            };
        }
    }

    protected final int slotCount;

    protected final int capacity;
    protected final @Nullable FluidStack @NotNull [] values;

    protected final Int2ObjectMap<FluidTankSlotView> overrides = new Int2ObjectArrayMap<>();

    protected final @Nullable FluidStackTank[] fluidStackTankCache;

    /**
     * The capacity override
     * <p>
     * It's used as a workaround to the problem that getting the capacity in constructor of the machine may result in an
     * invalid or unexpected value. For example, the child machine can initialize their capacity after the super call,
     * but we've already get the value during the super call. So the child can use override to fix the value.
     * <p>
     * But it's really a bad design, so remove this when the problem got fixed.
     */
    protected @Nullable IntSupplier capacityOverride;

    public GTMultiFluidTank(int capacity, int slots) {
        this.slotCount = slots;
        this.capacity = capacity;

        this.values = new FluidStack[slots];
        this.fluidStackTankCache = new FluidStackTank[slots];
    }

    public void setOverride(int slot, FluidTankSlotView view) {
        overrides.put(slot, view);
    }

    public void setOverride(int slot, Supplier<FluidStack> getter, Consumer<FluidStack> setter) {
        setOverride(slot, FluidTankSlotView.of(getter, setter));
    }

    /**
     * The capacity getter.
     */
    public void setCapacityOverride(IntSupplier capacityOverride) {
        this.capacityOverride = capacityOverride;
    }

    public @Nullable FluidStack get(int slot) {
        FluidTankSlotView override = overrides.get(slot);
        FluidStack fluidStack = override != null ? override.get() : values[slot];
        // check amount, it can be 0-amount fluid because somebody drained it.
        if (fluidStack != null && fluidStack.amount == 0) {
            set(slot, null);
            return null;
        }
        return fluidStack;
    }

    public FluidStack set(int slot, FluidStack stack) {
        FluidTankSlotView override = overrides.get(slot);
        if (stack != null && stack.amount == 0) {
            stack = null;
        }
        if (override != null) override.set(stack);
        else values[slot] = stack;
        return stack;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public @NotNull FluidStackTank getFluidStackTank(int slot) {
        if (fluidStackTankCache[slot] == null) {
            return fluidStackTankCache[slot] = new FluidStackTank(
                () -> get(slot),
                (value) -> set(slot, value),
                this::getCapacity);
        } else {
            return Objects.requireNonNull(fluidStackTankCache[slot]);
        }
    }

    /**
     * @return all contained non-null non-empty fluid stacks.
     */
    public @NotNull FluidStack[] getAllStacks() {
        FluidStack[] ret = new FluidStack[slotCount];
        for (int i = 0; i < slotCount; i++) {
            // using get, so that overrides get applied.
            ret[i] = get(i);
        }
        return ret;
    }

    @ApiStatus.Internal
    public @Nullable FluidStack[] getRawArray() {
        return values;
    }

    public boolean isEmpty() {
        for (FluidStack value : values) {
            if (value != null) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FluidStackList{" + Arrays.stream(values)
            .map(
                fs -> fs != null ? fs.getFluid()
                    .getName() + " x"
                    + fs.amount : "<empty>")
            .collect(Collectors.joining(", ")) + "}";
    }

    // Persistance

    public NBTTagCompound writeData() {
        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < slotCount; i++) {
            // ignore overriden slots
            if (overrides.containsKey(i)) continue;
            FluidStack fluidStack = get(i);
            if (fluidStack == null) continue;
            tag.setTag("Slot" + i, fluidStack.writeToNBT(new NBTTagCompound()));
        }
        return tag;
    }

    public void readData(NBTTagCompound tag) {
        if (tag.hasNoTags()) return;
        for (int i = 0; i < slotCount; i++) {
            // ignore overriden slots
            if (overrides.containsKey(i)) continue;
            if (tag.hasKey("Slot" + i)) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("Slot" + i));
                set(i, fluidStack);
            }
        }
    }

    // Helper

    /**
     * Matches the Fluids.
     *
     * @param wildcard if {@code true}, null is allowed to be used as wildcard matcher on the other.
     */
    private static boolean isFluidEqual(FluidStack self, @Nullable FluidStack other, boolean wildcard) {
        if (self == null) return false;
        // shortcircut when other is null, but wildcard is allowed.
        // allow any non-null fluid for this situation.
        if (other == null) return wildcard;
        return self.isFluidEqual(other);
    }

    // IFluidTank

    @Override
    public FluidStack getFluid() {
        return get(0);
    }

    @Override
    public int getFluidAmount() {
        FluidStack fluidStack = get(0);
        return fluidStack != null ? fluidStack.amount : 0;
    }

    @Override
    public int getCapacity() {
        IntSupplier override = capacityOverride;
        if (override != null) {
            return override.getAsInt();
        }
        return capacity;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return fill(ForgeDirection.UNKNOWN, resource, doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return drain(ForgeDirection.UNKNOWN, maxDrain, doDrain);
    }

    // IFluidHandler

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        // find the same the fluid first
        for (int i = 0; i < slotCount; i++) {
            FluidStack curr = get(i);
            if (isFluidEqual(curr, resource, false)) {
                int capacity = getCapacity();
                // get the maximum amount to fill the slot (0 <= toFill <= capacity - curr.amount)
                int toFill = MathHelper.clamp_int(resource.amount, 0, capacity - curr.amount);
                if (doFill) {
                    curr.amount += toFill;
                    resource.amount -= toFill;
                }
                return toFill;
            }
        }
        // find a empty slot second
        for (int i = 0; i < slotCount; i++) {
            if (get(i) == null) {
                int toFill = MathHelper.clamp_int(resource.amount, 0, getCapacity() - i);
                if (doFill) {
                    FluidStack copy = resource.copy();
                    copy.amount = toFill;
                    set(i, copy);
                    resource.amount -= toFill;
                }
                return toFill;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        for (int i = 0; i < slotCount; i++) {
            FluidStack fluidStack = get(i);
            if (fluidStack != null && fluidStack.isFluidEqual(resource)) {
                int toDrain = MathHelper.clamp_int(resource.amount, 0, fluidStack.amount);
                if (doDrain) {
                    fluidStack.amount -= toDrain;
                }
                FluidStack copy = fluidStack.copy();
                copy.amount = toDrain;
                return copy;
            }
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        for (int i = 0; i < slotCount; i++) {
            FluidStack fluidStack = get(i);
            if (fluidStack != null) {
                int toDrain = MathHelper.clamp_int(maxDrain, 0, fluidStack.amount);
                if (doDrain) {
                    fluidStack.amount -= toDrain;
                }
                FluidStack copy = fluidStack.copy();
                copy.amount = toDrain;
                return copy;
            }
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        for (int i = 0; i < slotCount; i++) {
            // if any slot can accept at least 1mb of given fluid
            if (fill(from, new FluidStack(fluid, 1), false) > 0) return true;
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        for (int i = 0; i < slotCount; i++) {
            // if any slot can drain at any amount of given fluid
            if (drain(from, new FluidStack(fluid, 1), false) != null) return true;
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] ret = new FluidTankInfo[slotCount];
        for (int i = 0; i < slotCount; i++) {
            ret[i] = new FluidTankInfo(get(i), getCapacity());
        }
        return ret;
    }
}
