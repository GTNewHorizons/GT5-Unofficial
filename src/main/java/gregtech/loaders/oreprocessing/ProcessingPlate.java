package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.NI;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS_STD;
import static gregtech.api.util.GTModHandler.RecipeBits.BUFFERED;
import static gregtech.api.util.GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingPlate implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingPlate INSTANCE;

    public ProcessingPlate() {
        INSTANCE = this;
        OrePrefixes.plate.add(this);
        OrePrefixes.plateDouble.add(this);
        OrePrefixes.plateTriple.add(this);
        OrePrefixes.plateQuadruple.add(this);
        OrePrefixes.plateQuintuple.add(this);
        OrePrefixes.plateDense.add(this);
        OrePrefixes.plateSuperdense.add(this);
        OrePrefixes.plateAlloy.add(this);
        OrePrefixes.itemCasing.add(this);
    }

    /**
     * Register processes for the {@link ItemStack} with Ore Dictionary Name Prefix "plate"
     *
     * @param prefix      always != null, the {@link OrePrefixes} of the {@link ItemStack}
     * @param material    always != null, and can be == _NULL if the Prefix is Self Referencing or not Material based!
     *                    the {@link Materials} of the {@link ItemStack}
     * @param oreDictName the Ore Dictionary Name {@link String} of the {@link ItemStack}
     * @param modName     the ModID {@link String} of the mod providing this {@link ItemStack}
     * @param stack       always != null, the {@link ItemStack} to register
     */
    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        final boolean noSmashing = MU.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        final boolean noWorking = MU.hasFlag(material, GTMaterialFlag.NO_WORKING);
        final long materialMass = MU.mass(material);

        switch (prefix.getName()) {
            case "plate" -> registerPlate(material, stack, noSmashing);
            case "plateDouble" -> registerPlateDouble(material, stack, noSmashing, materialMass);
            case "plateTriple" -> registerPlateTriple(material, stack, noSmashing, materialMass);
            case "plateQuadruple" -> registerPlateQuadruple(material, stack, noSmashing, materialMass, noWorking);
            case "plateQuintuple" -> registerPlateQuintuple(material, stack, noSmashing, materialMass);
            case "plateDense" -> registerPlateDense(material, stack, noSmashing, materialMass);
            case "plateSuperdense" -> registerPlateSuperdense(material, stack, noSmashing, materialMass);
            case "itemCasing" -> registerItemCasing(prefix, material, stack, noSmashing);
            case "plateAlloy" -> registerPlateAlloy(oreDictName, stack);
            default -> {}
        }
    }

    private void registerPlate(final Material material, final ItemStack stack, final boolean noSmashing) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);
        GTModHandler.removeRecipeDelayed(stack);

        GTUtility.removeSimpleIC2MachineRecipe(
            GTUtility.copyAmount(9, stack),
            GTModHandler.getCompressorRecipeList(),
            GTOreDictUnificator.get(OrePrefixes.plateDense, material, 1L));

        if (MU.fuelPower(material) > 0) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .metadata(FUEL_VALUE, MU.fuelPower(material))
                .metadata(FUEL_TYPE, MU.fuelType(material))
                .addTo(GTRecipeConstants.Fuel);
        }

        if (MU.hasMolten(material)
            && !(material == Materials2Materials.AnnealedCopper || material == Materials2Materials.CastIron)) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                .fluidInputs(MU.molten(material, 1 * INGOTS))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(calculateRecipeEU(material, 8))
                .addTo(fluidSolidifierRecipes);
        }

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.foil, material, 2L),
            BITS_STD,
            new Object[] { "hX", 'X', MU.craftIngredient(OrePrefixes.plate, material) });

        if (material == Materials2Materials.Paper) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(2, stack),
                BUFFERED,
                new Object[] { "XXX", 'X', new ItemStack(Items.reeds, 1, WILDCARD) });
        }

        if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))) {

            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            boolean belowTierIV = (processingTierEU == null ? 0 : processingTierEU) < TierEU.IV;

            if (!noSmashing) {

                if (belowTierIV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.plate, material, 1L),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", "X", 'X', MU.craftIngredient(OrePrefixes.ingot, material) });

                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.plate, material, 1L),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", 'X', MU.craftIngredient(OrePrefixes.gem, material) });
                }
            }

            if (MU.hasFlag(material, GTMaterialFlag.MORTAR_GRINDABLE)) {

                if (belowTierIV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.dust, material, 1L),
                        BITS_STD,
                        new Object[] { ToolDictNames.craftingToolMortar,
                            MU.craftIngredient(OrePrefixes.plate, material) });
                }
            }
        }
    }

    private void registerPlateDouble(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // 2 double -> 1 quadruple plate
            if (GTOreDictUnificator.get(OrePrefixes.plateQuadruple, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .circuit(2)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, material, 1L))
                    .duration(Math.max(materialMass * 2L, 1L))
                    .eut(calculateRecipeEU(material, 96))
                    .addTo(benderRecipes);
            }
            // 2 plates -> 1 double plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 2L))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 2L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 2L))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (10)))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        if (!noSmashing) {
            Object plateStack = MU.craftIngredient(OrePrefixes.plate, material);
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
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

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            if (GTOreDictUnificator.get(OrePrefixes.plateDense, material, 1L) != null) {
                // 3 triple plates -> 1 dense plate
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(3, stack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, material, 1L))
                    .duration(Math.max(materialMass * 3L, 1L))
                    .eut(calculateRecipeEU(material, 96))
                    .addTo(benderRecipes);
            }

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 3L))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 3L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 3L))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (20)))
                .duration(4 * SECONDS + 16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        if (!noSmashing) {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                Object plateStack = MU.craftIngredient(OrePrefixes.plate, material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', MU.craftIngredient(OrePrefixes.plateDouble, material), 'B', plateStack });
            }
        }

        if (GTOreDictUnificator.get(OrePrefixes.compressed, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.compressed, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials2Materials.DarkAsh, 1L))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(implosionRecipes);
        }
    }

    private void registerPlateQuadruple(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass, final boolean noWorking) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // Quadruple plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 4L))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 4L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 4L))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (30)))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }
        if (!noSmashing) {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                Object plateStack = MU.craftIngredient(OrePrefixes.plate, material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', MU.craftIngredient(OrePrefixes.plateTriple, material), 'B', plateStack });
            }
        }
    }

    private void registerPlateQuintuple(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // quintuple plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 5L))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .duration(Math.max(materialMass * 5L, 1L))
                .eut(calculateRecipeEU(material, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 5L))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, stack))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (40)))
                .duration(8 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }
        if (!noSmashing) {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                Object plateStack = MU.craftIngredient(OrePrefixes.plate, material);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, stack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', MU.craftIngredient(OrePrefixes.plateQuadruple, material), 'B', plateStack });
            }
        }
    }

    private void registerPlateDense(final Material material, final ItemStack stack, final boolean noSmashing,
        final long materialMass) {

        registerCover(material, stack);

        GTModHandler.removeRecipeByOutputDelayed(stack);

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            // Dense plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 9L))
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

        if (!noSmashing || MU.hasFlag(material, GTMaterialFlag.STRETCHY)) {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            int compression_tier = ((processingTierEU == null ? 0 : processingTierEU) >= TierEU.RECIPE_UEV
                || MU.hasFlag(material, GTMaterialFlag.BLACK_HOLE)) ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 64))
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

        if (MU.hasMolten(material)) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 1L))
                .fluidInputs(MU.molten(material, 1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(calculateRecipeEU(material, 8))
                .addTo(fluidSolidifierRecipes);
        }

        if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE)) && !noSmashing) {

            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 1L),
                    BITS_STD,
                    new Object[] { "h X", 'X', MU.craftIngredient(OrePrefixes.plate, material) });
            }
        }

        if (GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, material, 2L),
                    ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(GTUtility.copyAmount(3, stack))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(calculateRecipeEU(material, 15))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L),
                    ItemList.Shape_Extruder_Casing.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 2L))
                .duration(((int) Math.max(MU.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 45))
                .addTo(extruderRecipes);
        }

        if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 2L))
                .fluidInputs(
                    MU.fluid(
                        Materials2Materials.Water,
                        Math.max(
                            4,
                            Math.min(
                                1000,
                                ((int) Math.max(MU.mass(material), 1L)) * (calculateRecipeEU(material, 16)) / 320))))
                .duration(2 * ((int) Math.max(MU.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 2L))
                .fluidInputs(
                    GTModHandler.getDistilledWater(
                        Math.max(
                            3,
                            Math.min(
                                750,
                                ((int) Math.max(MU.mass(material), 1L)) * (calculateRecipeEU(material, 16)) / 426))))
                .duration(2 * ((int) Math.max(MU.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 2L))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Lubricant,
                        Materials2FluidShapes.fluidLiquid,
                        (int) Math.max(
                            1,
                            Math.min(
                                250,
                                ((int) Math.max(MU.mass(material), 1)) * (calculateRecipeEU(material, 16)) / 1280))))
                .duration(((int) Math.max(MU.mass(material), 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, material, 2L))
                .fluidInputs(
                    MU.fluid(
                        Materials2Materials.dimensionallyshiftedsuperfluid,
                        Math.max(
                            1,
                            Math.min(
                                10,
                                ((int) Math.max(MU.mass(material), 1L)) * (calculateRecipeEU(material, 16)) / 4000))))
                .duration(((int) Math.max(MU.mass(material) / 2.5, 1L)) * TICKS)
                .eut(calculateRecipeEU(material, 16))
                .addTo(cutterRecipes);
        }
    }

    private void registerPlateAlloy(final String oreDictName, final ItemStack stack) {

        switch (oreDictName) {
            case "plateAlloyCarbon" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("generator", 1L), GTUtility.copyAmount(4, stack))
                    .itemOutputs(GTModHandler.getIC2Item("windMill", 1L))
                    .duration(5 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack), new ItemStack(Blocks.glass, 3, WILDCARD))
                    .itemOutputs(ItemList.ReinforcedGlass.get(4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.copyAmount(1, stack),
                        MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (3)))
                    .itemOutputs(ItemList.ReinforcedGlass.get(4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
            }
            case "plateAlloyAdvanced" -> {
                RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack), new ItemStack(Blocks.glass, 3, WILDCARD))
                    .itemOutputs(ItemList.ReinforcedGlass.get(4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
                RA.stdBuilder()
                    .itemInputs(
                        GTUtility.copyAmount(1, stack),
                        MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (3)))
                    .itemOutputs(ItemList.ReinforcedGlass.get(4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
            }
            case "plateAlloyIridium" ->

                // Remove IC2 Shaped recipe for Iridium Reinforced Plate
                GTModHandler.removeRecipeByOutputDelayed(stack);
            default -> {}
        }
    }

    private void registerCover(final Material material, final ItemStack stack) {

        // Get ItemStack of Block matching Materials
        ItemStack tStack = NI;
        // Try different prefixes to use same smooth stones as older GT5U
        for (OrePrefixes orePrefix : new OrePrefixes[] { OrePrefixes.block, OrePrefixes.block_, OrePrefixes.stoneSmooth,
            OrePrefixes.stone }) {
            if ((tStack = GTOreDictUnificator.get(orePrefix, material, 1)) != NI) break;
        }

        // Register the cover
        CoverRegistry.registerDecorativeCover(
            stack,
            // If there is an ItemStack of Block for Materials
            tStack == NI ?
            // Use Materials mRGBa dyed blocs/materialicons/MATERIALSET/block1 icons
                TextureFactory.builder()
                    .addIcon(MU.iconSet(material).mTextures[MaterialIconRegistry.IconType.BLOCK1.ordinal()])
                    .setRGBA(MU.rgba(material))
                    .stdOrient()
                    .build()
                :
                // or copy Block texture
                TextureFactory.of(Block.getBlockFromItem(tStack.getItem()), tStack.getItemDamage()));
    }
}
