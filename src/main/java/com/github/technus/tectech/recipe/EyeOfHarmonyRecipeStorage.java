package com.github.technus.tectech.recipe;

import static com.google.common.math.IntMath.pow;
import static pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper.dimToOreWrapper;

public abstract class EyeOfHarmonyRecipeStorage {

//    static final long MILLION = pow(10, 6); // Fix, these take int, not long, overflow occurring.
//    static final long BILLION = pow(10, 9);
//    static final long TRILLION = pow(10, 12);
//    static final long QUADRILLION = pow(10, 15);
//    static final long QUINTILLION = pow(10, 18);
    static final long SEXTILLION = pow(10, 21);

//
    public static EyeOfHarmonyRecipe overworld = new EyeOfHarmonyRecipe(dimToOreWrapper.get("Ow"),
            1.0,
            100,
            100,
            36_000L,
            0,
            100 * 10,
            0.4);
}
