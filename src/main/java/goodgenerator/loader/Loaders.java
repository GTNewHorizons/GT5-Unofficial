package goodgenerator.loader;

import static goodgenerator.util.DescTextLocalization.addText;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.blocks.myFluids.FluidsBuilder;
import goodgenerator.blocks.regularBlock.AntimatterRenderBlock;
import goodgenerator.blocks.regularBlock.BlockCasing;
import goodgenerator.blocks.regularBlock.BlockComplexTextureCasing;
import goodgenerator.blocks.regularBlock.BlockFrame;
import goodgenerator.blocks.regularBlock.BlockTEContainer;
import goodgenerator.blocks.regularBlock.BlockTurbineCasing;
import goodgenerator.blocks.tileEntity.AntimatterForge;
import goodgenerator.blocks.tileEntity.AntimatterGenerator;
import goodgenerator.blocks.tileEntity.AntimatterOutputHatch;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronAccelerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronSensor;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTEYOTTAHatch;
import goodgenerator.blocks.tileEntity.MTEComponentAssemblyLine;
import goodgenerator.blocks.tileEntity.MTECoolantTower;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatchME;
import goodgenerator.blocks.tileEntity.MTEExtremeHeatExchanger;
import goodgenerator.blocks.tileEntity.MTEFuelRefineFactory;
import goodgenerator.blocks.tileEntity.MTELargeEssentiaSmeltery;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer1;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer2;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer3;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer4;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer5;
import goodgenerator.blocks.tileEntity.MTEMultiNqGenerator;
import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import goodgenerator.blocks.tileEntity.MTEPreciseAssembler;
import goodgenerator.blocks.tileEntity.MTESupercriticalFluidTurbine;
import goodgenerator.blocks.tileEntity.MTEUniversalChemicalFuelEngine;
import goodgenerator.blocks.tileEntity.MTEYottaFluidTank;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import goodgenerator.crossmod.ic2.CropsLoader;
import goodgenerator.crossmod.nei.NEIConfig;
import goodgenerator.items.GGItem;
import goodgenerator.items.GGItemBlocks;
import goodgenerator.items.GGMaterial;
import goodgenerator.items.RadioactiveItem;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.MaterialFix;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.generators.MTEDieselGenerator;
import kekztech.common.blocks.BlockTFFTStorageField;

public class Loaders {

    public static final byte GoodGeneratorTexturePage = 12;

    public static final Item _null_ = new GGItem("_null_", null);

