package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class MaterialFix {

    public static void MaterialFluidExtractionFix(Werkstoff material) {
        if (material.hasItemType(OrePrefixes.gearGtSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.gearGtSmall))
                .fluidOutputs(material.getMolten(1 * INGOTS))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.spring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.spring))
                .fluidOutputs(material.getMolten(1 * INGOTS))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.itemCasing)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.itemCasing))
                .fluidOutputs(material.getMolten(1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.wireGt01)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.wireGt01))
                .fluidOutputs(material.getMolten(1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.cableGt01)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.cableGt01))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 2))
                .fluidOutputs(material.getMolten(1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.foil)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.foil))
                .fluidOutputs(material.getMolten(1 * QUARTER_INGOTS))
                .duration(8 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.springSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.springSmall))
                .fluidOutputs(material.getMolten(1 * QUARTER_INGOTS))
                .duration(8 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.ring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.ring))
                .fluidOutputs(material.getMolten(1 * QUARTER_INGOTS))
                .duration(8 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.bolt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.bolt))
                .fluidOutputs(material.getMolten(1 * EIGHTH_INGOTS))
                .duration(4 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.wireFine)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.wireFine))
                .fluidOutputs(material.getMolten(1 * EIGHTH_INGOTS))
                .duration(4 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.round)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.round))
                .fluidOutputs(material.getMolten(1 * NUGGETS))
                .duration(4 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.screw)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.screw))
                .fluidOutputs(material.getMolten(1 * NUGGETS))
                .duration(4 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.rotor)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.rotor))
                .fluidOutputs(material.getMolten(612))
                .duration(136 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.gearGt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.get(OrePrefixes.gearGt))
                .fluidOutputs(material.getMolten(4 * INGOTS))
                .duration(128 * TICKS)
                .eut(8)
                .addTo(fluidExtractionRecipes);
        }
    }

    public static void addRecipeForMultiItems() {
        for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
            if (tMaterial == null) continue;
            if (tMaterial.hasItemType(OrePrefixes.plateDouble)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.plate, 2))
                    .circuit(2)
                    .itemOutputs(tMaterial.get(OrePrefixes.plateDouble, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 2,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(benderRecipes);
                GTModHandler.addCraftingRecipe(
                    tMaterial.get(OrePrefixes.plateDouble, 1),
                    new Object[] { "P", "P", "h", 'P', tMaterial.get(OrePrefixes.plate, 1) });
            }
            if (tMaterial.hasItemType(OrePrefixes.plateTriple)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.plate, 3))
                    .circuit(3)
                    .itemOutputs(tMaterial.get(OrePrefixes.plateTriple, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 3,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(benderRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.ingot, 3))
                    .circuit(3)
                    .itemOutputs(tMaterial.get(OrePrefixes.plateTriple, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 3,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(benderRecipes);
                GTModHandler.addCraftingRecipe(
                    tMaterial.get(OrePrefixes.plateTriple, 1),
                    new Object[] { "B", "P", "h", 'P', tMaterial.get(OrePrefixes.plate, 1), 'B',
                        tMaterial.get(OrePrefixes.plateDouble, 1) });
            }
            if (tMaterial.hasItemType(OrePrefixes.plateDense)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.plate, 9))
                    .circuit(9)
                    .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 9,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(benderRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.ingot, 9))
                    .circuit(9)
                    .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 9,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(benderRecipes);

                if (tMaterial.hasItemType(OrePrefixes.plateTriple)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.plateTriple, 3))
                        .circuit(3)
                        .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                        .duration(
                            Math.max(
                                tMaterial.getStats()
                                    .getMass() * 3,
                                1L) * TICKS)
                        .eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                }
            }
            if (tMaterial.hasItemType(OrePrefixes.stickLong)) {
                if (tMaterial.hasItemType(OrePrefixes.stick)) {
                    GTModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.stickLong, 1),
                        new Object[] { "PhP", 'P', tMaterial.get(OrePrefixes.stick, 1) });
                }
            }
            if (tMaterial.hasItemType(OrePrefixes.spring)) {
                GTModHandler.addCraftingRecipe(
                    tMaterial.get(OrePrefixes.spring, 1),
                    new Object[] { " s ", "fPx", " P ", 'P', tMaterial.get(OrePrefixes.stickLong, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.stickLong, 1))
                    .circuit(1)
                    .itemOutputs(tMaterial.get(OrePrefixes.spring, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass() * 2,
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
            if (tMaterial.hasItemType(OrePrefixes.springSmall)) {
                GTModHandler.addCraftingRecipe(
                    tMaterial.get(OrePrefixes.springSmall, 1),
                    new Object[] { " s ", "fPx", 'P', tMaterial.get(OrePrefixes.stick, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(tMaterial.get(OrePrefixes.stick, 1))
                    .circuit(1)
                    .itemOutputs(tMaterial.get(OrePrefixes.springSmall, 1))
                    .duration(
                        Math.max(
                            tMaterial.getStats()
                                .getMass(),
                            1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
        }
    }
}
