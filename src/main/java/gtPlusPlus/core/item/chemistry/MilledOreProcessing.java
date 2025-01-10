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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import biomesoplenty.api.content.BOPCBlocks;
import biomesoplenty.api.content.BOPCItems;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

public class MilledOreProcessing extends ItemPackage {

    /**
     * Fluids
     */
    public static Fluid SphaleriteFlotationFroth;

    public static Fluid ChalcopyriteFlotationFroth;
    public static Fluid NickelFlotationFroth;
    public static Fluid PlatinumFlotationFroth;
    public static Fluid PentlanditeFlotationFroth;

    public static Fluid RedstoneFlotationFroth;
    public static Fluid SpessartineFlotationFroth;
    public static Fluid GrossularFlotationFroth;
    public static Fluid AlmandineFlotationFroth;
    public static Fluid PyropeFlotationFroth;
    public static Fluid MonaziteFlotationFroth;

    public static Fluid PineOil;

    /**
     * Items
     */

    // Zinc, Iron, Indium, Germanium
    public static Item milledSphalerite;

    // Copper, Iron, Cadmium, Indium
    public static Item milledChalcopyrite;

    // Nickel, Cobalt, Rhodium, Ruthenium
    public static Item milledNickel;

    // Platinum, Rhodium, Selenium, Tellurium
    public static Item milledPlatinum;

    // Iron, Nickel, Promethium, Hafnium
    public static Item milledPentlandite;

    // Redstone, Chrome, Firestone, Dysprosium
    public static Item milledRedstone;

    // Manganese, Aluminium, Osmium, Strontium
    public static Item milledSpessartine;

    // Calcium, Aluminium, Tungsten, Thallium
    public static Item milledGrossular;

    // Aluminium, Magnesium, Yttrium, Ytterbium
    public static Item milledAlmandine;

    // Magnesium, Manganese, Borax, Rhenium
    public static Item milledPyrope;

    // Erbium, Lanthanum, Praseodymium, Europium
    public static Item milledMonazite;

    @Override
    public void items() {

        milledSphalerite = BaseItemMilledOre.generate(Materials.Sphalerite, (int) TierEU.RECIPE_LuV);
        milledChalcopyrite = BaseItemMilledOre.generate(Materials.Chalcopyrite, (int) TierEU.RECIPE_IV);
        milledNickel = BaseItemMilledOre.generate(Materials.Nickel, (int) TierEU.RECIPE_IV);
        milledPlatinum = BaseItemMilledOre.generate(Materials.Platinum, (int) TierEU.RECIPE_LuV);
        milledPentlandite = BaseItemMilledOre.generate(Materials.Pentlandite, (int) TierEU.RECIPE_LuV);

        milledRedstone = BaseItemMilledOre.generate(Materials.Redstone, (int) TierEU.RECIPE_IV);
        milledSpessartine = BaseItemMilledOre.generate(Materials.Spessartine, (int) TierEU.RECIPE_LuV);
        milledGrossular = BaseItemMilledOre.generate(Materials.Grossular, (int) TierEU.RECIPE_LuV);
        milledAlmandine = BaseItemMilledOre.generate(Materials.Almandine, (int) TierEU.RECIPE_LuV);
        milledPyrope = BaseItemMilledOre.generate(Materials.Pyrope, (int) TierEU.RECIPE_EV);
        milledMonazite = BaseItemMilledOre.generate(Materials.Monazite, (int) TierEU.RECIPE_ZPM);
    }

    @Override
    public void blocks() {
        // None yet
    }

