package gregtech.api.enums;

public class CheckRecipeResults {

    public static final CheckRecipeResult SUCCESSFUL = CheckRecipeResult.ofSuccess("GT5U.gui.text.processing");
    public static final CheckRecipeResult NO_RECIPE = CheckRecipeResult.ofFailure("GT5U.gui.text.no_recipe");
    public static final CheckRecipeResult OUTPUT_FULL = CheckRecipeResult.ofFailure("GT5U.gui.text.output_full");
}
