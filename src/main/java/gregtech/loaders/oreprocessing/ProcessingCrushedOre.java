package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrushedOre() {
        OrePrefixes.crushedCentrifuged.add(this);
        OrePrefixes.crushedPurified.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.contains(SubTag.NO_ORE_PROCESSING)) {
            return;
        }

        switch (aPrefix.getName()) {
            case "crushedCentrifuged" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        GTOreDictUnificator.get(
                            OrePrefixes.dust,
                            GTUtility.selectItemInList(2, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
            }
            case "crushedPurified" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(
                            OrePrefixes.crushedCentrifuged,
                            aMaterial.mMacerateInto,
                            GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                            1L),
                        GTOreDictUnificator.get(
                            OrePrefixes.dust,
                            GTUtility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1111)
                    .duration(25 * SECONDS)
                    .eut(48)
                    .addTo(thermalCentrifugeRecipes);

                ItemStack tGem = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
                if (tGem == null) {
                    break;
                }

                // Blacklist materials which are handled by Werkstoff loader and coal, which has an override
                if (aMaterial == Materials.Salt || aMaterial == Materials.RockSalt
                    || aMaterial == Materials.Spodumene
                    || aMaterial == Materials.Coal) return;

                switch (aMaterial.mName) {
                    case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" -> GTValues.RA
                        .stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                            tGem,
                            GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                        .outputChances(300, 1200, 4500, 1400, 2800, 3500)
                        .duration(40 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(sifterRecipes);
                    default -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                            tGem,
                            GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                            GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                        .outputChances(100, 400, 1500, 2000, 4000, 5000)
                        .duration(40 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(sifterRecipes);
                }

            }
            default -> {}
        }
    }
}
