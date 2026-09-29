package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingIngot implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingIngot INSTANCE;

    public ProcessingIngot() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        boolean noSmashing = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        boolean stretchy = MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY);
        boolean noSmelting = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING);
        long materialMass = MaterialUtils.mass(material);
        boolean specialRecipeReq = MaterialUtils.unifiable(material)
            && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);

        switch (prefix.getName()) {
            case "ingot" -> {
                // Fuel recipe
                if (MaterialUtils.fuelPower(material) > 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material))
                        .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                        .addTo(GTRecipeConstants.Fuel);
                }
                if (MaterialUtils.hasMolten(material)
                    && !(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
                    // Fluid solidifier recipes

                    GTValues.RA.stdBuilder()
                        .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
                        .itemOutputs(MaterialParts.stack(OrePrefixes.ingot, material, 1L))
                        .fluidInputs(MaterialUtils.molten(material, 1 * INGOTS))
                        .duration(1 * SECONDS + 12 * TICKS)
                        .eut(calculateRecipeEU(material, 8))
                        .addTo(fluidSolidifierRecipes);
                }
                // Reverse recipes
                {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, false);
                    GTRecipeRegistrator.registerReverseMacerating(
                        stack,
                        material,
                        prefix.getMaterialAmount(),
                        null,
                        null,
                        null,
                        false,
                        false);
                    if (GTRecipeRegistrator.hasReverseArcSmeltingRecipe(material)) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, stack),
                            material,
                            prefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tStack = MaterialParts.stack(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L);
                if ((tStack != null) && ((MaterialUtils.blastFurnaceRequired(material)) || noSmelting)) {
                    GTModHandler.removeFurnaceSmelting(tStack);
                }
                if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)
                    && !MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
                    && MaterialUtils.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MaterialParts.stack(OrePrefixes.dust, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { ToolDictNames.craftingToolMortar,
                            MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
                }
                if (!noSmashing) {
                    // Forge hammer recipes
                    if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV
                        && MaterialParts.stack(OrePrefixes.plate, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(3, stack))
                            .itemOutputs(MaterialParts.stack(OrePrefixes.plate, material, 2L))
                            .duration(Math.max(materialMass, 1L))
                            .eut(calculateRecipeEU(material, 16))
                            .addTo(hammerRecipes);
                    }
                }
                if (!noSmashing || stretchy) {

                    // Bender recipes
                    {
                        if (MaterialParts.stack(OrePrefixes.plate, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plate, material, 1L))
                                .duration(Math.max(materialMass, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.plateDouble, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack))
                                .circuit(2)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plateDouble, material, 1L))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.plateTriple, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack))
                                .circuit(3)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plateTriple, material, 1L))
                                .duration(Math.max(materialMass * 3L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.plateQuadruple, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, stack))
                                .circuit(4)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plateQuadruple, material, 1L))
                                .duration(Math.max(materialMass * 4L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.plateQuintuple, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, stack))
                                .circuit(5)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plateQuintuple, material, 1L))
                                .duration(Math.max(materialMass * 5L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.plateDense, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .circuit(9)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.plateDense, material, 1L))
                                .duration(Math.max(materialMass * 9L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialParts.stack(OrePrefixes.foil, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(10)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.foil, material, 4L))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }
                    }
                }
            }
            case "ingotHot" -> {
                if (MaterialUtils.autoGenerateVacuumFreezerRecipes(material) && producesHotIngot(material)
                    && MaterialParts.stack(OrePrefixes.ingot, material, 1L) != null) {
                    // Vacuum freezer recipes
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialParts.stack(OrePrefixes.ingot, material, 1L))
                        .duration(((int) Math.max(materialMass * 3L, 1L)) * TICKS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(vacuumFreezerRecipes);
                }
            }
            default -> {}
        }
    }

    /// Whether `material`'s hot ingot gets a vacuum freezer recipe. Former Werkstoff materials carry the shape
    /// whether or not anything makes it, so theirs is frozen only when they blast-smelt above 1750 K, the
    /// threshold for a hot ingot output.
    private static boolean producesHotIngot(Material material) {
        if (!LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.ingotHot)) return true;
        return MaterialUtils.blastFurnaceRequired(material) && MaterialUtils.blastFurnaceTemp(material) > 1750;
    }
}
