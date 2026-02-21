package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.BetterLoadingScreen;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.massFabFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Stopwatch;

import cpw.mods.fml.common.ProgressManager;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTCLSCompat;
import gregtech.api.util.GTForestryCompat;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTScannerResult;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.basic.MTEMassfabricator;
import gregtech.common.tileentities.machines.basic.MTERockBreaker;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

@SuppressWarnings("deprecation")
public class GTPostLoad {

    public static void activateOreDictHandler() {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GTMod.proxy.activateOreDictHandler();

        // noinspection UnstableApiUsage// Stable enough for this project
        GTMod.GT_FML_LOGGER
            .info("Congratulations, you have been waiting long enough (" + stopwatch.stop() + "). Have a Cake.");
        GTLog.out.println(
            "GTMod: List of Lists of Tool Recipes: " + GTModHandler.sSingleNonBlockDamagableRecipeList_list.toString());
        GTLog.out.println(
            "GTMod: Vanilla Recipe List -> Outputs null or stackSize <=0: "
                + GTModHandler.sVanillaRecipeList_warntOutput.toString());
        GTLog.out.println(
            "GTMod: Single Non Block Damageable Recipe List -> Outputs null or stackSize <=0: "
                + GTModHandler.sSingleNonBlockDamagableRecipeList_warntOutput.toString());
    }

    public static void removeIc2Recipes(Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList,
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList, Map<IRecipeInput, RecipeOutput> aExtractorRecipeList,
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList,
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList) {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        // remove gemIridium exploit
        ItemStack iridiumOre = GTModHandler.getIC2Item("iridiumOre", 1);
        aCompressorRecipeList.entrySet()
            .parallelStream()
            .filter(
                e -> e.getKey()
                    .getInputs()
                    .size() == 1 && e.getKey()
                        .getInputs()
                        .get(0)
                        .isItemEqual(iridiumOre))
            .findAny()
            .ifPresent(e -> aCompressorRecipeList.remove(e.getKey()));
        // Remove all IC2
        GTModHandler.removeAllIC2Recipes();
        // noinspection UnstableApiUsage// Stable enough for this project
        GTMod.GT_FML_LOGGER.info("IC2 Removal (" + stopwatch.stop() + "). Have a Cake.");
    }

