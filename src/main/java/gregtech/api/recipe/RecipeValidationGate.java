package gregtech.api.recipe;

public final class RecipeValidationGate {

    public static final String GATE_MARKER = "GT recipe validation gate";

    private RecipeValidationGate() {}

    public static IllegalStateException intentionalShutdown(String reason) {
        return new IllegalStateException(GATE_MARKER + ": " + reason + " (intentional shutdown).");
    }
}
