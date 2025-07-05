package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
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
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class RecipesGeneral {

    public static void loadRecipes() {

        // Workbench Blueprint
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BlueprintBase.get(2),
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
            GregtechItemList.TumbagaMixDust.get(1),
            new Object[] { "dustGold", "dustGold", "dustCopper" });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialsAlloy.TUMBAGA.getDust(10),
            new Object[] { GregtechItemList.TumbagaMixDust.get(1), GregtechItemList.TumbagaMixDust.get(1),
                GregtechItemList.TumbagaMixDust.get(1), "dustGold" });

        // Basic Turbines
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BasicIronTurbine.get(1),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateIron", 'R', "ringIron", 'S', "stickIron" });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BasicBronzeTurbine.get(1),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateBronze", 'R', "ringBronze", 'S', "stickBronze" });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BasicSteelTurbine.get(1),
            new Object[] { "fPS", "PRP", "SPh", 'P', "plateSteel", 'R', "ringSteel", 'S', "stickSteel" });

        // Large Volumetric Flask
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_Aluminium.get(1))
            .itemOutputs(GregtechItemList.VOLUMETRIC_FLASK_8k.get(2))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        // Gigantic Volumetric Flask
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1))
            .itemOutputs(GregtechItemList.VOLUMETRIC_FLASK_32k.get(2))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        // Klein Bottle
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .itemOutputs(GregtechItemList.KLEIN_BOTTLE.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(16 * INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(fluidSolidifierRecipes);

        // Mining Explosives
        GTModHandler.addCraftingRecipe(
            GregtechItemList.MiningExplosives.get(3),
            new Object[] { "ITI", "TFT", "STS", 'I', Ic2Items.industrialTnt.copy(), 'T', new ItemStack(Blocks.tnt), 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1), 'S', "dustSulfur" });

        // Alkalus Disk
        GTModHandler.addCraftingRecipe(
            GregtechItemList.AlkalusDisk.get(1),
            new Object[] { "ABC", "DEF", "GHI", 'A', "gemExquisiteRuby", 'B', "gemFlawlessDiamond", 'C',
                "gemExquisiteDiamond", 'D', "gemFlawlessRuby", 'E', ItemList.Credit_Greg_Osmium.get(1), 'F',
                "gemFlawlessSapphire", 'G', "gemExquisiteEmerald", 'H', "gemFlawlessEmerald", 'I',
                "gemExquisiteSapphire" });

        // Wither Cage
        GTModHandler.addCraftingRecipe(
            GregtechItemList.WitherGuard.get(32),
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
            .itemOutputs(GregtechItemList.MagicFeather.get(1))
            .fluidInputs(Materials.Silver.getMolten(32 * INGOTS))
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
            .itemOutputs(GregtechItemList.PestKiller.get(1))
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
        addCompressionRecipes(
            false,
            GregtechItemList.CactusCharcoal.get(1),
            GregtechItemList.BlockCactusCharcoal.get(1),
            GregtechItemList.CompressedCactusCharcoal.get(1),
            GregtechItemList.DoubleCompressedCactusCharcoal.get(1),
            GregtechItemList.TripleCompressedCactusCharcoal.get(1),
            GregtechItemList.QuadrupleCompressedCactusCharcoal.get(1),
            GregtechItemList.QuintupleCompressedCactusCharcoal.get(1));

        addCompressionRecipes(
            false,
            GregtechItemList.CactusCoke.get(1),
            GregtechItemList.BlockCactusCoke.get(1),
            GregtechItemList.CompressedCactusCoke.get(1),
            GregtechItemList.DoubleCompressedCactusCoke.get(1),
            GregtechItemList.TripleCompressedCactusCoke.get(1),
            GregtechItemList.QuadrupleCompressedCactusCoke.get(1),
            GregtechItemList.QuintupleCompressedCactusCoke.get(1));

        addCompressionRecipes(
            false,
            GregtechItemList.SugarCharcoal.get(1),
            GregtechItemList.BlockSugarCharcoal.get(1),
            GregtechItemList.CompressedSugarCharcoal.get(1),
            GregtechItemList.DoubleCompressedSugarCharcoal.get(1),
            GregtechItemList.TripleCompressedSugarCharcoal.get(1),
            GregtechItemList.QuadrupleCompressedSugarCharcoal.get(1),
            GregtechItemList.QuintupleCompressedSugarCharcoal.get(1));

        addCompressionRecipes(
            false,
            GregtechItemList.SugarCoke.get(1),
            GregtechItemList.BlockSugarCoke.get(1),
            GregtechItemList.CompressedSugarCoke.get(1),
            GregtechItemList.DoubleCompressedSugarCoke.get(1),
            GregtechItemList.TripleCompressedSugarCoke.get(1),
            GregtechItemList.QuadrupleCompressedSugarCoke.get(1),
            GregtechItemList.QuintupleCompressedSugarCoke.get(1));
    }

    private static void addCompressionRecipes(boolean withCrafting, ItemStack item, ItemStack block, ItemStack one,
        ItemStack two, ItemStack three, ItemStack four, ItemStack five) {

        addCompressionRecipe(withCrafting, item, block);
        addCompressionRecipe(withCrafting, block, one);
        addCompressionRecipe(withCrafting, one, two);
        addCompressionRecipe(withCrafting, two, three);
        addCompressionRecipe(withCrafting, three, four);
        addCompressionRecipe(withCrafting, four, five);
    }

    private static void addCompressionRecipes(boolean withCrafting, ItemStack item, ItemStack block, ItemStack one,
        ItemStack two, ItemStack three) {

        addCompressionRecipe(withCrafting, item, block);
        addCompressionRecipe(withCrafting, block, one);
        addCompressionRecipe(withCrafting, one, two);
        addCompressionRecipe(withCrafting, two, three);
    }

    private static void addCompressionRecipe(boolean crafting, ItemStack raw, ItemStack compressed) {
        if (raw == null || compressed == null) return;

        if (crafting) {
            GTModHandler.addCraftingRecipe(compressed, new Object[] { "III", "III", "III", 'I', raw });
            GTModHandler.addShapelessCraftingRecipe(GTUtility.copyAmount(9, raw), new Object[] { compressed });
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, raw))
                .itemOutputs(compressed)
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }
    }

    private static void addCompressedObsidian() {
        // Compressed Obsidian
        addCompressionRecipes(
            true,
            null,
            new ItemStack(Blocks.obsidian),
            GregtechItemList.CompressedObsidian.get(1),
            GregtechItemList.DoubleCompressedObsidian.get(1),
            GregtechItemList.TripleCompressedObsidian.get(1),
            GregtechItemList.QuadrupleCompressedObsidian.get(1),
            GregtechItemList.QuintupleCompressedObsidian.get(1));

        // Compressed Glowstone
        addCompressionRecipe(true, new ItemStack(Blocks.glowstone), GregtechItemList.CompressedGlowstone.get(1));
        addCompressionRecipe(
            true,
            GregtechItemList.CompressedGlowstone.get(1),
            GregtechItemList.DoubleCompressedGlowstone.get(1));

        // Double -> Triple done differently
        GTModHandler.addCraftingRecipe(
            GregtechItemList.TripleCompressedGlowstone.get(1),
            new Object[] { "III", "IQI", "III", 'I', GregtechItemList.DoubleCompressedGlowstone.get(1), 'Q',
                GregtechItemList.InvertedObsidian.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.DoubleCompressedGlowstone.get(9),
            new Object[] { GregtechItemList.TripleCompressedGlowstone.get(1) });

        addCompressionRecipe(
            true,
            GregtechItemList.TripleCompressedGlowstone.get(1),
            GregtechItemList.QuadrupleCompressedGlowstone.get(1));
        addCompressionRecipe(
            true,
            GregtechItemList.QuadrupleCompressedGlowstone.get(1),
            GregtechItemList.QuintupleCompressedGlowstone.get(1));

        // Compressed Netherrack
        addCompressionRecipes(
            true,
            null,
            new ItemStack(Blocks.netherrack, 1),
            GregtechItemList.CompressedNetherrack.get(1),
            GregtechItemList.DoubleCompressedNetherrack.get(1),
            GregtechItemList.TripleCompressedNetherrack.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.netherrack, 9))
            .itemOutputs(GregtechItemList.CompressedNetherrack.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CompressedNetherrack.get(9))
            .itemOutputs(GregtechItemList.DoubleCompressedNetherrack.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DoubleCompressedNetherrack.get(9))
            .itemOutputs(GregtechItemList.TripleCompressedNetherrack.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(compressorRecipes);

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
        generatePipeRecipes(null, Materials.Clay.mDefaultLocalName, Materials.Clay.getMass(), 15);
    }

    private static void migratedRecipes() {
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
                MaterialsAlloy.MARAGING300.getGear(1), 'C', "circuitElite", 'B',
                GregtechItemList.Casing_Adv_BlastFurnace.get(1), 'H', ItemList.Hatch_Input_IV.get(1) });

        if (GTMod.proxy.mPollution) {
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
