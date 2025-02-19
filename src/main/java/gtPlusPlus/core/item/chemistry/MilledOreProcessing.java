package gtPlusPlus.core.item.chemistry;

import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.*;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
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
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

public class MilledOreProcessing extends ItemPackage {

    @Override
    public void items() {
        GregtechItemList.MilledSphalerite
            .set(BaseItemMilledOre.generate(Materials.Sphalerite, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledChalcopyrite
            .set(BaseItemMilledOre.generate(Materials.Chalcopyrite, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledNickel.set(BaseItemMilledOre.generate(Materials.Nickel, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledPlatinum.set(BaseItemMilledOre.generate(Materials.Platinum, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledPentlandite
            .set(BaseItemMilledOre.generate(Materials.Pentlandite, (int) TierEU.RECIPE_LuV));

        GregtechItemList.MilledRedstone.set(BaseItemMilledOre.generate(Materials.Redstone, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledSpessartine
            .set(BaseItemMilledOre.generate(Materials.Spessartine, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledGrossular.set(BaseItemMilledOre.generate(Materials.Grossular, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledAlmandine.set(BaseItemMilledOre.generate(Materials.Almandine, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledPyrope.set(BaseItemMilledOre.generate(Materials.Pyrope, (int) TierEU.RECIPE_EV));
        GregtechItemList.MilledMonazite.set(BaseItemMilledOre.generate(Materials.Monazite, (int) TierEU.RECIPE_ZPM));
    }

    @Override
    public void blocks() {}

    @Override
    public void fluids() {}

    public MilledOreProcessing() {
        super();
    }

    @Override
    public String errorMessage() {
        return "Failed to generate recipes for OreMillingProc.";
    }

    @Override
    public boolean generateRecipes() {
        addPineOilExtraction();
        addFlotationRecipes();
        addVacuumFurnaceRecipes();
        return true;
    }

    private void addVacuumFurnaceRecipes() {
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

    private void addFlotationRecipes() {

        // Sphalerite
        Material aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Sphalerite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14000))
            .fluidOutputs(new FluidStack(GTPPFluids.SphaleriteFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Chalcopyrite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Chalcopyrite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 12000))
            .fluidOutputs(new FluidStack(GTPPFluids.ChalcopyriteFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Nickel
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Nickel);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 25000))
            .fluidOutputs(new FluidStack(GTPPFluids.NickelFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Platinum
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35000))
            .fluidOutputs(new FluidStack(GTPPFluids.PlatinumFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Pentlandite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Pentlandite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 14000))
            .fluidOutputs(new FluidStack(GTPPFluids.PentlanditeFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Redstone
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Redstone);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 13000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedstoneFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Spessartine
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Spessartine);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 35000))
            .fluidOutputs(new FluidStack(GTPPFluids.SpessartineFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Grossular
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Grossular);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 28000))
            .fluidOutputs(new FluidStack(GTPPFluids.GrossularFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Almandine
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Almandine);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 18000))
            .fluidOutputs(new FluidStack(GTPPFluids.AlmandineFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Pyrope
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Pyrope);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.SodiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 8000))
            .fluidOutputs(new FluidStack(GTPPFluids.PyropeFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(flotationCellRecipes);

        // Monazite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Monazite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PotassiumEthylXanthate.get(32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(new FluidStack(GTPPFluids.PineOil, 30000))
            .fluidOutputs(new FluidStack(GTPPFluids.MonaziteFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);
    }

    private void addPineOilExtraction() {
        ArrayList<ItemStack> aLogs = new ArrayList<>();
        ArrayList<ItemStack> aLeaves = new ArrayList<>();
        ArrayList<ItemStack> aSaplings = new ArrayList<>();
        ArrayList<ItemStack> aPinecones = new ArrayList<>();

        ItemStack aCrushedPine = ItemUtils.getSimpleStack(AgriculturalChem.mCrushedPine, 1);

        aLogs.add(ItemUtils.getSimpleStack(BOPBlockRegistrator.log_Pine));
        aLeaves.add(ItemUtils.getSimpleStack(BOPBlockRegistrator.leaves_Pine));
        aSaplings.add(ItemUtils.getSimpleStack(BOPBlockRegistrator.sapling_Pine));
        aPinecones.add(ItemUtils.getSimpleStack(AgriculturalChem.mPinecone, 1));

        if (BiomesOPlenty.isModLoaded()) {
            aLogs.add(ItemUtils.simpleMetaStack(BOPCBlocks.logs4, 0, 1));
            aLeaves.add(ItemUtils.simpleMetaStack(BOPCBlocks.colorizedLeaves2, 1, 1));
            aSaplings.add(ItemUtils.simpleMetaStack(BOPCBlocks.colorizedSaplings, 5, 1));
            aPinecones.add(ItemUtils.simpleMetaStack(BOPCItems.misc, 13, 1));
        }
        if (Forestry.isModLoaded()) {
            ItemStack aForestryLog = ItemUtils.getItemStackFromFQRN("Forestry:logs", 1);
            if (aForestryLog != null) {
                aForestryLog.setItemDamage(20); // Set to Pine
                aLogs.add(aForestryLog);
            }
            ItemStack aForestryLeaves = ItemUtils.getItemStackFromFQRN("Forestry:leaves", 1);
            if (aForestryLeaves != null) {
                NBTUtils.setString(aForestryLeaves, "species", "forestry.treePine"); // Set to Pine
                aLeaves.add(aForestryLeaves);
            }
        }

        for (ItemStack aLog : aLogs) {
            addRecipe(aLog, ItemUtils.getSimpleStack(aCrushedPine, 16), new int[] { 10000, 7500, 5000, 2500 }, 10, 120);
        }
        for (ItemStack aLeaf : aLeaves) {
            addRecipe(aLeaf, ItemUtils.getSimpleStack(aCrushedPine, 2), new int[] { 5000, 5000, 2500, 2500 }, 10, 30);
        }
        for (ItemStack aSapling : aSaplings) {
            addRecipe(
                aSapling,
                ItemUtils.getSimpleStack(aCrushedPine, 4),
                new int[] { 7500, 7500, 2500, 2500 },
                10,
                60);
        }
        for (ItemStack aCone : aPinecones) {
            addRecipe(aCone, ItemUtils.getSimpleStack(aCrushedPine, 1), new int[] { 7500, 7500, 5000, 2500 }, 10, 60);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(16), ItemUtils.getSimpleStack(aCrushedPine, 64))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5))
            .fluidInputs(GTModHandler.getSteam(5000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 500))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(18), ItemUtils.getSimpleStack(aCrushedPine, 64))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5))
            .fluidInputs(FluidUtils.getSuperHeatedSteam(5000))
            .fluidOutputs(new FluidStack(GTPPFluids.PineOil, 1500))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

    }

    public boolean addRecipe(ItemStack aInput, ItemStack aOutput1, int[] aChances, int aTime, int aEU) {
        aOutput1 = GTOreDictUnificator.get(true, aOutput1);
        ItemStack[] aOutputs = new ItemStack[4];
        for (int i = 0; i < aChances.length; i++) {
            aOutputs[i] = aOutput1;
        }
        aOutputs = cleanArray(aOutputs);
        if ((GTUtility.isStackInvalid(aInput))
            || (GTUtility.isStackInvalid(aOutput1) || (GTUtility.getContainerItem(aInput, false) != null))) {
            return false;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(14), aInput)
            .itemOutputs(aOutputs)
            .duration(aTime * 20)
            .eut(aEU)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        return true;
    }

    public static ItemStack[] cleanArray(ItemStack[] input) {
        int aArraySize = input.length;
        ArrayList<ItemStack> aCleanedItems = new ArrayList<>();
        for (ItemStack checkStack : input) {
            if (ItemUtils.checkForInvalidItems(checkStack)) {
                aCleanedItems.add(checkStack);
            }
        }
        ItemStack[] aOutput = new ItemStack[aCleanedItems.size()];
        for (int i = 0; i < aArraySize; i++) {
            ItemStack aMappedStack = aCleanedItems.get(i);
            if (aMappedStack != null) {
                aOutput[i] = aMappedStack;
            }
        }
        return aOutput;
    }
}
