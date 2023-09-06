package gregtech.api.recipe.check;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public final class CheckRecipeResultRegistry {

    private static final Map<String, CheckRecipeResult> registry = new HashMap<>();

    /**
     * Registers CheckRecipeResult. No duplicated IDs are allowed.
     *
     * @param sample Sample object to register
     */
    public static void register(CheckRecipeResult sample) {
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

    public static CheckRecipeResult getSampleFromRegistry(String id) {
        if (!isRegistered(id)) {
            throw new RuntimeException("Unknown id: " + id);
        }
        return registry.get(id);
    }

    public static boolean isRegistered(String id) {
        return registry.containsKey(id);
    }

    /**
     * Successfully found recipe.
     */
    @Nonnull
    public static final CheckRecipeResult SUCCESSFUL = SimpleCheckRecipeResult.ofSuccess("success");
    /**
     * All requirements met to generator power.
     */
    @Nonnull
    public static final CheckRecipeResult GENERATING = SimpleCheckRecipeResult.ofSuccess("generating");
    /**
     * Cannot find recipe.
     */
    @Nonnull
    public static final CheckRecipeResult NO_RECIPE = SimpleCheckRecipeResult.ofFailure("no_recipe");
    /**
     * Cannot process recipe because output is full.
     */
    @Nonnull
    public static final CheckRecipeResult OUTPUT_FULL = SimpleCheckRecipeResult.ofFailure("output_full");
    /**
     * Default unknown state.
     */
    @Nonnull
    public static final CheckRecipeResult NONE = SimpleCheckRecipeResult.ofFailure("none");
    /**
     * Code crashed.
     */
    @Nonnull
    public static final CheckRecipeResult CRASH = SimpleCheckRecipeResult.ofFailure("crash");
    /**
     * Cannot find valid fuel for generator.
     */
    @Nonnull
    public static final CheckRecipeResult NO_FUEL_FOUND = SimpleCheckRecipeResult.ofFailure("no_fuel");
    /**
     * Cannot find valid turbine.
     */
    @Nonnull
    public static final CheckRecipeResult NO_TURBINE_FOUND = SimpleCheckRecipeResult.ofFailure("no_turbine");
    /**
     * No data sticks found for Assembly Line.
     */
    @Nonnull
    public static final CheckRecipeResult NO_DATA_STICKS = SimpleCheckRecipeResult.ofFailure("no_data_sticks");
    /**
     * EU/t overflowed.
     */
    @Nonnull
    public static final CheckRecipeResult POWER_OVERFLOW = SimpleCheckRecipeResult.ofFailure("power_overflow");
    /**
     * Progress time overflowed.
     */
    @Nonnull
    public static final CheckRecipeResult DURATION_OVERFLOW = SimpleCheckRecipeResult.ofFailure("duration_overflow");
    /**
     * Machine had an internal error
     */
    @Nonnull
    public static final CheckRecipeResult INTERNAL_ERROR = SimpleCheckRecipeResult.ofFailure("internal_error");

    /**
     * Cannot process recipe because the machine cannot handle required EUt.
     */
    @Nonnull
    public static CheckRecipeResult insufficientPower(long required) {
        return new ResultInsufficientPower(required);
    }

    /**
     * Cannot process recipe because the machine cannot handle its heat.
     */
    @Nonnull
    public static CheckRecipeResult insufficientHeat(int required) {
        return new ResultInsufficientHeat(required);
    }

    /**
     * Cannot process recipe because the machine is tiered and its tier is too low.
     */
    @Nonnull
    public static CheckRecipeResult insufficientMachineTier(int required) {
        return new ResultInsufficientMachineTier(required);
    }

    /**
     * Cannot process recipe because the machine doesn't have enough startup power.
     */
    @Nonnull
    public static CheckRecipeResult insufficientStartupPower(int required) {
        return new ResultInsufficientStartupPower(required);
    }

    static {
        register(new SimpleCheckRecipeResult(false, ""));
        register(new ResultInsufficientPower(0));
        register(new ResultInsufficientHeat(0));
        register(new ResultInsufficientMachineTier(0));
        register(new ResultInsufficientStartupPower(0));
    }
}
