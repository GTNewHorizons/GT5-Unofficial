package gtPlusPlus.xmod.pamsharvest.fishtrap;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
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
                GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                        .itemOutputs(
                                ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1),
                                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1))
                        .outputChances(10000, 1000).noFluidInputs().noFluidOutputs().duration(20 * SECONDS).eut(2)
                        .addTo(sMaceratorRecipes);
                GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                        .noItemOutputs().noFluidInputs().fluidOutputs(FluidUtils.getFluidStack("methane", 96))
                        .duration(19 * SECONDS).eut(5).addTo(sCentrifugeRecipes);
                GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(itemName, 1))
                        .noItemOutputs().noFluidInputs().fluidOutputs(FluidUtils.getFluidStack("fishoil", 50))
                        .duration(16 * TICKS).eut(4).addTo(sFluidExtractionRecipes);
            }
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken(greenheartFish, 1) != null) {
            GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                    .itemOutputs(
                            ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1),
                            ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBone", 1))
                    .outputChances(10000, 1000).noFluidInputs().noFluidOutputs().duration(20 * SECONDS).eut(2)
                    .addTo(sMaceratorRecipes);
            GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                    .noItemOutputs().noFluidInputs().fluidOutputs(FluidUtils.getFluidStack("methane", 96))
                    .duration(19 * SECONDS).eut(5).addTo(sCentrifugeRecipes);
            GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict(greenheartFish, 1))
                    .noItemOutputs().noFluidInputs().fluidOutputs(FluidUtils.getFluidStack("fishoil", 50))
                    .duration(16 * TICKS).eut(4).addTo(sFluidExtractionRecipes);
        }
    }
}
