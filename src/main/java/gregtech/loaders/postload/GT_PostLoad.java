package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.recipe.RecipeMaps.massFabFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.rockBreakerFakeRecipes;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Stopwatch;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_CLS_Compat;
import gregtech.api.util.GT_Forestry_Compat;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeRegistrator;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

@SuppressWarnings("deprecation")
public class GT_PostLoad {

    public static void activateOreDictHandler() {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GT_Mod.gregtechproxy.activateOreDictHandler();

        // noinspection UnstableApiUsage// Stable enough for this project
        GT_Mod.GT_FML_LOGGER
            .info("Congratulations, you have been waiting long enough (" + stopwatch.stop() + "). Have a Cake.");
        GT_Log.out.println(
            "GT_Mod: List of Lists of Tool Recipes: "
                + GT_ModHandler.sSingleNonBlockDamagableRecipeList_list.toString());
        GT_Log.out.println(
            "GT_Mod: Vanilla Recipe List -> Outputs null or stackSize <=0: "
                + GT_ModHandler.sVanillaRecipeList_warntOutput.toString());
        GT_Log.out.println(
            "GT_Mod: Single Non Block Damageable Recipe List -> Outputs null or stackSize <=0: "
                + GT_ModHandler.sSingleNonBlockDamagableRecipeList_warntOutput.toString());
    }

    public static void removeIc2Recipes(Map<IRecipeInput, RecipeOutput> aMaceratorRecipeList,
        Map<IRecipeInput, RecipeOutput> aCompressorRecipeList, Map<IRecipeInput, RecipeOutput> aExtractorRecipeList,
        Map<IRecipeInput, RecipeOutput> aOreWashingRecipeList,
        Map<IRecipeInput, RecipeOutput> aThermalCentrifugeRecipeList) {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        // remove gemIridium exploit
        ItemStack iridiumOre = GT_ModHandler.getIC2Item("iridiumOre", 1);
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
        // Add default IC2 recipe to GT
        GT_ModHandler.addIC2RecipesToGT(aMaceratorRecipeList, RecipeMaps.maceratorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aCompressorRecipeList, RecipeMaps.compressorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aExtractorRecipeList, RecipeMaps.extractorRecipes, true, true, true);
        GT_ModHandler.addIC2RecipesToGT(aOreWashingRecipeList, RecipeMaps.oreWasherRecipes, false, true, true);
        GT_ModHandler
            .addIC2RecipesToGT(aThermalCentrifugeRecipeList, RecipeMaps.thermalCentrifugeRecipes, true, true, true);
        // noinspection UnstableApiUsage// Stable enough for this project
        GT_Mod.GT_FML_LOGGER.info("IC2 Removal (" + stopwatch.stop() + "). Have a Cake.");
    }

