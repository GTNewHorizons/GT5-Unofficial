package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gtPlusPlus.core.recipe.common.CI.bitsd;
import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generatePipeRecipes;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generateWireRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;

public class RECIPES_General {

    static final ItemStack NULL = null;
    static ItemStack RECIPE_Paper;
    static ItemStack RECIPE_Dirt;
    static ItemStack RECIPE_Snow;
    static ItemStack RECIPE_Obsidian;
    static String RECIPE_LapisDust = "dustLazurite";
    static ItemStack OUTPUT_Blueprint;
    static ItemStack RECIPE_CraftingTable;
    static String RECIPE_BronzePlate = "plateBronze";
    static String RECIPE_StainlessPlate = "plateStainlessSteel";
    static ItemStack RECIPE_BasicCasingIC2;
    static ItemStack RECIPE_HydrogenDust;

    public static void loadRecipes() {
        RECIPE_Paper = ItemUtils.getSimpleStack(Items.paper);
        RECIPE_Dirt = ItemUtils.getSimpleStack(Blocks.dirt);
        RECIPE_Snow = ItemUtils.getSimpleStack(Blocks.snow);
        RECIPE_Obsidian = ItemUtils.getSimpleStack(Blocks.obsidian);
        RECIPE_CraftingTable = ItemUtils.getSimpleStack(Blocks.crafting_table);
        RECIPE_HydrogenDust = ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob);
        RECIPE_BasicCasingIC2 = ItemUtils.getItemStackFromFQRN("IC2:blockMachine", 1);
        OUTPUT_Blueprint = ItemUtils.getSimpleStack(ModItems.itemBlueprintBase, 2);
        run();
        addCompressedObsidian();
        migratedRecipes();
    }

    private static void run() {
        // Workbench Blueprint
        RecipeUtils.addShapedRecipe(
                RECIPE_Paper,
                RECIPE_LapisDust,
                NULL,
                RECIPE_Paper,
                RECIPE_LapisDust,
                NULL,
                RECIPE_LapisDust,
                RECIPE_LapisDust,
                NULL,
                OUTPUT_Blueprint);

        // Rainforest oak Sapling
        if (RecipeUtils.addShapedRecipe(
                "stickWood",
                "stickWood",
                "stickWood",
                "stickWood",
                "treeSapling",
                "stickWood",
                "stickWood",
                "dustBone",
                "stickWood",
                ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Rainforest))) {
            Logger.INFO("Added a recipe for Rainforest oak Saplings.");
        }

        // Iron bars
        final ItemStack ironBars = ItemUtils.getItemStackFromFQRN("dreamcraft:item.SteelBars", 1);

        // Fish Trap
        if (RecipeUtils.addShapedRecipe(
                ironBars,
                ironBars,
                ironBars,
                ironBars,
                "frameGtWroughtIron",
                ironBars,
                ironBars,
                ironBars,
                ironBars,
                ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))) {
            Logger.INFO("Added a recipe for the Fish Trap.");
        }

        // Potin
        if (RecipeUtils.addShapelessGregtechRecipe(
                new Object[] { "dustLead", "dustBronze", "dustTin", "dustLead", "dustBronze" },
                ALLOY.POTIN.getDust(5))) {
            Logger.INFO("Added shapeless recipe for Potin Dust.");
        }

        // Tumbaga
        if (RecipeUtils.addShapelessGregtechRecipe(
                new Object[] { "dustGold", "dustGold", "dustCopper" },
                ItemUtils.getSimpleStack(ModItems.dustTumbagaMix))) {
            Logger.INFO("Added shapeless recipe for Tumbaga Mix.");
        }
        if (RecipeUtils.addShapelessGregtechRecipe(
                new Object[] { ItemUtils.getSimpleStack(ModItems.dustTumbagaMix),
                        ItemUtils.getSimpleStack(ModItems.dustTumbagaMix),
                        ItemUtils.getSimpleStack(ModItems.dustTumbagaMix), "dustGold" },
                ALLOY.TUMBAGA.getDust(10))) {
            Logger.INFO("Added shapeless recipe for Tumbaga Dust.");
        }

        if (CORE.ConfigSwitches.enableMachine_Pollution) {
            RecipeUtils.addShapedRecipe(
                    CI.craftingToolFile,
                    "plateIron",
                    "stickIron",
                    "plateIron",
                    "ringIron",
                    "plateIron",
                    "stickIron",
                    "plateIron",
                    CI.craftingToolHammer_Hard,
                    ItemUtils.simpleMetaStack(ModItems.itemBasicTurbine, 0, 1));
            RecipeUtils.addShapedRecipe(
                    CI.craftingToolFile,
                    "plateBronze",
                    "stickBronze",
                    "plateBronze",
                    "ringBronze",
                    "plateBronze",
                    "stickBronze",
                    "plateBronze",
                    CI.craftingToolHammer_Hard,
                    ItemUtils.simpleMetaStack(ModItems.itemBasicTurbine, 1, 1));
            RecipeUtils.addShapedRecipe(
                    CI.craftingToolFile,
                    "plateSteel",
                    "stickSteel",
                    "plateSteel",
                    "ringSteel",
                    "plateSteel",
                    "stickSteel",
                    "plateSteel",
                    CI.craftingToolHammer_Hard,
                    ItemUtils.simpleMetaStack(ModItems.itemBasicTurbine, 2, 1));
        }

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getSimpleStack(Blocks.glass, 16),
                        ItemUtils.getSimpleStack(Blocks.glowstone, 16),
                        ItemList.Large_Fluid_Cell_Steel.get(1),
                        CI.getNumberedAdvancedCircuit(4))
                .itemOutputs(VolumetricFlaskHelper.getLargeVolumetricFlask(2))
                .fluidInputs(FluidUtils.getFluidStack("molten.borosilicateglass", 2000)).duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getSimpleStack(Blocks.glass, 64),
                        ItemUtils.getSimpleStack(Blocks.glowstone, 64),
                        ItemList.Large_Fluid_Cell_TungstenSteel.get(1),
                        CI.getNumberedAdvancedCircuit(5))
                .itemOutputs(VolumetricFlaskHelper.getGiganticVolumetricFlask(2))
                .fluidInputs(FluidUtils.getFluidStack("molten.borosilicateglass", 8000)).duration(15 * SECONDS)
                .eut(TierEU.RECIPE_IV).addTo(assemblerRecipes);

        // Mining Explosive
        Logger.RECIPE("[Inspection] Explosives");
        if (RecipeUtils.addShapedRecipe(
                CI.explosiveITNT,
                CI.explosiveTNT,
                CI.explosiveITNT,
                CI.explosiveTNT,
                "frameGtIron",
                CI.explosiveTNT,
                "dustSulfur",
                CI.explosiveTNT,
                "dustSulfur",
                ItemUtils.getSimpleStack(ModBlocks.blockMiningExplosive, 3))) {
            Logger.INFO("Added a recipe for Mining Explosives.");
        }

        // Alkalus Coin
        if (RecipeUtils.addShapedRecipe(
                "gemExquisiteRuby",
                "gemFlawlessDiamond",
                "gemExquisiteDiamond",
                "gemFlawlessRuby",
                ItemList.Credit_Greg_Osmium.get(1),
                "gemFlawlessSapphire",
                "gemExquisiteEmerald",
                "gemFlawlessEmerald",
                "gemExquisiteSapphire",
                ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))) {
            Logger.INFO("Added a recipe for The Alkalus Disk.");
        }

        /*
         * final String fancyGems[] = new String[]{"gemExquisiteDiamond", "gemExquisiteEmerald", "gemExquisiteRuby",
         * "gemExquisiteSapphire"}; final ItemStack gemShards[] = new
         * ItemStack[]{ItemUtils.simpleMetaStack(ModItems.itemGemShards, 0, 1),
         * ItemUtils.simpleMetaStack(ModItems.itemGemShards, 1, 1), ItemUtils.simpleMetaStack(ModItems.itemGemShards, 2,
         * 1), ItemUtils.simpleMetaStack(ModItems.itemGemShards, 3, 1)}; int l=0; for (final String gem : fancyGems){
         * GameRegistry.addShapelessRecipe( gemShards[l], ItemUtils.getItemStackOfAmountFromOreDict(gem, 1), new
         * ItemStack(ModItems.itemAlkalusDisk, 1, OreDictionary.WILDCARD_VALUE)); l++; }
         */

        Logger.RECIPE("[Inspection] Wither Cage");
        if (RecipeUtils.addShapedRecipe(
                "stickBlackSteel",
                "plateTungstenSteel",
                "stickBlackSteel",
                "plateTungstenSteel",
                getSimpleStack(Items.nether_star),
                "plateTungstenSteel",
                "stickBlackSteel",
                "plateTungstenSteel",
                "stickBlackSteel",
                ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard, 32))) {
            Logger.INFO("Added a recipe for Wither Cages.");
        }

        // Magic Feather
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getSimpleStack(Items.feather, 64),
                        ItemUtils.getSimpleStack(Blocks.emerald_block, 32),
                        ItemUtils.getSimpleStack(Blocks.diamond_block, 32),
                        Materials.Ruby.getBlocks(32),
                        Materials.Sapphire.getBlocks(32),
                        ItemUtils.getSimpleStack(Blocks.gold_block, 32))
                .itemOutputs(ItemUtils.getSimpleStack(ModItems.itemMagicFeather, 1))
                .fluidInputs(Materials.Silver.getMolten(32 * 144)).duration(2 * MINUTES).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

        // Pest Killer
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        CI.getTieredMachineCasing(1),
                        CI.getElectricPump(2, 1),
                        CI.getPlate(2, 4),
                        ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(0), 2),
                        CI.getNumberedCircuit(16))
                .itemOutputs(ItemUtils.getSimpleStack(ModBlocks.blockPestKiller))
                .fluidInputs(FluidUtils.getHotWater(500)).duration(1 * MINUTES).eut(TierEU.RECIPE_LV / 2)
                .addTo(assemblerRecipes);
    }

    private static boolean addCompressedObsidian() {
        // Invert Obsidian
        ItemStack aInvertedObsidian = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1);
        if (RecipeUtils.addShapedRecipe(
                getSimpleStack(Items.redstone),
                getSimpleStack(Items.glowstone_dust),
                getSimpleStack(Items.redstone),
                getSimpleStack(Items.glowstone_dust),
                ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 1, 1),
                getSimpleStack(Items.glowstone_dust),
                getSimpleStack(Items.redstone),
                getSimpleStack(Items.glowstone_dust),
                getSimpleStack(Items.redstone),
                aInvertedObsidian)) {
            Logger.INFO("Added a recipe for Inverted Obsidian.");
        }

        final ItemStack[] mItems = new ItemStack[6];
        mItems[0] = ItemUtils.getSimpleStack(Blocks.obsidian);
        for (int r = 0; r < 5; r++) {
            mItems[r + 1] = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, r, 1);
        }

        final ItemStack[] mItems2 = new ItemStack[6];
        mItems2[0] = ItemUtils.getSimpleStack(Blocks.glowstone);
        for (int r = 0; r < 5; r++) {
            mItems2[r + 1] = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 6 + r, 1);
        }

        // Compressed Obsidian 1-5
        for (int r = 0; r < 5; r++) {

            final ItemStack input = mItems[r];
            final ItemStack output = mItems[r + 1];

            if (RecipeUtils.addShapedRecipe(input, input, input, input, input, input, input, input, input, output)) {
                Logger.INFO("Added a recipe for Compressed Obsidian [" + r + "]");
            }

            if (RecipeUtils
                    .addShapelessGregtechRecipe(new ItemStack[] { output }, ItemUtils.getSimpleStack(input, 9))) {
                Logger.INFO("Added a shapeless recipe for Compressed Obsidian [" + r + "]");
            }
        }

        // Compressed Glowstone 1-5
        for (int r = 0; r < 5; r++) {

            final ItemStack input = mItems2[r];
            final ItemStack output = mItems2[r + 1];

            if (RecipeUtils.addShapedRecipe(
                    input,
                    input,
                    input,
                    input,
                    r == 2 ? aInvertedObsidian : input,
                    input,
                    input,
                    input,
                    input,
                    output)) {
                Logger.INFO("Added a recipe for Compressed Glowstone [" + r + "]");
            }

            if (RecipeUtils
                    .addShapelessGregtechRecipe(new ItemStack[] { output }, ItemUtils.getSimpleStack(input, 9))) {
                Logger.INFO("Added a shapeless recipe for Compressed Glowstone [" + r + "]");
            }
        }
        return true;
    }

    private static void migratedRecipes() {

        RecipeUtils.generateMortarRecipe(
                ItemUtils.getSimpleStack(ModItems.itemPlateRawMeat),
                ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1));

        generateWireRecipes(ELEMENT.getInstance().ZIRCONIUM);
        generateWireRecipes(ALLOY.HG1223);
        generateWireRecipes(ALLOY.LEAGRISIUM);
        generateWireRecipes(ALLOY.TRINIUM_TITANIUM);
        generateWireRecipes(ELEMENT.STANDALONE.HYPOGEN);
        generateWireRecipes(ELEMENT.STANDALONE.CHRONOMATIC_GLASS);

        // No Material for void, natch.
        if (Thaumcraft.isModLoaded()) {
            generatePipeRecipes(GT_Materials.Void.mDefaultLocalName, GT_Materials.Void.getMass(), 16);
        }

        Material[] gtpp = new Material[] { ALLOY.STABALLOY, ALLOY.TANTALLOY_60, ALLOY.TANTALLOY_61, ALLOY.POTIN,
                ALLOY.MARAGING300, ALLOY.MARAGING350, ALLOY.INCONEL_690, ALLOY.INCONEL_792, ALLOY.HASTELLOY_X,
                ALLOY.TRINIUM_NAQUADAH_CARBON };

        for (Material mat : gtpp) {
            // generatePipeRecipes multiplies the voltage multiplier by 8 because ??! reasons.
            generatePipeRecipes(mat.getLocalizedName(), mat.getMass(), mat.vVoltageMultiplier / 8);
        }

        Materials[] h = new Materials[] { Materials.Europium, Materials.Tungsten, Materials.DarkSteel, Materials.Clay,
                Materials.Lead, };

        for (Materials e : h) {
            if (e == Materials.DarkSteel) {
                if (!EnderIO.isModLoaded()) {
                    continue;
                }
            }
            int tVoltageMultiplier = (e.mBlastFurnaceTemp >= 2800) ? 64 : 16;
            generatePipeRecipes(e.mDefaultLocalName, e.getMass(), tVoltageMultiplier);
        }

        RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[6],
                ALLOY.MARAGING250.getGear(1),
                CI.component_Plate[6],
                CI.getTieredCircuitOreDictName(4),
                GregtechItemList.Casing_AdvancedVacuum.get(1),
                CI.getTieredCircuitOreDictName(4),
                CI.component_Plate[5],
                ItemList.Hatch_Input_IV.get(1),
                CI.component_Plate[5],
                GregtechItemList.Hatch_Input_Cryotheum.get(1L));

        RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[6],
                ALLOY.MARAGING300.getGear(1),
                CI.component_Plate[6],
                CI.getTieredCircuitOreDictName(5),
                GregtechItemList.Casing_Adv_BlastFurnace.get(1),
                CI.getTieredCircuitOreDictName(5),
                CI.component_Plate[6],
                ItemList.Hatch_Input_IV.get(1),
                CI.component_Plate[6],
                GregtechItemList.Hatch_Input_Pyrotheum.get(1L));

        RecipeUtils.addShapedGregtechRecipe(
                CI.component_Plate[8],
                ALLOY.PIKYONIUM.getGear(1),
                CI.component_Plate[9],
                CI.getTieredCircuitOreDictName(7),
                GregtechItemList.Casing_Naq_Reactor_A.get(1),
                CI.getTieredCircuitOreDictName(7),
                CI.component_Plate[9],
                ItemList.Hatch_Input_ZPM.get(1),
                CI.component_Plate[8],
                GregtechItemList.Hatch_Input_Naquadah.get(1L, new Object[0]));

        if (PollutionUtils.isPollutionEnabled()) {
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_LV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_LV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_MV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_MV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_HV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_HV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_HV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_EV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_EV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_EV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_IV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_IV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_IV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_LuV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LuV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_LuV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_ZPM.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_ZPM.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_ZPM.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_UV.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_UV.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_UV.get(1) });
            GT_ModHandler.addCraftingRecipe(
                    GregtechItemList.Hatch_Muffler_Adv_MAX.get(1L, new Object[0]),
                    bitsd,
                    new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MAX.get(1),
                            Character.valueOf('P'), GregtechItemList.Pollution_Cleaner_MAX.get(1) });
        }
    }
}
