package gregtech.loaders.postload;

import static gregtech.api.enums.GT_Values.VP;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.IguanaTweaksTinkerConstruct;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes;
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
import gregtech.api.util.GT_CLS_Compat;
import gregtech.api.util.GT_Forestry_Compat;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
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
        GT_ModHandler
            .addIC2RecipesToGT(aMaceratorRecipeList, GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, true, true, true);
        GT_ModHandler
            .addIC2RecipesToGT(aCompressorRecipeList, GT_Recipe.GT_Recipe_Map.sCompressorRecipes, true, true, true);
        GT_ModHandler
            .addIC2RecipesToGT(aExtractorRecipeList, GT_Recipe.GT_Recipe_Map.sExtractorRecipes, true, true, true);
        GT_ModHandler
            .addIC2RecipesToGT(aOreWashingRecipeList, GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, false, true, true);
        GT_ModHandler.addIC2RecipesToGT(
            aThermalCentrifugeRecipeList,
            GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
            true,
            true,
            true);
        // noinspection UnstableApiUsage// Stable enough for this project
        GT_Mod.GT_FML_LOGGER.info("IC2 Removal (" + stopwatch.stop() + "). Have a Cake.");
    }

    public static void registerFluidCannerRecipes() {
        ItemStack iSData0 = new ItemStack(Items.potionitem, 1, 0);
        ItemStack iLData0 = ItemList.Bottle_Empty.get(1L);

        for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
            .getRegisteredFluidContainerData()) {
            if ((tData.filledContainer.getItem() == Items.potionitem) && (tData.filledContainer.getItemDamage() == 0)) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(iLData0)
                    .itemOutputs(iSData0)
                    .fluidInputs(Materials.Water.getFluid(250L))
                    .duration(4 * TICKS)
                    .eut(1)
                    .addTo(sFluidCannerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(iSData0)
                    .itemOutputs(iLData0)
                    .duration(4 * TICKS)
                    .eut(1)
                    .addTo(sFluidCannerRecipes);
            } else if (tData.emptyContainer.isItemEqual(
                Objects.requireNonNull(
                    GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0)))) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0))
                            .itemOutputs(tData.filledContainer)
                            .fluidInputs(tData.fluid)
                            .duration((tData.fluid.amount / 62) * TICKS)
                            .eut(1)
                            .addTo(sFluidCannerRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(tData.filledContainer)
                            .itemOutputs(
                                GT_ModHandler.getModItem(IguanaTweaksTinkerConstruct.ID, "clayBucketFired", 1L, 0))
                            .fluidOutputs(tData.fluid)
                            .duration((tData.fluid.amount / 62) * TICKS)
                            .eut(1)
                            .addTo(sFluidCannerRecipes);
                    } else {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(tData.emptyContainer)
                            .itemOutputs(tData.filledContainer)
                            .fluidInputs(tData.fluid)
                            .duration((tData.fluid.amount / 62) * TICKS)
                            .eut(1)
                            .addTo(sFluidCannerRecipes);
                        if (GT_Utility.getContainerItem(tData.filledContainer, true) == null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(tData.filledContainer)
                                .fluidOutputs(tData.fluid)
                                .duration((tData.fluid.amount / 62) * TICKS)
                                .eut(1)
                                .addTo(sFluidCannerRecipes);
                        } else {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(tData.filledContainer)
                                .itemOutputs(GT_Utility.getContainerItem(tData.filledContainer, true))
                                .fluidOutputs(tData.fluid)
                                .duration((tData.fluid.amount / 62) * TICKS)
                                .eut(1)
                                .addTo(sFluidCannerRecipes);
                        }
                    }
        }
    }

    public static void addFakeRecipes() {
        GT_Log.out.println("GT_Mod: Adding Fake Recipes for NEI");

        if (Forestry.isModLoaded()) {
            GT_Forestry_Compat.populateFakeNeiRecipes();
        }

        if (ItemList.IC2_Crop_Seeds.get(1L) != null) {
            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { ItemList.IC2_Crop_Seeds.getWildcard(1L) },
                new ItemStack[] { ItemList.IC2_Crop_Seeds.getWithName(1L, "Scanned Seeds") },
                null,
                null,
                null,
                160,
                8,
                0);
        }
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { new ItemStack(Items.written_book, 1, 32767) },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Scanned Book Data") },
            ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"),
            null,
            null,
            128,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { new ItemStack(Items.filled_map, 1, 32767) },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Scanned Map Data") },
            ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"),
            null,
            null,
            128,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { ItemList.Tool_DataOrb.getWithName(1L, "Orb to overwrite") },
            new ItemStack[] { ItemList.Tool_DataOrb.getWithName(1L, "Copy of the Orb") },
            ItemList.Tool_DataOrb.getWithName(0L, "Orb to copy"),
            null,
            null,
            512,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Stick to overwrite") },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Copy of the Stick") },
            ItemList.Tool_DataStick.getWithName(0L, "Stick to copy"),
            null,
            null,
            128,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Raw Prospection Data") },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Analyzed Prospection Data") },
            null,
            null,
            null,
            1000,
            30,
            0);
        if (GalacticraftCore.isModLoaded()) {
            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { Objects
                    .requireNonNull(GT_ModHandler.getModItem(GalacticraftCore.ID, "item.schematic", 1, Short.MAX_VALUE))
                    .setStackDisplayName("Any Schematic") },
                new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic") },
                ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"),
                null,
                null,
                36000,
                480,
                0);
            if (GalacticraftMars.isModLoaded()) GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { Objects
                    .requireNonNull(GT_ModHandler.getModItem(GalacticraftMars.ID, "item.schematic", 1, Short.MAX_VALUE))
                    .setStackDisplayName("Any Schematic") },
                new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic") },
                ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"),
                null,
                null,
                36000,
                480,
                0);
            if (GalaxySpace.isModLoaded()) {
                for (int i = 4; i < 9; i++) {
                    GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                        false,
                        new ItemStack[] { GT_ModHandler.getModItem(GalaxySpace.ID, "item.SchematicTier" + i, 1)
                            .setStackDisplayName("Any Schematic") },
                        new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Scanned Schematic") },
                        ItemList.Tool_DataStick.getWithName(1L, "Stick to save it to"),
                        null,
                        null,
                        36000,
                        480,
                        0);
                }
            }
        }
        Materials.getMaterialsMap()
            .values()
            .forEach(tMaterial -> {
                if ((tMaterial.mElement != null) && (!tMaterial.mElement.mIsIsotope)
                    && (tMaterial != Materials.Magic)
                    && (tMaterial.getMass() > 0L)) {
                    ItemStack tOutput = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(tOutput, "Elemental-Scan");
                    Behaviour_DataOrb.setDataName(tOutput, tMaterial.mElement.name());
                    ItemStack tInput = GT_OreDictUnificator.get(OrePrefixes.dust, tMaterial, 1L);
                    ItemStack[] iSMat0 = new ItemStack[] { tInput };
                    ItemStack[] iSMat1 = new ItemStack[] { tOutput };
                    if (tInput != null) {
                        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                            false,
                            iSMat0,
                            iSMat1,
                            ItemList.Tool_DataOrb.get(1L),
                            null,
                            null,
                            (int) (tMaterial.getMass() * 8192L),
                            30,
                            0);
                        GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(
                            false,
                            null,
                            iSMat0,
                            iSMat1,
                            new FluidStack[] { Materials.UUMatter.getFluid(tMaterial.getMass()) },
                            null,
                            (int) (tMaterial.getMass() * 512L),
                            (int) VP[1],
                            0);
                        return;
                    }
                    tInput = GT_OreDictUnificator.get(OrePrefixes.cell, tMaterial, 1L);
                    iSMat0 = new ItemStack[] { tInput };
                    if (tInput != null) {
                        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                            false,
                            iSMat0,
                            iSMat1,
                            ItemList.Tool_DataOrb.get(1L),
                            null,
                            null,
                            (int) (tMaterial.getMass() * 8192L),
                            30,
                            0);
                        GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(
                            false,
                            null,
                            iSMat0,
                            iSMat1,
                            new FluidStack[] { Materials.UUMatter.getFluid(tMaterial.getMass()) },
                            null,
                            (int) (tMaterial.getMass() * 512L),
                            (int) VP[1],
                            0);
                    }
                }
            });

        if (!GT_MetaTileEntity_Massfabricator.sRequiresUUA) GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes.addFakeRecipe(
            false,
            null,
            null,
            null,
            null,
            new FluidStack[] { Materials.UUMatter.getFluid(1L) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier,
            256,
            0);
        GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
            null,
            null,
            new FluidStack[] { Materials.UUAmplifier.getFluid(GT_MetaTileEntity_Massfabricator.sUUAperUUM) },
            new FluidStack[] { Materials.UUMatter.getFluid(1L) },
            GT_MetaTileEntity_Massfabricator.sDurationMultiplier / GT_MetaTileEntity_Massfabricator.sUUASpeedBonus,
            256,
            0);
        GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { ItemList.Display_ITS_FREE.getWithName(1L, "IT'S FREE! Place Lava on Side") },
            new ItemStack[] { new ItemStack(Blocks.cobblestone, 1) },
            null,
            null,
            null,
            16,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { ItemList.Display_ITS_FREE.getWithName(1L, "IT'S FREE! Place Lava on Top") },
            new ItemStack[] { new ItemStack(Blocks.stone, 1) },
            null,
            null,
            null,
            16,
            30,
            0);
        GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_Utility.getIntegratedCircuit(1) },
            new ItemStack[] { new ItemStack(Blocks.obsidian, 1) },
            null,
            null,
            null,
            128,
            30,
            0);
    }

    public static void nerfVanillaTools() {
        if (GT_Mod.gregtechproxy.mNerfedVanillaTools) {
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
                        .getToolStats(new ItemStack(GT_MetaGenerated_Tool_01.INSTANCE, 1, i)) != null) {
                        ItemStack tStack = new ItemStack(GT_MetaGenerated_Tool_01.INSTANCE, 1, i);
                        GT_MetaGenerated_Tool_01.INSTANCE.isItemStackUsable(tStack);
                        aList.add(
                            GT_MetaGenerated_Tool_01.INSTANCE
                                .getToolWithStats(i, 1, Materials.Lead, Materials.Lead, null));
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
                }
                super.displayAllReleventItems(aList);
            }
        };
    }

    public static void addSolidFakeLargeBoilerFuels() {
        GT_Recipe.GT_Recipe_Map.sLargeBoilerFakeFuels.addSolidRecipes(
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
            GT_Recipe.GT_Recipe_Map.sLargeBoilerFakeFuels
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
