package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.NI;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS_STD;
import static gregtech.api.util.GTModHandler.RecipeBits.BUFFERED;
import static gregtech.api.util.GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingPlate implements PartRecipeGenerator {

    public static final ProcessingPlate INSTANCE = new ProcessingPlate();

    private ProcessingPlate() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        final boolean noSmashing = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        final boolean noWorking = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING);
        final long materialMass = MaterialUtils.mass(material);

        switch (prefix.getName()) {
            case "plate" -> registerPlate(material, stack, noSmashing);
            case "plateDouble" -> registerPlateDouble(material, stack, noSmashing, materialMass);
            case "plateTriple" -> registerPlateTriple(material, stack, noSmashing, materialMass);
            case "plateQuadruple" -> registerPlateQuadruple(material, stack, noSmashing, materialMass, noWorking);
            case "plateQuintuple" -> registerPlateQuintuple(material, stack, noSmashing, materialMass);
            case "plateDense" -> registerPlateDense(material, stack, noSmashing, materialMass);
            case "plateSuperdense" -> registerPlateSuperdense(material, stack, noSmashing, materialMass);
            case "itemCasing" -> registerItemCasing(prefix, material, stack, noSmashing);
            default -> {}
        }
    }

    private void registerPlate(final Material material, final ItemStack stack, final boolean noSmashing) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);
        // The only plate that has a valid recipe to remove
        if (material == Materials.Wood) GTModHandler.removeRecipeDelayed(stack);

        GTUtility.removeSimpleIC2MachineRecipe(
            GTUtility.copyAmount(9, stack),
            GTModHandler.getCompressorRecipeList(),
            MaterialLibAPI.findStack(material, Shapes.plateDense, 1));

        if (MaterialUtils.fuelPower(material) > 0) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .metadata(FUEL_VALUE, MaterialUtils.fuelPower(material))
                .metadata(FUEL_TYPE, MaterialUtils.fuelType(material))
                .addTo(GTRecipeConstants.Fuel);
        }

        if (MaterialUtils.hasMolten(material)
            && !(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.plate, 1))
                .fluidInputs(MaterialUtils.molten(material, 1 * INGOTS))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(calculateRecipeEU(material, 8))
                .addTo(fluidSolidifierRecipes);
        }

        if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
            if (material.hasShape(Shapes.foil)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(material, Shapes.foil, 2),
                    BITS_STD,
                    new Object[] { "hX", 'X', OrePrefixes.plate.ingredient(material) });
            }

            if (material == Materials.Paper) {
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(2, stack),
                    BUFFERED | DO_NOT_CHECK_FOR_COLLISIONS,
                    new Object[] { "XXX", 'X', new ItemStack(Items.reeds, 1, WILDCARD) });
            }
        }

        if (MaterialUtils.unifiable(material)) {

            boolean belowTierIV = MaterialUtils.processingMaterialTierEU(material) < TierEU.IV;

            if (!noSmashing) {

                if (belowTierIV) {
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.getStack(material, Shapes.plate, 1),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", "X", 'X', OrePrefixes.ingot.ingredient(material) });

                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.getStack(material, Shapes.plate, 1),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", 'X', OrePrefixes.gem.ingredient(material) });
                }
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE)) {

                if (belowTierIV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MaterialLibAPI.getStack(material, Shapes.dust, 1),
                        BITS_STD,
                        new Object[] { ToolDictNames.craftingToolMortar, OrePrefixes.plate.ingredient(material) });
                }
            }
        }
    }

    private void registerPlateDouble(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // 2 double -> 1 quadruple plate
            if (material.hasShape(Shapes.plateQuadruple)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .circuit(2)
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.plateQuadruple, 1))
                    .duration(Math.max(materialMass * 2L, 1L))
                    .eut(calculateRecipeEU(material, 96))
                    .addTo(benderRecipes);
            }
            // 2 plates -> 1 double plate
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 2))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 2L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 2))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 10))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        if (!noSmashing) {
            Object plateStack = OrePrefixes.plate.ingredient(material);
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', plateStack, 'B', plateStack });
            }
        }
    }

    private void registerPlateTriple(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            if (material.hasShape(Shapes.plateDense)) {
                // 3 triple plates -> 1 dense plate
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(3, stack))
                    .circuit(3)
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.plateDense, 1))
                    .duration(Math.max(materialMass * 3L, 1L))
                    .eut(calculateRecipeEU(material, 96))
                    .addTo(benderRecipes);
            }

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 3))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 3L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 3))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 20))
                .duration(4 * SECONDS + 16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        if (!noSmashing) {
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                Object plateStack = OrePrefixes.plate.ingredient(material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateDouble.ingredient(material), 'B', plateStack });
            }
        }
    }

    private void registerPlateQuadruple(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass, final boolean noWorking) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // Quadruple plate
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 4))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 4L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 4))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 30))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }
        if (!noSmashing) {
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                Object plateStack = OrePrefixes.plate.ingredient(material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateTriple.ingredient(material), 'B', plateStack });
            }
        }
    }

    private void registerPlateQuintuple(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // quintuple plate
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 5))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 5L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 5))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 40))
                .duration(8 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }
        if (!noSmashing) {
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                Object plateStack = OrePrefixes.plate.ingredient(material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateQuadruple.ingredient(material), 'B', plateStack });
            }
        }
    }

    private void registerPlateDense(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // Dense plate
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 9))
                .circuit(9)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 9L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        }
    }

    private void registerPlateSuperdense(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {
        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MaterialUtils.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            int compression_tier = (MaterialUtils.processingMaterialTierEU(material) >= TierEU.RECIPE_UEV
                || MaterialUtils.hasFlag(material, GTMaterialFlag.BLACK_HOLE)) ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.plate, 64))
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 32L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .metadata(COMPRESSION_TIER, compression_tier)
                .addTo(compressorRecipes);
        }
    }

    private void registerItemCasing(final OrePrefixes prefix, final Material material, final ItemStack stack,
        final boolean noSmashing) {

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (MaterialUtils.hasMolten(material)
            && !(material == Materials.AnnealedCopper || material == Materials.CastIron)) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 1))
                .fluidInputs(MaterialUtils.molten(material, 1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(calculateRecipeEU(material, 8))
                .addTo(fluidSolidifierRecipes);
        }

        if (MaterialUtils.unifiable(material) && !noSmashing) {

            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(material, Shapes.itemCasing, 1),
                    BITS_STD,
                    new Object[] { "h X", 'X', OrePrefixes.plate.ingredient(material) });
            }
        }

        if (material.hasShape(Shapes.ingot)) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 2), ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(GTUtility.copyAmount(3, stack))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(calculateRecipeEU(material, 15))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 1), ItemList.Shape_Extruder_Casing.get(0L))
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 2))
                .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 45))
                .addTo(extruderRecipes);
        }

        if (material.hasShape(Shapes.plate)) {
            ItemStack plate = MaterialLibAPI.getStack(material, Shapes.plate, 1);
            GTValues.RA.stdBuilder()
                .itemInputs(plate)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 2))
                .fluidInputs(
                    GTUtility.getWater(
                        Math.max(
                            4,
                            Math.min(
                                1000,
                                ((int) Math.max(MaterialUtils.mass(material), 1L)) * (calculateRecipeEU(material, 16))
                                    / 320))))
                .duration(2 * ((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(plate)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 2))
                .fluidInputs(
                    GTModHandler.getDistilledWater(
                        Math.max(
                            3,
                            Math.min(
                                750,
                                ((int) Math.max(MaterialUtils.mass(material), 1L)) * (calculateRecipeEU(material, 16))
                                    / 426))))
                .duration(2 * ((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(plate)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 2))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.Lubricant,
                        FluidShapes.fluidLiquid,
                        (int) Math.max(
                            1,
                            Math.min(
                                250,
                                ((int) Math.max(MaterialUtils.mass(material), 1)) * (calculateRecipeEU(material, 16))
                                    / 1280))))
                .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(plate)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.itemCasing, 2))
                .fluidInputs(
                    MaterialUtils.fluid(
                        Materials.dimensionallyshiftedsuperfluid,
                        Math.max(
                            1,
                            Math.min(
                                10,
                                ((int) Math.max(MaterialUtils.mass(material), 1L)) * (calculateRecipeEU(material, 16))
                                    / 4000))))
                .duration(((int) Math.max(MaterialUtils.mass(material) / 2.5, 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);
        }
    }

    private void registerCover(final Material material, final ItemStack stack) {

        // Get ItemStack of Block matching Materials
        ItemStack tStack = NI;
        // Try different prefixes to use same smooth stones as older GT5U
        for (OrePrefixes orePrefix : new OrePrefixes[] { OrePrefixes.block, OrePrefixes.block_, OrePrefixes.stoneSmooth,
            OrePrefixes.stone }) {
            if ((tStack = MaterialParts.stack(orePrefix, material, 1)) != NI) break;
        }

        // Register the cover
        CoverRegistry.registerDecorativeCover(
            stack,
            // If there is an ItemStack of Block for Materials
            tStack == NI ?
            // Use the material's RGBA-dyed blocks/materials/MATERIALSET/block1 icons
                GTMaterialTextures.stored(GTMaterialIcons.block("block1", material), () -> MaterialUtils.rgba(material))
                :
                // or copy Block texture
                TextureFactory.of(Block.getBlockFromItem(tStack.getItem()), tStack.getItemDamage()));
    }
}
