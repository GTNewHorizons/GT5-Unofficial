package gregtech.common.gui.modularui.widget;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

public class FluidDisplaySyncHandler extends ValueSyncHandler<FluidStack> {

    public static boolean isFluidEmpty(@Nullable FluidStack fluidStack) {
        return fluidStack == null || fluidStack.amount <= 0;
    }

    @Nullable
    public static FluidStack copyFluid(@Nullable FluidStack fluidStack) {
        return isFluidEmpty(fluidStack) ? null : fluidStack.copy();
    }

    @Nullable
    private FluidStack cache;
    public Supplier<FluidStack> getter;
    private boolean controlsAmount;

    public FluidDisplaySyncHandler(Supplier<FluidStack> getter) {
        this.getter = getter;
    }

    public FluidDisplaySyncHandler controlsAmount(boolean controlsAmount) {
        this.controlsAmount = controlsAmount;
        return this;
    }

    public boolean controlsAmount() {
        return this.controlsAmount;
    }

    @Nullable
    @Override
    public FluidStack getValue() {
        return this.cache == null ? null : this.cache.copy();
    }

    @Override
    public void setValue(@Nullable FluidStack value, boolean setSource, boolean sync) {
        this.cache = copyFluid(value);
        if (sync) {
            if (NetworkUtils.isClient()) {
                syncToServer(0, this::write);
            } else {
                syncToClient(0, this::write);
            }
        }
        onValueChanged();
    }

    public boolean needsSync() {
        FluidStack current = this.getter.get();
        if (current == this.cache) return false;
        if (current == null || this.cache == null) return true;
        return current.amount != this.cache.amount || !current.isFluidEqual(this.cache);
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        if (isFirstSync || needsSync()) {
            setValue(this.getter.get(), false, false);
            return true;
        }
        return false;
    }

    @Override
    public void write(PacketBuffer buffer) {
        NetworkUtils.writeFluidStack(buffer, this.cache);
    }

    @Override
    public void read(PacketBuffer buffer) {
        setValue(NetworkUtils.readFluidStack(buffer), true, false);
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {
        if (id == 0) {
            read(buf);
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) {
        if (id == 0) {
            read(buf);
        }
    }

    /**
     * In 1.7.10 placing water or lava does not play sound, so we do nothing here.
     * Override if you want to play something.
     */
    private void playSound(FluidStack fluid, boolean fill) {}
}
