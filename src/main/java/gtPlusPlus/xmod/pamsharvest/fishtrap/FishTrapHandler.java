package gtPlusPlus.xmod.pamsharvest.fishtrap;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class FishTrapHandler {

    static final String prefix = "food";
    static final String suffix = "raw";
    static final String greenheartFish = "foodGreenheartfish";
    private static final String[] harvestcraftFish = { "Anchovy", "Bass", "Calamari", "Carp", "Catfish", "Charr",
        "Clam", "Crab", "Crayfish", "Eel", "Frog", "Grouper", "Herring", "Jellyfish", "Mudfish", "Octopus", "Perch",
        "Scallop", "Shrimp", "Snail", "Snapper", "Tilapia", "Trout", "Tuna", "Turtle", "Walley" };

    public static void pamsHarvestCraftCompat() {
        for (String fish : harvestcraftFish) {
            final String itemName = prefix + fish + suffix;
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(itemName, 1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                    .itemOutputs(
                        ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                    .fluidOutputs(FluidUtils.getFluidStack("methane", 48))
                    .duration(19 * SECONDS + 4 * TICKS)
                    .eut(5)
                    .addTo(centrifugeRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                    .fluidOutputs(FluidUtils.getFluidStack("fishoil", 50))
                    .duration(16 * TICKS)
                    .eut(4)
                    .addTo(fluidExtractionRecipes);
            }
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(greenheartFish, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                .itemOutputs(
                    ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1),
                    ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1))
                .outputChances(10000, 1000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                .fluidOutputs(FluidUtils.getFluidStack("methane", 48))
                .duration(19 * SECONDS + 4 * TICKS)
                .eut(5)
                .addTo(centrifugeRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                .fluidOutputs(FluidUtils.getFluidStack("fishoil", 50))
                .duration(16 * TICKS)
                .eut(4)
                .addTo(fluidExtractionRecipes);
        }
    }
}
