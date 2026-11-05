package gregtech.api.util.shutdown;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.recipe.RecipeEnvironment;

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
     * Required processing environment is unavailable
     */
    @Nonnull
    public static final ShutDownReason ENVIRONMENT = SimpleShutDownReason.ofCritical("environment");
    /**
     * Failed to output the pollution.
     */
    @Nonnull
    public static final ShutDownReason POLLUTION_FAIL = SimpleShutDownReason.ofCritical("pollution_fail");
    /**
     * Failed to output the fluids.
     */
    @Nonnull
    public static final ShutDownReason FLUID_OUTPUT_FAILED = SimpleShutDownReason.ofCritical("fluid_output_failed");
    /**
     * Failed to output the items.
     */
    @Nonnull
    public static final ShutDownReason ITEM_OUTPUT_FAILED = SimpleShutDownReason.ofCritical("item_output_failed");
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
     * No valid rotor found.
     */
    @Nonnull
    public static final ShutDownReason NO_ROTOR = SimpleShutDownReason.ofNormal("no_rotor");
    /**
     * Not enough wind.
     */
    @Nonnull
    public static final ShutDownReason WIND_LOW = SimpleShutDownReason.ofNormal("wind_low");
    /**
     * Wind is too strong.
     */
    @Nonnull
    public static final ShutDownReason WIND_HIGH = SimpleShutDownReason.ofNormal("wind_high");
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
     * Shut down due to insufficient dynamo size
     */
    @Nonnull
    public static final ShutDownReason INSUFFICIENT_DYNAMO = SimpleShutDownReason.ofCritical("insufficient_dynamo");

    /**
     * Critical shut down due to overheat.
     */
    @Nonnull
    public static final ShutDownReason OVERHEAT_FAIL = SimpleShutDownReason.ofCritical("overheat_fail");

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

    @Nonnull
    public static ShutDownReason environmentNotMet(Collection<RecipeEnvironment> environments) {
        return new EnvironmentFailure(
            environments.stream()
                .map(RecipeEnvironment::getTranslationKey)
                .collect(Collectors.toList()));
    }

    private static final class EnvironmentFailure extends SimpleShutDownReason {

        private List<String> translationKeys;

        private EnvironmentFailure(Collection<String> translationKeys) {
            super("environment", true);
            this.translationKeys = ImmutableList.copyOf(translationKeys);
        }

        @Nonnull
        @Override
        public String getID() {
            return "environment";
        }

        @Nonnull
        @Override
        public String getDisplayString() {
            String environments = translationKeys.stream()
                .map(StatCollector::translateToLocal)
                .collect(Collectors.joining(", "));
            return StatCollector
                .translateToLocalFormatted("GT5U.gui.text.shutdown_reason.environment_details", environments);
        }

        @Nonnull
        @Override
        public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
            NBTTagList environments = new NBTTagList();
            for (String key : translationKeys) {
                environments.appendTag(new NBTTagString(key));
            }
            tag.setTag("environments", environments);
            return tag;
        }

        @Override
        public void readFromNBT(@Nonnull NBTTagCompound tag) {
            NBTTagList environments = tag.getTagList("environments", Constants.NBT.TAG_STRING);
            ImmutableList.Builder<String> keys = ImmutableList.builder();
            for (int i = 0; i < environments.tagCount(); i++) {
                keys.add(environments.getStringTagAt(i));
            }
            translationKeys = keys.build();
        }

        @Nonnull
        @Override
        public ShutDownReason newInstance() {
            return new EnvironmentFailure(ImmutableList.of());
        }

        @Override
        public void encode(@Nonnull PacketBuffer buffer) {
            ByteBufUtils.writeTag(buffer, writeToNBT(new NBTTagCompound()));
        }

        @Override
        public void decode(PacketBuffer buffer) {
            readFromNBT(ByteBufUtils.readTag(buffer));
        }
    }

    static {
        register(new SimpleShutDownReason("", false));
        register(new EnvironmentFailure(ImmutableList.of()));
        register(new ReasonOutOfFluid(null));
        register(new ReasonOutOfItem(new ItemStack(Items.feather, 1)));
        register(new ReasonOutOfStuff("stuff", 1));
        register(new ReasonMissingCondensate(null));
    }
}
