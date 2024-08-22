package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;
import static gregtech.api.util.RecipeBuilder.MINUTES;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;
import static gregtech.api.util.RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.RecipeConstants.FUEL_VALUE;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.api.util.RecipeConstants;
import gregtech.common.GT_Proxy;

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
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 2)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(RecipeConstants.Fuel);
                }

                if (!OrePrefixes.block.isIgnored(aMaterial)
                    && OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L) != null) {
                    // Compressor recipes
                    // need to avoid iridium exploit
                    if (aMaterial != Materials.Iridium) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(9, aStack))
                            .itemOutputs(OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                }

                // Smelting recipe
                if (!aNoSmelting) {
                    GT_ModHandler.addSmeltingRecipe(
                        GT_Utility.copyAmount(1, aStack),
                        OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L));
                }

                if (aNoSmashing) {
                    // Forge hammer recipes
                    {
                        if (OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(aStack)
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L))
                                .duration(3 * SECONDS + 4 * TICKS)
                                .eut(16)
                                .addTo(hammerRecipes);
                        }
                    }
                } else {
                    // Forge hammer recipes
                    {
                        // need to avoid iridium exploit
                        if (OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null
                            && aMaterial != Materials.Iridium) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass, 1L))
                                .eut(calculateRecipeEU(aMaterial, 16))
                                .addTo(hammerRecipes);
                        }
                    }

                    // Bender recipes
                    if (aMaterial != Materials.Iridium) {
                        if (OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                            // Plate
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                .duration((int) Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 24))
                                .addTo(benderRecipes);
                        }

                        if (OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null) {
                            // Double plates
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), GT_Utility.getIntegratedCircuit(2))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 2L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L) != null) {
                            // Triple plate
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(3, aStack), GT_Utility.getIntegratedCircuit(3))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 3L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L) != null) {
                            // Quadruple plate
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(4, aStack), GT_Utility.getIntegratedCircuit(4))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 4L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L) != null) {
                            // Quintuple plate
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(5, aStack), GT_Utility.getIntegratedCircuit(5))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 5L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }

                        if (OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L) != null) {
                            // dense plate
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(9, aStack), GT_Utility.getIntegratedCircuit(9))
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L))
                                .duration(Math.max(aMaterialMass * 9L, 1L))
                                .eut(calculateRecipeEU(aMaterial, 96))
                                .addTo(benderRecipes);
                        }
                    }
                }
                if (aNoWorking) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        // Lathe recipes
                        if (OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null
                            && OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack))
                                .itemOutputs(
                                    OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                                    OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L))
                                .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 16))
                                .addTo(latheRecipes);
                        }
                    }
                } else {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GT_ModHandler.addCraftingRecipe(
                                OreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L),
                                GT_Proxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawless.get(aMaterial) });

                            if (aMaterial.contains(SubTag.SMELTING_TO_GEM)) {
                                GT_ModHandler.addCraftingRecipe(
                                    GT_Utility.copyAmount(1, aStack),
                                    GT_Proxy.tBits,
                                    new Object[] { "XXX", "XXX", "XXX", 'X', OrePrefixes.nugget.get(aMaterial) });
                            }

                            if (aSpecialRecipeReq) {
                                GT_ModHandler.addCraftingRecipe(
                                    OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                    GT_Proxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gem.get(aMaterial) });
                            }
                        }
                    }
                }

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver
                    // Laser engraver recipes
                    {

                        if (OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1L) != null) {
                            is.stackSize = 0;

                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(3, aStack), is)
                                .itemOutputs(OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1L))
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
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower / 2)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(RecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null
                        && OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L),
                                OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1L))
                            .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                            .eut(8)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GT_ModHandler.addCraftingRecipe(
                                OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L),
                                GT_Proxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemFlawed.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GT_ModHandler.addCraftingRecipe(
                                    OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L),
                                    GT_Proxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemChipped.get(aMaterial) });
                            }
                        }
                    }
                }
                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver
                    is.stackSize = 0;

                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), is)
                        .itemOutputs(OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1L))
                        .duration(30 * SECONDS)
                        .eut(30)
                        .addTo(laserEngraverRecipes);

                }
            }
            case gemExquisite -> {
                // Fuel recipes
                if (aFuelPower) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 8)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(RecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Crafting recipes
                        {
                            if (aSpecialRecipeReq) {
                                GT_ModHandler.addCraftingRecipe(
                                    OreDictUnificator.get(OrePrefixes.dust, aMaterial, 4L),
                                    GT_Proxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemExquisite.get(aMaterial) });
                            }
                        }
                    }
                }

                // Forge hammer recipes
                {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack)
                        .itemOutputs(OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(16)
                        .addTo(hammerRecipes);
                }
            }
            case gemFlawed -> {
                // fuel recipes
                if (aFuelPower) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(RecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null
                        && OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 2L),
                                OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L))
                            .duration(((int) Math.max(aMaterialMass, 1L)) * TICKS)
                            .eut(12)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GT_ModHandler.addCraftingRecipe(
                                OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L),
                                GT_Proxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gem.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GT_ModHandler.addCraftingRecipe(
                                    OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L),
                                    GT_Proxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawed.get(aMaterial) });
                            }
                        }
                    }
                }
                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack)
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(16)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;

                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), is)
                        .itemOutputs(OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .duration(30 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(laserEngraverRecipes);

                }
            }
            case gemFlawless -> {

                // Fuel recipes
                if (aFuelPower) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower * 4)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(RecipeConstants.Fuel);
                }

                if (!aNoWorking) {
                    // Lathe recipes
                    if (OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null
                        && OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                                OreDictUnificator.getDust(
                                    aMaterial,
                                    aPrefix.mMaterialAmount - OrePrefixes.stickLong.mMaterialAmount))
                            .duration(((int) Math.max(aMaterialMass * 5L, 1L)) * TICKS)
                            .eut(16)
                            .addTo(latheRecipes);
                    }

                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        // Implosion compressor recipes
                        {
                            if (OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Block_Powderbarrel.get(16))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("dynamite", 4, null))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(3, aStack), new ItemStack(Blocks.tnt, 8))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(
                                        GT_Utility.copyAmount(3, aStack),
                                        GT_ModHandler.getIC2Item("industrialTnt", 2))
                                    .itemOutputs(
                                        OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1),
                                        OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2))
                                    .duration(1 * SECONDS)
                                    .eut(TierEU.RECIPE_LV)
                                    .addTo(implosionRecipes);
                            }
                        }

                        // Crafting recipes
                        {
                            GT_ModHandler.addCraftingRecipe(
                                OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L),
                                GT_Proxy.tBits,
                                new Object[] { "h", "X", 'X', OrePrefixes.gemExquisite.get(aMaterial) });
                            if (aSpecialRecipeReq) {
                                GT_ModHandler.addCraftingRecipe(
                                    OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L),
                                    GT_Proxy.tBits,
                                    new Object[] { "X", "m", 'X', OrePrefixes.gemFlawless.get(aMaterial) });
                            }
                        }
                    }
                }
                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack)
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(16)
                    .addTo(hammerRecipes);

                for (ItemStack is : OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""))) { // Engraver

                    is.stackSize = 0;
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), is)
                        .itemOutputs(OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L))
                        .duration(2 * MINUTES)
                        .eut(TierEU.RECIPE_EV)
                        .addTo(laserEngraverRecipes);
                }
            }
            default -> {}
        }
    }
}
