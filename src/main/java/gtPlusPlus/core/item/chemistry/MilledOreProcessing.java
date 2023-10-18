package gtPlusPlus.core.item.chemistry;

import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.data.Quad;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.bop.HANDLER_BiomesOPlenty;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;

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

        milledSphalerite = BaseItemMilledOre.generate(Materials.Sphalerite, MaterialUtils.getVoltageForTier(6));
        milledChalcopyrite = BaseItemMilledOre.generate(Materials.Chalcopyrite, MaterialUtils.getVoltageForTier(5));
        milledNickel = BaseItemMilledOre.generate(Materials.Nickel, MaterialUtils.getVoltageForTier(5));
        milledPlatinum = BaseItemMilledOre.generate(Materials.Platinum, MaterialUtils.getVoltageForTier(6));
        milledPentlandite = BaseItemMilledOre.generate(Materials.Pentlandite, MaterialUtils.getVoltageForTier(6));

        milledRedstone = BaseItemMilledOre.generate(Materials.Redstone, MaterialUtils.getVoltageForTier(5));
        milledSpessartine = BaseItemMilledOre.generate(Materials.Spessartine, MaterialUtils.getVoltageForTier(6));
        milledGrossular = BaseItemMilledOre.generate(Materials.Grossular, MaterialUtils.getVoltageForTier(6));
        milledAlmandine = BaseItemMilledOre.generate(Materials.Almandine, MaterialUtils.getVoltageForTier(6));
        milledPyrope = BaseItemMilledOre.generate(Materials.Pyrope, MaterialUtils.getVoltageForTier(4));
        milledMonazite = BaseItemMilledOre.generate(Materials.Monazite, MaterialUtils.getVoltageForTier(7));
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

    private static void addMiscRecipes() {

        /*
         * First 5
         */

        // milledSphalerite
        registerOreDataForMilledType(
                SphaleriteFlotationFroth,
                ELEMENT.getInstance().ZINC,
                180,
                ELEMENT.getInstance().IRON,
                120,
                ELEMENT.getInstance().INDIUM,
                64,
                ELEMENT.getInstance().GERMANIUM,
                15);
        // milledChalcopyrite
        registerOreDataForMilledType(
                ChalcopyriteFlotationFroth,
                ELEMENT.getInstance().COPPER,
                180,
                ELEMENT.getInstance().IRON,
                120,
                ELEMENT.getInstance().CADMIUM,
                50,
                ELEMENT.getInstance().INDIUM,
                10);
        // milledNickel
        registerOreDataForMilledType(
                NickelFlotationFroth,
                ELEMENT.getInstance().NICKEL,
                150,
                ELEMENT.getInstance().COBALT,
                120,
                ELEMENT.getInstance().RHODIUM,
                32,
                ELEMENT.getInstance().RUTHENIUM,
                16);
        // milledPlatinum
        registerOreDataForMilledType(
                PlatinumFlotationFroth,
                ELEMENT.getInstance().PLATINUM,
                120,
                ELEMENT.getInstance().RHODIUM,
                60,
                ELEMENT.getInstance().SELENIUM,
                40,
                ELEMENT.getInstance().TELLURIUM,
                10);
        // milledPentlandite
        registerOreDataForMilledType(
                PentlanditeFlotationFroth,
                ELEMENT.getInstance().IRON,
                150,
                ELEMENT.getInstance().NICKEL,
                100,
                ELEMENT.getInstance().PROMETHIUM,
                20,
                ELEMENT.getInstance().HAFNIUM,
                10);

        /*
         * Second 5
         */
        // milledRedstone
        registerOreDataForMilledType(
                RedstoneFlotationFroth,
                NONMATERIAL.REDSTONE,
                300,
                ELEMENT.getInstance().CHROMIUM,
                60,
                MaterialUtils.generateMaterialFromGtENUM(Materials.Firestone),
                45,
                ELEMENT.getInstance().DYSPROSIUM,
                16);
        // milledSpessartine
        registerOreDataForMilledType(
                SpessartineFlotationFroth,
                ELEMENT.getInstance().MANGANESE,
                150,
                ELEMENT.getInstance().ALUMINIUM,
                90,
                ELEMENT.getInstance().OSMIUM,
                30,
                ELEMENT.getInstance().STRONTIUM,
                20);
        // milledGrossular
        registerOreDataForMilledType(
                GrossularFlotationFroth,
                ELEMENT.getInstance().CALCIUM,
                180,
                ELEMENT.getInstance().ALUMINIUM,
                110,
                ELEMENT.getInstance().TUNGSTEN,
                60,
                ELEMENT.getInstance().THALLIUM,
                15);
        // milledAlmandine
        registerOreDataForMilledType(
                AlmandineFlotationFroth,
                ELEMENT.getInstance().ALUMINIUM,
                150,
                ELEMENT.getInstance().MAGNESIUM,
                75,
                ELEMENT.getInstance().YTTRIUM,
                25,
                ELEMENT.getInstance().YTTERBIUM,
                15);
        // milledPyrope
        registerOreDataForMilledType(
                PyropeFlotationFroth,
                ELEMENT.getInstance().MAGNESIUM,
                110,
                ELEMENT.getInstance().MANGANESE,
                70,
                MaterialUtils.generateMaterialFromGtENUM(Materials.Borax),
                60,
                ELEMENT.getInstance().RHENIUM,
                20);
        // milledMonazite TODO
        registerOreDataForMilledType(
                MonaziteFlotationFroth,
                ELEMENT.getInstance().ERBIUM,
                64,
                ELEMENT.getInstance().LANTHANUM,
                32,
                ELEMENT.getInstance().LUTETIUM,
                16,
                ELEMENT.getInstance().EUROPIUM,
                8);
    }

    @Override
    public String errorMessage() {
        return "Failed to generate recipes for OreMillingProc.";
    }

    @Override
    public boolean generateRecipes() {
        addMiscRecipes();
        addPineOilExtraction();
        addFlotationRecipes1();
        addFlotationRecipes2();
        addVacuumFurnaceRecipes();
        return true;
    }

    private void addVacuumFurnaceRecipes() {
        int aCircuitID = 1;

        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(SphaleriteFlotationFroth, 4000) },
                getOutputsFromMap(SphaleriteFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(ChalcopyriteFlotationFroth, 4000) },
                getOutputsFromMap(ChalcopyriteFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(5),
                4500);

        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(NickelFlotationFroth, 4000) },
                getOutputsFromMap(NickelFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(5),
                4500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(PlatinumFlotationFroth, 4000) },
                getOutputsFromMap(PlatinumFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(PentlanditeFlotationFroth, 4000) },
                getOutputsFromMap(PentlanditeFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);

        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(RedstoneFlotationFroth, 4000) },
                getOutputsFromMap(RedstoneFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(5),
                4500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(SpessartineFlotationFroth, 4000) },
                getOutputsFromMap(SpessartineFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(GrossularFlotationFroth, 4000) },
                getOutputsFromMap(GrossularFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(AlmandineFlotationFroth, 4000) },
                getOutputsFromMap(AlmandineFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(6),
                5500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(PyropeFlotationFroth, 4000) },
                getOutputsFromMap(PyropeFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(4),
                3500);
        CORE.RA.addVacuumFurnaceRecipe(
                new ItemStack[] { CI.getNumberedCircuit(aCircuitID++) },
                new FluidStack[] { FluidUtils.getFluidStack(MonaziteFlotationFroth, 4000) },
                getOutputsFromMap(MonaziteFlotationFroth),
                new FluidStack[] { FluidUtils.getFluidStack(AgriculturalChem.RedMud, 2000), FluidUtils.getWater(2000) },
                20 * 120,
                MaterialUtils.getVoltageForTier(7),
                7500);
    }

    private void addFlotationRecipes1() {

        // Sphalerite
        CORE.RA.addFlotationRecipe(
                Materials.Sphalerite,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 14000), },
                new FluidStack[] { FluidUtils.getFluidStack(SphaleriteFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));

        // Chalcopyrite
        CORE.RA.addFlotationRecipe(
                Materials.Chalcopyrite,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 12000), },
                new FluidStack[] { FluidUtils.getFluidStack(ChalcopyriteFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(5));

        // Nickel
        CORE.RA.addFlotationRecipe(
                Materials.Nickel,
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 25000), },
                new FluidStack[] { FluidUtils.getFluidStack(NickelFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(5));

        // Platinum
        CORE.RA.addFlotationRecipe(
                Materials.Platinum,
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 35000), },
                new FluidStack[] { FluidUtils.getFluidStack(PlatinumFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));

        // Pentlandite
        CORE.RA.addFlotationRecipe(
                Materials.Pentlandite,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 14000), },
                new FluidStack[] { FluidUtils.getFluidStack(PentlanditeFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));
    }

    private void addFlotationRecipes2() {

        // Redstone
        CORE.RA.addFlotationRecipe(
                Materials.Redstone,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 13000), },
                new FluidStack[] { FluidUtils.getFluidStack(RedstoneFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(5));

        // Spessartine
        CORE.RA.addFlotationRecipe(
                Materials.Spessartine,
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 35000), },
                new FluidStack[] { FluidUtils.getFluidStack(SpessartineFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));

        // Grossular
        CORE.RA.addFlotationRecipe(
                Materials.Grossular,
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 28000), },
                new FluidStack[] { FluidUtils.getFluidStack(GrossularFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));

        // Almandine
        CORE.RA.addFlotationRecipe(
                Materials.Almandine,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 18000), },
                new FluidStack[] { FluidUtils.getFluidStack(AlmandineFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(5));

        // Pyrope
        CORE.RA.addFlotationRecipe(
                Materials.Pyrope,
                ItemUtils.getSimpleStack(GenericChem.mSodiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 8000), },
                new FluidStack[] { FluidUtils.getFluidStack(PyropeFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(4));

        // Monazite
        CORE.RA.addFlotationRecipe(
                Materials.Monazite,
                ItemUtils.getSimpleStack(GenericChem.mPotassiumEthylXanthate, 1),
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 30000), },
                new FluidStack[] { FluidUtils.getFluidStack(MonaziteFlotationFroth, 1000) },
                20 * 480,
                MaterialUtils.getVoltageForTier(6));
    }

    private void addPineOilExtraction() {
        AutoMap<ItemStack> aLogs = new AutoMap<>();
        AutoMap<ItemStack> aLeaves = new AutoMap<>();
        AutoMap<ItemStack> aSaplings = new AutoMap<>();
        AutoMap<ItemStack> aPinecones = new AutoMap<>();

        ItemStack aCrushedPine = ItemUtils.getSimpleStack(AgriculturalChem.mCrushedPine, 1);

        aLogs.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.log_Pine));
        aLeaves.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.leaves_Pine));
        aSaplings.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Pine));
        aPinecones.add(ItemUtils.getSimpleStack(AgriculturalChem.mPinecone, 1));

        if (BiomesOPlenty.isModLoaded()) {
            aLogs.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.logs4, 0, 1));
            aLeaves.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.colorizedLeaves2, 1, 1));
            aSaplings.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.colorizedSaplings, 5, 1));
            aPinecones.add(ItemUtils.simpleMetaStack(HANDLER_BiomesOPlenty.mPineCone, 13, 1));
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

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(16), ItemUtils.getSimpleStack(aCrushedPine, 64) },
                new FluidStack[] { FluidUtils.getSteam(5000), },
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5) },
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 500) },
                new int[] { 2000, 2000, 2000, 2000 },
                20 * 60,
                120,
                3);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(18), ItemUtils.getSimpleStack(aCrushedPine, 64) },
                new FluidStack[] { FluidUtils.getSuperHeatedSteam(5000), },
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5) },
                new FluidStack[] { FluidUtils.getFluidStack(PineOil, 1500) },
                new int[] { 3000, 3000, 3000, 3000 },
                20 * 45,
                120,
                4);
    }

    public boolean addRecipe(ItemStack aInput, ItemStack aOutput1, int[] aChances, int aTime, int aEU) {
        aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
        ItemStack aOutputs[] = new ItemStack[4];
        for (int i = 0; i < aChances.length; i++) {
            aOutputs[i] = aOutput1;
        }
        aOutputs = cleanArray(aOutputs);
        if ((GT_Utility.isStackInvalid(aInput))
                || (GT_Utility.isStackInvalid(aOutput1) || (GT_Utility.getContainerItem(aInput, false) != null))) {
            return false;
        }

        return CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedAdvancedCircuit(14), aInput },
                new FluidStack[] {},
                aOutputs,
                new FluidStack[] {},
                aChances,
                aTime * 20,
                aEU,
                3);
    }

    public static ItemStack[] cleanArray(ItemStack[] input) {
        int aArraySize = input.length;
        AutoMap<ItemStack> aCleanedItems = new AutoMap<>();
        for (ItemStack checkStack : input) {
            if (ItemUtils.checkForInvalidItems(checkStack)) {
                aCleanedItems.put(checkStack);
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

    private static final HashMap<String, Quad<Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>>> aMilledFluidMap = new HashMap<>();

    public static void registerOreDataForMilledType(Fluid aMilledFluid, Materials aOutput1, int aPerc1,
            Materials aOutput2, int aPerc2, Materials aOutput3, int aPerc3, Materials aOutput4, int aPerc4) {
        registerOreDataForMilledType(
                aMilledFluid,
                MaterialUtils.generateMaterialFromGtENUM(aOutput1),
                aPerc1,
                MaterialUtils.generateMaterialFromGtENUM(aOutput2),
                aPerc2,
                MaterialUtils.generateMaterialFromGtENUM(aOutput3),
                aPerc3,
                MaterialUtils.generateMaterialFromGtENUM(aOutput4),
                aPerc4);
    }

    public static void registerOreDataForMilledType(Fluid aMilledFluid, Material aOutput1, int aPerc1,
            Material aOutput2, int aPerc2, Material aOutput3, int aPerc3, Material aOutput4, int aPerc4) {

        Pair<Material, Integer> aFluidOutput1 = new Pair<>(aOutput1, aPerc1);
        Pair<Material, Integer> aFluidOutput2 = new Pair<>(aOutput2, aPerc2);
        Pair<Material, Integer> aFluidOutput3 = new Pair<>(aOutput3, aPerc3);
        Pair<Material, Integer> aFluidOutput4 = new Pair<>(aOutput4, aPerc4);
        Quad<Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>> aDataQuad = new Quad<>(
                aFluidOutput1,
                aFluidOutput2,
                aFluidOutput3,
                aFluidOutput4);
        aMilledFluidMap.put(aMilledFluid.getUnlocalizedName(), aDataQuad);
    }

    private static ItemStack[] getOutputsFromMap(Fluid aFluid) {
        String aKey = aFluid.getUnlocalizedName();
        return getArrayFromQuad(aMilledFluidMap.get(aKey));
    }

    private static ItemStack[] getArrayFromQuad(
            Quad<Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>, Pair<Material, Integer>> aData) {
        AutoMap<ItemStack> aOutputs = new AutoMap<>();
        for (Object aPair : aData.values()) {
            if (aPair != null && Pair.class.isInstance(aPair)) {
                Pair aObj = (Pair) aPair;
                Material aMat = (Material) aObj.getKey();
                int aCount = (int) aObj.getValue();
                aOutputs.addAll(getItemStackFromPair(aMat, aCount));
            }
        }
        ItemStack[] aRealOutputArray = new ItemStack[aOutputs.size()];
        int aIndex = 0;
        for (ItemStack aStack : aOutputs) {
            aRealOutputArray[aIndex++] = aStack;
        }
        return aRealOutputArray;
    }

    private static AutoMap<ItemStack> getItemStackFromPair(Material aMat, Integer aCount) {
        AutoMap<ItemStack> aOutputs = new AutoMap<>();
        if (aCount > 64) {
            AutoMap<Integer> sizes = getStackSizes(aCount);
            for (int aSplitSize : sizes) {
                ItemStack aDustStack = aMat.getDust(aSplitSize);
                aOutputs.put(aDustStack);
            }
        } else {
            ItemStack aDustStack = aMat.getDust(aCount);
            aOutputs.put(aDustStack);
        }
        return aOutputs;
    }

    private static AutoMap<Integer> getStackSizes(int aBigSize) {
        AutoMap<Integer> aSizes = new AutoMap<>();
        if (aBigSize <= 64) {
            aSizes.add(aBigSize);
        } else {
            for (int i = aBigSize; i > 0; i -= 64) {
                aSizes.add(i);
            }
        }
        return aSizes;
    }
}
