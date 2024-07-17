package gregtech.common;

import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.util.GT_RecipeBuilder;

public class GT_RecipeAdder implements IGT_RecipeAdder {

    @Override
    public GT_RecipeBuilder stdBuilder() {
        return GT_RecipeBuilder.builder();
    }
}
