package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrushedOre() {
        OrePrefixes.crushedCentrifuged.add(this);
        OrePrefixes.crushedPurified.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aPrefix) {
            case crushedCentrifuged -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
                    .duration(10 * TICKS)
                    .eut(16)
                    .addTo(hammerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        OreDictUnificator.get(
                            OrePrefixes.dust,
                            GT_Utility.selectItemInList(2, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
            }
            case crushedPurified -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        OreDictUnificator.get(
                            OrePrefixes.crushedCentrifuged,
                            aMaterial.mMacerateInto,
                            OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                            1L),
                        OreDictUnificator.get(
                            OrePrefixes.dust,
                            GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1111)
                    .duration(25 * SECONDS)
                    .eut(48)
                    .addTo(thermalCentrifugeRecipes);

                ItemStack tGem = OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
                if (tGem == null) {
                    break;
                }

                switch (aMaterial.mName) {
                    case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" -> GT_Values.RA
                        .stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                            tGem,
                            OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                        .outputChances(300, 1200, 4500, 1400, 2800, 3500)
                        .duration(40 * SECONDS)
                        .eut(16)
                        .addTo(sifterRecipes);
                    default -> GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                            tGem,
                            OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                            OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                        .outputChances(100, 400, 1500, 2000, 4000, 5000)
                        .duration(40 * SECONDS)
                        .eut(16)
                        .addTo(sifterRecipes);
                }

            }
            default -> {}
        }
    }
}
