package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Materials.Obsidian;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockSugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedGlowstone;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedNetherrack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedObsidian;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedSugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedGlowstone;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedNetherrack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedObsidian;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedSugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedGlowstone;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedObsidian;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedSugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedGlowstone;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedObsidian;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedSugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.SugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.SugarCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedGlowstone;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedNetherrack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedObsidian;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedSugarCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedSugarCoke;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generatePipeRecipes;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generateWireRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
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

        addCompressedCactusCharcoal();
        addCompressedCactusCoke();
        addCompressedSugarCharcoal();
        addCompressedSugarCoke();
        addCompressedObsidian();
        addCompressedNetherrack();
        addCompressedGlowstone();

        migratedRecipes();
        addPipesAndWires();
    }

    private static void addCompressedCactusCharcoal() {
        addCompressionRecipe(CactusCharcoal.get(9), BlockCactusCharcoal.get(1));
        addCompressionRecipe(BlockCactusCharcoal.get(9), CompressedCactusCharcoal.get(1));
        addCompressionRecipe(CompressedCactusCharcoal.get(9), DoubleCompressedCactusCharcoal.get(1));
        addCompressionRecipe(DoubleCompressedCactusCharcoal.get(9), TripleCompressedCactusCharcoal.get(1));
        addCompressionRecipe(TripleCompressedCactusCharcoal.get(9), QuadrupleCompressedCactusCharcoal.get(1));
        addCompressionRecipe(QuadrupleCompressedCactusCharcoal.get(9), QuintupleCompressedCactusCharcoal.get(1));

        addDecompressionRecipe(BlockCactusCharcoal.get(1), CactusCharcoal.get(9));
        addDecompressionRecipe(CompressedCactusCharcoal.get(1), BlockCactusCharcoal.get(9));
        addDecompressionRecipe(DoubleCompressedCactusCharcoal.get(1), CompressedCactusCharcoal.get(9));
        addDecompressionRecipe(TripleCompressedCactusCharcoal.get(1), DoubleCompressedCactusCharcoal.get(9));
        addDecompressionRecipe(QuadrupleCompressedCactusCharcoal.get(1), TripleCompressedCactusCharcoal.get(9));
        addDecompressionRecipe(QuintupleCompressedCactusCharcoal.get(1), QuadrupleCompressedCactusCharcoal.get(9));
    }

    private static void addCompressedCactusCoke() {
        addCompressionRecipe(CactusCoke.get(9), BlockCactusCoke.get(1));
        addCompressionRecipe(BlockCactusCoke.get(9), CompressedCactusCoke.get(1));
        addCompressionRecipe(CompressedCactusCoke.get(9), DoubleCompressedCactusCoke.get(1));
        addCompressionRecipe(DoubleCompressedCactusCoke.get(9), TripleCompressedCactusCoke.get(1));
        addCompressionRecipe(TripleCompressedCactusCoke.get(9), QuadrupleCompressedCactusCoke.get(1));
        addCompressionRecipe(QuadrupleCompressedCactusCoke.get(9), QuintupleCompressedCactusCoke.get(1));

        addDecompressionRecipe(BlockCactusCoke.get(1), CactusCoke.get(9));
        addDecompressionRecipe(CompressedCactusCoke.get(1), BlockCactusCoke.get(9));
        addDecompressionRecipe(DoubleCompressedCactusCoke.get(1), CompressedCactusCoke.get(9));
        addDecompressionRecipe(TripleCompressedCactusCoke.get(1), DoubleCompressedCactusCoke.get(9));
        addDecompressionRecipe(QuadrupleCompressedCactusCoke.get(1), TripleCompressedCactusCoke.get(9));
        addDecompressionRecipe(QuintupleCompressedCactusCoke.get(1), QuadrupleCompressedCactusCoke.get(9));
    }

    private static void addCompressedSugarCharcoal() {
        addCompressionRecipe(SugarCharcoal.get(9), BlockSugarCharcoal.get(1));
        addCompressionRecipe(BlockSugarCharcoal.get(9), CompressedSugarCharcoal.get(1));
        addCompressionRecipe(CompressedSugarCharcoal.get(9), DoubleCompressedSugarCharcoal.get(1));
        addCompressionRecipe(DoubleCompressedSugarCharcoal.get(9), TripleCompressedSugarCharcoal.get(1));
        addCompressionRecipe(TripleCompressedSugarCharcoal.get(9), QuadrupleCompressedSugarCharcoal.get(1));
        addCompressionRecipe(QuadrupleCompressedSugarCharcoal.get(9), QuintupleCompressedSugarCharcoal.get(1));

        addDecompressionRecipe(BlockSugarCharcoal.get(1), SugarCharcoal.get(9));
        addDecompressionRecipe(CompressedSugarCharcoal.get(1), BlockSugarCharcoal.get(9));
        addDecompressionRecipe(DoubleCompressedSugarCharcoal.get(1), CompressedSugarCharcoal.get(9));
        addDecompressionRecipe(TripleCompressedSugarCharcoal.get(1), DoubleCompressedSugarCharcoal.get(9));
        addDecompressionRecipe(QuadrupleCompressedSugarCharcoal.get(1), TripleCompressedSugarCharcoal.get(9));
        addDecompressionRecipe(QuintupleCompressedSugarCharcoal.get(1), QuadrupleCompressedSugarCharcoal.get(9));
    }

    private static void addCompressedSugarCoke() {
        addCompressionRecipe(SugarCoke.get(9), BlockSugarCoke.get(1));
        addCompressionRecipe(BlockSugarCoke.get(9), CompressedSugarCoke.get(1));
        addCompressionRecipe(CompressedSugarCoke.get(9), DoubleCompressedSugarCoke.get(1));
        addCompressionRecipe(DoubleCompressedSugarCoke.get(9), TripleCompressedSugarCoke.get(1));
        addCompressionRecipe(TripleCompressedSugarCoke.get(9), QuadrupleCompressedSugarCoke.get(1));
        addCompressionRecipe(QuadrupleCompressedSugarCoke.get(9), QuintupleCompressedSugarCoke.get(1));

        addDecompressionRecipe(BlockSugarCoke.get(1), SugarCoke.get(9));
        addDecompressionRecipe(CompressedSugarCoke.get(1), BlockSugarCoke.get(9));
        addDecompressionRecipe(DoubleCompressedSugarCoke.get(1), CompressedSugarCoke.get(9));
        addDecompressionRecipe(TripleCompressedSugarCoke.get(1), DoubleCompressedSugarCoke.get(9));
        addDecompressionRecipe(QuadrupleCompressedSugarCoke.get(1), TripleCompressedSugarCoke.get(9));
        addDecompressionRecipe(QuintupleCompressedSugarCoke.get(1), QuadrupleCompressedSugarCoke.get(9));
    }

    private static void addCompressedObsidian() {
        addCompressionRecipe(Obsidian.getBlocks(9), CompressedObsidian.get(1));
        addCompressionRecipe(CompressedObsidian.get(9), DoubleCompressedObsidian.get(1));
        addCompressionRecipe(DoubleCompressedObsidian.get(9), TripleCompressedObsidian.get(1));
        addCompressionRecipe(TripleCompressedObsidian.get(9), QuadrupleCompressedObsidian.get(1));
        addCompressionRecipe(QuadrupleCompressedObsidian.get(9), QuintupleCompressedObsidian.get(1));

        addDecompressionRecipe(CompressedObsidian.get(1), Obsidian.getBlocks(9));
        addDecompressionRecipe(DoubleCompressedObsidian.get(1), CompressedObsidian.get(9));
        addDecompressionRecipe(TripleCompressedObsidian.get(1), DoubleCompressedObsidian.get(9));
        addDecompressionRecipe(QuadrupleCompressedObsidian.get(1), TripleCompressedObsidian.get(9));
        addDecompressionRecipe(QuintupleCompressedObsidian.get(1), QuadrupleCompressedObsidian.get(9));
    }

    private static void addCompressedNetherrack() {
        addCompressionRecipe(new ItemStack(Blocks.netherrack, 9, 0), CompressedNetherrack.get(1));
        addCompressionRecipe(CompressedNetherrack.get(9), DoubleCompressedNetherrack.get(1));
        addCompressionRecipe(DoubleCompressedNetherrack.get(9), TripleCompressedNetherrack.get(1));

        addDecompressionRecipe(CompressedNetherrack.get(1), new ItemStack(Blocks.netherrack, 9, 0));
        addDecompressionRecipe(DoubleCompressedNetherrack.get(1), CompressedNetherrack.get(9));
        addDecompressionRecipe(TripleCompressedNetherrack.get(1), DoubleCompressedNetherrack.get(9));
    }

    private static void addCompressedGlowstone() {
        addCompressionRecipe(new ItemStack(Blocks.glowstone, 9, 0), CompressedGlowstone.get(1));
        addCompressionRecipe(CompressedGlowstone.get(9), DoubleCompressedGlowstone.get(1));
        addCompressionRecipe(DoubleCompressedGlowstone.get(9), TripleCompressedGlowstone.get(1));
        addCompressionRecipe(TripleCompressedGlowstone.get(9), QuadrupleCompressedGlowstone.get(1));
        addCompressionRecipe(QuadrupleCompressedGlowstone.get(9), QuintupleCompressedGlowstone.get(1));

        addDecompressionRecipe(CompressedGlowstone.get(1), new ItemStack(Blocks.glowstone, 9, 0));
        addDecompressionRecipe(DoubleCompressedGlowstone.get(1), CompressedGlowstone.get(9));
        addDecompressionRecipe(TripleCompressedGlowstone.get(1), DoubleCompressedGlowstone.get(9));
        addDecompressionRecipe(QuadrupleCompressedGlowstone.get(1), TripleCompressedGlowstone.get(9));
        addDecompressionRecipe(QuintupleCompressedGlowstone.get(1), QuadrupleCompressedGlowstone.get(9));
    }

    private static void addCompressionRecipe(@NotNull ItemStack raw, @NotNull ItemStack compressed) {
        GTValues.RA.stdBuilder()
            .itemInputs(raw)
            .itemOutputs(compressed)
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
    }

    private static void addDecompressionRecipe(@NotNull ItemStack compressed, @NotNull ItemStack raw) {
        GTValues.RA.stdBuilder()
            .itemInputs(compressed)
            .itemOutputs(raw)
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(hammerRecipes);
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
