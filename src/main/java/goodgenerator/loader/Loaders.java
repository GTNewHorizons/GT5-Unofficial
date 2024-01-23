package goodgenerator.loader;

import static goodgenerator.util.DescTextLocalization.addText;
import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.blocks.myFluids.FluidsBuilder;
import goodgenerator.blocks.regularBlock.Casing;
import goodgenerator.blocks.regularBlock.ComplexTextureCasing;
import goodgenerator.blocks.regularBlock.Frame;
import goodgenerator.blocks.regularBlock.TEBlock;
import goodgenerator.blocks.regularBlock.TurbineCasing;
import goodgenerator.blocks.tileEntity.ComponentAssemblyLine;
import goodgenerator.blocks.tileEntity.CoolantTower;
import goodgenerator.blocks.tileEntity.EssentiaHatch;
import goodgenerator.blocks.tileEntity.EssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.EssentiaOutputHatch_ME;
import goodgenerator.blocks.tileEntity.ExtremeHeatExchanger;
import goodgenerator.blocks.tileEntity.FuelRefineFactory;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.DieselGenerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.NeutronAccelerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.NeutronSensor;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.YOTTAHatch;
import goodgenerator.blocks.tileEntity.LargeEssentiaGenerator;
import goodgenerator.blocks.tileEntity.LargeEssentiaSmeltery;
import goodgenerator.blocks.tileEntity.LargeFusionComputer1;
import goodgenerator.blocks.tileEntity.LargeFusionComputer2;
import goodgenerator.blocks.tileEntity.LargeFusionComputer3;
import goodgenerator.blocks.tileEntity.LargeFusionComputer4;
import goodgenerator.blocks.tileEntity.LargeFusionComputer5;
import goodgenerator.blocks.tileEntity.MultiNqGenerator;
import goodgenerator.blocks.tileEntity.NeutronActivator;
import goodgenerator.blocks.tileEntity.PreciseAssembler;
import goodgenerator.blocks.tileEntity.SupercriticalFluidTurbine;
import goodgenerator.blocks.tileEntity.UniversalChemicalFuelEngine;
import goodgenerator.blocks.tileEntity.YottaFluidTank;
import goodgenerator.client.render.BlockRenderHandler;
import goodgenerator.crossmod.ic2.CropsLoader;
import goodgenerator.crossmod.nei.NEI_Config;
import goodgenerator.crossmod.thaumcraft.LargeEssentiaEnergyData;
import goodgenerator.items.MyItemBlocks;
import goodgenerator.items.MyItems;
import goodgenerator.items.MyMaterial;
import goodgenerator.items.RadioactiveItem;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.MaterialFix;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class Loaders {

    public static final int IDOffset = 32001;
    public static final byte GoodGeneratorTexturePage = 12;

    public static final Item _null_ = new MyItems("_null_", null);

    public static final Item radiationProtectionPlate = new MyItems("radiationProtectionPlate", GoodGenerator.GG);
    public static final Item wrappedUraniumIngot = new MyItems("wrappedUraniumIngot", GoodGenerator.GG);
    public static final Item highDensityUraniumNugget = new RadioactiveItem(
            "highDensityUraniumNugget",
            GoodGenerator.GG,
            200);
    public static final Item highDensityUranium = new RadioactiveItem("highDensityUranium", GoodGenerator.GG, 1800);
    public static final Item wrappedThoriumIngot = new MyItems("wrappedThoriumIngot", GoodGenerator.GG);
    public static final Item highDensityThoriumNugget = new RadioactiveItem(
            "highDensityThoriumNugget",
            GoodGenerator.GG,
            50);
    public static final Item highDensityThorium = new RadioactiveItem("highDensityThorium", GoodGenerator.GG, 450);
    public static final Item wrappedPlutoniumIngot = new MyItems("wrappedPlutoniumIngot", GoodGenerator.GG);
    public static final Item highDensityPlutoniumNugget = new RadioactiveItem(
            "highDensityPlutoniumNugget",
            GoodGenerator.GG,
            450);
    public static final Item highDensityPlutonium = new RadioactiveItem("highDensityPlutonium", GoodGenerator.GG, 4050);
    public static final Item rawAtomicSeparationCatalyst = new MyItems("rawAtomicSeparationCatalyst", GoodGenerator.GG);
    public static final Item advancedRadiationProtectionPlate = new MyItems(
            "advancedRadiationProtectionPlate",
            GoodGenerator.GG);
    public static final Item aluminumNitride = new MyItems("aluminumNitride", "AlN", GoodGenerator.GG);
    public static final Item specialCeramics = new MyItems("specialCeramics", GoodGenerator.GG);
    public static final Item specialCeramicsPlate = new MyItems("specialCeramicsPlate", GoodGenerator.GG);
    public static final Item radioactiveWaste = new RadioactiveItem("radioactiveWaste", GoodGenerator.GG, 400);
    public static final Item plasticCase = new MyItems("plasticCase", GoodGenerator.GG);
    public static final Item quartzWafer = new MyItems("quartzWafer", GoodGenerator.GG);
    public static final Item microHeater = new MyItems("microHeater", GoodGenerator.GG);
    public static final Item quartzCrystalResonator = new MyItems("quartzCrystalResonator", GoodGenerator.GG);
    public static final Item inverter = new MyItems("inverter", addText("inverter.tooltip", 1), GoodGenerator.GG);
    public static final Item neutronSource = new MyItems("neutronSource", GoodGenerator.GG);
    public static final Item naquadahMass = new MyItems(
            "naquadahMass",
            addText("naquadahMass.tooltip", 1),
            GoodGenerator.GG);
    public static final Item enrichedNaquadahMass = new MyItems(
            "enrichedNaquadahMass",
            addText("enrichedNaquadahMass.tooltip", 1),
            GoodGenerator.GG);
    public static final Item naquadriaMass = new MyItems(
            "naquadriaMass",
            addText("naquadriaMass.tooltip", 1),
            GoodGenerator.GG);
    public static final Item advancedFuelRod = new MyItems("advancedFuelRod", GoodGenerator.GG);
    public static final Item fluidCore = new MyItems(
            "fluidCore",
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":fluidCore/1", GoodGenerator.MOD_ID + ":fluidCore/2",
                    GoodGenerator.MOD_ID + ":fluidCore/3", GoodGenerator.MOD_ID + ":fluidCore/4",
                    GoodGenerator.MOD_ID + ":fluidCore/5", GoodGenerator.MOD_ID + ":fluidCore/6",
                    GoodGenerator.MOD_ID + ":fluidCore/7", GoodGenerator.MOD_ID + ":fluidCore/8",
                    GoodGenerator.MOD_ID + ":fluidCore/9", GoodGenerator.MOD_ID + ":fluidCore/10" });
    public static final Item upgradeEssentia = new MyItems(
            "upgradeEssentia",
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":upgradeEssentia/null",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/air", GoodGenerator.MOD_ID + ":upgradeEssentia/thermal",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/unstable",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/victus", GoodGenerator.MOD_ID + ":upgradeEssentia/tainted",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/mechanics",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/spirit",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/radiation",
                    GoodGenerator.MOD_ID + ":upgradeEssentia/electric" });
    public static final Item highEnergyMixture = new MyItems(
            "highEnergyMixture",
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":highEnergyMixture" });
    public static final Item saltyRoot = new MyItems(
            "saltyRoot",
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":saltyRoot" });
    public static final Item huiCircuit = new MyItems(
            "huiCircuit",
            addText("huiCircuit.tooltip", 5),
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":ciruits/1", GoodGenerator.MOD_ID + ":ciruits/2",
                    GoodGenerator.MOD_ID + ":ciruits/3", GoodGenerator.MOD_ID + ":ciruits/4",
                    GoodGenerator.MOD_ID + ":ciruits/5", });

    public static final Item circuitWrap = new MyItems(
            "circuitWrap",
            GoodGenerator.GG,
            new String[] { GoodGenerator.MOD_ID + ":wraps/0", GoodGenerator.MOD_ID + ":wraps/1",
                    GoodGenerator.MOD_ID + ":wraps/2", GoodGenerator.MOD_ID + ":wraps/3",
                    GoodGenerator.MOD_ID + ":wraps/4", GoodGenerator.MOD_ID + ":wraps/5",
                    GoodGenerator.MOD_ID + ":wraps/6", GoodGenerator.MOD_ID + ":wraps/7",
                    GoodGenerator.MOD_ID + ":wraps/8", GoodGenerator.MOD_ID + ":wraps/9",
                    GoodGenerator.MOD_ID + ":wraps/10", GoodGenerator.MOD_ID + ":wraps/11",
                    GoodGenerator.MOD_ID + ":wraps/12", GoodGenerator.MOD_ID + ":wraps/13",
                    GoodGenerator.MOD_ID + ":wraps/14" });

    public static final Block MAR_Casing = new Casing(
            "MAR_Casing",
            new String[] { GoodGenerator.MOD_ID + ":MAR_Casing" });
    public static final Block FRF_Casings = new Casing(
            "FRF_Casing",
            new String[] { "gregtech:iconsets/MACHINE_CASING_MINING_BLACKPLUTONIUM" });
    public static final Block FRF_Coil_1 = new Casing(
            "FRF_Coil_1",
            new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/1" });
    public static final Block FRF_Coil_2 = new Casing(
            "FRF_Coil_2",
            new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/2" });
    public static final Block FRF_Coil_3 = new Casing(
            "FRF_Coil_3",
            new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/3" });
    public static final Block FRF_Coil_4 = new Casing(
            "FRF_Coil_4",
            new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/4" });
    public static final Block radiationProtectionSteelFrame = new Frame(
            "radiationProtectionSteelFrame",
            new String[] { GoodGenerator.MOD_ID + ":radiationProtectionSteelFrame" });
    public static final Block fieldRestrictingGlass = new Frame(
            "fieldRestrictingGlass",
            new String[] { GoodGenerator.MOD_ID + ":fieldRestrictingGlass" });
    public static final Block rawCylinder = new Casing(
            "rawCylinder",
            new String[] { GoodGenerator.MOD_ID + ":rawCylinder" });
    public static final Block titaniumPlatedCylinder = new Casing(
            "titaniumPlatedCylinder",
            new String[] { GoodGenerator.MOD_ID + ":titaniumPlatedCylinder" });
    public static final Block magicCasing = new Casing(
            "magicCasing",
            new String[] { GoodGenerator.MOD_ID + ":MagicCasing" });
    public static final Block essentiaCell = new Casing(
            "essentiaCell",
            new String[] { GoodGenerator.MOD_ID + ":essentiaCell/1", GoodGenerator.MOD_ID + ":essentiaCell/2",
                    GoodGenerator.MOD_ID + ":essentiaCell/3", GoodGenerator.MOD_ID + ":essentiaCell/4" });
    public static final Block speedingPipe = new ComplexTextureCasing(
            "speedingPipe",
            new String[] { GoodGenerator.MOD_ID + ":speedingPipe_SIDE" },
            new String[] { GoodGenerator.MOD_ID + ":speedingPipe_TOP" });
    public static final Block yottaFluidTankCell = new Casing(
            "yottaFluidTankCell",
            new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCell/1",
                    GoodGenerator.MOD_ID + ":yottaFluidTankCell/2", GoodGenerator.MOD_ID + ":yottaFluidTankCell/3",
                    GoodGenerator.MOD_ID + ":yottaFluidTankCell/4", GoodGenerator.MOD_ID + ":yottaFluidTankCell/5",
                    GoodGenerator.MOD_ID + ":yottaFluidTankCell/6", GoodGenerator.MOD_ID + ":yottaFluidTankCell/7",
                    GoodGenerator.MOD_ID + ":yottaFluidTankCell/8", GoodGenerator.MOD_ID + ":yottaFluidTankCell/9",
                    GoodGenerator.MOD_ID + ":yottaFluidTankCell/10", });
    public static final Block yottaFluidTankCasing = new ComplexTextureCasing(
            "yottaFluidTankCasing",
            new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCasing_SIDE" },
            new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCasing_TOP" });
    public static final Block supercriticalFluidTurbineCasing = new TurbineCasing(
            "supercriticalFluidTurbineCasing",
            "supercriticalFluidTurbineCasing");
    public static final Block pressureResistantWalls = new Casing(
            "pressureResistantWalls",
            new String[] { GoodGenerator.MOD_ID + ":pressureResistantWalls" });
    public static final Block preciseUnitCasing = new Casing(
            "preciseUnitCasing",
            new String[] { GoodGenerator.MOD_ID + ":preciseUnitCasing/1", GoodGenerator.MOD_ID + ":preciseUnitCasing/2",
                    GoodGenerator.MOD_ID + ":preciseUnitCasing/3" });
    public static final Block compactFusionCoil = new Casing(
            "compactFusionCoil",
            new String[] { GoodGenerator.MOD_ID + ":fuison/1", GoodGenerator.MOD_ID + ":fuison/2",
                    GoodGenerator.MOD_ID + ":fuison/3", GoodGenerator.MOD_ID + ":fuison/4",
                    GoodGenerator.MOD_ID + ":fuison/5" });
    public static final Block essentiaFilterCasing = new Casing(
            "essentiaFilterCasing",
            new String[] { GoodGenerator.MOD_ID + ":essentiaFilterCasing" });
    public static Block essentiaHatch;
    public static Block essentiaOutputHatch;
    public static Block essentiaOutputHatch_ME;
    public static final Block componentAssemblylineCasing = new Casing(
            "componentAssemblyLineCasing",
            new String[] { GoodGenerator.MOD_ID + ":compAsslineCasing/0", // LV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/1", // MV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/2", // HV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/3", // EV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/4", // IV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/5", // LuV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/6", // ZPM
                    GoodGenerator.MOD_ID + ":compAsslineCasing/7", // UV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/8", // UHV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/9", // UEV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/10", // UIV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/11", // UMV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/12", // UXV
                    GoodGenerator.MOD_ID + ":compAsslineCasing/13" // MAX
            });
    public static ItemStack MAR;
    public static ItemStack FRF;
    public static ItemStack UCFE;
    public static ItemStack LEG;
    public static ItemStack NS;
    public static ItemStack NA;
    public static ItemStack YFT;
    public static ItemStack YFH;
    public static ItemStack SCTurbine;
    public static ItemStack XHE;
    public static ItemStack PA;
    public static ItemStack LES;
    public static ItemStack CT;
    public static ItemStack[] LFC = new ItemStack[5];

    public static ItemStack[] NeutronAccelerators = new ItemStack[9];
    public static ItemStack[] Generator_Diesel = new ItemStack[2];

    public static ItemStack CompAssline;
    // public static Item Isotope = new NuclearMetaItemGenerator();

    public static void GTMetaTileRegister() {
        Loaders.MAR = new MultiNqGenerator(12732, "NaG", "Large Naquadah Reactor").getStackForm(1L);
        Loaders.FRF = new FuelRefineFactory(16999, "FRF", "Naquadah Fuel Refinery").getStackForm(1L);
        Loaders.UCFE = new UniversalChemicalFuelEngine(
                IDOffset,
                "UniversalChemicalFuelEngine",
                "Universal Chemical Fuel Engine").getStackForm(1L);
        for (int i = 0; i < 9; i++) {
            Loaders.NeutronAccelerators[i] = new NeutronAccelerator(
                    IDOffset + 2 + i,
                    "Neutron Accelerator " + GT_Values.VN[i],
                    "Neutron Accelerator " + GT_Values.VN[i],
                    i).getStackForm(1L);
        }
        Loaders.NS = new NeutronSensor(IDOffset + 11, "Neutron Sensor", "Neutron Sensor", 5).getStackForm(1L);
        Loaders.NA = new NeutronActivator(IDOffset + 12, "NeutronActivator", "Neutron Activator").getStackForm(1L);
        Loaders.YFT = new YottaFluidTank(IDOffset + 13, "YottaFluidTank", "YOTTank").getStackForm(1L);
        Loaders.YFH = new YOTTAHatch(IDOffset + 14, "YottaFluidTankHatch", "YOTHatch", 5).getStackForm(1L);
        Loaders.SCTurbine = new SupercriticalFluidTurbine(
                IDOffset + 15,
                "SupercriticalSteamTurbine",
                "SC Steam Turbine").getStackForm(1L);
        Loaders.XHE = new ExtremeHeatExchanger(IDOffset + 16, "ExtremeHeatExchanger", "Extreme Heat Exchanger")
                .getStackForm(1L);
        Loaders.PA = new PreciseAssembler(IDOffset + 17, "PreciseAssembler", "Precise Auto-Assembler MT-3662")
                .getStackForm(1L);
        Loaders.LFC[0] = new LargeFusionComputer1(
                IDOffset + 18,
                "LargeFusionComputer1",
                "Compact Fusion Computer MK-I Prototype").getStackForm(1);
        Loaders.LFC[1] = new LargeFusionComputer2(
                IDOffset + 19,
                "LargeFusionComputer2",
                "Compact Fusion Computer MK-II").getStackForm(1L);
        Loaders.LFC[2] = new LargeFusionComputer3(
                IDOffset + 20,
                "LargeFusionComputer3",
                "Compact Fusion Computer MK-III").getStackForm(1L);
        if (GTPlusPlus.isModLoaded()) {
            Loaders.LFC[3] = new LargeFusionComputer4(
                    IDOffset + 21,
                    "LargeFusionComputer4",
                    "Compact Fusion Computer MK-IV Prototype").getStackForm(1L);
            Loaders.LFC[4] = new LargeFusionComputer5(
                    IDOffset + 22,
                    "LargeFusionComputer5",
                    "Compact Fusion Computer MK-V").getStackForm(1L);
        }
        Loaders.Generator_Diesel[0] = new DieselGenerator(
                1113,
                "basicgenerator.diesel.tier.04",
                "Turbo Supercharging Combustion Generator",
                4).getStackForm(1L);
        Loaders.Generator_Diesel[1] = new DieselGenerator(
                1114,
                "basicgenerator.diesel.tier.05",
                "Ultimate Chemical Energy Releaser",
                5).getStackForm(1L);
        Loaders.CT = new CoolantTower(IDOffset + 24, "CoolantTower", "Coolant Tower").getStackForm(1L);
        Loaders.CompAssline = new ComponentAssemblyLine(
                IDOffset + 25,
                "ComponentAssemblyLine",
                "Component Assembly Line").getStackForm(1L);
        CrackRecipeAdder.registerPipe(30995, MyMaterial.incoloy903, 15000, 8000, true);
        CrackRecipeAdder.registerWire(32749, MyMaterial.signalium, 12, 131072, 16, true);
        CrackRecipeAdder.registerWire(32737, MyMaterial.lumiium, 8, 524288, 64, true);
    }

    public static void Register() {

        GameRegistry.registerItem(_null_, "_null_", GoodGenerator.MOD_ID);
        NEI_Config.hide(_null_);

        GameRegistry.registerBlock(MAR_Casing, MyItemBlocks.class, "MAR_Casing");
        GameRegistry.registerBlock(radiationProtectionSteelFrame, MyItemBlocks.class, "radiationProtectionSteelFrame");
        GameRegistry.registerBlock(fieldRestrictingGlass, MyItemBlocks.class, "fieldRestrictingGlass");
        GameRegistry.registerBlock(FRF_Casings, MyItemBlocks.class, "FRF_Casings");
        GameRegistry.registerBlock(FRF_Coil_1, MyItemBlocks.class, "FRF_Coil_1");
        GameRegistry.registerBlock(FRF_Coil_2, MyItemBlocks.class, "FRF_Coil_2");
        GameRegistry.registerBlock(FRF_Coil_3, MyItemBlocks.class, "FRF_Coil_3");
        GameRegistry.registerBlock(FRF_Coil_4, MyItemBlocks.class, "FRF_Coil_4");
        GameRegistry.registerBlock(rawCylinder, MyItemBlocks.class, "rawCylinder");
        GameRegistry.registerBlock(titaniumPlatedCylinder, MyItemBlocks.class, "titaniumPlatedCylinder");
        GameRegistry.registerBlock(speedingPipe, MyItemBlocks.class, "speedingPipe");
        GameRegistry.registerBlock(yottaFluidTankCell, MyItemBlocks.class, "yottaFluidTankCells");
        GameRegistry.registerBlock(yottaFluidTankCasing, MyItemBlocks.class, "yottaFluidTankCasing");
        GameRegistry
                .registerBlock(supercriticalFluidTurbineCasing, MyItemBlocks.class, "supercriticalFluidTurbineCasing");
        GameRegistry.registerBlock(componentAssemblylineCasing, MyItemBlocks.class, "componentAssemblylineCasing");
        GameRegistry.registerBlock(pressureResistantWalls, MyItemBlocks.class, "pressureResistantWalls");
        GameRegistry.registerBlock(preciseUnitCasing, MyItemBlocks.class, "preciseUnitCasing");
        GameRegistry.registerBlock(compactFusionCoil, MyItemBlocks.class, "compactFusionCoil");
        GameRegistry.registerItem(radiationProtectionPlate, "radiationProtectionPlate", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedUraniumIngot, "wrappedUraniumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityUraniumNugget, "highDensityUraniumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityUranium, "highDensityUranium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedThoriumIngot, "wrappedThoriumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityThoriumNugget, "highDensityThoriumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityThorium, "highDensityThorium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedPlutoniumIngot, "wrappedPlutoniumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityPlutoniumNugget, "highDensityPlutoniumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityPlutonium, "highDensityPlutonium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rawAtomicSeparationCatalyst, "rawAtomicSeparationCatalyst", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                advancedRadiationProtectionPlate,
                "advancedRadiationProtectionPlate",
                GoodGenerator.MOD_ID);
        GameRegistry.registerItem(aluminumNitride, "aluminumNitride", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(specialCeramics, "specialCeramics", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(specialCeramicsPlate, "specialCeramicsPlate", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(radioactiveWaste, "radioactiveWaste", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(plasticCase, "plasticCase", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(quartzWafer, "quartzWafer", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(microHeater, "microHeater", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(quartzCrystalResonator, "quartzCrystalResonator", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(inverter, "inverter", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(neutronSource, "neutronSource", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(naquadahMass, "naquadahMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(enrichedNaquadahMass, "enrichedNaquadahMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(naquadriaMass, "naquadriaMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(advancedFuelRod, "advancedFuelRod", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(fluidCore, "fluidCore", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highEnergyMixture, "highEnergyMixture", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(saltyRoot, "saltyRoot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(huiCircuit, "huiCircuit", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(circuitWrap, "circuitWrap", GoodGenerator.MOD_ID);
    }

    public static void compactMod() {
        if (Loader.isModLoaded("Thaumcraft")) {
            LargeEssentiaEnergyData.processEssentiaData();
            GameRegistry.registerItem(upgradeEssentia, "upgradeEssentia", GoodGenerator.MOD_ID);
            GameRegistry.registerTileEntity(EssentiaHatch.class, "EssentiaHatch");
            GameRegistry.registerTileEntity(EssentiaOutputHatch.class, "EssentiaOutputHatch");
            GameRegistry.registerTileEntity(EssentiaOutputHatch_ME.class, "EssentiaOutputHatch_ME");
            Loaders.LEG = new LargeEssentiaGenerator(IDOffset + 1, "LargeEssentiaGenerator", "Large Essentia Generator")
                    .getStackForm(1L);
            Loaders.LES = new LargeEssentiaSmeltery(IDOffset + 23, "LargeEssentiaSmeltery", "Large Essentia Smeltery")
                    .getStackForm(1L);
            essentiaHatch = new TEBlock("essentiaHatch", new String[] { GoodGenerator.MOD_ID + ":essentiaHatch" }, 1);
            essentiaOutputHatch = new TEBlock(
                    "essentiaOutputHatch",
                    new String[] { GoodGenerator.MOD_ID + ":essentiaOutputHatch" },
                    2);
            essentiaOutputHatch_ME = new TEBlock(
                    "essentiaOutputHatch_ME",
                    new String[] { GoodGenerator.MOD_ID + ":essentiaOutputHatch_ME" },
                    3);
            GameRegistry.registerBlock(magicCasing, MyItemBlocks.class, "magicCasing");
            GameRegistry.registerBlock(essentiaCell, MyItemBlocks.class, "essentiaCell");
            GameRegistry.registerBlock(essentiaHatch, MyItemBlocks.class, "essentiaHatch");
            GameRegistry.registerBlock(essentiaOutputHatch, MyItemBlocks.class, "essentiaOutputHatch");
            GameRegistry.registerBlock(essentiaFilterCasing, MyItemBlocks.class, "essentiaFilterCasing");
            GameRegistry.registerBlock(essentiaOutputHatch_ME, MyItemBlocks.class, "essentiaOutputHatch_ME");

            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][0] = TextureFactory.of(magicCasing);
        }
    }

    public static void addOreDic() {
        OreDictionary.registerOre("dustAluminumNitride", aluminumNitride);
    }

    public static void addTexturePage() {
        if (Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] == null) {
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] = new ITexture[128];
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][1] = TextureFactory
                    .of(yottaFluidTankCasing);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][2] = TextureFactory
                    .of(supercriticalFluidTurbineCasing);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][3] = TextureFactory
                    .of(preciseUnitCasing, 0);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][4] = TextureFactory
                    .of(preciseUnitCasing, 1);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][5] = TextureFactory
                    .of(preciseUnitCasing, 2);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][6] = TextureFactory
                    .of(GregTech_API.sBlockConcretes, 8);
        }
    }

    public static void preInitLoad() {
        Register();
        addOreDic();
        addTexturePage();
        compactMod();
        FluidsBuilder.Register();
        FuelRodLoader.RegisterRod();
    }

    public static void initLoad() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            new BlockRenderHandler();
        }
        GTMetaTileRegister();
        initLoadRecipes();
        CropsLoader.registerCrops();
    }

    public static void postInitLoad() {
        postInitLoadRecipes();
    }

    public static void completeLoad() {
        RecipeLoader_02.FinishLoadRecipe();
        MaterialFix.addRecipeForMultiItems();
        ComponentAssemblyLineRecipeLoader.run();
    }

    public static void initLoadRecipes() {
        RecipeLoader.InitLoadRecipe();
        RecipeLoader_02.InitLoadRecipe();
        FuelRecipeLoader.RegisterFuel();
        NaquadahReworkRecipeLoader.RecipeLoad();
    }

    public static void postInitLoadRecipes() {
        RecipeLoader.RecipeLoad();
        RecipeLoader.Fixer();
        RecipeLoader_02.RecipeLoad();
        NeutronActivatorLoader.NARecipeLoad();
    }
}
