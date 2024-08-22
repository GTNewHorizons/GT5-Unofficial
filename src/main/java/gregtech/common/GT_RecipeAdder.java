package gregtech.common;

import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.util.RecipeBuilder;

public class GT_RecipeAdder implements IGT_RecipeAdder {

    @Override
    public RecipeBuilder stdBuilder() {
        return RecipeBuilder.builder();
    }
}
