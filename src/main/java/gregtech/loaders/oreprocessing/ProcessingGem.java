package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingGem implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO COMPARE WITH GEM???
                                                                                      // generators

    public ProcessingGem() {
        OrePrefixes.gem.add(this);
        OrePrefixes.gemChipped.add(this);
        OrePrefixes.gemExquisite.add(this);
        OrePrefixes.gemFlawed.add(this);
        OrePrefixes.gemFlawless.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        long aMaterialMass = aMaterial.getMass();
        boolean aNoSmashing = aMaterial.contains(SubTag.NO_SMASHING);
        boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        boolean aNoSmelting = aMaterial.contains(SubTag.NO_SMELTING);
        boolean aSpecialRecipeReq = aMaterial.contains(SubTag.MORTAR_GRINDABLE);
        boolean aFuelPower = aMaterial.mFuelPower > 0;

        switch (aPrefix) {
            case gem -> {
                // fuel recipes
                if (aFuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 2)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!OrePrefixes.block.isIgnored(aMaterial)
                    && GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L) != null) {
                    // Compressor recipes
                    // need to avoid iridium exploit
                    if (aMaterial != Materials.Iridium) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, aStack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                }

                // Smelting recipe
                if (!aNoSmelting) {
                    GTModHandler.addSmeltingRecipe(
                        GTUtility.copyAmount(1, aStack),
                        GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L));
                }

                if (aNoSmashing) {
                    // Forge hammer recipes
                    {
                        if (GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(aStack)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L))
                                .duration(3 * SECONDS + 4 * TICKS)
                                .eut(16)
                                .addTo(hammerRecipes);
                        }
                    }
                } else {
                    // Forge hammer recipes
                    {
                        // need to avoid iridium exploit
                        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null
                            && aMaterial != Materials.Iridium) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass, 1L))
                                .eut(calculateRecipeEU(aMaterial, 16))
                                .addTo(hammerRecipes);
                        }
                    }

                    // Bender recipes
                    if (aMaterial != Materials.Iridium) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                            // Plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .duration((int) Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null) {
                            // Double plates
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, aStack), GTUtility.getIntegratedCircuit(2))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L) != null) {
                            // Triple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, aStack), GTUtility.getIntegratedCircuit(3))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 3L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L) != null) {
                            // Quadruple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(4, aStack), GTUtility.getIntegratedCircuit(4))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 4L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L) != null) {
                            // Quintuple plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(5, aStack), GTUtility.getIntegratedCircuit(5))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 5L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L) != null) {
                            // dense plate
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, aStack), GTUtility.getIntegratedCircuit(9))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 9L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }
                    }
                }
                if (aNoWorking) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        // Lathe recipes
                        if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null
                            && GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L))
                                .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 16))
                                .addTo(latheRecipes);
                        }
                    }
                } else {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L),
                                GTProxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawless.get(aMaterial) });

                            if (aMaterial.contains(SubTag.SMELTING_TO_GEM)) {
                                GTModHandler.addCraftingRecipe(
                                    GTUtility.copyAmount(1, aStack),
                                    GTProxy.tBits,
                                    new Object[] { "XXX", "XXX", "XXX", 'X', OrePrefixes.nugget.get(aMaterial) });
                            }

                            if (aSpecialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                    GTProxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gem.get(aMaterial) });
                            }
                        }
                    }
                }

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver
                    // Laser engraver recipes
                    {

                        if (GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1L) != null) {
                            is.stackSize = 0;

                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(3, aStack), is)
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1L))
                                .duration(60 * SECONDS)
                                .eut(TierEU.RECIPE_HV)
                                .addTo(laserEngraverRecipes);
                        }

                    }
                }
            }
            case gemChipped -> {
                // Fuel recipes
                if (aFuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower / 2)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1L))
                            .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                            .eut(8)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L),
                                GTProxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawed.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L),
                                    GTProxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemChipped.get(aMaterial) });
                            }
                        }
                    }
                }
                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver
                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, aStack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1L))
                        .duration(30 * SECONDS)
                        .eut(30)
                        .addTo(laserEngraverRecipes);

                }
            }
            case gemExquisite -> {
                // Fuel recipes
                if (aFuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 8)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Crafting recipes
                        {
                            if (aSpecialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 4L),
                                    GTProxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemExquisite.get(aMaterial) });
                            }
                        }
                    }
                }

                // Forge hammer recipes
                {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(16)
                        .addTo(hammerRecipes);
                }
            }
            case gemFlawed -> {
                // fuel recipes
                if (aFuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 2L),
                                GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L))
                            .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                            .eut(12)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L),
                                GTProxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gem.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L),
                                    GTProxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawed.get(aMaterial) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(aStack)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(16)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;

                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, aStack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .duration(30 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(laserEngraverRecipes);

                }
            }
            case gemFlawless -> {

                // Fuel recipes
                if (aFuelPower) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 4)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                                GTOreDictUnificator.getDust(
                                    aMaterial,
                                    aPrefix.mMaterialAmount - OrePrefixes.stickLong.mMaterialAmount))
                            .duration(((int) Math.max(aMaterialMass * 5L, 1L)) * TICKS)
                            .eut(16)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(
                                        GTUtility.copyAmount(3, aStack),
                                        GTModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L),
                                GTProxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemExquisite.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GTModHandler.addCraftingRecipe(
                                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L),
                                    GTProxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawless.get(aMaterial) });
                            }
                        }
                    }
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(aStack)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(16)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, aStack), is)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L))
                        .duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_EV)
                        .addTo(laserEngraverRecipes);
                }
            }
            default -> {}
        }
    }
}
