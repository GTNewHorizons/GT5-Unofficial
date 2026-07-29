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

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

public class MaterialFix {

    public static void MaterialFluidExtractionFix(Material material) {
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.gearGtSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.gearGtSmall, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.spring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.spring, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.itemCasing)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.itemCasing, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * HALF_INGOTS)))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.wireGt01)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * HALF_INGOTS)))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.cableGt01)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.cableGt01, material, 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dustSmall, 2))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * HALF_INGOTS)))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.foil)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.foil, material, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.springSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.springSmall, material, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.ring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ring, material, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.bolt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.bolt, material, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * EIGHTH_INGOTS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.wireFine)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireFine, material, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * EIGHTH_INGOTS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.round)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.round, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.screw)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.screw, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.rotor)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.rotor, material, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (612)))
                .duration(136 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.gearGt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.gearGt, material, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
                .duration(128 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
    }

    public static void addRecipeForMultiItems() {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.plateDouble)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.plate, ml, 2))
                    .circuit(2)
                    .itemOutputs(MU.stack(OrePrefixes.plateDouble, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 2, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
                GTModHandler.addCraftingRecipe(
                    MU.stack(OrePrefixes.plateDouble, ml, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "P", "P", "h", 'P', MU.stack(OrePrefixes.plate, ml, 1) });
            }
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.plateTriple)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.plate, ml, 3))
                    .circuit(3)
                    .itemOutputs(MU.stack(OrePrefixes.plateTriple, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.ingot, ml, 3))
                    .circuit(3)
                    .itemOutputs(MU.stack(OrePrefixes.plateTriple, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
                GTModHandler.addCraftingRecipe(
                    MU.stack(OrePrefixes.plateTriple, ml, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "B", "P", "h", 'P', MU.stack(OrePrefixes.plate, ml, 1), 'B',
                        MU.stack(OrePrefixes.plateDouble, ml, 1) });
            }
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.plateDense)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.plate, ml, 9))
                    .circuit(9)
                    .itemOutputs(MU.stack(OrePrefixes.plateDense, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 9, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.ingot, ml, 9))
                    .circuit(9)
                    .itemOutputs(MU.stack(OrePrefixes.plateDense, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 9, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);

                if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.plateTriple)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(MU.stack(OrePrefixes.plateTriple, ml, 3))
                        .circuit(3)
                        .itemOutputs(MU.stack(OrePrefixes.plateDense, ml, 1))
                        .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                        .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                        .addTo(benderRecipes);
                }
            }
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.stickLong)) {
                if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.stick)) {
                    GTModHandler.addCraftingRecipe(
                        MU.stack(OrePrefixes.stickLong, ml, 1),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PhP", 'P', MU.stack(OrePrefixes.stick, ml, 1) });
                }
            }
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.spring)) {
                GTModHandler.addCraftingRecipe(
                    MU.stack(OrePrefixes.spring, ml, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fPx", " P ", 'P', MU.stack(OrePrefixes.stickLong, ml, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.stickLong, ml, 1))
                    .circuit(1)
                    .itemOutputs(MU.stack(OrePrefixes.spring, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 2, 1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
            if (Materials2WerkstoffIndex.generatesPrefix(ml, OrePrefixes.springSmall)) {
                GTModHandler.addCraftingRecipe(
                    MU.stack(OrePrefixes.springSmall, ml, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fPx", 'P', MU.stack(OrePrefixes.stick, ml, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(MU.stack(OrePrefixes.stick, ml, 1))
                    .circuit(1)
                    .itemOutputs(MU.stack(OrePrefixes.springSmall, ml, 1))
                    .duration(Math.max(MaterialUtils.mass(ml), 1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
        }
    }
}
