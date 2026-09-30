package gregtech.loaders.oreprocessing;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cableRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import appeng.api.config.TunnelType;
import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.enums.materials.Wildcard;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingWire INSTANCE;

    private final Material[] dielectrics = { Materials.PolyvinylChloride, Materials.Polydimethylsiloxane };

    private static Object tt;

    public ProcessingWire() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (tt == TunnelType.ME) {
            try {
                tt = TunnelType.valueOf("GT_POWER");
            } catch (IllegalArgumentException ignored) {
                tt = TunnelType.IC2_POWER;
            }
        }

        int cableWidth;
        Shape correspondingCable;

        switch (prefix.getName()) {
            case "wireGt01" -> {
                cableWidth = 1;
                correspondingCable = TEBlockShapes.cableGt01;
                if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
                    // Bender recipes
                    {
                        if (MaterialParts.stack(OrePrefixes.springSmall, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.springSmall, material, 2L))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(material, 8))
                                .addTo(benderRecipes);
                        }
                    }

                    // Wiremill Recipes
                    {
                        if (MaterialParts.stack(OrePrefixes.wireFine, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialParts.stack(OrePrefixes.wireFine, material, 4L))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(material, 8))
                                .addTo(wiremillRecipes);
                        }
                    }
                }

                // crafting recipe
                if (!material.getProperty(GTMaterialProperties.IS_SUPERCONDUCTOR) && MaterialUtils.unifiable(material)
                    && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)
                    && (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV)) {
                    GTModHandler.addCraftingRecipe(
                        MaterialParts.stack(OrePrefixes.wireGt01, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "Xx", 'X', MaterialParts.craftIngredient(OrePrefixes.plate, material) });

                }

                // Assembler recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .circuit(2)
                    .itemOutputs(MaterialParts.stack(OrePrefixes.wireGt02, material, 1L))
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(4, stack))
                    .circuit(4)
                    .itemOutputs(MaterialParts.stack(OrePrefixes.wireGt04, material, 1L))
                    .duration(10 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(8, stack))
                    .circuit(8)
                    .itemOutputs(MaterialParts.stack(OrePrefixes.wireGt08, material, 1L))
                    .duration(15 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(12, stack))
                    .circuit(12)
                    .itemOutputs(MaterialParts.stack(OrePrefixes.wireGt12, material, 1L))
                    .duration(20 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(16, stack))
                    .circuit(16)
                    .itemOutputs(MaterialParts.stack(OrePrefixes.wireGt16, material, 1L))
                    .duration(25 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
            }
            case "wireGt02" -> {
                cableWidth = 2;
                correspondingCable = TEBlockShapes.cableGt02;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.wireGt01, material, 2L),
                    new Object[] { oreDictName });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material) });
            }
            case "wireGt04" -> {
                cableWidth = 4;
                correspondingCable = TEBlockShapes.cableGt04;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.wireGt01, material, 4L),
                    new Object[] { oreDictName });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt02, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt02, material) });
            }
            case "wireGt08" -> {
                cableWidth = 8;
                correspondingCable = TEBlockShapes.cableGt08;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.wireGt01, material, 8L),
                    new Object[] { oreDictName });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt01, material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt04, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt04, material) });
            }
            case "wireGt12" -> {
                cableWidth = 12;
                correspondingCable = TEBlockShapes.cableGt12;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.wireGt01, material, 12L),
                    new Object[] { oreDictName });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt08, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt04, material) });
            }
            case "wireGt16" -> {
                cableWidth = 16;
                correspondingCable = TEBlockShapes.cableGt16;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.wireGt01, material, 16L),
                    new Object[] { oreDictName });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt08, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt08, material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { MaterialParts.craftIngredient(OrePrefixes.wireGt12, material),
                        MaterialParts.craftIngredient(OrePrefixes.wireGt04, material) });

                AE2addNewAttunement(stack);
            }
            default -> {
                GT_FML_LOGGER.error(
                    "OrePrefix {} cannot be registered as a cable for Material {}",
                    prefix.getName(),
                    MaterialUtils.internalName(material));
                return;
            }
        }

        int costMultiplier = cableWidth / 4 + 1;

        switch (MaterialUtils.internalName(material)) {
            case "RedAlloy", "Cobalt", "Lead", "Tin", "Zinc", "SolderingAlloy" -> {
                ArrayList<Object> craftingListRubber = new ArrayList<>();
                craftingListRubber.add(oreDictName);
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Rubber));
                }

                // shapeless crafting
                if (material.hasShape(correspondingCable)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MaterialLibAPI.getStack(material, correspondingCable, 1),
                        craftingListRubber.toArray());
                }

                // Packer recipe
                if (material.hasShape(correspondingCable)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTUtility.copyAmount(1, stack),
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.plate, costMultiplier))
                        .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(packagerRecipes);
                }
                // Cable recipes
                {
                    if (material.hasShape(correspondingCable)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.Rubber,
                                    FluidShapes.fluidMolten,
                                    (int) (costMultiplier * INGOTS)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.StyreneButadieneRubber,
                                    FluidShapes.fluidMolten,
                                    (int) (costMultiplier * 3 * QUARTER_INGOTS)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(Materials.Silicone, (long) costMultiplier * HALF_INGOTS))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.Rubber,
                                    FluidShapes.fluidMolten,
                                    (int) (costMultiplier * INGOTS)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.StyreneButadieneRubber,
                                    FluidShapes.fluidMolten,
                                    (int) (costMultiplier * 3 * QUARTER_INGOTS)))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(stack)
                            .circuit(24)
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(Materials.Silicone, (long) costMultiplier * HALF_INGOTS))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(assemblerRecipes);

                        for (Material dielectric : dielectrics) {
                            for (Material syntheticRubber : Wildcard.AnySyntheticRubber.members()) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(4, stack),
                                        GTUtility.copyAmount(
                                            costMultiplier,
                                            MaterialParts.stack(OrePrefixes.dust, dielectric, 1)))
                                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                                    .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                                    .duration(20 * SECONDS)
                                    .eut(TierEU.RECIPE_ULV)
                                    .addTo(cableRecipes);

                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        stack,
                                        GTUtility.copyAmount(
                                            costMultiplier,
                                            MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)))
                                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                                    .fluidInputs(
                                        MaterialUtils.molten(syntheticRubber, (long) costMultiplier * QUARTER_INGOTS))
                                    .duration(5 * SECONDS)
                                    .eut(TierEU.RECIPE_ULV)
                                    .addTo(cableRecipes);

                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(4, stack),
                                        GTUtility.copyAmount(
                                            costMultiplier,
                                            MaterialParts.stack(OrePrefixes.dust, dielectric, 1)))
                                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                                    .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                                    .duration(20 * SECONDS)
                                    .eut(TierEU.RECIPE_ULV)
                                    .addTo(assemblerRecipes);

                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        stack,
                                        GTUtility.copyAmount(
                                            costMultiplier,
                                            MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)))
                                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                                    .fluidInputs(
                                        MaterialUtils.molten(syntheticRubber, (long) costMultiplier * QUARTER_INGOTS))
                                    .duration(5 * SECONDS)
                                    .eut(TierEU.RECIPE_ULV)
                                    .addTo(assemblerRecipes);

                            }
                        }
                    }
                }
                // Alloy Smelter recipes
                if (correspondingCable == TEBlockShapes.cableGt01) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.ingot, 2),
                            MaterialParts.stack(OrePrefixes.wireGt01, material, 1L))
                        .itemOutputs(MaterialParts.stack(OrePrefixes.cableGt01, material, 1L))
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(alloySmelterRecipes);
                } else if (correspondingCable == TEBlockShapes.cableGt02) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.ingot, 2),
                            MaterialParts.stack(OrePrefixes.wireGt02, material, 1L))
                        .itemOutputs(MaterialParts.stack(OrePrefixes.cableGt02, material, 1L))
                        .duration(10 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(alloySmelterRecipes);
                } else if (correspondingCable == TEBlockShapes.cableGt04) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.ingot, 4),
                            MaterialParts.stack(OrePrefixes.wireGt04, material, 1L))
                        .itemOutputs(MaterialParts.stack(OrePrefixes.cableGt04, material, 1L))
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(alloySmelterRecipes);
                }
            }
            case "RedstoneAlloy", "Iron", "Nickel", "Cupronickel", "Copper", "AnnealedCopper", "ElectricalSteel", "Kanthal", "Gold", "Electrum", "Silver", "BlueAlloy", "EnergeticAlloy", "Nichrome", "Steel", "BlackSteel", "Titanium", "Aluminium", "TPVAlloy", "VibrantAlloy" -> {

                if (!material.hasShape(correspondingCable)) {
                    break;
                }
                // Cable recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Rubber, FluidShapes.fluidMolten, (int) (costMultiplier * INGOTS)))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(cableRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials.StyreneButadieneRubber,
                            FluidShapes.fluidMolten,
                            (int) (costMultiplier * 3 * QUARTER_INGOTS)))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(cableRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(MaterialUtils.molten(Materials.Silicone, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(cableRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Rubber, FluidShapes.fluidMolten, (int) (costMultiplier * INGOTS)))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials.StyreneButadieneRubber,
                            FluidShapes.fluidMolten,
                            (int) (costMultiplier * 3 * QUARTER_INGOTS)))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(stack)
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(MaterialUtils.molten(Materials.Silicone, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(assemblerRecipes);

                for (Material dielectric : dielectrics) {
                    for (Material syntheticRubber : Wildcard.AnySyntheticRubber.members()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialParts.stack(OrePrefixes.dust, dielectric, 1)))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                            .duration(20 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                stack,
                                GTUtility.copyAmount(
                                    costMultiplier,
                                    MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * QUARTER_INGOTS))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialParts.stack(OrePrefixes.dust, dielectric, 1)))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                            .duration(20 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(assemblerRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                stack,
                                GTUtility.copyAmount(
                                    costMultiplier,
                                    MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * QUARTER_INGOTS))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(assemblerRecipes);

                    }
                }
            }

            default -> {
                if (!material.hasShape(correspondingCable)) {
                    break;
                }

                // Cable recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        stack,
                        MaterialLibAPI.getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier))
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(MaterialUtils.molten(Materials.Silicone, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(cableRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        stack,
                        MaterialLibAPI.getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier))
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(MaterialUtils.molten(Materials.Silicone, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);

                for (Material dielectric : dielectrics) {
                    for (Material syntheticRubber : Wildcard.AnySyntheticRubber.members()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialParts.stack(OrePrefixes.dust, dielectric, 1)),
                                MaterialLibAPI
                                    .getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier * 4))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                            .duration(20 * SECONDS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(cableRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                stack,
                                GTUtility.copyAmount(
                                    costMultiplier,
                                    MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)),
                                MaterialLibAPI.getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, costMultiplier * 36L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(cableRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialParts.stack(OrePrefixes.dust, dielectric, 1)),
                                MaterialLibAPI
                                    .getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier * 4))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 4))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * INGOTS))
                            .duration(20 * SECONDS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(assemblerRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                stack,
                                GTUtility.copyAmount(
                                    costMultiplier,
                                    MaterialParts.stack(OrePrefixes.dustSmall, dielectric, 1)),
                                MaterialLibAPI.getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, costMultiplier * 36L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(material, 8))
                            .addTo(assemblerRecipes);
                    }
                }
            }
        }

        if (material.hasShape(correspondingCable)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(material, 8))
                .addTo(unpackagerRecipes);

            AE2AddNetAttunementCable(stack, correspondingCable, material);
        } else {
            AE2addNewAttunement(stack);
        }
    }

    // region AE2 compat
    static {
        setAE2Field();
    }

    private static void setAE2Field() {
        tt = TunnelType.ME;
    }

    private void AE2addNewAttunement(ItemStack stack) {
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(stack, (TunnelType) tt);
    }

    private void AE2AddNetAttunementCable(ItemStack stack, Shape correspondingCable, Material material) {
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(stack, (TunnelType) tt);
        Api.INSTANCE.registries()
            .p2pTunnel()
            .addNewAttunement(MaterialLibAPI.getStack(material, correspondingCable, 1), (TunnelType) tt);
    }
    // end region
}
