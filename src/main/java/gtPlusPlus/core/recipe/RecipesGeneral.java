package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generatePipeRecipes;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generateWireRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import ic2.core.Ic2Items;

public class RecipesGeneral {

    public static void loadRecipes() {

        // Workbench Blueprint
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModItems.itemBlueprintBase, 2),
            new Object[] { "PL ", "PL ", "LL ", 'P', new ItemStack(Items.paper), 'L',
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lazurite, 1) });

        // Rainforest Oak Sapling
        GTModHandler.addCraftingRecipe(
            new ItemStack(BOPBlockRegistrator.sapling_Rainforest),
            new Object[] { "SSS", "SPS", "SBS", 'S', "stickWood", 'P', "treeSapling", 'B', "dustBone" });

        // Potin
        GTModHandler.addShapelessCraftingRecipe(
            MaterialsAlloy.POTIN.getDust(5),
            new Object[] { "dustLead", "dustBronze", "dustTin", "dustLead", "dustBronze" });

        // Tumbaga
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.dustTumbagaMix),
            new Object[] { "dustGold", "dustGold", "dustCopper" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialsAlloy.TUMBAGA.getDust(10),
            new Object[] { new ItemStack(ModItems.dustTumbagaMix), new ItemStack(ModItems.dustTumbagaMix),
                new ItemStack(ModItems.dustTumbagaMix), "dustGold" });

        // Basic Turbines
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModItems.itemBasicTurbine),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateIron", 'R', "ringIron", 'S', "stickIron" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModItems.itemBasicTurbine, 1, 1),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateBronze", 'R', "ringBronze", 'S', "stickBronze" });
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModItems.itemBasicTurbine, 1, 2),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateSteel", 'R', "ringSteel", 'S', "stickSteel" });

        // Large Volumetric Flask
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_Aluminium.get(1))
            .itemOutputs(VolumetricFlaskHelper.getLargeVolumetricFlask(2))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        // Gigantic Volumetric Flask
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1))
            .itemOutputs(VolumetricFlaskHelper.getGiganticVolumetricFlask(2))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        // Klein Bottle
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .itemOutputs(VolumetricFlaskHelper.getKleinBottle(1))
            .fluidInputs(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(2304))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(fluidSolidifierRecipes);

        // Mining Explosives
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModBlocks.blockMiningExplosive, 3),
            new Object[] { "ITI", "TFT", "STS", 'I', Ic2Items.industrialTnt.copy(), 'T', new ItemStack(Blocks.tnt), 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1), 'S', "dustSulfur" });

        // Alkalus Disk
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModItems.itemAlkalusDisk),
            new Object[] { "ABC", "DEF", "GHI", 'A', "gemExquisiteRuby", 'B', "gemFlawlessDiamond", 'C',
                "gemExquisiteDiamond", 'D', "gemFlawlessRuby", 'E', ItemList.Credit_Greg_Osmium.get(1), 'F',
                "gemFlawlessSapphire", 'G', "gemExquisiteEmerald", 'H', "gemFlawlessEmerald", 'I',
                "gemExquisiteSapphire" });

        // Wither Cage
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModBlocks.blockWitherGuard, 32),
            new Object[] { "SPS", "PWP", "SPS", 'S', "stickBlackSteel", 'P', "plateTungstenSteel", 'W',
                new ItemStack(Items.nether_star) });

        // Magic Feather
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.feather, 64),
                new ItemStack(Blocks.emerald_block, 32),
                new ItemStack(Blocks.diamond_block, 32),
                Materials.Ruby.getBlocks(32),
                Materials.Sapphire.getBlocks(32),
                new ItemStack(Blocks.gold_block, 32))
            .itemOutputs(new ItemStack(ModItems.itemMagicFeather, 1))
            .fluidInputs(Materials.Silver.getMolten(32 * 144))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Pest Killer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LV.get(1),
                ItemList.Pump_MV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 2),
                GTUtility.getIntegratedCircuit(16))
            .itemOutputs(new ItemStack(ModBlocks.blockPestKiller))
            .fluidInputs(FluidUtils.getFluidStack("ic2hotwater", 500))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        addCompressedObsidian();
        addFuelBlocks();
        migratedRecipes();
        addPipesAndWires();
    }

    private static void addFuelBlocks() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemCactusCharcoal, 9))
            .itemOutputs(new ItemStack(ModBlocks.blockCactusCharcoal))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemCactusCoke, 9))
            .itemOutputs(new ItemStack(ModBlocks.blockCactusCoke))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemSugarCharcoal, 9))
            .itemOutputs(new ItemStack(ModBlocks.blockSugarCharcoal))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.itemSugarCoke, 9))
            .itemOutputs(new ItemStack(ModBlocks.blockSugarCoke))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
        for (int i = 1; i < 6; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ModBlocks.blockCactusCharcoal, 9, i - 1))
                .itemOutputs(new ItemStack(ModBlocks.blockCactusCharcoal, 1, i))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
        for (int i = 1; i < 6; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ModBlocks.blockCactusCoke, 9, i - 1))
                .itemOutputs(new ItemStack(ModBlocks.blockCactusCoke, 1, i))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
        for (int i = 1; i < 6; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ModBlocks.blockSugarCharcoal, 9, i - 1))
                .itemOutputs(new ItemStack(ModBlocks.blockSugarCharcoal, 1, i))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
        for (int i = 1; i < 6; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ModBlocks.blockSugarCoke, 9, i - 1))
                .itemOutputs(new ItemStack(ModBlocks.blockSugarCoke, 1, i))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
    }

    private static void addCompressedObsidian() {
        // Inverted Obsidian
        GTModHandler.addCraftingRecipe(
            new ItemStack(ModBlocks.blockCompressedObsidian, 1, 5),
            new Object[] { "RGR", "GOG", "RGR", 'R', new ItemStack(Items.redstone), 'G',
                new ItemStack(Items.glowstone_dust), 'O', new ItemStack(ModBlocks.blockCompressedObsidian, 1, 1) });

        // Compressed Obsidian 1-5
        final ItemStack[] mItems = new ItemStack[6];
        mItems[0] = new ItemStack(Blocks.obsidian);
        for (int r = 0; r < 5; r++) {
            mItems[r + 1] = new ItemStack(ModBlocks.blockCompressedObsidian, 1, r);
        }

        for (int r = 0; r < 5; r++) {
            final ItemStack input = mItems[r];
            final ItemStack output = mItems[r + 1];

            GTModHandler.addCraftingRecipe(output, new Object[] { "III", "III", "III", 'I', input });
            GTModHandler.addShapelessCraftingRecipe(GTUtility.copyAmount(9, input), new Object[] { output });
        }

        // Compressed Glowstone 1-5
        final ItemStack[] mItems2 = new ItemStack[6];
        mItems2[0] = new ItemStack(Blocks.glowstone);
        for (int r = 0; r < 5; r++) {
            mItems2[r + 1] = new ItemStack(ModBlocks.blockCompressedObsidian, 1, 6 + r);
        }

        for (int r = 0; r < 5; r++) {
            final ItemStack input = mItems2[r];
            final ItemStack output = mItems2[r + 1];

            if (r == 2) {
                GTModHandler.addCraftingRecipe(
                    output,
                    new Object[] { "III", "IQI", "III", 'I', input, 'Q',
                        new ItemStack(ModBlocks.blockCompressedObsidian, 1, 5) });
            } else {
                GTModHandler.addCraftingRecipe(output, new Object[] { "III", "III", "III", 'I', input });
            }

            GTModHandler.addShapelessCraftingRecipe(GTUtility.copyAmount(9, input), new Object[] { output });
        }
    }

    private static void addPipesAndWires() {
        generateWireRecipes(MaterialsElements.getInstance().ZIRCONIUM);
        generateWireRecipes(MaterialsAlloy.HG1223);
        generateWireRecipes(MaterialsAlloy.LEAGRISIUM);
        generateWireRecipes(MaterialsAlloy.TRINIUM_TITANIUM);
        generateWireRecipes(MaterialsElements.STANDALONE.HYPOGEN);
        generateWireRecipes(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS);

        generatePipeRecipes(MaterialsAlloy.STABALLOY);
        generatePipeRecipes(MaterialsAlloy.TANTALLOY_60);
        generatePipeRecipes(MaterialsAlloy.TANTALLOY_61);
        generatePipeRecipes(MaterialsAlloy.POTIN);
        generatePipeRecipes(MaterialsAlloy.MARAGING300);
        generatePipeRecipes(MaterialsAlloy.MARAGING350);
        generatePipeRecipes(MaterialsAlloy.INCONEL_690);
        generatePipeRecipes(MaterialsAlloy.INCONEL_792);
        generatePipeRecipes(MaterialsAlloy.HASTELLOY_X);
        generatePipeRecipes(MaterialsAlloy.TRINIUM_NAQUADAH_CARBON);
        generatePipeRecipes(Materials.Clay.mDefaultLocalName, Materials.Clay.getMass(), 15);
    }

    private static void migratedRecipes() {
        // Raw Meat Plate
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1),
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(ModItems.itemPlateRawMeat) });

        // Cryotheum Cooling Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Cryotheum.get(1L),
            new Object[] { "MGM", "CBC", "PHP", 'M', MaterialsAlloy.MARAGING250.getPlate(1), 'G',
                MaterialsAlloy.MARAGING250.getGear(1), 'C', "circuitData", 'B',
                GregtechItemList.Casing_AdvancedVacuum.get(1), 'P', Materials.Aluminium.getPlates(1), 'H',
                ItemList.Hatch_Input_IV.get(1) });

        // Pyrotheum Heating Vent
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Pyrotheum.get(1L),
            new Object[] { "MGM", "CBC", "MHM", 'M', MaterialsAlloy.MARAGING250.getPlate(1), 'G',
                MaterialsAlloy.MARAGING250.getGear(1), 'C', "circuitElite", 'B',
                GregtechItemList.Casing_Adv_BlastFurnace.get(1), 'H', ItemList.Hatch_Input_IV.get(1) });

        if (GTMod.gregtechproxy.mPollution) {
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_LV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_LV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_LV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_MV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_MV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_MV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_HV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_HV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_HV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_EV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_EV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_EV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_IV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_IV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_IV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_LuV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_LuV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_LuV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_ZPM.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_ZPM.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_ZPM.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_UV.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_UV.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_UV.get(1) });
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Hatch_Muffler_Adv_MAX.get(1L),
                BITSD,
                new Object[] { "M", "P", 'M', ItemList.Hatch_Muffler_MAX.get(1), 'P',
                    GregtechItemList.Pollution_Cleaner_MAX.get(1) });
        }
    }
}
