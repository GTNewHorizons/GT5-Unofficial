package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.enums.GTValues;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ALGAE_POND_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.algaePondRecipes;

public class RecipeLoaderAlgaePond {
    public static void generate(){
        // Tier 0
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(6),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2)
            )
            .outputChances(100_00, 90_00)
            .metadata(ALGAE_POND_TIER, 0)
            .duration(1* MINUTES+40*SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 1
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(2),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4)
            )
            .outputChances(100_00, 100_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 1)
            .duration(1* MINUTES+30*SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 2
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(7),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 2)
            .duration(1* MINUTES+20*SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 3
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(1),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4)
            )
            .outputChances(100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 3)
            .duration(1* MINUTES+10*SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 4
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(6),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4)
                )
            .outputChances(100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 4)
            .duration(1* MINUTES)
            .eut(0)
            .addTo(algaePondRecipes);
        // Tier 5
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(10),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(2),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 5)
            .duration(50*SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 6
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+1*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+1*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+1*2),
                GregtechItemList.RedAlgaeBiomass.get(2+1),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 6)
            .duration(25*SECONDS+12*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 7
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+3*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+3*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+3*2),
                GregtechItemList.RedAlgaeBiomass.get(2+3),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 7)
            .duration(12*SECONDS+16*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 8
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+6*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+6*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+6*2),
                GregtechItemList.RedAlgaeBiomass.get(2+6),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 8)
            .duration(6*SECONDS+8*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 9
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+10*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+10*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+10*2),
                GregtechItemList.RedAlgaeBiomass.get(2+10),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 9)
            .duration(3*SECONDS+4*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 10
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+15*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+15*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+15*2),
                GregtechItemList.RedAlgaeBiomass.get(2+15),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 10)
            .duration(1*SECONDS+12*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 11
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+21*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+21*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+21*2),
                GregtechItemList.RedAlgaeBiomass.get(2+21),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 11)
            .duration(16*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 12
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+28*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+28*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+28*2),
                GregtechItemList.RedAlgaeBiomass.get(2+28),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 12)
            .duration(8*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 13
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+36*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+36*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+36*2),
                GregtechItemList.RedAlgaeBiomass.get(2+36),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 13)
            .duration(4*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 14
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+45*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+45*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+45*2),
                GregtechItemList.RedAlgaeBiomass.get(2+45),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 14)
            .duration(2*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 15
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11+55*4),
                GregtechItemList.BrownAlgaeBiomass.get(10+55*3),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(6+55*2),
                GregtechItemList.RedAlgaeBiomass.get(2+55),
                //
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(4),
                GregtechItemList.GreenAlgaeBiomass.get(8),
                //
                GregtechItemList.BrownAlgaeBiomass.get(4),
                //
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                //
                GregtechItemList.RedAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(8)
            )
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 15)
            .duration(1*TICKS)
            .eut(0)
            .addTo(algaePondRecipes);
    }
}
