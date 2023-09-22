package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSifterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
                    .itemInputs(GT_Utility.copyAmount(1L, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
                    .duration(10 * TICKS)
                    .eut(16)
                    .addTo(sHammerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1L, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                        GT_OreDictUnificator.get(
                            OrePrefixes.dust,
                            GT_Utility.selectItemInList(2, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(sMaceratorRecipes);
            }
            case crushedPurified -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1L, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(
                            OrePrefixes.crushedCentrifuged,
                            aMaterial.mMacerateInto,
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                            1L),
                        GT_OreDictUnificator.get(
                            OrePrefixes.dust,
                            GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                            1L))
                    .outputChances(10000, 1111)
                    .duration(25 * SECONDS)
                    .eut(48)
                    .addTo(sThermalCentrifugeRecipes);

                ItemStack tGem = GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
                if (tGem != null) {
                    switch (aMaterial.mName) {
                        case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" -> GT_Values.RA
                            .stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                                tGem,
                                GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                            .outputChances(300, 1200, 4500, 1400, 2800, 3500)
                            .duration(40 * SECONDS)
                            .eut(16)
                            .addTo(sSifterRecipes);
                        default -> GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, tGem, 1L),
                                tGem,
                                GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, tGem, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, tGem, 1L))
                            .outputChances(100, 400, 1500, 2000, 4000, 5000)
                            .duration(40 * SECONDS)
                            .eut(16)
                            .addTo(sSifterRecipes);
                    }
                }
            }
            default -> {}
        }
    }
}
