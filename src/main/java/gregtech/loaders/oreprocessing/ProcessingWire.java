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
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingWire implements PartRecipeGenerator {

    public static final ProcessingWire INSTANCE = new ProcessingWire();

    private final Material[] dielectrics = { Materials.PolyvinylChloride, Materials.Polydimethylsiloxane };

    private static Object tt;

    private ProcessingWire() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
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
                        if (MaterialLibAPI.findStack(material, Shapes.springSmall, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.springSmall, 2))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(material, 8))
                                .addTo(benderRecipes);
                        }
                    }

                    // Wiremill Recipes
                    {
                        if (MaterialLibAPI.findStack(material, Shapes.wireFine, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack))
                                .circuit(1)
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.wireFine, 4))
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
                        MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "Xx", 'X', OrePrefixes.plate.ingredient(material) });

                }

                // Assembler recipes
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .circuit(2)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.wireGt02, 1))
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(4, stack))
                    .circuit(4)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.wireGt04, 1))
                    .duration(10 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(8, stack))
                    .circuit(8)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.wireGt08, 1))
                    .duration(15 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(12, stack))
                    .circuit(12)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.wireGt12, 1))
                    .duration(20 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(16, stack))
                    .circuit(16)
                    .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.wireGt16, 1))
                    .duration(25 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);
            }
            case "wireGt02" -> {
                cableWidth = 2;
                correspondingCable = TEBlockShapes.cableGt02;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 2),
                    new Object[] { prefix.ingredient(material) });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt01.ingredient(material),
                        OrePrefixes.wireGt01.ingredient(material) });
            }
            case "wireGt04" -> {
                cableWidth = 4;
                correspondingCable = TEBlockShapes.cableGt04;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 4),
                    new Object[] { prefix.ingredient(material) });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material),
                        OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt02.ingredient(material),
                        OrePrefixes.wireGt02.ingredient(material) });
            }
            case "wireGt08" -> {
                cableWidth = 8;
                correspondingCable = TEBlockShapes.cableGt08;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 8),
                    new Object[] { prefix.ingredient(material) });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material),
                        OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material),
                        OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material),
                        OrePrefixes.wireGt01.ingredient(material), OrePrefixes.wireGt01.ingredient(material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt04.ingredient(material),
                        OrePrefixes.wireGt04.ingredient(material) });
            }
            case "wireGt12" -> {
                cableWidth = 12;
                correspondingCable = TEBlockShapes.cableGt12;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 12),
                    new Object[] { prefix.ingredient(material) });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt08.ingredient(material),
                        OrePrefixes.wireGt04.ingredient(material) });
            }
            case "wireGt16" -> {
                cableWidth = 16;
                correspondingCable = TEBlockShapes.cableGt16;
                // Shapeless crafting recipes
                GTModHandler.addShapelessCraftingRecipe(
                    MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 16),
                    new Object[] { prefix.ingredient(material) });

                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt08.ingredient(material),
                        OrePrefixes.wireGt08.ingredient(material) });
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    new Object[] { OrePrefixes.wireGt12.ingredient(material),
                        OrePrefixes.wireGt04.ingredient(material) });

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
                craftingListRubber.add(prefix.ingredient(material));
                for (int i = 0; i < costMultiplier; i++) {
                    craftingListRubber.add(OrePrefixes.plate.ingredient(Materials.Rubber));
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
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.Silicone,
                                    FluidShapes.fluidMolten,
                                    costMultiplier * HALF_INGOTS))
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
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.Silicone,
                                    FluidShapes.fluidMolten,
                                    costMultiplier * HALF_INGOTS))
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
                                            MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)))
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
                                            MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)))
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
                                            MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)))
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
                                            MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)))
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
                            MaterialLibAPI.findStack(material, TEBlockShapes.wireGt01, 1))
                        .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.cableGt01, 1))
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(alloySmelterRecipes);
                } else if (correspondingCable == TEBlockShapes.cableGt02) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.ingot, 2),
                            MaterialLibAPI.findStack(material, TEBlockShapes.wireGt02, 1))
                        .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.cableGt02, 1))
                        .duration(10 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(alloySmelterRecipes);
                } else if (correspondingCable == TEBlockShapes.cableGt04) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            MaterialLibAPI.getStack(Materials.Rubber, Shapes.ingot, 4),
                            MaterialLibAPI.findStack(material, TEBlockShapes.wireGt04, 1))
                        .itemOutputs(MaterialLibAPI.findStack(material, TEBlockShapes.cableGt04, 1))
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
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, costMultiplier * HALF_INGOTS))
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
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(assemblerRecipes);

                for (Material dielectric : dielectrics) {
                    for (Material syntheticRubber : Wildcard.AnySyntheticRubber.members()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)))
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
                                    MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)))
                            .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                            .fluidInputs(MaterialUtils.molten(syntheticRubber, (long) costMultiplier * QUARTER_INGOTS))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(cableRecipes);

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)))
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
                                    MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)))
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
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(cableRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        stack,
                        MaterialLibAPI.getStack(Materials.PolyphenyleneSulfide, Shapes.foil, costMultiplier))
                    .circuit(24)
                    .itemOutputs(MaterialLibAPI.getStack(material, correspondingCable, 1))
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, costMultiplier * HALF_INGOTS))
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(material, 8))
                    .addTo(assemblerRecipes);

                for (Material dielectric : dielectrics) {
                    for (Material syntheticRubber : Wildcard.AnySyntheticRubber.members()) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTUtility.copyAmount(4, stack),
                                GTUtility
                                    .copyAmount(costMultiplier, MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)),
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
                                    MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)),
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
                                    .copyAmount(costMultiplier, MaterialLibAPI.findStack(dielectric, Shapes.dust, 1)),
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
                                    MaterialLibAPI.findStack(dielectric, Shapes.dustSmall, 1)),
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