    public static void registerFluidCannerRecipes() {
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if (tData.fluid.amount <= 0) {
                continue;
            }
            // lava clay bucket is registered with empty container with 0 stack size
            ItemStack emptyContainer = tData.emptyContainer.copy();
            emptyContainer.stackSize = 1;
            GTValues.RA.stdBuilder()
                .itemInputs(emptyContainer)
                .itemOutputs(tData.filledContainer)
                .fluidInputs(tData.fluid)
                .duration((tData.fluid.amount / 62) * TICKS)
                .eut(1)
                .addTo(cannerRecipes);
            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemInputs(tData.filledContainer);
            if (tData.emptyContainer.stackSize > 0) {
                builder.itemOutputs(tData.emptyContainer);
            }
            builder.fluidOutputs(tData.fluid)
                .duration((tData.fluid.amount / 62) * TICKS)
                .eut(1)
                .addTo(cannerRecipes);
        }
    }

    public static void addFakeRecipes() {
        GTLog.out.println("GTMod: Adding Fake Recipes for NEI");

        if (Forestry.isModLoaded()) {
            GTForestryCompat.populateFakeNeiRecipes();
        }

        if (ItemList.IC2_Crop_Seeds.get(1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Crop_Seeds.getWildcard(1L))
                .itemOutputs(ItemList.IC2_Crop_Seeds.getWithName(1L, "Scanned Seeds"))
                .duration(8 * SECONDS)
                .eut(8)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.written_book, 1, 32767))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Book Data"))
            .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.filled_map, 1, 32767))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Map Data"))
            .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataOrb.getWithName(1L, "Orb to overwrite"))
            .itemOutputs(ItemList.Tool_DataOrb.getWithName(1L, "Copy of the Orb"))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataStick.getWithName(1L, "Stick to overwrite"))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Copy of the Stick"))
            .special(ItemList.Tool_DataStick.getWithName(0L, "Stick to copy"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataStick.getWithName(1L, "Raw Prospection Data"))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Analyzed Prospection Data"))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        if (GalacticraftCore.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    Objects
                        .requireNonNull(
                            GTModHandler.getModItem(GalacticraftCore.ID, "item.schematic", 1, Short.MAX_VALUE))
                        .setStackDisplayName("Any Schematic"))
                .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                .duration(30 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);

            if (GalacticraftMars.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        Objects
                            .requireNonNull(
                                GTModHandler.getModItem(GalacticraftMars.ID, "item.schematic", 1, Short.MAX_VALUE))
                            .setStackDisplayName("Any Schematic"))
                    .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                    .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                    .duration(30 * MINUTES)
                    .eut(TierEU.RECIPE_MV)
                    .ignoreCollision()
                    .fake()
                    .addTo(scannerFakeRecipes);
            }
            if (GalaxySpace.isModLoaded()) {
                for (int i = 4; i < 9; i++) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTModHandler.getModItem(GalaxySpace.ID, "item.SchematicTier" + i, 1)
                                .setStackDisplayName("Any Schematic"))
                        .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                        .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                        .duration(30 * MINUTES)
                        .eut(TierEU.RECIPE_HV)
                        .ignoreCollision()
                        .fake()
                        .addTo(scannerFakeRecipes);
                }
            }
        }
        Materials.getMaterialsMap()
            .values()
            .forEach(tMaterial -> {
                // check if material is scannable
                GTScannerResult scannerResult = ScannerHandlerLoader.getElementScanResult(tMaterial);
                if (scannerResult == null || scannerResult.isNotMet()) return;

                // add recipe if a dust exists.
                GTRecipeBuilder builder = null;
                ItemStack dustItem = GTOreDictUnificator.get(OrePrefixes.dust, tMaterial, 1L);
                if (dustItem != null) {
                    builder = GTValues.RA.stdBuilder()
                        .itemInputs(dustItem);
                    // add corresponding fluid replicator recipe.
                    GTValues.RA.stdBuilder()
                        .itemOutputs(dustItem)
                        .special(scannerResult.output)
                        .metadata(GTRecipeConstants.MATERIAL, tMaterial)
                        .addTo(RecipeMaps.replicatorRecipes);
                }
                // else try to add a recipe for the cell.
                if (builder == null) {
                    ItemStack cellItem = GTOreDictUnificator.get(OrePrefixes.cell, tMaterial, 1L);
                    if (cellItem == null) return;
                    // create builder
                    builder = GTValues.RA.stdBuilder()
                        .itemInputs(cellItem);
                    // add corresponding replicator recipe
                    FluidStack fluidStack = GTUtility.getFluidForFilledItem(cellItem, false);
                    GTRecipeBuilder replicatorRecipeBuilder = GTValues.RA.stdBuilder();
                    if (fluidStack != null) {
                        replicatorRecipeBuilder.fluidOutputs(fluidStack);
                    } else {
                        // if there is no fluid for some reason, add a cell recipe, with cell input.
                        replicatorRecipeBuilder.itemInputs(Materials.Empty.getCells(1))
                            .itemOutputs(cellItem);
                    }
                    replicatorRecipeBuilder.special(scannerResult.output)
                        .metadata(GTRecipeConstants.MATERIAL, tMaterial)
                        .addTo(RecipeMaps.replicatorRecipes);
                }

                builder.itemOutputs(scannerResult.output)
                    .special(ItemList.Tool_DataOrb.get(1L))
                    .duration(scannerResult.duration)
                    .eut(scannerResult.eut)
                    .fake()
                    .ignoreCollision()
                    .addTo(scannerFakeRecipes);
            });

        if (!MTEMassfabricator.sRequiresUUA) {

            MTEMassfabricator.nonUUARecipe = GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.UUMatter.getFluid(1L))
                .duration(MTEMassfabricator.sDurationMultiplier)
                .eut(MTEMassfabricator.BASE_EUT)
                .ignoreCollision()
                .fake()
                .build()
                .get();

            massFabFakeRecipes.add(MTEMassfabricator.nonUUARecipe);

        }

        MTEMassfabricator.uuaRecipe = GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.UUAmplifier.getFluid(MTEMassfabricator.sUUAperUUM))
            .fluidOutputs(Materials.UUMatter.getFluid(1L))
            .duration(MTEMassfabricator.sDurationMultiplier / MTEMassfabricator.sUUASpeedBonus)
            .eut(MTEMassfabricator.BASE_EUT)
            .ignoreCollision()
            .fake()
            .build()
            .get();

        massFabFakeRecipes.add(MTEMassfabricator.uuaRecipe);

        MTERockBreaker.addRockBreakerRecipe(
            b -> b.recipeDescription(StatCollector.translateToLocal("gt.recipe.rockbreaker.fakeitem.top"))
                .sideBlocks(Blocks.water)
                .topBlock(Blocks.lava)
                .outputItem(new ItemStack(Blocks.stone, 1))
                .duration(16 * TICKS));

        MTERockBreaker.addRockBreakerRecipe(
            b -> b.recipeDescription(StatCollector.translateToLocal("gt.recipe.rockbreaker.fakeitem.side"))
                .sideBlocks(Blocks.water, Blocks.lava)
                .outputItem(new ItemStack(Blocks.cobblestone, 1))
                .duration(16 * TICKS));

        MTERockBreaker.addRockBreakerRecipe(
            b -> b.sideBlocks(Blocks.water)
                .anywhereBlocks(Blocks.lava)
                .inputItem(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), true)
                .circuit(1)
                .outputItem(new ItemStack(Blocks.obsidian, 1))
                .duration(6 * SECONDS + 8 * TICKS));

        MTERockBreaker.addRockBreakerRecipe(
            b -> b.sideBlocks(Blocks.water)
                .anywhereBlocks(Blocks.lava)
                .inputItem(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), true)
                .circuit(6)
                .outputItem(new ItemStack(Blocks.netherrack, 1))
                .duration(16 * TICKS));

        if (Mods.EtFuturumRequiem.isModLoaded()) {
            MTERockBreaker.addRockBreakerRecipe(
                b -> b.sideBlocks(Blocks.lava)
                    .bottomBlock(Blocks.soul_sand)
                    .inputItem(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "blue_ice", 0, 0), false)
                    .circuit(1)
                    .outputItem(GTOreDictUnificator.get(OrePrefixes.stone, Materials.Basalt, 1L))
                    .duration(16 * TICKS));

            MTERockBreaker.addRockBreakerRecipe(
                b -> b.sideBlocks(Blocks.lava)
                    .bottomBlock(Blocks.soul_sand)
                    .inputItem(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "magma", 0, 0), false)
                    .circuit(1)
                    .outputItem(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "cobbled_deepslate", 1, 0))
                    .duration(16 * TICKS));
        }
    }

    public static void changeWoodenVanillaTools() {
        if (!GTMod.proxy.mChangeWoodenVanillaTools) {
            return;
        }

        GTLog.out.println("GTMod: Updating Vanilla Wooden Tools");
        Items.wooden_sword.setMaxDamage(64);
        Items.wooden_pickaxe.setMaxDamage(64);
        Items.wooden_shovel.setMaxDamage(64);
        Items.wooden_axe.setMaxDamage(64);
        Items.wooden_hoe.setMaxDamage(64);
    }

    public static void replaceVanillaMaterials() {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GTMod.GT_FML_LOGGER.info("Replacing Vanilla Materials in recipes, please wait.");
        Set<Materials> replaceVanillaItemsSet = Arrays.stream(Materials.values())
            .filter(GTRecipeRegistrator::hasVanillaRecipes)
            .collect(Collectors.toSet());

        ProgressManager.ProgressBar progressBar = ProgressManager
            .push("Register materials", replaceVanillaItemsSet.size());
        if (BetterLoadingScreen.isModLoaded()) {
            GTCLSCompat.doActualRegistrationCLS(progressBar, replaceVanillaItemsSet);
            GTCLSCompat.pushToDisplayProgress();
        } else {
            replaceVanillaItemsSet.forEach(m -> {
                progressBar.step(m.mDefaultLocalName);
                doActualRegistration(m);
            });
        }
        ProgressManager.pop(progressBar);
        // noinspection UnstableApiUsage// stable enough for project
        GTMod.GT_FML_LOGGER.info("Replaced Vanilla Materials (" + stopwatch.stop() + "). Have a Cake.");
    }

    public static void doActualRegistration(Materials m) {
        String plateName = OrePrefixes.plate.get(m)
            .toString();
        boolean noSmash = !m.contains(SubTag.NO_SMASHING);
        if (m.hasMetalItems()) GTRecipeRegistrator.registerUsagesForMaterials(plateName, noSmash, m.getIngots(1));
        if (m.hasGemItems()) GTRecipeRegistrator.registerUsagesForMaterials(plateName, noSmash, m.getGems(1));
        if (m.getBlocks(1) != null) GTRecipeRegistrator.registerUsagesForMaterials(null, noSmash, m.getBlocks(1));
    }

    public static void addSolidFakeLargeBoilerFuels() {
        RecipeMaps.largeBoilerFakeFuels.getBackend()
            .addSolidRecipes(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Coal, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lignite, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Lignite, 1),
                GTOreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plank, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.slab, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(ItemList.Block_SSFUEL.get(1)),
                GTOreDictUnificator.get(ItemList.Block_MSSFUEL.get(1)),
                GTOreDictUnificator.get(OrePrefixes.rod, Materials.Blaze, 1));
        if (Thaumcraft.isModLoaded()) {
            RecipeMaps.largeBoilerFakeFuels.getBackend()
                .addSolidRecipe(GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1));
        }
    }

    public static void addCauldronRecipe() {
        for (Materials material : Materials.getAll()) {
            ItemStack dustImpure = GTOreDictUnificator.get(OrePrefixes.dustImpure, material, 1);
            ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, material, 1);

            if (dust == null || dustImpure == null) {
                continue;
            }

            GTValues.RA.stdBuilder()
                .itemInputs(dustImpure)
                .fluidInputs(Materials.Water.getFluid(333))
                .itemOutputs(dust)
                .duration(0)
                .eut(0)
                .fake()
                .addTo(RecipeMaps.cauldronRecipe);
        }
    }

    public static void identifyAnySteam() {
        final String[] steamCandidates = { "steam", "ic2steam" };
        final String[] superHeatedSteamCandidates = { "ic2superheatedsteam" };

        GTModHandler.sAnySteamFluidIDs = Arrays.stream(steamCandidates)
            .map(FluidRegistry::getFluid)
            .filter(Objects::nonNull)
            .map(FluidRegistry::getFluidID)
            .collect(Collectors.toList());
        GTModHandler.sSuperHeatedSteamFluidIDs = Arrays.stream(superHeatedSteamCandidates)
            .map(FluidRegistry::getFluid)
            .filter(Objects::nonNull)
            .map(FluidRegistry::getFluidID)
            .collect(Collectors.toList());
    }
}
