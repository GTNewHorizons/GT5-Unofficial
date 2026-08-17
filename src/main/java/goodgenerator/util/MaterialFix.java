package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
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
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

public class MaterialFix {

    public static void MaterialFluidExtractionFix(Material material) {
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.gearGtSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.gearGtSmall, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.spring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.spring, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.foil)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.foil, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.springSmall)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.springSmall, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.ring)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ring, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * QUARTER_INGOTS)))
                .duration(8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.bolt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.bolt, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * EIGHTH_INGOTS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.wireFine)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.wireFine, 1))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * EIGHTH_INGOTS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.screw)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.screw, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (1 * NUGGETS)))
                .duration(4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.rotor)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.rotor, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, 612))
                .duration(136 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
        if (LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.gearGt)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.gearGt, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
                .duration(128 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(fluidExtractionRecipes);
        }
    }

    /// Whether the werkstoff part set covers every one of `prefixes` for `material`. The gate reads
    /// [LegacyWerkstoffIndex#generatesPrefix], the werkstoff part set alone: widening it to
    /// `MaterialParts#generatesPrefix` would double-generate against gregtech's own part loaders.
    private static boolean generatesAll(Material material, OrePrefixes... prefixes) {
        for (OrePrefixes prefix : prefixes) {
            if (!LegacyWerkstoffIndex.generatesPrefix(material, prefix)) return false;
        }
        return true;
    }

    public static void addRecipeForMultiItems() {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            if (generatesAll(ml, OrePrefixes.plateDouble, OrePrefixes.plate)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.plate, 2))
                    .circuit(2)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateDouble, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 2, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(ml, Shapes.plateDouble, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "P", "P", "h", 'P', MaterialLibAPI.getStack(ml, Shapes.plate, 1) });
            }
            if (generatesAll(ml, OrePrefixes.plateTriple, OrePrefixes.plate)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.plate, 3))
                    .circuit(3)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateTriple, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.plateTriple, OrePrefixes.ingot)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.ingot, 3))
                    .circuit(3)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateTriple, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.plateTriple, OrePrefixes.plate, OrePrefixes.plateDouble)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(ml, Shapes.plateTriple, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "B", "P", "h", 'P', MaterialLibAPI.getStack(ml, Shapes.plate, 1), 'B',
                        MaterialLibAPI.getStack(ml, Shapes.plateDouble, 1) });
            }
            if (generatesAll(ml, OrePrefixes.plateDense, OrePrefixes.plate)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.plate, 9))
                    .circuit(9)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateDense, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 9, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.plateDense, OrePrefixes.ingot)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.ingot, 9))
                    .circuit(9)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateDense, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 9, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.plateDense, OrePrefixes.plateTriple)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.plateTriple, 3))
                    .circuit(3)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.plateDense, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 3, 1L) * TICKS)
                    .eut(BWUtil.calculateRecipeEU(ml, (int) (TierEU.RECIPE_MV / 2)))
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.stickLong, OrePrefixes.stick)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(ml, Shapes.stickLong, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PhP", 'P', MaterialLibAPI.getStack(ml, Shapes.stick, 1) });
            }
            if (generatesAll(ml, OrePrefixes.spring, OrePrefixes.stickLong)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(ml, Shapes.spring, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fPx", " P ", 'P', MaterialLibAPI.getStack(ml, Shapes.stickLong, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.stickLong, 1))
                    .circuit(1)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.spring, 1))
                    .duration(Math.max(MaterialUtils.mass(ml) * 2, 1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
            if (generatesAll(ml, OrePrefixes.springSmall, OrePrefixes.stick)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(ml, Shapes.springSmall, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fPx", 'P', MaterialLibAPI.getStack(ml, Shapes.stick, 1) });
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.stick, 1))
                    .circuit(1)
                    .itemOutputs(MaterialLibAPI.getStack(ml, Shapes.springSmall, 1))
                    .duration(Math.max(MaterialUtils.mass(ml), 1L) * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(benderRecipes);
            }
        }
    }
}