    public static final Item radiationProtectionPlate = new GGItem("radiationProtectionPlate", GoodGenerator.GG);
    public static final Item wrappedUraniumIngot = new GGItem("wrappedUraniumIngot", GoodGenerator.GG);
    public static final Item highDensityUraniumNugget = new RadioactiveItem(
        "highDensityUraniumNugget",
        GoodGenerator.GG,
        200);
    public static final Item highDensityUranium = new RadioactiveItem("highDensityUranium", GoodGenerator.GG, 1800);
    public static final Item wrappedThoriumIngot = new GGItem("wrappedThoriumIngot", GoodGenerator.GG);
    public static final Item highDensityThoriumNugget = new RadioactiveItem(
        "highDensityThoriumNugget",
        GoodGenerator.GG,
        50);
    public static final Item highDensityThorium = new RadioactiveItem("highDensityThorium", GoodGenerator.GG, 450);
    public static final Item wrappedPlutoniumIngot = new GGItem("wrappedPlutoniumIngot", GoodGenerator.GG);
    public static final Item highDensityPlutoniumNugget = new RadioactiveItem(
        "highDensityPlutoniumNugget",
        GoodGenerator.GG,
        450);
    public static final Item highDensityPlutonium = new RadioactiveItem("highDensityPlutonium", GoodGenerator.GG, 4050);
    public static final Item rawAtomicSeparationCatalyst = new GGItem("rawAtomicSeparationCatalyst", GoodGenerator.GG);
    public static final Item advancedRadiationProtectionPlate = new GGItem(
        "advancedRadiationProtectionPlate",
        GoodGenerator.GG);
    public static final Item aluminumNitride = new GGItem("aluminumNitride", "AlN", GoodGenerator.GG);
    public static final Item specialCeramics = new GGItem("specialCeramics", GoodGenerator.GG);
    public static final Item specialCeramicsPlate = new GGItem("specialCeramicsPlate", GoodGenerator.GG);
    public static final Item radioactiveWaste = new RadioactiveItem("radioactiveWaste", GoodGenerator.GG, 400);
    public static final Item plasticCase = new GGItem("plasticCase", GoodGenerator.GG);
    public static final Item quartzWafer = new GGItem("quartzWafer", GoodGenerator.GG);
    public static final Item microHeater = new GGItem("microHeater", GoodGenerator.GG);
    public static final Item quartzCrystalResonator = new GGItem("quartzCrystalResonator", GoodGenerator.GG);
    public static final Item inverter = new GGItem("inverter", addText("inverter.tooltip", 1), GoodGenerator.GG);
    public static final Item neutronSource = new GGItem("neutronSource", GoodGenerator.GG);
    public static final Item naquadahMass = new GGItem(
        "naquadahMass",
        addText("naquadahMass.tooltip", 1),
        GoodGenerator.GG);
    public static final Item enrichedNaquadahMass = new GGItem(
        "enrichedNaquadahMass",
        addText("enrichedNaquadahMass.tooltip", 1),
        GoodGenerator.GG);
    public static final Item naquadriaMass = new GGItem(
        "naquadriaMass",
        addText("naquadriaMass.tooltip", 1),
        GoodGenerator.GG);
    public static final Item advancedFuelRod = new GGItem("advancedFuelRod", GoodGenerator.GG);
    public static final Item fluidCore = new GGItem(
        "fluidCore",
        GoodGenerator.GG,
        new String[] { GoodGenerator.MOD_ID + ":fluidCore/1", GoodGenerator.MOD_ID + ":fluidCore/2",
            GoodGenerator.MOD_ID + ":fluidCore/3", GoodGenerator.MOD_ID + ":fluidCore/4",
            GoodGenerator.MOD_ID + ":fluidCore/5", GoodGenerator.MOD_ID + ":fluidCore/6",
            GoodGenerator.MOD_ID + ":fluidCore/7", GoodGenerator.MOD_ID + ":fluidCore/8",
            GoodGenerator.MOD_ID + ":fluidCore/9", GoodGenerator.MOD_ID + ":fluidCore/10" });
    public static final Item highEnergyMixture = new GGItem(
        "highEnergyMixture",
        GoodGenerator.GG,
        new String[] { GoodGenerator.MOD_ID + ":highEnergyMixture" });
    public static final Item saltyRoot = new GGItem(
        "saltyRoot",
        GoodGenerator.GG,
        new String[] { GoodGenerator.MOD_ID + ":saltyRoot" });
    public static final Item huiCircuit = new GGItem(
        "huiCircuit",
        addText("huiCircuit.tooltip", 5),
        GoodGenerator.GG,
        new String[] { GoodGenerator.MOD_ID + ":ciruits/1", GoodGenerator.MOD_ID + ":ciruits/2",
            GoodGenerator.MOD_ID + ":ciruits/3", GoodGenerator.MOD_ID + ":ciruits/4",
            GoodGenerator.MOD_ID + ":ciruits/5", });

