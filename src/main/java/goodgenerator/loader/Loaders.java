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
import goodgenerator.blocks.tileEntity.MTEFuelRefineFactoryLegacy;
import goodgenerator.blocks.tileEntity.MTELargeEssentiaSmeltery;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer1;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer2;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer3;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer4;
import goodgenerator.blocks.tileEntity.MTELargeFusionComputer5;
import goodgenerator.blocks.tileEntity.MTEMultiNqGeneratorLegacy;
import goodgenerator.blocks.tileEntity.MTENeutronActivator;
import goodgenerator.blocks.tileEntity.MTEPreciseAssembler;
import goodgenerator.blocks.tileEntity.MTESupercriticalFluidTurbineLegacy;
import goodgenerator.blocks.tileEntity.MTEUniversalChemicalFuelEngineLegacy;
import goodgenerator.blocks.tileEntity.MTEYottaFluidTank;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import goodgenerator.crossmod.nei.NEIConfig;
import goodgenerator.items.GGItem;
import goodgenerator.items.GGItemBlocks;
import goodgenerator.items.RadioactiveItem;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.MaterialFix;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
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
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/1", Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/2",
            Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/3", Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/4",
            Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/5", Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/6",
            Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/7", Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/8",
            Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/9", Mods.ModIDs.GOOD_GENERATOR + ":fluidCore/10" });
    public static final Item highEnergyMixture = new GGItem(
        "highEnergyMixture",
        GoodGenerator.GG,
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":highEnergyMixture" });
    public static final Item huiCircuit = new GGItem(
        "huiCircuit",
        addText("huiCircuit.tooltip", 5),
        GoodGenerator.GG,
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":ciruits/1", Mods.ModIDs.GOOD_GENERATOR + ":ciruits/2",
            Mods.ModIDs.GOOD_GENERATOR + ":ciruits/3", Mods.ModIDs.GOOD_GENERATOR + ":ciruits/4",
            Mods.ModIDs.GOOD_GENERATOR + ":ciruits/5", });

    public static final Item circuitWrap = new GGItem(
        "circuitWrap",
        GoodGenerator.GG,
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":wraps/0", Mods.ModIDs.GOOD_GENERATOR + ":wraps/1",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/2", Mods.ModIDs.GOOD_GENERATOR + ":wraps/3",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/4", Mods.ModIDs.GOOD_GENERATOR + ":wraps/5",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/6", Mods.ModIDs.GOOD_GENERATOR + ":wraps/7",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/8", Mods.ModIDs.GOOD_GENERATOR + ":wraps/9",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/10", Mods.ModIDs.GOOD_GENERATOR + ":wraps/11",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/12", Mods.ModIDs.GOOD_GENERATOR + ":wraps/13",
            Mods.ModIDs.GOOD_GENERATOR + ":wraps/14" });

    public static final Block MAR_Casing = new BlockCasing(
        "MAR_Casing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":MAR_Casing" });
    public static final Block FRF_Casings = new BlockCasing(
        "FRF_Casing",
        new String[] { "gregtech:iconsets/MACHINE_CASING_MINING_BLACKPLUTONIUM" });
    public static final Block FRF_Coil_1 = new BlockCasing(
        "FRF_Coil_1",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":FRF_Coils/1" });
    public static final Block FRF_Coil_2 = new BlockCasing(
        "FRF_Coil_2",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":FRF_Coils/2" });
    public static final Block FRF_Coil_3 = new BlockCasing(
        "FRF_Coil_3",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":FRF_Coils/3" });
    public static final Block FRF_Coil_4 = new BlockCasing(
        "FRF_Coil_4",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":FRF_Coils/4" });
    public static final Block radiationProtectionSteelFrame = new BlockFrame(
        "radiationProtectionSteelFrame",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":radiationProtectionSteelFrame" });
    public static final Block fieldRestrictingGlass = new BlockFrame(
        "fieldRestrictingGlass",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":fieldRestrictingGlass" });
    public static final Block rawCylinder = new BlockCasing(
        "rawCylinder",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":rawCylinder" });
    public static final Block titaniumPlatedCylinder = new BlockCasing(
        "titaniumPlatedCylinder",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":titaniumPlatedCylinder" });
    public static final Block magicCasing = new BlockCasing(
        "magicCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":MagicCasing" });
    public static final Block essentiaCell = new BlockCasing(
        "essentiaCell",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":essentiaCell/1", Mods.ModIDs.GOOD_GENERATOR + ":essentiaCell/2",
            Mods.ModIDs.GOOD_GENERATOR + ":essentiaCell/3", Mods.ModIDs.GOOD_GENERATOR + ":essentiaCell/4" });
    public static final Block speedingPipe = new BlockComplexTextureCasing(
        "speedingPipe",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":speedingPipe_SIDE" },
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":speedingPipe_TOP" });
    public static final Block yottaFluidTankCell = new BlockCasing(
        "yottaFluidTankCell",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/1",
            Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/2", Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/3",
            Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/4", Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/5",
            Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/6", Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/7",
            Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/8", Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/9",
            Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCell/10", });
    public static final Block yottaFluidTankCasing = new BlockComplexTextureCasing(
        "yottaFluidTankCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCasing_SIDE" },
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":yottaFluidTankCasing_TOP" });
    public static final Block supercriticalFluidTurbineCasing = new BlockTurbineCasing(
        "supercriticalFluidTurbineCasing",
        "supercriticalFluidTurbineCasing");
    public static final Block pressureResistantWalls = new BlockCasing(
        "pressureResistantWalls",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":pressureResistantWalls" });
    public static final Block impreciseUnitCasing = new BlockCasing(
        "impreciseUnitCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":preciseUnitCasing/0" });
    public static final Block preciseUnitCasing = new BlockCasing(
        "preciseUnitCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":preciseUnitCasing/1",
            Mods.ModIDs.GOOD_GENERATOR + ":preciseUnitCasing/2", Mods.ModIDs.GOOD_GENERATOR + ":preciseUnitCasing/3",
            Mods.ModIDs.GOOD_GENERATOR + ":preciseUnitCasing/4" });
    public static final Block compactFusionCoil = new BlockCasing(
        "compactFusionCoil",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":fuison/1", Mods.ModIDs.GOOD_GENERATOR + ":fuison/2",
            Mods.ModIDs.GOOD_GENERATOR + ":fuison/3", Mods.ModIDs.GOOD_GENERATOR + ":fuison/4",
            Mods.ModIDs.GOOD_GENERATOR + ":fuison/5" });
    public static final Block antimatterContainmentCasing = new BlockFrame(
        "antimatterContainmentCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":antimatterContainmentCasing" });
    public static final Block magneticFluxCasing = new BlockCasing(
        "magneticFluxCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":magneticFluxCasing" });
    public static final Block gravityStabilizationCasing = new BlockCasing(
        "gravityStabilizationCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":gravityStabilizationCasing" });
    public static final Block protomatterActivationCoil = new BlockCasing(
        "protomatterActivationCoil",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":protomatterActivationCoil" });
    public static final Block antimatterAnnihilationMatrix = new BlockCasing(
        "antimatterAnnihilationMatrix",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":antimatterAnnihilationMatrix" });
    public static final Block antimatterRenderBlock = new AntimatterRenderBlock();
    public static final Block essentiaFilterCasing = new BlockCasing(
        "essentiaFilterCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":essentiaFilterCasing" });
    public static Block essentiaOutputHatch;
    public static Block essentiaOutputHatch_ME;
    public static final Block componentAssemblylineCasing = new BlockCasing(
        "componentAssemblyLineCasing",
        new String[] { Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/0", // LV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/1", // MV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/2", // HV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/3", // EV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/4", // IV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/5", // LuV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/6", // ZPM
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/7", // UV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/8", // UHV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/9", // UEV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/10", // UIV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/11", // UMV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/12", // UXV
            Mods.ModIDs.GOOD_GENERATOR + ":compAsslineCasing/13" // MAX
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
        Loaders.MAR = new MTEMultiNqGeneratorLegacy(
            MetaTileEntityIDs.MultiNqGenerator.ID,
            "NaG",
            "Large Naquadah Reactor").getStackForm(1L);
        Loaders.FRF = new MTEFuelRefineFactoryLegacy(
            MetaTileEntityIDs.FuelRefineFactory.ID,
            "FRF",
            "Naquadah Fuel Refinery").getStackForm(1L);
        Loaders.UCFE = new MTEUniversalChemicalFuelEngineLegacy(
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
        Loaders.SCTurbine = new MTESupercriticalFluidTurbineLegacy(
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
        CrackRecipeAdder.registerPipe(Materials2Materials.Incoloy903);
        CrackRecipeAdder.registerWire(Materials2Materials.Signalium);
        CrackRecipeAdder.registerWire(Materials2Materials.Lumiium);
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

        GameRegistry.registerItem(_null_, "_null_", Mods.ModIDs.GOOD_GENERATOR);
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
        GameRegistry.registerItem(radiationProtectionPlate, "radiationProtectionPlate", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(wrappedUraniumIngot, "wrappedUraniumIngot", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityUraniumNugget, "highDensityUraniumNugget", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityUranium, "highDensityUranium", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(wrappedThoriumIngot, "wrappedThoriumIngot", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityThoriumNugget, "highDensityThoriumNugget", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityThorium, "highDensityThorium", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(wrappedPlutoniumIngot, "wrappedPlutoniumIngot", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityPlutoniumNugget, "highDensityPlutoniumNugget", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highDensityPlutonium, "highDensityPlutonium", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry
            .registerItem(rawAtomicSeparationCatalyst, "rawAtomicSeparationCatalyst", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(
            advancedRadiationProtectionPlate,
            "advancedRadiationProtectionPlate",
            Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(aluminumNitride, "aluminumNitride", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(specialCeramics, "specialCeramics", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(specialCeramicsPlate, "specialCeramicsPlate", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(radioactiveWaste, "radioactiveWaste", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(plasticCase, "plasticCase", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(quartzWafer, "quartzWafer", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(microHeater, "microHeater", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(quartzCrystalResonator, "quartzCrystalResonator", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(inverter, "inverter", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(neutronSource, "neutronSource", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(naquadahMass, "naquadahMass", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(enrichedNaquadahMass, "enrichedNaquadahMass", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(naquadriaMass, "naquadriaMass", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(advancedFuelRod, "advancedFuelRod", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(fluidCore, "fluidCore", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(highEnergyMixture, "highEnergyMixture", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(huiCircuit, "huiCircuit", Mods.ModIDs.GOOD_GENERATOR);
        GameRegistry.registerItem(circuitWrap, "circuitWrap", Mods.ModIDs.GOOD_GENERATOR);
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
            new String[] { Mods.ModIDs.GOOD_GENERATOR + ":essentiaOutputHatch" },
            2);
        essentiaOutputHatch_ME = new BlockTEContainer(
            "essentiaOutputHatch_ME",
            new String[] { Mods.ModIDs.GOOD_GENERATOR + ":essentiaOutputHatch_ME" },
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

        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.ULV.materialName()),
            new ItemStack(circuitWrap, 1, 0));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.LV.materialName()),
            new ItemStack(circuitWrap, 1, 1));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.MV.materialName()),
            new ItemStack(circuitWrap, 1, 2));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.HV.materialName()),
            new ItemStack(circuitWrap, 1, 3));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.EV.materialName()),
            new ItemStack(circuitWrap, 1, 4));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.IV.materialName()),
            new ItemStack(circuitWrap, 1, 5));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.LuV.materialName()),
            new ItemStack(circuitWrap, 1, 6));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.ZPM.materialName()),
            new ItemStack(circuitWrap, 1, 7));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UV.materialName()),
            new ItemStack(circuitWrap, 1, 8));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UHV.materialName()),
            new ItemStack(circuitWrap, 1, 9));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UEV.materialName()),
            new ItemStack(circuitWrap, 1, 10));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UIV.materialName()),
            new ItemStack(circuitWrap, 1, 11));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UMV.materialName()),
            new ItemStack(circuitWrap, 1, 12));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.UXV.materialName()),
            new ItemStack(circuitWrap, 1, 13));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wrapCircuit.oreDictName(Circuits.MAX.materialName()),
            new ItemStack(circuitWrap, 1, 14));
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
        RecipeLoader.InitLoadRecipe();
        RecipeLoader2.InitLoadRecipe();
        FuelRecipeLoader.RegisterFuel();
        NaquadahRecipeLoader.RecipeLoad();
    }

    public static void postInitLoad() {
        RecipeLoader.RecipeLoad();
        RecipeLoader.Fixer();
        RecipeLoader2.RecipeLoad();
        NeutronActivatorLoader.NARecipeLoad();
        MaterialFix.addRecipeForMultiItems();
        ComponentAssemblyLineLoader.run();
    }

    public static void completeLoad() {
        RecipeLoader2.FinishLoadRecipe();
    }
}