    public static void registerFluidCannerRecipes() {
        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            // lava clay bucket is registered with empty container with 0 stack size
            ItemStack emptyContainer = tData.emptyContainer.copy();
            emptyContainer.stackSize = 1;
            GT_Values.RA.stdBuilder()
                .itemInputs(emptyContainer)
                .itemOutputs(tData.filledContainer)
                .fluidInputs(tData.fluid)
                .duration((tData.fluid.amount / 62) * TICKS)
                .eut(1)
                .addTo(fluidCannerRecipes);
            GT_RecipeBuilder builder = GT_Values.RA.stdBuilder()
                .itemInputs(tData.filledContainer);
            if (tData.emptyContainer.stackSize > 0) {
                builder.itemOutputs(tData.emptyContainer);
            }
            builder.fluidOutputs(tData.fluid)
                .duration((tData.fluid.amount / 62) * TICKS)
                .eut(1)
                .addTo(fluidCannerRecipes);
        }
    }

    public static void addFakeRecipes() {
        GT_Log.out.println("GT_Mod: Adding Fake Recipes for NEI");

        if (Forestry.isModLoaded()) {
            GT_Forestry_Compat.populateFakeNeiRecipes();
        }

        if (ItemList.IC2_Crop_Seeds.get(1L) != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.IC2_Crop_Seeds.getWildcard(1L))
                .itemOutputs(ItemList.IC2_Crop_Seeds.getWithName(1L, "Scanned Seeds"))
                .duration(8 * SECONDS)
                .eut(8)
                .noOptimize()
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.written_book, 1, 32767))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Book Data"))
            .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.filled_map, 1, 32767))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Map Data"))
            .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataOrb.getWithName(1L, "Orb to overwrite"))
            .itemOutputs(ItemList.Tool_DataOrb.getWithName(1L, "Copy of the Orb"))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataStick.getWithName(1L, "Stick to overwrite"))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Copy of the Stick"))
            .special(ItemList.Tool_DataStick.getWithName(0L, "Stick to copy"))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataStick.getWithName(1L, "Raw Prospection Data"))
            .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Analyzed Prospection Data"))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .ignoreCollision()
            .fake()
            .addTo(scannerFakeRecipes);

        if (GalacticraftCore.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    Objects
                        .requireNonNull(
                            GT_ModHandler.getModItem(GalacticraftCore.ID, "item.schematic", 1, Short.MAX_VALUE))
                        .setStackDisplayName("Any Schematic"))
                .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                .duration(30 * MINUTES)
                .eut(TierEU.RECIPE_HV)
                .noOptimize()
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes);

            if (GalacticraftMars.isModLoaded()) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        Objects
                            .requireNonNull(
                                GT_ModHandler.getModItem(GalacticraftMars.ID, "item.schematic", 1, Short.MAX_VALUE))
                            .setStackDisplayName("Any Schematic"))
                    .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                    .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                    .duration(30 * MINUTES)
                    .eut(TierEU.RECIPE_MV)
                    .noOptimize()
                    .ignoreCollision()
                    .fake()
                    .addTo(scannerFakeRecipes);
            }
            if (GalaxySpace.isModLoaded()) {
                for (int i = 4; i < 9; i++) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_ModHandler.getModItem(GalaxySpace.ID, "item.SchematicTier" + i, 1)
                                .setStackDisplayName("Any Schematic"))
                        .itemOutputs(ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic"))
                        .special(ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"))
                        .duration(30 * MINUTES)
                        .eut(TierEU.RECIPE_HV)
                        .noOptimize()
                        .ignoreCollision()
                        .fake()
                        .addTo(scannerFakeRecipes);
                }
            }
        }
        Materials.getMaterialsMap()
            .values()
            .forEach(tMaterial -> {
                if ((tMaterial.mElement != null) && (!tMaterial.mElement.mIsIsotope)
                    && (tMaterial != Materials.Magic)
                    && (tMaterial.getMass() > 0L)) {
                    ItemStack dataOrb = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(dataOrb, "Elemental-Scan");
                    Behaviour_DataOrb.setDataName(dataOrb, tMaterial.mElement.name());
                    ItemStack dustItem = GT_OreDictUnificator.get(OrePrefixes.dust, tMaterial, 1L);
                    if (dustItem != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(dustItem)
                            .itemOutputs(dataOrb)
                            .special(ItemList.Tool_DataOrb.get(1L))
                            .duration((int) (tMaterial.getMass() * 8192L))
                            .eut(TierEU.RECIPE_LV)
                            .fake()
                            .ignoreCollision()
                            .addTo(scannerFakeRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemOutputs(dustItem)
                            .special(dataOrb)
                            .metadata(GT_RecipeConstants.MATERIAL, tMaterial)
                            .addTo(RecipeMaps.replicatorRecipes);
                        return;
                    }
                    ItemStack cellItem = GT_OreDictUnificator.get(OrePrefixes.cell, tMaterial, 1L);
                    if (cellItem != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(cellItem)
                            .itemOutputs(dataOrb)
                            .special(ItemList.Tool_DataOrb.get(1L))
                            .duration((int) (tMaterial.getMass() * 8192L))
                            .eut(TierEU.RECIPE_LV)
                            .fake()
                            .ignoreCollision()
                            .addTo(scannerFakeRecipes);
                        FluidStack fluidStack = GT_Utility.getFluidForFilledItem(cellItem, false);
                        GT_RecipeBuilder builder = GT_Values.RA.stdBuilder();
                        if (fluidStack != null) {
                            builder.fluidOutputs(fluidStack);
                        } else {
                            builder.itemInputs(Materials.Empty.getCells(1))
                                .itemOutputs(cellItem);
                        }
                        builder.special(dataOrb)
                            .metadata(GT_RecipeConstants.MATERIAL, tMaterial)
                            .addTo(RecipeMaps.replicatorRecipes);
                    }
                }
            });

        if (!GT_MetaTileEntity_Massfabricator.sRequiresUUA) {

            GT_MetaTileEntity_Massfabricator.nonUUARecipe = GT_Values.RA.stdBuilder()
                .fluidOutputs(Materials.UUMatter.getFluid(1L))
                .duration(GT_MetaTileEntity_Massfabricator.sDurationMultiplier)
                .eut(GT_MetaTileEntity_Massfabricator.BASE_EUT)
                .ignoreCollision()
                .noOptimize()
                .fake()
                .build()
                .get();

            massFabFakeRecipes.add(GT_MetaTileEntity_Massfabricator.nonUUARecipe);

        }

        GT_MetaTileEntity_Massfabricator.uuaRecipe = GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.UUAmplifier.getFluid(GT_MetaTileEntity_Massfabricator.sUUAperUUM))
            .fluidOutputs(Materials.UUMatter.getFluid(1L))
            .duration(
                GT_MetaTileEntity_Massfabricator.sDurationMultiplier / GT_MetaTileEntity_Massfabricator.sUUASpeedBonus)
            .eut(GT_MetaTileEntity_Massfabricator.BASE_EUT)
            .ignoreCollision()
            .noOptimize()
            .fake()
            .build()
            .get();

        massFabFakeRecipes.add(GT_MetaTileEntity_Massfabricator.uuaRecipe);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Display_ITS_FREE.getWithName(1L, "IT'S FREE! Place Lava on Side"))
            .itemOutputs(new ItemStack(Blocks.cobblestone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .noOptimize()
            .fake()
            .addTo(rockBreakerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Display_ITS_FREE.getWithName(1L, "IT'S FREE! Place Lava on Side"))
            .itemOutputs(new ItemStack(Blocks.stone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .noOptimize()
            .fake()
            .addTo(rockBreakerFakeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.obsidian, 1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .ignoreCollision()
            .noOptimize()
            .fake()
            .addTo(rockBreakerFakeRecipes);
    }

    public static void nerfVanillaTools() {
        if (!GT_Mod.gregtechproxy.mNerfedVanillaTools) {
            return;
        }

        GT_Log.out.println("GT_Mod: Nerfing Vanilla Tool Durability");
        Items.wooden_sword.setMaxDamage(12);
        Items.wooden_pickaxe.setMaxDamage(12);
        Items.wooden_shovel.setMaxDamage(12);
        Items.wooden_axe.setMaxDamage(12);
        Items.wooden_hoe.setMaxDamage(12);

        Items.stone_sword.setMaxDamage(48);
        Items.stone_pickaxe.setMaxDamage(48);
        Items.stone_shovel.setMaxDamage(48);
        Items.stone_axe.setMaxDamage(48);
        Items.stone_hoe.setMaxDamage(48);

        Items.iron_sword.setMaxDamage(256);
        Items.iron_pickaxe.setMaxDamage(256);
        Items.iron_shovel.setMaxDamage(256);
        Items.iron_axe.setMaxDamage(256);
        Items.iron_hoe.setMaxDamage(256);

        Items.golden_sword.setMaxDamage(24);
        Items.golden_pickaxe.setMaxDamage(24);
        Items.golden_shovel.setMaxDamage(24);
        Items.golden_axe.setMaxDamage(24);
        Items.golden_hoe.setMaxDamage(24);

        Items.diamond_sword.setMaxDamage(768);
        Items.diamond_pickaxe.setMaxDamage(768);
        Items.diamond_shovel.setMaxDamage(768);
        Items.diamond_axe.setMaxDamage(768);
        Items.diamond_hoe.setMaxDamage(768);

    }

    public static void replaceVanillaMaterials() {
        @SuppressWarnings("UnstableApiUsage") // Stable enough for this project
        Stopwatch stopwatch = Stopwatch.createStarted();
        GT_Mod.GT_FML_LOGGER.info("Replacing Vanilla Materials in recipes, please wait.");
        Set<Materials> replaceVanillaItemsSet = GT_Mod.gregtechproxy.mUseGreatlyShrukenReplacementList
            ? Arrays.stream(Materials.values())
                .filter(GT_RecipeRegistrator::hasVanillaRecipes)
                .collect(Collectors.toSet())
            : new HashSet<>(Arrays.asList(Materials.values()));

        ProgressManager.ProgressBar progressBar = ProgressManager
            .push("Register materials", replaceVanillaItemsSet.size());
        if (GT_Values.cls_enabled) {
            try {
                GT_CLS_Compat.doActualRegistrationCLS(progressBar, replaceVanillaItemsSet);
                GT_CLS_Compat.pushToDisplayProgress();
            } catch (InvocationTargetException | IllegalAccessException e) {
                GT_Mod.GT_FML_LOGGER.catching(e);
            }
        } else {
            replaceVanillaItemsSet.forEach(m -> {
                progressBar.step(m.mDefaultLocalName);
                doActualRegistration(m);
            });
        }
        ProgressManager.pop(progressBar);
        // noinspection UnstableApiUsage// stable enough for project
        GT_Mod.GT_FML_LOGGER.info("Replaced Vanilla Materials (" + stopwatch.stop() + "). Have a Cake.");
    }

    public static void doActualRegistration(Materials m) {
        String plateName = OrePrefixes.plate.get(m)
            .toString();
        boolean noSmash = !m.contains(SubTag.NO_SMASHING);
        if ((m.mTypes & 2) != 0) GT_RecipeRegistrator.registerUsagesForMaterials(plateName, noSmash, m.getIngots(1));
        if ((m.mTypes & 4) != 0) GT_RecipeRegistrator.registerUsagesForMaterials(plateName, noSmash, m.getGems(1));
        if (m.getBlocks(1) != null) GT_RecipeRegistrator.registerUsagesForMaterials(null, noSmash, m.getBlocks(1));
    }

    public static void createGTtoolsCreativeTab() {
        new CreativeTabs("GTtools") {

            @SideOnly(Side.CLIENT)
            @Override
            public ItemStack getIconItemStack() {
                return ItemList.Tool_Cheat.get(1, new ItemStack(Blocks.iron_block, 1));
            }

            @SideOnly(Side.CLIENT)
            @Override
            public Item getTabIconItem() {
                return ItemList.Circuit_Integrated.getItem();
            }

            @Override
            public void displayAllReleventItems(List<ItemStack> aList) {

                for (int i = 0; i < 32766; i += 2) {
                    if (GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolStats(new ItemStack(GT_MetaGenerated_Tool_01.INSTANCE, 1, i)) == null) {
                        continue;
                    }

                    ItemStack tStack = new ItemStack(GT_MetaGenerated_Tool_01.INSTANCE, 1, i);
                    GT_MetaGenerated_Tool_01.INSTANCE.isItemStackUsable(tStack);
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(i, 1, Materials.Lead, Materials.Lead, null));
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE
                            .getToolWithStats(i, 1, Materials.Nickel, Materials.Nickel, null));
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE
                            .getToolWithStats(i, 1, Materials.Cobalt, Materials.Cobalt, null));
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE
                            .getToolWithStats(i, 1, Materials.Osmium, Materials.Osmium, null));
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE
                            .getToolWithStats(i, 1, Materials.Adamantium, Materials.Adamantium, null));
                    aList.add(
                        GT_MetaGenerated_Tool_01.INSTANCE
                            .getToolWithStats(i, 1, Materials.Neutronium, Materials.Neutronium, null));

                }
                super.displayAllReleventItems(aList);
            }
        };
    }

    public static void addSolidFakeLargeBoilerFuels() {
        RecipeMaps.largeBoilerFakeFuels.getBackend()
            .addSolidRecipes(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 1),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Coal, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lignite, 1),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Lignite, 1),
                GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.plank, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.slab, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(ItemList.Block_SSFUEL.get(1)),
                GT_OreDictUnificator.get(ItemList.Block_MSSFUEL.get(1)),
                GT_OreDictUnificator.get(OrePrefixes.rod, Materials.Blaze, 1));
        if (Thaumcraft.isModLoaded()) {
            RecipeMaps.largeBoilerFakeFuels.getBackend()
                .addSolidRecipe(GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1));
        }
    }

    public static void identifyAnySteam() {
        final String[] steamCandidates = { "steam", "ic2steam" };
        final String[] superHeatedSteamCandidates = { "ic2superheatedsteam" };

        GT_ModHandler.sAnySteamFluidIDs = Arrays.stream(steamCandidates)
            .map(FluidRegistry::getFluid)
            .filter(Objects::nonNull)
            .map(FluidRegistry::getFluidID)
            .collect(Collectors.toList());
        GT_ModHandler.sSuperHeatedSteamFluidIDs = Arrays.stream(superHeatedSteamCandidates)
            .map(FluidRegistry::getFluid)
            .filter(Objects::nonNull)
            .map(FluidRegistry::getFluidID)
            .collect(Collectors.toList());
    }
}
