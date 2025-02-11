package gregtech.api.util;

import gregtech.api.recipe.check.CheckRecipeResult;

public class ProcessingResult {

    private final boolean success;
    private final CheckRecipeResult result;

    private ProcessingResult(boolean success, CheckRecipeResult result) {
        this.success = success;
        this.result = result;
    }

    public static ProcessingResult success(CheckRecipeResult result) {
        return new ProcessingResult(true, result);
    }

    public static ProcessingResult failure(CheckRecipeResult result) {
        return new ProcessingResult(false, result);
    }

    public boolean wasSuccessful() {
        return success;
    }

    public CheckRecipeResult getResult() {
        return result;
    }
}
