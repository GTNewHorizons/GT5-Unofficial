package gregtech.loaders.oreprocessing;

import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materialrecipes.LoaderSifterRecipes;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingCrushedOre INSTANCE;

    public ProcessingCrushedOre() {
        INSTANCE = this;
        OrePrefixes.crushedCentrifuged.add(this);
        OrePrefixes.crushedPurified.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        if (ProcessingOreMachine.owns(material)) {
            return;
        }

        switch (prefix.getName()) {
            case "crushedCentrifuged" -> {
                // An ore material need not have a dust of its own -- ChargedCertusQuartz removes the dust shape.
                ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L);
                if (dust == null) break;

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(convertOre(material, dust))
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(
                        convertOre(
                            material,
                            GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L),
                            GTOreDictUnificator.get(
                                OrePrefixes.dust,
                                GTUtility.selectItemInList(
                                    2,
                                    MaterialUtils.macerateInto(material),
                                    MaterialUtils.oreByProducts(material)),
                                1L)))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
            }
            case "crushedPurified" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(
                        convertOre(
                            material,
                            GTOreDictUnificator.get(
                                OrePrefixes.crushedCentrifuged,
                                MaterialUtils.macerateInto(material),
                                GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L),
                                1L),
                            GTOreDictUnificator.get(
                                OrePrefixes.dust,
                                GTUtility.selectItemInList(
                                    1,
                                    MaterialUtils.macerateInto(material),
                                    MaterialUtils.oreByProducts(material)),
                                1L)))
                    .outputChances(10000, 1111)
                    .duration(25 * SECONDS)
                    .eut(48)
                    .addTo(thermalCentrifugeRecipes);

                ItemStack tGem = GTOreDictUnificator.get(OrePrefixes.gem, material, 1L);
                if (tGem == null) {
                    break;
                }

                // Coal has an override elsewhere, LoaderSifterRecipes owns its carriers' sifter recipe, and
                // Salt and Spodumene deliberately have none.
                if (material == Materials.Coal || material == Materials.Salt
                    || material == Materials.Spodumene
                    || LoaderSifterRecipes.ownsSifterRecipe(material)) return;

                switch (MaterialUtils.internalName(material)) {
                    case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" -> GTValues.RA
                        .stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            convert(
                                material,
                                GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, tGem, 1L),
                                tGem,
                                GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.gemChipped, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dust, material, tGem, 1L)))
                        .outputChances(300, 1200, 4500, 1400, 2800, 3500)
                        .duration(40 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(sifterRecipes);
                    default -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            convert(
                                material,
                                GTOreDictUnificator.get(OrePrefixes.gemExquisite, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.gemFlawless, material, tGem, 1L),
                                tGem,
                                GTOreDictUnificator.get(OrePrefixes.gemFlawed, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.gemChipped, material, tGem, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dust, material, tGem, 1L)))
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
