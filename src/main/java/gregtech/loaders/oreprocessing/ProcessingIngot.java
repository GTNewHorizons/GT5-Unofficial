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
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
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
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.ingot, 1))
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
                ItemStack tStack = MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1);
                if ((tStack != null) && ((MaterialUtils.blastFurnaceRequired(material)) || noSmelting)) {
                    GTModHandler.removeFurnaceSmelting(tStack);
                }
                if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)
                    && !MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
                    && MaterialUtils.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.dust, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { ToolDictNames.craftingToolMortar, OrePrefixes.ingot.ingredient(material) });
                }
                if (!noSmashing) {
                    // Forge hammer recipes
                    if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV
                        && MaterialLibAPI.findStack(material, Shapes.plate, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(3, stack))
                            .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plate, 2))
                            .duration(Math.max(materialMass, 1L))
                            .eut(calculateRecipeEU(material, 16))
                            .addTo(hammerRecipes);
                    }
                }
                if (!noSmashing || stretchy) {

                    // Bender recipes
                    {
                        if (MaterialLibAPI.findStack(material, Shapes.plate, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plate, 1))
                                .duration(Math.max(materialMass, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateDouble, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack))
                                .circuit(2)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateDouble, 1))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateTriple, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, stack))
                                .circuit(3)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateTriple, 1))
                                .duration(Math.max(materialMass * 3L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateQuadruple, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, stack))
                                .circuit(4)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateQuadruple, 1))
                                .duration(Math.max(materialMass * 4L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateQuintuple, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, stack))
                                .circuit(5)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateQuintuple, 1))
                                .duration(Math.max(materialMass * 5L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.plateDense, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, stack))
                                .circuit(9)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.plateDense, 1))
                                .duration(Math.max(materialMass * 9L, 1L))
                                .eut(calculateRecipeEU(material, 96))
                                .addTo(benderRecipes);
                        }

                        if (MaterialLibAPI.findStack(material, Shapes.foil, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(10)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.foil, 4))
                                .duration(Math.max(materialMass * 2L, 1L))
                                .eut(calculateRecipeEU(material, 24))
                                .addTo(benderRecipes);
                        }
                    }
                }
            }
            case "ingotHot" -> {
                if (MaterialUtils.autoGenerateVacuumFreezerRecipes(material)
                    && MaterialLibAPI.findStack(material, Shapes.ingot, 1) != null) {
                    Integer duration = material.getProperty(GTMaterialProperties.VACUUM_FREEZER_DURATION);
                    Integer voltage = material.getProperty(GTMaterialProperties.VACUUM_FREEZER_VOLTAGE);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.ingot, 1))
                        .duration(duration != null ? duration : Math.max(materialMass * 3L, 1L) * TICKS)
                        .eut(voltage != null ? voltage : TierEU.RECIPE_MV)
                        .addTo(vacuumFreezerRecipes);
                }
            }
            default -> {}
        }
    }
}
