package gregtech.api.util.shutdown;

import static gregtech.api.util.GTModHandler.getWater;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ShutDownReasonRegistry {

    private static final Map<String, ShutDownReason> registry = new HashMap<>();

    /**
     * Registers ShutDownReason. No duplicated IDs are allowed.
     *
     * @param sample Sample object to register
     */
    public static void register(ShutDownReason sample) {
        if (isRegistered(sample.getID())) {
            throw new IllegalStateException(
                String.format(
                    "ID %s is already registered for %s",
                    sample.getID(),
                    registry.get(sample.getID())
                        .getClass()
                        .getCanonicalName()));
        }
        registry.put(sample.getID(), sample);
    }

    public static ShutDownReason getSampleFromRegistry(String id) {
        if (!isRegistered(id)) {
            throw new RuntimeException("Unknown id: " + id);
        }
        return registry.get(id);
    }

    public static boolean isRegistered(String id) {
        return registry.containsKey(id);
    }

    /**
     * Shut down due to power loss.
     */
    @Nonnull
    public static final ShutDownReason POWER_LOSS = SimpleShutDownReason.ofCritical("power_loss");
    /**
     * Failed to output the pollution.
     */
    @Nonnull
    public static final ShutDownReason POLLUTION_FAIL = SimpleShutDownReason.ofCritical("pollution_fail");
    /**
     * Shut down due to incomplete structure.
     */
    @Nonnull
    public static final ShutDownReason STRUCTURE_INCOMPLETE = SimpleShutDownReason.ofNormal("structure_incomplete");
    /**
     * Shut down due to machine damage.
     */
    @Nonnull
    public static final ShutDownReason NO_REPAIR = SimpleShutDownReason.ofNormal("no_repair");
    /**
     * No valid turbine found.
     */
    @Nonnull
    public static final ShutDownReason NO_TURBINE = SimpleShutDownReason.ofNormal("no_turbine");
    /**
     * No correct machine part in controller slot.
     */
    @Nonnull
    public static final ShutDownReason NO_MACHINE_PART = SimpleShutDownReason.ofNormal("no_machine_part");
    /**
     * Default unknown state.
     */
    @Nonnull
    public static final ShutDownReason NONE = SimpleShutDownReason.ofNormal("none");
    /**
     * Critical unknown state.
     */
    @Nonnull
    public static final ShutDownReason CRITICAL_NONE = SimpleShutDownReason.ofCritical("none");

    /**
     * Fluid that needs to be constantly supplied are out. E.g. PCB coolant with cooling upgrades enabled.
     */
    @Nonnull
    public static ShutDownReason outOfFluid(@Nonnull FluidStack required) {
        return new ReasonOutOfFluid(required);
    }

    /**
     * Item that needs to be constantly supplied are out.
     */
    @Nonnull
    public static ShutDownReason outOfItem(@Nonnull ItemStack required) {
        return new ReasonOutOfItem(required);
    }

    /**
     * Stuff that needs to be constantly supplied are out.
     */
    @Nonnull
    public static ShutDownReason outOfStuff(@Nonnull String required, int amount) {
        return new ReasonOutOfStuff(required, amount);
    }

    static {
        register(new SimpleShutDownReason("", false));
        register(new ReasonOutOfFluid(getWater(0)));
        register(new ReasonOutOfItem(new ItemStack(Items.feather, 1)));
        register(new ReasonOutOfStuff("stuff", 1));
    }
}
