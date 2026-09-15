package gregtech.common;

import gregtech.api.interfaces.internal.IGTRecipeAdder;
import gregtech.api.util.GTRecipeBuilder;

public class RecipeAdder implements IGTRecipeAdder {

    @Override
    public GTRecipeBuilder stdBuilder() {
        return GTRecipeBuilder.builder();
    }
}
