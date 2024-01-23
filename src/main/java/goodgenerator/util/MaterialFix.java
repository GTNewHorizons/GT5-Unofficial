package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class MaterialFix {

    public static void MaterialFluidExtractionFix(Werkstoff material) {
        if (material.hasItemType(OrePrefixes.ingot)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.ingot)).fluidOutputs(material.getMolten(144))
                    .duration(1 * SECONDS + 12 * TICKS).eut(8).addTo(fluidExtractionRecipes);
            GT_Values.RA.stdBuilder().itemInputs(ItemList.Shape_Mold_Ingot.get(0)).fluidInputs(material.getMolten(144))
                    .itemOutputs(material.get(OrePrefixes.ingot)).duration(1 * SECONDS + 12 * TICKS)
                    .eut(TierEU.RECIPE_MV / 2).addTo(fluidSolidifierRecipes);
        }
        if (material.hasItemType(OrePrefixes.plate)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.plate)).fluidOutputs(material.getMolten(144))
                    .duration(1 * SECONDS + 12 * TICKS).eut(8).addTo(fluidExtractionRecipes);
            GT_Values.RA.stdBuilder().itemInputs(ItemList.Shape_Mold_Plate.get(0)).fluidInputs(material.getMolten(144))
                    .itemOutputs(material.get(OrePrefixes.plate)).duration(1 * SECONDS + 12 * TICKS)
                    .eut(TierEU.RECIPE_MV / 2).addTo(fluidSolidifierRecipes);
        }
        if (material.hasItemType(OrePrefixes.gearGtSmall)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.gearGtSmall))
                    .fluidOutputs(material.getMolten(144)).duration(1 * SECONDS + 12 * TICKS).eut(8)
                    .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.stickLong)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.stickLong))
                    .fluidOutputs(material.getMolten(144)).duration(1 * SECONDS + 12 * TICKS).eut(8)
                    .addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.spring)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.spring)).fluidOutputs(material.getMolten(144))
                    .duration(1 * SECONDS + 12 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.stick)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.stick)).fluidOutputs(material.getMolten(72))
                    .duration(16 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.itemCasing)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.itemCasing))
                    .fluidOutputs(material.getMolten(72)).duration(16 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.wireGt01)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.wireGt01))
                    .fluidOutputs(material.getMolten(72)).duration(16 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.cableGt01)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.cableGt01))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 2))
                    .fluidOutputs(material.getMolten(72)).duration(16 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.foil)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.foil)).fluidOutputs(material.getMolten(36))
                    .duration(8 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.springSmall)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.springSmall))
                    .fluidOutputs(material.getMolten(36)).duration(8 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.ring)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.ring)).fluidOutputs(material.getMolten(36))
                    .duration(8 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.bolt)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.bolt)).fluidOutputs(material.getMolten(18))
                    .duration(4 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.wireFine)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.wireFine))
                    .fluidOutputs(material.getMolten(18)).duration(4 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.round)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.round)).fluidOutputs(material.getMolten(16))
                    .duration(4 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.screw)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.screw)).fluidOutputs(material.getMolten(16))
                    .duration(4 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.nugget)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.nugget)).fluidOutputs(material.getMolten(16))
                    .duration(4 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.rotor)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.rotor)).fluidOutputs(material.getMolten(612))
                    .duration(136 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
        if (material.hasItemType(OrePrefixes.gearGt)) {
            GT_Values.RA.stdBuilder().itemInputs(material.get(OrePrefixes.gearGt)).fluidOutputs(material.getMolten(576))
                    .duration(128 * TICKS).eut(8).addTo(fluidExtractionRecipes);
        }
    }

    public static void addRecipeForMultiItems() {
        for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
            if (tMaterial == null) continue;
            if (tMaterial.hasItemType(OrePrefixes.plateDouble) && tMaterial.hasItemType(OrePrefixes.ingotDouble)) {
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.plate, 2), GT_Utility.getIntegratedCircuit(2))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateDouble, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 2, 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.ingotDouble, 1), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateDouble, 1))
                        .duration(Math.max(tMaterial.getStats().getMass(), 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.plateDouble, 1),
                        new Object[] { "P", "P", "h", 'P', tMaterial.get(OrePrefixes.plate, 1) });
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.ingotDouble, 1),
                        new Object[] { "P", "P", "h", 'P', tMaterial.get(OrePrefixes.ingot, 1) });
            }
            if (tMaterial.hasItemType(OrePrefixes.plateTriple) && tMaterial.hasItemType(OrePrefixes.ingotTriple)) {
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.plate, 3), GT_Utility.getIntegratedCircuit(3))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateTriple, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 3, 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.ingot, 3), GT_Utility.getIntegratedCircuit(3))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateTriple, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 3, 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.ingotTriple, 1), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateTriple, 1))
                        .duration(Math.max(tMaterial.getStats().getMass(), 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.plateTriple, 1),
                        new Object[] { "B", "P", "h", 'P', tMaterial.get(OrePrefixes.plate, 1), 'B',
                                tMaterial.get(OrePrefixes.plateDouble, 1) });
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.ingotTriple, 1),
                        new Object[] { "B", "P", "h", 'P', tMaterial.get(OrePrefixes.ingot, 1), 'B',
                                tMaterial.get(OrePrefixes.ingotDouble, 1) });
            }
            if (tMaterial.hasItemType(OrePrefixes.plateDense)) {
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.plate, 9), GT_Utility.getIntegratedCircuit(9))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 9, 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.ingot, 9), GT_Utility.getIntegratedCircuit(9))
                        .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 9, 1L) * TICKS).eut(TierEU.RECIPE_MV / 2)
                        .addTo(benderRecipes);

                if (tMaterial.hasItemType(OrePrefixes.plateTriple) && tMaterial.hasItemType(OrePrefixes.ingotTriple)) {
                    GT_Values.RA.stdBuilder()
                            .itemInputs(tMaterial.get(OrePrefixes.plateTriple, 3), GT_Utility.getIntegratedCircuit(3))
                            .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                            .duration(Math.max(tMaterial.getStats().getMass() * 3, 1L) * TICKS)
                            .eut(TierEU.RECIPE_MV / 2).addTo(benderRecipes);
                    GT_Values.RA.stdBuilder()
                            .itemInputs(tMaterial.get(OrePrefixes.ingotTriple, 3), GT_Utility.getIntegratedCircuit(3))
                            .itemOutputs(tMaterial.get(OrePrefixes.plateDense, 1))
                            .duration(Math.max(tMaterial.getStats().getMass() * 3, 1L) * TICKS)
                            .eut(TierEU.RECIPE_MV / 2).addTo(benderRecipes);
                }
            }
            if (tMaterial.hasItemType(OrePrefixes.stick)) {
                if (tMaterial.hasItemType(OrePrefixes.cellMolten)) {
                    GT_Values.RA.stdBuilder().itemInputs(ItemList.Shape_Mold_Rod.get(0))
                            .fluidInputs(tMaterial.getMolten(72)).itemOutputs(tMaterial.get(OrePrefixes.stick, 1))
                            .duration(Math.max(tMaterial.getStats().getMass() >> 1, 1L) * TICKS).eut(TierEU.RECIPE_HV)
                            .addTo(fluidSolidifierRecipes);
                }
            }
            if (tMaterial.hasItemType(OrePrefixes.stickLong)) {
                if (tMaterial.hasItemType(OrePrefixes.cellMolten)) {
                    GT_Values.RA.stdBuilder().itemInputs(ItemList.Shape_Mold_Rod_Long.get(0))
                            .fluidInputs(tMaterial.getMolten(144)).itemOutputs(tMaterial.get(OrePrefixes.stickLong, 1))
                            .duration(Math.max(tMaterial.getStats().getMass(), 1L) * TICKS).eut(TierEU.RECIPE_HV)
                            .addTo(fluidSolidifierRecipes);
                }
                if (tMaterial.hasItemType(OrePrefixes.stick)) {
                    GT_ModHandler.addCraftingRecipe(
                            tMaterial.get(OrePrefixes.stickLong, 1),
                            new Object[] { "PhP", 'P', tMaterial.get(OrePrefixes.stick, 1) });
                    GT_Values.RA.stdBuilder().itemInputs(tMaterial.get(OrePrefixes.stick, 2))
                            .itemOutputs(tMaterial.get(OrePrefixes.stickLong, 1))
                            .duration(Math.max(tMaterial.getStats().getMass(), 1L) * TICKS).eut(TierEU.RECIPE_LV / 2)
                            .addTo(hammerRecipes);
                }
            }
            if (tMaterial.hasItemType(OrePrefixes.spring)) {
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.spring, 1),
                        new Object[] { " s ", "fPx", " P ", 'P', tMaterial.get(OrePrefixes.stickLong, 1) });
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.stickLong, 1), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(tMaterial.get(OrePrefixes.spring, 1))
                        .duration(Math.max(tMaterial.getStats().getMass() * 2, 1L) * TICKS).eut(TierEU.RECIPE_LV / 2)
                        .addTo(benderRecipes);
            }
            if (tMaterial.hasItemType(OrePrefixes.springSmall)) {
                GT_ModHandler.addCraftingRecipe(
                        tMaterial.get(OrePrefixes.springSmall, 1),
                        new Object[] { " s ", "fPx", 'P', tMaterial.get(OrePrefixes.stick, 1) });
                GT_Values.RA.stdBuilder()
                        .itemInputs(tMaterial.get(OrePrefixes.stick, 1), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(tMaterial.get(OrePrefixes.springSmall, 1))
                        .duration(Math.max(tMaterial.getStats().getMass(), 1L) * TICKS).eut(TierEU.RECIPE_LV / 2)
                        .addTo(benderRecipes);
            }
        }
        Materials tUHV = Materials.Longasssuperconductornameforuhvwire;
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.stick, tUHV, 2))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, tUHV, 1))
                .duration(Math.max(tUHV.getMass(), 1L) * TICKS).eut(TierEU.RECIPE_LV / 2).addTo(hammerRecipes);
    }
}
