package gregtech.api.enums;

public class CheckRecipeResult {

    private final boolean success;
    private final String transKey;

    private CheckRecipeResult(boolean success, String transKey) {
        this.success = success;
        this.transKey = transKey;
    }

    public String getTransKey() {
        return transKey;
    }

    public boolean wasSuccessful() {
        return success;
    }

    public static CheckRecipeResult ofSuccess(String transKey) {
        return new CheckRecipeResult(true, transKey);
    }

    public static CheckRecipeResult ofFailure(String transKey) {
        return new CheckRecipeResult(false, transKey);
    }
}