    public static final Item circuitWrap = new GGItem(
        "circuitWrap",
        GoodGenerator.GG,
        new String[] { GoodGenerator.MOD_ID + ":wraps/0", GoodGenerator.MOD_ID + ":wraps/1",
            GoodGenerator.MOD_ID + ":wraps/2", GoodGenerator.MOD_ID + ":wraps/3", GoodGenerator.MOD_ID + ":wraps/4",
            GoodGenerator.MOD_ID + ":wraps/5", GoodGenerator.MOD_ID + ":wraps/6", GoodGenerator.MOD_ID + ":wraps/7",
            GoodGenerator.MOD_ID + ":wraps/8", GoodGenerator.MOD_ID + ":wraps/9", GoodGenerator.MOD_ID + ":wraps/10",
            GoodGenerator.MOD_ID + ":wraps/11", GoodGenerator.MOD_ID + ":wraps/12", GoodGenerator.MOD_ID + ":wraps/13",
            GoodGenerator.MOD_ID + ":wraps/14" });

    public static final Block MAR_Casing = new BlockCasing(
        "MAR_Casing",
        new String[] { GoodGenerator.MOD_ID + ":MAR_Casing" });
    public static final Block FRF_Casings = new BlockCasing(
        "FRF_Casing",
        new String[] { "gregtech:iconsets/MACHINE_CASING_MINING_BLACKPLUTONIUM" });
    public static final Block FRF_Coil_1 = new BlockCasing(
        "FRF_Coil_1",
        new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/1" });
    public static final Block FRF_Coil_2 = new BlockCasing(
        "FRF_Coil_2",
        new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/2" });
    public static final Block FRF_Coil_3 = new BlockCasing(
        "FRF_Coil_3",
        new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/3" });
    public static final Block FRF_Coil_4 = new BlockCasing(
        "FRF_Coil_4",
        new String[] { GoodGenerator.MOD_ID + ":FRF_Coils/4" });
    public static final Block radiationProtectionSteelFrame = new BlockFrame(
        "radiationProtectionSteelFrame",
        new String[] { GoodGenerator.MOD_ID + ":radiationProtectionSteelFrame" });
    public static final Block fieldRestrictingGlass = new BlockFrame(
        "fieldRestrictingGlass",
        new String[] { GoodGenerator.MOD_ID + ":fieldRestrictingGlass" });
    public static final Block rawCylinder = new BlockCasing(
        "rawCylinder",
        new String[] { GoodGenerator.MOD_ID + ":rawCylinder" });
    public static final Block titaniumPlatedCylinder = new BlockCasing(
        "titaniumPlatedCylinder",
        new String[] { GoodGenerator.MOD_ID + ":titaniumPlatedCylinder" });
    public static final Block magicCasing = new BlockCasing(
        "magicCasing",
        new String[] { GoodGenerator.MOD_ID + ":MagicCasing" });
    public static final Block essentiaCell = new BlockCasing(
        "essentiaCell",
        new String[] { GoodGenerator.MOD_ID + ":essentiaCell/1", GoodGenerator.MOD_ID + ":essentiaCell/2",
            GoodGenerator.MOD_ID + ":essentiaCell/3", GoodGenerator.MOD_ID + ":essentiaCell/4" });
    public static final Block speedingPipe = new BlockComplexTextureCasing(
        "speedingPipe",
        new String[] { GoodGenerator.MOD_ID + ":speedingPipe_SIDE" },
        new String[] { GoodGenerator.MOD_ID + ":speedingPipe_TOP" });
    public static final Block yottaFluidTankCell = new BlockCasing(
        "yottaFluidTankCell",
        new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCell/1", GoodGenerator.MOD_ID + ":yottaFluidTankCell/2",
            GoodGenerator.MOD_ID + ":yottaFluidTankCell/3", GoodGenerator.MOD_ID + ":yottaFluidTankCell/4",
            GoodGenerator.MOD_ID + ":yottaFluidTankCell/5", GoodGenerator.MOD_ID + ":yottaFluidTankCell/6",
            GoodGenerator.MOD_ID + ":yottaFluidTankCell/7", GoodGenerator.MOD_ID + ":yottaFluidTankCell/8",
            GoodGenerator.MOD_ID + ":yottaFluidTankCell/9", GoodGenerator.MOD_ID + ":yottaFluidTankCell/10", });
    public static final Block yottaFluidTankCasing = new BlockComplexTextureCasing(
        "yottaFluidTankCasing",
        new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCasing_SIDE" },
        new String[] { GoodGenerator.MOD_ID + ":yottaFluidTankCasing_TOP" });
    public static final Block supercriticalFluidTurbineCasing = new BlockTurbineCasing(
        "supercriticalFluidTurbineCasing",
        "supercriticalFluidTurbineCasing");
    public static final Block pressureResistantWalls = new BlockCasing(
        "pressureResistantWalls",
        new String[] { GoodGenerator.MOD_ID + ":pressureResistantWalls" });
    public static final Block impreciseUnitCasing = new BlockCasing(
        "impreciseUnitCasing",
        new String[] { GoodGenerator.MOD_ID + ":preciseUnitCasing/0" });
    public static final Block preciseUnitCasing = new BlockCasing(
        "preciseUnitCasing",
        new String[] { GoodGenerator.MOD_ID + ":preciseUnitCasing/1", GoodGenerator.MOD_ID + ":preciseUnitCasing/2",
            GoodGenerator.MOD_ID + ":preciseUnitCasing/3", GoodGenerator.MOD_ID + ":preciseUnitCasing/4" });
    public static final Block compactFusionCoil = new BlockCasing(
        "compactFusionCoil",
        new String[] { GoodGenerator.MOD_ID + ":fuison/1", GoodGenerator.MOD_ID + ":fuison/2",
            GoodGenerator.MOD_ID + ":fuison/3", GoodGenerator.MOD_ID + ":fuison/4",
            GoodGenerator.MOD_ID + ":fuison/5" });
    public static final Block antimatterContainmentCasing = new BlockFrame(
        "antimatterContainmentCasing",
        new String[] { GoodGenerator.MOD_ID + ":antimatterContainmentCasing" });
    public static final Block magneticFluxCasing = new BlockCasing(
        "magneticFluxCasing",
        new String[] { GoodGenerator.MOD_ID + ":magneticFluxCasing" });
    public static final Block gravityStabilizationCasing = new BlockCasing(
        "gravityStabilizationCasing",
        new String[] { GoodGenerator.MOD_ID + ":gravityStabilizationCasing" });
    public static final Block protomatterActivationCoil = new BlockCasing(
        "protomatterActivationCoil",
        new String[] { GoodGenerator.MOD_ID + ":protomatterActivationCoil" });
    public static final Block antimatterAnnihilationMatrix = new BlockCasing(
        "antimatterAnnihilationMatrix",
        new String[] { GoodGenerator.MOD_ID + ":antimatterAnnihilationMatrix" });
    public static final Block antimatterRenderBlock = new AntimatterRenderBlock();
    public static final Block essentiaFilterCasing = new BlockCasing(
        "essentiaFilterCasing",
        new String[] { GoodGenerator.MOD_ID + ":essentiaFilterCasing" });
    public static Block essentiaOutputHatch;
    public static Block essentiaOutputHatch_ME;
    public static final Block componentAssemblylineCasing = new BlockCasing(
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

    public static ItemStack[] NeutronAccelerators = new ItemStack[13];
    public static ItemStack[] Generator_Diesel = new ItemStack[2];

    public static ItemStack CompAssline;
    public static ItemStack AMForge;
    public static ItemStack AMGenerator;
    public static ItemStack AMHatch;
    // public static Item Isotope = new NuclearMetaItemGenerator();

    public static void GTMetaTileRegister() {
        Loaders.MAR = new MTEMultiNqGenerator(MetaTileEntityIDs.MultiNqGenerator.ID, "NaG", "Large Naquadah Reactor")
            .getStackForm(1L);
        Loaders.FRF = new MTEFuelRefineFactory(MetaTileEntityIDs.FuelRefineFactory.ID, "FRF", "Naquadah Fuel Refinery")
            .getStackForm(1L);
        Loaders.UCFE = new MTEUniversalChemicalFuelEngine(
            MetaTileEntityIDs.LegacyUniversalChemicalFuelEngine.ID,
            "UniversalChemicalFuelEngine",
            "Universal Chemical Fuel Engine").getStackForm(1L);
        int[] neutronAcceleratorIDs = new int[] { MetaTileEntityIDs.NeutronAcceleratorULV.ID,
            MetaTileEntityIDs.NeutronAcceleratorLV.ID, MetaTileEntityIDs.NeutronAcceleratorMV.ID,
            MetaTileEntityIDs.NeutronAcceleratorHV.ID, MetaTileEntityIDs.NeutronAcceleratorEV.ID,
            MetaTileEntityIDs.NeutronAcceleratorIV.ID, MetaTileEntityIDs.NeutronAcceleratorLuV.ID,
            MetaTileEntityIDs.NeutronAcceleratorZPM.ID, MetaTileEntityIDs.NeutronAcceleratorUV.ID,
            MetaTileEntityIDs.NeutronAcceleratorUHV.ID, MetaTileEntityIDs.NeutronAcceleratorUEV.ID,
            MetaTileEntityIDs.NeutronAcceleratorUIV.ID, MetaTileEntityIDs.NeutronAcceleratorUMV.ID, };
        for (int i = 0; i < 13; i++) {
            Loaders.NeutronAccelerators[i] = new MTENeutronAccelerator(
                neutronAcceleratorIDs[i],
                "Neutron Accelerator " + GTValues.VN[i],
                "Neutron Accelerator " + GTValues.VN[i],
                i).getStackForm(1L);
        }
        Loaders.NS = new MTENeutronSensor(MetaTileEntityIDs.NeutronSensor.ID, "Neutron Sensor", "Neutron Sensor", 5)
            .getStackForm(1L);
        Loaders.NA = new MTENeutronActivator(
            MetaTileEntityIDs.NeutronActivator.ID,
            "NeutronActivator",
            "Neutron Activator").getStackForm(1L);
        Loaders.YFT = new MTEYottaFluidTank(MetaTileEntityIDs.YottaFluidTank.ID, "YottaFluidTank", "YOTTank")
            .getStackForm(1L);
        Loaders.YFH = new MTEYOTTAHatch(MetaTileEntityIDs.YottaHatch.ID, "YottaFluidTankHatch", "YOTHatch", 5)
            .getStackForm(1L);
        Loaders.AMHatch = new AntimatterOutputHatch(
            MetaTileEntityIDs.AntimatterHatch.ID,
            "AntimatterHatch",
            "Antimatter Hatch").getStackForm(1L);
        Loaders.SCTurbine = new MTESupercriticalFluidTurbine(
            MetaTileEntityIDs.SupercriticalFluidTurbine.ID,
            "SupercriticalSteamTurbine",
            "Large Supercritical Steam Turbine").getStackForm(1L);
        Loaders.XHE = new MTEExtremeHeatExchanger(
            MetaTileEntityIDs.ExtremeHeatExchanger.ID,
            "ExtremeHeatExchanger",
            "Extreme Heat Exchanger").getStackForm(1L);
        Loaders.PA = new MTEPreciseAssembler(
            MetaTileEntityIDs.PreciseAssembler.ID,
            "PreciseAssembler",
            "Precise Auto-Assembler MT-3662").getStackForm(1L);
        Loaders.LFC[0] = new MTELargeFusionComputer1(
            MetaTileEntityIDs.LargeFusionComputer1.ID,
            "LargeFusionComputer1",
            "Compact Fusion Computer MK-I Prototype").getStackForm(1);
        Loaders.LFC[1] = new MTELargeFusionComputer2(
            MetaTileEntityIDs.LargeFusionComputer2.ID,
            "LargeFusionComputer2",
            "Compact Fusion Computer MK-II").getStackForm(1L);
        Loaders.LFC[2] = new MTELargeFusionComputer3(
            MetaTileEntityIDs.LargeFusionComputer3.ID,
            "LargeFusionComputer3",
            "Compact Fusion Computer MK-III").getStackForm(1L);
        Loaders.LFC[3] = new MTELargeFusionComputer4(
            MetaTileEntityIDs.LargeFusionComputer4.ID,
            "LargeFusionComputer4",
            "Compact Fusion Computer MK-IV Prototype").getStackForm(1L);
        Loaders.LFC[4] = new MTELargeFusionComputer5(
            MetaTileEntityIDs.LargeFusionComputer5.ID,
            "LargeFusionComputer5",
            "Compact Fusion Computer MK-V").getStackForm(1L);
        Loaders.Generator_Diesel[0] = new MTEDieselGenerator(
            MetaTileEntityIDs.DieselGeneratorEV.ID,
            "basicgenerator.diesel.tier.04",
            "Turbo Supercharging Combustion Generator",
            4,
            65).getStackForm(1L);
        Loaders.Generator_Diesel[1] = new MTEDieselGenerator(
            MetaTileEntityIDs.DieselGeneratorIV.ID,
            "basicgenerator.diesel.tier.05",
            "Ultimate Chemical Energy Releaser",
            5,
            50).getStackForm(1L);
        Loaders.CT = new MTECoolantTower(MetaTileEntityIDs.CoolantTower.ID, "CoolantTower", "Coolant Tower")
            .getStackForm(1L);
        Loaders.CompAssline = new MTEComponentAssemblyLine(
            MetaTileEntityIDs.ComponentAssemblyLine.ID,
            "ComponentAssemblyLine",
            "Component Assembly Line").getStackForm(1L);
        CrackRecipeAdder.registerPipe(MetaTileEntityIDs.PipeIncoloy903.ID, GGMaterial.incoloy903, 15000, 8000, true);
        CrackRecipeAdder.registerWire(MetaTileEntityIDs.WireSignalium.ID, GGMaterial.signalium, 12, 131072, 32, true);
        CrackRecipeAdder.registerWire(MetaTileEntityIDs.WireLumiium.ID, GGMaterial.lumiium, 8, 524288, 64, true);
        Loaders.AMForge = new AntimatterForge(
            MetaTileEntityIDs.AntimatterForge.ID,
            "AntimatterForge",
            "Semi-Stable Antimatter Stabilization Sequencer").getStackForm(1L);
        Loaders.AMGenerator = new AntimatterGenerator(
            MetaTileEntityIDs.AntimatterGenerator.ID,
            "AntimatterGenerator",
            "Shielded Lagrangian Annihilation Matrix").getStackForm(1L);
    }

    public static void Register() {

        GameRegistry.registerItem(_null_, "_null_", GoodGenerator.MOD_ID);
        NEIConfig.hide(_null_);

        GameRegistry.registerBlock(MAR_Casing, GGItemBlocks.class, "MAR_Casing");
        GameRegistry.registerBlock(radiationProtectionSteelFrame, GGItemBlocks.class, "radiationProtectionSteelFrame");
        GameRegistry.registerBlock(fieldRestrictingGlass, GGItemBlocks.class, "fieldRestrictingGlass");
        GameRegistry.registerBlock(FRF_Casings, GGItemBlocks.class, "FRF_Casings");
        GameRegistry.registerBlock(FRF_Coil_1, GGItemBlocks.class, "FRF_Coil_1");
        GameRegistry.registerBlock(FRF_Coil_2, GGItemBlocks.class, "FRF_Coil_2");
        GameRegistry.registerBlock(FRF_Coil_3, GGItemBlocks.class, "FRF_Coil_3");
        GameRegistry.registerBlock(FRF_Coil_4, GGItemBlocks.class, "FRF_Coil_4");
        GameRegistry.registerBlock(rawCylinder, GGItemBlocks.class, "rawCylinder");
        GameRegistry.registerBlock(titaniumPlatedCylinder, GGItemBlocks.class, "titaniumPlatedCylinder");
        GameRegistry.registerBlock(speedingPipe, GGItemBlocks.class, "speedingPipe");
        GameRegistry.registerBlock(yottaFluidTankCell, GGItemBlocks.class, "yottaFluidTankCells");
        GameRegistry.registerBlock(yottaFluidTankCasing, GGItemBlocks.class, "yottaFluidTankCasing");
        GameRegistry
            .registerBlock(supercriticalFluidTurbineCasing, GGItemBlocks.class, "supercriticalFluidTurbineCasing");
        GameRegistry.registerBlock(componentAssemblylineCasing, GGItemBlocks.class, "componentAssemblylineCasing");
        GameRegistry.registerBlock(pressureResistantWalls, GGItemBlocks.class, "pressureResistantWalls");
        GameRegistry.registerBlock(impreciseUnitCasing, GGItemBlocks.class, "impreciseUnitCasing");
        GameRegistry.registerBlock(preciseUnitCasing, GGItemBlocks.class, "preciseUnitCasing");
        GameRegistry.registerBlock(compactFusionCoil, GGItemBlocks.class, "compactFusionCoil");
        GameRegistry.registerBlock(antimatterContainmentCasing, GGItemBlocks.class, "antimatterContainmentCasing");
        GameRegistry.registerBlock(magneticFluxCasing, GGItemBlocks.class, "magneticFluxCasing");
        GameRegistry.registerBlock(protomatterActivationCoil, GGItemBlocks.class, "protomatterActivationCoil");
        GameRegistry.registerBlock(antimatterAnnihilationMatrix, GGItemBlocks.class, "antimatterAnnihilationMatrix");
        GameRegistry.registerBlock(gravityStabilizationCasing, GGItemBlocks.class, "gravityStabilizationCasing");
        GameRegistry.registerBlock(antimatterRenderBlock, "antimatterRenderBlock");
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
        GameRegistry
            .registerItem(advancedRadiationProtectionPlate, "advancedRadiationProtectionPlate", GoodGenerator.MOD_ID);
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
        GameRegistry.registerTileEntity(TileAntimatter.class, "AntimatterRender");

        GTStructureChannels.PRASS_UNIT_CASING.registerAsIndicator(new ItemStack(impreciseUnitCasing), 1);
        for (int i = 1; i < 6; i++) {
            GTStructureChannels.PRASS_UNIT_CASING
                .registerAsIndicator(new ItemStack(preciseUnitCasing, 1, i - 1), i + 1);
        }
        for (int i = 0; i < 14; i++) {
            GTStructureChannels.COMPONENT_ASSEMBLYLINE_CASING
                .registerAsIndicator(new ItemStack(componentAssemblylineCasing, 1, i), i + 1);
        }
    }

    public static void compactMod() {
        if (!Mods.Thaumcraft.isModLoaded()) return;
        GameRegistry.registerTileEntity(MTEEssentiaOutputHatch.class, "EssentiaOutputHatch");
        GameRegistry.registerTileEntity(MTEEssentiaOutputHatchME.class, "EssentiaOutputHatch_ME");
        Loaders.LES = new MTELargeEssentiaSmeltery(
            MetaTileEntityIDs.LargeEssentiaSmeltery.ID,
            "LargeEssentiaSmeltery",
            "Large Essentia Smeltery").getStackForm(1L);
        essentiaOutputHatch = new BlockTEContainer(
            "essentiaOutputHatch",
            new String[] { GoodGenerator.MOD_ID + ":essentiaOutputHatch" },
            2);
        essentiaOutputHatch_ME = new BlockTEContainer(
            "essentiaOutputHatch_ME",
            new String[] { GoodGenerator.MOD_ID + ":essentiaOutputHatch_ME" },
            3);
        GameRegistry.registerBlock(magicCasing, GGItemBlocks.class, "magicCasing");
        GameRegistry.registerBlock(essentiaCell, GGItemBlocks.class, "essentiaCell");
        GameRegistry.registerBlock(essentiaOutputHatch, GGItemBlocks.class, "essentiaOutputHatch");
        GameRegistry.registerBlock(essentiaFilterCasing, GGItemBlocks.class, "essentiaFilterCasing");
        GameRegistry.registerBlock(essentiaOutputHatch_ME, GGItemBlocks.class, "essentiaOutputHatch_ME");

        Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][0] = TextureFactory.of(magicCasing);
    }

    public static void addOreDic() {
        OreDictionary.registerOre("dustAluminumNitride", aluminumNitride);

        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.ULV, new ItemStack(circuitWrap, 1, 0));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.LV, new ItemStack(circuitWrap, 1, 1));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.MV, new ItemStack(circuitWrap, 1, 2));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.HV, new ItemStack(circuitWrap, 1, 3));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.EV, new ItemStack(circuitWrap, 1, 4));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.IV, new ItemStack(circuitWrap, 1, 5));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.LuV, new ItemStack(circuitWrap, 1, 6));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.ZPM, new ItemStack(circuitWrap, 1, 7));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UV, new ItemStack(circuitWrap, 1, 8));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UHV, new ItemStack(circuitWrap, 1, 9));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UEV, new ItemStack(circuitWrap, 1, 10));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UIV, new ItemStack(circuitWrap, 1, 11));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UMV, new ItemStack(circuitWrap, 1, 12));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.UXV, new ItemStack(circuitWrap, 1, 13));
        GTOreDictUnificator.registerOre(OrePrefixes.wrapCircuit, Materials.MAX, new ItemStack(circuitWrap, 1, 14));
    }

    public static void addTexturePage() {
        if (Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] == null) {
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] = new ITexture[128];
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][1] = TextureFactory
                .of(yottaFluidTankCasing);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][2] = TextureFactory
                .of(supercriticalFluidTurbineCasing);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][3] = TextureFactory
                .of(GregTechAPI.sBlockConcretes, 8);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][4] = TextureFactory
                .of(impreciseUnitCasing, 0);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][5] = TextureFactory
                .of(preciseUnitCasing, 0);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][6] = TextureFactory
                .of(preciseUnitCasing, 1);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][7] = TextureFactory
                .of(preciseUnitCasing, 2);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][8] = TextureFactory
                .of(preciseUnitCasing, 3);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][9] = TextureFactory
                .of(magneticFluxCasing, 0);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][10] = TextureFactory
                .of(gravityStabilizationCasing, 0);
            // index 126 taken by GTNH-Lanthanides
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][127] = TextureFactory
                .of(BlockTFFTStorageField.TFFTCasingIcon.INSTANCE);
        }
    }

    public static void preInitLoad() {
        Register();
        addOreDic();
        addTexturePage();
        compactMod();
        FluidsBuilder.Register();
    }

    public static void initLoad() {
        GTMetaTileRegister();
        initLoadRecipes();
        CropsLoader.registerCrops();
    }

    public static void postInitLoad() {
        postInitLoadRecipes();
    }

    public static void completeLoad() {
        RecipeLoader2.FinishLoadRecipe();
        MaterialFix.addRecipeForMultiItems();
        ComponentAssemblyLineLoader.run();
    }

    public static void initLoadRecipes() {
        RecipeLoader.InitLoadRecipe();
        RecipeLoader2.InitLoadRecipe();
        FuelRecipeLoader.RegisterFuel();
        NaquadahReworkRecipeLoader.RecipeLoad();
    }

    public static void postInitLoadRecipes() {
        RecipeLoader.RecipeLoad();
        RecipeLoader.Fixer();
        RecipeLoader2.RecipeLoad();
        NeutronActivatorLoader.NARecipeLoad();
    }
}
