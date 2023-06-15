package gregtech.api.enums;

public enum CheckRecipeResults implements CheckRecipeResult {

    SUCCESSFUL("GT5U.gui.text.processing"),
    NO_RECIPE("GT5U.gui.text.no_recipe"),
    OUTPUT_FULL("GT5U.gui.text.output_full");

    private final String displayString;

    CheckRecipeResults(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String getResultDisplayString() {
        return displayString;
    }

    @Override
    public boolean wasSuccessful() {
        return this.equals(SUCCESSFUL);
    }
}