    @Override
    public void fluids() {

        short[] aZincFrothRGB = Materials.Sphalerite.mRGBa;
        SphaleriteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.zincflotation",
            "Sphalerite Froth",
            32 + 175,
            new short[] { aZincFrothRGB[0], aZincFrothRGB[1], aZincFrothRGB[2], 100 },
            true);
        short[] aCopperFrothRGB = Materials.Chalcopyrite.mRGBa;
        ChalcopyriteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.copperflotation",
            "Chalcopyrite Froth",
            32 + 175,
            new short[] { aCopperFrothRGB[0], aCopperFrothRGB[1], aCopperFrothRGB[2], 100 },
            true);
        short[] aNickelFrothRGB = Materials.Nickel.mRGBa;
        NickelFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.nickelflotation",
            "Nickel Froth",
            32 + 175,
            new short[] { aNickelFrothRGB[0], aNickelFrothRGB[1], aNickelFrothRGB[2], 100 },
            true);
        short[] aPlatinumFrothRGB = Materials.Platinum.mRGBa;
        PlatinumFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.platinumflotation",
            "Platinum Froth",
            32 + 175,
            new short[] { aPlatinumFrothRGB[0], aPlatinumFrothRGB[1], aPlatinumFrothRGB[2], 100 },
            true);
        short[] aPentlanditeFrothRGB = Materials.Pentlandite.mRGBa;
        PentlanditeFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.pentlanditeflotation",
            "Pentlandite Froth",
            32 + 175,
            new short[] { aPentlanditeFrothRGB[0], aPentlanditeFrothRGB[1], aPentlanditeFrothRGB[2], 100 },
            true);

        short[] aRedstoneFrothRGB = Materials.Redstone.mRGBa;
        RedstoneFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.redstoneflotation",
            "Redstone Froth",
            32 + 175,
            new short[] { aRedstoneFrothRGB[0], aRedstoneFrothRGB[1], aRedstoneFrothRGB[2], 100 },
            true);
        short[] aSpessartineFrothRGB = Materials.Spessartine.mRGBa;
        SpessartineFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.spessartineflotation",
            "Spessartine Froth",
            32 + 175,
            new short[] { aSpessartineFrothRGB[0], aSpessartineFrothRGB[1], aSpessartineFrothRGB[2], 100 },
            true);
        short[] aGrossularFrothRGB = Materials.Grossular.mRGBa;
        GrossularFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.grossularflotation",
            "Grossular Froth",
            32 + 175,
            new short[] { aGrossularFrothRGB[0], aGrossularFrothRGB[1], aGrossularFrothRGB[2], 100 },
            true);
        short[] aAlmandineFrothRGB = Materials.Almandine.mRGBa;
        AlmandineFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.almandineflotation",
            "Almandine Froth",
            32 + 175,
            new short[] { aAlmandineFrothRGB[0], aAlmandineFrothRGB[1], aAlmandineFrothRGB[2], 100 },
            true);
        short[] aPyropeFrothRGB = Materials.Pyrope.mRGBa;
        PyropeFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.pyropeflotation",
            "Pyrope Froth",
            32 + 175,
            new short[] { aPyropeFrothRGB[0], aPyropeFrothRGB[1], aPyropeFrothRGB[2], 100 },
            true);
        short[] aMonaziteFrothRGB = Materials.Monazite.mRGBa;
        MonaziteFlotationFroth = FluidUtils.generateFluidNoPrefix(
            "froth.Monaziteflotation",
            "Monazite Froth",
            32 + 175,
            new short[] { aMonaziteFrothRGB[0], aMonaziteFrothRGB[1], aMonaziteFrothRGB[2], 100 },
            true);

        PineOil = FluidUtils
            .generateFluidNoPrefix("pineoil", "Pine Oil", 32 + 175, new short[] { 250, 200, 60, 100 }, true);
    }

    public MilledOreProcessing() {
        super();
        Logger.INFO("Adding Ore Milling content");
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
            .fluidInputs(FluidUtils.getFluidStack(SphaleriteFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(ChalcopyriteFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_IV)
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
            .fluidInputs(FluidUtils.getFluidStack(NickelFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_IV)
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
            .fluidInputs(FluidUtils.getFluidStack(PlatinumFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(PentlanditeFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(RedstoneFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_IV)
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
            .fluidInputs(FluidUtils.getFluidStack(SpessartineFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(GrossularFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(AlmandineFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_LuV)
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
            .fluidInputs(FluidUtils.getFluidStack(PyropeFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3500)
            .duration(2 * MINUTES)
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Erbium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 8))
            .fluidInputs(FluidUtils.getFluidStack(MonaziteFlotationFroth, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000))
            .eut((int) TierEU.RECIPE_UV)
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
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 14000))
            .fluidOutputs(FluidUtils.getFluidStack(SphaleriteFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Chalcopyrite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Chalcopyrite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 12000))
            .fluidOutputs(FluidUtils.getFluidStack(ChalcopyriteFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Nickel
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Nickel);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 25000))
            .fluidOutputs(FluidUtils.getFluidStack(NickelFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Platinum
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Platinum);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 35000))
            .fluidOutputs(FluidUtils.getFluidStack(PlatinumFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Pentlandite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Pentlandite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 14000))
            .fluidOutputs(FluidUtils.getFluidStack(PentlanditeFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Redstone
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Redstone);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 13000))
            .fluidOutputs(FluidUtils.getFluidStack(RedstoneFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Spessartine
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Spessartine);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 35000))
            .fluidOutputs(FluidUtils.getFluidStack(SpessartineFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Grossular
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Grossular);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 28000))
            .fluidOutputs(FluidUtils.getFluidStack(GrossularFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(flotationCellRecipes);

        // Almandine
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Almandine);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 18000))
            .fluidOutputs(FluidUtils.getFluidStack(AlmandineFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(flotationCellRecipes);

        // Pyrope
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Pyrope);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 8000))
            .fluidOutputs(FluidUtils.getFluidStack(PyropeFlotationFroth, 1000))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(flotationCellRecipes);

        // Monazite
        aMat = MaterialUtils.generateMaterialFromGtENUM(Materials.Monazite);
        FlotationRecipeHandler.registerOreType(aMat);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64))
            .fluidInputs(FluidUtils.getFluidStack(PineOil, 30000))
            .fluidOutputs(FluidUtils.getFluidStack(MonaziteFlotationFroth, 1000))
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
            .itemInputs(CI.getNumberedAdvancedCircuit(16), ItemUtils.getSimpleStack(aCrushedPine, 64))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5))
            .fluidInputs(FluidUtils.getSteam(5000))
            .fluidOutputs(FluidUtils.getFluidStack(PineOil, 500))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(18), ItemUtils.getSimpleStack(aCrushedPine, 64))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5))
            .fluidInputs(FluidUtils.getSuperHeatedSteam(5000))
            .fluidOutputs(FluidUtils.getFluidStack(PineOil, 1500))
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
            .itemInputs(CI.getNumberedAdvancedCircuit(14), aInput)
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
