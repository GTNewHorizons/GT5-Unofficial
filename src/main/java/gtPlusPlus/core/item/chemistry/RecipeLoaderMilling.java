package gtPlusPlus.core.item.chemistry;

import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.chemicalPlantRecipes;
import static gregtech.api.recipe.RecipeMaps.flotationCellRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFurnaceRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import biomesoplenty.api.content.BOPCBlocks;
import biomesoplenty.api.content.BOPCItems;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderMilling {

    public static void generate() {
        addPineOilExtraction();
        addFlotationRecipes();
        addVacuumFurnaceRecipes();
    }

    private static void addVacuumFurnaceRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (52)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (56)),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Germanium, Shapes.dust, 15))
            .fluidInputs(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (52)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (56)),
                MaterialLibAPI.getStack(Materials.Cadmium, Shapes.dust, (int) (50)),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, (int) (10)))
            .fluidInputs(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (22)),
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, (int) (56)),
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 16))
            .fluidInputs(new FluidStack(GTPPFluids.NickelFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 60),
                MaterialLibAPI.getStack(Materials.Selenium, Shapes.dust, 40),
                MaterialLibAPI.getStack(Materials.Tellurium, Shapes.dust, (int) (10)))
            .fluidInputs(new FluidStack(GTPPFluids.PlatinumFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (22)),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, (int) (36)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (20)),
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 10))
            .fluidInputs(new FluidStack(GTPPFluids.PentlanditeFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (44)),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, (int) (60)),
                MaterialLibAPI.getStack(Materials.Firestone, Shapes.dust, (int) (45)),
                MaterialLibAPI.getStack(Materials.Dysprosium, Shapes.dust, (int) (16)))
            .fluidInputs(new FluidStack(GTPPFluids.RedstoneFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (22)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (26)),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (30)),
                MaterialLibAPI.getStack(Materials.Strontium, Shapes.dust, (int) (20)))
            .fluidInputs(new FluidStack(GTPPFluids.SpessartineFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (52)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (46)),
                MaterialLibAPI.getStack(Materials.Tungsten, Shapes.dust, (int) (60)),
                MaterialLibAPI.getStack(Materials.Thallium, Shapes.dust, 15))
            .fluidInputs(new FluidStack(GTPPFluids.GrossularFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (22)),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (11)),
                MaterialLibAPI.getStack(Materials.Yttrium, Shapes.dust, (int) (25)),
                MaterialLibAPI.getStack(Materials.Ytterbium, Shapes.dust, (int) (15)))
            .fluidInputs(new FluidStack(GTPPFluids.AlmandineFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, (int) (46)),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, (int) (6)),
                MaterialLibAPI.getStack(Materials.Borax, Shapes.dust, (int) (60)),
                MaterialLibAPI.getStack(Materials.Rhenium, Shapes.dust, 20))
            .fluidInputs(new FluidStack(GTPPFluids.PyropeFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Erbium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, (int) (8)))
            .fluidInputs(new FluidStack(GTPPFluids.MonaziteFlotationFroth, 4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_000), GTUtility.getWater(2_000))
            .eut(TierEU.RECIPE_UV)
            .metadata(COIL_HEAT, 7500)
            // 60s UV instead of 120s ZPM to avoid fusion skip
            .duration(1 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, (int) (55)),
                MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, (int) (40)),
                MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, (int) (40)),
                MaterialLibAPI.getStack(Materials.Ardite, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, (int) (32)))
            .fluidInputs(new FluidStack(GTPPFluids.NetherrackFlotationFroth, 3_000))
            .fluidOutputs(MaterialUtils.fluid(Materials.poornetherwaste, 16_000))
            .eut((int) TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 7200)
            .duration(1 * MINUTES)
            .addTo(vacuumFurnaceRecipes);
    }

    private static void addFlotationRecipes() {
        // Sphalerite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Sphalerite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Sphalerite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Sphalerite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Sphalerite, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14_000))
            .fluidOutputs(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Chalcopyrite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Chalcopyrite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Chalcopyrite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Chalcopyrite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Chalcopyrite, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 12_000))
            .fluidOutputs(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Nickel
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Nickel, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 25_000))
            .fluidOutputs(new FluidStack(GTPPFluids.NickelFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Platinum
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Platinum, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Platinum, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Platinum, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Platinum, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PlatinumFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Pentlandite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pentlandite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pentlandite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pentlandite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pentlandite, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PentlanditeFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Redstone
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Redstone, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 13_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedstoneFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Spessartine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Spessartine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Spessartine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Spessartine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Spessartine, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35_000))
            .fluidOutputs(new FluidStack(GTPPFluids.SpessartineFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Grossular
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Grossular, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Grossular, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Grossular, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Grossular, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 28_000))
            .fluidOutputs(new FluidStack(GTPPFluids.GrossularFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Almandine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Almandine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Almandine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Almandine, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Almandine, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 18_000))
            .fluidOutputs(new FluidStack(GTPPFluids.AlmandineFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Pyrope
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pyrope, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pyrope, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pyrope, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Pyrope, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 8_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PyropeFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(flotationCellRecipes);

        // Monazite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Monazite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Monazite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Monazite, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Monazite, 64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 30_000))
            .fluidOutputs(new FluidStack(GTPPFluids.MonaziteFlotationFroth, 1_000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Netherrack
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Netherrack, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Netherrack, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Netherrack, 64),
                GTOreDictUnificator.get(OrePrefixes.milled, Materials.Netherrack, 64))
            .fluidInputs(MaterialUtils.fluid(Materials.nefariousoil, 8_000))
            .fluidOutputs(new FluidStack(GTPPFluids.NetherrackFlotationFroth, 8_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);
    }

    private static void addPineOilExtraction() {
        addPineLogRecipe(new ItemStack(BOPBlockRegistrator.log_Pine));
        addPineLeafRecipe(new ItemStack(BOPBlockRegistrator.leaves_Pine));
        addPineSaplingRecipe(new ItemStack(BOPBlockRegistrator.sapling_Pine));
        addPineconeRecipe(GregtechItemList.Pinecone.get(1));

        if (BiomesOPlenty.isModLoaded()) {
            addPineLogRecipe(new ItemStack(BOPCBlocks.logs4));
            addPineLeafRecipe(new ItemStack(BOPCBlocks.colorizedLeaves2, 1, 1));
            addPineSaplingRecipe(new ItemStack(BOPCBlocks.colorizedSaplings, 1, 5));
            addPineconeRecipe(new ItemStack(BOPCItems.misc, 1, 13));
        }

        if (Forestry.isModLoaded()) {
            addPineLogRecipe(GTModHandler.getModItem(Forestry.ID, "logs", 1, 20));

            ItemStack forestryLeaves = GTModHandler.getModItem(Forestry.ID, "leaves", 1);
            if (forestryLeaves != null) {
                ItemStackNBT.setString(forestryLeaves, "species", "forestry.treePine"); // Set to Pine
                addPineLeafRecipe(forestryLeaves);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CrushedPineMaterials.get(64))
            .circuit(16)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, (int) (5)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, (int) (5)),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 5),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 5))
            .fluidInputs(MaterialUtils.gas(Materials.Steam, 5_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 500))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CrushedPineMaterials.get(64))
            .circuit(18)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, (int) (5)),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, (int) (5)),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 5),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 5))
            .fluidInputs(FluidRegistry.getFluidStack("ic2superheatedsteam", 5_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 1_500))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineLogRecipe(ItemStack log) {
        GTValues.RA.stdBuilder()
            .itemInputs(log)
            .circuit(14)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(64))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineLeafRecipe(ItemStack leaf) {
        GTValues.RA.stdBuilder()
            .itemInputs(leaf)
            .circuit(14)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineSaplingRecipe(ItemStack sapling) {
        GTValues.RA.stdBuilder()
            .itemInputs(sapling)
            .circuit(14)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineconeRecipe(ItemStack cone) {
        GTValues.RA.stdBuilder()
            .itemInputs(cone)
            .circuit(14)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(4))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }
}
