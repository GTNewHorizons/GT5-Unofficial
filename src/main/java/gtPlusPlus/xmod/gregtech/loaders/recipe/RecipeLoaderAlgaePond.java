package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ALGAE_POND_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.algaePondRecipes;

import gregtech.api.enums.GTValues;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderAlgaePond {

    public static void generate() {

        // Tier 0
        GTValues.RA.stdBuilder()
            .itemOutputs(GregtechItemList.AlgaeBiomass.get(6), GregtechItemList.GreenAlgaeBiomass.get(2))
            .outputChances(100_00, 90_00)
            .metadata(ALGAE_POND_TIER, 0)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 1
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(6))
            .outputChances(100_00, 100_00, 90_00)
            .metadata(ALGAE_POND_TIER, 1)
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 2
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(7),
                GregtechItemList.GreenAlgaeBiomass.get(14))
            .outputChances(100_00, 100_00, 90_00)
            .metadata(ALGAE_POND_TIER, 2)
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 3
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(1),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4))
            .outputChances(100_00, 100_00, 100_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 3)
            .duration(1 * MINUTES + 10 * SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 4
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(6),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4))
            .outputChances(100_00, 100_00, 100_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 4)
            .duration(1 * MINUTES)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 5
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(11),
                GregtechItemList.BrownAlgaeBiomass.get(10),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(2),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(4))
            .outputChances(100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 5)
            .duration(50 * SECONDS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 6
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(15),
                GregtechItemList.BrownAlgaeBiomass.get(13),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(8),
                GregtechItemList.RedAlgaeBiomass.get(3),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 6)
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 7
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(23),
                GregtechItemList.BrownAlgaeBiomass.get(19),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(12),
                GregtechItemList.RedAlgaeBiomass.get(5),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 7)
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 8
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(35),
                GregtechItemList.BrownAlgaeBiomass.get(28),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(18),
                GregtechItemList.RedAlgaeBiomass.get(8),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 8)
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 9
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(51),
                GregtechItemList.BrownAlgaeBiomass.get(40),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(26),
                GregtechItemList.RedAlgaeBiomass.get(12),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 9)
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 10
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(7),
                GregtechItemList.BrownAlgaeBiomass.get(55),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(36),
                GregtechItemList.RedAlgaeBiomass.get(17),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 10)
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 11
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(31),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(9),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(48),
                GregtechItemList.RedAlgaeBiomass.get(23),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 11)
            .duration(16 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 12
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(59),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(30),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(62),
                GregtechItemList.RedAlgaeBiomass.get(30),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(100_00, 100_00, 100_00, 100_00, 100_00, 100_00, 100_00, 90_00, 90_00, 90_00, 90_00)
            .metadata(ALGAE_POND_TIER, 12)
            .duration(8 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 13
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(27),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(54),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(64),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(14),
                GregtechItemList.RedAlgaeBiomass.get(38),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                90_00,
                90_00,
                90_00,
                90_00)
            .metadata(ALGAE_POND_TIER, 13)
            .duration(4 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 14
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(63),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(17),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(64),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(32),
                GregtechItemList.RedAlgaeBiomass.get(47),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                90_00,
                90_00,
                90_00,
                90_00)
            .metadata(ALGAE_POND_TIER, 14)
            .duration(2 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);

        // Tier 15
        GTValues.RA.stdBuilder()
            .itemOutputs(
                GregtechItemList.AlgaeBiomass.get(10),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(64),
                GregtechItemList.GreenAlgaeBiomass.get(39),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(64),
                GregtechItemList.BrownAlgaeBiomass.get(47),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(64),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(52),
                GregtechItemList.RedAlgaeBiomass.get(57),
                GregtechItemList.GreenAlgaeBiomass.get(14),
                GregtechItemList.BrownAlgaeBiomass.get(4),
                GregtechItemList.GoldenBrownAlgaeBiomass.get(4),
                GregtechItemList.RedAlgaeBiomass.get(12))
            .outputChances(
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                100_00,
                90_00,
                90_00,
                90_00,
                90_00)
            .metadata(ALGAE_POND_TIER, 15)
            .duration(1 * TICKS)
            .eut(0)
            .addTo(algaePondRecipes);
    }
}
