package gtPlusPlus.core.item.chemistry;

import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.flotationCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import biomesoplenty.api.content.BOPCBlocks;
import biomesoplenty.api.content.BOPCItems;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialsElements;
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
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 52),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 56),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 64),
                MaterialsElements.getInstance().GERMANIUM.getDust(15))
            .fluidInputs(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 52),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 56),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 50),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 10))
            .fluidInputs(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 22),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 56),
                MaterialsElements.getInstance().RHODIUM.getDust(32),
                MaterialsElements.getInstance().RUTHENIUM.getDust(16))
            .fluidInputs(new FluidStack(GTPPFluids.NickelFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                PTMetallicPowder.get(OrePrefixes.dust, 64),
                PTMetallicPowder.get(OrePrefixes.dust, 64),
                MaterialsElements.getInstance().RHODIUM.getDust(60),
                MaterialsElements.getInstance().SELENIUM.getDust(40),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tellurium, 10))
            .fluidInputs(new FluidStack(GTPPFluids.PlatinumFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 22),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 36),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Promethium, 20),
                MaterialsElements.getInstance().HAFNIUM.getDust(10))
            .fluidInputs(new FluidStack(GTPPFluids.PentlanditeFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 44),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 60),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Firestone, 45),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Dysprosium, 16))
            .fluidInputs(new FluidStack(GTPPFluids.RedstoneFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 22),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 26),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 30),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Strontium, 20))
            .fluidInputs(new FluidStack(GTPPFluids.SpessartineFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 52),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 46),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 60),
                MaterialsElements.getInstance().THALLIUM.getDust(15))
            .fluidInputs(new FluidStack(GTPPFluids.GrossularFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 22),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Yttrium, 25),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ytterbium, 15))
            .fluidInputs(new FluidStack(GTPPFluids.AlmandineFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_LuV)
            .metadata(COIL_HEAT, 5500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 46),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Borax, 60),
                MaterialsElements.getInstance().RHENIUM.getDust(20))
            .fluidInputs(new FluidStack(GTPPFluids.PyropeFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Erbium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 8))
            .fluidInputs(new FluidStack(GTPPFluids.MonaziteFlotationFroth, 4000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2000), GTModHandler.getWater(2000))
            .eut(TierEU.RECIPE_UV)
            .metadata(COIL_HEAT, 7500)
            // 60s UV instead of 120s ZPM to avoid fusion skip
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14000))
            .fluidOutputs(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 12000))
            .fluidOutputs(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 25000))
            .fluidOutputs(new FluidStack(GTPPFluids.NickelFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35000))
            .fluidOutputs(new FluidStack(GTPPFluids.PlatinumFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14000))
            .fluidOutputs(new FluidStack(GTPPFluids.PentlanditeFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 13000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedstoneFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35000))
            .fluidOutputs(new FluidStack(GTPPFluids.SpessartineFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 28000))
            .fluidOutputs(new FluidStack(GTPPFluids.GrossularFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 18000))
            .fluidOutputs(new FluidStack(GTPPFluids.AlmandineFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 8000))
            .fluidOutputs(new FluidStack(GTPPFluids.PyropeFlotationFroth, 1000))
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
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 30000))
            .fluidOutputs(new FluidStack(GTPPFluids.MonaziteFlotationFroth, 1000))
            .duration(8 * MINUTES)
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
                NBTTagCompound tag = forestryLeaves.getTagCompound();
                if (tag == null) {
                    forestryLeaves.setTagCompound(tag = new NBTTagCompound());
                }
                tag.setString("species", "forestry.treePine"); // Set to Pine
                addPineLeafRecipe(forestryLeaves);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(16), GregtechItemList.CrushedPineMaterials.get(64))
            .itemOutputs(
                Materials.Ash.getDustTiny(5),
                Materials.Ash.getDustTiny(5),
                Materials.DarkAsh.getDustTiny(5),
                Materials.DarkAsh.getDustTiny(5))
            .fluidInputs(GTModHandler.getSteam(5000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 500))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(18), GregtechItemList.CrushedPineMaterials.get(64))
            .itemOutputs(
                Materials.Ash.getDustTiny(5),
                Materials.Ash.getDustTiny(5),
                Materials.DarkAsh.getDustTiny(5),
                Materials.DarkAsh.getDustTiny(5))
            .fluidInputs(FluidRegistry.getFluidStack("ic2superheatedsteam", 5000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 1500))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineLogRecipe(ItemStack log) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), log)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(64))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineLeafRecipe(ItemStack leaf) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), leaf)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineSaplingRecipe(ItemStack sapling) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), sapling)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addPineconeRecipe(ItemStack cone) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), cone)
            .itemOutputs(GregtechItemList.CrushedPineMaterials.get(4))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }
}
