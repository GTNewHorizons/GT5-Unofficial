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
     * Found recipe, but cannot process it because the machine cannot handle its voltage.
     */
    public static CheckRecipeResult insufficientPower(long required) {
        return new ResultInsufficientPower(required);
    }

    static {
        register(SUCCESSFUL);
        register(NO_RECIPE);
        register(OUTPUT_FULL);
        register(NONE);
        register(new ResultInsufficientPower(0));
    }
}
