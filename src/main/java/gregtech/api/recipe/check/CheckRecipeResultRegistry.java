package gregtech.api.recipe.check;

import java.util.HashMap;
import java.util.Map;

public class CheckRecipeResultRegistry {

    private static final Map<String, CheckRecipeResult> registry = new HashMap<>();

    /**
     * Registers CheckRecipeResult. No duplicated IDs are allowed.
     *
     * @param sample Sample object to register
     */
    public static void register(CheckRecipeResult sample) {
        if (registry.containsKey(sample.getID())) {
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
        if (!registry.containsKey(id)) {
            throw new RuntimeException("Unknown id: " + id);
        }
        return registry.get(id);
    }

    /**
     * Successfully found recipe.
     */
    public static final CheckRecipeResult SUCCESSFUL = SimpleCheckRecipeResult.ofSuccessFactory("success");
    /**
     * Cannot find recipe.
     */
    public static final CheckRecipeResult NO_RECIPE = SimpleCheckRecipeResult.ofFailureFactory("no_recipe");
    /**
     * Found recipe, but cannot process it because output is full.
     */
    public static final CheckRecipeResult OUTPUT_FULL = SimpleCheckRecipeResult.ofFailureFactory("output_full");
    /**
     * Default unknown state.
     */
    public static final CheckRecipeResult NONE = SimpleCheckRecipeResult.ofFailureFactory("none");
    /**
     * Found recipe, but cannot process it because bio upgrade is missing.
     */
    public static final CheckRecipeResult BIO_UPGRADE_MISSING = SimpleCheckRecipeResult
        .ofFailureFactory("bio_upgrade_missing");

    /**
     * Found recipe, but cannot process it because the machine cannot handle its voltage.
     */
    public static CheckRecipeResult insufficientPower(long required) {
        return new ResultInsufficientPower(required);
    }

    /**
     * Found recipe, but cannot process it because the machine cannot handle its heat.
     */
    public static CheckRecipeResult insufficientHeat(int required) {
        return new ResultInsufficientHeat(required);
    }

    /**
     * Found recipe, but cannot process it because the machine is tiered and its tier is too low.
     */
    public static CheckRecipeResult insufficientMachineTier(long required) {
        return new ResultInsufficientMachineTier(required);
    }

    static {
        register(SUCCESSFUL);
        register(NO_RECIPE);
        register(OUTPUT_FULL);
        register(NONE);
        register(BIO_UPGRADE_MISSING);
        register(new ResultInsufficientPower(0));
        register(new ResultInsufficientHeat(0));
        register(new ResultInsufficientMachineTier(0));
    }
}
