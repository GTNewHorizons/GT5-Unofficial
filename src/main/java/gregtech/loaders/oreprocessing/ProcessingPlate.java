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
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingPlate implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPlate() {
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
     * @param aPrefix      always != null, the {@link OrePrefixes} of the {@link ItemStack}
     * @param aMaterial    always != null, and can be == _NULL if the Prefix is Self Referencing or not Material based!
     *                     the {@link Materials} of the {@link ItemStack}
     * @param aOreDictName the Ore Dictionary Name {@link String} of the {@link ItemStack}
     * @param aModName     the ModID {@link String} of the mod providing this {@link ItemStack}
     * @param aStack       always != null, the {@link ItemStack} to register
     */
    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        final boolean aNoSmashing = aMaterial.contains(SubTag.NO_SMASHING);
        final boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        final long aMaterialMass = aMaterial.getMass();

        switch (aPrefix.getName()) {
            case "plate" -> registerPlate(aMaterial, aStack, aNoSmashing);
            case "plateDouble" -> registerPlateDouble(aMaterial, aStack, aNoSmashing, aMaterialMass);
            case "plateTriple" -> registerPlateTriple(aMaterial, aStack, aNoSmashing, aMaterialMass);
            case "plateQuadruple" -> registerPlateQuadruple(aMaterial, aStack, aNoSmashing, aMaterialMass, aNoWorking);
            case "plateQuintuple" -> registerPlateQuintuple(aMaterial, aStack, aNoSmashing, aMaterialMass);
            case "plateDense" -> registerPlateDense(aMaterial, aStack, aNoSmashing, aMaterialMass);
            case "plateSuperdense" -> registerPlateSuperdense(aMaterial, aStack, aNoSmashing, aMaterialMass);
            case "itemCasing" -> registerItemCasing(aPrefix, aMaterial, aStack, aNoSmashing);
            case "plateAlloy" -> registerPlateAlloy(aOreDictName, aStack);
            default -> {}
        }
    }

    private void registerPlate(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);
        GTModHandler.removeRecipeDelayed(aStack);

        GTUtility.removeSimpleIC2MachineRecipe(
            GTUtility.copyAmount(9, aStack),
            GTModHandler.getCompressorRecipeList(),
            GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L));

        if (aMaterial.mFuelPower > 0) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                .metadata(FUEL_TYPE, aMaterial.mFuelType)
                .addTo(GTRecipeConstants.Fuel);
        }

        if (aMaterial.mStandardMoltenFluid != null
            && !(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
                .itemOutputs(aMaterial.getPlates(1))
                .fluidInputs(aMaterial.getMolten(1 * INGOTS))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(calculateRecipeEU(aMaterial, 8))
                .addTo(fluidSolidifierRecipes);
        }

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.foil, aMaterial, 2L),
            BITS_STD,
            new Object[] { "hX", 'X', OrePrefixes.plate.get(aMaterial) });

        if (aMaterial == Materials.Paper) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(2, aStack),
                BUFFERED,
                new Object[] { "XXX", 'X', new ItemStack(Items.reeds, 1, WILDCARD) });
        }

        if (aMaterial.mUnifiable && aMaterial.mMaterialInto == aMaterial) {

            if (!aNoSmashing) {

                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        aMaterial.getPlates(1),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", "X", 'X', OrePrefixes.ingot.get(aMaterial) });

                    GTModHandler.addCraftingRecipe(
                        aMaterial.getPlates(1),
                        BITS_STD,
                        new Object[] { "h", // craftingToolHardHammer
                            "X", 'X', OrePrefixes.gem.get(aMaterial) });
                }
            }

            if (aMaterial.contains(SubTag.MORTAR_GRINDABLE)) {

                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        aMaterial.getDust(1),
                        BITS_STD,
                        new Object[] { ToolDictNames.craftingToolMortar, OrePrefixes.plate.get(aMaterial) });
                }
            }
        }
    }

    private void registerPlateDouble(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            // 2 double -> 1 quadruple plate
            if (GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, aStack))
                    .circuit(2)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                    .duration(Math.max(aMaterialMass * 2L, 1L))
                    .eut(calculateRecipeEU(aMaterial, 96))
                    .addTo(benderRecipes);
            }
            // 2 plates -> 1 double plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 2L))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 2L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 2L))
                .circuit(2)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .fluidInputs(Materials.Glue.getFluid(10L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(8)
                .addTo(assemblerRecipes);
        }

        if (!aNoSmashing) {
            Object aPlateStack = OrePrefixes.plate.get(aMaterial);
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, aStack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', aPlateStack, 'B', aPlateStack });
            }
        }
    }

    private void registerPlateTriple(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            if (GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L) != null) {
                // 3 triple plates -> 1 dense plate
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(3, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                    .duration(Math.max(aMaterialMass * 3L, 1L))
                    .eut(calculateRecipeEU(aMaterial, 96))
                    .addTo(benderRecipes);
            }

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 3L))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 3L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 3L))
                .circuit(3)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .fluidInputs(Materials.Glue.getFluid(20L))
                .duration(4 * SECONDS + 16 * TICKS)
                .eut(8)
                .addTo(assemblerRecipes);
        }

        if (!aNoSmashing) {
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                Object aPlateStack = OrePrefixes.plate.get(aMaterial);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, aStack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateDouble.get(aMaterial), 'B', aPlateStack });
            }
        }

        if (GTOreDictUnificator.get(OrePrefixes.compressed, aMaterial, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.compressed, aMaterial, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 1L))
                .duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(ADDITIVE_AMOUNT, 2)
                .addTo(implosionRecipes);
        }
    }

    private void registerPlateQuadruple(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass, final boolean aNoWorking) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            // Quadruple plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 4L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L))
                .circuit(4)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .fluidInputs(Materials.Glue.getFluid(30L))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(8)
                .addTo(assemblerRecipes);
        }
        if (!aNoSmashing) {
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                Object aPlateStack = OrePrefixes.plate.get(aMaterial);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, aStack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateTriple.get(aMaterial), 'B', aPlateStack });
            }
        }
    }

    private void registerPlateQuintuple(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            // quintuple plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 5L))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 5L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .addTo(benderRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 5L))
                .circuit(5)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .fluidInputs(Materials.Glue.getFluid(40L))
                .duration(8 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);
        }
        if (!aNoSmashing) {
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                Object aPlateStack = OrePrefixes.plate.get(aMaterial);
                GTModHandler.addCraftingRecipe(
                    GTUtility.copyAmount(1, aStack),
                    DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED,
                    new Object[] { "I", "B", "h", // craftingToolHardHammer
                        'I', OrePrefixes.plateQuadruple.get(aMaterial), 'B', aPlateStack });
            }
        }
    }

    private void registerPlateDense(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass) {

        registerCover(aMaterial, aStack);

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            // Dense plate
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 9L))
                .circuit(9)
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 9L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .addTo(benderRecipes);
        }
    }

    final CompressionTierKey COMPRESSION_TIER = CompressionTierKey.INSTANCE;

    private void registerPlateSuperdense(final Materials aMaterial, final ItemStack aStack, final boolean aNoSmashing,
        final long aMaterialMass) {
        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (!aNoSmashing || aMaterial.contains(SubTag.STRETCHY)) {
            int compression_tier = (aMaterial.processingMaterialTierEU >= TierEU.RECIPE_UEV
                || aMaterial.contains(SubTag.BLACK_HOLE)) ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 64))
                .itemOutputs(GTUtility.copyAmount(1, aStack))
                .duration(Math.max(aMaterialMass * 32L, 1L))
                .eut(calculateRecipeEU(aMaterial, 96))
                .metadata(COMPRESSION_TIER, compression_tier)
                .addTo(compressorRecipes);
        }
    }

    private void registerItemCasing(final OrePrefixes aPrefix, final Materials aMaterial, final ItemStack aStack,
        final boolean aNoSmashing) {

        GTModHandler.removeRecipeByOutputDelayed(aStack);

        if (aMaterial.mStandardMoltenFluid != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 1L))
                .fluidInputs(aMaterial.getMolten(1 * HALF_INGOTS))
                .duration(16 * TICKS)
                .eut(calculateRecipeEU(aMaterial, 8))
                .addTo(fluidSolidifierRecipes);
        }

        if (aMaterial.mUnifiable && aMaterial.mMaterialInto == aMaterial && !aNoSmashing) {

            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 1L),
                    BITS_STD,
                    new Object[] { "h X", 'X', OrePrefixes.plate.get(aMaterial) });
            }
        }

        if (GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 2L),
                    ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(GTUtility.copyAmount(3, aStack))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(calculateRecipeEU(aMaterial, 15))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                    ItemList.Shape_Extruder_Casing.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 2L))
                .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                .eut(calculateRecipeEU(aMaterial, 45))
                .addTo(extruderRecipes);
        }

        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 2L))
                .fluidInputs(
                    Materials.Water.getFluid(
                        Math.max(
                            4,
                            Math.min(
                                1000,
                                ((int) Math.max(aMaterial.getMass(), 1L)) * (calculateRecipeEU(aMaterial, 16)) / 320))))
                .duration(2 * ((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                .eut(calculateRecipeEU(aMaterial, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 2L))
                .fluidInputs(
                    GTModHandler.getDistilledWater(
                        Math.max(
                            3,
                            Math.min(
                                750,
                                ((int) Math.max(aMaterial.getMass(), 1L)) * (calculateRecipeEU(aMaterial, 16)) / 426))))
                .duration(2 * ((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                .eut(calculateRecipeEU(aMaterial, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 2L))
                .fluidInputs(
                    Materials.Lubricant.getFluid(
                        Math.max(
                            1,
                            Math.min(
                                250,
                                ((int) Math.max(aMaterial.getMass(), 1L)) * (calculateRecipeEU(aMaterial, 16))
                                    / 1280))))
                .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                .eut(calculateRecipeEU(aMaterial, 16))
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, aMaterial, 2L))
                .fluidInputs(
                    Materials.DimensionallyShiftedSuperfluid.getFluid(
                        Math.max(
                            1,
                            Math.min(
                                10,
                                ((int) Math.max(aMaterial.getMass(), 1L)) * (calculateRecipeEU(aMaterial, 16))
                                    / 4000))))
                .duration(((int) Math.max(aMaterial.getMass() / 2.5, 1L)) * TICKS)
                .eut(calculateRecipeEU(aMaterial, 16))
                .addTo(cutterRecipes);
        }
    }

    private void registerPlateAlloy(final String aOreDictName, final ItemStack aStack) {

        switch (aOreDictName) {
            case "plateAlloyCarbon" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("generator", 1L), GTUtility.copyAmount(4, aStack))
                    .itemOutputs(GTModHandler.getIC2Item("windMill", 1L))
                    .duration(5 * MINUTES + 20 * SECONDS)
                    .eut(8)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack), new ItemStack(Blocks.glass, 3, WILDCARD))
                    .itemOutputs(GTModHandler.getIC2Item("reinforcedGlass", 4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack), Materials.Glass.getDust(3))
                    .itemOutputs(GTModHandler.getIC2Item("reinforcedGlass", 4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
            }
            case "plateAlloyAdvanced" -> {
                RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack), new ItemStack(Blocks.glass, 3, WILDCARD))
                    .itemOutputs(GTModHandler.getIC2Item("reinforcedGlass", 4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
                RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack), Materials.Glass.getDust(3))
                    .itemOutputs(GTModHandler.getIC2Item("reinforcedGlass", 4L))
                    .duration(20 * SECONDS)
                    .eut(4)
                    .addTo(alloySmelterRecipes);
            }
            case "plateAlloyIridium" ->

                // Remove IC2 Shaped recipe for Iridium Reinforced Plate
                GTModHandler.removeRecipeByOutputDelayed(aStack);
            default -> {}
        }
    }

    private void registerCover(final Materials aMaterial, final ItemStack aStack) {

        // Get ItemStack of Block matching Materials
        ItemStack tStack = NI;
        // Try different prefixes to use same smooth stones as older GT5U
        for (OrePrefixes orePrefix : new OrePrefixes[] { OrePrefixes.block, OrePrefixes.block_, OrePrefixes.stoneSmooth,
            OrePrefixes.stone }) {
            if ((tStack = GTOreDictUnificator.get(orePrefix, aMaterial, 1)) != NI) break;
        }

        // Register the cover
        CoverRegistry.registerDecorativeCover(
            aStack,
            // If there is an ItemStack of Block for Materials
            tStack == NI ?
            // Use Materials mRGBa dyed blocs/materialicons/MATERIALSET/block1 icons
                TextureFactory.builder()
                    .addIcon(aMaterial.mIconSet.mTextures[TextureSet.INDEX_block1])
                    .setRGBA(aMaterial.mRGBa)
                    .stdOrient()
                    .build()
                :
                // or copy Block texture
                TextureFactory.of(Block.getBlockFromItem(tStack.getItem()), tStack.getItemDamage()));
    }
}
